package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import android.os.Handler;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//For added simplicity while coding autonomous with the new FTC system. Utilized inheritance and polymorphism.
public abstract class AutonomousBase extends RobotBase {

    //Only used during autonomous.
    protected GyroSensor gyroscope;
    protected ColorSensor leftColorSensor, rightColorSensor, bottomColorSensor;
    protected Servo leftSensorServo, rightSensorServo;
    protected final int LEFT_SERVO_MAX = 90, RIGHT_SERVO_MAX = 90;

    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Initialize gyroscope (will output whether it was found or not.
        gyroscope = Initialize(GyroSensor.class, "Gyroscope");
        OutputToDriverStation("Got gyro sensor");
        if (gyroscope != null)
        {
            //Start gyroscope calibration.
            OutputToDriverStation("Gyroscope Calibrating...");
            gyroscope.calibrate();
            //Pause to prevent errors.
            sleep(1000);

            while (gyroscope.isCalibrating())
                sleep(50);

            OutputToDriverStation("Gyroscope Calibration Complete!");
        }

        //Initialize color sensors for either side (do in AutonomousBase because they are useless during teleop.
        leftColorSensor = Initialize(ColorSensor.class, "colorLeft");
        rightColorSensor = Initialize(ColorSensor.class, "colorRight");
        bottomColorSensor = Initialize(ColorSensor.class, "colorBottom");

        leftSensorServo = Initialize(Servo.class, "servoLeft");
        rightSensorServo = Initialize(Servo.class, "servoRight");

        leftSensorServo.setPosition(1.0);
        rightSensorServo.setPosition(1.0);
    }

    //All children should have special instructions.
    protected abstract void driverStationSaysGO();

    private final double MIN_TURN_POWER = .1, MAX_TURN_POWER = .5;

    //Used to turn to a specified heading.
    protected void turn(double power, double heading)
    {
        if (gyroscope != null)
        {
            //Output a message to the user
            OutputToDriverStation("Turning to " + heading + " degrees WITH GYRO at " + power + " power.");

            //Wait a moment: otherwise there tends to an error.
            sleep(500);
            gyroscope.resetZAxisIntegrator();
            sleep(500);

            //The variables used to calculate the way that the motors will turn.
            int currHeading;
            double leftPower, rightPower;

            while (getGyroscopeHeading() != heading) {
                //Get the gyroscope heading.
                currHeading = getGyroscopeHeading();

                //Calculate the power of each respective motor.
                leftPower = (currHeading - heading) * power;
                rightPower = (heading - currHeading) * power;

                //Clamp values.
                if (leftPower >= 0)
                    leftPower = Range.clip(leftPower, MIN_TURN_POWER, MAX_TURN_POWER);
                else if (leftPower < 0)
                    leftPower = Range.clip(leftPower, -MAX_TURN_POWER, -MIN_TURN_POWER);

                if (rightPower >= 0)
                    rightPower = Range.clip(rightPower, MIN_TURN_POWER, MAX_TURN_POWER);
                else if (rightPower < 0)
                    rightPower = Range.clip(rightPower, -MAX_TURN_POWER, -MIN_TURN_POWER);

                //Set the motor powers.
                left.setPower(leftPower);
                right.setPower(rightPower);

                //Output data to the DS.
                //Don't change this, since it is useful to have real-time data in this case.
                OutputRealTimeData(
                        new String[] {
                                "Turning with gyro...",
                                "Current heading " + currHeading,
                                "Turning to " + heading,
                                "Left Power " + leftPower,
                                "Right Power " + rightPower
                        }
                );

                idle();
            }
        } else {
            //Important for the driver to know.
            OutputToDriverStation("Turning " + (power >= 0 ? "left" : "right") + " for " + heading + " seconds WITHOUT GYRO");

            //The turning point.
            left.setPower(power);
            right.setPower(power);

            //Sleep for some period of time.
            sleep((int) (heading));
        }

        stopMotors();

        OutputToDriverStation("Turn completed.");
    }

    //Used to drive in a straight line with the aid of the gyroscope.
    protected void drive(double power, double length)
    {
        //Add the output to the driver station.
        OutputToDriverStation("Driving at " + power + " power, for " + length + " seconds," + "WITH" + (gyroscope == null ? "OUT" : "") + "a gyroscope");

        //Initialize the gyro if it exists.
        if (gyroscope != null)
            gyroscope.resetZAxisIntegrator();

        //Required variables.
        double startTime = System.currentTimeMillis();
        int heading;

        //Set initial motor powers.
        left.setPower(power);
        right.setPower(power);

        sleep(100);

        //Gyroscope turning mechanics.
        while (System.currentTimeMillis() - startTime < length)
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

                //Output data to the DS.
                //Note: the 2nd parameter "%.2f" changes the output of the max decimal points.
                OutputRealTimeData(
                        new String[]{
                                "Heading: " + heading,
                                "Current Time: " + ((System.currentTimeMillis() - startTime) * 1),
                                "Stopping at: " + length,
                                "L Power: " + leftPower,
                                "R Power: " + rightPower
                        }
                );

            }

            //Wait a hardware cycle.
            idle();
        }
        //Stop the bot.
        stopMotors();

        OutputToDriverStation("Movement completed");
    }

    private Handler handler;
    private Runnable thread;

    protected void startDriving (double power)
    {
        if (handler == null)
        {
            OutputToDriverStation("Started driver thread");
            handler = new Handler(hardwareMap.appContext.getMainLooper());

            thread = new Runnable()
            {
                @Override
                public void run()
                {
                    OutputToDriverStation("Thread just ran through one loop.");
                    sleep(50);
                }
            };

            handler.post(thread);
        }
        else
        {
            OutputToDriverStation("Can't make new handler, handler is already created!");
        }
    }

    protected void stopDriving()
    {
        if (handler != null)
        {
            OutputToDriverStation("Stopped handler on stop");
            handler.removeCallbacks(thread);
            handler = null;
        }
        else
        {
            OutputToDriverStation("Can't stop handler, handler is not created!");
        }
    }

    //The gyroscope value goes from 0 to 360: when the bot turns left, it immediately goes to 360.  This makes sure that the value makes sense for calculations.
    protected int getGyroscopeHeading() {
        int heading = gyroscope.getHeading();

        if (heading > 180 && heading < 360)
            heading -= 360;

        return heading;
    }

    //Stops all drive motors.
    protected void stopMotors() {
        left.setPower(0);
        right.setPower(0);
    }
}
