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

import java.util.stream.Stream;

/**
 * A transformation that transforms based on a {@code Function}.
 *
 * @author Theodore Dubois
 */
public class FunctionTransformer implements Transformer {

    private final TransformerFunction func;
    private final ArgumentList args;

    /**
     * Constructs an {@code FunctionTransformation} that transforms based on the
     * specified {@code func} and passes it the {@code args}.
     *
     * @param func the function to use for the transformation
     * @param args the arguments to pass to the function
     */
    public FunctionTransformer(TransformerFunction func, ArgumentList args) {
        this.func = func;
        this.args = args;
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return getFunction().invoke(data, getArguments());
    }

    /**
     * Returns the function.
     *
     * @return the function
     */
    public TransformerFunction getFunction() {
        return func;
    }

    /**
     * Returns the arguments.
     *
     * @return the arguments
     */
    public ArgumentList getArguments() {
        return args;
    }

}
