package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.exceptions.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Various internal utilities that assist us.
 */
public class Util
    {
    //----------------------------------------------------------------------------------------------
    // Miscellany
    //----------------------------------------------------------------------------------------------

    static public double milliseconds(ElapsedTime elapsed)
        {
        return elapsed.time() * 1000.0;
        }

    //----------------------------------------------------------------------------------------------
    // Threading
    //----------------------------------------------------------------------------------------------

    static public void handleCapturedInterrupt(Exception e)
        {
        // Log.d(SynchronousOpMode.TAG, "caught an thread interrupt, reinterrupting: " + e);
        Thread.currentThread().interrupt();
        }

    //----------------------------------------------------------------------------------------------
    // Private field access
    //
    // Ugh. We wish we didn't have to do this. But the definitions of some classes we need
    // to override leave us no choice.
    //----------------------------------------------------------------------------------------------

    static List<Method> getDeclaredMethods(Class<?> clazz)
    // Guard against silly class loaders
        {
        Method[] methods;
        try {
            methods = clazz.getDeclaredMethods();
            }
        catch (Exception e)
            {
            methods = new Method[0];
            }
        List<Method> result = new LinkedList<Method>();
        result.addAll(Arrays.asList(methods));
        return result;
        }

    static public List<Method> getDeclaredMethodsIncludingSuper(Class<?> clazz)
        {
        if (clazz.getSuperclass() == null)
            {
            return getDeclaredMethods(clazz);
            }
        else
            {
            List<Method> result = getDeclaredMethodsIncludingSuper(clazz.getSuperclass());
            result.addAll(getDeclaredMethods(clazz));
            return result;
            }
        }

    static List<Field> getLocalDeclaredNonStaticFields(Class<?> clazz)
        {
        List<Field> result = new LinkedList<Field>();
        for (Field field : Arrays.asList(clazz.getDeclaredFields()))
            {
            final int requiredModifiers   = 0;
            final int prohibitedModifiers = Modifier.STATIC;

            if ((field.getModifiers() & requiredModifiers)   == requiredModifiers
             && (field.getModifiers() & prohibitedModifiers) == 0)
                {
                result.add(field);
                }
            }
        return result;
        }

    static public List<Field> getDeclaredNonStaticFieldsIncludingSuper(Class<?> clazz)
        {
        if (clazz.getSuperclass() == null)
            {
            return getLocalDeclaredNonStaticFields(clazz);
            }
        else
            {
            List<Field> result = getDeclaredNonStaticFieldsIncludingSuper(clazz.getSuperclass());
            result.addAll(getLocalDeclaredNonStaticFields(clazz));
            return result;
            }
        }
    
    static public Field getAccessibleClassNonStaticField(Object target, int iField)
        {
        Class<?> c = target.getClass();
        List<Field> fields = getDeclaredNonStaticFieldsIncludingSuper(c);
        Field field = fields.get(iField);
        
        if (!field.isAccessible())
            field.setAccessible(true);
        
        return field;        
        }

    static public int getPrivateIntField(Object target, int iField)
        {
        Field field = getAccessibleClassNonStaticField(target, iField);
        return getPrivateIntField(target, field);
        }

    static public int getPrivateIntField(Object target, Field field)
        {
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
        return Util.<T>getPrivateObjectField(target, field);
        }

    static public <T> T getPrivateObjectField(Object target, Field field)
        {
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
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
        Field field = getAccessibleClassNonStaticField(target, iField);
        try
            {
            field.setDouble(target, value);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }
    }
