/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.trp;

import java.util.List;
import java.util.stream.Stream;

/**
 * A transformation that transforms based on a {@code Function}.
 * @author Theodore Dubois
 */
public class FunctionTransformation implements Transformation {
    private final TransformerFunction func;
    private final List<Object> args;

    /**
     * Constructs an {@code FunctionTransformation} that transforms based on the
     * specified {@code func} and passes it the {@code args}.
     * @param func the function to use for the transformation
     * @param args the arguments to pass to the function
     */
    public FunctionTransformation(TransformerFunction func, List<Object> args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return func.invoke(data, args.toArray());
    }
}