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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A {@code Packet} provides for organising data and extending its customisability.
 * In a packet, data are structured into three components: header, body and footer.
 * <p>
 * The header is a 32-bit integer value that indicates what type of packet this is
 * and what can be generalised about the data that follows: the body.
 * <p>
 * The body is the main, information-rich data component. This is where the data which the
 * programmer intends to deliver is stored.
 * <p>
 * Like the header, the footer is another value that holds information about the packet
 * and essentially the body. But unlike the header, which is restricted to implementations
 * of {@link Headerable}, the footer provides for higher customisability, where the programmer
 * specifies and parses the value in their own programmes themselves.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public final class Packet implements Serializable, Comparable<Packet>
{
    /** Class serial version UID */
    private static final long serialVersionUID = 7474662397063358803L;

    /** Header value */
    final int header;

    /** Byte array UTF-8 encoding of a {@code String} value */
    final byte[] body;

    /** Unsigned 16-bit type to store further customisation of a {@code Packet} */
    final char footer;

    /** Size of this packet in bytes */
    final int size;

    /** Default constructor creates a no-operation {@code Packet}. */
    public Packet()
    {
        this(Header.NOP, "", 0);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#BOOLEAN} and default {@code 0} footer.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @see   Packet#Packet(boolean, int)
     */
    public Packet(boolean val)
    {
        this(Header.BOOLEAN, val, 0);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#BOOLEAN} and footer specified.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @param footer the footer value
     * @see   Packet#Packet(Headerable, Object, int)
     */
    public Packet(boolean val, int footer)
    {
        this(Header.DOUBLE, val, footer);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#INTEGER} and default {@code 0} footer.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @see   Packet#Packet(int, int)
     */
    public Packet(int val)
    {
        this(val, 0);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#INTEGER} and footer specified.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @param footer the footer value
     * @see   Packet#Packet(Headerable, Object, int)
     */
    public Packet(int val, int footer)
    {
        this(Header.DOUBLE, val, footer);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#DOUBLE} and default {@code 0} footer.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @see   Packet#Packet(double, int)
     */
    public Packet(double val)
    {
        this(val, 0);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#DOUBLE} and footer specified.
     * <p>
     * The {@code double}
     * value is converted to {@code String} to be encoded in UTF-8.
     *
     * @param val the {@code double} value for the body.
     * @param footer the footer value
     * @see   Packet#Packet(Headerable, Object, int)
     */
    public Packet(double val, int footer)
    {
        this(Header.DOUBLE, val, footer);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#STRING} and default {@code 0} footer.
     * <p>
     * The {@code String}
     * value is to be encoded in UTF-8.
     *
     * @param val the {@code String} value for the body.
     * @see   Packet#Packet(String, int)
     */
    public Packet(@NotNull String val)
    {
        this(val, 0);
    }

    /**
     * Creates a {@code Packet} of type {@link Header#STRING} and footer specified.
     * <p>
     * The {@code String}
     * value is to be encoded in UTF-8.
     *
     * @param val the {@code String} value for the body.
     * @param footer the footer value.
     * @see   Packet#Packet(Headerable, Object, int)
     */
    public Packet(@NotNull String val, int footer)
    {
        this(Header.STRING, val, footer);
    }

    /**
     * Creates a {@code Packet} out of complex objects specified.
     * <p>
     * Used by classes outside of this package, this constructor makes sure
     * the would-be header value is appropriate, either it is a {@link Header}
     * enum or it is a custom instance of a class implementing {@link Headerable}.
     * This constructor also makes sure that the data body of this packet is
     * encoded in UTF-8 encoding, by encoding the data body in this constructor
     * itself rather than by the caller. Agreeing to the contract, a packet body
     * must always be encoded in UTF-8.
     * <p>
     * If {@code header} is {@code null}, this packet is simply a {@link Header#NOP}.
     * If {@code body} is {@code null}, the body will be assigned a zero-length byte array.
     *
     * @param header the header value.
     * @param body the body value.
     * @param footer the footer value.
     * @see   Packet#Packet(int, byte[], char)
     */
    public Packet(Headerable header, Object body, int footer)
    {
        this(header == null ? 0 : header.getMask(),
             body == null ? null : body.toString().getBytes(StandardCharsets.UTF_8),
             (char) footer);
    }

    /**
     * Creates a {@code Packet} out of raw data specified.
     * <p>
     * Used by classes in the same package only, this constructor assumes arguments
     * passed by the caller are appropriate for constructing this packet object.
     * It is the responsibility of the caller to make sure the arguments passed
     * to this constructor are appropriate.
     * <p>
     * If {@code header} is {@code 0}, this packet is simply a {@link Header#NOP}.
     * If {@code body} is {@code null}, the body will be assigned a zero-length byte array.
     *
     * @param header the header value.
     * @param body the body data.
     * @param footer the footer value.
     */
    Packet(int header, byte[] body, char footer)
    {
        if (body == null)
            body = new byte[0];
        this.header = header;
        this.body = body;
        this.footer = footer;
        this.size = 4 + body.length + 2;    // size of int header + byte[] body + char footer
    }

    /**
     * Returns the hashcode customised for this {@code Packet}.
     * <p>
     * The hashcode
     * depends on the fields of this {@code Packet}.
     *
     * @return hashcode of this {@code Packet}.
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + header;
        result = 31 * result + Arrays.hashCode(body);
        result = 31 * result + footer;
        return result;
    }

    /**
     * Indicates whether this {@code Packet} is "equal" to {@code obj}. This method
     * is overridden to take in consideration of the fields of this {@code Packet}.
     *
     * @param  obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     *         argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Packet))
            return false;
        Packet tmp = (Packet) obj;
        return tmp.header == header && Arrays.equals(tmp.body, body) && tmp.footer == footer;
    }

    /**
     * Compares this {@code Packet} with the specified {@code Packet} for order.
     * Returns a negative integer, zero, or a positive integer as this {@code Packet}
     * is less than, equal to, or greater than the specified {@code Packet}.
     * <p>
     * This method compares the header, body and footer values of this {@code Packet}
     * and the specified {@code Packet}. This method subtracts the header and footer
     * values of the specified {@code Packet} from their similar respective fields
     * of this {@code Packet}. The body of both {@code Packet}s are compared
     * lexicographically by performing {@code Arrays.compare(body, o.body)}. The
     * results of all calculations of the three fields are then summed up and returned;
     *
     * @param  o the {@code Packet} to be compared.
     * @return a negative integer, zero, or a positive integer as this {@code Packet}
     *         is less than, equal to, or greater than the specified {@code Packet}.
     * @see    Arrays#compare(byte[], byte[])
     */
    @Override
    public int compareTo(@NotNull Packet o)
    {
        int h = header - o.header;
        int b = Arrays.compare(body, o.body);
        int f = footer - o.footer;
        return h + b + f;
    }

    /**
     * Returns a string representation of this {@code Packet}.
     * <p>
     * The string representation consists of this {@code Packet}'s fields: header value,
     * body in {@code String} format and footer value. The fields are enclosed in square
     * brackets ("{@code []}") and separated by "{@code :}" (colon). The header value is
     * always formatted in three digits and the footer is always formatted in five digits.
     * Therefore, the string representation of a {@code Packet} is always at least 12
     * characters long.
     *
     * @return a string representation of this {@code Packet}.
     */
    @Override
    public String toString()
    {
        return String.format("[%03d:%s:%05d]",  header, new String(body, StandardCharsets.UTF_8), (int) footer);
    }

    /**
     * Returns the {@code header} value.
     *
     * @return the {@code header} value.
     */
    public int getHeader()
    {
        return header;
    }

    /**
     * Returns the UTF-8 encoded {@code body} data byte array.
     *
     * @return the UTF-8 encoded {@code body} data byte array.
     */
    public byte[] getBody()
    {
        return body;
    }

    /**
     * Returns the {@code footer} value.
     *
     * @return the {@code footer} value.
     */
    public char getFooter()
    {
        return footer;
    }

    /**
     * Returns the total size of all fields in bytes.
     *
     * @return the {@code size}.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns a {@code Packet} object holding the value extracted from the specified
     * string when parsed with the {@code Header} type for the header mask.
     * <p>
     * The result is a {@code Packet} object that represents the packet value specified
     * by the string. The string must follow the {@code Packet} pattern as represented
     * by {@link Packet#toString()}.
     * <p>
     * This method simply performs {@code valueOf(s, Header.class)} and returns
     * the result.
     *
     * @param  s the {@code String} containing the {@code Packet} representation
     *         to be parsed.
     * @return the {@code Packet} represented by the {@code String} argument and
     *         {@link Header} type header mask.
     * @throws IllegalArgumentException if the specified {@code String} does
     *         not contain a parsable {@code Packet} with a {@code Header} type
     *         header mask.
     */
    public static Packet valueOf(@NotNull String s)
    {
        return valueOf(s, Header.class);
    }

    /**
     * Returns a {@code Packet} object holding the value extracted from the specified
     * string when parsed with the {@code Headerable} given by the second argument.
     * <p>
     * The result is a {@code Packet} object that represents the packet value specified
     * by the string. The string must follow the {@code Packet} pattern as represented
     * by {@link Packet#toString()}.
     * <p>
     * The {@code Headerable} argument must implement its own {@code valueOf} static method, as
     * this method calls it through reflection to validate the header mask. All exceptions
     * thrown through this reflection is either wrapped around {@code IllegalArgumentException}
     * and rethrown or, like {@code IllegalAccessException}, ignored because overloaded valueOf
     * static method is always implemented as public.
     *
     * @param  s the {@code String} containing the {@code Packet} representation
     *         to be parsed.
     * @param  headerable the {@code Headerable} to validate the header mask.
     * @return the {@code Packet} represented by the {@code String} argument in the
     *         specified {@code Headerable}.
     * @throws IllegalArgumentException if the specified {@code String} does
     *         not contain a parsable {@code Packet} or the specified {@code Headerable}
     *         does not implement its own {@code valueOf} static method.
     */
    public static Packet valueOf(@NotNull String s, @NotNull Class<? extends Headerable> headerable)
    {
        Headerable header = null;
        String body;
        int headerMask, footer, len = s.length();

        if (len < 12)
            throw new IllegalArgumentException(s);
        if (!(s.startsWith("[") && s.endsWith("]")))        // check enclosing brackets
            throw new IllegalArgumentException(s);
        s = s.substring(1, s.length()-1);
        try                                                 // check header and footer
        {
            headerMask = Integer.valueOf(s.substring(0, 3));
            footer = Integer.valueOf(s.substring(s.length()-5));
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException(s);
        }
        s = s.substring(3, s.length()-4);
        if (!(s.startsWith(":") && s.endsWith(":")))        // check colons
            throw new IllegalArgumentException(s);
        s = s.substring(1, s.length()-1);
        body = s;

        try
        {
            header = (Headerable) headerable.getMethod("valueOf", int.class).invoke(null, headerMask);
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("valueOf static method not implemented by " + headerable.getName());
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException(e.getCause());
        }
        catch (IllegalAccessException ignored) { }

        return new Packet(header, body, footer);
    }

    /**
     * Returns the object type of the specified {@code Packet} body
     * according to its header. Requires explicit conversion by caller.
     *
     * @param  p the {@code Packet} to format.
     * @return the object type of the specified {@code Packet} body.
     */
    public static Object format(@NotNull Packet p)
    {
        String valStr = new String(p.getBody(), StandardCharsets.UTF_8);
        switch (Header.valueOf(p.header))
        {
            case BOOLEAN:
                return Boolean.valueOf(valStr);
            case INTEGER:
                return Integer.valueOf(valStr);
            case DOUBLE:
                return Double.valueOf(valStr);
            default:
                return valStr;
        }
    }
}
