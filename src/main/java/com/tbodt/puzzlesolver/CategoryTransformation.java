/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.stream.Stream;

/**
 * A transformation that filters out strings in a category.
 */
public class CategoryTransformation implements Transformation {
    private final Category category;

    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public Stream<String> transform(Stream<String> data) {
        return data.filter(datum -> category.getItems().contains(datum));
    }

}
