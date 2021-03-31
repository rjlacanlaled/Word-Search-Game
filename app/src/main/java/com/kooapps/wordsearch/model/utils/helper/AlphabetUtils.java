package com.kooapps.wordsearch.model.utils.helper;

import java.util.Random;

public class AlphabetUtils {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    // GENERATE A RANDOM LOWERCASE LETTER
    public static char generateRandomLowerCaseLetter() {
        return ALPHABET[new Random().nextInt(ALPHABET.length)];
    }
}
