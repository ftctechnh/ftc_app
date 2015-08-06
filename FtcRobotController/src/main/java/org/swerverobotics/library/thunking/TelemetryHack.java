package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a doozy of a hack. All just to make the output look prettier by having
 * a reasonable sort order. Sorry folks! Maybe we can remove this at some future date.
 */
public class TelemetryHack extends com.qualcomm.robotcore.robocol.Telemetry
    {
    private static final Charset a = Charset.forName("UTF-8");
    Map<String,String> b;
    Map<String,Float> c;
    String d;
    long e;
    
    public TelemetryHack(Telemetry target)
        {
        // Transfer the state from the target to us 
        this.b = Util.< Map<String,String> >getPrivateObjectField(target, 2);
        this.c = Util.< Map<String,Float>  >getPrivateObjectField(target, 3);
        this.d = Util.<String>getPrivateObjectField(target, 4);
        this.e = Util.getPrivateLongField(target, 5);
        
        Util.setPrivateObjectField(this, 2, this.b);
        Util.setPrivateObjectField(this, 3, this.c);
        Util.setPrivateObjectField(this, 4, this.d);
        Util.setPrivateLongField(this, 5, this.e);
        }
    
    @Override public synchronized byte[] toByteArray() throws RobotCoreException
        {
        this.e = System.currentTimeMillis();
        if (this.b.size() > 256)
            {
            throw new RobotCoreException("Cannot have more than 256 string data points");
            }
        else if (this.c.size() > 256)
            {
            throw new RobotCoreException("Cannot have more than 256 number data points");
            }
        else
            {
            int var1 = this.a() + 8;
            int var2 = 3 + var1;
            if (var2 > 4098)
                {
                throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", new Object[] {Integer.valueOf(var2), Integer.valueOf(4098)}));
                }
            else
                {
                ByteBuffer var3 = ByteBuffer.allocate(var2);
                var3.put(this.getRobocolMsgType().asByte());
                var3.putShort((short) var1);
                var3.putLong(this.e);
                if (this.d.length() == 0)
                    {
                    var3.put((byte) 0);
                    }
                else
                    {
                    byte[] var4 = this.d.getBytes(a);
                    if (var4.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry tag cannot exceed 256 bytes [%s]", new Object[] {this.d}));
                        }

                    var3.put((byte) var4.length);
                    var3.put(var4);
                    }

                var3.put((byte) this.b.size());
                Iterator var8 = this.b.entrySet().iterator();

                Map.Entry var5;
                byte[] var6;
                while (var8.hasNext())
                    {
                    var5 = (Map.Entry) var8.next();
                    var6 = ((String) var5.getKey()).getBytes(a);
                    byte[] var7 = ((String) var5.getValue()).getBytes(a);
                    if (var6.length > 256 || var7.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%s]", new Object[] {var5.getKey(), var5.getValue()}));
                        }

                    var3.put((byte) var6.length);
                    var3.put(var6);
                    var3.put((byte) var7.length);
                    var3.put(var7);
                    }

                var3.put((byte) this.c.size());
                var8 = this.c.entrySet().iterator();

                while (var8.hasNext())
                    {
                    var5 = (Map.Entry) var8.next();
                    var6 = ((String) var5.getKey()).getBytes(a);
                    float var9 = ((Float) var5.getValue()).floatValue();
                    if (var6.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%f]", new Object[] {var5.getKey(), Float.valueOf(var9)}));
                        }

                    var3.put((byte) var6.length);
                    var3.put(var6);
                    var3.putFloat(var9);
                    }

                return var3.array();
                }
            }
        }

    private int a()
        {
        byte var1 = 0;
        int var4 = var1 + 1 + this.d.getBytes(a).length;
        ++var4;

        Iterator var2;
        Map.Entry var3;
        for (var2 = this.b.entrySet().iterator(); var2.hasNext(); var4 += 1 + ((String) var3.getValue()).getBytes(a).length)
            {
            var3 = (Map.Entry) var2.next();
            var4 += 1 + ((String) var3.getKey()).getBytes(a).length;
            }

        ++var4;

        for (var2 = this.c.entrySet().iterator(); var2.hasNext(); var4 += 4)
            {
            var3 = (Map.Entry) var2.next();
            var4 += 1 + ((String) var3.getKey()).getBytes(a).length;
            }

        return var4;
        }
    }
