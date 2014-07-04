/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.trp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
