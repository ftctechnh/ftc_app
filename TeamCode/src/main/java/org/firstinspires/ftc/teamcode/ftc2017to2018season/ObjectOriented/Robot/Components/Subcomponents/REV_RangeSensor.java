package org.firstinspires.ftc.teamcode.ftc2017to2018season.ObjectOriented.Robot.Components.Subcomponents;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class REV_RangeSensor {

    public DistanceSensor rangeSensor;


    public REV_RangeSensor(){
        this.rangeSensor = rangeSensor;
    }


    public double getCM() {

        double dist = rangeSensor.getDistance(DistanceUnit.CM);

        return dist;
    }
}
