package io.github.thunderbots.robotcontroller.fileloader;

import java.lang.annotation.Annotation;

import io.github.thunderbots.lightning.annotation.OpMode;

public class AnnotationReader {

    public static boolean isActive(Class<? extends com.qualcomm.robotcore.eventloop.opmode.OpMode> c) {
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof OpMode) {
                return ((OpMode) annotation).active();
            }
        }
        return false;
    }

}
