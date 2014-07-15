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

    /**
     * Creates a {@code CompoundTransformer} that wraps the given list of {@code Transformer}s.
     *
     * @param transformers the list of {@code Transfomers}
     */
    public CompoundTransformer(List<? extends Transformer> transformers) {
        this.transformers = Collections.unmodifiableList(transformers);
    }

    /**
     * Returns a new {@link Builder}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link CompoundTransformation}s. Builds them up one transformer at a time. If
     * every {@code Transformer} added is actually a {@code Filter}, the returned
     * {@code CompoundTransformation} will implement {@code Filter}.
     */
    public static class Builder {
        private final List<Transformer> transformers = new ArrayList<>();
        private boolean built = false;

        private Builder() {
        }

        /**
         * Add a new {@code Transformer} to this builder.
         *
         * @param tx the transformer
         */
        public void add(Transformer tx) {
            if (built)
                throw new IllegalStateException("builder already built");
            if (tx instanceof CompoundTransformer)
                transformers.addAll(((CompoundTransformer) tx).getTransformers());
            else
                transformers.add(tx);
        }

        /**
         * Build a new {@link CompoundTransformer}.
         *
         * @return a new {@link CompoundTransformer}
         */
        public CompoundTransformer build() {
            if (built)
                throw new IllegalStateException("builder already built");
            built = true;
            if (transformers.stream().allMatch(tx -> tx instanceof Filter))
                return new CompoundFilter(transformers);
            return new CompoundTransformer(transformers);
        }
    }

    /**
     * Returns the {@code Transformers} in this.
     *
     * @return the {@code Transformers} in this
     */
    public List<Transformer> getTransformers() {
        return Collections.unmodifiableList(transformers);
    }

    @Override
    public Stream<WordSequence> transform(Stream<WordSequence> data) {
        for (Transformer tx : transformers)
            data = tx.transform(data);
        return data;
    }

    private static class CompoundFilter extends CompoundTransformer implements Filter {
        public CompoundFilter(List<Transformer> transformers) {
            super(transformers);
        }

        @Override
        public boolean test(WordSequence t) {
            boolean ret = true;
            for (Transformer tx : getTransformers())
                ret = ret && ((Filter) tx).test(t);
            return ret;
        }
    }
}
