package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.JewelManipulator;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents.MR_RangeSensor;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents.REV_ColorSensor;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents.REV_RangeSensor;

/**
 * Created by adityamavalankar on 3/20/18.
 */

public class HardwareConfig extends LinearOpMode {

    public REV_ColorSensor jewelColor;
    public REV_RangeSensor jewelRange;
    public MR_RangeSensor rangeSensor;
    public JewelManipulator jewelManipulator;


    public HardwareConfig hardwareConfig;

    public HardwareConfig(){
        this.hardwareConfig = hardwareConfig;
    }

    public void doHardwareConfig(){
        rangeSensor = hardwareMap.get(MR_RangeSensor.class, "frontRange");


    }
    public void runOpMode() {}
}
