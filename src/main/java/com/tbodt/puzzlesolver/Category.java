/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * A category. Embodies a set of words in the category.
 */
public class Category implements Set<String> {
    private Set<String> elements;
    
    private static final String CATEGORY_FILES_PROPERTY = "com.tbodt.puzzlesolver.categoryFilesPath";
    private static final String CATEGORY_FILES;
    static {
        if (System.getProperty(CATEGORY_FILES_PROPERTY) != null)
            CATEGORY_FILES = System.getProperty(CATEGORY_FILES_PROPERTY);
        else
            CATEGORY_FILES = "categories";
    }
    
    private Category(String name) throws IOException {
        elements = new HashSet<String>(Files.readAllLines(Paths.get(CATEGORY_FILES, name)));
    }

    public static Category forName(String name) throws IOException {
        return new Category(name);
    }

    @Override public int size() {return elements.size();}
    @Override public boolean isEmpty() {return elements.isEmpty();}
    @Override public boolean contains(Object o) {return elements.contains(o);}
    @Override public Iterator<String> iterator() {return elements.iterator();}
    @Override public Object[] toArray() {return elements.toArray();}
    @Override public <T> T[] toArray(T[] a) {return elements.toArray(a);}
    @Override public boolean add(String e) {return elements.add(e);}
    @Override public boolean remove(Object o) {return elements.remove(o);}
    @Override public boolean containsAll(Collection<?> c) {return elements.containsAll(c);}
    @Override public boolean addAll(Collection<? extends String> c) {return elements.addAll(c);}
    @Override public boolean retainAll(Collection<?> c) {return elements.retainAll(c);}
    @Override public boolean removeAll(Collection<?> c) {return elements.removeAll(c);}
    @Override public void clear() {elements.clear();}
    @Override public boolean equals(Object o) {return elements.equals(o);}
    @Override public int hashCode() {return elements.hashCode();}
    @Override public Spliterator<String> spliterator() {return elements.spliterator();}
}
