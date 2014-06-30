/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
