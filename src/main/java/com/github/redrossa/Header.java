package com.github.redrossa;

import java.util.Arrays;

/**
 * The header indicates the standardised type of the data body of
 * a TTP packet. A mask is a three-digit integer value of a header.
 * The first digit indicates category, and the last two digits are
 * used for the actual distinction. 0XX are operations, 1XX are
 * types to be converted into the implementor's language of use and
 * 2XX are binary variables for intercommunication between implementors.
 * Masks that are written with less than three digits are assumed to be
 * of the operation category.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public enum Header
{
    /** No operation or placeholder */
    NOP(0),

    /** Allows for custom operations encoded in bytes for subprotocol implementations */
    OP(1),

    /** Null reference */
    NULL(100),

    /** Boolean type */
    BOOLEAN(101),

    /** Integer type */
    INTEGER(102),

    /** Double type */
    DOUBLE(103),

    /** String type */
    STRING(104),

    /** Binary false variable used for response */
    BAD(200),

    /** Binary true variable used for response */
    OK(201);

    /** The underlying mask of the Header enum. */
    private int mask;

    /**
     * The sole constructor of the Header enum.
     *
     * @param   mask    mask associated with a Header enum.
     */
    Header(int mask)
    {
        this.mask = mask;
    }

    /**
     * Returns this Header enum's mask.
     *
     * @return  this header's mask.
     */
    public int getMask()
    {
        return mask;
    }

    /**
     * Returns the Header enum associated with {@code mask}.
     * If no enum is associated with it, {@code NOP} is returned
     * instead.
     *
     * @param   mask    an {@code int} value whose associated enum is
     *                  to be returned.
     * @return  the Header enum associated with {@code mask}.
     */
    public static Header valueOf(int mask)
    {
        return Arrays.stream(values())
                     .filter(header -> header.mask == mask)
                     .findFirst()
                     .orElse(null);
    }
}
