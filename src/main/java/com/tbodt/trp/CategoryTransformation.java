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

/**
 * A transformation that filters out words not in a category.
 */
public class CategoryTransformation implements Filter {
    private final Category category;

    /**
     * Constructs a {@code CategoryTransformation} that filters out words not in {@code category}.
     * @param category a category
     */
    public CategoryTransformation(Category category) {
        this.category = category;
    }

    @Override
    public boolean test(WordSequence ws) {
        return category.getItems().contains(ws);
    }
}
