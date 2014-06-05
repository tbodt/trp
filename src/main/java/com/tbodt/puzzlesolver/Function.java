/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class Function {
    private Map<List<ArgumentType>, Lambda> overloadings;
    private static final Map<String, Function> functions = new HashMap<>();

    static {
        functions.put("add", new Function(Functions::add));
        functions.put("distinct", new Function(Functions::distinct));
        functions.put("unique", new Function(Functions::distinct));
        functions.put("uniq", new Function(Functions::distinct));
        functions.put("anagram", new Function(Functions::anagram));
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
        Stream<String> invoke(Stream<String> data, Object... parameters);

    }

    private Function(Lambda lambda, ArgumentType... argTypes) {
        this(Collections.singletonMap(Arrays.asList(argTypes), lambda));
    }

    private Function(Map<List<ArgumentType>, Lambda> overloadings) {
        this.overloadings = Collections.unmodifiableMap(overloadings);
    }

    public static Function forName(String name) {
        return functions.get(name);
    }

    public boolean isValidArguments(Object[] args) {
        List<ArgumentType> argTypes = Arrays.stream(args).map(ArgumentType::typeOf).collect(Collectors.toList());
        return overloadings.containsKey(argTypes);
    }

    public Stream<String> invoke(Stream<String> data, Object[] args) {
        List<ArgumentType> argTypes = Arrays.stream(args).map(ArgumentType::typeOf).collect(Collectors.toList());
        if (!overloadings.containsKey(argTypes))
            throw new IllegalArgumentException("nonexistent overloading");
        return overloadings.get(argTypes).invoke(data, args);
    }

    private static final class Functions {
        public static Stream<String> add(Stream<String> data, Object[] parameters) {
            return Stream.concat(data, Stream.of((String) parameters[0]));
        }

        public static Stream<String> distinct(Stream<String> data, Object[] parameters) {
            return data.distinct();
        }

        public static Stream<String> anagram(Stream<String> data, Object[] parameters) {
            return data.unordered().flatMap(Functions::allAnagrams).distinct();
        }

        private static Stream<String> allAnagrams(String data) {
            if (data.length() <= 1)
                return Stream.of(data);
            Stream<String> ret = Stream.empty();
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                String rest = new StringBuilder(data).deleteCharAt(i).toString();
                ret = Stream.concat(ret, allAnagrams(rest).map(str -> ch + str)).unordered();
            }
            return ret;
        }

        private Functions() {
        }
    }
}
