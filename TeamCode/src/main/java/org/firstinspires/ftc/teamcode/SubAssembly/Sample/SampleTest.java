package org.firstinspires.ftc.teamcode.SubAssembly.Sample;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "SubAssembly Test", group = "Test")
public class SampleTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("SubAssembly Test OpMode");

        /* initialize sub-assemblies
         */
        SampleTemplate SubAssembly = new SampleTemplate(this);

        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            SubAssembly.test();
            telemetry.update();

            SubAssembly.Servo_1.setSetpoint(SampleTemplate.Setpoints.POS_2);
            SubAssembly.Servo_1.nextSetpoint();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}
