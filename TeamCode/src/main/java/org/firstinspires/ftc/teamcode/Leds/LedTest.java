package org.firstinspires.ftc.teamcode.Leds;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Led Test", group = "Test")
public class LedTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("LedTest: ");

        /* initialize sub-assemblies
         */
        LedControl Led = new LedControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Led.init(hardwareMap);

        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();


        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            egamepad1.updateEdge();
            egamepad2.updateEdge();

               Led.darkBlue();
               sleep(500);
               Led.darkGreen();
               sleep(500);
               Led.orange();
               sleep(500);
               Led.darkRed();
               sleep(500);
               Led.skyBlue();
               sleep(500);
               Led.lawnGreen();
               sleep(500);
               Led.white();
               sleep(500);
               Led.red();
               sleep(500);
               Led.rainbowRainbowPalette();
               sleep(3000);



            //SubAssembly.test();
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}
