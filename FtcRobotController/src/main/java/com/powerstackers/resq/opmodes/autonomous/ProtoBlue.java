package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.RobotAuto;
import com.powerstackers.resq.common.enums.PublicEnums.MotorSetting;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Derek on 1/20/2016.
 */
public class ProtoBlue extends LinearOpMode {

    RobotAuto robot;
//    MotorSetting settingBrushMotor = MotorSetting.STOP;

    @Override
    public void runOpMode() throws InterruptedException {

//        robot.initializeRobot();

        waitForStart();

        if (opModeIsActive()==true) {
            robot.setBrush(MotorSetting.FORWARD);
            robot.algorithm.goTicks(robot.algorithm.inchesToTicks(68), 0.4);
            robot.setBrush(MotorSetting.STOP);
        }

        stop();


        /*
         * Motors
         */
//        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        motorFRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        motorFLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//
//        motorFRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motorFLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

    }
}
