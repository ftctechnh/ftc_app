package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AnimatornicsRobot {

    private DcMotor lfMotor;
    private DcMotor lbMotor;
    private DcMotor rfMotor;
    private DcMotor rbMotor;

    private Telemetry telemetry;

    public AnimatornicsRobot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.telemetry = telemetry;
        lfMotor = hardwareMap.get(DcMotor.class, "lfmotor");
        lbMotor = hardwareMap.get(DcMotor.class, "lbmotor");
        rfMotor = hardwareMap.get(DcMotor.class, "rfmotor");
        rbMotor = hardwareMap.get(DcMotor.class, "rbmotor");

        telemetry.addData("Status", "DC motor variables initialized");
        telemetry.update();
    }

    public void setWheelPower(double lfPower, double lbPower, double rfPower, double rbPower) {
        lfMotor.setPower(lfPower);
        lbMotor.setPower(lbPower);
        rfMotor.setPower(rfPower);
        rbMotor.setPower(rbPower);

        telemetry.addData("Status", "Changed DC motors power");
        telemetry.update();
    }
}
