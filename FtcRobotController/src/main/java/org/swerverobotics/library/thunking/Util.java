package org.swerverobotics.library.thunking;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;
import java.lang.reflect.Field;

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

    static int getPrivateIntField(Object target, int iMember)
        {
        Class<?> c = target.getClass();
        Field field = c.getDeclaredFields()[iMember];
        try
            {
            return field.getInt(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static boolean getPrivateBooleanField(Object target, int iMember)
        {
        Class<?> c = target.getClass();
        Field field = c.getDeclaredFields()[iMember];
        try
            {
            return field.getBoolean(target);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    static <T> T getPrivateField(Object target, int iMember)
        {
        Class<?> c = target.getClass();
        Field field = c.getDeclaredFields()[iMember];
        try
            {
            return (T)(field.get(target));
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    }
