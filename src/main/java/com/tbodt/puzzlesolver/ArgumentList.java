/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A list of the types of arguments to a function.
 *
 * @author Theodore Dubois
 */
public final class ArgumentList {
    private final List<ArgumentType> argTypes;
    private final boolean varargs; // the last argument may be repeated

    /**
     * Constructs an {@code ArgumentList} from the given list of {@code ArgumentType}s.
     *
     * @param argTypes the types of arguments
     */
    public ArgumentList(ArgumentType... argTypes) {
        this(Arrays.asList(argTypes));
    }

    /**
     * Constructs an {@code ArgumentList} from the given list of {@code ArgumentType}s and whether
     * the last argument may be repeated zero or more times.
     *
     * @param argTypes the types of arguments
     * @param varargs whether the last argument may be repeated zero or more times
     */
    public ArgumentList(boolean varargs, ArgumentType... argTypes) {
        this(Arrays.asList(argTypes), varargs);
    }

    /**
     * Constructs an {@code ArgumentList} from the given list of {@code ArgumentType}s.
     *
     * @param argTypes the types of arguments
     */
    public ArgumentList(List<ArgumentType> argTypes) {
        this(argTypes, false);
    }

    /**
     * Constructs an {@code ArgumentList} from the given list of {@code ArgumentType}s and whether
     * the last argument may be repeated zero or more times.
     *
     * @param argTypes the types of arguments
     * @param varargs whether the last argument may be repeated zero or more times
     */
    public ArgumentList(List<ArgumentType> argTypes, boolean varargs) {
        this.argTypes = Collections.unmodifiableList(argTypes);
        this.varargs = varargs;
    }

    /**
     * The type of a function argument, either an integer or a string.
     */
    public static enum ArgumentType {
        /**
         * An integer argument type.
         */
        INTEGER,
        /**
         * A string argument type.
         */
        STRING;

        /**
         * The {@code ArgumentType} corresponding to the given object.
         *
         * @param arg the given object
         * @return the {@code ArgumentType} corresponding to the given object
         */
        public static ArgumentType typeOf(Object arg) {
            if (arg.getClass() == Integer.class)
                return INTEGER;
            else if (arg.getClass() == String.class)
                return STRING;
            else
                throw new IllegalArgumentException("invalid argument type " + arg);
        }

    }

    /**
     * An argument list for the array of objects passed.
     *
     * @param args an array of objects
     * @return an argument list for the array of objects passed
     */
    public static ArgumentList of(Object[] args) {
        return new ArgumentList(Arrays.stream(args).map(ArgumentType::typeOf).collect(
                Collectors.toList()));
    }

    /**
     * Returns the list of {@code ArgumentType}s that this {@code ArgumentList} represents.
     *
     * @return the list of {@code ArgumentType}s that this {@code ArgumentList} represents
     */
    public List<ArgumentType> getArgumentTypes() {
        return Collections.unmodifiableList(argTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArgumentList))
            return false;
        final ArgumentList other = (ArgumentList) obj;
        if (varargs == other.varargs)
            return argTypes.equals(other.argTypes);
        if (!varargs)
            return other.equals(this);
        ArgumentType varargType = getArgumentTypes().get(getArgumentTypes().size() - 1);
        List<ArgumentType> list = new ArrayList<>(other.getArgumentTypes());
        while (list.size() > 0 && list.get(list.size() - 1).equals(varargType))
            list.remove(list.size() - 1);
        list.add(varargType);
        return list.equals(getArgumentTypes());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.argTypes);
        hash = 37 * hash + (this.varargs ? 1 : 0);
        return hash;
    }

}
