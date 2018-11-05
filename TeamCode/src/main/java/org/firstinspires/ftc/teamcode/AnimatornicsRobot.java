package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AnimatornicsRobot {

    private DcMotor lfMotor;
    private DcMotor lbMotor;
    private DcMotor rfMotor;
    private DcMotor rbMotor;

    private DcMotor collectMotor;
    private DcMotor flipMotor;
    private DcMotor liftMotor;
    private DcMotor collectSlideMotor;

    private Telemetry telemetry;

    public AnimatornicsRobot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.telemetry = telemetry;
        lfMotor = hardwareMap.get(DcMotor.class, "lfmotor");
        lbMotor = hardwareMap.get(DcMotor.class, "lbmotor");
        rfMotor = hardwareMap.get(DcMotor.class, "rfmotor");
        rbMotor = hardwareMap.get(DcMotor.class, "rbmotor");

        collectMotor = hardwareMap.get(DcMotor.class, "collect_motor");
        collectSlideMotor = hardwareMap.get(DcMotor.class, "collect_slide_motor");
        flipMotor = hardwareMap.get(DcMotor.class, "flip_motor");
        liftMotor = hardwareMap.get(DcMotor.class, "lift_motor");

        telemetry.addData("Status", "DC motor variables initialized");
    }

    public void setWheelPower(RoverRuckesTeleOp op) {

        double lfPower = op.gamepad1.left_stick_y - op.gamepad1.left_stick_x;
        double lbPower = op.gamepad1.left_stick_y + op.gamepad1.left_stick_x;
        double rfPower = - (op.gamepad1.right_stick_y - op.gamepad1.right_stick_x);
        double rbPower = - (op.gamepad1.right_stick_y + op.gamepad1.right_stick_x);

        lfPower = Range.clip(lfPower, -1, 1);
        lbPower = Range.clip(lbPower, -1, 1);
        rfPower = Range.clip(rfPower, -1, 1);
        rbPower = Range.clip(rbPower, -1, 1);

        lfMotor.setPower(lfPower);
        lbMotor.setPower(lbPower);
        rfMotor.setPower(rfPower);
        rbMotor.setPower(rbPower);

        telemetry.addData("Status", "Changed Wheels powers");

        double liftPower = op.gamepad1.left_trigger - op.gamepad1.right_trigger;
        liftMotor.setPower(liftPower);
        telemetry.addData("Status", "liftPower: " + liftPower);

        double collectPower = op.gamepad2.left_trigger - op.gamepad2.right_trigger;
        collectMotor.setPower(collectPower);
        telemetry.addData("Status", "collectPower: " + collectPower);

        double collectSlidePower = -op.gamepad2.left_stick_y;
        collectSlideMotor.setPower(collectSlidePower);
        telemetry.addData("Status", "collectSlidePower: " + collectPower);

        double flipPower = -op.gamepad2.right_stick_y;
        flipMotor.setPower(flipPower);
        telemetry.addData("Status", "flipPower: " + flipPower);
    }
}
