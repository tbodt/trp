/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class Function {
    private static final Map<String, Function.Transformation> functions = new HashMap<>();

    static {
        functions.put("add_s", Function::add);
        functions.put("distinct_", Function::distinct);
        functions.put("unique_", Function::distinct);
        functions.put("uniq_", Function::distinct);
        functions.put("anagram_", Function::anagram);
    }

    public static Function.Transformation forName(String name) {
        return functions.get(name);
    }

    public static Function.Transformation forNameAndArgs(String name, Class<?>[] args) {
        return forName(name + "_" + argumentString(args));
    }

    public static String argumentString(Class<?>[] args) {
        StringBuilder b = new StringBuilder();
        for (Class<?> cl : args)
            if (cl == int.class || cl == Integer.class)
                b.append("i");
            else if (cl == String.class)
                b.append("s");
            else
                b.append("?");
        return b.toString();
    }

    @FunctionalInterface
    public interface Transformation {
        Stream<String> transform(Stream<String> data, Object... parameters);

    }

    public static Stream<String> add(Stream<String> data, Object[] parameters) {
        return Stream.concat(data, Stream.of((String) parameters[0]));
    }

    public static Stream<String> distinct(Stream<String> data, Object[] parameters) {
        return data.distinct();
    }

    public static Stream<String> anagram(Stream<String> data, Object[] parameters) {
        return data.unordered().flatMap(Function::allAnagrams).distinct();
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
}
