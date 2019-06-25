package com.github.redrossa;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A packet output stream lets an application write TTP packets
 * to an output stream in a portable way. An application can
 * then use a packet input stream to read the packet back in.
 *
 * @author  Adriano Raksi
 * @version 1.0-SNAPSHOT
 * @since   2019-06-19
 */
public class PacketOutputStream extends OutputStream implements PacketOutput
{
    /**
     * The DataOutputStream writes bytes into a binary stream. Packets
     * are deconstructed into its primitive component types, which are
     * then written using this DataOutputStream.
     */
    protected DataOutputStream out;

    /**
     * Creates a PacketOutputStream that uses DataOutputStream using the
     * underlying OutputStream specified.
     *
     * @param   out the specified output stream
     */
    public PacketOutputStream(OutputStream out)
    {
        this.out = new DataOutputStream(out);
    }

    /**
     * Returns the current value of the counter {@code written},
     * the number of bytes written to this data output stream so far.
     * If the counter overflows, it will be wrapped to Integer.MAX_VALUE.
     * <p>
     * This method simply performs {@code out.size()} and returns the result.
     *
     * @return  the value of the {@code written} field.
     * @see     DataOutputStream#size()
     */
    public final int size()
    {
        return out.size();
    }

    /**
     * See the general contract of the {@code writePacket} method of
     * {@code PacketOutput}.
     * <p>
     * Data for this operation are written to the contained input stream.
     *
     * @param   p   the packet object to be written.
     * @throws  IOException   the stream has been closed and the contained
     *          input stream does not support reading after close, or
     *          another I/O error occurs.
     * @see     DataOutputStream#writeInt(int)
     * @see     DataOutputStream#write(byte[], int, int)
     */
    @Override
    public void writePacket(@NotNull Packet p) throws IOException
    {
        out.writeInt(p.header());
        out.writeInt(p.body().length);
        out.write(p.body(), 0, p.body().length);
        out.writeChar(p.footer());
    }

    /**
     * Writes the specified byte (the low eight bits of the argument
     * {@code b}) to the underlying output stream.
     * <p>
     * This method simply calls {@code out.write()} of the this object's
     * instantiated DataOutputStream.
     *
     * @param   b   the {@code byte} to be written.
     * @throws  IOException if an I/O error occurs.
     * @see     DataOutputStream#write(int)
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
     * This method simply calls {@code out.write(b, 0, b.length)} of the
     * this object's instantiated DataOutputStream.
     * <p>
     * This method is equivalent to calling {@code write(b, 0, b.length)}.
     *
     * @param   b   the data.
     * @throws  IOException if an I/O error occurs.
     * @see     DataOutputStream#write(byte[], int, int)
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
     * This method simply calls {@code out.write(b, off, len)} of the
     * this object's instantiated DataOutputStream.
     *
     * @param   b   the data.
     * @param   off the start offset in the data.
     * @param   len the number of bytes to write.
     * @throws  IOException if an I/O error occurs.
     * @see     DataOutputStream#write(byte[], int, int)
     */
    @Override
    public synchronized void write(@NotNull byte[] b, int off, int len) throws IOException
    {
        out.write(b, off, len);
    }

    /**
     * Flushes this data output stream. This forces any buffered output
     * bytes to be written out to the stream.
     * <p>
     * This method simply calls {@code out.write(b, off, len)} of the
     * this object's instantiated DataOutputStream.
     *
     * @throws  IOException if an I/O error occurs.
     * @see     DataOutputStream#flush()
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
     * This method simply calls {@code out.close()} of the this object's
     * instantiated DataOutputStream.
     *
     * @throws  IOException if an I/O error occurs.
     * @see     DataOutputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        out.close();
    }
}
