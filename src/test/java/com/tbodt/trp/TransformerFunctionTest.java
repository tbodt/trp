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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Theodore Dubois
 */
public class TransformerFunctionTest {
    @Test
    public void testAnagram() {
        TransformerFunction anagram = TransformerFunction.forName("anagram");
        doSingleTest(anagram, new String[] {"ab"}, new String[] {"ab", "ba"});
        doSingleTest(anagram, new String[] {"a"}, new String[] {"a"});
        doSingleTest(anagram, new String[] {"ab cd"}, new String[] {"ab cd", "ba cd", "ab dc", "ba dc"});
        doSingleTest(anagram, new String[] {"abc"}, new String[] {"abc", "acb", "bac", "bca", "cab", "cba"});
    }
    
    @Test
    public void testSplitWords() {
        TransformerFunction splitWords = TransformerFunction.forName("splitWords");
        doSingleTest(splitWords, new String[] {"ab"}, new String[] {"a b"}, 1, 1);
        doSingleTest(splitWords, new String[] {"abc"}, new String[] {}, 2);
    }
    
    @Test
    public void testRemove() {
        TransformerFunction remove = TransformerFunction.forName("remove");
        doSingleTest(remove, new String[] {"ab"}, new String[] {"b"}, "a");
        doSingleTest(remove, new String[] {"abc"}, new String[] {"a"}, "bc");
        doSingleTest(remove, new String[] {"abc"}, new String[] {"abc"}, "ois");
    }
    
    @Test
    public void testAppend() {
        TransformerFunction append = TransformerFunction.forName("append");
        doSingleTest(append, new String[] {"ab"}, new String[] {"abc"}, "c");
    }
    
    private static void doSingleTest(TransformerFunction fn, String[] input, String[] expectedOutput, Object... args) {
        Stream<WordSequence> in = Arrays.stream(input).map(WordSequence::new);
        Stream<WordSequence> out = Arrays.stream(expectedOutput).map(WordSequence::new);
        Set<WordSequence> expected = fn.invoke(in, new ArgumentList(Arrays.asList(args))).collect(Collectors.toSet());
        Set<WordSequence> actual = out.collect(Collectors.toSet());
        assertEquals(expected, actual);
    }
}
