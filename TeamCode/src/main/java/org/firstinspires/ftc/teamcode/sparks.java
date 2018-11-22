package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class sparks extends OpMode
{
    //Creates hardware map from the hardwareMap class
    hardwareMap robot = new hardwareMap(DcMotor.RunMode.RUN_USING_ENCODER);

    //Initializes Time for Robot
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        //Tells driver that hardware mapping is initializing
        telemetry.addData("Status ", "Initializing");

        //Initializes hardware mapping from hardwareMap class
        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status ", "Initialized");
    }

    @Override
    public void start()
    {
        //Sets runtime back to zero as soon as loop starts
        runtime.reset();
    }

    @Override
    public void loop()
    {
        //Initializes variables that hold value of motor powers
        double leftPower;
        double rightPower;

        //Initializes variables which holds input from controller
        double drive = gamepad1.left_stick_y;
        double turn  = gamepad1.right_stick_x;

        //Modifies input of controller variables and feeds them into power variables
        leftPower   = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower  = Range.clip(drive - turn, -1.0, 1.0) ;

        //Sets DC motor powers to the power variables
        robot.leftFront.setPower(leftPower);
        robot.rightFront.setPower(rightPower);
        robot.leftBack.setPower(leftPower);
        robot.rightBack.setPower(rightPower);

        //Gives information on power and time for troubleshooting
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }
}
