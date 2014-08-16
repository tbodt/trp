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

import java.util.Collections;
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
        assertEquals(ws.toString(), "hello");
        assertEquals(ws.combine(), ws);
        assertEquals(ws.getWords(), Collections.singletonList(new WordSequence.Word("hello")));
        assertEquals(ws.count(), 1);
        int i = 0;
        for (WordSequence.Word word : ws) {
            i++;
            assertEquals(word, new WordSequence.Word("hello"));
            assertEquals(word.toString(), "hello");
            assertEquals(word, ws.getWords().get(i));
        }
        assertEquals(ws.append(new WordSequence.Word("goodbye")), new WordSequence("hello goodbye"));
    }
}
