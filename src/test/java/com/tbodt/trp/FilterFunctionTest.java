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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Theodore Dubois
 */
public class FilterFunctionTest {
    @Test
    public void testLength() {
        FilterFunction length = FilterFunction.forName("length");
        assertTrue(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(5))));
        assertTrue(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(5, 10))));
        assertTrue(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(0, 5))));
        assertTrue(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(5, 5))));
        assertFalse(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(8))));
        assertFalse(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(8, 9))));
        assertFalse(length.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(2, 3))));
        assertFalse(length.invoke(new WordSequence("hi and bye"), new ArgumentList(Arrays.asList(5))));
    }

    @Test
    public void testEndsWith() {
        FilterFunction endsWith = FilterFunction.forName("endsWith");
        assertTrue(endsWith.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("llo"))));
        assertFalse(endsWith.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("hel"))));
        assertFalse(endsWith.invoke(new WordSequence("hi and bye"), new ArgumentList(Arrays.asList("bye"))));
    }

    @Test
    public void testStartsWith() {
        FilterFunction startsWith = FilterFunction.forName("startsWith");
        assertTrue(startsWith.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("hel"))));
        assertFalse(startsWith.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("llo"))));
        assertFalse(startsWith.invoke(new WordSequence("hi and bye"), new ArgumentList(Arrays.asList("hi"))));
    }
    
    @Test
    public void testContains() {
        FilterFunction contains = FilterFunction.forName("contains");
        assertTrue(contains.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("ell"))));
        assertFalse(contains.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList("reo"))));
        assertFalse(contains.invoke(new WordSequence("hi and bye"), new ArgumentList(Arrays.asList("and"))));
    }
    
    @Test
    public void testCountWords() {
        FilterFunction countWords = FilterFunction.forName("countWords");
        assertTrue(countWords.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(1))));
        assertTrue(countWords.invoke(new WordSequence("hello goodbye"), new ArgumentList(Arrays.asList(2))));
        assertFalse(countWords.invoke(new WordSequence("hi and bye"), new ArgumentList(Arrays.asList(2))));
    }
    
    @Test
    public void testIn() {
        FilterFunction in = FilterFunction.forName("in");
        assertTrue(in.invoke(new WordSequence("hello"), new ArgumentList(Arrays.asList(Category.forName("words").stream()))));
        assertFalse(in.invoke(new WordSequence("not hello"), new ArgumentList(Arrays.asList(Category.forName("words").stream()))));
    }
    
    @Test
    public void testAny() {
        WordSequence testws = new WordSequence("word");
        FilterFunction any = FilterFunction.forName("any");
        assertTrue(any.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE))));
        assertTrue(any.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE, Filter.TRUE))));
        assertTrue(any.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE, Filter.FALSE))));
        assertFalse(any.invoke(testws, new ArgumentList(Arrays.asList(Filter.FALSE))));
        assertFalse(any.invoke(testws, new ArgumentList(Arrays.asList(Filter.FALSE, Filter.FALSE))));
    }
    
    @Test
    public void testAll() {
        WordSequence testws = new WordSequence("word");
        FilterFunction all = FilterFunction.forName("all");
        assertTrue(all.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE))));
        assertTrue(all.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE, Filter.TRUE))));
        assertFalse(all.invoke(testws, new ArgumentList(Arrays.asList(Filter.TRUE, Filter.FALSE))));
        assertFalse(all.invoke(testws, new ArgumentList(Arrays.asList(Filter.FALSE))));
        assertFalse(all.invoke(testws, new ArgumentList(Arrays.asList(Filter.FALSE, Filter.FALSE))));
    }
}
