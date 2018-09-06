
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

 
 @TeleOp(name="Controller Test", group="Pushbot")
 @Disabled
 public class ControllerTest extends OpMode{
 
     /* Declare OpMode members. */
     //HardwareGromit gromit      = new HardwareGromit(); // use the class created to define a Pushbot's hardware
                                                          // could also use HardwarePushbotMatrix class.
    
     /*
      * Code to run ONCE when the driver hits INIT
      */
     @Override
     public void init() {
         /* Initialize the hardware variables.
          * The init() method of the hardware class does all the work here
          */
        // gromit.init(hardwareMap);
 
         // Send telemetry message to signify robot waiting;
         telemetry.addData("To Start Test", "Press Play");    //
     }
 
     /*
      * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
      */
     @Override
     public void init_loop() {
     }
     public boolean dpadup = false;
     public boolean dpaddown = false;
     public boolean dpadleft = false;
     public boolean dpadright = false;
     public boolean buttona = false;
     public boolean buttonb = false;
     public boolean buttonx = false;
     public boolean buttony = false;
     public boolean buttonback = false;
     public boolean buttonstart = false;
     public boolean buttonjoyright = false;
     public boolean buttonjoyleft = false;
     public boolean rightbumper = false;
     public boolean leftbumper = false;
     public boolean righttrigger = false;
     public boolean lefttrigger = false;
     public boolean joyrightx = false;
     public boolean joyrighty = false;
     public boolean joyleftx = false;
     public boolean joylefty = false;

     /*
      * Code to run ONCE when the driver hits PLAY
      */
     @Override
     public void start() {
     }
 
     /*
      * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
      */
     @Override
     public void loop() {


         telemetry.addLine("*** Controller Test ****");

         telemetry.addLine().addData("", dpadup).addData("Dpad Up    ", gamepad1.dpad_up);
         telemetry.addLine().addData("", dpaddown).addData("Dpad Down  ", gamepad1.dpad_down);
         telemetry.addLine().addData("", dpadleft).addData("Dpad Left  ", gamepad1.dpad_left);
         telemetry.addLine().addData("", dpadright).addData("Dpad Right ", gamepad1.dpad_right);
         telemetry.addLine().addData("", buttona).addData("Button A   ", gamepad1.a);
         telemetry.addLine().addData("", buttonb).addData("Button B   ", gamepad1.b);
         telemetry.addLine().addData("", buttonx).addData("Button X   ", gamepad1.x);
         telemetry.addLine().addData("", buttony).addData("Button Y   ", gamepad1.y);
         telemetry.addLine().addData("", buttonback).addData("Btn   Back ", gamepad1.back);
         telemetry.addLine().addData("", buttonstart).addData("Btn   Start", gamepad1.start);
         telemetry.addLine().addData("", buttonjoyright).addData("R Joy Btn  ", gamepad1.right_stick_button);
         telemetry.addLine().addData("", buttonjoyleft).addData("L Joy Btn  ", gamepad1.left_stick_button);
         telemetry.addLine().addData("", rightbumper).addData("R Bumper   ", gamepad1.right_bumper);
         telemetry.addLine().addData("", leftbumper).addData("L Bumper   ", gamepad1.left_bumper);
         telemetry.addLine().addData("", righttrigger).addData("R Trigger  ", gamepad1.right_trigger);
         telemetry.addLine().addData("", lefttrigger).addData("L Trigger  ", gamepad1.left_trigger);
         telemetry.addLine().addData("", joyrightx).addData("R Joy X    ", gamepad1.right_stick_x);
         telemetry.addLine().addData("", joyrighty).addData("R Joy Y    ", gamepad1.right_stick_y);
         telemetry.addLine().addData("", joyleftx).addData("L Joy X    ", gamepad1.left_stick_x);
         telemetry.addLine().addData("", joylefty).addData("L Joy Y    ", gamepad1.left_stick_y);


         if (gamepad1.dpad_up) dpadup = true;
         if (gamepad1.dpad_down) dpaddown = true;
         if (gamepad1.dpad_left) dpadleft = true;
         if (gamepad1.dpad_right) dpadright = true;
         if (gamepad1.a) buttona = true;
         if (gamepad1.b) buttonb = true;
         if (gamepad1.x) buttonx = true;
         if (gamepad1.y) buttony = true;
         if (gamepad1.back) buttonback = true;
         if (gamepad1.start) buttonstart = true;
         if (gamepad1.right_stick_button) buttonjoyright = true;
         if (gamepad1.left_stick_button) buttonjoyleft = true;
         if (gamepad1.right_bumper) rightbumper = true;
         if (gamepad1.left_bumper) leftbumper = true;
         if (gamepad1.right_trigger > 0.1) righttrigger = true;
         if (gamepad1.left_trigger > 0.1) lefttrigger = true;
         if (gamepad1.right_stick_x > 0.1) joyrightx = true;
         if (gamepad1.right_stick_y > 0.1) joyrighty = true;
         if (gamepad1.left_stick_x > 0.1) joyleftx = true;
         if (gamepad1.left_stick_y > 0.1) joylefty = true;


         if ((dpadup) &&
                 (dpaddown) &&
                 (dpadleft) &&
                 (dpadright) &&
                 (buttona) &&
                 (buttonb) &&
                 (buttonx) &&
                 (buttony) &&
                 (buttonback) &&
                 (buttonstart) &&
                 (buttonjoyright) &&
                 (buttonjoyleft) &&
                 (rightbumper) &&
                 (leftbumper) &&
                 (righttrigger) &&
                 (lefttrigger) &&
                 (joyrightx) &&
                 (joyrighty) &&
                 (joyleftx) &&
                 (joylefty)) {

             telemetry.addLine("****** All TESTS COMPLETE*******");
         }
        telemetry.update();

     }
     /*
      * Code to run ONCE after the driver hits STOP
      */
     @Override
     public void stop() {
     }
 }
