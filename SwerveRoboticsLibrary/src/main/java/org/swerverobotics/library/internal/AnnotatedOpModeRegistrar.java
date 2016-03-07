package org.swerverobotics.library.internal;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.robocol.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
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

    private static final String              LOGGING_TAG = SynchronousOpMode.LOGGING_TAG;

    List<Class>                              allClasses;
    List<String>                             packagesAndClassesToIgnore;

    final OpModeManager                      opModeManager;
    final Context                            context;
    final DexFile                            dexFile;

    final HashMap<String, LinkedList<Class<OpMode>>> opModeGroups;                  // key == group name
    final String                             defaultOpModeGroupName = "$$$$$$$";    // arbitrary, but unlikely to be used by users
    final HashSet<Class>                     classesSeen;
    final HashMap<Class, String>             classNameOverrides;

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
            registrar = new AnnotatedOpModeRegistrar(manager);
            }
        catch (Exception e)
            {
            Log.wtf(LOGGING_TAG, e);
            registrar = null;
            }

        if (registrar != null)
            registrar.doRegistration();
        }

    private AnnotatedOpModeRegistrar(final OpModeManager opModeManager) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
        {
        this.allClasses                = new LinkedList<Class>();
        this.opModeManager             = opModeManager;
        this.classNameOverrides        = new HashMap<>();
        this.opModeGroups              = new HashMap<>();
        this.classesSeen               = new HashSet<>();

        // We ignore certain packages to make us more robust
        this.packagesAndClassesToIgnore = new LinkedList<>();
        this.packagesAndClassesToIgnore.add("com.google");
        this.packagesAndClassesToIgnore.add("io.netty");

        // Find the file in which we are executing
        this.context = getApplicationContextRaw();
        this.dexFile = new DexFile(this.context.getPackageCodePath());
        }

    static Context getApplicationContextRaw() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
        {
        Class<?> activityThreadClass    = Class.forName("android.app.ActivityThread");
        Method methodCurrentApplication = activityThreadClass.getMethod("currentApplication");
        return (Application) methodCurrentApplication.invoke(null, (Object[]) null);
        }

    /** Use magic to find the current application context */
    public static Context getApplicationContext()
        {
        try {
            return getApplicationContextRaw();
            }
        catch (Exception e)
            {
            return null;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    void doRegistration()
    // The body of this is from the following, without which we could not have been successful
    // in this endeavor.
    //      https://github.com/dmssargent/Xtensible-ftc_app/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/FtcOpModeRegister.java
    // Many thanks.
        {
        // Help with later debugging
        Thread.currentThread().setName("FtcRobotControllerService$b");

        // Find every class of potential interest, just once
        this.allClasses = findAllClasses();

        // Find all the candidates
        this.findOpModesFromClassAnnotations();
        this.processAnnotatedStaticMethods();

        // Sort the linked lists within opModes, first by flavor and second by name
        Comparator<Class<OpMode>> comparator = new Comparator<Class<OpMode>>()
            {
            @Override public int compare(Class<OpMode> lhs, Class<OpMode> rhs)
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
        for (String key : opModeGroups.keySet())
            {
            Collections.sort(opModeGroups.get(key), comparator);
            }

        // Display each group on the driver station in alphabetical order according
        // to the name of the first member of each group.
        TreeMap<String, LinkedList<Class<OpMode>>> sortedOpModes = new TreeMap<>();
        for (String groupName : opModeGroups.keySet())
            {
            Class<OpMode> groupSortKey = opModeGroups.get(groupName).getFirst();
            sortedOpModes.put(getOpModeName(groupSortKey), opModeGroups.get(groupName));
            }

        // Flatten the list
        List<Class<OpMode>> opModesToRegister = new LinkedList<Class<OpMode>>();
        for (LinkedList<Class<OpMode>> opModeList : sortedOpModes.values())
            {
            for (Class<OpMode> opMode : opModeList)
                {
                opModesToRegister.add(opMode);
                }
            }

        // Finally, register all the OpModes
        for (Class<OpMode> opMode : opModesToRegister)
            {
            String name = getOpModeName(opMode);
            this.opModeManager.register(name, opMode);
            Log.d(LOGGING_TAG, String.format("registered {%s} as {%s}", opMode.getSimpleName(), name));
            }
        }

    void reportOpModeConfigurationError(String format, Object... args)
        {
        String message = String.format(format, args);
        // Show the message in the log
        Log.w(LOGGING_TAG, String.format("configuration error: %s", message));
        // Make the message appear on the driver station (only the first one will actually appear)
        RobotLog.setGlobalErrorMsg(message);
        }

    /**
     * Find the list of OpMode classes which should be registered by looking
     * in the class annotations.
     */
    private void findOpModesFromClassAnnotations()
        {
        for (Class clazz : this.allClasses)
            {
            boolean isTeleOp     = clazz.isAnnotationPresent(TeleOp.class);
            boolean isAutonomous = clazz.isAnnotationPresent(Autonomous.class);

            // If it's neither teleop or autonomous, then it's not interesting to us
            if (!isTeleOp && !isAutonomous)
                continue;

            // If we have BOTH Autonomous and TeleOp annotations on a class, that's an error, we'll ignore it.
            if (isTeleOp && isAutonomous)
                {
                reportOpModeConfigurationError("'%s' class is annotated both as 'TeleOp' and 'Autonomous'; please choose at most one", clazz.getSimpleName());
                continue;
                }

            // There's some things we need to check about the actual class
            if (!checkOpModeClassConstraints(clazz, null))
                continue;

            // If the class has been annotated as @Disabled, then ignore it
            if (clazz.isAnnotationPresent(Disabled.class))
                continue;

            // It passes all our tests, add it!
            addAnnotatedOpMode(clazz);
            }
        }

    boolean checkOpModeClassConstraints(Class clazz, String opModeName)
        {
        // If the class doesn't extend OpMode, that's an error, we'll ignore it
        if (!isOpMode(clazz))
            {
            reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return false;
            }

        // If it's not 'public', it can't be loaded by the system and won't work. We report
        // the error and ignore
        if (!Modifier.isPublic(clazz.getModifiers()))
            {
            reportOpModeConfigurationError("'%s' class is not declared 'public'", clazz.getSimpleName());
            return false;
            }

        // Some opmode names aren't allowed to be used
        if (opModeName == null)
            {
            opModeName = getOpModeName((Class<OpMode>)clazz);
            }
        if (!isLegalOpModeName(opModeName))
            {
            reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", opModeName);
            return false;
            }

        return true;
        }

    private void processAnnotatedStaticMethods()
        {
        // This will, nicely, have duplicates removed by the time we get here. But it might contain methods
        // we can't actually invoke, so beware.
        Set<Method> registrarMethods = new HashSet<Method>();
        Set<Method> onRobotRunningMethods = new HashSet<Method>();
        Set<Method> onRobotStartupFailureMethods = new HashSet<Method>();

        findAnnotatedStaticMethodsOfInterest(registrarMethods, onRobotRunningMethods, onRobotStartupFailureMethods);

        // Call the OpMode registration methods now
        AnnotationOpModeManager manager = new AnnotationOpModeManager();
        for (Method method : registrarMethods)
            {
            try {
                // We support both with and without a context for compatibility
                if (getParameterCount(method)==1)
                    method.invoke(null, manager);
                else if (getParameterCount(method)==2)
                    method.invoke(null, context, manager);
                }
            catch (Exception e)
                {
                // ignored
                }
            }

        // Remember the robot start methods for later
        RobotStateTransitionNotifier.setStateTransitionCallbacks(context, onRobotRunningMethods, onRobotStartupFailureMethods);
        }

    private int getParameterCount(Method method)
        {
        Class<?>[] parameters = method.getParameterTypes();
        return parameters.length;
        }

    private void findAnnotatedStaticMethodsOfInterest(Set<Method> registrarMethods, Set<Method> onRobotRunningMethods, Set<Method> onRobotStartupFailureMethods)
        {
        for (Class clazz : this.allClasses)
            {
            List<Method> methods = Util.getDeclaredMethodsIncludingSuper(clazz);
            for (Method method : methods)
                {
                int requiredModifiers   = Modifier.STATIC | Modifier.PUBLIC;
                int prohibitedModifiers = Modifier.ABSTRACT;
                if (!((method.getModifiers() & requiredModifiers) == requiredModifiers && (method.getModifiers() & prohibitedModifiers) == 0))
                    continue;

                Class<?>[] parameters = method.getParameterTypes();

                if (method.isAnnotationPresent(OpModeRegistrar.class))
                    {
                    // the 1-parameter version is legacy (just a manager) instead of also a context
                    if (getParameterCount(method)==1 || getParameterCount(method)==2)
                        registrarMethods.add(method);
                    }

                if (method.isAnnotationPresent(OnRobotRunning.class))
                    {
                    if (getParameterCount(method)==1)
                        onRobotRunningMethods.add(method);
                    }

                if (method.isAnnotationPresent(OnRobotStartupFailure.class))
                    {
                    if (getParameterCount(method)==1)
                        onRobotStartupFailureMethods.add(method);
                    }
                }
            }
        }

    class AnnotationOpModeManager implements IOpModeManager
        {
        public void register(Class clazz)
            {
            if (checkOpModeClassConstraints(clazz, null))
                {
                addAnnotatedOpMode((Class<OpMode>)clazz);
                }
            }

        public void register(String name, Class clazz)
            {
            if (checkOpModeClassConstraints(clazz, name))
                {
                addUserNamedOpMode((Class<OpMode>)clazz, name);
                }
            }

        public void register(String name, OpMode opModeInstance)
            {
            // We just go ahead and register this, as there's nothing else to do.
            opModeManager.register(name, opModeInstance);
            Log.d(LOGGING_TAG, String.format("registered instance {%s} as {%s}", opModeInstance.toString(), name));
            }
        }

    /**
     * Find all the classes in the context in which we should consider looking, which
     * (currently?) is the entire .APK in which we are found.
     */
    private List<Class> findAllClasses()
        {
        List<Class> result = new LinkedList<Class>();

        // Iterate over all the classes in this whole .APK
        LinkedList<String> classNames = new LinkedList<>(Collections.list(dexFile.entries()));
        for (String className : classNames)
            {
            // Ignore classes that are in some packages that we know aren't worth considering
            boolean shouldIgnore = false;
            for (String packageName : packagesAndClassesToIgnore)
                {
                if (Util.isPrefixOf(packageName, className))
                    {
                    shouldIgnore = true;
                    break;
                    }
                }
            if (shouldIgnore)
                continue;

            // Get the Class from the className
            Class clazz;
            try {
                clazz = Class.forName(className, false, context.getClassLoader());
                }
            catch (NoClassDefFoundError|ClassNotFoundException ex)
                {
                // We can't find that class
                Log.w(LOGGING_TAG, className + " " + ex.toString(), ex);
                if (className.contains("$"))
                    {
                    // Prevent loading similar inner classes, a performance optimization
                    className = className.substring(0, className.indexOf("$") /*- 1*/);
                    }

                packagesAndClassesToIgnore.add(className);
                continue;
                }

            // Remember that class
            result.add(clazz);
            }

        return result;
        }

    /** add this class, which has opmode annotations, to the map of classes to register */
    private void addAnnotatedOpMode(Class<OpMode> clazz)
        {
        if (clazz.isAnnotationPresent(TeleOp.class))
            {
            Annotation annotation = clazz.getAnnotation(TeleOp.class);
            String groupName = ((TeleOp) annotation).group();
            addOpModeWithGroupName(clazz, groupName);
            }

        if (clazz.isAnnotationPresent(Autonomous.class))
            {
            Annotation annotation = clazz.getAnnotation(Autonomous.class);
            String groupName = ((Autonomous) annotation).group();
            addOpModeWithGroupName(clazz, groupName);
            }
        }

    private void addOpModeWithGroupName(Class<OpMode> clazz, String groupName)
        {
        if (groupName.equals(""))
            addToOpModeGroup(defaultOpModeGroupName, clazz);
        else
            addToOpModeGroup(groupName, clazz);
        }

    /** Add a class for which the user has provided the name as opposed to
     *  the name being taken from the class and its own annotations */
    private void addUserNamedOpMode(Class<OpMode> clazz, String name)
        {
        addToOpModeGroup(defaultOpModeGroupName, clazz);
        this.classNameOverrides.put(clazz, name);
        }

    /** Add a class to the map under the indicated key */
    private void addToOpModeGroup(String groupName, Class<OpMode> clazz)
        {
        // Have we seen this class before?
        if (!this.classesSeen.contains(clazz))
            {
            this.classesSeen.add(clazz);

            if (this.opModeGroups.containsKey(groupName))
                {
                this.opModeGroups.get(groupName).add(clazz);
                }
            else
                {
                LinkedList<Class<OpMode>> temp = new LinkedList<>();
                temp.add(clazz);
                this.opModeGroups.put(groupName, temp);
                }
            }
        else
            {
            // We've already got this class somewhere; don't put it in a second time.
            }
        }

    /** Returns the name we are to use for this class in the driver station display */
    private String getOpModeName(Class<OpMode> opMode)
        {
        String name;

        if (this.classNameOverrides.containsKey(opMode))
            name = this.classNameOverrides.get(opMode);
        else if (opMode.isAnnotationPresent(TeleOp.class))
            name = opMode.getAnnotation(TeleOp.class).name();
        else if (opMode.isAnnotationPresent(Autonomous.class))
            name = opMode.getAnnotation(Autonomous.class).name();
        else
            name = opMode.getSimpleName();

        if (name.equals(""))
            name = opMode.getSimpleName();

        return name;
        }

    private boolean isLegalOpModeName(String name)
        {
        if (name == null)
            return false;
        switch (name)
            {
            case "":
            case "Stop Robot":  // Reserved for use by the runtime infrastructure
                return false;
            default:
                return true;
            }
        }

    private boolean isOpMode(Class clazz)
        {
        return inheritsFrom(clazz, OpMode.class);
        }

    /** Answers whether one class is or inherits from another */
    private static boolean inheritsFrom(Class baseClass, Class superClass)
        {
        while (baseClass != null)
            {
            if (baseClass == superClass)
                return true;
            baseClass = baseClass.getSuperclass();
            }
        return false;
        }

    /**
     * An OpMode injected whenever too many OpModes have been declared, or the size of the characters
     * in the names is greater than the allowable number within the FIRST SDK
     */
    public class OpModeNamesTooLong extends OpMode
        {
        @Override public void init()
            {
            telemetry.addData("", "The overall length of OpMode names is too long");
            }

        @Override public void loop()
            {
            telemetry.addData("", "The overall length of OpMode names is too long");
            }
        }
    }
