package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="OmniDriveJack")

public class OmniDrive extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FR = null;
    private DcMotor FL = null;
    private DcMotor BR = null;
    private DcMotor BL = null;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FR  = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        BR  = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FR.setDirection(DcMotor.Direction.REVERSE);
        FL.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.FORWARD);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;

        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double drive_y = gamepad1.left_stick_y;
        double drive_x = -gamepad1.left_stick_x;
        telemetry.addData("drive_y", drive_y);
        telemetry.addData("drive_x", drive_x);
        double turn  = -gamepad1.right_stick_x;

        if(Math.abs(drive_y) > .3) {
            telemetry.addData("Status", "drive");
            FR.setDirection(DcMotor.Direction.REVERSE);
            FL.setDirection(DcMotor.Direction.FORWARD);
            BR.setDirection(DcMotor.Direction.REVERSE);
            BL.setDirection(DcMotor.Direction.FORWARD);

            leftPower    = Range.clip(drive_y + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive_y - turn, -1.0, 1.0) ;

            FL.setPower(leftPower);
            BL.setPower(leftPower);
            FR.setPower(rightPower);
            BR.setPower(rightPower);
        } else if (Math.abs(drive_x) > .3) {
            telemetry.addData("Status", "strafe");
            FR.setDirection(DcMotor.Direction.FORWARD);
            FL.setDirection(DcMotor.Direction.FORWARD);
            BR.setDirection(DcMotor.Direction.REVERSE);
            BL.setDirection(DcMotor.Direction.REVERSE);

            leftPower    = Range.clip(drive_x + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive_x - turn, -1.0, 1.0) ;

            FL.setPower(leftPower);
            BL.setPower(rightPower);
            FR.setPower(leftPower);
            BR.setPower(rightPower);
        } else {
            telemetry.addData("Status", "else");
            FR.setDirection(DcMotor.Direction.REVERSE);
            FL.setDirection(DcMotor.Direction.FORWARD);
            BR.setDirection(DcMotor.Direction.REVERSE);
            BL.setDirection(DcMotor.Direction.FORWARD);

            leftPower = Range.clip(turn, -1.0, 1.0) ;
            rightPower = Range.clip(-turn, -1.0, 1.0) ;

            FL.setPower(leftPower);
            BL.setPower(leftPower);
            FR.setPower(rightPower);
            BR.setPower(rightPower);
        }

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
        // leftPower  = -gamepad1.left_stick_y ;
        // rightPower = -gamepad1.right_stick_y ;

        // Send calculated power to wheels

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}