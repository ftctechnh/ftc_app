package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.TypeConversion;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a doozy of a hack. All just to make the output look prettier by having
 * a reasonable sort order. Sorry folks! Maybe we can remove this at some future date.
 * I sure hope so.
 */
public class TelemetryHack extends com.qualcomm.robotcore.robocol.Telemetry
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    
    private static final Charset utf8Charset = Charset.forName("UTF-8");
    Map<String,String> stringDataPoints;
    Map<String,Float> numberDataPoints;
    Field fieldTelemetryTag;
    Field fieldTimeStamp;

    void setTimeStamp(long timestamp)
        {
        try { this.fieldTimeStamp.setLong(this, timestamp); } catch (Exception e) {}
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public TelemetryHack(Telemetry target)
        {
        // Transfer the state from the target to us 
        this.stringDataPoints = Util.< Map<String,String> >getPrivateObjectField(target, 2);
        this.numberDataPoints = Util.< Map<String,Float>  >getPrivateObjectField(target, 3);
        String telemetryTag = Util.<String>getPrivateObjectField(target, 4);
        long   timestamp = Util.getPrivateLongField(target, 5);
        
        Util.setPrivateObjectField(this, 2, this.stringDataPoints);
        Util.setPrivateObjectField(this, 3, this.numberDataPoints);
        Util.setPrivateObjectField(this, 4, telemetryTag);
        Util.setPrivateLongField(this, 5, timestamp);
        
        this.fieldTelemetryTag = Util.getAccessibleClassField(this, 4);
        this.fieldTimeStamp    = Util.getAccessibleClassField(this, 5);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------
    
    private final int cbMax = 4098;
    
    @Override public synchronized byte[] toByteArray() throws RobotCoreException
        {
        this.setTimeStamp(System.currentTimeMillis());
        if (this.stringDataPoints.size() > 256)
            {
            throw new RobotCoreException("Cannot have more than 256 string data points");
            }
        else if (this.numberDataPoints.size() > 256)
            {
            throw new RobotCoreException("Cannot have more than 256 number data points");
            }
        else
            {
            int cbPayloadAndTimestmap = this.getCbPayload() + 8;
            int cbTelemetryData = 3 + cbPayloadAndTimestmap;
            if (cbTelemetryData > cbMax)
                {
                throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", cbTelemetryData, cbMax));
                }
            else
                {
                ByteBuffer rgb = ByteBuffer.allocate(cbTelemetryData);
                
                // 1+2 == 3
                rgb.put(this.getRobocolMsgType().asByte());
                rgb.putShort((short) cbPayloadAndTimestmap);
                
                // cb(timestamp) == 8
                rgb.putLong(this.getTimestamp());
                
                // payload
                if (this.getTag().length() == 0)
                    {
                    rgb.put((byte) 0);
                    }
                else
                    {
                    byte[] cbTag = this.getTag().getBytes(utf8Charset);
                    if (cbTag.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry tag cannot exceed 256 bytes [%s]", this.getTag()));
                        }

                    rgb.put((byte) cbTag.length);
                    rgb.put(cbTag);
                    }

                rgb.put((byte) this.stringDataPoints.size());

                // emit the string valued data points
                Iterator iterator = this.stringDataPoints.entrySet().iterator();
                Map.Entry keyValue;
                byte[] rgbKey;
                while (iterator.hasNext())
                    {
                    keyValue = (Map.Entry) iterator.next();
                    rgbKey = ((String) keyValue.getKey()).getBytes(utf8Charset);
                    byte[] rgbValue = ((String) keyValue.getValue()).getBytes(utf8Charset);
                    if (rgbKey.length > 256 || rgbValue.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%s]", keyValue.getKey(), keyValue.getValue()));
                        }

                    rgb.put((byte) rgbKey.length);
                    rgb.put(rgbKey);
                    rgb.put((byte) rgbValue.length);
                    rgb.put(rgbValue);
                    }

                // emit the numeric valued data points
                rgb.put((byte) this.numberDataPoints.size());
                iterator = this.numberDataPoints.entrySet().iterator();
                while (iterator.hasNext())
                    {
                    keyValue = (Map.Entry) iterator.next();
                    rgbKey = ((String) keyValue.getKey()).getBytes(utf8Charset);
                    float floatDataPoint = ((Float) keyValue.getValue()).floatValue();
                    if (rgbKey.length > 256)
                        {
                        throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%f]", keyValue.getKey(), floatDataPoint));
                        }

                    rgb.put((byte) rgbKey.length);
                    rgb.put(rgbKey);
                    rgb.putFloat(floatDataPoint);
                    }

                return rgb.array();
                }
            }
        }

    private int getCbPayload()
        {
        byte cbInitial = 0;
        int cbNeeded = cbInitial + 1 + this.getTag().getBytes(utf8Charset).length;
        ++cbNeeded;

        Iterator iterator;
        Map.Entry keyValue;
        for (iterator = this.stringDataPoints.entrySet().iterator(); iterator.hasNext(); cbNeeded += 1 + ((String) keyValue.getValue()).getBytes(utf8Charset).length)
            {
            keyValue = (Map.Entry) iterator.next();
            cbNeeded += 1 + ((String) keyValue.getKey()).getBytes(utf8Charset).length;
            }

        ++cbNeeded;

        for (iterator = this.numberDataPoints.entrySet().iterator(); iterator.hasNext(); cbNeeded += 4)
            {
            keyValue = (Map.Entry) iterator.next();
            cbNeeded += 1 + ((String) keyValue.getKey()).getBytes(utf8Charset).length;
            }

        return cbNeeded;
        }

    public synchronized void fromByteArray(byte[] byteArray) throws RobotCoreException
        {
        this.clearData();
        ByteBuffer rgbPayloadAndTimestamp = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.setTimeStamp(rgbPayloadAndTimestamp.getLong());
        int cbTag = TypeConversion.unsignedByteToInt(rgbPayloadAndTimestamp.get());
        if (cbTag == 0)
            {
            this.setTag("");
            }
        else
            {
            byte[] rgbTag = new byte[cbTag];
            rgbPayloadAndTimestamp.get(rgbTag);
            this.setTag(new String(rgbTag, utf8Charset));
            }

        byte cStringPoints = rgbPayloadAndTimestamp.get();

        int i;
        for (int iDataPoint = 0; iDataPoint < cStringPoints; ++iDataPoint)
            {
            i = TypeConversion.unsignedByteToInt(rgbPayloadAndTimestamp.get());
            byte[] rgbKey = new byte[i];
            rgbPayloadAndTimestamp.get(rgbKey);
            int cbValue = TypeConversion.unsignedByteToInt(rgbPayloadAndTimestamp.get());
            byte[] rgbValue = new byte[cbValue];
            rgbPayloadAndTimestamp.get(rgbValue);
            String key = new String(rgbKey, utf8Charset);
            String value = new String(rgbValue, utf8Charset);
            this.stringDataPoints.put(key, value);
            }

        byte cNumberPoints = rgbPayloadAndTimestamp.get();

        for (i = 0; i < cNumberPoints; ++i)
            {
            int cbKey = TypeConversion.unsignedByteToInt(rgbPayloadAndTimestamp.get());
            byte[] rgbKey = new byte[cbKey];
            rgbPayloadAndTimestamp.get(rgbKey);
            String key = new String(rgbKey, utf8Charset);
            float value = rgbPayloadAndTimestamp.getFloat();
            this.numberDataPoints.put(key, Float.valueOf(value));
            }
        }    
    }
