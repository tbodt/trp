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
import java.util.*;

/**
 * A category. Embodies a set of words in the category.
 */
public class Category {
    private final Set<WordSequence> items = new HashSet<>();
    private static final Map<String, Category> cache = new HashMap<>();

    private Category(String name) {
        if (Category.class.getResource(name) == null)
            throw new IllegalArgumentException("category " + name + " nonexistent");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(Category.class.getResourceAsStream(name)))) {
            String item;
            while ((item = in.readLine()) != null)
                items.add(new WordSequence(item));
        } catch (IOException ex) {
            throw new RuntimeException(ex); // those darn ol' checked exceptions are such a hassle
        }
    }

    /**
     * Returns the category with the given name.
     *
     * @param name the name
     * @return the category with the given name
     */
    public static Category forName(String name) {
        if (Category.class.getResource(name) == null)
            return null;
        if (cache.containsKey(name))
            return cache.get(name);
        Category c = new Category(name);
        cache.put(name, c);
        return c;
    }

    /**
     * Returns all items in the category.
     *
     * @return all items in the category
     */
    public Set<WordSequence> getItems() {
        return Collections.unmodifiableSet(items);
    }

}
