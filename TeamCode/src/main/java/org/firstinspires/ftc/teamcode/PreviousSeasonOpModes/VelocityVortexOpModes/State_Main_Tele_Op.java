package org.firstinspires.ftc.teamcode.PreviousSeasonOpModes.VelocityVortexOpModes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class State_Main_Tele_Op extends OpMode {

    StateHardwareMap robot = new StateHardwareMap();
    //Setting up global variables
    float initialDrivespeed = 1f;
    float driveSpeed = initialDrivespeed;
    float intakeSpeed = 1f;
    float threshold = - 0.05f;
    boolean forward = true;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        //Running all of the functions
        runWheels();
        reverse();
        runIntake();
        shoot();
        pressBeacon();
        telemetry();
    }

    //Drives right drive motors based on right joystick and left drive motors based on left joystick
    public void runWheels() {
        if (driveSpeed > 0) {
            //Threshold ensures that the motors wont move when joystick is released even if the joysticks don't reset exactly to 0
            if (Math.abs(gamepad1.left_stick_y) > threshold) {
                robot.fleft.setPower(-driveSpeed * gamepad1.left_stick_y);
                robot.bleft.setPower(-driveSpeed * gamepad1.left_stick_y);
            } else {
                robot.fleft.setPower(0);
                robot.bleft.setPower(0);
            }

            if (Math.abs(gamepad1.right_stick_y) > threshold) {
                robot.fright.setPower(-driveSpeed * gamepad1.right_stick_y);
                robot.bright.setPower(-driveSpeed * gamepad1.right_stick_y);
            } else {
                robot.fright.setPower(0);
                robot.bright.setPower(0);
            }
        }
        else{
            //Threshold ensures that the motors wont move when joystick is released even if the joysticks don't reset exactly to 0
            if (Math.abs(gamepad1.right_stick_y) > threshold) {
                robot.fleft.setPower(-driveSpeed * gamepad1.right_stick_y);
                robot.bleft.setPower(-driveSpeed * gamepad1.right_stick_y);
            } else {
                robot.fleft.setPower(0);
                robot.bleft.setPower(0);
            }
            if (Math.abs(gamepad1.left_stick_y) > threshold) {
                robot.fright.setPower(-driveSpeed * gamepad1.left_stick_y);
                robot.bright.setPower(-driveSpeed * gamepad1.left_stick_y);
            } else {
                robot.fright.setPower(0);
                robot.bright.setPower(0);
            }
        }
    }

    //If joystick is pushed forward, the intake motor will push balls out, if it is pushed backwards, the intake motor will pull balls in
    public void runIntake() {
        if(Math.abs(gamepad2.left_stick_y) > threshold)
            robot.intake.setPower(- intakeSpeed * gamepad2.left_stick_y);
        else
            robot.intake.setPower(0);
    }

    public void shoot() {
        if(gamepad2.a)
            robot.flicker.setPower(.8);
        else if(gamepad2.x)
            robot.flicker.setPower(-.25);
        else
            robot.flicker.setPower(0);
    }

    public void pressBeacon() {
        if(gamepad2.right_trigger >= .8)
            robot.rightBeacon.setPosition(0);
        else if(gamepad2.right_bumper)
            robot.rightBeacon.setPosition(1);
        else
            robot.rightBeacon.setPosition(0.5);

        if(gamepad2.left_trigger >= .8)
            robot.leftBeacon.setPosition(1);
        else if(gamepad2.left_bumper)
            robot.leftBeacon.setPosition(0);
        else
            robot.leftBeacon.setPosition(0.5);
    }

    public void reverse(){
        //Makes the controller drive the robot with the intake as the front if the start button is pressed
        if(gamepad1.dpad_up && !forward){
            driveSpeed = Math.abs(driveSpeed);
            forward = true;
        }

        //Makes the controller drive the robot with the intake as the back if the back button is pressed
        if(gamepad1.dpad_down && forward){
            driveSpeed = - Math.abs(driveSpeed);
            forward = false;
        }
        if(gamepad1.right_trigger > 0.8)
            driveSpeed = initialDrivespeed * .15f * (Math.abs(driveSpeed) / driveSpeed);
        if(gamepad1.right_trigger < 0.8 && Math.abs(driveSpeed) < initialDrivespeed)
            driveSpeed = initialDrivespeed * (Math.abs(driveSpeed) / driveSpeed);
    }

    public void telemetry() {
        //Using telemetry to send joystick data to the phones for debugging purposes
        telemetry.addData("1 - Left Joystick:" , gamepad1.left_stick_y);
        telemetry.addData("1 - Right Joystick:" , gamepad1.right_stick_y);
        telemetry.addData("2 - Right Joystick:", gamepad2.right_stick_y);
        telemetry.addData("2 - Left Trigger:",gamepad2.left_trigger);
        telemetry.addData("2 - Left Bumper:",gamepad2.left_bumper);
        telemetry.addData("2 - Right Trigger:",gamepad2.right_trigger);
        telemetry.addData("2 - Right Bumper:",gamepad2.right_bumper);
        telemetry.addData("2 - A button:" ,gamepad2.a);
        telemetry.addData("2 - X button:" ,gamepad2.x);
        telemetry.addData("Speed:" , driveSpeed);
        telemetry.addData("Intake Speed:" , intakeSpeed);
        telemetry.addData("Blueness:" , robot.color.blue());
        telemetry.addData("Redness:" , robot.color.red());
        telemetry.addData("Forward:" , forward);
    }
}
