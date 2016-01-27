    package com.powerstackers.resq.common;

    import static java.lang.Math.PI;
    import static java.lang.Math.abs;

    /**
     * Created by Derek on 1/18/2016.
     */
public class JonsAlgo {

        RobotAuto robot;

    public JonsAlgo(RobotAuto robot) {
        this.robot = robot;
    }

/*
*	GLOBAL CONSTANTS
*/
    private
    double ticksPerRevolution = 1120;	// Number of encoder ticks per motor rotation
    double wheelDiameter = 4;	// Diameter of your wheels in inches
    double driveGearMultiplier = 1.0;		// Drive gear multiplier.
    // EXAMPLE: If your drive train is geared 2:1 (1 motor rotation = 2 wheel rotations), set this to 2
    double turnOvershootThreshold = 0.1;



    /*
    *	inchesToTicks
    *	Convert a distance in inches to a number of ticks
    */
    long inchesToTicks(double inches)
    {
        // Given a distance in inches, calculate the equivalent distance in motor encoder ticks.
        // We calculate this by taking the number of wheel rotations (inches/(PI*wheelDiameter)) multiplied
        // by the inverse of the gear ratio, to get the number of motor rotations. Multiply one more time
        // by the number of motor encoder ticks per one motor revolution.

        // Optional debug information
	/*writeDebugStreamLine("Drive gear multiplier: %f", 1/driveGearMultiplier);
	writeDebugStreamLine("Ticks per revolution: %d", ticksPerRevolution);
	writeDebugStreamLine("Inches to travel: %f", (inches/(PI*wheelDiameter)));*/
        return (long) ((1/driveGearMultiplier)*ticksPerRevolution*(inches/(PI*wheelDiameter)));
    }

    /*
    *	ticksToInches
    *	Convert a number of ticks to a distance in inches
    */
    double ticksToInches(long ticks)
    {
        // Given a number of encoder ticks, calculate the equivalent distance in inches.
        // We calculate this by taking the number of ticks traveled, divided by the number of ticks per revolution,
        // and then multiplied by the gear ratio multiplier to get the number of wheel rotations. Multiply one more
        // time by the circumference of the wheels (PI*wheelDiameter).
        return (ticks/ticksPerRevolution)*driveGearMultiplier*(PI*wheelDiameter);
    }

    /*
    *	goTicks
    *	Move the robot a discance in ticks
    */
    void goTicks(long ticks, double speed/*, bool collisionAvoidance*/)
    {

        long startLeft = robot.getLeftEncoder();
        long startRight = robot.getRightEncoder();

        // Target encoder values for the left and right motors
        long targetRight = startRight + ticks;
        long targetLeft = startLeft + ticks;

        double leftCorrect	= 1.0;
        double rightCorrect	= 0.9;


        // Print what we are going to do to the debug stream
//        writeDebugStreamLine("-- GOING TICKS --\n\tMoving %d ticks (%3.2f inches) %s at %d speed",
//        ticks, ticksToInches(ticks), ((ticks>0)?"forward":"backward"), speed);

        // If we are going forwards or backwards
        // A positive number of ticks to travel indicates we are moving fowards.
        // A negative value indicates we are moving backwards.
        if(ticks > 0)
        {
            // Set the drive motors to the given speed
            //driveMotorsTo(speed);
            robot.setPowerLeft(speed * leftCorrect);
            robot.setPowerRight(speed * rightCorrect);

            // While this function runs, keep the robot on a constant heading
            //StartTask(stablizePath);
            // Wait until both motors have reached the target
            while(robot.getLeftEncoder() < targetLeft && robot.getRightEncoder() < targetRight)
            {
                //writeDebugStreamLine("Curr: %d\tTarg: %d", nMotorEncoder[mDriveLeft], targetLeft);
            }
            // Now that we've reached our destination, turn off the stablization
//            StopTask(stablizePath);
            // Stop the drive motors here
            robot.setPowerLeft(0);
            robot.setPowerRight(0);
        }
        else if(ticks < 0)
        {
            // Set the drive motors to the speed (in reverse)
            //driveMotorsTo(-1 * speed);
            robot.setPowerLeft(-speed * leftCorrect);
            robot.setPowerRight(-speed * rightCorrect);

            // While this function runs, keep the robot on a constant heading
            //StartTask(stablizePath);
            // Wait until both motors have reached the target
            while(robot.getLeftEncoder() > targetLeft && robot.getRightEncoder() > targetRight)
            {
                //writeDebugStreamLine("Curr: %d\tTarg: %d", nMotorEncoder[mDriveLeft], targetLeft);
            }
            // Now that we've reached our destination, turn off the stablization
//            StopTask(stablizePath);
            // Turn off the drive motors here
            robot.setPowerLeft(0);
            robot.setPowerRight(0);
        }

        // Write to the debug stream that we are done
//        writeDebugStreamLine("-- MOVING DONE --\n\tTotal distance travelled: %2.2f inches", ticksToInches(nMotorEncoder[mDriveLeft] - startLeft));
    }

    /*
    *	turnDegrees
    *	Turn the robot a certain number of degrees
    */
    void turnDegrees(double degrees, int speed) throws InterruptedException {
//         Store the number of degrees turned so far, i.e., the difference of
        // the current position and the starting position.

        double degreesSoFar = 0;

        // Take an initial reading of the gyro sensor. This compensates for any initial spin the gyro may have.

//        double initialTurnReading = currentGryoReading();

        // Decide whether to turn clockwise or counterclockwise.
        // A positive degree target inmplies turning counterclockwise. A negative target implies clockwise.


        if(degrees < 0)
        {
            robot.setPowerLeft(-1 * speed);
            robot.setPowerRight(speed);
        }
        else
        {
           robot.setPowerLeft(speed);
            robot.setPowerRight(-1 * speed);
        }

        //writeDebugStreamLine("\tSet left motor to %d", motor[mDriveLeft]);
        //writeDebugStreamLine("\tSet right motor to %d", motor[mDriveRight]);

        // For as long as the current degree measure doesn't equal the target. This will work in the clockwise and
        // counterclockwise directions, since we are comparing the absolute values
        while(abs(degreesSoFar) < abs(degrees))
        {
            //writeDebugStreamLine("%d", time1[T1];
            //ClearTimer(T1);
            // 10 millisecond interval
//            wait(10);

            // Calculate the gyro's angular velocity reading.
            // The reading is given as the current sensor value, minus any initial spin that the gyro may have had.

//            double reading = currentGryoReading() - initialTurnReading;

            // Gyro sensor returns an angular speed. To calculate the distance, we multiply the rate by the time interval (.01 seconds).
//            degreesSoFar += reading * 0.02;

            //	writeDebugStreamLine("dgr: %f\t\tred: %f", degreesSoFar, reading);
            //	nxtDisplayTextLine(7, "%3.2f", degreesSoFar);

        }

        // Stop all drive motors
        robot.setPowerAll(0);

        // Notify the drivers that we are done.
//        writeDebugStreamLine("\tTotal degrees turned: %f", degreesSoFar);

        //writeDebugStreamLine("Degreees turned: %f", degreesSoFar);
        //writeDebugStreamLine("Degrees wanted: %f", degrees);

        // If the turn overshot, turn back the other direction a small amount
        if(abs(degreesSoFar - degrees) > turnOvershootThreshold)
        {
//            writeDebugStreamLine("Turn overshot by %f degrees! Turning back...", degreesSoFar - degrees);
            turnDegrees(-1*(degreesSoFar - degrees), 50);
        }

    }


    /*
    *	wallAlign
    *	Use a wall or other rigid surface to align the robot.
    */
    void wallAlign(boolean forwardBackward) throws InterruptedException {
//        writeDebugStreamLine("-- ALIGNING ROBOT --\n\tMoving %s", forwardBackward? "forward":"backward");

        boolean ALIGN_FORWARD =	true;
        boolean ALIGN_BACKWARD = false;

        double alignSpeed = 40;

        // If we are going to move forward into the wall
        if(forwardBackward==ALIGN_FORWARD)
        {
            robot.setPowerAll(alignSpeed);
        }
        // If we are going to move backward into the wall
        else
        {
            robot.setPowerAll(-alignSpeed);
        }

        // Store whether the left and right sides are finished aligning
        boolean rightDone = false;
        boolean leftDone = false;

        // Store the previously read value of the left and right encoders, and initialize them to the current positions
        long leftPrevValue = robot.getLeftEncoder();
        long rightPrevValue = robot.getRightEncoder();

        // This is our threshold. A change less than this will indicate that the motor has met resistance
        long stopThreshold = 300;

        while(!rightDone && !leftDone)
        {
            // Wait for 1 second to give the motors time to move, if they're going to
            wait(100);

            // If the left motor hasn't changed an acceptable amount in the last second, then it has met resistance
            if(abs(leftPrevValue - robot.getLeftEncoder()) < stopThreshold && !leftDone)
            {
                // Turn the motor off, and indicate that this side is aligned
                robot.setPowerLeft(0);
                leftDone = true;
//                writeDebugStreamLine("\tLeft side aligned. Diff: %d", abs(leftPrevValue - nMotorEncoder[mDriveLeft]));
//                PlaySound(soundBeepBeep);

                // Set the opposite drive motor to full power to finish aligning the robot
                if(!rightDone)
                   robot.setPowerRight(ALIGN_FORWARD?100:-100);
            }
            // If the right motor hasn't changed an acceptable amount in the last second, then it has met resistance
            if(abs(rightPrevValue - robot.getRightEncoder()) < stopThreshold && !rightDone)
            {
                // Turn the motor off, and indicate that this side is aligned
                robot.setPowerRight(0);
                rightDone = true;
//                writeDebugStreamLine("\tRight side aligned. Diff: %d", abs(rightPrevValue - nMotorEncoder[mDriveRight]));
//                PlaySound(soundBeepBeep);

                // Set the opposite drive motor to full poewr to finish aligning the robot
                if(!leftDone)
                    robot.setPowerLeft(ALIGN_FORWARD?100:-100);
            }

            leftPrevValue = robot.getLeftEncoder();
            rightPrevValue = robot.getRightEncoder();

        }

//        writeDebugStreamLine("-- ALIGNMENT DONE --");
    }
}
