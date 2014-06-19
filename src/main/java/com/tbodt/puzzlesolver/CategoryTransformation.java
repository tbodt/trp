/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.stream.Stream;

/**
 * A transformation that filters out words not in a category.
 */
public class CategoryTransformation implements Transformation {
    private final Category category;

    /**
     * Constructs a {@code CategoryTransformation} that filters out words not in {@code category}.
     * @param category a category
     */
    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return data.filter(datum -> category.getItems().contains(datum));
    }

}
