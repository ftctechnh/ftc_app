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
    void register(String name, Class opMode);
    void register(String name, OpMode opMode);
    }
