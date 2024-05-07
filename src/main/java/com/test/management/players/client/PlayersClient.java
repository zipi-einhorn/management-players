package com.test.management.players.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class PlayersClient {
    String finalSendIds;

    @Value("${api-key}")
    private String apiKey;
    @Value("${url-players}")
    private String urlPlayers;

    public List<Map> players(List<Integer> ids) throws URISyntaxException, IOException, InterruptedException {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        HttpRequest getPlayers = HttpRequest.newBuilder().uri(getListPlayers(ids)).header("Authorization", apiKey).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<InputStream> response = httpClient.send(getPlayers, HttpResponse.BodyHandlers.ofInputStream());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        Map data = objectMapper.readValue(response.body(), Map.class);
        return data != null && data.containsKey("data") ? (List<Map>) data.get("data") : null;
    }

    private String playersIds(List<Integer> ids) {
        this.finalSendIds = null;
        ids.stream().map((x) -> this.finalSendIds = finalSendIds + "player_ids[]=" + x.toString() + "&").toArray();
        if (this.finalSendIds == null) {
            return "";
        }
        return finalSendIds.substring(0, finalSendIds.length() - 2);
    }

    private URI getListPlayers(List<Integer> players) throws URISyntaxException {
        return new URI(urlPlayers + "/v1/players?" + playersIds(players));
    }

    public PlayersClient() {
        this.finalSendIds = "";
    }
}