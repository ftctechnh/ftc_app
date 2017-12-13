package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="IterativeDriver")
public class IterativeDriver_Tutorial extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor l_front, l_back, r_front, r_back;

    /*
     * This method is where we set up the robot to run.
     * This code runs ONCE when you click "INIT".
     */
    @Override
    public void init() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
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

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Personally, I haven't seen a use for this one yet. Play around with telemetry in this loop.
     * This code runs repeatedly once you click "INIT" until you click the play button.
     */
    @Override
    public void init_loop() {
        // Telemetry is a tool to get information to the drivers.
        // The most basic use for this is telemetry.addLine().
        // It takes a String, but you can probably feed it other things too.
        telemetry.addLine("Look, I'm on the Driver Station!");
        // Leaving it empty is like pressing enter.
        telemetry.addLine();
        // For something a bit more powerful, try telemetry.addData().
        telemetry.addData("OpMode: ", "robot still hasn't started");
        telemetry.addData("RobotController: ", "i'm bored.");
        // You can feed the second argument of telemetry.addData() anything.
        telemetry.addData("RobotController -> DcMotor l_front: ", l_front.getDirection());
        // let's give that a second to sink in
        try {Thread.sleep(1000);} catch (InterruptedException ignored) {} // this is how to make the robot wait
        // If the screen gets messy, you can always clear it.
        telemetry.clear();
        // wait half a second
        try {Thread.sleep(500);} catch (InterruptedException ignored) {}
        // Play with telemetry for a bit if it's interesting to you.
    }

    /*
     * Currently, we don't have a use for this.
     * This code runs ONCE when the driver clicks the play button.
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * This is our main loop - this is where the code that drives the robot goes.
     * This code runs repeatedly after the driver clicks the play button until they click the stop button.
     */
    @Override
    public void loop() {
        telemetry.clear();
        // variables for the left and right motor powers
        double leftPower = gamepad1.left_stick_y;
        double rightPower = gamepad1.right_stick_y;

        // Remember setPower() from the drive() method in Autonomous?
        // Write code that sets the motor's power to those variables.
        // Set the left motors to leftPower, and the right motors to RightPower.
        //TODO make the robot move based on the joystick values

        // Once you're done with the code, add leftPower and rightPower to telemetry.
        //TODO tell the driver what power their motors are at
    }

    /*
     * this code runs ONCE after the driver presses the stop button.
     */
    @Override
    public void stop() {
        // IMPORTANT: You MUST manually stop the robot at the end of the OpMode.
        // If you don't the motors will keep running forever.
        // It's kinda funny until this issue makes your robot hit Heather. She nearly cut my hand off!
        l_front.setPower(0);
        l_back.setPower(0);
        r_front.setPower(0);
        l_back.setPower(0);
    }

}
