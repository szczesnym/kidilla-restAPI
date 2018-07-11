package com.crud.tasks.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)

public class TrelloBoardDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("closed")
    private boolean isClosed;

    @JsonProperty("lists")
    private List<TrelloListDto> lists;

    @Override
    public String toString() {
        return "TrelloBoardDto{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
