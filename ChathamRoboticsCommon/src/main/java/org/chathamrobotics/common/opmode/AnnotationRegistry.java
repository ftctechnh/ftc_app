package org.chathamrobotics.common.opmode;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ClassUtil;

import org.chathamrobotics.common.robot.RobotErrors;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassManager;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Registers custom opmode annotations
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class AnnotationRegistry implements ClassFilter {
    private static final String TAG = AnnotationRegistry.class.getSimpleName();
    private static final AnnotationHandler[] ANNOTATION_HANDLERS = {
            new AnnotationHandler(AutonomousRnB.class, clazz -> {
                try {
                    //noinspection unchecked
                    clazz.getConstructor(Boolean.TYPE);
                    return true;
                } catch (Exception e) {return false;}
            }, (clazz, manager) -> {
                String name = getOpModeName(clazz), group = getOpModeGroup(clazz);
                Constructor<OpMode> constructor = clazz.getConstructor(Boolean.TYPE);

                manager.register(
                        new OpModeMeta(name + " (Red)", OpModeMeta.Flavor.AUTONOMOUS, (group.equals(OpModeMeta.DefaultGroup) ? "Red" : group)),
                        constructor.newInstance(true)
                );

                manager.register(
                        new OpModeMeta(name + " (Blue)", OpModeMeta.Flavor.AUTONOMOUS, (group.equals(OpModeMeta.DefaultGroup) ? "Blue" : group)),
                        constructor.newInstance(false)
                );
            }),
    };
    private static final AnnotationRegistry theInstance = new AnnotationRegistry();

    /**
     * Registers all of the opmodes using the custom annotations
     * @param manager   the opmode manager
     */
    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        ClassManager classManager = ClassManager.getInstance();

        classManager.registerFilter(theInstance);

        classManager.processAllClasses();

        for (AnnotationHandler handler : ANNOTATION_HANDLERS)
            handler.doRegister(manager);
    }

    /**
     * Returns the opmode's name
     * @param clazz the opmode
     * @return      the opmode's name
     */
    private static String getOpModeName(Class<? extends OpMode> clazz) {
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
     * A handler of a opmode annotation
     */
    private static class AnnotationHandler {

        public interface Filterer {
            boolean filter(Class clazz);
        }

        public interface Registerer {
            void register(Class<OpMode> clazz, OpModeManager manager) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
        }

        private final Class<? extends Annotation> annotation;
        private final Filterer filterer;
        private final Registerer registerer;
        private ArrayList<Class<OpMode>> opModeCache = new ArrayList<>();

        public AnnotationHandler(Class<? extends Annotation> annotation, Filterer filterer, Registerer registerer) {
            this.annotation = annotation;
            this.filterer = filterer;
            this.registerer = registerer;
        }

        public void doFilter(Class clazz) {
            if (! clazz.isAnnotationPresent(annotation)) return;

            if (filterer.filter(clazz) && OpMode.class.isAssignableFrom(clazz) && ! opModeCache.contains(clazz)) {
                //noinspection unchecked
                opModeCache.add((Class<OpMode>) clazz);
            }
        }

        public void doRegister(OpModeManager manager) {
            for (Class<OpMode> clazz : opModeCache) {
                try {
                    registerer.register(clazz, manager);
                } catch (Exception err) {
                    RobotErrors.reportGlobalError(TAG, err, "Encountered an error while registering '%s'", clazz.getSimpleName());
                }
            }
        }

        public void clear() {opModeCache.clear();}

        public boolean hasAnnotation(Class clazz) {return clazz.isAnnotationPresent(annotation);}
    }

    private static boolean isOpMode(Class clazz) {
        return ClassUtil.inheritsFrom(clazz, OpMode.class);
    }

    @Override
    public void filterAllClassesStart() {
        for (AnnotationHandler handler : ANNOTATION_HANDLERS) handler.clear();
    }

    @Override
    public void filterClass(Class clazz) {
        if (clazz.isAnnotationPresent(Disabled.class)) return;

        int opModeCount = 0;

        for (AnnotationHandler handler : ANNOTATION_HANDLERS)
            if (handler.hasAnnotation(clazz)) opModeCount++;
        if (opModeCount == 0) return;

        if (clazz.isAnnotationPresent(Autonomous.class)) opModeCount++;
        if (clazz.isAnnotationPresent(TeleOp.class)) opModeCount++;

        if (opModeCount == 0) return;
        else if (opModeCount > 1) {
            if (clazz.getAnnotations().length < 2) return; // idk why this happens

            RobotErrors.reportGlobalError(TAG,"'%s' class is annotated by multiple opmode annotations", clazz.getSimpleName());
            return;
        }

        // ensure that the class is an opmode
        if (! isOpMode(clazz)) {
            RobotErrors.reportGlobalError(TAG,"'%s' class doesn't inherit from the class 'OpMode'", clazz.getSimpleName());
            return;
        }

        // ensure that the class is public
        if (! Modifier.isPublic(clazz.getModifiers())) {
            RobotErrors.reportGlobalError(TAG,"'%s' class is not declared 'public'", clazz.getSimpleName());
            return;
        }

        // validate opmode name
        @SuppressWarnings("unchecked")
        String name = getOpModeName((Class<OpMode>) clazz);
        if (name.equals(OpModeManager.DEFAULT_OP_MODE_NAME) || name.trim().equals("")) {
            RobotErrors.reportGlobalError(TAG, "\"%s\" is not a legal OpMode name", name);
            return;
        }

        for (AnnotationHandler handler : ANNOTATION_HANDLERS)
            if (handler.hasAnnotation(clazz)) handler.doFilter(clazz);
    }

    @Override
    public void filterAllClassesComplete() {}


    @Override
    public void filterOnBotJavaClassesStart() {
        for (AnnotationHandler handler : ANNOTATION_HANDLERS) handler.clear();
    }

    @Override
    public void filterOnBotJavaClass(Class clazz) {
        filterClass(clazz);
    }

    @Override
    public void filterOnBotJavaClassesComplete() {}
}
