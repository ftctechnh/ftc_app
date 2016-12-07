package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Red Beacons Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousRedBeacons extends AutonomousBase {
    //Autonomous code for the Red alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        rightSensorServo.setPosition(RIGHT_SERVO_MAX);
        sleep(0);
        drive(0.5, 200);   // Drive forward a little ways from the wall.
        turn(0.4, 50);       // Turn 40 degrees.

        double power = 0.6;
        int heading;

        sleep(500);
        gyroscope.resetZAxisIntegrator();
        sleep(500);

        //Set initial motor powers.
        left.setPower(power);
        right.setPower(power);

        //Gyroscope turning mechanics.
        while (bottomColorSensor.alpha() < 10)
        {
            if (gyroscope != null)
            {
                // Get the heading info.
                heading = getGyroscopeHeading();

                //The gyroscope heading value has to be translated into a useful value.  It currently goes to 359 and then moves down when moving clockwise, and goes up from 0 at moving counter-clockwise.


                //Create values.
                double leftPower = power + (heading) / (20.0);
                double rightPower = power - (heading) / (20.0);

                //Clamp values.
                if (leftPower > 1)
                    leftPower = 1;
                else if (leftPower < -1)
                    leftPower = -1;

                if (rightPower > 1)
                    rightPower = 1;
                else if (rightPower < -1)
                    rightPower = -1;

                //Set the motor powers.
                left.setPower(leftPower);
                right.setPower(rightPower);
            }

            //Wait a hardware cycle.
            idle();
        }

        drive(-0.5, 50);
        stopMotors();
        sleep(1000);

        drive(0.5, -50);
        turn(.6, -50);
        //Stop the bot.
        stopMotors();

        OutputToDriverStation("Movement to color sensor completed");
    }
}