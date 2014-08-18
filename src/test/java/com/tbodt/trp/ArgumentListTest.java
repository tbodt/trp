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

import java.util.*;
import java.util.stream.Stream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Theodore Dubois
 */
public class ArgumentListTest {

    @Test
    public void testAccessors() {
        Object[] args = {
            "hello", // string
            3, // integer
            "string data", // string data
            Collections.emptySet(), // set data
            Stream.empty(), // stream data
        };
        ArgumentList aList = new ArgumentList(Arrays.asList(args));
        
        assertEquals("hello", aList.string(0));
        assertEquals("hello", aList.argument(0));
        assertEquals(3, aList.integer(1));
        assertEquals(3, aList.argument(1));
        assertEquals(Collections.singleton(new WordSequence("string data")), aList.data(2));
        assertEquals(Collections.singleton(new WordSequence("string data")), aList.argument(2));
        assertEquals(Collections.emptySet(), aList.data(3));
        assertEquals(Collections.emptySet(), aList.argument(3));
        assertEquals(Collections.emptySet(), aList.data(4));
        assertEquals(Collections.emptySet(), aList.argument(4));
    }
}
