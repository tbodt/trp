/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.stream.Stream;

/**
 * An input transformation.
 */
public interface Transformation {

    /**
     * Apply a series of intermediate operations on {@code data} and return the result.
     * @param data the data to transform
     * @return the result of the transformation
     */
    Stream<WordSequence> transform(Stream<WordSequence> data);
}
