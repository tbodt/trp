/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;

/**
 * A transformation that filters out strings in a category.
 */
public class CategoryTransformation implements Transformation {
    private final Category category;

    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public Set<String> transform(String data) {
        if (category.getItems().contains(data))
            return new HashSet<>(Arrays.asList(data));
        else
            return Collections.emptySet();
    }

}
