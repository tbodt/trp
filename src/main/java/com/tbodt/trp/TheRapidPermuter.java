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
        String input;
        while ((input = in.readLine()) != null) {
            if (input.equals("exit"))
                System.exit(0);
            // The most important line in the program!
            CommandProcessor.processCommand(input).ifPresent(data ->
                    data.forEach(System.out::println));
            System.gc(); // why not?
            System.out.print("trp> ");
        }
    }

    public static void reportError(String msg) {
        System.out.println(msg);
        errors = true;
    }
}
