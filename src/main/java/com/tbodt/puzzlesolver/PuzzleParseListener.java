/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import com.tbodt.puzzlesolver.PuzzleParser.CategoryDataContext;
import com.tbodt.puzzlesolver.PuzzleParser.CategoryTransformationContext;
import com.tbodt.puzzlesolver.PuzzleParser.FunctionTransformationContext;
import com.tbodt.puzzlesolver.PuzzleParser.StringDataContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Theodore Dubois
 */
public class PuzzleParseListener extends PuzzleBaseListener {

    private final Set<String> data = new HashSet<>();
    private final List<Transformation> transformations = new ArrayList<>();

    @Override
    public void exitStringData(StringDataContext ctx) {
        data.add(ctx.STRING().getText());
    }

    @Override
    public void exitCategoryData(CategoryDataContext ctx) {
        try {
            data.addAll(Category.forName(ctx.CATEGORY().getText()).getItems());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void exitCategoryTransformation(CategoryTransformationContext ctx) {
        super.exitCategoryTransformation(ctx);
    }

    @Override
    public void exitFunctionTransformation(FunctionTransformationContext ctx) {
        super.exitFunctionTransformation(ctx);
    }

    public Set<String> getData() {
        return Collections.unmodifiableSet(data);
    }

    public List<Transformation> getTransformations() {
        return Collections.unmodifiableList(transformations);
    }
}
