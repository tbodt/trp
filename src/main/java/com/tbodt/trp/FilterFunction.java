/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import static com.tbodt.trp.ArgumentTypeList.ArgumentType.*;
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
        Lambda lengthI = (ws, args) -> ws.combine().getWords().get(0).length() == args.integer(0);
        Lambda lengthII = (ws, args) -> {
            int length = ws.combine().getWords().get(0).length();
            return length >= args.integer(0) && length <= args.integer(1);
        };
        Map<ArgumentTypeList, Lambda> lengthOverloadings = new HashMap<>();
        lengthOverloadings.put(new ArgumentTypeList(INTEGER), lengthI);
        lengthOverloadings.put(new ArgumentTypeList(INTEGER, INTEGER), lengthII);
        functions.put("length", new FilterFunction(lengthOverloadings));

        functions.put("endsWith", new FilterFunction(
                (ws, args) -> ws.combine().toString().endsWith(args.string(0)),
                new ArgumentTypeList(STRING)));
        functions.put("startsWith", new FilterFunction(
                (ws, args) -> ws.combine().toString().startsWith(args.string(0)),
                new ArgumentTypeList(STRING)));
        functions.put("all", new FilterFunction((ws, args) -> {
            boolean good = true;
            for (WordSequence.Word word : ws)
                good = good && args.filter(0).test(new WordSequence(word));
            return good;
        }, new ArgumentTypeList(FILTER)));
        functions.put("any", new FilterFunction((ws, args) -> {
            boolean good = false;
            for (WordSequence.Word word : ws)
                good = good || args.filter(0).test(new WordSequence(word));
            return good;
        }, new ArgumentTypeList(FILTER)));
    }

    /**
     * A {@code FunctionalInterface} that describes a filter function.
     */
    @FunctionalInterface
    public interface Lambda extends TransformerFunction.Lambda {

        @Override
        public default Stream<WordSequence> invoke(Stream<WordSequence> data, ArgumentList parameters) {
            return data.filter(ws -> test(ws, parameters));
        }

        /**
         * Tests the {@code ws} in whatever way makes sense, guided by the {@code args}.
         *
         * @param ws the word sequence
         * @param args the arguments
         * @return I am moving your eyes up to the method description
         */
        boolean test(WordSequence ws, ArgumentList args);
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
        this(lambda, new ArgumentTypeList());
    }

    private FilterFunction(Lambda lambda, ArgumentTypeList argTypes) {
        this(Collections.singletonMap(argTypes, lambda));
    }

    private FilterFunction(Map<ArgumentTypeList, Lambda> overloadings) {
        super(Collections.unmodifiableMap(overloadings));
    }

    @Override
    public Stream<WordSequence> invoke(Stream<WordSequence> data, ArgumentList args) {
        return ((FilterFunction.Lambda) lambdaForArgs(args)).invoke(data, args);
    }

    /**
     * Invokes the function using the
     * {@link Lambda#test(com.tbodt.trp.WordSequence, java.lang.Object[])} method.
     *
     * @param ws passed to the test method
     * @param args passed to the test method
     * @return the result of the test method
     */
    public boolean invoke(WordSequence ws, ArgumentList args) {
        return ((FilterFunction.Lambda) lambdaForArgs(args)).test(ws, args);
    }
}
