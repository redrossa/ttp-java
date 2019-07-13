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
public final class Packet implements Serializable
{
    /** Class serial version UID */
    private static final long serialVersionUID = 7474662397063358803L;

    /** Header value */
    final int header;

    /** Byte array UTF-8 encoding of a {@code String} value */
    final byte[] body;

    /** Unsigned 16-bit type to store further customisation of a {@code Packet} */
    final char footer;

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
     *
     * @param header the header value.
     * @param body the body value.
     * @param footer the footer value.
     * @see   Packet#Packet(int, byte[], char)
     */
    private Packet(@NotNull Headerable header, @NotNull Object body, int footer)
    {
        this(header.getMask(), body.toString().getBytes(StandardCharsets.UTF_8), (char) footer);
    }

    /**
     * Creates a {@code Packet} out of raw data specified.
     * <p>
     * Used by classes in the same package only, this constructor assumes arguments
     * passed by the caller are appropriate for constructing this packet object.
     * It is the responsibility of the caller to make sure the arguments passed
     * to this constructor are appropriate.
     *
     * @param header the header value.
     * @param body the body data.
     * @param footer the footer value.
     */
    Packet(int header, byte[] body, char footer)
    {
        if (body == null)
            throw new NullPointerException();
        this.header = header;
        this.body = body;
        this.footer = footer;
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
     * Returns a string representation of this {@code Packet}.
     * <p>
     * The string representation consists of this {@code Packet}'s fields: header value,
     * body in {@code String} format and footer value. The fields are enclosed in square
     * brackets ("{@code []}") and separated by "{@code / }" (space, forward slash, space).
     *
     * @return a string representation of this {@code Packet}.
     */
    @Override
    public String toString()
    {
        return String.format("[%03d / %s / %05d]",  header, new String(body, StandardCharsets.UTF_8), (int) footer);
    }

    /**
     * Returns the UTF-8-decoded {@code body} data byte array.
     * <p>
     * Encoding should never fail because {@code body} is always encoded in UTF-8. A failure
     * indicates an invalid creation of {@code Packet} outside of this package.
     *
     * @return the UTF-8-decoded {@code body} data byte array.
     */
    public String format()
    {
        return new String(body, StandardCharsets.UTF_8);
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
}
