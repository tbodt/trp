/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public final class WordSequence implements Iterable<WordSequence.Word> {
    private final List<Word> words;
    private static final Word[] EMPTY_WORD_ARRAY = new Word[0];

    public WordSequence(String data) {
        words = Pattern.compile("\\s+").splitAsStream(data).map(Word::new).collect(Collectors.toList());
    }

    public WordSequence(Word datum) {
        words = Collections.singletonList(datum);
    }

    public WordSequence(List<Word> data) {
        words = new ArrayList<>(data);
    }

    public WordSequence(Word[] words) {
        this.words = Arrays.asList(words);
    }

    public WordSequence(Stream<Word> data) {
        words = data.collect(Collectors.toList());
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }

    public static class Word implements CharSequence {
        private final String word;

        public Word(String word) {
            if (word.matches(".*\\s.*"))
                throw new IllegalArgumentException("words do not contain whitespace");
            this.word = word;
        }

        @Override
        public char charAt(int index) {
            return word.charAt(index);
        }

        @Override
        public CharSequence subSequence(int beginIndex, int endIndex) {
            return word.subSequence(beginIndex, endIndex);
        }

        @Override
        public int length() {
            return word.length();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Word))
                return false;
            return word.equalsIgnoreCase(((Word) other).word);
        }

        @Override
        public int hashCode() {
            return word.hashCode();
        }

        @Override
        public String toString() {
            return word;
        }

    }

    public WordSequence append(Word word) {
        List<Word> wordsList = new ArrayList<>(getWords());
        wordsList.add(word);
        return new WordSequence(wordsList);
    }

    public WordSequence combine() {
        return new WordSequence(String.join("", words));
    }

    @Override
    public Iterator<Word> iterator() {
        return Collections.unmodifiableList(words).iterator();
    }

    @Override
    public int hashCode() {
        return words.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WordSequence && ((WordSequence) obj).words.equals(words);
    }

    @Override
    public String toString() {
        return String.join(" ", words);
    }

    public static java.util.function.Function<WordSequence, Stream<WordSequence>> forEachWord(java.util.function.Function<Word, Stream<Word>> function) {
        return ws -> {
            Stream<WordSequence> result = null;
            for (Word word : ws) {
                Stream<Word> transformed = function.apply(word);
                if (result == null)
                    result = transformed.map(WordSequence::new);
                else
                    result = result.flatMap(seq -> transformed.map(w -> seq.append(w)));
            }
            return result;
        };
    }

}
