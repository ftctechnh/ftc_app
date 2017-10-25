
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Ana Controller Test", group="Pushbot")
//@Disabled
public class AnaControllerTest extends OpMode{

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
        


        if (gamepad1.dpad_up) dpadup = true;
        if (gamepad1.dpad_down) dpaddown = true;
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
                (dpaddown)     ) {

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
