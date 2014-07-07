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

import java.util.*;
import java.util.stream.Stream;

/**
 * Wraps multiple {@link Transformer}s into one {@link Transformer}.
 *
 * @author Theodore Dubois
 */
public class CompoundTransformer implements Transformer {
    private final List<Transformer> transformers;

    public CompoundTransformer(List<Transformer> transformers) {
        this.transformers = Collections.unmodifiableList(transformers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Transformer> transformers = new ArrayList<>();

        private Builder() {
        }

        public void add(Transformer tx) {
            transformers.add(tx);
        }
        
        public CompoundTransformer build() {
            return new CompoundTransformer(transformers);
        }
    }

    public List<Transformer> getTransformers() {
        return Collections.unmodifiableList(transformers);
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        for (Transformer tx : transformers)
            data = tx.transform(data);
        return data;
    }
}
