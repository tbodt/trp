/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Main class for the Puzzle Solver.
 *
 * @author Theodore Dubois
 */
public class PuzzleSolver {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print(">>> ");
            String input = in.readLine();
            if (input == null) {
                break;
            }
            ANTLRInputStream inputStream = new ANTLRInputStream(input);
            PuzzleLexer lexer = new PuzzleLexer(inputStream);
            PuzzleParser parser = new PuzzleParser(new CommonTokenStream(lexer));
            ParseTree tree = parser.puzzle();
            ParseTreeWalker walker = new ParseTreeWalker();
            PuzzleParseListener listener = new PuzzleParseListener();
            walker.walk(listener, tree);
            System.out.println(Transformer.transform(listener.getData(), listener.getTransformations()));
        }
    }
}
