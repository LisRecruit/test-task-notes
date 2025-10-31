package com.example.testtask.note;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class TextStatisticsUtil {
    private static final Pattern WORD = Pattern.compile("\\p{L}+[\\p{L}\\p{N}'\\-_]*");

    public static Map<String, Integer> wordFrequencies(String text) {
        if (text == null || text.isBlank()) return Collections.emptyMap();
        Map<String, Integer> counts = WORD.matcher(text).results()
                .map(mr -> mr.group().toLowerCase())
                .collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a,b)->a, LinkedHashMap::new));
    }
}
