package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "Autonomous - Red Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousRed extends AutonomousBase {
    //Autonomous code for the Red alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
//        drive(0.8, 1500);
//        turn(0.3, 110);
//        drive(1, 2000);
//        harvester.setPower(0.5);
//        sleep(3000);
        rightSensorServo.setPosition(RIGHT_SERVO_MAX);
        sleep(0);
        drive(0.5, 200);   // Drive forward a little ways from the wall.
        turn(.6, 40);       // Turn 40 degrees.

        //Initialize the gyro if it exists.
        if (gyroscope != null)
            gyroscope.resetZAxisIntegrator();

        double power = 0.8;
        int heading;

        //Set initial motor powers.
        left.setPower(power);
        right.setPower(power);

        sleep(100);

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
        //Stop the bot.
        stopMotors();

        OutputToDriverStation("Movement to color sensor completed");
    }
}