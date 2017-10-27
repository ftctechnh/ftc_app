package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name = "Red_Shoot_Beacons_Cap(test)", group = "Autonomous")
public class Red_Shoot_Beacons_Middle extends LinearOpMode {
    //Setting the hardware map as a global variable
    SupersHardwareMap robot;

    public void runOpMode(){
        //Sets up the hardware map
        robot = new SupersHardwareMap(false);
        robot.init(hardwareMap);
        waitForStart();

        //Drives forward and shoots twice then turns towards the beacon line
        driveInches(10, 1.5);
        robot.timer.reset();
        while(robot.timer.seconds() < 0.5 && opModeIsActive()){}
        moveFlicker(1, 1);
        robot.timer.reset();
        while(robot.timer.seconds() < 1 && opModeIsActive()){}
        moveFlicker(1, 1);
        gyroTurn(40);

        //Drives towards line and follows it, hits beacon, turns towards next beacon and repeats
        hitBeacon(false);
        gyroTurn(robot.startingAngle - robot.heading);
        driveInches(10, 1);
        hitBeacon(false);

        //Turns towards the cap ball, shoves it, and parks on center platform
        gyroTurn(115);

        robot.color.enableLed(true);

        driveInches(55, 1);

        robot.ldrive(0);
        robot.rdrive(0);
        driveInches(5, 1);
    }

    //Runs the flicker a specified number of rotations at the default flicker speed times the specified power coefficient(negative to go backwards)
    public void moveFlicker(double rotations, double powerCoefficient) {
        //Sets target position for encoder
        //May have to change based on gear reduction, multiply by 1/2 for 20 and 3/2 for 60, 40 is standard
        //1440 is one rotation for tetrix, 1120 is one rotation for AndyMark
        robot.flicker.setTargetPosition((int) java.lang.Math.floor(rotations * 3/2 * 1120) + robot.flicker.getCurrentPosition());

        //Sets the power
        robot.flicker.setPower(-powerCoefficient * robot.FLICKER_SPEED);

        //Runs the flicker until slightly before the target position is reached, but once it brakes it will be in the right place
        while(Math.abs(robot.flicker.getTargetPosition() - robot.flicker.getCurrentPosition()) > 120 && opModeIsActive()) {
            telemetry.addData("Flicker target:", robot.flicker.getTargetPosition());
            telemetry.addData("Flicker current:", robot.flicker.getCurrentPosition());
            telemetry.update();
        }

        //Stops the flicker after the target position is reached
        robot.flicker.setPower(0);
    }
    //TEST OUT ENCODER VALUES TO SEE WHAT NEEDS TO BE REVERSED
    //Drives the robot a specified number of inches at the default autonomous speed times the specified power coefficient(negative to go backwards)
    //Only use in autonomous
    public void driveInches(int inches, double powerCoefficient) {
        //Reverses the amount of inches to go (effectively reversing the encoder values) and speed if the robot is on red mode(backwards)
        if(!robot.notreversed) {
            powerCoefficient = - powerCoefficient;
            inches = - inches;
        }

        //Sets mode to run to position so that the encoders are used
        /*robot.fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

        //Sets the target position based on the amount of inches to be covered and the starting position
        robot.fleft.setTargetPosition((int) java.lang.Math.floor((inches / (robot.WHEEL_DIAMETER * 3.1416)) * 1120) + robot.fleft.getCurrentPosition()); //Change 1120 based on motor type
        //Right motors are reversed, so negative encoder values should hopefully compensate for that
        //robot.fright.setTargetPosition((int) - java.lang.Math.floor((inches / (WHEEL_DIAMETER * 3.1416)) * 1120) + robot.fright.getCurrentPosition()); //Change 1120 based on motor type

        //Sets motor power based on the default autonomous speed and the power coefficient parameter
        robot.fleft.setPower(powerCoefficient * robot.AUTONOMOUS_DRIVE_SPEED);
        robot.fright.setPower(powerCoefficient * robot.AUTONOMOUS_DRIVE_SPEED);
        robot.bleft.setPower(powerCoefficient * robot.AUTONOMOUS_DRIVE_SPEED);
        robot.bright.setPower(powerCoefficient * robot.AUTONOMOUS_DRIVE_SPEED);

        //If this is too laggy, it may work better to use just one encoder
        //Keeps moving the wheels until they are within 10 encoder ticks of the target value
        while(Math.abs(robot.fleft.getTargetPosition() - robot.fleft.getCurrentPosition()) > 20 /* || Math.abs(robot.fright.getTargetPosition() - robot.fright.getCurrentPosition()) > 20*/ && opModeIsActive()) {
           /* //Turns off right or left wheels individually if they reach the target position first
            if(Math.abs(robot.fleft.getTargetPosition() - robot.fleft.getCurrentPosition()) <= 20 && robot.fleft.getPower() != 0) {
                robot.fleft.setPower(0);
                robot.bleft.setPower(0);
            }
            if(Math.abs(robot.fright.getTargetPosition() - robot.fright.getCurrentPosition()) <= 20 && robot.fright.getPower() != 0) {
                robot.fright.setPower(0);
                robot.bright.setPower(0);
            }
            */
            //Sends telemetry for debugging
            telemetry.addData("robot.fleft target", robot.fleft.getTargetPosition());
            telemetry.addData("robot.fleft current", robot.fleft.getCurrentPosition());
            telemetry.addData("robot.fright target", robot.fright.getTargetPosition());
            telemetry.addData("robot.fright current", robot.fright.getCurrentPosition());
            telemetry.update();
        }

        //Stops wheels after target position has been reached and sets motors back to their regular mode that doesn't use encoders
        robot.fleft.setPower(0);
        robot.fright.setPower(0);
        robot.bleft.setPower(0);
        robot.bright.setPower(0);

        /*robot.fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/
    }
    //Turns the robot a specified number of degrees, clockwise = negative angle
    //Only use in autonomous
    public void gyroTurn(double degrees) {
        robot.updateGyro();
        //Sets the current robot.robot.heading as the initial robot.heading for reference when turning
        float gyroheadingInitial = robot.heading;
        //Turns the correct direction until the angle has been reached
        if (degrees <= 0) {
            while (robot.heading > degrees + gyroheadingInitial && opModeIsActive()) {
                robot.ldrive(1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
                robot.rdrive(- 1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
                robot.updateGyro();
            }
        } else {
            while (robot.heading < degrees + gyroheadingInitial && opModeIsActive()) {
                robot.rdrive(1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
                robot.ldrive(-1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
                robot.updateGyro();
            }
        }

        //Stops wheels
        robot.ldrive(0);
        robot.rdrive(0);
    }

    //Drives up to the line in front of the beacon, follows it, then hits the beacon
    //For the time being, only use in autonomous
    public void hitBeacon(boolean colorisblue) {
        //Drives until line is found
        while(robot.ods2.getLightDetected() < robot.MIDDLE_REFLECTIVITY && opModeIsActive()) {
            robot.ldrive(0.75 * robot.AUTONOMOUS_DRIVE_SPEED);
            robot.rdrive(0.75 * robot.AUTONOMOUS_DRIVE_SPEED);
        }

        //Brakes
        robot.ldrive(0);
        robot.rdrive(0);

        /*if(colorisblue)
            gyroTurn(robot.startingAngle - robot.heading - 5);
        else
            gyroTurn(robot.startingAngle - robot.heading + 5);


        driveInches(-3, -1);*/
        /*//Line Following(sensor on right side of line on blue side)
        //Backs up to be on the correct side of the line if on blue side
        if(colorisblue) {
            while (ods2.getLightDetected() > MIDDLE_REFLECTIVITY && opModeIsActive()) {
                robot.ldrive(-robot.AUTONOMOUS_DRIVE_SPEED);
                robot.rdrive(-robot.AUTONOMOUS_DRIVE_SPEED);
            }
        }
        //Drives forward to the correct side of theline if on red side
        else {
            while (ods2.getLightDetected() > MIDDLE_REFLECTIVITY && opModeIsActive()) {
                robot.ldrive(robot.AUTONOMOUS_DRIVE_SPEED);
                robot.rdrive(robot.AUTONOMOUS_DRIVE_SPEED);
            }
        }
            //Makes the robot turn left if it is off of the line, right if it is on the line, and straight otherwise, then stops once close enough to the beacon(might be too laggy with all of the arithmetic)
            while(ods.getLightDetected() < BEACON_DISTANCE && opModeIsActive()) {
                double speed = (robot.AUTONOMOUS_DRIVE_SPEED + 0.1) -  robot.AUTONOMOUS_DRIVE_SPEED * (ods.getLightDetected() / BEACON_DISTANCE);
                robot.ldrive(speed + speed * ((MIDDLE_REFLECTIVITY - ods2.getLightDetected()) / (LINE_REFLECTIVITY - MIDDLE_REFLECTIVITY)));
                robot.rdrive(speed - speed * ((MIDDLE_REFLECTIVITY - ods2.getLightDetected()) / (LINE_REFLECTIVITY - MIDDLE_REFLECTIVITY)));
            }
        //Brakes
        robot.ldrive(0);
        robot.rdrive(0);*/


        //Optional thing to replace line following(light sensor would be exactly in the middle of the robot for this)
        //Turns 90 from the starting angle to face the beacon
        robot.updateGyro();
        if(colorisblue)
            gyroTurn(robot.startingAngle - robot.heading - 85);
        else
            gyroTurn(robot.startingAngle - robot.heading + 85);

        //Drives until close enough, and gets slower as it goes(replace with line following, go straight for testing
        while(robot.ods.getLightDetected() < robot.BEACON_DISTANCE && opModeIsActive()) {
            double speed = (robot.AUTONOMOUS_DRIVE_SPEED * .3 + 0.07) -  robot.AUTONOMOUS_DRIVE_SPEED * .3 * (robot.ods.getLightDetected() / robot.BEACON_DISTANCE);
            robot.ldrive(speed);
            robot.rdrive(speed);
        }

        robot.ldrive(0);
        robot.rdrive(0);

        robot.timer.reset();
        while(robot.timer.seconds() < 0.5 && opModeIsActive()) {}

        //Turns depending on robot.color
        if((colorisblue && robot.color.blue() > robot.color.red()) || (!colorisblue && robot.color.blue() < robot.color.red())) {
            robot.rdrive(1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
            robot.ldrive(-0.75 * robot.AUTONOMOUS_DRIVE_SPEED);
        }
        else {
            robot.ldrive(1.5 * robot.AUTONOMOUS_DRIVE_SPEED);
            robot.rdrive(-0.75 * robot.AUTONOMOUS_DRIVE_SPEED);
        }

        robot.timer.reset();
        while(robot.timer.seconds() < 0.25 && opModeIsActive()) {}

        robot.ldrive(0);
        robot.rdrive(0);

        while(robot.timer.seconds() < 1 && opModeIsActive()) {
            robot.ldrive(robot.AUTONOMOUS_DRIVE_SPEED * 0.75);
            robot.rdrive(robot.AUTONOMOUS_DRIVE_SPEED * 0.75);
        }

        driveInches(-8, -1);

        robot.updateGyro();
        if(colorisblue)
            gyroTurn(robot.startingAngle - robot.heading - 85);
        else
            gyroTurn(robot.startingAngle - robot.heading + 85);
        /*if(colorisblue)
            gyroTurn(startingAngle - robot.heading + 85);
        else
            gyroTurn(startingAngle - robot.heading - 85);*/
    }
}
