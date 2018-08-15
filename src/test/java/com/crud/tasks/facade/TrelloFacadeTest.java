package com.crud.tasks.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class TrelloFacadeTest {
    @InjectMocks
    private TrelloFacade trelloFacade;

    @Mock
    private TrelloService trelloService;

    @Mock
    private TrelloValidator trelloValidator;

    @Mock
    private TrelloMapper trelloMapper;

    private List<TrelloListDto> trelloLists;
    private List<TrelloBoardDto> trelloBoards;
    private List<TrelloList> mappedTrelloLists;
    private List<TrelloBoard> mappedTrelloBoards;

    @Before
    public void init() {
        trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloListDto("1", "test_list", false));

        trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoardDto("1", "test", trelloLists));

        mappedTrelloLists = new ArrayList<>();
        mappedTrelloLists.add((new TrelloList("1", "test_list", false)));

        mappedTrelloBoards = new ArrayList<>();
        mappedTrelloBoards.add(new TrelloBoard("1", "test", mappedTrelloLists));
    }

    @Test
    public void shouldFetchEmptyList() {

        //Given
        Mockito.when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        Mockito.when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedTrelloBoards);
        Mockito.when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(new ArrayList<>());

        //When
        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(trelloBoardDtos);
        assertEquals(0, trelloBoardDtos.size());
    }

    @Test
    public void shouldFetchTrelloBoards() {
        //Given
        Mockito.when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        Mockito.when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedTrelloBoards);
        Mockito.when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoards);
        Mockito.when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);

        //When
        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(trelloBoardDtos);
        assertEquals(1, trelloBoardDtos.size());

        trelloBoardDtos.forEach(trelloBoardDto ->
                {
                    assertEquals("test", trelloBoardDto.getId());
                    assertEquals("1", trelloBoardDto.getName());

                    trelloBoardDto.getLists().forEach(trelloListDto ->
                            {
                                    assertEquals("1", trelloListDto.getId());
                                    assertEquals("test_list", trelloListDto.getName());
                                    assertFalse( trelloBoardDto.isClosed());
                            }
                    );
                }
        );
    }

    @Test
    public void shouldCreteTrelloCard() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test", "Test card", "LEFT", "1");
        TrelloCard trelloCard = new TrelloCard("Test", "Test Card", "LEFT", "1");
        Mockito.when(trelloMapper.mapToCard(trelloCardDto)).thenReturn(trelloCard);
        Mockito.when(trelloMapper.mapToCardDto(trelloCard)).thenReturn(trelloCardDto);
        Mockito.when(trelloService.createTrelloCard(trelloCardDto)).thenReturn(new CreatedTrelloCardDto("1", "Test Card", "http://test.com/index.htm"));
        //When
        CreatedTrelloCardDto sutCreatedTrelloCard = trelloFacade.createCard(trelloCardDto);
        //Then
        assertEquals("1", sutCreatedTrelloCard.getId());
        assertEquals("Test Card" , sutCreatedTrelloCard.getName());
        assertEquals("http://test.com/index.htm", sutCreatedTrelloCard.getShortUrl());
    }
}
