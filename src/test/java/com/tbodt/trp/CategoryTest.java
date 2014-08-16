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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Theodore Dubois
 */
public class CategoryTest {
    private int i; // unfortunately needed
    
    @Test
    public void testCategory() {
        Category cat = Category.forName("words");
        Set<WordSequence> fromGetItems = cat.getItems();
        Set<WordSequence> fromStream = cat.stream().collect(Collectors.toSet());
        if (!fromGetItems.equals(fromStream)) {
            System.err.println("getItems and stream didn't give the same result");
            System.err.println("in getItems but not stream:");
            Set<WordSequence> intersection = new HashSet<>(fromStream);
            intersection.retainAll(fromGetItems);
            System.err.println(intersection);
            System.err.println("in stream but not getItems:");
            intersection = new HashSet<>(fromGetItems);
            intersection.retainAll(fromStream);
            fail();
        }
    }
}
