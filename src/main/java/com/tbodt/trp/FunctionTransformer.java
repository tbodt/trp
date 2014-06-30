/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.stream.Stream;

/**
 * A transformation that transforms based on a {@code Function}.
 *
 * @author Theodore Dubois
 */
public class FunctionTransformer implements Transformer {

    private final TransformerFunction func;
    private final ArgumentList args;

    /**
     * Constructs an {@code FunctionTransformation} that transforms based on the
     * specified {@code func} and passes it the {@code args}.
     *
     * @param func the function to use for the transformation
     * @param args the arguments to pass to the function
     */
    public FunctionTransformer(TransformerFunction func, ArgumentList args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return getFunction().invoke(data, getArguments());
    }

    /**
     * Returns the function.
     *
     * @return the function
     */
    public TransformerFunction getFunction() {
        return func;
    }

    /**
     * Returns the arguments.
     *
     * @return the arguments
     */
    public ArgumentList getArguments() {
        return args;
    }

}
