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

import java.io.*;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Main class for The Rapid Permuter.
 *
 * @author Theodore Dubois
 */
public class TheRapidPermuter {

    private static boolean errors;

    /**
     * Main method for The Rapid Permuter.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("trp> ");
        String input = in.readLine();
        while (input != null) {
            if (input.equals("exit"))
                System.exit(0);
            errors = false;
            ANTLRInputStream inputStream = new ANTLRInputStream(input);
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
            Stream<WordSequence> dataStream = (Stream<WordSequence>) visitor.visit(tree);
            if (errors == false) {
                dataStream.forEach(System.out::println);
            }
            System.gc(); // why not?
            System.out.print("trp> ");
            input = in.readLine();
        }
    }

    public static void reportError(String msg) {
        System.out.println(msg);
        errors = true;
    }
}
