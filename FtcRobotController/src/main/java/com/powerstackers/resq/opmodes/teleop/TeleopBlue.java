package com.powerstackers.resq.opmodes.teleop;

import com.powerstackers.resq.common.enums.PublicEnums.AllianceColor;

import org.swerverobotics.library.interfaces.TeleOp;

/**
 * @author Jonathan
 */
@TeleOp(name = "Teleop Blue", group = "Powerstackers")
public class TeleopBlue extends ResqTeleop {
    public TeleopBlue() {
        super(AllianceColor.BLUE);
    }
}
