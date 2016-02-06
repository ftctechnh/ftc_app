package com.powerstackers.resq.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

/**
 * Created by Derek on 12/23/2015.
 */
public class TedbotAuto extends LinearOpMode {


    double enRightPosition = 0.0;
    double enLeftPosition = 0.0;

//    double EnRightpower = 1;
//    double EnLeftpower = 1;

    // Robot Movements in steps
    double EnRightS1 = -1000;
//    double EnLeftS1 = 1000;
    double EnRightS2 = -500;
//    double EnLeftS2 = 500;

    //Color Values
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

    DeviceInterfaceModule cdim;
//    ColorSensor colorSensor;
//    ColorSensor colorFSensor;
//    TouchSensor touchSensor;

    DcMotor motorBRight;
    DcMotor motorBLeft;

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        /*Motors
         *
         */
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);

        /*
         * Sensors
         */
//        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
//        colorSensor.enableLed(true);
//        colorFSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorFSensor"));
//        colorFSensor.enableLed(true);
//        touchSensor = hardwareMap.touchSensor.get("touchSensor");

        /*
         * Motors
         */
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        // wait for the start button to be pressed
        waitForStart();

        while (enRightPosition > EnRightS1) {
//            enLeftPosition = motorBLeft.getCurrentPosition();
           enRightPosition = motorBRight.getCurrentPosition();
            motorBRight.setPower(1);
            motorBLeft.setPower(1);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR","Value: "+String.valueOf(motorBRight.getCurrentPosition()));


//            if (enLeftPosition > EnLeftS1 && enRightPosition > EnRightS1) {
//
//                motorBLeft.setPower(0);
//                motorBRight.setPower(0);
//
//            }
        }

        motorBLeft.setPower(0);
        motorBRight.setPower(0);
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (enRightPosition < EnRightS2) {
//            enLeftPosition = motorBLeft.getCurrentPosition();
            enRightPosition = motorBRight.getCurrentPosition();
            motorBRight.setPower(1);
            motorBLeft.setPower(-1);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR", "Value: " + String.valueOf(motorBRight.getCurrentPosition()));
        }

        motorBLeft.setPower(0);
        motorBRight.setPower(0);
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (enRightPosition > EnRightS1) {
//            enLeftPosition = motorBLeft.getCurrentPosition();
            enRightPosition = motorBRight.getCurrentPosition();
            motorBRight.setPower(1);
            motorBLeft.setPower(1);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR", "Value: " + String.valueOf(motorBRight.getCurrentPosition()));

        }

        motorBLeft.setPower(0);
        motorBRight.setPower(0);
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        // Motor controls
//        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS); //TODO set to button later
//        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motorBLeft.setTargetPosition(1000);
//        motorBRight.setTargetPosition(1000);
//       while (true) {


//       }



        /* ColorSensor Controls
         *
         */
//        if (colorSensor.blue() > colorSensor.red()) {
//            RobotAuto.servoBeaconPosition = 0.20;
//
//        } else if (colorSensor.red() > colorSensor.blue()) {
//            RobotAuto.servoBeaconPosition = 0.80;
//        } else {
//            RobotAuto.servoBeaconPosition = 0.50;
//        }



//    stop();
    }
}
