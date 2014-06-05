/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.puzzlesolver;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class FunctionTransformation implements Transformation {
    private final Function func;
    private final List<Object> args;

    public FunctionTransformation(Function func, List<Object> args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public Stream<String> transform(Stream<String> data) {
        return func.invoke(data, args.toArray());
    }
}
