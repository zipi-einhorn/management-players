package com.test.management.players.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

  @Value("${bezkoder.openapi.dev-url}")
  private String devUrl;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");
    Contact contact = new Contact();
    contact.setEmail("einhorn.zipi@gmail.com");
    contact.setName("Tzipporah");
    Info info = new Info()
        .title("Players details API")
        .contact(contact)
        .description("This APIs exposes endpoints to get details about a players")
            .termsOfService("https://www.bezkoder.com/terms");
    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}