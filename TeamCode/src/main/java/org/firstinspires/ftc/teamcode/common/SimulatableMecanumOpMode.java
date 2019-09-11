package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.mecanum.MecanumHardware;

public abstract class SimulatableMecanumOpMode extends OpMode {
    public MecanumHardware getRobot() {
        return new MecanumHardware(this);
    }
}
