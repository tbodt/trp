/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import com.tbodt.jstack.ArrayStack;
import com.tbodt.jstack.Stack;
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
    private final Stack<List<Transformer>> transformations = new ArrayStack<>();
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
        transformations.push(new ArrayList<>());
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
        transformations.peek().add(new CategoryTransformation(cat));
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
    public void enterTransformationValue(PuzzleParser.TransformationValueContext ctx) {
        transformations.push(new ArrayList<>());
    }

    @Override
    public void exitTransformationValue(PuzzleParser.TransformationValueContext ctx) {
        values.put(ctx, transformations.pop());
    }

    @Override
    public void exitFunctionTransformation(PuzzleParser.FunctionTransformationContext ctx) {
        String name = ctx.FUNC().getText();
        ArgumentList args = new ArgumentList(ctx.value().stream().map(vctx ->
                values.get((ParseTree) vctx)).collect(Collectors.toList()));
        TransformerFunction fun = TransformerFunction.forName(name);
        if (fun == null) {
            errListener.syntaxError(null, null, 0, 0, "no function with name " + name, null);
            return;
        }
        if (!fun.isValidArguments(args)) {
            errListener.syntaxError(null, null, 0, 0, "arguments " + args + " invalid", null);
            return;
        }
        transformations.peek()
                .add(new FunctionTransformer(TransformerFunction.forName(name), args));
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
        return Collections.unmodifiableList(transformations.peek());
    }

}
