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
import java.util.Arrays;
import org.apache.commons.cli.*;

/**
 * Main class for The Rapid Permuter.
 *
 * @author Theodore Dubois
 */
@SuppressWarnings("static-access") // the builder for the commons cli is poorly designed
public class TheRapidPermuter {

    private static final Option command = OptionBuilder
            .withLongOpt("command")
            .hasArg()
            .withDescription("Command to execute")
            .create('c');
    private static final Option timing = OptionBuilder
            .withLongOpt("timing")
            .withDescription("Time command executions in milliseconds")
            .create('t');
    private static final Option help = OptionBuilder
            .withLongOpt("help")
            .withDescription("Display command line option help")
            .create();
    private static final Options options = new Options()
            .addOption(command)
            .addOption(timing)
            .addOption(help);
    private static CommandLine cmd;

    /**
     * Main method for The Rapid Permuter.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            cmd = new BasicParser().parse(options, args);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        if (Arrays.asList(cmd.getOptions()).contains(help)) { // another way commons cli is severely broken
            new HelpFormatter().printHelp("trp", options);
            return;
        }

        if (cmd.hasOption('c'))
            doCommand(cmd.getOptionValue('c'));
        else {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("trp> ");
            String input;
            while ((input = in.readLine()) != null) {
                if (input.equals("exit"))
                    return; // exit
                doCommand(input);
                System.out.print("trp> ");
            }
        }
    }

    private static void doCommand(String command) {
        long before = System.nanoTime();

        // The most important line in the program!
        CommandProcessor.processCommand(command).ifPresent(data
                -> data.forEach(System.out::println));

        long after = System.nanoTime();
        if (cmd.hasOption('t'))
            System.out.println((after - before) / 1_000_000 + " ms");
        System.gc(); // why not?
    }
}
