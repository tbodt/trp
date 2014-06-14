/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import com.tbodt.puzzlesolver.WordSequence.Word;
import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class TransformerFunction {
    private Map<ArgumentList, Transformer> overloadings;
    private static final Map<String, TransformerFunction> functions = new HashMap<>();

    static {
        functions.put("distinct", new TransformerFunction(Transformers::distinct));
        functions.put("unique", new TransformerFunction(Transformers::distinct));
        functions.put("uniq", new TransformerFunction(Transformers::distinct));
        functions.put("anagram", new TransformerFunction(Transformers::anagram));
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
        public static Stream<WordSequence> distinct(Stream<WordSequence> data, Object[] parameters) {
            return data.distinct();
        }

        public static Stream<WordSequence> anagram(Stream<WordSequence> data, Object[] parameters) {
            return data.unordered().flatMap(WordSequence.forEachWord(Transformers::allAnagrams)).distinct();
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

        private Transformers() {
        }
    }
}
