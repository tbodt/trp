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
    private Object[] args = {
        "hello", // string
        3, // integer
        Collections.emptySet(), // set data
        Stream.empty(), // stream data
    };
    private ArgumentList aList = new ArgumentList(Arrays.asList(args));

    @Test
    public void testAccessors() {
        assertEquals(Arrays.asList(args), aList.arguments());
        assertEquals(args.length, aList.length());

        assertEquals("hello", aList.string(0));
        assertEquals("hello", aList.argument(0));
        assertEquals(3, aList.integer(1));
        assertEquals(3, aList.argument(1));
        assertEquals(Collections.emptySet(), aList.data(2));
        assertEquals(Collections.emptySet(), aList.argument(2));
        assertEquals(Collections.emptySet(), aList.data(3));
        assertEquals(Collections.emptySet(), aList.argument(3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStringFailure() {
        aList.string(1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIntegerFailureFailure() {
        aList.integer(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDataFailure() {
        aList.data(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexFailure() {
        aList.argument(1294);
    }
}
