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
        return new ArgumentList(Arrays.stream(args).map(ArgumentType::typeOf).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArgumentList))
            return false;
        final ArgumentList other = (ArgumentList) obj;
        if (!varargs && !other.varargs)
            return argTypes.equals(other.argTypes);
        return false; // varargs are not completely supported yet
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.argTypes);
        hash = 37 * hash + (this.varargs ? 1 : 0);
        return hash;
    }

}
