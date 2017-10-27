package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

//Importing useful classes for the motors, servos, and sensors
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

public class State_Full_Blue_Autonomous extends LinearOpMode {

    //Setting up global variables
    ElapsedTime timer = new ElapsedTime();
    StateHardwareMap robot = new StateHardwareMap();
    
    //Main function that automatically runs when program is started
    public void runOpMode() {
        //Sets up the hardware map
        robot.init(hardwareMap);

        //Waiting for program to be started before starting
            waitForStart();

        shoot();
        positionNextBall();
        shoot();
        driveInches(-10);
        gyroTurn(180);
        gyroTurn(-45);
        driveInches(50);
        gyroTurn(45);//2.3-4.3
        float heaading = robot.heading;
        align();
        gyroTurn(-(robot.heading - heaading));
        heaading = robot.heading;
        align();
        gyroTurn(-(robot.heading - heaading));
        lineUp(-1);
        pressBeacon();
        driveInches(15);
        lineUp(1);
        pressBeacon();

        robot.fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fright.setTargetPosition(-420);
        robot.fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.fright.setPower(-1.5 * robot.DRIVE_SPEED);
        robot.bright.setPower(-1.5 * robot.DRIVE_SPEED);
        while (robot.fright.getCurrentPosition() > robot.fright.getTargetPosition() && opModeIsActive()) {
        }
        robot.fright.setPower(0);
        robot.bright.setPower(0);

        robot.fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fleft.setTargetPosition(-420);
        robot.fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.fleft.setPower(-1.5 * robot.DRIVE_SPEED);
        robot.bleft.setPower(-1.5 * robot.DRIVE_SPEED);
        while (robot.fleft.getCurrentPosition() > robot.fleft.getTargetPosition() && opModeIsActive()) {
        }
        robot.fleft.setPower(0);
        robot.bleft.setPower(0);

        gyroTurn(127);
        while (robot.ultrasonic.getUltrasonicLevel() / 2.54 < 10) {
            robot.fright.setPower(robot.DRIVE_SPEED);
            robot.bright.setPower(robot.DRIVE_SPEED);
            robot.fleft.setPower(robot.DRIVE_SPEED);
            robot.bleft.setPower(robot.DRIVE_SPEED);
        }

        robot.fleft.setPower(0);
        robot.bleft.setPower(0);
        robot.fright.setPower(0);
        robot.bright.setPower(0);
    }


    //Turns the robot a specified number of degrees(might not work when wheel direction is reversed) right = negative
    public void gyroTurn(float degrees) {
        //Sets the current robot.heading as the initial robot.heading for reference when turning
        float gyroheadingInitial = robot.heading;
        //Turns the correct direction until the angle has been reached
        if (degrees <= 0) {
            while (robot.heading > degrees + gyroheadingInitial && opModeIsActive()) {
                robot.fleft.setPower(robot.DRIVE_SPEED);
                robot.bleft.setPower(robot.DRIVE_SPEED);
                robot.fright.setPower(-robot.DRIVE_SPEED);
                robot.bright.setPower(-robot.DRIVE_SPEED);
                robot.updateGyro();
            }
        } else {
            while (robot.heading < degrees + gyroheadingInitial && opModeIsActive()) {
                robot.fleft.setPower(-robot.DRIVE_SPEED);
                robot.bleft.setPower(-robot.DRIVE_SPEED);
                robot.fright.setPower(robot.DRIVE_SPEED);
                robot.bright.setPower(robot.DRIVE_SPEED);
                robot.updateGyro();
            }
        }

        //Stops wheels
        robot.fleft.setPower(0);
        robot.bleft.setPower(0);
        robot.fright.setPower(0);
        robot.bright.setPower(0);
    }

    //Drives forward until it finds the line in front of the beacon and stops
    //Set direction to 1 to go forwards, and -1 to go backwards
    public void lineUp(int direction) {
        robot.fleft.setPower(robot.DRIVE_SPEED * .5 * direction);
        robot.bleft.setPower(robot.DRIVE_SPEED * .5 * direction);
        robot.fright.setPower(robot.DRIVE_SPEED * .5 * direction);
        robot.bright.setPower(robot.DRIVE_SPEED * .5 * direction);
        while(robot.leftLight.getLightDetected() < robot.COLOR_THRESHOLD && robot.rightLight.getLightDetected() < robot.COLOR_THRESHOLD && opModeIsActive()){}
        while(robot.leftLight.getLightDetected() < robot.COLOR_THRESHOLD && opModeIsActive()) {
            robot.fright.setPower(0);
            robot.bright.setPower(0);
            robot.fleft.setPower(robot.DRIVE_SPEED * .35 * direction);
            robot.bleft.setPower(robot.DRIVE_SPEED * .35 * direction);
        }
        while(robot.rightLight.getLightDetected() < robot.COLOR_THRESHOLD && opModeIsActive()) {
            robot.fleft.setPower(0);
            robot.bleft.setPower(0);
            robot.fright.setPower(robot.DRIVE_SPEED * .35 * direction);
            robot.bright.setPower(robot.DRIVE_SPEED * .35 * direction);
        }
        robot.fleft.setPower(0);
        robot.bleft.setPower(0);
        robot.fright.setPower(0);
        robot.bright.setPower(0);
    }

    //Uses the color sensor to detect which button to press, then presses it(not currently functional)
    public void pressBeacon() {
        //Checks if the color is red and hits it if it is(have to find a better way to detect the colors)
        robot.leftBeacon.setPosition(0.5);
        robot.rightBeacon.setPosition(0.5);

        if(robot.color.red() > robot.color.blue() && opModeIsActive()) {
            telemetry.addData("left", true);
            timer.reset();
            while(timer.seconds() < 2 && opModeIsActive())
                robot.leftBeacon.setPosition(1);
            robot.leftBeacon.setPosition(.5);
        }

        //If it isn't blue it hits the other one
        else {
            telemetry.addData("right", true);
            timer.reset();
            while(timer.seconds() < 2 && opModeIsActive())
                robot.rightBeacon.setPosition(0);
            robot.rightBeacon.setPosition(.5);
        }

        //Runs the pushers backwards to reset their position
        timer.reset();
        while(timer.seconds() < 2 && opModeIsActive()) {
            robot.leftBeacon.setPosition(0);
            robot.rightBeacon.setPosition(1);
        }
        robot.leftBeacon.setPosition(.5);
        robot.rightBeacon.setPosition(.5);
    }

    //Note for using encoders:
    //One rotation of an 40 gear reduction Andymark motor is 1120
    //One rotation of a Tetrix motor is 1440

    //Drives the robot a specified number of inches(negative value to go backwards)
    public void driveInches(float inches){
        //Figures out what value to give the encoder based on the amount of inches to be covered
        int encoderInput = (int) java.lang.Math.floor((inches / (robot.WHEEL_DIAMETER * 3.1416)) * 1120); //Change 1120 based on motor type
        //Resets encoders and sets the power and position to be used
        robot.fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fright.setTargetPosition(encoderInput);
        if(inches > 0) {
            robot.fright.setPower(robot.DRIVE_SPEED);
            robot.bright.setPower(robot.DRIVE_SPEED);
            robot.fleft.setPower(robot.DRIVE_SPEED);
            robot.bleft.setPower(robot.DRIVE_SPEED);
        }

        else {
            robot.fright.setPower(-robot.DRIVE_SPEED);
            robot.bright.setPower(-robot.DRIVE_SPEED);
            robot.fleft.setPower(-robot.DRIVE_SPEED);
            robot.bleft.setPower(-robot.DRIVE_SPEED);
        }
        robot.fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        timer.reset();
        robot.updateGyro();
        double initAngle = robot.heading;
        while(Math.abs(robot.fright.getCurrentPosition()) < Math.abs(robot.fright.getTargetPosition()) && timer.seconds() < 5 && opModeIsActive()){
            //Waits until the motors have gone the right distance, then stops one side early if it reached the target position first
            robot.updateGyro();
            if (robot.heading > initAngle) {
                robot.fleft.setPower(robot.DRIVE_SPEED * 1.5);
                robot.bleft.setPower(robot.DRIVE_SPEED * 1.5);
                robot.fright.setPower(robot.DRIVE_SPEED * 0.5);
                robot.bright.setPower(robot.DRIVE_SPEED * 0.5);
            } else if (robot.heading < initAngle) {
                robot.fleft.setPower(robot.DRIVE_SPEED * 0.5);
                robot.bleft.setPower(robot.DRIVE_SPEED * 0.5);
                robot.fright.setPower(robot.DRIVE_SPEED * 1.5);
                robot.bright.setPower(robot.DRIVE_SPEED * 1.5);
            } else {
                robot.fleft.setPower(robot.DRIVE_SPEED);
                robot.bleft.setPower(robot.DRIVE_SPEED);
                robot.fright.setPower(robot.DRIVE_SPEED);
                robot.bright.setPower(robot.DRIVE_SPEED);
            }
        }
        robot.fright.setPower(0);
        robot.fright.setPower(0);

        //Stops wheels and sets motors back to their regular mode
        robot.fleft.setPower(0);
        robot.bleft.setPower(0);
        robot.fright.setPower(0);
        robot.bright.setPower(0);
        robot.fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //Runs the flicker at a specified power and encoder value, used for positionNextBall and shoot
    public void moveflicker(int encoderInput, float power){
        //Must be multiplied by 3/2 because it has a gear reduction of 60 instead of the usual 40
        encoderInput = (int) java.lang.Math.floor(encoderInput * 1.5 * 0.9) ;
        robot.flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flicker.setTargetPosition(encoderInput);
        robot.flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.flicker.setPower(power);
        timer.reset();
        while(Math.abs(robot.flicker.getCurrentPosition()) < Math.abs(robot.flicker.getTargetPosition()) && timer.seconds() < 5 && opModeIsActive()){
            telemetry.addData("robot.flicker current:", robot.flicker.getCurrentPosition());
            telemetry.addData("robot.flicker target:", robot.flicker.getTargetPosition());
            telemetry.update();
        }
        robot.flicker.setPower(0);
    }

    //Positions the next ball in the flicker
    public void positionNextBall(){
        //Runs the robot.intake motor which should correctly position the ball
        timer.reset();
        while(timer.seconds() < 3 && opModeIsActive()){
            robot.intake.setPower(-1);
        }
        robot.intake.setPower(0);

        //Moves the particle to the shooting location if the robot.intake doesn't correctly deliver it
        //Note: 373 is 1/3 of rotation
        moveflicker(-373, -.25f);
        moveflicker(373, .25f);
        timer.reset();
        while (timer.seconds() < 1 && opModeIsActive()){}
    }

    //Shoots the ball with the flicker
    public void shoot(){
        //Moves the particle to the shooting location if the intake doesn't correctly deliver it
        moveflicker(1120, .8f);
    }

    public void align(){
        driveInches(5);

        float distance = 0;
        while (distance == 0 && opModeIsActive()) {
            timer.reset();
            while(timer.seconds() < 1 && opModeIsActive()){}
            distance = (float) robot.ultrasonic.getUltrasonicLevel();
        }


        robot.fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fleft.setTargetPosition( (int) Math.floor( -210 * (distance / 2.54 - 2.8)));
        robot.fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.fleft.setPower(1.5 * robot.DRIVE_SPEED);
        robot.bleft.setPower(1.5 * robot.DRIVE_SPEED);
        while (robot.fleft.getCurrentPosition() < robot.fleft.getTargetPosition() && opModeIsActive()){}
        robot.fleft.setPower(0);
        robot.bleft.setPower(0);

        robot.fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fright.setTargetPosition( (int) Math.floor( -210 * (distance / 2.54 - 2.8)));
        robot.fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.fright.setPower(1.5 * robot.DRIVE_SPEED);
        robot.bright.setPower(1.5 * robot.DRIVE_SPEED);
        while(robot.fright.getCurrentPosition() < robot.fright.getTargetPosition() && opModeIsActive()){}
        robot.fright.setPower(0);
        robot.bright.setPower(0);
    }
}