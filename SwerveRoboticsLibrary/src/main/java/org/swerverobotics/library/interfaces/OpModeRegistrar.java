package org.swerverobotics.library.interfaces;

import java.lang.annotation.*;

/**
 * Provides an easy and non-centralized way of contributing to the OpMode list
 * shown on an FTC Driver Station. While {@link Autonomous} and {@link TeleOp} annotations
 * can be placed on your *own* classes to register them, to register classes found
 * in libraries *other* than your own it is best to use a mechanism that does not require
 * that you modify source code in that other library. OpModeRegistar provides such a
 * mechanism.
 * <p>Place an OpModeRegistrar annotation on a static method in your code, and that
 * method will be automatically called at the right time to register classes. Inside
 * the method, call opModeManager.register() methods, just as you would had you been
 * registering methods by putting your code inside the FtcOpModeRegister.register()
 * method as the robot controller runtime suggests.</p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpModeRegistrar
    {
    }
