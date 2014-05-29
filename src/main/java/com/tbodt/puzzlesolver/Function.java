/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbodt.puzzlesolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Theodore Dubois
 */
public class Function {
    private final Method method;
    private static final Map<String, Function> cache = new HashMap<>();

    private Function(String name) throws NoSuchMethodException {
        Method[] methods = Function.class.getMethods();
        Method mt = null;
        for (Method m : methods) {
            if (!Modifier.isStatic(m.getModifiers()))
                continue;
            if (Modifier.isPrivate(m.getModifiers()))
                continue;
            if (m.getName().equals("forNameAndArgs"))
                continue;
            if (m.getName().equals(name))
                mt = m;
        }
        if (mt != null)
            method = mt;
        else
            throw new NoSuchMethodException(name);
    }

    public static Function forNameAndArgs(String name) {
        if (cache.containsKey(name))
            return cache.get(name);
        try {
            Function f = new Function(name);
            cache.put(name, f);
            return f;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

}
