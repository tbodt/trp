/*
 * Copyright (C) 2014 Theodore Dubois
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tbodt.trp;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A sequence of {@code Word}s.
 *
 * @author Theodore Dubois
 */
public final class WordSequence implements Iterable<WordSequence.Word> {
    private final List<Word> words;
    private static final Word[] EMPTY_WORD_ARRAY = new Word[0];

    /**
     * Constructs a {@code WordSequence} from each word in {@code data}.
     *
     * @param data the data
     */
    public WordSequence(CharSequence data) {
        words = Pattern.compile("\\s+").splitAsStream(data).map(Word::new).collect(Collectors.toList());
    }

    /**
     * Constructs a {@code WordSequence} of a single word.
     *
     * @param datum the word
     */
    public WordSequence(Word datum) {
        words = Collections.singletonList(datum);
    }

    /**
     * Constructs a {@code WordSequence} from a list of {@code Word}s.
     *
     * @param data the list of words
     */
    public WordSequence(List<Word> data) {
        words = new ArrayList<>(data);
    }

    /**
     * Constructs a {@code WordSequence} from an array of {@code Word}s.
     *
     * @param words the array of words
     */
    public WordSequence(Word[] words) {
        this.words = Arrays.asList(words);
    }

    /**
     * Constructs a {@code WordSequence} from the words in a
     * {@code Stream<Word>}.
     *
     * @param data the stream
     */
    public WordSequence(Stream<Word> data) {
        words = data.collect(Collectors.toList());
    }

    /**
     * Returns a list of all words in this {@code WordSequence}.
     *
     * @return a list of all words in this {@code WordSequence}
     */
    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }
    
    /**
     * Returns the number of words in this {@code WordSequence}.
     * 
     * @return the number of words in this {@code WordSequence}
     */
    public int count() {
        return words.size();
    }

    /**
     * A single word.
     */
    public static class Word implements CharSequence {
        private final String word;

        /**
         * Constructs a {@code Word} from a string.
         *
         * @param word the string
         */
        public Word(String word) {
            if (word.matches(".*\\s.*"))
                throw new IllegalArgumentException("words do not contain whitespace");
            this.word = word.toLowerCase();
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

    /**
     * Return a new {@code WordSequence} with the given word appended.
     *
     * @param word the word to append
     * @return a new {@code WordSequence} with the given word appended
     */
    public WordSequence append(Word word) {
        List<Word> wordsList = new ArrayList<>(getWords());
        wordsList.add(word);
        return new WordSequence(wordsList);
    }

    /**
     * Combines all words into one word by concatenating them.
     *
     * @return a new {@code WordSequence} which has one word, all the words
     * combined
     */
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

    /**
     * Returns a
     * {@link java.util.function.Function}{@code <WordSequence, Stream<WordSequence>>}
     * that applies the argument to each word in the {@code WordSequence} and
     * combines the results. 
     *
     * @param function the function
     * @return what it says above
     */
    public static Function<WordSequence, Stream<WordSequence>> forEachWord(Function<Word, Stream<Word>> function) {
        return ws -> {
            Stream<WordSequence> result = null;
            for (Word word : ws)
                if (result == null)
                    result = function.apply(word).map(WordSequence::new);
                else
                    result = result.flatMap(seq -> function.apply(word).map(w -> seq.append(w)));
            return result;
        };
    }

}
