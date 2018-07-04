package com.crud.tasks.controller;

import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/trello")
public class TrelloController {

    @Autowired
    private TrelloClient trelloClient;

    @RequestMapping(method = RequestMethod.GET, value = "getTrelloBoards")
    public void getTrelloBoards() {

        //List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards();
        Optional<List<TrelloBoardDto>> trelloBoards = Optional.ofNullable(trelloClient.getTrelloBoards());
        if (trelloBoards.isPresent()) {
            trelloBoards.get().stream()
                    .filter(trelloBoard -> !(trelloBoard.getId().equals(null) && trelloBoard.getName().equals(null)) && trelloBoard.getName().contains("Kodilla"))
                    .forEach(System.out::println);
        }
        //Original bootCamp Entry: trelloBoards.forEach(trelloBoardDto -> System.out.println(trelloBoardDto.getId() + " " + trelloBoardDto.getName()));
    }
}