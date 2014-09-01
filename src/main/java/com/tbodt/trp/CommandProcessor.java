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

import java.util.Optional;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A class with static methods that process a TRP command.
 *
 * @author Theodore Dubois
 */
public final class CommandProcessor {
    private static boolean errors;

    /**
     * Process a TRP command. If the command succeeded, returns the result in a {@code Optional<Stream<WordSequence>>}. If the command failed, returns
     * an empty optional.
     *
     * @param command the command
     * @return If the command succeeded, returns the result in a {@code Optional<Stream<WordSequence>>}. If the command failed, returns an empty
     * optional.
     */
    public static Optional<Stream<WordSequence>> processCommand(String command) {
        errors = false;
        ANTLRInputStream inputStream = new ANTLRInputStream(command);
        CommandLexer lexer = new CommandLexer(inputStream);
        CommandParser parser = new CommandParser(new CommonTokenStream(lexer));
        ANTLRErrorListener errListener = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1, String msg, RecognitionException re) {
                reportError(msg);
            }
        };
        parser.removeErrorListeners();
        parser.addErrorListener(errListener);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errListener);
        ParseTree tree = parser.command();
        CommandParseVisitor visitor = new CommandParseVisitor();
        @SuppressWarnings("unchecked")
        Stream<WordSequence> dataStream = (Stream<WordSequence>) visitor.visit(tree);
        if (errors == false)
            return Optional.of(dataStream);
        else
            return Optional.empty();
    }

    /**
     * Prints out the error message and causes the command being processed to fail.
     *
     * @param error the error message
     */
    public static void reportError(String error) {
        System.err.println(error);
        errors = true;
    }
}
