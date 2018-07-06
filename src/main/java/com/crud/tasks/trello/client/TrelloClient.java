package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TrelloClient {
    @Value("${trello.api.endpoint.prod}")
    private String trelloApiEndpoint;

    @Value("${trello.app.key}")
    private String trelloAppKey;

    @Value("${trello.app.token}")
    private String trelloToken;

    @Value("${trello.app.username}")
    private String trelloUsername;

    @Autowired
    private RestTemplate restTemplate;

    public List<TrelloBoardDto> getTrelloBoards() {
        URI url = buildTrelloGetBoardsUrl();

        System.out.println(url);
        TrelloBoardDto[] boardsResponse = Optional.ofNullable(restTemplate.getForObject(url, TrelloBoardDto[].class)).orElse(new TrelloBoardDto[0]);

        return Arrays.asList(boardsResponse);
    }

    public CreatedTrelloCard createNewCard(TrelloCardDto trelloCardDto) {
        return  Optional.ofNullable(restTemplate.postForObject(buildTrelloCreateNewCardUrl(trelloCardDto),
                                                        null,
                                                                CreatedTrelloCard.class))
                                    .orElse(new CreatedTrelloCard());
    }

    private URI buildTrelloGetBoardsUrl() {
        return UriComponentsBuilder.fromHttpUrl(trelloApiEndpoint + "/members/" + this.trelloUsername + "/boards")
                .queryParam("key", trelloAppKey)
                .queryParam("token", trelloToken)
                .queryParam("fields", "name,id")
                .queryParam("lists", "all")
                .build().encode().toUri();
    }

    private URI buildTrelloCreateNewCardUrl(final TrelloCardDto trelloCardDto)
    {
        return UriComponentsBuilder.fromHttpUrl(trelloApiEndpoint +"/cards/")
                .queryParam("key", trelloAppKey)
                .queryParam("token", trelloToken)
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .build().encode().toUri();
    }
}
