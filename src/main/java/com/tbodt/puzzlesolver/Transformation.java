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
    Stream<String> transform(Stream<String> data);
}
