package org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes;

        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
- Name: Holonomic Field-Centric Tele-Op
- Creator[s]: Talon
- Date Created: 6/16/17
- Objective: To drive our holonomic robot around in a field centric manner, which is easier for drivers
           since they don't have to keep track of the front of the robot.
- Controls: The right joystick controls translational movement, while the triggers control rotation.
          Additionally, the A button makes the robot rotate to face the current direction of travel,
          the start button sets the forward direction of the robot to its current heading,
          and holding both bumpers sends the robot into ultra-turbo mode, which enhances its speed.
- Sensor Usage: Our Adafruit IMU gyro sensor is used in this program for detecting the robot's heading,
                which is used to determine what direction the robot needs to go with respect to its front.
- Key Algorithms: Lines (x-y) contains the algorithm that converts joystick input into actual motor
                  values. First the rectangular vector obtained from the joystick values is converted
                  into a polar vector. Then the robot heading is subtracted to find the angle that the
                  robot must travel with respect to its front. This angle is run through some trigonometry
                  to find ratios for the motor values, which are then adjusted to match the desired motor power.
- Uniqueness: Although this is a fairly standard field centric drive, the added customizations to give
              the driver more options(A/start/bumpers) is what makes this program stand out.
- Possible Improvements: The main algorithm is a bit lengthy possibly causing some lag, so if needed
                         the joystick polar vector could be converted back to rectangular and plugged
                         into the motors like in the robot centric program instead of using all of the
                         trigonometry. Also pivoting could be added in if found to be useful, but
                         I have not yet thought of a practical use for it.
 */

@TeleOp(name = "Holonomic Field-Centric Tele-Op Talon", group = "holonomic Talon")
public class Holonomic_FieldCentric_TeleOp extends OpMode
{
    Holonomic_Hardware robot;
    double angleFromDriver = Math.PI/2;
    double jTheta;
    double jp;
    double theta;

    @Override
    public void init ()
    {
        robot = new Holonomic_Hardware(hardwareMap, telemetry, true);

    }

    @Override
    public void loop ()
    {
        if(gamepad1.right_bumper && gamepad1.left_bumper)
            robot.dp = 0.2;
        else
            robot.dp = 1;

        robot.updateGyro();

        jTheta = (double) Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x);

        jp = (double) Math.sqrt(gamepad1.right_stick_x * gamepad1.right_stick_x + gamepad1.right_stick_y * gamepad1.right_stick_y);

        if(jp > 1)
            jp = 1;

        theta = (jTheta + angleFromDriver - robot.heading);

        robot.drive(
                (Math.sin(theta)+Math.cos(theta))*jp/2 + gamepad1.right_trigger - gamepad1.left_trigger,
                (Math.sin(theta)-Math.cos(theta))*jp/2 - gamepad1.right_trigger + gamepad1.left_trigger,
                (Math.sin(theta)-Math.cos(theta))*jp/2 + gamepad1.right_trigger - gamepad1.left_trigger,
                (Math.sin(theta)+Math.cos(theta))*jp/2 - gamepad1.right_trigger + gamepad1.left_trigger
                    );

        telemetry.addData("Ultra Turbo Mode Activated", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData("Left Trigger", gamepad1.left_trigger);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Joystick Direction", Math.toDegrees(jTheta));
        telemetry.addData("Joystick Magnitude", gamepad1.right_stick_y);
        telemetry.addData("Gyro Heading", robot.heading);
    }
}