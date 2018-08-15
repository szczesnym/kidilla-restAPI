package com.crud.tasks.trello.validator;

import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.domain.TrelloList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrelloValidatorTest {
    private TrelloValidator trelloValidator = new TrelloValidator();

    @Test
    public void validateTestOnlyTrelloBoards() {
        //Given
        TrelloList trelloList = new TrelloList("List:1", "Test list 1", false);
        TrelloBoard testTrelloBoard1 = new TrelloBoard("1", "test", new ArrayList<>(Arrays.asList(trelloList)));
        TrelloBoard testTrelloBoard2 = new TrelloBoard("2", "test", new ArrayList<>(Arrays.asList(trelloList)));
        List<TrelloBoard> listOfTrelloBoadrsToValidate = new ArrayList<>();
        listOfTrelloBoadrsToValidate.add(testTrelloBoard1);
        listOfTrelloBoadrsToValidate.add(testTrelloBoard2);

        //When
        List<TrelloBoard> validatedListOfTrelloBoards = trelloValidator.validateTrelloBoards(listOfTrelloBoadrsToValidate);

        //Then
        assertTrue(validatedListOfTrelloBoards.isEmpty());
    }

    @Test
    public void validateMixedTrelloBoards() {
        //Given
        TrelloList trelloList = new TrelloList("List:1", "Test list 1", false);
        TrelloBoard testTrelloBoard1 = new TrelloBoard("1", "1st board", new ArrayList<>(Arrays.asList(trelloList)));
        TrelloBoard testTrelloBoard2 = new TrelloBoard("2", "2nd board", new ArrayList<>(Arrays.asList(trelloList)));
        TrelloBoard testTrelloBoard3 = new TrelloBoard("3", "test", new ArrayList<>(Arrays.asList(trelloList)));
        List<TrelloBoard> listOfTrelloBoadrsToValidate = new ArrayList<>();
        listOfTrelloBoadrsToValidate.add(testTrelloBoard1);
        listOfTrelloBoadrsToValidate.add(testTrelloBoard2);
        listOfTrelloBoadrsToValidate.add(testTrelloBoard3);

        //When
        List<TrelloBoard> validatedListOfTrelloBoards = trelloValidator.validateTrelloBoards(listOfTrelloBoadrsToValidate);

        //Then
        assertEquals(2, validatedListOfTrelloBoards.size());
    }


}