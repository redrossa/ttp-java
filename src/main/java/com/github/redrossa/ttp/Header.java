package com.github.redrossa.ttp;

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
public enum Header implements Headerable
{
    /** No operation or placeholder */
    NOP(0),

    /** Allows for custom operations encoded in bytes for subprotocol implementations */
    OP(1),

    /** Boolean type */
    BOOLEAN(100),

    /** Integer type */
    INTEGER(101),

    /** Double type */
    DOUBLE(102),

    /** String type */
    STRING(103),

    /** Binary false variable used for response */
    BAD(200),

    /** Binary true variable used for response */
    OK(201);

    /** The underlying mask of the Header enum. */
    private final int mask;

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
     * See the general contract of the {@code getMask}
     * method of {@code Headerable}.
     * <p>
     * Returns this Header enum's mask.
     *
     * @return  this header's mask.
     */
    public final int getMask()
    {
        return mask;
    }

    /**
     * Returns the header enum associated with {@code mask}
     * for enum {@code Header} class. If no enum is associated
     * with it, null is returned.
     * <p>
     * This method simply performs
     * {@code Headerable.valueOf(mask, Header.class} and returns the
     * result.
     *
     * @param   mask    an {@code int} value whose associated enum is
     *                  to be returned.
     * @return  the Header enum associated with {@code mask} or null
     *          if not found.
     */
    public static Header valueOf(int mask)
    {
        return Headerable.valueOf(mask, Header.class);
    }
}
