package com.github.redrossa.ttp;

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
    NOP(0),

    /** Allows for custom operation implementations */
    OP(1),

    /** Boolean type */
    BOOLEAN(100),

    /** Integer type */
    INTEGER(101),

    /** Double type */
    DOUBLE(102),

    /** String type */
    STRING(103),

    /** Binary false response */
    BAD(200),

    /** Binary true response */
    OK(201);

    /** The underlying mask */
    private final int mask;

    /**
     * The sole constructor of {@code Header}.
     *
     * @param mask the mask of this {@code Header}.
     */
    Header(int mask)
    {
        this.mask = mask;
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
     */
    public static Header valueOf(int mask)
    {
        return Headerable.valueOf(mask, Header.class);
    }
}
