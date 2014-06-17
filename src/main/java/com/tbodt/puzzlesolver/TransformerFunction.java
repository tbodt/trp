/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import com.tbodt.puzzlesolver.WordSequence.Word;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Theodore Dubois
 */
public class TransformerFunction {
    private Map<ArgumentList, Transformer> overloadings;
    private static final Map<String, TransformerFunction> functions = new HashMap<>();

    static {
        functions.put("anagram", new TransformerFunction(Transformers::anagram));
        functions.put("splitWords", new TransformerFunction(Transformers::splitWords,
                new ArgumentList(true, ArgumentList.ArgumentType.INTEGER)));
    }

    @FunctionalInterface
    public interface Transformer {
        Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] parameters);

    }

    private TransformerFunction(Transformer lambda) {
        this(lambda, new ArgumentList());
    }

    private TransformerFunction(Transformer lambda, ArgumentList argTypes) {
        this(Collections.singletonMap(argTypes, lambda));
    }

    private TransformerFunction(Map<ArgumentList, Transformer> overloadings) {
        this.overloadings = Collections.unmodifiableMap(overloadings);
    }

    public static TransformerFunction forName(String name) {
        return functions.get(name);
    }

    public boolean isValidArguments(Object[] args) {
        ArgumentList argTypes = ArgumentList.of(args);
        return overloadings.containsKey(argTypes);
    }

    public Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] args) {
        ArgumentList argTypes = ArgumentList.of(args);
        if (!overloadings.containsKey(argTypes))
            throw new IllegalArgumentException("nonexistent overloading");
        return overloadings.get(argTypes).invoke(data, args);
    }

    private static final class Transformers {
        public static Stream<WordSequence> anagram(Stream<WordSequence> data, Object[] parameters) {
            class AnagramIterator implements Iterator<Word> {
                private final Word start;
                private Word current;
                private boolean done;

                AnagramIterator(Word start) {
                    current = this.start = start;
                }

                @Override
                public boolean hasNext() {
                    return !done;
                }

                @Override
                public Word next() {
                    if (done)
                        throw new NoSuchElementException();
                    StringBuilder b = new StringBuilder(current);
                    for (int i = b.length() - 1; i > 0; i--)
                        if (b.charAt(i - 1) < b.charAt(i)) {
                            int j = b.length() - 1;
                            while (b.charAt(i - 1) >= b.charAt(j))
                                j--;
                            swap(b, i - 1, j);
                            reverse(b, i);
                            current = new Word(b.toString());
                            done = current.equals(start);
                            return current;
                        }
                    current = new Word(b.reverse().toString());
                    done = current.equals(start);
                    return current;
                }

                private void swap(StringBuilder b, int i, int j) {
                    char tmp = b.charAt(i);
                    b.setCharAt(i, b.charAt(j));
                    b.setCharAt(j, tmp);
                }

                private void reverse(StringBuilder b, int i) {
                    int j = b.length() - 1;
                    while (i < j) {
                        swap(b, i, j);
                        i++;
                        j--;
                    }
                }
            }
            return data.flatMap(WordSequence.forEachWord(w -> StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(
                            new AnagramIterator(w),
                            Spliterator.DISTINCT + Spliterator.IMMUTABLE + Spliterator.NONNULL),
                    false)));
        }

        public static Stream<WordSequence> splitWords(Stream<WordSequence> data, Object[] args) {
            int[] wordLengths = Arrays.stream(args).mapToInt(a -> (Integer) a).toArray();
            int wordLengthSum = Arrays.stream(wordLengths).sum();
            return data.filter(ws -> ws.getWords().size() == 1)
                    .filter(ws -> ws.getWords().stream().mapToInt(Word::length).sum() == wordLengthSum)
                    .map(ws -> ws.getWords().get(0))
                    .map(w -> {
                        String str = w.toString();
                        List<Word> words = new ArrayList<>();
                        for (int wordLength : wordLengths) {
                            String word = str.substring(0, wordLength);
                            str = str.substring(wordLength);
                            words.add(new Word(word));
                        }
                        return new WordSequence(words);
                    });
        }

        private Transformers() {
        }
    }
}
