package org.firstinspires.ftc.team9853.utils;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassManager;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaClassLoader;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/10/2017
 */
public class AnnotationRegistry implements ClassFilter {
    private static final String TAG = AnnotationRegistry.class.getSimpleName();
    private static ArrayList<Class<OpMode>> annotatedClasses= new ArrayList();

    /**
     * Register all of the opmodes using the custom annotations
     *
     * @param manager       the opmode manager
     * @throws IOException
     */
    @OpModeRegistrar
    public static void register(OpModeManager manager) throws IOException {
        ClassManager classManager = ClassManager.getInstance();
        AnnotationRegistry registry = new AnnotationRegistry();

        classManager.registerFilter(registry);

        classManager.processAllClasses();

        for (Class<OpMode> clazz : annotatedClasses) {
            try {
                if (clazz.isAnnotationPresent(AutonomousRnB.class)) {
                    Annotation annotation = clazz.getAnnotation(AutonomousRnB.class);
                    String opModeName = getOpModeName(clazz);
                    String opModeGroup = getOpModeGroup(clazz);
                    Constructor<OpMode> constructor = clazz.getConstructor(Boolean.TYPE);

                    Log.d(TAG, "Registered " + opModeName + " opmode using AutonomousRnB");

                    manager.register(
                            new OpModeMeta(opModeName + " (Red)", OpModeMeta.Flavor.AUTONOMOUS, opModeGroup),
                            constructor.newInstance(true)
                    );

                    manager.register(
                            new OpModeMeta(opModeName + " (Blue)", OpModeMeta.Flavor.AUTONOMOUS, opModeGroup),
                            constructor.newInstance(false)
                    );
                }
            } catch (Exception err) {
                reportOpModeConfigurationError("Encountered error while trying to instantiate class '%s': '%s'", clazz.getSimpleName(), err.getMessage());
            }
        }
    }

    @Override
    public void filterAllClassesStart() {
        annotatedClasses.clear();
    }

    /**
     * Filters all of the classes and extracts the classes with the custom annotations
     *
     * @param clazz the class to check
     */
    @Override
    public void filterClass(Class clazz) {
        if (! isSupportedAnnotation(clazz)) return;
        if (clazz.isAnnotationPresent(Disabled.class)) return;

        // check that class only uses one of these annotations
        if ((Boolean.compare(clazz.isAnnotationPresent(TeleOp.class), false)
                + Boolean.compare(clazz.isAnnotationPresent(Autonomous.class), false)
                + Boolean.compare(clazz.isAnnotationPresent(AutonomousRnB.class), false)
        ) > 1) {
            reportOpModeConfigurationError("'%s' class is annotated by multiple opmode annotations", clazz.getSimpleName());
            return;
        }

        if (! ClassUtil.inheritsFrom(clazz, OpMode.class)) {
            reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return;
        }

        if (! Modifier.isPublic(clazz.getModifiers())) {
            reportOpModeConfigurationError("'%s' class is not declared 'public'", clazz.getSimpleName());
            return;
        }

        String name = getOpModeName(clazz);
        if (name.equals(OpModeManager.DEFAULT_OP_MODE_NAME) || name.trim().equals("")) {
            reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", name);
            return;
        }

        if (! hasConstructor(clazz)) {
            reportOpModeConfigurationError("'%s' class does not contain the necessary constructor", clazz.getSimpleName());
            return;
        }

        annotatedClasses.add(clazz);
    }

    @Override
    public void filterAllClassesComplete() {
        // nothing
    }

    @Override
    public void filterOnBotJavaClassesStart() {
        for (Class<OpMode> clazz : annotatedClasses) {
            if (OnBotJavaClassLoader.isOnBotJava(clazz))
                annotatedClasses.remove(clazz);
        }
    }

    @Override
    public void filterOnBotJavaClass(Class clazz) {
        filterClass(clazz);
    }

    @Override
    public void filterOnBotJavaClassesComplete() {
        // nothing
    }

    /**
     * Reports an error that was encountered while configuring the opmode
     *
     * @param format    the format string
     * @param args      the substring args
     */
    public static void reportOpModeConfigurationError(String format, Object... args) {
        String message = String.format(format, args);
        // Show the message in the log
        Log.w(TAG, String.format("configuration error: %s", message));
        // Make the message appear on the driver station (only the first one will actually appear)
        RobotLog.setGlobalErrorMsg(message);
    }

    /**
     * Check whether or not this class is using a annotation that is supported
     *
     * @param clazz the class to check
     * @return      whether or not this class is using a annotation that is supported
     */
    private static Boolean isSupportedAnnotation(Class<OpMode> clazz) {
        return clazz.isAnnotationPresent(AutonomousRnB.class);
    }

    /**
     * Gets the name of the opmode
     *
     * @param clazz the opmode class
     * @return      the name of the opmode
     */
    private static String getOpModeName(Class<OpMode> clazz) {
        String name = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(AutonomousRnB.class)) {
            name = clazz.getAnnotation(AutonomousRnB.class).name();
        }

        if (name.trim().equals("")) {
            name = clazz.getSimpleName();
        }

        return name;
    }

    /**
     * gets the opmode's group
     *
     * @param clazz the opmode class
     * @return      the opmode's group
     */
    private static String getOpModeGroup(Class<OpMode> clazz) {
        String group = "";

        if (clazz.isAnnotationPresent(AutonomousRnB.class)) {
            group = clazz.getAnnotation(AutonomousRnB.class).group();
        }

        if (group.trim().equals("")) {
            group = OpModeMeta.DefaultGroup;
        }

        return group;
    }

    /**
     * Check whether or not the opmode has the required constructors
     *
     * @param clazz the opmode class
     * @return      whether or not the opmode has the required constructors
     */
    private static Boolean hasConstructor(Class<OpMode> clazz) {
        try {
            if (clazz.isAnnotationPresent(AutonomousRnB.class)) {
                // ensure that the constructor exists
                clazz.getConstructor(Boolean.TYPE);

                return true;
            }

            return false;
        } catch (Exception err) {
            return false;
        }
    }
}
