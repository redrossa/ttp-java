/*
 * MIT License
 *
 * Copyright (c) 2019 Adriano Raksi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.redrossa.ttp;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This interface provides the usability of an instance class or
 * enum as a header of a {@link Packet}. Implementation of this
 * interface must declare a field in their instances that holds a
 * unique {@code int} value called a mask. Ideally, the instances
 * must be static and their respective masks unique from other
 * masks of other instances of that same class or enum. Additionally,
 * the class or enum must provide an instance implementation of the
 * {@link Headerable#getMask()} which returns its mask value.
 * <p>
 * A mask, in decimal, is composed of three digits. The first digit
 * indicates the category and the last two indicates the value of
 * that specific header. So in one category, there can only be a
 * maximum of 100 types of headers (from X00 to X99 inclusive), and
 * in a {@code Headerable} implementation, there can only be a maximum
 * of 10 types of categories (from 0XX to 9XX inclusive). This
 * information of organising into categories is not, however, stored
 * by the {@code Packet}, not to mention the class of the implementation
 * of this interface. Therefore, it is up to the programmer to decode
 * into what type of {@code Headerable} implementation is the
 * {@code Packet} delivering.
 * <p>
 * The {@link Headerable#valueOf(int, Class)} method allows the programmer
 * to retrieve which instance in the specified {@code Headerable} class
 * implementation is the header of the {@code Packet}, especially since
 * the header information is stored in the {@code Packet} raw (in
 * {@code int}). It is recommended for classes or enums implementing this
 * interface to provide an implementation of their own {@code valueOf} method
 * which may call {@code Headerable}'s {@code valueOf} method that passes its
 * own class and returns the result.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-29
 * @see     Header
 */
public interface Headerable
{
    /**
     * Returns the unique mask of this header instance.
     *
     * @return the mask.
     */
    int getMask();

    /**
     * Returns the header instance associated with the specified mask
     * of the specified class that implements this interface. If no
     * header instance in the specified class is associated with the mask,
     * null is returned.
     * <p>
     * It is recommended for classes or enums implementing this
     * interface to provide an implementation of their own {@code valueOf} method
     * which may call this method that passes its own class and returns the result.
     *
     * @param  mask the mask to find with which a header is associated.
     * @param  clazz the class that implements this interface.
     * @return the header instance associated with the specified mask
     *         or null if not found.
     */
    static <T extends Headerable> T valueOf(int mask, @NotNull Class<T> clazz)
    {
        return Arrays.stream(clazz.getEnumConstants())
                     .filter(header -> header.getMask() == mask)
                     .findFirst()
                     .orElse(null);
    }
}
