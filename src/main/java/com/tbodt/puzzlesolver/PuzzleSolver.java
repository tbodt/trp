/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
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
        while (true) {
            System.out.print(">>> ");
            String input = in.readLine();
            if (input == null) {
                break;
            }
            errors = false;
            ANTLRInputStream inputStream = new ANTLRInputStream(input);
            PuzzleLexer lexer = new PuzzleLexer(inputStream);
            PuzzleParser parser = new PuzzleParser(new CommonTokenStream(lexer));
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int i, int i1, String string, RecognitionException re) {
                    errors = true;
                }
            });
            ParseTree tree = parser.puzzle();
            ParseTreeWalker walker = new ParseTreeWalker();
            PuzzleParseListener listener = new PuzzleParseListener();
            walker.walk(listener, tree);
            if (errors == false) {
                System.out.println(transform(listener.getData(), listener.getTransformations()));
            }
        }
    }

    public static Set<String> transform(Set<String> data, List<Transformation> transformations) {
        for (Transformation tx : transformations) {
            Set<String> newData = new HashSet<>();
            for (String datum : data) {
                newData.addAll(tx.transform(datum));
            }
            data = newData;
        }
        return data;
    }
}
