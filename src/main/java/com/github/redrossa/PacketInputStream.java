package com.github.redrossa;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A packet input stream lets an application read TTP packets from
 * an underlying input stream in a machine-independent way. An
 * application uses a packet output stream to write packets that
 * can later be read by a packet input stream.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public class PacketInputStream extends InputStream implements PacketInput
{
    /**
     * The DataInputStream to read bytes from a binary stream are
     * reconstructed into primitive data, which in turn are used to
     * construct a new Packet object.
     */
    protected volatile DataInputStream in;

    /**
     * Creates a PacketInputStream that uses DataInputStream using the
     * underlying InputStream specified.
     *
     * @param in the specified input stream
     */
    public PacketInputStream(InputStream in)
    {
        this.in = new DataInputStream(in);
    }

    /**
     * See the general contract of the {@code readPacket}
     * method of {@code PacketInput}.
     * <p>
     * Data for this operation are read from the contained input stream.
     *
     * @return  reference to Packet object constructed from data read
     *          from a binary stream.
     * @throws  EOFException  if this input stream has reached the end.
     * @throws  IOException   the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     DataInputStream#readInt()
     * @see     DataInputStream#read(byte[])
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
     * Reads the next byte of data from this input stream. The value byte
     * is returned as an {@code int} in the range {@code 0} to
     * {@code 255}. If no byte is available because the end of the
     * stream has been reached, the value {@code -1} is returned.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     * <p>
     * This method simply performs {@code in.read()} and returns the
     * result.
     *
     * @return  the next byte of data, or {@code -1} if the end of the
     *          stream is reached.
     * @throws  IOException   if an I/O error occurs.
     * @see     java.io.FilterInputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        return in.read();
    }

    /**
     * Reads some number of bytes from the contained input stream and
     * stores them into the buffer array {@code b}. The number of
     * bytes actually read is returned as an integer. This method blocks
     * until input data is available, end of file is detected, or an
     * exception is thrown.
     * <p>
     * This method simply performs {@code in.read(b)} and returns the result.
     *
     * @param   b  the buffer into which the data is read.
     * @return  the total number of bytes read into the buffer, or {@code -1}
     *          if there is no more data because the end of the stream has been
     *          reached.
     * @throws  IOException    if the first byte cannot be read for any reason
     *          other than end of file, the stream has been closed and the
     *          underlying input stream does not support reading after close,
     *          or another I/O error occurs.
     * @see     DataInputStream#read(byte[])
     */
    @Override
    public final int read(@NotNull byte[] b) throws IOException
    {
        return in.read(b);
    }

    /**
     * Reads up to {@code len} bytes of data from the contained
     * input stream into an array of bytes.  An attempt is made to read
     * as many as {@code len} bytes, but a smaller number may be read,
     * possibly zero. The number of bytes actually read is returned as an
     * integer.
     * <p>
     * This method simply performs {@code in.read(b, off, len)} and returns
     * the result.
     *
     * @param   b   the buffer into which the data is read.
     * @param   off the start offset in the destination array {@code b}
     * @param   len the maximum number of bytes read.
     * @return  the total number of bytes read into the buffer, or
     *          {@code -1} if there is no more data because the end
     *          of the stream has been reached.
     * @throws  NullPointerException        if {@code b} is {@code null}.
     * @throws  IndexOutOfBoundsException   if {@code off} is negative,
     *          {@code len} is negative, or {@code len} is greater than
     *          {@code b.length - off}
     * @throws  IOException if the first byte cannot be read for any reason
     *          other than end of file, the stream has been closed and the
     *          underlying input stream does not support reading after close,
     *          or another I/O error occurs.
     * @see     DataInputStream#read(byte[], int, int)
     */
    @Override
    public final int read(@NotNull byte[] b, int off, int len) throws IOException
    {
        return in.read(b, off, len);
    }

    /**
     * Skips over and discards {@code n} bytes of data from the
     * input stream. The {@code skip} method may, for a variety of
     * reasons, end up skipping over some smaller number of bytes,
     * possibly {@code 0}. The actual number of bytes skipped is
     * returned.
     * <p>
     * This method simply performs {@code in.skip(n)} and returns the result.
     *
     * @param   n   the number of bytes to be skipped.
     * @return  the actual number of bytes skipped.
     * @throws  IOException if {@code  in.skip(n)} throws an IOException.
     * @see     java.io.FilterInputStream#skip(long)
     */
    @Override
    public long skip(long n) throws IOException
    {
        return in.skip(n);
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking by the next
     * caller of a method for this input stream. The next caller might be
     * the same thread or another thread.  A single read or skip of this
     * many bytes will not block, but may read or skip fewer bytes.
     * <p>
     * This method simply performs {@code in.available()} and returns the result.
     *
     * @return  an estimate of the number of bytes that can be read (or skipped
     *          over) from this input stream without blocking.
     * @throws  IOException  if an I/O error occurs.
     * @see     java.io.FilterInputStream#available()
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
     * @throws  IOException  if an I/O error occurs.
     * @see     java.io.FilterInputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        in.close();
    }
}
