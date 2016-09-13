// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package com.coppelia;

@SuppressWarnings("ALL")
public class CharWA
{
    char[] w;

    public CharWA(int i)
    {
        w = new char[i];
    }
	
    public CharWA(String s)
    {
		w=s.toCharArray();
    }
	
    public String getString()
    {
		String a;
        a = new String(w);
		return a;
    }

    public void initArray(int i)
    {
        w = new char[i];
    }

    public char[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public char[] getNewArray(int i)
    {
        w = new char[i];
        return w;
    }
}