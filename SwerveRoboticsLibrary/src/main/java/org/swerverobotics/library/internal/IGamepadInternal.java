package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.swerverobotics.library.interfaces.IGamepad;

/**
 * Internal interface for accessing gamepads
 */
public interface IGamepadInternal extends IGamepad
    {
    boolean updateGamepad(Gamepad newTarget);
    }
