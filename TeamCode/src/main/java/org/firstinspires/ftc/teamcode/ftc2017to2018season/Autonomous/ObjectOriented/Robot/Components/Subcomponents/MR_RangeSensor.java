package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class MR_RangeSensor extends LinearOpMode {

    public ModernRoboticsI2cRangeSensor rangeSensor;


    public MR_RangeSensor(String hardwareConfig){
        this.rangeSensor = rangeSensor;
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, hardwareConfig);
    }


    public double getCM() {

        double dist = rangeSensor.getDistance(DistanceUnit.CM);

        return dist;
    }

    @Override
    public void runOpMode(){}
}
