package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * IOpModeManager instances are used as part of a decentralized OpMode registration
 * mechanism.
 *
 * @see OpModeRegistrar
 */
public interface IOpModeManager
    {
    /**
     * Register a class for display on the driver station and availability for game play.
     * New instances of this class will be created as needed. The name used on the driver
     * station menu is taken from a {@link TeleOp} or {@link Autonomous} annotation on the
     * class itself (any {@link Disabled} attribute which may also be present is ignored).
     *
     * @param opModeClass   the class of the OpMode to create
     * @see TeleOp
     * @see Autonomous
     * @see #register(String, Class)
     */
    void register(Class opModeClass);

    /**
     * Register a class for display on the driver station and availability for game play.
     * New instances of this class will be created as needed.
     *
     * @param name          the name to show on the driver station menu
     * @param opModeClass   the class of the OpMode to create
     * @see #register(Class)
     */
    void register(String name, Class opModeClass);

    /**
     * Register an *instance* of a class for display on the driver station and availability
     * for game play. You won't likely use this method very often.
     *
     * @param name              the name to show on the driver station menu
     * @param opModeInstance    the object instance to use for that menu item
     */
    void register(String name, OpMode opModeInstance);
    }
