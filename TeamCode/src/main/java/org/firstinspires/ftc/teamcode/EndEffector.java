package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

class EndEffector {
    DcMotor pivot;
    Telemetry telemetry;
    EndEffector(HardwareMap hardwareMap, Telemetry telemetry)
    {
        pivot = hardwareMap.dcMotor.get("pivot");
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void raise()
    {
        pivot.setTargetPosition(200); //ticks
    }

    void lower()
    {
        pivot.setTargetPosition(0);
    }
    
    double getRadius()
    {
        return 36;
    }
}
