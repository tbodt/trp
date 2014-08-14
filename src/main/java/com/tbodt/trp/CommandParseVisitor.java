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

import static com.tbodt.trp.CommandProcessor.reportError;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A parse tree visitor for The Rapid Permuter.
 *
 * @author Theodore Dubois
 */
public class CommandParseVisitor extends CommandBaseVisitor<Object> {

    @Override
    public Object visitCommand(CommandParser.CommandContext ctx) {
        return visit(ctx.data());
    }

    @Override
    public Object visitStringData(CommandParser.StringDataContext ctx) {
        return Stream.of(new WordSequence(stripEnds(ctx.STRING().getText())));
    }

    @Override
    public Object visitCategoryData(CommandParser.CategoryDataContext ctx) {
        String catName = stripEnds(ctx.CATEGORY().getText());
        Category cat = Category.forName(catName);
        if (cat == null) {
            reportError("nonexistent category " + catName);
            return Stream.empty();
        }
        return cat.stream();
    }

    @Override
    public Object visitTransformedData(CommandParser.TransformedDataContext ctx) {
        Stream<WordSequence> data = (Stream<WordSequence>) visit(ctx.data());
        Transformer tx = Transformer.IDENTITY;
        for (CommandParser.TransformationContext tc : ctx.transformation())
            tx = tx.append((Transformer) visit(tc));
        return tx.transform(data.parallel());
    }

    @Override
    public Object visitIntValue(CommandParser.IntValueContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Object visitStringValue(CommandParser.StringValueContext ctx) {
        return stripEnds(ctx.STRING().getText());
    }

    @Override
    public Object visitTransformationValue(CommandParser.TransformationValueContext ctx) {
        return visit(ctx.transformation());
    }

    @Override
    public Object visitDataValue(CommandParser.DataValueContext ctx) {
        return visit(ctx.data());
    }
    
    @Override
    public Object visitFunctionTransformation(CommandParser.FunctionTransformationContext ctx) {
        String name = ctx.FUNC().getText();
        ArgumentList args = new ArgumentList(ctx.value().stream().map(vctx
                -> visit(vctx)).collect(Collectors.toList()));
        TransformerFunction fun = TransformerFunction.forName(name);
        if (fun == null) {
            reportError("no function with name " + name);
            return Transformer.IDENTITY;
        }
        if (!fun.isValidArguments(args)) {
            reportError("arguments " + args + " invalid");
            return Transformer.IDENTITY;
        }
        return new FunctionTransformer(fun, args);
    }

    private static String stripEnds(String str) {
        return str.substring(1, str.length() - 1);
    }
}
