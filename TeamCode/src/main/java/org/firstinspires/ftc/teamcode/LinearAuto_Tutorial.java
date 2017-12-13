package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "LinearAuto")
public class LinearAuto_Tutorial extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    // declare variables for our motors
    private DcMotor l_front, l_back, r_front, r_back;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the hardware variables. That string should be the same as the names that you set up on the phone.
        l_front  = hardwareMap.get(DcMotor.class, "l_front");
        l_back  = hardwareMap.get(DcMotor.class, "l_back");
        r_front = hardwareMap.get(DcMotor.class, "r_front");
        r_back = hardwareMap.get(DcMotor.class, "r_back");

        // On our robot, not all the motors are oriented the same way. One side is "backwards".
        // To compensate for this, we need to switch the direction of the motors.
        // set the left motors to forward, because sometimes they forget
        l_front.setDirection(DcMotor.Direction.FORWARD);
        l_back.setDirection(DcMotor.Direction.FORWARD);
        // set the right motors to reverse to make the robot move straight
        r_front.setDirection(DcMotor.Direction.REVERSE);
        r_back.setDirection(DcMotor.Direction.REVERSE);

        // This is telemetry.
        telemetry.addData("Status", "Initialized");
        telemetry.update(); // You'll learn about this very soon.

        // wait for the driver to press the play button
        waitForStart();
        runtime.reset();

        // This is where the main OpMode happens.
        // Here's an example of how to drive the robot
        drive(100, 100, 500); // Drive forward at 100% power for 500ms. TODO change this
        //TODO make the robot go backwards
        //TODO make the robot turn in place
        //TODO make the robot turn, but not in place
        //ADVANCED: TODO make the robot drive and turn at the same time (move forward and turn simultaneously)
    }

    /*
     * Here's a super convenient drive function to make writing this a lot easier.
     * Have a good look in here and understand what's going on before you use it. You'll need the knowledge later.
     */
    private void drive(double leftPower, double rightPower, long time) throws InterruptedException {
        // set left motors
        l_front.setPower(leftPower/100);
        l_back.setPower(leftPower/100);
        // set right motors
        r_front.setPower(rightPower/100);
        r_back.setPower(rightPower/100);
        // wait
        Thread.sleep(time);
        // stop
        l_front.setPower(0);
        l_back.setPower(0);
        r_front.setPower(0);
        l_back.setPower(0);
    }
}
