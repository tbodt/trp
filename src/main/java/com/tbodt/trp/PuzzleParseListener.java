/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.*;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * A parse tree listener for The Rapid Permuter.
 *
 * @author Theodore Dubois
 */
@SuppressWarnings("null")
public class PuzzleParseListener extends PuzzleBaseListener {

    private final Set<WordSequence> data = new HashSet<>();
    private final List<Transformer> transformations = new ArrayList<>();
    private final ParseTreeProperty<Object> values = new ParseTreeProperty<>();
    private final ANTLRErrorListener errListener;

    /**
     * Constructs a {@code PuzzleParseListener} that outputs error messages to the given error
     * listener.
     *
     * @param errListener the error listener.
     */
    public PuzzleParseListener(ANTLRErrorListener errListener) {
        this.errListener = errListener;
    }

    @Override
    public void exitStringData(PuzzleParser.StringDataContext ctx) {
        data.add(new WordSequence(stripEnds(ctx.STRING().getText())));
    }

    @Override
    public void exitCategoryData(PuzzleParser.CategoryDataContext ctx) {
        String catName = stripEnds(ctx.CATEGORY().getText());
        Category cat = Category.forName(catName);
        if (cat == null) {
            errListener.syntaxError(null, null, 0, 0, "nonexistent category " + catName, null);
            return;
        }
        data.addAll(cat.getItems());
    }

    @Override
    public void exitCategoryTransformation(PuzzleParser.CategoryTransformationContext ctx) {
        String catName = stripEnds(ctx.CATEGORY().getText());
        Category cat = Category.forName(catName);
        if (cat == null) {
            errListener.syntaxError(null, null, 0, 0, "nonexistent category " + catName, null);
            return;
        }
        transformations.add(new CategoryTransformation(cat));
    }

    @Override
    public void exitIntValue(PuzzleParser.IntValueContext ctx) {
        values.put(ctx, Integer.valueOf(ctx.INT().getText()));
    }

    @Override
    public void exitStringValue(PuzzleParser.StringValueContext ctx) {
        values.put(ctx, ctx.STRING().getText());
    }

    @Override
    public void exitFunctionTransformation(PuzzleParser.FunctionTransformationContext ctx) {
        String name = ctx.FUNC().getText();
        List<Object> args = new ArrayList<>(ctx.value());
        args = args.stream().map(vctx -> values.get((ParseTree) vctx)).collect(Collectors.toList());
        TransformerFunction fun = TransformerFunction.forName(name);
        if (fun == null) {
            errListener.syntaxError(null, null, 0, 0, "no function with name " + name, null);
            return;
        }
        if (!fun.isValidArguments(args.toArray())) {
            errListener.syntaxError(null, null, 0, 0, "arguments " + args + " invalid", null);
            return;
        }
        transformations.add(new FunctionTransformation(TransformerFunction.forName(name), args));
    }

    private static String stripEnds(String str) {
        return str.substring(1, str.length() - 1);
    }

    /**
     * Returns the data set described by the input this parse tree listener heard.
     *
     * @return the data set described by the input this parse tree listener heard.
     */
    public Set<WordSequence> getData() {
        return Collections.unmodifiableSet(data);
    }

    /**
     * Returns the transformations described by the input this parse tree listener heard.
     *
     * @return the transformations described by the input this parse tree listener heard.
     */
    public List<Transformer> getTransformations() {
        return Collections.unmodifiableList(transformations);
    }

}
