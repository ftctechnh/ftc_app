// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package com.coppelia;

@SuppressWarnings("ALL")
public class BoolWA
{
    boolean w[];

    public BoolWA(int i)
    {
        w = new boolean[i];
    }

    public void initArray(int i)
    {
        w = new boolean[i];
    }

    public boolean[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public boolean[] getNewArray(int i)
    {
        w = new boolean[i];
        return w;
    }
}