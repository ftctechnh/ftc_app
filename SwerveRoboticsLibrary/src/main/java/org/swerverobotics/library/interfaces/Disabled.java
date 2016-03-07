package org.swerverobotics.library.interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a way to temporarily disable an OpMode annotated with
 * {@link Autonomous} or {@link TeleOp} from showing up
 * on the driver station OpMode list.
 *
 * @see Autonomous
 * @see TeleOp
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Disabled
    {
    }
