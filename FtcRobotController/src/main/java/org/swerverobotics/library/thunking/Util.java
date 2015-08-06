package org.swerverobotics.library.thunking;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;

/**
 * Various utilities that assist in thunking.
 */
public class Util
    {
    //----------------------------------------------------------------------------------------------
    // Private field access
    //
    // Ugh. We wish we didn't have to do this. But the definitions of some classes we need
    // to override leave us no choice.
    //----------------------------------------------------------------------------------------------
    
    static Vector<Field> getDeclaredFieldsIncludingSuper(Class<?> c)
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
    
    static Field getAccessibleClassField(Object target, int iField)
        {
        Class<?> c = target.getClass();
        Vector<Field> fields = getDeclaredFieldsIncludingSuper(c);
        Field field = fields.elementAt(iField);
        
        if (!field.isAccessible())
            field.setAccessible(true);
        
        return field;        
        }

    static int getPrivateIntField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getInt(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static long getPrivateLongField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getLong(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static short getPrivateShortField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getShort(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static double getPrivateDoubleField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getDouble(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static float getPrivateFloatField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getFloat(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    static boolean getPrivateBooleanField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getBoolean(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static byte getPrivateByteField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return field.getByte(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    // @SuppressWarnings("unchecked")
    static <T> T getPrivateObjectField(Object target, int iField)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            return (T)(field.get(target));
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    
    static <T> void setPrivateObjectField(Object target, int iField, T value)
        {
        Field field = getAccessibleClassField(target, iField);
        try 
            {
            field.set(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static void setPrivateLongField(Object target, int iField, long value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setLong(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static void setPrivateIntField(Object target, int iField, int value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setInt(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static void setPrivateByteField(Object target, int iField, byte value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setByte(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static void setPrivateFloatField(Object target, int iField, float value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setFloat(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static void setPrivateDoubleField(Object target, int iField, double value)
        {
        Field field = getAccessibleClassField(target, iField);
        try
            {
            field.setDouble(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    }
