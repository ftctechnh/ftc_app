package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
//import com.qualcomm.robotcore.eventloop.opmode.OpModeMeta; //FIXME: idk where OpModeMeta went
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/3/2017
 */
public class CustomRegistry {
    @OpModeRegistrar
    public static void register(OpModeManager manager) throws ClassNotFoundException, IOException, URISyntaxException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        Iterable<Class> autoRnBOpModes = getClassesWithAnnotation(AutonomousRnB.class, (new CustomRegistry()).getClass().getPackage().getName());

        for (Class<OpMode> clazz : autoRnBOpModes) {
            Annotation annotation = clazz.getAnnotation(Autonomous.class);
            String groupName = ((Autonomous) annotation).group();
            String opModeName = clazz.getSimpleName();

            // red
//            manager.register(
//                    new OpModeMeta("Red: " + opModeName, OpModeMeta.Flavor.AUTONOMOUS, groupName),
//                    clazz.getConstructor(Boolean.class).newInstance(true)
//            ); // FIXME: idk where OpModeMeta went

            // blue
//            manager.register(
//                    new OpModeMeta("Blue: " + opModeName, OpModeMeta.Flavor.AUTONOMOUS, groupName),
//                    clazz.getConstructor(Boolean.class).newInstance(false)
//            ); // FIXME: idk where OpModeMeta went
        }
    }

    private static Iterable<Class> getClassesWithAnnotation(Class<? extends Annotation> annotationClass, String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        Iterable<Class> classes = getClasses(packageName);
        List<Class> annotatedClasses = new ArrayList<Class>();

        for (Class clazz : classes) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                annotatedClasses.add(clazz);
            }
        }

        return annotatedClasses;
    }

    private static Iterable<Class> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        List<Class> classes = new ArrayList<Class>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }

        for (File dir : dirs) {
            classes.addAll(findClasses(dir, packageName));
        }

        return classes;
    }

    private static List<Class> findClasses(File dir, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();

        if (! dir.exists()) return classes;

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));

            }
        }

        return classes;
    }
}
