/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.io.*;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Main class for the Puzzle Solver.
 *
 * @author Theodore Dubois
 */
public class PuzzleSolver {

    private static boolean errors;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">>> ");
        String input = in.readLine();
        while (input != null) {
            errors = false;
            ANTLRInputStream inputStream = new ANTLRInputStream(input);
            PuzzleLexer lexer = new PuzzleLexer(inputStream);
            PuzzleParser parser = new PuzzleParser(new CommonTokenStream(lexer));
            ANTLRErrorListener errListener = new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1, String msg, RecognitionException re) {
                    System.out.println(msg);
                    errors = true;
                }

            };
            parser.addErrorListener(errListener);
            lexer.addErrorListener(errListener);
            ParseTree tree = parser.puzzle();
            ParseTreeWalker walker = new ParseTreeWalker();
            PuzzleParseListener listener = new PuzzleParseListener(errListener);
            walker.walk(listener, tree);
            if (errors == false) {
                Stream<String> dataStream = listener.getData().parallelStream();
                for (Transformation tx : listener.getTransformations())
                    dataStream = tx.transform(dataStream).unordered();
                dataStream.forEach(System.out::println);
            }
            System.gc(); // why not?
            System.out.print(">>> ");
            input = in.readLine();
        }
    }

}
