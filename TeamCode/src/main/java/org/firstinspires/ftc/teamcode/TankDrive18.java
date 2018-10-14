package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gyroscope;

@TeleOp(name = "TankDrive18", group = "Tank")
public class TankDrive18 extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor motorTest;
    private DigitalChannel digitalTouch;

    @Override
    public void runOpMode()  {

        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Wait for the game to start ( driver presses play)
        waitForStart();

        //run until the end of the match (driver presses stop)
        while (opModeIsActive()){
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
