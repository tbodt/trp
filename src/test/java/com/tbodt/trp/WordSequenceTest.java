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

import com.tbodt.trp.WordSequence.Word;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Theodore Dubois
 */
public class WordSequenceTest {
    @Test
    public void testMethods() {
        WordSequence ws = new WordSequence("hello");
        assertEquals(ws, new WordSequence(new Word("hello")));
        assertEquals(ws.toString(), "hello");
        assertEquals(ws.combine(), ws);
        assertEquals(ws.getWords(), Collections.singletonList(new Word("hello")));
        assertEquals(ws.count(), 1);
        int i = 0;
        for (Word word : ws) {
            assertEquals(word, new Word("hello"));
            assertEquals(word.toString(), "hello");
            assertEquals(word, ws.getWords().get(i));
            i++;
        }
        assertEquals(i, 1);
        assertEquals(ws.append(new Word("goodbye")), new WordSequence("hello goodbye"));
    }

    @Test
    public void testConstructors() {
        WordSequence[] shouldBeEqual = {
            new WordSequence("hello goodbye"),
            new WordSequence(Arrays.asList(new Word("hello"), new Word("goodbye"))),
            new WordSequence(Stream.of(new Word("hello"), new Word("goodbye"))),
            new WordSequence(new Word[] {new Word("hello"), new Word("goodbye")}),
        };
        for (WordSequence ws1 : shouldBeEqual)
            for (WordSequence ws2 : shouldBeEqual)
                assertEquals(ws1, ws2);
    }
}
