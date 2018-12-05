package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "User Test", group = "Test")
public class UserTest extends LinearOpMode {

    UserControl User = new UserControl();
    boolean bYesNo;
    boolean bLeftRight;
    boolean bRedBlue;
    UserControl.DPAD dPad;

    public void getUserInput() {
        bYesNo = User.getYesNo("Test it?");
        if (bYesNo)
            telemetry.addLine("Yes");
        else
            telemetry.addLine("No");
        telemetry.update();

        bLeftRight = User.getLeftRight("Which way?");
        if (bLeftRight)
            telemetry.addLine("Left");
        else
            telemetry.addLine("Right");
        telemetry.update();

        bRedBlue = User.getRedBlue("Which color?");
        if (bRedBlue)
            telemetry.addLine("Red");
        else
            telemetry.addLine("Blue");
        telemetry.update();

        dPad = User.getDPad("Which direction?");
        switch (dPad) {
            case UP:
                telemetry.addLine("Up");
                break;
            case DOWN:
                telemetry.addLine("Down");
                break;
            case LEFT:
                telemetry.addLine("Left");
                break;
            case RIGHT:
                telemetry.addLine("Right");
                break;
        }
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        telemetry.addLine("User Test OpMode");
        telemetry.update();

        /* initialize sub-assemblies
         */
        User.init(this);
        telemetry.update();

        /* Get user information */
        getUserInput();


        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            if (gamepad1.x) {
                bYesNo = User.getYesNo("Test it?");
            }
            if (bYesNo)
                telemetry.addLine("Yes");
            else
                telemetry.addLine("No");
            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }

    }
}
