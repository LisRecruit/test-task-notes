package com.example.testtask.note;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextStatisticsUtilTest {
    @Test
    void shouldHandleEmptyAndNullText() {
        assertTrue(TextStatisticsUtil.wordFrequencies(null).isEmpty());

        assertTrue(TextStatisticsUtil.wordFrequencies("").isEmpty());

        assertTrue(TextStatisticsUtil.wordFrequencies("   ").isEmpty());
    }

    @Test
    void shouldCountAndSortWordsAccordingToSpecification() {
        String text = "note is just a NOTE. Note, note is great just Great!";

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("note", 4);
        expected.put("great", 2);
        expected.put("is", 2);
        expected.put("just", 2);
        expected.put("a", 1);


        Map<String, Integer> actual = TextStatisticsUtil.wordFrequencies(text);

        assertEquals(5, actual.size());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void shouldRemovePunctuationAndNumbersInWords() {
        String text = "This is a note with numbers 123-456 and punctuation!!! and dash-es. Great's";

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("and", 2);
        expected.put("a", 1);
        expected.put("dash-es", 1);
        expected.put("great's", 1);
        expected.put("is", 1);
        expected.put("note", 1);
        expected.put("numbers", 1);
        expected.put("punctuation", 1);
        expected.put("this", 1);
        expected.put("with", 1);

        Map<String, Integer> actual = TextStatisticsUtil.wordFrequencies(text);

        assertEquals(expected.keySet().toString(), actual.keySet().toString());
        assertEquals(10, actual.size());
    }

    @Test
    void shouldHandleSpecialCharactersAsDelimiters() {
        String text = "First_word-second_word.Third:word";

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("first_word-second_word", 1);
        expected.put("third", 1);
        expected.put("word", 1);

        Map<String, Integer> actual = TextStatisticsUtil.wordFrequencies(text);

        assertEquals(3, actual.size());
        assertEquals(1, actual.get("first_word-second_word"));
    }

}