package org.swerverobotics.library.interfaces;

import java.lang.annotation.*;

/**
 * Provides an easy and non-centralized way of determining the OpMode list
 * shown on an FTC Driver Station.  Put an {@linkplain TeleOp} annotation on
 * your teleop OpModes that you want to show up in the driver station display.
 *
 * If you want to temporarily disable an opmode from showing up, then set then also add
 * a {@linkplain Disabled} annotation to it.
 *
 * @see Autonomous
 * @see Disabled
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeleOp
    {
    /**
     * The name to be used on the driver station display. If empty, the name of
     * the OpMode class will be used.
     */
    String name() default "";

    /**
     * Optionally indicate the name of a Autonomous class with whom
     * this TeleOp class is to be paired.
     */
    String pairWithAuto() default "";
    }
