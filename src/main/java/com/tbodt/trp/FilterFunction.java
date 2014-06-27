/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.*;
import java.util.stream.Stream;

/**
 * A function that filters a data stream.
 *
 * @author Theodore Dubois
 */
public final class FilterFunction extends TransformerFunction {
    private static final Map<String, FilterFunction> functions = new HashMap<>();

    static {
        functions.put("length", new FilterFunction(
                (ws, args) -> ws.combine().getWords().get(0).length() == (Integer) args[0],
                new ArgumentList(ArgumentList.ArgumentType.INTEGER)));
        functions.put("endsWith", new FilterFunction(
                (ws, args) -> ws.combine().toString().endsWith((String) args[0]), 
                new ArgumentList(ArgumentList.ArgumentType.STRING)));
    }

    /**
     * A {@code FunctionalInterface} that describes a filter function.
     */
    @FunctionalInterface
    public interface Lambda extends TransformerFunction.Lambda {

        @Override
        public default Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] parameters) {
            return data.filter(ws -> test(ws, parameters));
        }

        /**
         * Tests the {@code ws} in whatever way makes sense, guided by the {@code args}.
         *
         * @param ws the word sequence
         * @param args the arguments
         * @return I am moving your eyes up to the method description
         */
        boolean test(WordSequence ws, Object[] args);
    }

    /**
     * Returns the {@code FilterFunction} with the given name.
     *
     * @param name the name
     * @return the {@code FilterFunction} with the given name
     */
    public static FilterFunction forName(String name) {
        return functions.get(name);
    }

    private FilterFunction(Lambda lambda) {
        this(lambda, new ArgumentList());
    }

    private FilterFunction(Lambda lambda, ArgumentList argTypes) {
        this(Collections.singletonMap(argTypes, lambda));
    }

    private FilterFunction(Map<ArgumentList, Lambda> overloadings) {
        super(Collections.unmodifiableMap(overloadings));
    }

    @Override
    public Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] args) {
        return ((FilterFunction.Lambda) lambdaForArgs(args)).invoke(data, args);
    }

    /**
     * Invokes the function using the {@link Lambda#test(com.tbodt.trp.WordSequence, java.lang.Object[])} method.
     * @param ws passed to the test method
     * @param args passed to the test method
     * @return the result of the test method
     */
    public boolean invoke(WordSequence ws, Object[] args) {
        return ((FilterFunction.Lambda) lambdaForArgs(args)).test(ws, args);
    }
}
