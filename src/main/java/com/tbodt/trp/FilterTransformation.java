/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.trp;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author theodoredubois
 */
public class FilterTransformation extends FunctionTransformation implements Filter {

    public FilterTransformation(FilterFunction func, List<Object> args) {
        super(func, args);
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return data.filter(this);
    }
    
    

    @Override
    public boolean test(WordSequence t) {
        return getFunction().invoke(t, getArguments().toArray());
    }

    @Override
    public FilterFunction getFunction() {
        return (FilterFunction) super.getFunction();
    }

}
