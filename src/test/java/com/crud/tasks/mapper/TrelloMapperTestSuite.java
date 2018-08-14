package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import com.crud.tasks.facade.TrelloMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class TrelloMapperTestSuite {
    //@Autowired
    private TrelloMapper trelloMapper;
    private TrelloCardDto trelloCardDto;
    private TrelloCard trelloCard;
    private TrelloBoardDto trelloBoardDto;
    private TrelloBoard trelloBoard;
    private TrelloList trelloList1, trelloList2;
    private TrelloListDto trelloListDto1, trelloListDto2;
    private List<TrelloList> listOfTrelloLists;
    private List<TrelloListDto> listOfTrelloDtoLists;

    @Before
    public void init() {
        trelloMapper = new TrelloMapper(); //Dlaczego nie działa Autowired ?
        trelloCard = new TrelloCard("Test Card", "Desc test Card", "TOP", "ListID");
        trelloCardDto = new TrelloCardDto("Test CardDto", "Desc test Card DTO", "LEFT", "ListID");
        trelloList1 = new TrelloList("List1", "List 1", true);
        trelloList2 = new TrelloList("List2", "List 2", false);
        trelloListDto1 = new TrelloListDto("ListDto1", "List Dto 1", false);
        trelloListDto2 = new TrelloListDto("ListDto2", "List Dto 2", true);
        trelloBoard = new TrelloBoard("Board1", "Board 1", new ArrayList<>(Arrays.asList(trelloList1)));
        trelloBoardDto = new TrelloBoardDto("BoardDto1", "Board Dto 1", new ArrayList<>(Arrays.asList(trelloListDto1)));
        listOfTrelloLists = new ArrayList<>(Arrays.asList(trelloList1, trelloList2));
        listOfTrelloDtoLists = new ArrayList<>(Arrays.asList(trelloListDto1, trelloListDto2));
    }


    @Test
    public void testMappingBoardsDtoToBoards() {
        //When
        List<TrelloBoardDto> listTrelloBoardDto = trelloMapper.mapToBoardsDto(new ArrayList<>(Arrays.asList(trelloBoard)));
        TrelloBoardDto sutTrelloBoardDto = listTrelloBoardDto.get(0);
        //When
        Assert.assertEquals(1, listTrelloBoardDto.size());
        Assert.assertEquals("Board 1", sutTrelloBoardDto.getId());
        Assert.assertEquals("Board1", sutTrelloBoardDto.getName());
        Assert.assertTrue(sutTrelloBoardDto.getLists().get(0).equals(trelloListDto1));
    }

    @Test
    public void testMappingBoardsToBoardsDto() {
        //When
        List<TrelloBoard> listTrelloBoard = trelloMapper.mapToBoards(new ArrayList<>(Arrays.asList(trelloBoardDto)));
        TrelloBoard sutTrelloBoard = listTrelloBoard.get(0);
        //When
        Assert.assertEquals(1, listTrelloBoard.size());
        Assert.assertEquals("Board Dto 1", sutTrelloBoard.getId());
        Assert.assertEquals("BoardDto1", sutTrelloBoard.getName());
        Assert.assertTrue(sutTrelloBoard.getLists().get(0).equals(trelloList1));


    }

    @Test
    public void testMappingListDtoToList() {
        //When
        List<TrelloList> sutListTrelloList = trelloMapper.mapToList(listOfTrelloDtoLists);
        //Then
        Assert.assertEquals(2, sutListTrelloList.size());
        Assert.assertTrue(sutListTrelloList.get(0).equals(new TrelloList("ListDto1", "List Dto 1", false)));
        Assert.assertTrue(sutListTrelloList.get(1).equals(new TrelloList("ListDto2", "List Dto 2", true)));


    }

    @Test
    public void testMappingListToListDto() {
        //When
        List<TrelloListDto> sutListTrelloListDto = trelloMapper.mapToListDto(listOfTrelloLists);
        //Then
        Assert.assertEquals(2, sutListTrelloListDto.size());
        Assert.assertTrue(sutListTrelloListDto.get(0).equals(new TrelloListDto("List1", "List 1", true)));
        Assert.assertTrue(sutListTrelloListDto.get(1).equals(new TrelloListDto("List2", "List 2", false)));
    }

    @Test
    public void testMappingCardDtoToCard() {
        //When
        TrelloCard responseCard = new TrelloCard("Test CardDto", "Desc test Card DTO", "LEFT", "ListID");
        TrelloCard sutTrelloCard = trelloMapper.mapToCard(trelloCardDto);

        //Then
/*        Assert.assertEquals("Test CardDto", sutTrelloCard.getName());
        Assert.assertEquals("Desc test Card DTO", sutTrelloCard.getDescription());
        Assert.assertEquals("LEFT", sutTrelloCard.getPos());
        Assert.assertEquals("ListID", sutTrelloCard.getListId());
Do omówienia który rodzaj testów jest lepszy
*/
        Assert.assertTrue(sutTrelloCard.equals(responseCard));

    }

    @Test
    public void testMappingCardToCardDto() {
        //When
        TrelloCardDto responseCardDto = new TrelloCardDto("Test Card", "Desc test Card", "TOP", "ListID");
        ;
        TrelloCardDto sutTrelloCardDto = trelloMapper.mapToCardDto(trelloCard);
        //Then
        Assert.assertTrue(sutTrelloCardDto.equals(responseCardDto));
    }


}
