package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

/**
 * Created by inspirationteam on 11/20/17.
 */

public class testAutoMethods extends Autonomous_General {

    public void runOpMode(){
        initiate();
        telemetry.addData("--->", "Gyro Calibrating");
        telemetry.update();
        gyro.calibrate();


        while(gyro.isCalibrating()){
            sleep(50);
            idle();

        }

        telemetry.addData("---->","Gyro Calibrated. Good to go...");
        telemetry.update();

        waitForStart();

        gyro.resetZAxisIntegrator();

        //test Straight Drive
        telemetry.addData("Testing Straight Drive","");
        telemetry.update();
        straightDrive(0.5);
        sleep(2000);
        stopMotors();
        sleep(3000);


        //test turn left and right
        telemetry.addData("Testing Turn Left", "");
        telemetry.update();
        turnLeft(0.5);
        sleep(2000);
        stopMotors();
        sleep(3000);
        telemetry.addData("Testing Turn Right" , "");
        telemetry.update();
        turnRight(0.5);
        sleep(2000);
        stopMotors();
        sleep(3000);

        //strafe left and right
        telemetry.addData("Testing strafeRight","");
        telemetry.update();
        strafeRight(0.5);
        sleep(2000);
        stopMotors();
        sleep(3000);
        telemetry.addData("Testing strafeLeft" ,"");
        telemetry.update();
        strafeLeft(0.5);
        sleep(2000);
        stopMotors();
        sleep(3000);

        //gyro turn
        telemetry.addData("Testing gyro turn", "90 degrees");
        telemetry.update();
        gyroTurn(0.3,90);
        sleep(1000);
        gyroTurn(0.3,0);
    }

}
