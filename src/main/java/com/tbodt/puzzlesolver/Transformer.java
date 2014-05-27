/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tbodt.puzzlesolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Theodore Dubois
 */
public class Transformer {
    public static Set<String> transform(Set<String> data, List<Transformation> transformations) {
        for (Transformation tx : transformations) {
            Set<String> newData = new HashSet<>();
            for (String datum : data)
                newData.addAll(tx.transform(datum));
            data = newData;
        }
        return data;
    }

    private Transformer() {
    }
}
