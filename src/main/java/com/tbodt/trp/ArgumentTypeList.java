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
import java.util.stream.Collectors;

/**
 * A list of the types of arguments to a function.
 *
 * @author Theodore Dubois
 */
public final class ArgumentTypeList {
    private final List<ArgumentType> argTypes;
    private final boolean varargs; // the last argument may be repeated

    /**
     * Constructs an {@code ArgumentTypeList} from the given list of {@code ArgumentType}s.
     *
     * @param argTypes the types of arguments
     */
    public ArgumentTypeList(ArgumentType... argTypes) {
        this(Arrays.asList(argTypes));
    }

    /**
     * Constructs an {@code ArgumentTypeList} from the given list of {@code ArgumentType}s and
     * whether the last argument may be repeated zero or more times.
     *
     * @param argTypes the types of arguments
     * @param varargs whether the last argument may be repeated zero or more times
     */
    public ArgumentTypeList(boolean varargs, ArgumentType... argTypes) {
        this(Arrays.asList(argTypes), varargs);
    }

    /**
     * Constructs an {@code ArgumentTypeList} from the given list of {@code ArgumentType}s.
     *
     * @param argTypes the types of arguments
     */
    public ArgumentTypeList(List<ArgumentType> argTypes) {
        this(argTypes, false);
    }

    /**
     * Constructs an {@code ArgumentTypeList} from the given list of {@code ArgumentType}s and
     * whether the last argument may be repeated zero or more times.
     *
     * @param argTypes the types of arguments
     * @param varargs whether the last argument may be repeated zero or more times
     */
    public ArgumentTypeList(List<ArgumentType> argTypes, boolean varargs) {
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
        STRING,
        /**
         * A transformer argument type.
         */
        TRANSFORMER,
        /**
         * A filter argument type.
         */
        FILTER;

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
            else if (arg instanceof Filter)
                return FILTER;
            else if (arg instanceof Transformer)
                return TRANSFORMER;
            else
                throw new IllegalArgumentException("invalid argument type " + arg);
        }

    }

    /**
     * An argument list for the list of objects passed.
     *
     * @param args a list of objects
     * @return an argument list for the list of objects passed
     */
    public static ArgumentTypeList of(List<Object> args) {
        return new ArgumentTypeList(args.stream().map(ArgumentType::typeOf).collect(Collectors
                .toList()));
    }

    /**
     * An argument list for the {@code ArgumentList} of objects passed.
     *
     * @param args an {@code ArgumentList} of objects
     * @return an argument list for the {@code ArgumentList} of objects passed
     */
    public static ArgumentTypeList of(ArgumentList args) {
        return new ArgumentTypeList(args.stream().map(ArgumentType::typeOf).collect(Collectors
                .toList()));
    }

    /**
     * Returns the list of {@code ArgumentType}s that this {@code ArgumentTypeList} represents.
     *
     * @return the list of {@code ArgumentType}s that this {@code ArgumentTypeList} represents
     */
    public List<ArgumentType> getTypes() {
        return Collections.unmodifiableList(argTypes);
    }

    /**
     * Whether the {@code args} could be passed to a function with this as the argument descriptor.
     *
     * @param args the arguments
     * @return whether the {@code args} could be passed to a function with this as the argument
     * descriptor
     */
    public boolean matches(ArgumentList args) {
        List<ArgumentType> arguments = new ArrayList<>(args.getArgumentTypes().getTypes()); // this may be modified
        List<ArgumentType> descriptor = getTypes();
        if (varargs) {
            ArgumentType varargType = descriptor.get(descriptor.size() - 1);
            while (arguments.size() > 0 && arguments.get(arguments.size() - 1).equals(varargType))
                arguments.remove(arguments.size() - 1);
            arguments.add(varargType);
        }
        if (arguments.size() != descriptor.size())
            return false;
        for (int i = 0; i < arguments.size(); i++)
            if (arguments.get(i) == ArgumentType.FILTER && descriptor.get(i) == ArgumentType.TRANSFORMER)
                arguments.set(i, ArgumentType.TRANSFORMER);
        return arguments.equals(descriptor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ArgumentTypeList other = (ArgumentTypeList) obj;
        if (!Objects.equals(this.argTypes, other.argTypes))
            return false;
        return this.varargs == other.varargs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }
}
