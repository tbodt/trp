/*
 * Copyright (C) 2014 Theodore Dubois
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
public class CommandParseListener extends CommandBaseListener {

    private final Set<WordSequence> data = new HashSet<>();
    private final Stack<CompoundTransformer.Builder> transformations = new ArrayStack<>();
    private final ParseTreeProperty<Object> values = new ParseTreeProperty<>();
    private final ANTLRErrorListener errListener;

    /**
     * Constructs a {@code CommandParseListener} that outputs error messages to
     * the given error listener.
     *
     * @param errListener the error listener.
     */
    public CommandParseListener(ANTLRErrorListener errListener) {
        this.errListener = errListener;
        transformations.push(CompoundTransformer.builder());
    }

    @Override
    public void exitStringData(CommandParser.StringDataContext ctx) {
        data.add(new WordSequence(stripEnds(ctx.STRING().getText())));
    }

    @Override
    public void exitCategoryData(CommandParser.CategoryDataContext ctx) {
        String catName = stripEnds(ctx.CATEGORY().getText());
        Category cat = Category.forName(catName);
        if (cat == null) {
            errListener.syntaxError(null, null, 0, 0, "nonexistent category " + catName, null);
            return;
        }
        data.addAll(cat.getItems());
    }

    @Override
    public void exitCategoryTransformation(CommandParser.CategoryTransformationContext ctx) {
        String catName = stripEnds(ctx.CATEGORY().getText());
        Category cat = Category.forName(catName);
        if (cat == null) {
            errListener.syntaxError(null, null, 0, 0, "nonexistent category " + catName, null);
            return;
        }
        transformations.peek().add(new CategoryTransformation(cat));
    }

    @Override
    public void exitIntValue(CommandParser.IntValueContext ctx) {
        values.put(ctx, Integer.valueOf(ctx.INT().getText()));
    }

    @Override
    public void exitStringValue(CommandParser.StringValueContext ctx) {
        values.put(ctx, ctx.STRING().getText());
    }

    @Override
    public void enterTransformationValue(CommandParser.TransformationValueContext ctx) {
        transformations.push(CompoundTransformer.builder());
    }

    @Override
    public void exitTransformationValue(CommandParser.TransformationValueContext ctx) {
        CompoundTransformer transformation = transformations.pop().build();
        if (transformation.getTransformers().size() == 1)
            values.put(ctx, transformation.getTransformers().get(0));
        else
            values.put(ctx, transformations.pop());
    }

    @Override
    public void exitFunctionTransformation(CommandParser.FunctionTransformationContext ctx) {
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
    public CompoundTransformer getTransformations() {
        return transformations.peek().build();
    }

}
