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

import static com.tbodt.trp.ArgumentTypeList.ArgumentType.*;
import com.tbodt.trp.WordSequence.Word;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A function that transforms a data stream.
 *
 * @author Theodore Dubois
 */
public class TransformerFunction {
    private Map<ArgumentTypeList, Lambda> overloadings;
    private static final Map<String, TransformerFunction> functions = new HashMap<>();

    static {
        functions.put("anagram", new TransformerFunction(Transformers::anagram));
        functions.put("splitWords", new TransformerFunction(Transformers::splitWords,
                new ArgumentTypeList(true, INTEGER)));
        functions.put("remove", new TransformerFunction(Transformers::remove,
                new ArgumentTypeList(STRING)));
        functions.put("append", new TransformerFunction(Transformers::append,
                new ArgumentTypeList(STRING)));
    }

    /**
     * A {@code FunctionalInterface} that describes a transformer function.
     */
    @FunctionalInterface
    public interface Lambda {
        /**
         * Invokes the transformer.
         *
         * @param data the data to transform
         * @param parameters the parameters
         * @return the transformed stream
         */
        Stream<WordSequence> invoke(Stream<WordSequence> data, ArgumentList parameters);
    }

    private TransformerFunction(Lambda lambda) {
        this(lambda, new ArgumentTypeList());
    }

    private TransformerFunction(Lambda lambda, ArgumentTypeList argTypes) {
        this(Collections.singletonMap(argTypes, lambda));
    }

    /**
     *
     * @param overloadings
     */
    protected TransformerFunction(Map<ArgumentTypeList, Lambda> overloadings) {
        this.overloadings = Collections.unmodifiableMap(overloadings);
    }

    /**
     * Returns the {@code TransformerFunction} with the given name.
     *
     * @param name the name
     * @return {@code TransformerFunction} with the given name
     */
    public static TransformerFunction forName(String name) {
        if (functions.containsKey(name))
            return functions.get(name);
        else
            return FilterFunction.forName(name);
    }

    /**
     * Whether the arguments specified would be valid to pass to the
     * {@link TransformerFunction#invoke} method.
     *
     * @param args the arguments
     * @return whether the arguments specified would be valid to pass to the
     * {@link TransformerFunction#invoke} method
     */
    public boolean isValidArguments(ArgumentList args) {
        return overloadings.entrySet().stream().anyMatch(entry -> entry.getKey().matches(args));
    }

    /**
     * Invoke the transformer function.
     *
     * @param data the data to transform
     * @param args the arguments to the function
     * @return the result of the function transforming {@code data}
     */
    public Stream<WordSequence> invoke(Stream<WordSequence> data, ArgumentList args) {
        return lambdaForArgs(args).invoke(data, args);
    }

    protected Lambda lambdaForArgs(ArgumentList args) {
        return overloadings.entrySet().stream().filter(entry -> entry.getKey().matches(args))
                .findAny().orElseThrow(IllegalArgumentException::new).getValue();

    }

    private static final class Transformers {
        public static Stream<WordSequence> anagram(Stream<WordSequence> data, ArgumentList parameters) {
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

        public static Stream<WordSequence> splitWords(Stream<WordSequence> data, ArgumentList args) {
            int[] wordLengths = args.stream().mapToInt(a -> (Integer) a).toArray();
            int wordLengthSum = Arrays.stream(wordLengths).sum();
            return data.filter(ws -> ws.count() == 1)
                    .map(ws -> ws.getWords().get(0))
                    .filter(w -> w.length() == wordLengthSum)
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

        public static Stream<WordSequence> remove(Stream<WordSequence> data, ArgumentList args) {
            String str = args.string(0);
            return data.filter(ws -> ws.toString().contains(str))
                    .map(ws -> new WordSequence(ws.getWords().stream().map(word
                                            -> new Word(word.toString().replaceAll(Pattern
                                                            .quote(str), ""))
                                    )));
        }

        public static Stream<WordSequence> append(Stream<WordSequence> data, ArgumentList args) {
            String str = args.string(0);
            return data.filter(ws -> ws.count() != 1)
                    .map(ws -> new WordSequence(ws.getWords().get(0) + str));

        }

        private Transformers() {
        }
    }
}
