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
     * Apply a series of intermediate operations on {@code data} and return the result.
     *
     * @param data the data to transform
     * @return the result of the transformation
     */
    Stream<WordSequence> transform(Stream<WordSequence> data);
    
    /**
     * Returns a new {@link Transformer} that first applies this transformation to the data, then to {@code after}.
     * 
     * @param after the transformation to apply after this transformation
     * @return a new {@link Transformer} that first applies this transformation to the data, then to {@code after}
     */
    default Transformer append(Transformer after) {
        return new Transformer() {
            public Stream<WordSequence> transform(Stream<WordSequence> data) {
                return after.transform(Transformer.this.transform(data));
            }
        }
    }
}
