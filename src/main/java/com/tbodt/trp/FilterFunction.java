/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.trp;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A function that filters a data stream.
 * 
 * @author Theodore Dubois
 */
public class FilterFunction extends TransformerFunction {
    
    /**
     * A {@code FunctionalInterface} that describes a filter function.
     */
    @FunctionalInterface
    public interface Lambda extends TransformerFunction.Lambda, Predicate<WordSequence> {
        @Override
        public default Stream<WordSequence> invoke(Stream<WordSequence> data, Object[] parameters) {
            return data.filter(this);
        }
    }

    FilterFunction(Lambda lambda) {
        this(lambda, new ArgumentList());
    }

    FilterFunction(Lambda lambda, ArgumentList argTypes) {
        this(Collections.singletonMap(argTypes, lambda));
    }

    FilterFunction(Map<ArgumentList, Lambda> overloadings) {
        super(Collections.unmodifiableMap(overloadings));
    }
}
