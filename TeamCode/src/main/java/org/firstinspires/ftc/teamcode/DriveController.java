package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
 * This class manages all motor and drive base methods.
 * FYI, the grey comments are just things to notice, but the green ones are top priority
 */

public class DriveController {
    // Declare OpMode members. AKA, variables and shit.
    public DcMotor driveLeftFront;
    public DcMotor driveLeftBack;
    public DcMotor driveRightFront;
    public DcMotor driveRightBack;

    /**
     * idk wtf this is supposed to signify, but is probably some unfinished thing
    ///// MOTOR ENCODER VALUES /////
    */
    public DriveController(HardwareMap hMap, LinearOpMode linearOpMode) {

        HardwareMap hardwareMap = hMap;

        //initialize hardware shit. N.B. Names MUST correspond to those in the phones configuration.
        driveLeftFront  = hardwareMap.get(DcMotor.class, "drive_left_front");
        driveLeftBack = hardwareMap.get(DcMotor.class, "drive_left_back");
        driveRightFront = hardwareMap.get(DcMotor.class, "drive_right_front");
        driveRightBack = hardwareMap.get(DcMotor.class, "drive_right_back");

        /**
         //reverse the motors that need to be reversed
         driveLeftFront.setDirection(DcMotor.Direction.???);
         driveLeftBack.setDirection(DcMotor.Direction.???);
         driveRightFront.setDirection(DcMotor.Direction.???);
         driveRightBack.setDirection(DcMotor.Direction.???);
         */

        /**
         * this block needs to be confirmed
        motorRightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorRightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        */

    }
            //Fuck, everything past here needs to be checked
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

    public void turnGyro(double degres, Telemetry telemetry, int speed/* speed is between 1 and 5 inclusive*/ ){
        telemetry.addData("Error", "Code not writen yet: DriveController.turnGyro");
        
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
