/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public interface Filter extends Transformer, Predicate<WordSequence> {
    @Override
    public default Stream<WordSequence> transform(Stream<WordSequence> data) {
        return data.filter(this);
    }

    @Override
    public boolean test(WordSequence t);
}
