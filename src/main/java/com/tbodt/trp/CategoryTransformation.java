/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

/**
 * A transformation that filters out words not in a category.
 */
public class CategoryTransformation implements Filter {
    private final Category category;

    /**
     * Constructs a {@code CategoryTransformation} that filters out words not in {@code category}.
     * @param category a category
     */
    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public boolean test(WordSequence ws) {
        return category.getItems().contains(ws);
    }
}
