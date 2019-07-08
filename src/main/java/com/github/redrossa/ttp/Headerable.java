package com.github.redrossa.ttp;

import org.jetbrains.annotations.NotNull;

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
 * <p>
 * Enum types that implement this interface may be used as headers of
 * TTP packet, allowing an extensible enum feature for custom headers.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-29
 */
public interface Headerable
{
    /**
     * Returns this Header enum's mask.
     *
     * @return  this header's mask.
     */
    int getMask();

    /**
     * Returns the header enum associated with {@code mask}
     * and the enum class that implements this interface.
     * If no enum is associated with it, null is returned.
     *
     * @param   mask    an {@code int} value whose associated enum is
     *                  to be returned.
     * @param   clazz   the enum that implements this interface.
     * @return  the Header enum associated with {@code mask} or null
     *          if not found.
     */
    static <T extends Headerable> T valueOf(int mask, @NotNull Class<T> clazz)
    {
        return Arrays.stream(clazz.getEnumConstants())
                     .filter(header -> header.getMask() == mask)
                     .findFirst()
                     .orElse(null);
    }
}
