package org.swerverobotics.library.internal;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.*;
import org.swerverobotics.library.interfaces.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;

import dalvik.system.DexFile;

/**
 * Call {@linkplain AnnotatedOpModeRegistrar#register(OpModeManager)} from FtcOpModeRegister.register()
 * in order to automatically register OpMode classes that you have annotated with @Autonomous
 * or @TeleOp.
 *
 * @see Autonomous
 * @see TeleOp
 */
public class AnnotatedOpModeRegistrar
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    LinkedList<String>  partialClassNamesToIgnore;

    Class<?>            activityThreadClass;
    Method              methodCurrentApplication;
    Context             context;
    DexFile             dexFile;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Call this method from FtcOpModeRegister.register() in order to register OpModes which
     * have been annotated as {@linkplain Autonomous} or {@linkplain TeleOp} OpModes.
     *
     * @param manager   the manager used to carry out the actual registration
     */
    public static void register(final OpModeManager manager)
        {
        AnnotatedOpModeRegistrar registrar = null;
        try {
            registrar = new AnnotatedOpModeRegistrar();
            }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | IOException e)
            {
            registrar = null;
            }

        if (registrar != null)
            registrar.doRegistration(manager);
        }

    private AnnotatedOpModeRegistrar() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
        {
        this.partialClassNamesToIgnore = new LinkedList<>();
        this.partialClassNamesToIgnore.add("com.google");
        this.partialClassNamesToIgnore.add("io.netty");

        // Find the file in which we are executing
        this.activityThreadClass      = Class.forName("android.app.ActivityThread");
        this.methodCurrentApplication = activityThreadClass.getMethod("currentApplication");
        this.context                  = (Application) methodCurrentApplication.invoke(null, (Object[]) null);
        this.dexFile                  = new DexFile(context.getPackageCodePath());
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    private final String TAG = "AnnotatedOpModeReg";

    void doRegistration(final OpModeManager opModeManager)
    // The body of this is from the following, without which we could not have been successful
    // in this endeavor.
    //      https://github.com/dmssargent/Xtensible-ftc_app/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/FtcOpModeRegister.java
    // Many thanks.
        {
        // A list of annotated OpModes, grouped by their pairing properties
        HashMap<String, LinkedList<Class>> opModes = new HashMap<>();

        opModes = findOpModesToRegister();

        // Sort the linked lists within opModes
        Comparator<Class> comparator = new Comparator<Class>()
            {
            @Override public int compare(Class lhs, Class rhs)
                {
                if (lhs.isAnnotationPresent(TeleOp.class) && rhs.isAnnotationPresent(TeleOp.class))
                    return getOpModeName(lhs).compareTo(getOpModeName(rhs));

                else if (lhs.isAnnotationPresent(Autonomous.class) && rhs.isAnnotationPresent(TeleOp.class))
                    return 1;

                else if (lhs.isAnnotationPresent(TeleOp.class) && rhs.isAnnotationPresent(Autonomous.class))
                    return -1;

                else if (lhs.isAnnotationPresent(Autonomous.class) && rhs.isAnnotationPresent(Autonomous.class))
                    return getOpModeName(lhs).compareTo(getOpModeName(rhs));

                return -1;
                }
            };
        for (String key : opModes.keySet())
            {
            Collections.sort(opModes.get(key), comparator);
            }

        // "Sort the map by keys, after discarding the old keys, use the new key from
        // the first item in each LinkedList, and change from HashMap to TreeMap"
        TreeMap<String, LinkedList<Class>> sortedOpModes = new TreeMap<>();
        for (String key : opModes.keySet())
            {
            Class<? extends OpMode> opMode = opModes.get(key).getFirst();
            sortedOpModes.put(getOpModeName(opMode), opModes.get(key));
            }

        // Finally, register all the opmodes
        for (LinkedList<Class> opModeList : sortedOpModes.values())
            {
            for (Class opMode : opModeList)
                {
                opModeManager.register(getOpModeName(opMode), opMode);
                }
            }
        }

    /**
     * Find the list of OpMode classes which should be registered.
     * @return a list of annotated OpModes, grouped by their pairing properties (if any)
     */
    private HashMap<String, LinkedList<Class>> findOpModesToRegister()
        {
        // A list of annotated OpModes, grouped by their pairing properties (if any)
        HashMap<String, LinkedList<Class>> result = new HashMap<>();

        // Iterate over all the classes in this whole .APK
        LinkedList<String> classNames = new LinkedList<>(Collections.list(dexFile.entries()));
        for (String className : classNames)
            {
            // Ignore classes that are in some domains we are to ignore
            // TODO: simple containment probably isn't the right test here
            boolean shouldIgnore = false;
            for (String domain : partialClassNamesToIgnore)
                {
                if (className.contains(domain))
                    {
                    shouldIgnore = true;
                    break;
                    }
                }
            if (shouldIgnore)
                continue;

            // Get the class from the className
            Class clazz;
            try {
                clazz = Class.forName(className, false, context.getClassLoader());
                }
            catch (NoClassDefFoundError|ClassNotFoundException ex)
                {
                Log.w(TAG, className + " " + ex.toString(), ex);
                if (className.contains("$"))
                    {
                    className = className.substring(0, className.indexOf("$") - 1);
                    }
                partialClassNamesToIgnore.add(className);
                continue;
                }

            // If the class doesn't extend OpMode, that's an error, we'll ignore it
            if (!inheritsFrom(clazz, OpMode.class))
                continue;

            // If we have BOTH autonomous and teleop annotations on a class, that's an error, we'll ignore it.
            if (clazz.isAnnotationPresent(TeleOp.class) && clazz.isAnnotationPresent(Autonomous.class))
                continue;

            // If the class has been annotated as @Disabled, then ignore it
            if (clazz.isAnnotationPresent(Disabled.class))
                continue;

            // It passes all our tests, add it!
            addAnnotatedClass(result, clazz);
            }

        return result;
        }

    /** add this class, which has annotations, to the map of classes to register */
    private void addAnnotatedClass(HashMap<String, LinkedList<Class>> map, Class clazz)
        {
        // Locate TeleOp and Autonomous pairs
        if (clazz.isAnnotationPresent(TeleOp.class))
            {
            Annotation annotation = clazz.getAnnotation(TeleOp.class);
            String name = ((TeleOp) annotation).pairWithAuto();
            if (name.equals(""))
                addToMap(map, clazz);
            else
                addToMap(map, name, clazz);
            }

        if (clazz.isAnnotationPresent(Autonomous.class))
            {
            Annotation annotation = clazz.getAnnotation(Autonomous.class);
            String name = ((Autonomous) annotation).pairWithTeleOp();
            if (name.equals(""))
                addToMap(map, clazz);
            else
                addToMap(map, name, clazz);
            }
        }

    /** Add a class to the map under the indicated key */
    private void addToMap(HashMap<String, LinkedList<Class>> map, String key, Class clazz)
        {
        if (map.containsKey(key))
            {
            map.get(key).add(clazz);
            }
        else
            {
            LinkedList<Class> temp = new LinkedList<>();
            temp.add(clazz);
            map.put(key, temp);
            }
        }

    /** Add a class to the map undre a key we make up */
    private void addToMap(HashMap<String, LinkedList<Class>> map, Class clazz)
        {
        int i = 0;
        while (map.containsKey(Integer.toString(i)))
            i++;
        addToMap(map, Integer.toString(i), clazz);
        }

    /** Returns the name we are to use for this class in the driver station display */
    private String getOpModeName(Class<? extends OpMode> opMode)
        {
        String name;
        if (opMode.isAnnotationPresent(TeleOp.class))
            name = opMode.getAnnotation(TeleOp.class).name();
        else if (opMode.isAnnotationPresent(Autonomous.class))
            name = opMode.getAnnotation(Autonomous.class).name();
        else
            name = opMode.getSimpleName();
        if (name.equals(""))
            name = opMode.getSimpleName();
        return name;
        }

    /** Answers whether one class is or inhertits from another */
    private boolean inheritsFrom(Class baseClass, Class superClass)
        {
        while (baseClass != null)
            {
            if (baseClass == superClass)
                return true;
            baseClass = baseClass.getSuperclass();
            }
        return false;
        }
    }
