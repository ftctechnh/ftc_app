package org.firstinspires.ftc.teamcode.prototype;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;


@TeleOp(name="Drive", group="Robot")
@Disabled
public class DRIVETEST extends LinearOpMode {
    public DcMotor leftMotor   = null;
    public DcMotor  rightMotor  = null;

    HardwarePushbot robot = new HardwarePushbot();              //'Use a ROUS shardware
    //public final static double press_r = .6;
    //public final static double press_l = .6;

    @Override
    public void runOpMode() throws InterruptedException {
        double Left;
        double Right;

        HardwareMap hwMap           =  null;
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        leftMotor   = hwMap.dcMotor.get("left motor");
        rightMotor  = hwMap.dcMotor.get("right motor");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Left = -gamepad1.left_stick_y ;
            Right = -gamepad1.right_stick_y;

            robot.leftMotor.setPower(Left);
            robot.rightMotor.setPower(Right);

        }
    }
}
