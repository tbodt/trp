/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
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
    
    public boolean invoke(WordSequence ws, Object[] args) {
        return ((FilterFunction.Lambda) lambdaForArgs(args)).test(ws, args);
    }
}
