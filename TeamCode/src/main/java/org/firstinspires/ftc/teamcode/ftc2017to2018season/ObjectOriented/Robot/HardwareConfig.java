package org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.JewelManipulator;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.Subcomponents.MR_RangeSensor;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.Subcomponents.REV_ColorSensor;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.Subcomponents.REV_RangeSensor;

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
