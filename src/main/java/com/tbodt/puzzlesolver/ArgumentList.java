/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Theodore Dubois
 */
public final class ArgumentList {
    private final List<ArgumentType> argTypes;
    private final boolean varargs; // the last argument may be repeated

    public ArgumentList(ArgumentType... argTypes) {
        this(Arrays.asList(argTypes));
    }

    public ArgumentList(boolean varargs, ArgumentType... argTypes) {
        this(Arrays.asList(argTypes), varargs);
    }

    public ArgumentList(List<ArgumentType> argTypes) {
        this(argTypes, false);
    }

    public ArgumentList(List<ArgumentType> argTypes, boolean varargs) {
        this.argTypes = Collections.unmodifiableList(argTypes);
        this.varargs = varargs;
    }

    public static enum ArgumentType {
        INTEGER, STRING;

        public static ArgumentType typeOf(Object arg) {
            if (arg.getClass() == Integer.class)
                return INTEGER;
            else if (arg.getClass() == String.class)
                return STRING;
            else
                throw new IllegalArgumentException("invalid argument type " + arg);
        }

    }

    public static ArgumentList of(Object[] args) {
        return new ArgumentList(Arrays.stream(args).map(ArgumentType::typeOf).collect(
                Collectors.toList()));
    }

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
        while (list.get(list.size() - 1).equals(varargType))
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
