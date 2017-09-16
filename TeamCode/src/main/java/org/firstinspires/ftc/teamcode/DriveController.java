package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This class manages all motor and drive base methods.
 */

public class DriveController {

    public DcMotor motorRightF;
    public DcMotor motorRightB;
    public DcMotor motorLeftF;
    public DcMotor motorLeftB;

    ///// MOTOR ENCODER VALUES /////

    public DriveController(HardwareMap hMap, LinearOpMode linearOpMode) {

        HardwareMap hardwareMap = hMap;

        motorLeftF = hardwareMap.dcMotor.get("FLM");
        motorLeftB = hardwareMap.dcMotor.get("BLM");
        motorRightF = hardwareMap.dcMotor.get("FRM");
        motorRightB = hardwareMap.dcMotor.get("BRM");

        //motorLeftF.setDirection(DcMotor.Direction.REVERSE);
        //motorLeftB.setDirection(DcMotor.Direction.REVERSE);

        motorRightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorRightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void initEncoders() {
        motorRightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorRightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorRightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorRightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorLeftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setPower(double power) {
        motorRightF.setPower(power);
        motorRightB.setPower(power);
        motorLeftF.setPower(power);
        motorLeftB.setPower(power);
    }

    public void setPowerEncoderMotors(double power) {
        motorRightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorLeftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorRightF.setPower(0);
        motorLeftF.setPower(0);
        motorRightB.setPower(power);
        motorLeftB.setPower(power);
    }

    public void setBothPowers(double l, double r) {
        motorRightF.setPower(r);
        motorRightB.setPower(r);
        motorLeftF.setPower(l);
        motorLeftB.setPower(l);
    }


    /*public void moveForwardWorking(double cm, double lpower, double rpower, boolean coast, Telemetry telemetry, LinearOpMode linearOpMode) throws InterruptedException{

        initEncoders();

        double ticks = cm / cmPerTick;

        motorRightB.setTargetPosition((int)ticks);
        motorLeftB.setTargetPosition((int)ticks);

        // Turn On RUN_TO_POSITION
        motorLeftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorLeftB.setPower(lpower);
        motorLeftF.setPower(0);
        motorRightB.setPower(rpower);
        motorRightF.setPower(0);

        while((linearOpMode == null || linearOpMode.opModeIsActive()) && Math.abs(motorLeftB.getCurrentPosition()) < ticks) { //(changed 11/3)
            // WAIT
            telemetry.addData("Right ", motorRightB.getCurrentPosition());
            telemetry.addData("Left ", motorLeftB.getCurrentPosition());
            telemetry.addData("cm", cm);
            telemetry.addData("Target Ticks: ", ticks);
            telemetry.update();


            Thread.sleep(10);
        }

        if (coast) {
            motorLeftF.setPower(0);
            motorRightF.setPower(0);
            Thread.sleep(1000);
        }
        setPower(0);

        motorRightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }*/

}
