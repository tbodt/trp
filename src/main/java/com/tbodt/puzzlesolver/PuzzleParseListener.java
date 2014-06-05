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
import com.tbodt.puzzlesolver.PuzzleParser.ValueContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void exitCategoryTransformation(CategoryTransformationContext ctx) {
        try {
            transformations.add(new CategoryTransformation(Category.forName(ctx.CATEGORY().getText())));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void exitFunctionTransformation(FunctionTransformationContext ctx) {
        String name = ctx.FUNC().getText();
        List<Object> args = new ArrayList<>(ctx.value());
        args = args.stream().map(vctx -> ((ValueContext) vctx).val).collect(Collectors.toList());
        transformations.add(new FunctionTransformation(Function.forName(name), args));
    }

    public Set<String> getData() {
        return Collections.unmodifiableSet(data);
    }

    public List<Transformation> getTransformations() {
        return Collections.unmodifiableList(transformations);
    }

}
