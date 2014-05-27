/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A transformation that filters out strings in a category.
 */
public class CategoryTransformation implements Transformation {
    private Category category;

    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public List<String> transform(String data) {
        if (category.contains(data))
            return Arrays.asList(data);
        else
            return Collections.emptyList();
    }

}
