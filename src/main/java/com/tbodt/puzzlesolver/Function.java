/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author Theodore Dubois
 */
public class Function {
    private static final Map<String, Function.Transformation> functions = new HashMap<>();
    static {
        functions.put("addChicken", Function::addChicken);
    }
    
    public static Function.Transformation forName(String name) {
        return functions.get(name);
    }
    
    @FunctionalInterface
    public interface Transformation {
        Stream<String> transform(Stream<String> data, Object... parameters);
    }

    public static Stream<String> addChicken(Stream<String> data, Object[] parameters) {
        return Stream.concat(data, Stream.of("chicken"));
    }
}
