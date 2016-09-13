// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package com.coppelia;

@SuppressWarnings("ALL")
public class StringWA
{
    String[] w;

    public StringWA(int i)
    {
        w = new String[i];
    }

    public void initArray(int i)
    {
        w = new String[i];
    }

    public String[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public String[] getNewArray(int i)
    {
        w = new String[i];
        return w;
    }
}