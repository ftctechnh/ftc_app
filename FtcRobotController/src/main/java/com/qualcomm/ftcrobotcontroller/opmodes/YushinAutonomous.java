package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;

/* Created by yushin on 11/21/15.
*/
public class YushinAutonomous extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private GyroSensor sensorGyro;
    int heading = 0;
    final int GyroTolerance = 4;
    private DcMotor leftMotor2;
    private DcMotor rightMotor2;

    // DcMotorController motorController1;
    DcMotorController motorController0;

    // heading is never negative, but it can flip from 1 to 359, so do modulus 180
// If this function returns negative, it means turns right to correct; positive means turn left
    private int SubtractFromCurrHeading(int x) {
        int result = 0;
        int ch = sensorGyro.getHeading();
        int diff = Math.abs(ch - x);
        if (diff >= 180) { // more than 180deg apart, so flip
            result = 360 - diff;
            if (x < 180) {
                result = -result;
            }
        } else {
            result = ch - x;
        }
        return result;
    }

    //HiTechnicMotorController hMotorController1;
    HiTechnicMotorController hMotorController0;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        //rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor2 = hardwareMap.dcMotor.get("leftMotor2");
        //rightMotor2 = hardwareMap.dcMotor.get("rightMotor2");
        sensorGyro = hardwareMap.gyroSensor.get("gyro_sensor");

        //motorController1 = rightMotor.getController();
        motorController0 = leftMotor.getController();
        //hMotorController1 = new HiTechnicMotorController(motorController1);
        hMotorController0 = new HiTechnicMotorController(motorController0);
        //hMotorController1.setDebugLog(true);
        hMotorController0.setDebugLog(true);
        hMotorController0.resetMotor2Encoder();
        //hMotorController1.resetMotor2Encoder();
        //hMotorController0.resetMotor1Encoder();
        //hMotorController1.resetMotor1Encoder();

        // calibrate the gyro.
        sensorGyro.calibrate();

        // make sure the gyro is calibrated.
        while (sensorGyro.isCalibrating()) {
            Thread.sleep(50);
        }
        // for (int i = 0; i <= 60; i++) {
        while (hMotorController0.state != 5) {
            //hMotorController1.process(); // get through the initialization states
            hMotorController0.process();
            telemetry.addData("state", String.format("%d", hMotorController0.state));
        }
        telemetry.addData("state", String.format("waiting %d", hMotorController0.state));

        // wait for the start button to be pressed.
        waitForStart();


        while (opModeIsActive()) {

            sensorGyro.resetZAxisIntegrator();

            // drive straight by gyro
            while (hMotorController0.getMotor2Encoder() < 10800) {
                Thread.sleep(100);
                //hMotorController1.process();
                hMotorController0.process();
                telemetry.addData("count0", hMotorController0.getMotor2Encoder());
                //telemetry.addData("count1", hMotorController1.getMotor2Encoder());
                telemetry.addData("power0", hMotorController0.getMotor2Power());
                //telemetry.addData("power1", hMotorController1.getMotor2Power());
                telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(0)));
                telemetry.addData("state", String.format("%d", hMotorController0.state));
                if (Math.abs(SubtractFromCurrHeading(0)) < GyroTolerance) {
                    // on course, go straight
                    //hMotorController1.setMotor1Power(0.40);
                    //hMotorController1.setMotor2Power(0.40);
                    hMotorController0.setMotor1Power(0.40);
                    hMotorController0.setMotor2Power(0.40);
                    //telemetry.addData("count","1");
                    //telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(TurnDegrees)));
                } else { // not on course, correct
                    if (SubtractFromCurrHeading(0) > 0) {
                        // correct left
                        //hMotorController1.setMotor1Power(0.40);
                        //hMotorController1.setMotor2Power(0.40);
                        hMotorController0.setMotor1Power(0.20);
                        hMotorController0.setMotor2Power(0.20);

                    } else {
                        // correct right
                        //hMotorController1.setMotor1Power(0.20);
                        //hMotorController1.setMotor2Power(0.20);
                        hMotorController0.setMotor1Power(0.40);
                        hMotorController0.setMotor2Power(0.40);

                    }
                }
            }
// if (telemetry.addData("Red ", colorSensor.red()) < ){
// hMotorController0.setMotor1Power(0.10);
// hMotorController0.setMotor2Power(0.10);
// hMotorController1.setMotor1Power(0.10);
// hMotorController1.setMotor2Power(0.10);
// }

        }
    }
}