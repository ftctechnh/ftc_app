package org.firstinspires.ftc.teamcode.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class Flipinator9000 {
    public static double FLIP_kV = 0;
    public static double FLIP_kStatic = 0;

    public static double MAX_ACCEL = 20;
    public static double MAX_VELO = 200;

    public static int DOWN_POSITION = 0;
    public static int UP_POSITION = 500;
    PIDFController ROTARY_CONTROLLER;
    public static PIDCoefficients ROTARY_COEFFS = new PIDCoefficients(-0.01, 0, 0);
    MotionProfile PROFILE;

    private DcMotorEx leftFlipper, rightFlipper;

    public Flipinator9000 (DcMotorEx leftFlipper, DcMotorEx rightFlipper) {
        this.leftFlipper = leftFlipper;
        this.rightFlipper = rightFlipper;
        leftFlipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFlipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFlipper.setPower(0);
        rightFlipper.setPower(0);

        ROTARY_CONTROLLER = new PIDFController(ROTARY_COEFFS, FLIP_kV, FLIP_kStatic);
        ROTARY_CONTROLLER.setOutputBounds(-1, 1);
        /*PIDF
        PROFILE = MotionProfileGenerator.generateSimpleMotionProfile(
                new MotionState(0), // start state
                new MotionState(10, 0, 0), // goal state
                //*/
    }

    public void update(int encoderPosition) {
        double power = ROTARY_CONTROLLER.update(encoderPosition);
        //ROTARY_Con
        leftFlipper.setPower(power);
        rightFlipper.setPower(power); // One is reversed in initialization
    }

    public void flipUp() {updateFlipTarget(UP_POSITION);}
    public void flipDown() {updateFlipTarget(DOWN_POSITION);}

    private void updateFlipTarget(int target) {
        ROTARY_CONTROLLER.setTargetPosition(target);
        leftFlipper.setTargetPosition(target);
        rightFlipper.setTargetPosition(target);
    }
}
