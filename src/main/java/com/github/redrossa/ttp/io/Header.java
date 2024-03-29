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

package com.github.redrossa.ttp.io;

/**
 * The {@code Header} class provides an implementation of the
 * {@link Headerable} interface. This class provides the standard
 * types of headers of a {@code Packet}. There are three categories:
 * operations, data types and responses. The operation category has
 * a mask of 0, the data types of 1 and the responses of 2. Programmers
 * may specify their own operations to which they parse themselves
 * in the body or footer, while using the operation type headers. There
 * are four standard types: boolean, integer, double and string. These
 * types are free from language-specific size limitations because
 * of the use of UTF-8 encoding in their delivery. Therefore, restrictions
 * depend on that UTF-8 byte encoding. The response type headers enable
 * for detailed binary communication between endpoints. Use them over
 * boolean type to distinguish between a {@code Packet} intended for
 * delivering usable data type and one for implementation-level error
 * handling.
 * <p>
 * This class provides an implementation of its own {@code valueOf} method
 * by calling the {@link Headerable#valueOf(int, Class)} and passing its
 * own class. This is intended to minimise the writing of calling the
 * {@code Headerable}'s {@code valueOf} method to find the header of this
 * class instance associated with a specified mask.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public enum Header implements Headerable
{
    /** No operation or placeholder */
    NOP(0, String.class),

    /** Allows for custom operation implementations */
    OP(1, String.class),

    /** Boolean type */
    BOOLEAN(100, Boolean.class),

    /** Integer type */
    INTEGER(101, Integer.class),

    /** Double type */
    DOUBLE(102, Double.class),

    /** String type */
    STRING(103, String.class),

    /** Packet type */
    PACKET(104, Packet.class),

    /** Binary false response */
    BAD(200, String.class),

    /** Binary true response */
    OK(201, String.class);

    /** The underlying mask */
    private final int mask;

    /** The class type of the content of this header */
    private final Class type;

    /**
     * The sole constructor of {@code Header}.
     *
     * @param mask the mask of this {@code Header}.
     */
    Header(int mask, Class type)
    {
        this.mask = mask;
        this.type = type;
    }

    /**
     * See the general contract of the {@code getMask}
     * method of {@code Headerable}.
     * <p>
     * Returns the unique mask of this header instance.
     *
     * @return the mask.
     */
    public final int getMask()
    {
        return mask;
    }

    /**
     * See the general contract of the {@code getType}
     * method of {@code Headerable}.
     * <p>
     * Returns the type of the content that resides with this header
     * in a {@code Packet}.
     *
     * @return the content type.
     */
    public final Class getType()
    {
        return type;
    }

    /**
     * Returns the {@code Header} instance associated with the specified mask.
     * If no instance is associated with the mask, null is returned.
     * <p>
     * This method simply performs
     * {@code Headerable.valueOf(mask, Header.class} and returns the
     * result. This is intended to minimise the writing of calling the
     * {@code Headerable}'s {@code valueOf} method to find the header of this
     * class instance associated with a specified mask.
     *
     * @param  mask an {@code int} value whose associated enum is
     *         to be returned.
     * @return the Header enum associated with {@code mask} or null
     *         if not found.
     * @see    Headerable#valueOf(int, Class)
     */
    public static Header valueOf(int mask)
    {
        return Headerable.valueOf(mask, Header.class);
    }
}
