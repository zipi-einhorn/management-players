package com.test.management.players.controller;

import com.test.management.players.service.CSVHelper;
import com.test.management.players.service.PlayersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayersController {
  @Autowired
  PlayersService playersService;

  //I use in post because in the swagger when I use upload file I need post.
  @Operation(summary = "Get the CSV of list players and return csv with more details",
          description = "Returns a details players")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully return details players"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "", consumes = {"multipart/form-data"})
  public ResponseEntity<Resource> getPlayersDetails(@RequestParam MultipartFile importFile)
          throws IOException, URISyntaxException, InterruptedException {
    List<PlayerCSV> players = CSVHelper.csvToTutorials(importFile.getInputStream());
    InputStreamResource file = playersService.getPlayersDetails(players.stream().map(x -> x.id).toList());
    String filename = "tutorials.csv";
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/csv")).body(file);
  }

  @Operation(summary = "Send Player th the cash",
          description = "Every 15 the function check if the player in the cash if not put to the cash")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/send-user-to-the-cash")
  public void sendUserTOTHeCashEvery15Minute(@RequestParam Integer player) {
    playersService.pullsThePlayerDetailsEvery15minutes(player);
  }

  @Operation(summary = "Check if user on the cash",
          description = "Check if user on the cash")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully")
  })
  @GetMapping(value = "check-if-user-in-the-cash")
  public Boolean createTutorial(@RequestParam Integer player) {
    return playersService.isUserInTheCash(player);
  }
}