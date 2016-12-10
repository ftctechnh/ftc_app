// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package com.coppelia;

@SuppressWarnings("ALL")
public class StringW
{
    String w;

    public StringW(String s)
    {
        w = new String(s);
    }

    public String getValue()
    {
        return w;
    }

    public void setValue(String s)
    {
        w = new String(s);
    }
}