package org.swerverobotics.library.interfaces;

import java.lang.annotation.*;

/**
 * Provides an easy and non-centralized way of determining the OpMode list
 * shown on an FTC Driver Station.  Put an {@link TeleOp} annotation on
 * your teleop OpModes that you want to show up in the driver station display.
 *
 * If you want to temporarily disable an opmode from showing up, then set then also add
 * a {@link Disabled} annotation to it.
 *
 * @see Autonomous
 * @see Disabled
 * @see org.swerverobotics.library.examples.SynchTeleOp
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeleOp
    {
    /**
     * The name to be used on the driver station display. If empty, the name of
     * the OpMode class will be used.
     * @return the name to use for the OpMode on the driver station
     */
    String name() default "";

    /**
     * Optionally indicates a group of other OpModes with which the annotated
     * OpMode should be sorted on the driver station OpMode list.
     * @return the group into which the annotated OpMode is to be categorized
     */
    String group() default "";
    }
