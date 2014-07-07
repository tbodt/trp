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
 * A transformation that transforms based on a {@code Filter}.
 *
 * @author theodore
 */
public class FilterTransformer extends FunctionTransformer implements Filter {

    /**
     * Constructs an {@code FilterTransformation} that transforms based on the specified
     * {@code func} and passes it the {@code args}.
     *
     * @param func
     * @param args
     */
    public FilterTransformer(FilterFunction func, ArgumentList args) {
        super(func, args);
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        return data.filter(this);
    }

    @Override
    public boolean test(WordSequence t) {
        return getFunction().invoke(t, getArguments());
    }

    @Override
    public FilterFunction getFunction() {
        return (FilterFunction) super.getFunction();
    }

}
