package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Made with Love by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "forward beacon blue")
@Disabled
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

        if (bColorSensorLeft.red() > bColorSensorLeft.blue()) {
            currentColor = "red";

            telemetry.addData("current color is red", bColorSensorLeft.red());
            telemetry.update();
        } else if (bColorSensorLeft.red() < bColorSensorLeft.blue()) {
            currentColor = "blue";

            telemetry.addData("current color is blue", bColorSensorLeft.blue());
            telemetry.update();

        } else {

            currentColor = "blank";
        }
    }
}
