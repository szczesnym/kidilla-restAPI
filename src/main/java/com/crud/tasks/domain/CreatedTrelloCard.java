package com.crud.tasks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatedTrelloCard {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatedTrelloCard.class);
    private String id, name, shortUrl;
    private int board, votes, card;


    public CreatedTrelloCard(String id, String name, String shortUrl) {
        this.id = id;
        this.name = name;
        this.shortUrl = shortUrl;
    }

    public CreatedTrelloCard(String jsonResponse) {
        super();
        LOGGER.info("Input JSON:{}", jsonResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            this.id = jsonNode.get("id").asText();
            this.name = jsonNode.get("name").asText();
            this.shortUrl = jsonNode.get("shortUrl").asText();
            this.board = jsonNode.get("badges").get("attachmentsByType").get("trello").get("board").asInt();
            this.card = jsonNode.get("badges").get("attachmentsByType").get("trello").get("card").asInt();
            this.votes = jsonNode.get("badges").get("votes").asInt();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
