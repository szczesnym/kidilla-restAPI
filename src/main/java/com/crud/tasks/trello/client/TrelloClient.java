package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCardDto;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.config.TrelloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TrelloClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TrelloConfig trelloConfig;

    public List<TrelloBoardDto> getTrelloBoards() {
        URI url = buildTrelloGetBoardsUrl();

        try {
            TrelloBoardDto[] boardsResponse = Optional.ofNullable(restTemplate.getForObject(url, TrelloBoardDto[].class)).orElse(new TrelloBoardDto[0]);
            return Arrays.asList(boardsResponse);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }

    }

    public CreatedTrelloCardDto createNewCard(TrelloCardDto trelloCardDto) {
        try {
            String stringCreatedTrelloCard = restTemplate.postForObject(buildTrelloCreateNewCardUrl(trelloCardDto), null, String.class);
            LOGGER.info(stringCreatedTrelloCard);
            return new CreatedTrelloCardDto(stringCreatedTrelloCard);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new CreatedTrelloCardDto();
        }
    }


    private URI buildTrelloGetBoardsUrl() {
        return UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + "/members/" + trelloConfig.getTrelloUsername() + "/boards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("fields", "name,id")
                .queryParam("lists", "all")
                .build().encode().toUri();
    }

    private URI buildTrelloCreateNewCardUrl(final TrelloCardDto trelloCardDto) {
        return UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + "/cards/")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .build().encode().toUri();
    }
}
