package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ResQAuto extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    static double BLACKVALUE = 0;
    static double WHITEVALUE = 0.4;
    static double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);


    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
       // leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("/FIRST/calibration.txt"));
            String sCurrentLine;


            while ((sCurrentLine = br.readLine()) != null) {
                WHITEVALUE = Double.valueOf(sCurrentLine).doubleValue();
                System.out.println("Calibrated Value: " + WHITEVALUE);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        waitForStart();


            leftMotor.setPower(1.0);
            rightMotor.setPower(1.0);

            sleep(1000);

        leftMotor.setPowerFloat();
        rightMotor.setPowerFloat();

    }
}
