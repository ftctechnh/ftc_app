package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class SampleOpMode extends LinearOpMode {
    RRVHardwarePushbot robot = new RRVHardwarePushbot();   // Use a Pushbot's hardware
   // private Gyroscope imu;
   // private DcMotor motorTest;
   // private DigitalChannel digitalTouch;
   // private DistanceSensor sensorColorRange;
   // private Servo servoTest;



    @Override
    public void runOpMode() {
        //imu = hardwareMap.get(Gyroscope.class, "imu");
       // motorTest = hardwareMap.get(DcMotor.class, "motorTest");
       // digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
       // sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
       // servoTest = hardwareMap.get(Servo.class, "servoTest");

        telemetry.addData("Status", "Intialized");
        telemetry.update();
        //Wait for the game to start (driver presses PLAY)
        waitForStart();

        //run until the end of the match (driver presses STOP)
        while (opModeIsActive()){
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
