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
 * The {@code PacketInput} interface provides for reconstructing
 * a {@link Packet} object from a series of bytes from a binary
 * stream.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 * @see     PacketOutput
 * @see     PacketInputStream
 */
public interface PacketInput
{
    /**
     * Reads data from a binary stream and constructs a {@code Packet}
     * based on the data.
     * <p>
     * The first four incoming bytes read represent the header {@code int}
     * value of the {@code Packet}. The next four bytes represent the
     * {@code int} length of the body byte array. Utilise this value by
     * creating a byte buffer for reading the next chunk of bytes: body
     * of the {@code Packet}. The number of bytes of the body always equals
     * the previous latest four bytes {@code int} value. An {@link IOException}
     * should be thrown if the number of bytes of the body read is less than
     * that {@code int} value. Finally, the series of bytes concludes with
     * two bytes representing the footer {@code char} value.
     * <p>
     * This method should construct the {@code Packet} from the data
     * read using the package-private constructor
     * {@link Packet#Packet(int, byte[], char)}.
     * The {@code int} value representing the length of the body byte array
     * can simply be discarded.
     *
     * @return A {@code Packet} constructed from the data read.
     * @throws IOException the stream has been closed and the contained
     *         input stream does not support reading after close, or
     *         another I/O error occurs.
     */
    Packet readPacket() throws IOException;
}
