/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import com.tbodt.puzzlesolver.WordSequence.Word;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class TransformerFunction {
    private Map<List<ArgumentType>, Lambda> overloadings;
    private static final Map<String, TransformerFunction> functions = new HashMap<>();

    static {
        functions.put("distinct", new TransformerFunction(Functions::distinct));
        functions.put("unique", new TransformerFunction(Functions::distinct));
        functions.put("uniq", new TransformerFunction(Functions::distinct));
        functions.put("anagram", new TransformerFunction(Functions::anagram));
    }

    public enum ArgumentType {
        INTEGER, STRING;

        public static ArgumentType typeOf(Object arg) {
            if (arg.getClass() == Integer.class)
                return INTEGER;
            else if (arg.getClass() == String.class)
                return STRING;
            else
                throw new IllegalArgumentException("invalid argument type " + arg);
        }

    }

    @FunctionalInterface
    public interface Lambda {
        Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] parameters);

    }

    private TransformerFunction(Lambda lambda, ArgumentType... argTypes) {
        this(Collections.singletonMap(Arrays.asList(argTypes), lambda));
    }

    private TransformerFunction(Map<List<ArgumentType>, Lambda> overloadings) {
        this.overloadings = Collections.unmodifiableMap(overloadings);
    }

    public static TransformerFunction forName(String name) {
        return functions.get(name);
    }

    public boolean isValidArguments(Object[] args) {
        List<ArgumentType> argTypes = Arrays.stream(args).map(ArgumentType::typeOf).collect(Collectors.toList());
        return overloadings.containsKey(argTypes);
    }

    public Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] args) {
        List<ArgumentType> argTypes = Arrays.stream(args).map(ArgumentType::typeOf).collect(Collectors.toList());
        if (!overloadings.containsKey(argTypes))
            throw new IllegalArgumentException("nonexistent overloading");
        return overloadings.get(argTypes).invoke(data, args);
    }

    private static final class Functions {
        public static Stream<WordSequence> distinct(Stream<WordSequence> data, Object[] parameters) {
            return data.distinct();
        }

        public static Stream<WordSequence> anagram(Stream<WordSequence> data, Object[] parameters) {
            return data.unordered().flatMap(WordSequence.forEachWord(Functions::allAnagrams)).distinct();
        }

        private static Stream<Word> allAnagrams(Word data) {
            if (data.length() <= 1)
                return Stream.of(data);
            Stream<Word> ret = Stream.empty();
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                String rest = new StringBuilder(data).deleteCharAt(i).toString();
                ret = Stream.concat(ret, allAnagrams(new Word(rest)).map(word -> new Word(ch + word.toString()))).unordered();
            }
            return ret;
        }

        private Functions() {
        }
    }
}
