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
 * A list of arguments. Provides methods for easy argument access.
 *
 * @author Theodore Dubois
 */
public class ArgumentList {
    private final List<Object> args;
    private final ArgumentTypeList argTypes;

    /**
     * Constructs an {@code ArgumentList} from an array of arguments.
     *
     * @param args the arguments
     */
    public ArgumentList(List<Object> args) {
        this.args = args;
        this.argTypes = ArgumentTypeList.of(args);
    }

    /**
     * Returns the arguments.
     *
     * @return the arguments
     */
    public List<Object> arguments() {
        return Collections.unmodifiableList(args);
    }

    /**
     * Returns the argument at the given index.
     *
     * @param idx the index.
     * @return the argument at the given index
     */
    public Object argument(int idx) {
        return arguments().get(idx);
    }

    /**
     * Returns the number of arguments.
     *
     * @return the number of arguments
     */
    public int length() {
        return arguments().size();
    }

    /**
     * Returns the string at the given index.
     *
     * @param idx the index.
     * @return the argument at the given index
     * @throws IllegalArgumentException if the argument at the given index is not a string
     */
    public String string(int idx) {
        try {
            return (String) arguments().get(idx);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the integer at the given index.
     *
     * @param idx the index.
     * @return the argument at the given index
     * @throws IllegalArgumentException if the argument at the given index is not a string
     */
    public int integer(int idx) {
        try {
            return (Integer) arguments().get(idx);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the transformer at the given index.
     *
     * @param idx the index.
     * @return the argument at the given index
     * @throws IllegalArgumentException if the argument at the given index is not a transformer
     */
    public Transformer transformer(int idx) {
        try {
            return (Transformer) arguments().get(idx);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the filter at the given index.
     *
     * @param idx the index.
     * @return the argument at the given index
     * @throws IllegalArgumentException if the argument at the given index is not a filter
     */
    public Filter filter(int idx) {
        try {
            return (Filter) arguments().get(idx);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Return a stream of the arguments. This is a convenience method, as it behaves as if it was
     * implemented like this:
     * <pre>{@code
     * public Stream<Object> stream() {
     *     return getArguments().stream();
     * }}</pre>
     *
     * @return a stream of the arguments
     */
    public Stream<Object> stream() {
        return arguments().stream();
    }

    /**
     * Returns the types of the arguments.
     *
     * @return the types of the arguments
     */
    public ArgumentTypeList getArgumentTypes() {
        return argTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ArgumentList other = (ArgumentList) obj;
        if (!Objects.equals(this.args, other.args))
            return false;
        return Objects.equals(this.argTypes, other.argTypes);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.args);
        hash = 11 * hash + Objects.hashCode(this.argTypes);
        return hash;
    }
}
