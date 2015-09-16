package org.swerverobotics.library.internal;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;

/**
 * Various internal utilities that assist us.
 */
public class Util
    {
    //----------------------------------------------------------------------------------------------
    // Threading
    //----------------------------------------------------------------------------------------------

    static public void handleCapturedInterrupt()
        {
        Thread.currentThread().interrupt();
        }

    //----------------------------------------------------------------------------------------------
    // Private field access
    //
    // Ugh. We wish we didn't have to do this. But the definitions of some classes we need
    // to override leave us no choice.
    //----------------------------------------------------------------------------------------------
    
    static public Vector<Field> getDeclaredFieldsIncludingSuper(Class<?> c)
        {
        if (c.getSuperclass() == null)
            {
            return new Vector<Field>(Arrays.asList(c.getDeclaredFields()));
            }
        else
            {
            Vector<Field> result = getDeclaredFieldsIncludingSuper(c.getSuperclass());
            result.addAll(Arrays.asList(c.getDeclaredFields()));
            return result;
            }
        }
    
    static public Field getAccessibleClassField(Object target, int iField)
        {
        Class<?> c = target.getClass();
        Vector<Field> fields = getDeclaredFieldsIncludingSuper(c);
        Field field = fields.elementAt(iField);
        
        if (!field.isAccessible())
            field.setAccessible(true);
        
        return field;        
        }

    static public int getPrivateIntField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getInt(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public long getPrivateLongField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getLong(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public short getPrivateShortField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getShort(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public double getPrivateDoubleField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getDouble(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public float getPrivateFloatField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getFloat(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }
    static public boolean getPrivateBooleanField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getBoolean(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public byte getPrivateByteField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getByte(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    // @SuppressWarnings("unchecked")
    static public <T> T getPrivateObjectField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return (T)(field.get(target));
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }
    
    static public <T> void setPrivateObjectField(Object target, int iField, T value)
        {
        Field field = getAccessibleClassField(target, iField);
        try 
            {
            field.set(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public void setPrivateLongField(Object target, int iField, long value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setLong(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public void setPrivateIntField(Object target, int iField, int value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setInt(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public void setPrivateByteField(Object target, int iField, byte value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setByte(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public void setPrivateFloatField(Object target, int iField, float value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setFloat(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    static public void setPrivateDoubleField(Object target, int iField, double value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setDouble(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }
    
    //----------------------------------------------------------------------------------------------
    // Arithmetic utility
    //----------------------------------------------------------------------------------------------
    
    static public int makeIntLittle(byte low, byte hi)
        {
        return ((int)low) | (((int)hi)<<8);
        }
    static public int makeIntBig(byte hi, byte low)
        {
        return ((int)low) | (((int)hi)<<8);
        }
    
    static public int unpack10BitAnalog(byte[] rgb, int ib)
        {
        return (((int)rgb[ib])<<2) | (rgb[ib+1]&0x03);
        }
    }
