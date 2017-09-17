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
    public static final String TAG = AnnotationRegistry.class.getSimpleName();
    private static final ArrayList<Class<OpMode>> annotatedClasses = new ArrayList();

    private ArrayList<OpModeAnnotation> opModeAnnotations = new ArrayList<>();

    private static class InstanceHolder {
        public static AnnotationRegistry theInstance = new AnnotationRegistry();
    }

    public static AnnotationRegistry getInstance() {
        return InstanceHolder.theInstance;
    }

    /**
     * Register all of the opmodes using the custom annotations
     *
     * @param manager       the opmode manager
     */
    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        AnnotationRegistry registry = getInstance();

        // add annotations to use
        registry.useAnnotation(AutonomousRnB.class, (Class clazz) -> {
            try {
                // ensure that the constructor exists
                clazz.getConstructor(Boolean.TYPE);

                return true;
            } catch (Exception err) {
                return false;
            }
        }, (Class<OpMode> clazz, OpModeManager opModeManager) -> {
            String opModeName = getOpModeName(clazz);
            String opModeGroup = getOpModeGroup(clazz);
            Constructor<OpMode> constructor = clazz.getConstructor(Boolean.TYPE);

            Log.d(TAG, "Registered \"" + opModeName + "\" opmode using AutonomousRnB");

            manager.register(
                    new OpModeMeta(opModeName + " (Red)", OpModeMeta.Flavor.AUTONOMOUS, opModeGroup),
                    constructor.newInstance(true)
            );

            manager.register(
                    new OpModeMeta(opModeName + " (Blue)", OpModeMeta.Flavor.AUTONOMOUS, opModeGroup),
                    constructor.newInstance(false)
            );
        });

        registry.doRegister(manager);
    }

    /**
     * Preforms all of the opmode annotation's register and filter functions
     *
     * @param manager   the opmode manager
     */
    private void doRegister(OpModeManager manager) {
        ClassManager classManager = ClassManager.getInstance();

        classManager.registerFilter(getInstance());

        classManager.processAllClasses();

        for (OpModeAnnotation opModeAnnotation : opModeAnnotations) {
            opModeAnnotation.doRegister(manager);
        }
    }

    /**
     * A filter function for annotated class
     */
    interface AnnotationFilterer {
        boolean filter(Class clazz);
    }

    /**
     * A register function for annotated opmodes
     */
    interface AnnotatedOpModeRegister {
        void register(Class<OpMode> clazz, OpModeManager manager) throws Exception;
    }

    /**
     * Handles filtering and registration for a opmode annotation
     */
    private class OpModeAnnotation {
        public Class<? extends Annotation> annotation;

        private AnnotationFilterer filterer;
        private AnnotatedOpModeRegister register;
        private ArrayList<Class<OpMode>> annotatedOpModes = new ArrayList<>();

        /**
         * Creates an instance of OpModeAnnotation
         * @param annotation    the annotation class
         * @param filterer      the filter function
         * @param registerer    the register function
         */
        public OpModeAnnotation(Class<? extends  Annotation> annotation, AnnotationFilterer filterer, AnnotatedOpModeRegister registerer) {
            this.annotation = annotation;
            this.filterer = filterer;
            this.register = registerer;
        }

        /**
         * Filters the class for the opmode annotation
         *
         * @param clazz the class to filter
         */
        public void doFilter(Class clazz) {
            if (! clazz.isAnnotationPresent(annotation)) return;

            if (filterer.filter(clazz)) {
                annotatedOpModes.add(clazz);
            }
        }

        /**
         * Registers all opmodes using the annotation
         *
         * @param manager   the opmode manager
         */
        public void doRegister(OpModeManager manager) {
            for (Class<OpMode> clazz : annotatedOpModes) {
                try {
                    register.register(clazz, manager);
                } catch (Exception err) {
                    reportOpModeConfigurationError("Encountered an error while registering '%s': %s",
                            clazz.getSimpleName(), err.getStackTrace());
                }
            }
        }
    }

    /**
     * Tells AnnotationRegistry to use a opmode annotation
     *
     * @param annotation    the annotation
     * @param filterer      the annotation filter function
     * @param registerer    the opmode register function
     */
    public void useAnnotation(Class<? extends  Annotation> annotation, AnnotationFilterer filterer, AnnotatedOpModeRegister registerer) {
        useAnnotation(new OpModeAnnotation(annotation, filterer, registerer));
    }

    /**
     * Tells AnnotationRegistry to use a opmode annotation
     *
     * @param annotation    the opmode annotation
     */
    public void useAnnotation(OpModeAnnotation annotation) {
        opModeAnnotations.add(annotation);
    }


    @Override
    public void filterAllClassesStart() {
        // Do nothing
    }

    /**
     * Filters all of the classes and extracts the classes with the custom annotations
     *
     * @param clazz the class to check
     */
    @Override
    public void filterClass(Class clazz) {
        if (clazz.isAnnotationPresent(Disabled.class)) return;

        // check that class only uses one opmode annotation
        int opModeAnnotationCount = Boolean.compare(clazz.isAnnotationPresent(TeleOp.class), false)
                + Boolean.compare(clazz.isAnnotationPresent(Autonomous.class), false);

        for (OpModeAnnotation opModeAnnotation : opModeAnnotations) {
            if(clazz.isAnnotationPresent(opModeAnnotation.annotation)) opModeAnnotationCount ++;
        }

        if (opModeAnnotationCount == 0 ){
            return;
        } else if (opModeAnnotationCount > 1) {
            reportOpModeConfigurationError("'%s' class is annotated by multiple opmode annotations", clazz.getSimpleName());
            return;
        }

        // ensure that the class is an opmode
        if (! ClassUtil.inheritsFrom(clazz, OpMode.class)) {
            reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return;
        }

        // ensure that the class is public
        if (! Modifier.isPublic(clazz.getModifiers())) {
            reportOpModeConfigurationError("'%s' class is not declared 'public'", clazz.getSimpleName());
            return;
        }

        // validate opmode name
        String name = getOpModeName(clazz);
        if (name.equals(OpModeManager.DEFAULT_OP_MODE_NAME) || name.trim().equals("")) {
            reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", name);
            return;
        }

        // call filter functions
        for (OpModeAnnotation opModeAnnotation : opModeAnnotations) {
            if (clazz.isAnnotationPresent(opModeAnnotation.annotation)) {
                opModeAnnotation.doFilter(clazz);
            }
        }
    }

    @Override
    public void filterAllClassesComplete() {
        // do nothing
    }

    @Override
    public void filterOnBotJavaClassesStart() {
        // Do nothing
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
    private static void reportOpModeConfigurationError(String format, Object... args) {
        String message = String.format(format, args);
        // Show the message in the log
        Log.w(TAG, String.format("configuration error: %s", message));
        // Make the message appear on the driver station (only the first one will actually appear)
        RobotLog.setGlobalErrorMsg(message);
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
}
