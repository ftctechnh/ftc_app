package io.github.thunderbots.robotcontroller.fileloader;

import java.lang.annotation.Annotation;

import io.github.thunderbots.lightning.annotation.Active;
import io.github.thunderbots.lightning.annotation.OpMode;

public class AnnotationReader {

    public static boolean isActive(Class<? extends com.qualcomm.robotcore.eventloop.opmode.OpMode> c) {
        return c.isAnnotationPresent(Active.class);
    }

    public static String getOpModeName(Class<? extends com.qualcomm.robotcore.eventloop.opmode.OpMode> c) {
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof OpMode) {
                String name = ((OpMode) annotation).name();
                if (name != null && !name.equals("")) {
                    return name;
                }
            }
        }
        return c.getSimpleName();
    }

}
