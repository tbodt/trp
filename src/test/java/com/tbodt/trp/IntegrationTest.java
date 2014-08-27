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

import java.util.Arrays;
import java.util.Optional;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Theodore Dubois
 */
public class IntegrationTest {
    @Test
    public void testCommands() {
        testCommand("\"hi\"", new String[] {"hi"});
        testCommand("[testCategory]", new String[] {"test", "another test", "yet another test"});
        testCommand("[testCategory]: countWords(1)", new String[] {"test"});
    }
    
    @Test
    public void testErrors() {
        testError("[noSuchCategory]");
        testError("\"\": noSuchFunction");
    }
    
    private void testCommand(String command, String[] expectedStrings) {
        WordSequence[] expected = Arrays.stream(expectedStrings).map(WordSequence::new).toArray(WordSequence[]::new);
        System.out.println(command);
        Optional<Stream<WordSequence>> optional = CommandProcessor.processCommand(command);
        System.out.println(optional.isPresent());
        WordSequence[] actual = optional.get().toArray(WordSequence[]::new);
        assertArrayEquals(expected, actual);
    }
    
    private void testError(String command) {
        assertFalse(CommandProcessor.processCommand(command).isPresent());
    }
}
