package org.firstinspires.ftc.teamcode.prototype;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * Created by ROUS on 10/29/2016.
 */
@Autonomous(name="Auto Drive", group="Pushbot")
@Disabled
public class TIMEDRIVETEST extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot         robot   = new HardwarePushbot();   // Use a Pushbot's hardware



    //static final double     FORWARD_SPEED = 0.6;
    //static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        sleep(500);
        robot.leftMotor.setPower(.5);
        robot.rightMotor.setPower(.5);
        sleep(5000);
        stop();

        idle();
    }
}
