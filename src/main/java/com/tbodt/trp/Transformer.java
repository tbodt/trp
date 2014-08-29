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
 * An input transformation.
 */
public interface Transformer {
    /**
     * The identity transformer, which does nothing to the input.
     */
    public Transformer IDENTITY = data -> data;

    /**
     * Apply a series of intermediate operations on {@code data} and return the result.
     *
     * @param data the data to transform
     * @return the result of the transformation
     */
    public Stream<WordSequence> transform(Stream<WordSequence> data);

    /**
     * Returns a new {@link Transformer} that first applies this transformation to the data, then to
     * {@code after}.
     *
     * @param after the transformation to apply after this transformation
     * @return a new {@link Transformer} that first applies this transformation to the data, then to
     * {@code after}
     */
    public default Transformer append(Transformer after) {
        if (this == IDENTITY)
            return after;
        if (after == IDENTITY)
            return this;
        if (this instanceof Filter && after instanceof Filter) {
            // We need both lines. I'm too lazy to explain why, so try it and see!
            Filter ret = (WordSequence ws) -> ((Filter) this).test(ws) && ((Filter) after).test(ws);
            return ret;
        }
        return (Stream<WordSequence> data) -> after.transform(this.transform(data));
    }
}
