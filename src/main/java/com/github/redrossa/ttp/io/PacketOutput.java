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

import java.io.IOException;

/**
 * The {@code PacketOutput} interface provides for deconstructing
 * a {@link Packet} object into a series of bytes to write into a
 * binary stream.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 * @see     PacketInput
 * @see     PacketOutputStream
 */
public interface PacketOutput
{
    /**
     * Deconstructs a {@code Packet} into its component parts and writes
     * them into a binary stream as a series of bytes.
     * <p>
     * The first four bytes written represent the header {@code int}
     * value of the {@code Packet}. The next four bytes represent the
     * {@code int} length of the body byte array, followed by the actual
     * body byte array. Finally, the series of bytes concludes with
     * two bytes representing the footer {@code char} value.
     * <p>
     * This method should deconstruct the {@code Packet} by retrieving its
     * fields from {@link Packet#header}, {@link Packet#body} and
     * {@link Packet#footer}.
     *
     * @param  p the packet object to be written.
     * @throws IOException the stream has been closed and the contained
     *         input stream does not support reading after close, or
     *         another I/O error occurs.
     */
    void writePacket(Packet p) throws IOException;
}
