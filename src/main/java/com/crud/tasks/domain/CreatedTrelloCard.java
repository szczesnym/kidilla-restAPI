package com.crud.tasks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatedTrelloCard {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("shortUrl")
    private String shortUrl;

    private TrelloAttachmentByType trelloAttachmentsByType;
    private int votes = 0;

    @JsonProperty("badges")
    private void unpackNestedBadges(Map<String, Object> badges) {
        this.votes = (int)badges.get("votes");

        Map<String, Object>  attachmentsByType = (Map<String,Object>)badges.get("attachmentsByType");
        System.out.println(attachmentsByType);
        Map<String, Object> trello =  (Map<String,Object>)attachmentsByType.get("trello");
        this.trelloAttachmentsByType = new TrelloAttachmentByType((int)trello.get("board"), (int)trello.get("card"));
    }

}
