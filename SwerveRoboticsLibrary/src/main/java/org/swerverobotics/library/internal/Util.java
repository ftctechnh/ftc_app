package org.swerverobotics.library.internal;

import android.util.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import java.lang.reflect.*;
import java.util.*;
import static junit.framework.Assert.*;

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

    static public String getStackTrace(Exception e)
        {
        StringBuilder result = new StringBuilder();
        result.append(e.toString());
        for (StackTraceElement ele : e.getStackTrace())
            {
            result.append("\n");
            result.append(ele.toString());
            }
        return result.toString();
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
    // Private method access
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

    //----------------------------------------------------------------------------------------------
    // Private field access - utility
    //----------------------------------------------------------------------------------------------

    // The Field of the 'fieldDexIndex' field of class Field
    static Field fieldDexIndexField = getFieldDexIndexField();

    static Field getFieldDexIndexField()
    // Find the Field for the 'fieldDexIndex' field of class Field.
        {
        Class   fieldClass      = Field.class;
        Class   fieldSuperClass = fieldClass.getSuperclass();
        assertTrue(!BuildConfig.DEBUG || fieldSuperClass.getSuperclass() == Object.class);

        List<Field> superFields = getLocalDeclaredNonStaticFields(fieldSuperClass, false);
        List<Field> fieldFields = getLocalDeclaredNonStaticFields(fieldClass, false);

        int iFieldTarget = 7;

        Field result = fieldFields.get(iFieldTarget - superFields.size());
        if (!result.isAccessible())
            result.setAccessible(true);

        return result;
        }

    static Comparator<Field> fieldComparator = new Comparator<Field>()
    // A comparator that sorts fields according to their declaration order
        {
        @Override public int compare(Field a, Field b)
            {
            return getSortIndex(a) - getSortIndex(b);
            }
        };

    static int getSortIndex(Field field)
    // Returns a sort key that will sort Fields in their declared order
        {
        try {
            return fieldDexIndexField.getInt(field);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    //----------------------------------------------------------------------------------------------
    // Private field Field access - local
    //----------------------------------------------------------------------------------------------

    static List<Field> getLocalDeclaredNonStaticFields(Class<?> clazz)
        {
        return getLocalDeclaredNonStaticFields(clazz, true);
        }

    static List<Field> getLocalDeclaredNonStaticFields(Class<?> clazz, boolean sort)
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

        if (sort) Collections.sort(result, Util.fieldComparator);
        return result;
        }

    //----------------------------------------------------------------------------------------------
    // Private field Field access - super
    //----------------------------------------------------------------------------------------------

    static public List<Field> getDeclaredNonStaticFieldsIncludingSuper(Class<?> clazz, boolean sort)
        {
        if (clazz.getSuperclass() == null)
            {
            return getLocalDeclaredNonStaticFields(clazz, sort);
            }
        else
            {
            List<Field> result = getDeclaredNonStaticFieldsIncludingSuper(clazz.getSuperclass(), sort);
            result.addAll(getLocalDeclaredNonStaticFields(clazz, sort));
            return result;
            }
        }
    
    static public Field getAccessibleClassNonStaticField(Object target, int iField)
        {
        Class<?> c = target.getClass();
        List<Field> fields = getDeclaredNonStaticFieldsIncludingSuper(c, true);
        Field field = fields.get(iField);
        
        if (!field.isAccessible())
            field.setAccessible(true);
        
        return field;        
        }

    //----------------------------------------------------------------------------------------------
    // Private field access
    //----------------------------------------------------------------------------------------------

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
