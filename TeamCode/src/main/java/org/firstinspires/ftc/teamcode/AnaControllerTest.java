package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="AnaOnBot ControllerTest", group="Pushbot")

public class AnaControllerTest extends LinearOpMode {

    // Set all status flags to false
    public boolean dpadup = false;
    public boolean dpaddown = false;
    public boolean dpadleft = false;
    public boolean dpadright = false;
    public boolean button_a = false;
    public boolean button_b = false;
    public boolean button_x = false;
    public boolean button_y = false;
    public boolean start = false;
    public boolean back = false;
    public boolean righttrigger = false;
    public boolean lefttrigger = false;
    public boolean rightbumper = false;
    public boolean leftbumper = false;
    public boolean rightstickx = false;
    public boolean rightsticky = false;
    public boolean leftstickx = false;
    public boolean leftsticky = false;



    @Override
    public void runOpMode() {


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("To Start Test", "Press Play");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addLine("*** Controller Test ****");

            // show status flag and current value
            telemetry.addLine().addData("", dpadup).addData("Dpad Up    ", gamepad1.dpad_up);
            telemetry.addLine().addData("", dpaddown).addData("Dpad Down  ", gamepad1.dpad_down);
            telemetry.addLine().addData("", dpadleft).addData("Dpad Left  ", gamepad1.dpad_left);
            telemetry.addLine().addData("", dpadright).addData("Dpad Right  ", gamepad1.dpad_right);
            telemetry.addLine().addData("", button_a).addData("Button A  ", gamepad1.a);
            telemetry.addLine().addData("", button_b).addData("Button B  ", gamepad1.b);
            telemetry.addLine().addData("", button_x).addData("Button X  ", gamepad1.x);
            telemetry.addLine().addData("", button_y).addData("Button Y  ", gamepad1.y);
            telemetry.addLine().addData("", start).addData("Start  ", gamepad1.start);
            telemetry.addLine().addData("", back ).addData("Back  ", gamepad1.back);
            telemetry.addLine().addData("", righttrigger ).addData(" Right Trigger  ", gamepad1.right_trigger);
            telemetry.addLine().addData("", lefttrigger ).addData(" Left Trigger  ", gamepad1.left_trigger);
            telemetry.addLine().addData("", rightbumper ).addData(" Right Bumper  ", gamepad1.right_bumper);
            telemetry.addLine().addData("", leftbumper ).addData(" Left Bumper  ", gamepad1.left_bumper);
            telemetry.addLine().addData("", rightstickx ).addData(" Right Stick X  ", gamepad1.right_stick_x);
            telemetry.addLine().addData("", rightsticky ).addData(" Right Stick Y  ", gamepad1.right_stick_y);
            telemetry.addLine().addData("", leftstickx ).addData(" Left Stick X  ", gamepad1.left_stick_x);
            telemetry.addLine().addData("", leftsticky ).addData(" Left Stick Y  ", gamepad1.left_stick_y);




            // set status flag true if pressed
            if (gamepad1.dpad_up) dpadup = true;
            if (gamepad1.dpad_down) dpaddown = true;
            if (gamepad1.dpad_left) dpadleft = true;
            if (gamepad1.dpad_right) dpadright = true;
            if (gamepad1.a) button_a = true;
            if (gamepad1.b) button_b = true;
            if (gamepad1.x) button_x = true;
            if (gamepad1.y) button_y = true;
            if (gamepad1.start) start = true;
            if (gamepad1.back) back = true;
            if (gamepad1.right_trigger > 0.5 ) righttrigger = true;
            if (gamepad1.left_trigger > 0.5) lefttrigger = true;
            if (gamepad1.right_bumper) rightbumper = true;
            if (gamepad1.left_bumper) leftbumper = true;
            if (gamepad1.right_stick_x > 0.5) rightstickx = true;
            if (gamepad1.right_stick_y > 0.5) rightsticky = true;
            if (gamepad1.left_stick_x > 0.5) leftstickx = true;
            if (gamepad1.left_stick_y > 0.5) leftsticky = true;



/**    controller buttons I can think of
 *      gamepad1.dpad_left
 gamepad1.dpad_right
 gamepad1.a
 gamepad1.b
 gamepad1.x
 gamepad1.y
 gamepad1.back
 gamepad1.start
 gamepad1.right_stick_button
 gamepad1.left_stick_button
 gamepad1.right_bumper
 gamepad1.left_bumper
 gamepad1.right_trigger
 gamepad1.left_trigger
 gamepad1.right_stick_x
 gamepad1.right_stick_y
 gamepad1.left_stick_x
 gamepad1.left_stick_y   */


            // test to see if all have been set
            if (    (dpadup) &&
                    (dpaddown) &&
                    (button_a) &&
                    (button_b) &&
                    (button_x) &&
                    (button_y) &&
                    (start) &&
                    (back) &&
                    (righttrigger) &&
                    (lefttrigger) &&
                    (rightbumper) &&
                    (leftbumper) &&
                    (rightstickx) &&
                    (rightsticky) &&
                    (leftstickx) &&
                    (leftsticky)
                    )
            {

                telemetry.addLine("****** All TESTS COMPLETE*******");
            }
            telemetry.update();



        }
    }
}