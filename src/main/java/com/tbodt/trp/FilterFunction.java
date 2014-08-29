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
        Lambda lengthI = noMultipleWords((ws, args) -> ws.getWords().get(0).length() == args.integer(0));
        Lambda lengthII = noMultipleWords((ws, args) -> {
            int length = ws.getWords().get(0).length();
            return length >= args.integer(0) && length <= args.integer(1);
        });
        Map<ArgumentTypeList, Lambda> lengthOverloadings = new HashMap<>();
        lengthOverloadings.put(new ArgumentTypeList(INTEGER), lengthI);
        lengthOverloadings.put(new ArgumentTypeList(INTEGER, INTEGER), lengthII);
        functions.put("length", new FilterFunction(lengthOverloadings));

        functions.put("endsWith", new FilterFunction(
                noMultipleWords((ws, args) -> ws.toString().endsWith(args.string(0))),
                new ArgumentTypeList(STRING)));
        functions.put("startsWith", new FilterFunction(
                noMultipleWords((ws, args) -> ws.toString().startsWith(args.string(0))),
                new ArgumentTypeList(STRING)));
        functions.put("contains", new FilterFunction(
                noMultipleWords((ws, args) -> ws.toString().contains(args.string(0))),
                new ArgumentTypeList(STRING)));
        functions.put("countWords", new FilterFunction(
                (ws, args) -> ws.count() == args.integer(0),
                new ArgumentTypeList(INTEGER)));
        functions.put("in", new FilterFunction(
                (ws, args) -> args.data(0).contains(ws),
                new ArgumentTypeList(DATA)));
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
    
    private static Lambda noMultipleWords(Lambda in) {
        return (ws, args) -> {
            if (ws.getWords().size() != 1)
                return false;
            return in.test(ws, args);
        };
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
    
    @Override
    public Filter toTransformer(ArgumentList args) {
        return ws -> invoke(ws, args);
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
