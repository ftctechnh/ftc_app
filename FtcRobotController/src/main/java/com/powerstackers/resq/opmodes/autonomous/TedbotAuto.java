package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.RobotAuto;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

/**
 * Created by Derek on 12/23/2015.
 */
public class TedbotAuto extends LinearOpMode {

    //Color Values
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

    DeviceInterfaceModule cdim;
//    ColorSensor colorSensor;
//    ColorSensor colorFSensor;
//    TouchSensor touchSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        /**
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        /**
         * Sensors
         */
//        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
//        colorSensor.enableLed(true);
//        colorFSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorFSensor"));
//        colorFSensor.enableLed(true);
//        touchSensor = hardwareMap.touchSensor.get("touchSensor");

        /**
         * Motors
         */
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        RobotAuto.motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /**
         * Servos
         */
//        servoBeacon = hardwareMap.servo.get("servoBeacon");

        // wait for the start button to be pressed
        waitForStart();
//                enLeftPosition < EnLeftDelta ||
        while (RobotAuto.enRightPosition > RobotAuto.EnRightDelta) {
            RobotAuto.enLeftPosition = RobotAuto.motorBLeft.getCurrentPosition();
            RobotAuto.enRightPosition = RobotAuto.motorBRight.getCurrentPosition();
            RobotAuto.motorBRight.setPower(1);
            RobotAuto.motorBLeft.setPower(1);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(RobotAuto.motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR", "Value: " + String.valueOf(RobotAuto.motorBRight.getCurrentPosition()));

//            if (enLeftPosition > EnLeftDelta && enRightPosition > EnRightDelta) {
//
//                motorBLeft.setPower(0);
//                motorBRight.setPower(0);
//
//            }
        }

        RobotAuto.motorBLeft.setPower(0);
        RobotAuto.motorBRight.setPower(0);
        RobotAuto.motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        RobotAuto.motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        // Motor controls
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS); //TODO set to button later
//        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motorBLeft.setTargetPosition(1000);
//        motorBRight.setTargetPosition(1000);
//       while (true) {


//       }



        /** ColorSensor Controls
         *
         */
//        if (colorSensor.blue() > colorSensor.red()) {
//            servoBeaconPosition = 0.20;
//
//        } else if (colorSensor.red() > colorSensor.blue()) {
//            servoBeaconPosition = 0.80;
//        } else {
//            servoBeaconPosition = 0.50;
//        }




    }
}
