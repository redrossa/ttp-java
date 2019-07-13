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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A {@code PacketInputStream} lets an application read {@link Packet}s
 * from an underlying input stream in a machine-independent way. An
 * application uses a {@link PacketOutputStream} to write {@code Packet}s
 * that can later be read by a {@code PacketInputStream}.
 * <p>
 * A {@code PacketInputStream} uses a {@link DataInputStream} as the
 * underlying input stream. All read operations of this class call
 * the read operations of the underlying {@code DataInputStream}.
 * Implementations of methods from {@code InputStream} simply call
 * their respective {@code DataInputStream} methods and return the
 * result.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 * @see     PacketOutputStream
 * @see     DataInputStream
 */
public class PacketInputStream extends InputStream implements PacketInput
{
    /** The underlying input stream. */
    protected volatile DataInputStream in;

    /**
     * Creates a {@code PacketInputStream} by initialising the underlying
     * {@code DataInputStream} using the specified input stream.
     *
     * @param in the specified input stream.
     */
    public PacketInputStream(@NotNull InputStream in)
    {
        this.in = new DataInputStream(in);
    }

    /**
     * Reads data from the underlying input stream and constructs a
     * {@code Packet} based on the data.
     * <p>
     * See the general contract of the {@code readPacket}
     * method of {@link PacketInput}.
     *
     * @return a {@code Packet}.
     * @throws EOFException if this input stream has reached the end.
     * @throws IOException the stream has been closed and the contained
     *         input stream does not support reading after close, or
     *         another I/O error occurs.
     * @see    DataInputStream#readInt()
     * @see    DataInputStream#read(byte[])
     * @see    DataInputStream#readChar()
     */
    @Override
    public Packet readPacket() throws IOException
    {
        int h = in.readInt();
        byte[] b = new byte[in.readInt()];
        int bytesRead = in.read(b);
        if (bytesRead < b.length)
            throw new EOFException();
        char c = in.readChar();
        return new Packet(h, b, c);
    }

    /**
     * Reads the next byte of data from the underlying input stream.
     * The value byte is returned as an {@code int} in the range {@code 0}
     * to {@code 255}. If no byte is available because the end of the
     * stream has been reached, the value {@code -1} is returned.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     * <p>
     * This method simply performs {@code in.read()} and returns the
     * result.
     *
     * @return the next byte of data, or {@code -1} if the end of the
     *         stream is reached.
     * @throws IOException if an I/O error occurs.
     * @see    java.io.FilterInputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        return in.read();
    }

    /**
     * Reads some number of bytes from the underlying input stream and
     * stores them into the buffer array {@code b}. The number of
     * bytes actually read is returned as an integer. This method blocks
     * until input data is available, end of file is detected, or an
     * exception is thrown.
     * <p>
     * This method simply performs {@code in.read(b)} and returns the result.
     *
     * @param  b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or {@code -1}
     *         if there is no more data because the end of the stream has been
     *         reached.
     * @throws IOException if the first byte cannot be read for any reason
     *         other than end of file, the stream has been closed and the
     *         underlying input stream does not support reading after close,
     *         or another I/O error occurs.
     * @see    DataInputStream#read(byte[])
     */
    @Override
    public final int read(@NotNull byte[] b) throws IOException
    {
        return in.read(b);
    }

    /**
     * Reads up to {@code len} bytes of data from the underlying
     * input stream into an array of bytes.  An attempt is made to read
     * as many as {@code len} bytes, but a smaller number may be read,
     * possibly zero. The number of bytes actually read is returned as an
     * integer.
     * <p>
     * This method simply performs {@code in.read(b, off, len)} and returns
     * the result.
     *
     * @param  b the buffer into which the data is read.
     * @param  off the start offset in the destination array {@code b}
     * @param  len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or
     *         {@code -1} if there is no more data because the end
     *         of the stream has been reached.
     * @throws NullPointerException if {@code b} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code off} is negative,
     *         {@code len} is negative, or {@code len} is greater than
     *         {@code b.length - off}
     * @throws IOException if the first byte cannot be read for any reason
     *         other than end of file, the stream has been closed and the
     *         underlying input stream does not support reading after close,
     *         or another I/O error occurs.
     * @see    DataInputStream#read(byte[], int, int)
     */
    @Override
    public final int read(@NotNull byte[] b, int off, int len) throws IOException
    {
        return in.read(b, off, len);
    }

    /**
     * Skips over and discards {@code n} bytes of data from the
     * underlying input stream. The {@code skip} method may, for a
     * variety of reasons, end up skipping over some smaller number
     * of bytes, possibly {@code 0}. The actual number of bytes skipped is
     * returned.
     * <p>
     * This method simply performs {@code in.skip(n)} and returns the result.
     *
     * @param  n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if {@code  in.skip(n)} throws an IOException.
     * @see    java.io.FilterInputStream#skip(long)
     */
    @Override
    public long skip(long n) throws IOException
    {
        return in.skip(n);
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from the underlying input stream without blocking by
     * the next caller of a method for this input stream. The next caller
     * might be the same thread or another thread.  A single read or skip
     * of this many bytes will not block, but may read or skip fewer bytes.
     * <p>
     * This method simply performs {@code in.available()} and returns the result.
     *
     * @return an estimate of the number of bytes that can be read (or skipped
     *         over) from this input stream without blocking.
     * @throws IOException if an I/O error occurs.
     * @see    java.io.FilterInputStream#available()
     */
    @Override
    public int available() throws IOException
    {
        return in.available();
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * <p>
     * This method simply performs {@code in.close()} and returns the result.
     *
     * @throws IOException if an I/O error occurs.
     * @see    java.io.FilterInputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        in.close();
    }
}
