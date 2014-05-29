/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.io.*;
import java.util.*;

/**
 * A category. Embodies a set of words in the category.
 */
public class Category {

    private final Set<String> items = new HashSet<>();

    private static final Map<String, Category> cache = new HashMap<>();

    private Category(String name) throws IOException {
        if (Category.class.getResource(name) == null) {
            throw new IllegalArgumentException("category " + name + " nonexistent");
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(Category.class.getResourceAsStream(name)))) {
            String item;
            while ((item = in.readLine()) != null) {
                items.add(item);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex); // those darn ol' checked exceptions are such a hassle
        }
    }

    public static Category forName(String name) throws IOException {
        if (Category.class.getResource(name) == null)
            return null;
        if (cache.containsKey(name))
            return cache.get(name);
        Category c = new Category(name);
        cache.put(name, c);
        return c;
    }

    public Set<String> getItems() {
        return Collections.unmodifiableSet(items);
    }
}
