package org.firstinspires.ftc.team9853.utils;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeMeta;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ClassFilter;
import com.qualcomm.robotcore.util.ClassManager;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;

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
        ClassManager classManager = new ClassManager();
        AnnotationRegistry registry = new AnnotationRegistry();

        classManager.registerFilter(registry);

        classManager.processAllClasses();

        for (Class<OpMode> clazz : annotatedClasses) {
            try {
                Boolean isAutonomousRnB = clazz.isAnnotationPresent(AutonomousRnB.class);

                if (isAutonomousRnB) {
                    Annotation annotation = clazz.getAnnotation(AutonomousRnB.class);
                    String opModeName = ((AutonomousRnB) annotation).name();
                    String opModeGroup = ((AutonomousRnB) annotation).group();
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
                registry.reportOpModeConfigurationError("Got error while trying to instantiate class '%s': '%s'", clazz.getSimpleName(), err.getMessage());
            }
        }
    }

    /**
     * Filters all of the classes and extracts the classes with the custom annotations
     *
     * @param clazz the class to check
     */
    public void filter(Class clazz) {
        Boolean isAutonomousRnB = clazz.isAnnotationPresent(AutonomousRnB.class);

        if (! isAutonomousRnB) return;

        Boolean isTeleOp = clazz.isAnnotationPresent(TeleOp.class);
        Boolean isAutonomous = clazz.isAnnotationPresent(Autonomous.class);

        // check that the class is only using AutonomousRnB
        if (isTeleOp) {
            reportOpModeConfigurationError("'%s' class is annotated both as 'TeleOp' and 'AutonomousRnB'; please choose at most one", clazz.getSimpleName());
            return;
        }

        if (isAutonomous) {
            reportOpModeConfigurationError("'%s' class is annotated both as 'Autonomous' and 'AutonomousRnB'; please choose at most one", clazz.getSimpleName());
            return;
        }

        // check that the class inherits from OpMode
        if (! ClassUtil.inheritsFrom(clazz, OpMode.class)) {
            reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return;
        }

        // check that the class has the right modifiers
        if (! Modifier.isPublic(clazz.getModifiers())) {
            reportOpModeConfigurationError("'%s' class is not declared 'public'", clazz.getSimpleName());
            return;
        }


        try {
            if (isAutonomousRnB) {
                // ensure that the constructor exists
                clazz.getConstructor(Boolean.TYPE);

                annotatedClasses.add(clazz);
            }
        } catch (Exception err) {
            reportOpModeConfigurationError("'%s' class does not contain the necessary constructor", clazz.getSimpleName());
        }
    }

    /**
     * Reports an error that was encountered while configuring the opmode
     *
     * @param format    the format string
     * @param args      the substring args
     */
    public void reportOpModeConfigurationError(String format, Object... args) {
        String message = String.format(format, args);
        // Show the message in the log
        Log.w(TAG, String.format("configuration error: %s", message));
        // Make the message appear on the driver station (only the first one will actually appear)
        RobotLog.setGlobalErrorMsg(message);
    }
}
