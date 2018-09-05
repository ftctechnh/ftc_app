package org.firstinspires.ftc.teamcode.SubAssembly.Blade;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;
    /* Sub Assembly Test OpMode
     * This TeleOp OpMode is used to test the functionality of the specific sub assembly
     */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
    @TeleOp(name = "BladeTest", group = "Test")
    public class BladeTest extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {

            telemetry.addLine("Blade Test OpMode");

            /* initialize sub-assemblies
             */
            BladeConfig Blade = new BladeConfig(this);

            telemetry.update();

            //waits for that giant PLAY button to be pressed on RC
            waitForStart();

            //telling the code to run until you press that giant STOP button on RC
            while (opModeIsActive()) {

                Blade.test();
                /*Moving the Blade from the middle position to the left side to the right side and loops*/
                Blade.Servo_1.setSetpoint(BladeConfig.Setpoints.POS_2);
                sleep(1000);
                Blade.Servo_1.setSetpoint(BladeConfig.Setpoints.POS_1);
                sleep(1000);
                Blade.Servo_1.setSetpoint(BladeConfig.Setpoints.POS_3);
                sleep(1000);
                telemetry.update();

                //let the robot have a little rest, sleep is healthy
                sleep(40);
            }

        }
    }

