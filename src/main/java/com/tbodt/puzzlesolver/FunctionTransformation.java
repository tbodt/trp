/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.puzzlesolver;

import java.util.List;

/**
 *
 * @author Theodore Dubois
 */
public class FunctionTransformation implements Transformation {
    private Function func;
    private List<Object> args;

    public FunctionTransformation(Function func, List<Object> args) {
        this.func = func;
        this.args = args;
    }
    
    @Override
    public List<String> transform(String data) {
        throw new UnsupportedOperationException("Method transform in class FunctionTransformation is not implemented");
    }

}
