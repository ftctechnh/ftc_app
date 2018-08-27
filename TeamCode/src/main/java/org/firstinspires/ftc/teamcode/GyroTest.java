package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

public class GyroTest extends LinearOpMode {
    IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;

    ElapsedTime timer = new ElapsedTime();

    public void runOpMode(){
        boolean lastResetState = false;
        boolean curResetState  = false;

        modernRoboticsI2cGyro = hardwareMap.get(ModernRoboticsI2cGyro.class,    "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;

        telemetry.log().add("Gyro Calibrating. Do Not Move!");
        modernRoboticsI2cGyro.calibrate();

        timer.reset();
        while (!isStopRequested() && modernRoboticsI2cGyro.isCalibrating()) {
            telemetry.addData("calibrating", "%s", Math.round(timer.seconds()) % 2 == 0 ? "|.." : "..|");
            telemetry.update();
            sleep(50);
        }

        telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
        telemetry.clear(); telemetry.update();

    waitForStart();

        telemetry.log().clear();
        telemetry.log().add("Press A & B to reset heading");

    while (opModeIsActive()){

        int rawX = modernRoboticsI2cGyro.rawX();
        int rawY = modernRoboticsI2cGyro.rawY();
        int rawZ = modernRoboticsI2cGyro.rawZ();

        telemetry.log().add("X = " + rawX);
        telemetry.log().add("Y = " + rawY);
        telemetry.log().add("Z = " + rawZ);
        sleep(3000);
        telemetry.clear();
        telemetry.update();

        if ((gamepad1.a) && (gamepad1.b)){
            telemetry.clear();
            sleep(5000);
        }



    }
    }
}
