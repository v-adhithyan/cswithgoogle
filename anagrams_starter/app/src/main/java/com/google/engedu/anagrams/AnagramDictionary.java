/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private ArrayList<String> wordsList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String,
        ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordsList.add(word);
            wordSet.add(word);

            String key = sortLetters(word);
            if(lettersToWord.containsKey(key)) {
              ArrayList<String> list = lettersToWord.get(key);
              list.add(word);

              lettersToWord.put(key, list);
            } else {
              ArrayList<String> list = new ArrayList<String>();
              list.add(word);

              lettersToWord.put(key, list);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
      return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        targetWord = sortLetters(targetWord);

        for(String word: wordsList) {
          if(sortLetters(word).equals(targetWord)) {
            result.add(word);
          }
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for(char alphabet: alphabets) {
          String key = sortLetters(word + alphabet);
          if(lettersToWord.containsKey(key)) {
            ArrayList<String> list = lettersToWord.get(key);
            for(String anagram: list) {
              result.add(anagram);
            }
          }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int n = wordsList.size();

        String starterWord = "stage";
        while(true) {
          Random r = new Random();
          int random = r.nextInt(n);

          if(wordsList.get(random).length() == wordLength) {
            starterWord = wordsList.get(random);
            break;
          }
        }

        wordLength = (wordLength < MAX_WORD_LENGTH) ? (wordLength + 1) : DEFAULT_WORD_LENGTH;
        return starterWord;
    }

    private String sortLetters(String word) {
      char[] targetArray = word.toCharArray();
      Arrays.sort(targetArray);

      return new String(targetArray);
    }
}
