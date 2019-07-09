package com.github.redrossa.ttp.server;

import com.github.redrossa.ttp.MultiplexedPortal;
import com.github.redrossa.ttp.Selector;

import java.io.IOException;

public class SelectorImpl extends Selector
{
    /**
     * Creates a new Selector with the specified multiplexed portal.
     *
     * @param portal the multiplexed portal.
     */
    public SelectorImpl(MultiplexedPortal portal)
    {
        super(portal);
    }

    @Override
    protected void cycle()
    {
        cycles.incrementAndGet();
        try
        {
            for (int i = 0; i < portal.getChannelCount(); i++)
            {
                output(portal.getChannel(i));
                input();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
