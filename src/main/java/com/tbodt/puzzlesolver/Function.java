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
}
