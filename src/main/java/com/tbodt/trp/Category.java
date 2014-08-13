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
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A category. Embodies a set of words in the category.
 */
public class Category {
    private Set<WordSequence> items;
    private final Supplier<BufferedReader> in;
    private static final Map<String, Category> cache = new HashMap<>();

    private Category(String name) {
        Supplier<InputStream> stream;
        if (Category.class.getResource(name) != null)
            stream = () -> Category.class.getResourceAsStream(name);
        else {
            File file = new File(name);
            if (!file.exists())
                throw new IllegalArgumentException("category " + name + " nonexistent");
            stream = () -> {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    throw new AssertionError(); // we checked if the file exists!
                }
            };
        }
        in = () -> new BufferedReader(new InputStreamReader(stream.get()));
    }

    /**
     * Returns the category with the given name.
     *
     * @param name the name
     * @return the category with the given name
     */
    public static Category forName(String name) {
        if (Category.class.getResource(name) == null && !new File(name).exists())
            return null;
        if (cache.containsKey(name))
            return cache.get(name);
        Category c = new Category(name);
        cache.put(name, c);
        return c;
    }

    /**
     * Returns a stream of the items in this category. If you can, use this instead of
     * {@code getItems().stream()}, because, if possible, this doesn't slurp up the whole file.
     *
     * @return a stream of the items in this category
     */
    public Stream<WordSequence> stream() {
        Stream<WordSequence> retval = in.get().lines().filter(line ->
                line.chars().allMatch(ch -> Character.isLetter(ch) || Character.isWhitespace(ch)))
                .map(WordSequence::new);
        return retval;
    }

    /**
     * Returns all items in the category.
     *
     * @return all items in the category
     */
    public Set<WordSequence> getItems() {
        if (items == null)
            slurpFile();
        return Collections.unmodifiableSet(items);
    }

    private void slurpFile() {
        items = new HashSet<>();
        BufferedReader br = in.get();
        try {
            String item;
            while ((item = br.readLine()) != null)
                if (item.chars()
                        .allMatch(ch -> Character.isLetter(ch) || Character.isWhitespace(ch)))
                    items.add(new WordSequence(item));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex); // those darn ol' checked exceptions are such a hassle
        }
    }
}
