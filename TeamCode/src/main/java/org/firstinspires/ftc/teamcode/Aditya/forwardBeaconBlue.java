package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.AutonomousGeneral;

/**
 * Made with Love by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "forward beacon blue")
public class forwardBeaconBlue extends AutonomousGeneral{

    String currentTeam = "blue";
    String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();


        waitForStart();
        {

            readNewColor();

            if (currentColor == currentTeam) {


                 encoderDrive(0.3, 25, 25, 8);

                readNewColor();
            }
        }
    }

    public void readNewColor() {

        currentColor = "blank";

        if (colorSensor.red() > colorSensor.blue()) {
            currentColor = "red";

            telemetry.addData("current color is red", colorSensor.red());
            telemetry.update();
        } else if (colorSensor.red() < colorSensor.blue()) {
            currentColor = "blue";

            telemetry.addData("current color is blue", colorSensor.blue());
            telemetry.update();

        } else {

            currentColor = "blank";
        }
    }
}
