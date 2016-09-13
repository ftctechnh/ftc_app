// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package com.coppelia;

@SuppressWarnings("unused")
public class BoolW
{
    boolean w;

    public BoolW(boolean b)
    {
        w = b;
    }

    public boolean getValue()
    {
        return w;
    }

    public void setValue(boolean b)
    {
        w = b;
    }
}