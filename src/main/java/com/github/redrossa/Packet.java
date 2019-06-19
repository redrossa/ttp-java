package com.github.redrossa;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * In TTP, data are transferred into packets. A packet is structured into
 * two main components: header and body. There are four data types in TTP;
 * they are boolean, integer, double and string. These data types are stored
 * in byte arrays using the representation of their respective String UTF-8
 * values. The body component of a packet holds this byte array, while the
 * header component indicates which of the four TTP data types does the body
 * hold. The header is an {@code int} mask of a Header enum.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public class Packet implements Serializable
{
    /** Class serial version UID */
    private static final long serialVersionUID = 7474662397063358803L;

    /** Mask of the Header enum of this packet */
    private Header header;

    /** Byte array representation of the the String value of the accepted data type */
    private byte[] body;

    /** Unsigned 16-bit type to store further customisation of a packet */
    private char footer;

    /**
     * Creates a new Packet object of type boolean and default {@code 0} footer.
     *
     * @param   val {@code boolean} value of the data of the packet.
     */
    public Packet(boolean val)
    {
        this(Header.BOOLEAN, String.valueOf(val), '\0');
    }

    /**
     * Creates a new Packet object of type integer and default {@code 0} footer.
     *
     * @param   val {@code int} value of the data of the packet.
     */
    public Packet(int val)
    {
        this(Header.INTEGER, String.valueOf(val), '\0');
    }

    /**
     * Creates a new Packet object of type double and default {@code 0} footer.
     *
     * @param   val {@code double} value of the data of the packet.
     */
    public Packet(double val)
    {
        this(Header.DOUBLE, String.valueOf(val), '\0');
    }

    /**
     * Creates a new Packet object of type string and default {@code 0} footer.
     *
     * @param   val {@code String} value of the data of the packet.
     */
    public Packet(@NotNull String val)
    {
        this(Header.STRING, val, '\0');
    }

    /**
     * Creates a new Packet object of type {@code header}, of raw byte array data
     * {@code body} and of unsigned 16-bit {@code footer} specified. This
     * constructor guarantees the header mask specified of this packet exists and
     * that the body is universally encoded.
     *
     * @param   header  Header object to indicate the packet type.
     * @param   body    String body, which will be encoded into bytes. This way,
     *                  the byte encoding of the body is only of UTF-8.
     * @param   footer  extra minimal information of the packet.
     */
    @Contract(value = "null, _, _ -> fail; !null, null, _ -> fail", pure = true)
    public Packet(Header header, String body, char footer)
    {
        if (header == null || body == null)
            throw new NullPointerException();
        this.header = header;
        this.body = body.getBytes(StandardCharsets.UTF_8);
        this.footer = footer;
    }

    /**
     * Returns the header of this packet.
     * <p>
     * It is highly advisable to override this method for a subclass of the
     * Packet class and a subclass of the Header enum.
     *
     * @return  Header enum type associated with {@code mask}.
     */
    public final Header header()
    {
        return header;
    }

    /**
     * Returns the body of this packet.
     *
     * @return  byte array of this packet body.
     */
    public final byte[] body()
    {
        return body;
    }

    /**
     * Returns the footer of this packet.
     *
     * @return unsigned 16-bit data of this packet footer.
     */
    public final char footer()
    {
        return footer;
    }

    /**
     * Returns the hashcode customised for class Packet. The hashcode
     * of Packet depends on {@code header} and the hashcode of array
     * {@code body}.
     *
     * @return  hashcode of this packet.
     * @see     Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + header.hashCode();
        result = 31 * result + Arrays.hashCode(body);
        result = 31 * result + footer;
        return result;
    }

    /**
     * Indicates whether this packet is "equal" to {@code obj}. This method
     * is overridden to take in consideration of the instances of this Packet
     * object.
     *
     * @param   obj     the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     * @see     Object#equals(Object)
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
     * Returns the string representation of this Packet object.
     *
     * @return  a string representation of this Packet object.
     * @see     Object#toString()
     */
    @Override
    public String toString()
    {
        return header + " / " + new String(body, StandardCharsets.UTF_8) + " / " + (int) footer;
    }
}
