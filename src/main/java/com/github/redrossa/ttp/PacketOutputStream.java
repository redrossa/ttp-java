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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code PacketOutputStream} lets an application write {@code Packet}s
 * to an output stream in a portable way. An application can then use a
 * {@link PacketInputStream} to read the {@code Packet} back in.
 * <p>
 * A {@code PacketOutputStream} uses a {@link DataOutputStream} as the
 * underlying input stream. All write operations of this class call
 * the write operations of the underlying {@code DataOutputStream}.
 * Implementations of methods from {@code OutputStream} simply call
 * their respective {@code DataOutputStream} methods.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 * @see     PacketInputStream
 * @see     DataOutputStream
 */
public class PacketOutputStream extends OutputStream implements PacketOutput
{
    /** The underlying output stream. */
    protected DataOutputStream out;

    /**
     * Creates a {@code PacketOutputStream} by initialising the underlying
     * {@code DataOutputStream} using the specified output stream.
     *
     * @param out the specified input stream.
     */
    public PacketOutputStream(OutputStream out)
    {
        this.out = new DataOutputStream(out);
    }

    /**
     * Returns the current value of the counter {@code written} in the underlying
     * output stream, the number of bytes written to this stream so far.
     * If the counter overflows, it will be wrapped to Integer.MAX_VALUE.
     * <p>
     * This method simply performs {@code out.size()} and returns the result.
     *
     * @return the value of the {@code written} field.
     * @see    DataOutputStream#size()
     */
    public final int size()
    {
        return out.size();
    }

    /**
     * Deconstructs a {@code Packet} into its component parts and writes
     * them into the underlying output stream as a series of bytes.
     * <p>
     * See the general contract of the {@code writePacket} method of
     * {@code PacketOutput}.
     *
     * @param  p the packet object to be written.
     * @throws IOException the stream has been closed and the contained
     *         input stream does not support reading after close, or
     *         another I/O error occurs.
     * @see    DataOutputStream#writeInt(int)
     * @see    DataOutputStream#write(byte[], int, int)
     * @see    DataOutputStream#writeChar(int)
     */
    @Override
    public void writePacket(@NotNull Packet p) throws IOException
    {
        out.writeInt(p.header);
        out.writeInt(p.body.length);
        out.write(p.body, 0, p.body.length);
        out.writeChar(p.footer);
    }

    /**
     * Writes the specified byte (the low eight bits of the argument
     * {@code b}) to the underlying output stream.
     * <p>
     * This method simply performs {@code out.write()}.
     *
     * @param  b the {@code byte} to be written.
     * @throws IOException if an I/O error occurs.
     * @see    DataOutputStream#write(int)
     */
    @Override
    public synchronized void write(int b) throws IOException
    {
        out.write(b);
    }

    /**
     * Writes {@code len} bytes from the specified byte array
     * starting at offset {@code off} to the underlying output stream.
     * <p>
     * This method simply performs {@code out.write(b, 0, b.length)}.
     *
     * @param  b the data.
     * @throws IOException if an I/O error occurs.
     * @see    DataOutputStream#write(byte[], int, int)
     */
    @Override
    public synchronized void write(@NotNull byte[] b) throws IOException
    {
        out.write(b, 0, b.length);
    }

    /**
     * Writes {@code len} bytes from the specified byte array
     * starting at offset {@code off} to the underlying output stream.
     * <p>
     * This method simply performs {@code out.write(b, off, len)}.
     *
     * @param  b the data.
     * @param  off the start offset in the data.
     * @param  len the number of bytes to write.
     * @throws IOException if an I/O error occurs.
     * @see    DataOutputStream#write(byte[], int, int)
     */
    @Override
    public synchronized void write(@NotNull byte[] b, int off, int len) throws IOException
    {
        out.write(b, off, len);
    }

    /**
     * Flushes the underlying output stream. This forces any buffered output
     * bytes to be written out to the stream.
     * <p>
     * This method simply performs {@code out.write(b, off, len)}.
     *
     * @throws IOException if an I/O error occurs.
     * @see    DataOutputStream#flush()
     */
    @Override
    public void flush() throws IOException
    {
        out.flush();
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with the stream.
     * <p>
     * This method simply performs {@code out.close()}.
     *
     * @throws IOException if an I/O error occurs.
     * @see    DataOutputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        out.close();
    }
}
