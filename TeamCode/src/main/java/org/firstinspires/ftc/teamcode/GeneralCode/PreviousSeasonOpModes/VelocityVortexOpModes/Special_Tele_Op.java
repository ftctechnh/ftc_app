package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

//Necessary imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
//Adding this program to the phones
@TeleOp(name = "Special TeleOp", group = "TeleOp")

public class Special_Tele_Op extends OpMode {
    //Setting up robot class
    SupersHardwareMap robot = new SupersHardwareMap(true);

    //Setting up global variables
    double driveSpeed = robot.TELEOP_DRIVE_SPEED;
    //Increase threshold value if joysticks aren't resetting to exactly 0
    double threshold = 0.05f;

    //Setting up hardware maps, reversing motors, etc
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    //Runs all of the functions
    @Override
    public void loop() {
        runWheels();
        flickers();
        runIntake();
        rock();
        Telemetry();
    }

    //Drives right drive motors based on right joystick and left drive motors based on left joystick
    public void runWheels() {
        //Lowers speed when left trigger is held down
        driveSpeed = robot.TELEOP_DRIVE_SPEED - 0.8 * Math.abs(gamepad1.left_trigger);

        //Threshold ensures that the motors wont move when joystick is released even if the joysticks don't reset exactly to 0
            if(gamepad2.left_stick_y != 0) {
                robot.notreversed = true;
                robot.ldrive(-gamepad2.left_stick_y * robot.TELEOP_DRIVE_SPEED);
            }
            else if(gamepad1.right_stick_y != 0){
                robot.notreversed = false;
                robot.rdrive(-gamepad1.right_stick_y * driveSpeed);
            }
            else {
                if(!gamepad1.right_bumper && !gamepad1.left_bumper) {
                robot.fleft.setPower(0);
                robot.bleft.setPower(0);
                }
            }

            if (gamepad2.right_stick_y != 0) {
                robot.notreversed = true;
                robot.rdrive(-gamepad2.right_stick_y * robot.TELEOP_DRIVE_SPEED);
            } else if (gamepad1.left_stick_y != 0) {
                robot.notreversed = false;
                robot.ldrive(-gamepad1.left_stick_y * driveSpeed);
            } else {
                if(!gamepad1.right_bumper && !gamepad1.left_bumper) {
                    robot.fright.setPower(0);
                    robot.bright.setPower(0);
                }
            }
    }


    //Shoots when right trigger is pressed
    public void flickers(){
        if(gamepad1.right_trigger > 0.8)
            moveFlicker(1, 1);
        else
            robot.flicker.setPower(0);
    }

    //When joystick is pushed up, the intake motor pushes balls out, when it is pushed down, the intake motor pulls balls in
    public void runIntake() {
        if(Math.abs(gamepad2.right_trigger) > 0.1)
            robot.intake.setPower(gamepad2.right_trigger * robot.INTAKE_SPEED);
        else if(Math.abs(gamepad2.left_trigger) > 0.1)
            robot.intake.setPower(gamepad2.left_trigger * -robot.INTAKE_SPEED);
        else
            robot.intake.setPower(0);
    }

    public void rock() {
        if(gamepad1.right_bumper) {
            robot.fleft.setPower(-0.5);
            robot.bleft.setPower(-0.5);
            robot.fright.setPower(0.25);
            robot.bright.setPower(0.25);
        }
        else if(gamepad1.left_bumper) {
            robot.fleft.setPower(0.25);
            robot.bleft.setPower(0.25);
            robot.fright.setPower(-0.5);
            robot.bright.setPower(-0.5);
        }
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
        while(Math.abs(robot.flicker.getTargetPosition() - robot.flicker.getCurrentPosition()) > 120) {
            telemetry.addData("Flicker target:", robot.flicker.getTargetPosition());
            telemetry.addData("Flicker current:", robot.flicker.getCurrentPosition());
            telemetry.update();
        }

        //Stops the flicker after the target position is reached
        robot.flicker.setPower(0);
    }

    //Tells how to control robot for drivers, debugging info can be added here
    public void Telemetry(){
        telemetry.addData("Instructions:","(C = controller)");
        telemetry.addData("Drive (Reversed):", "C1, L/R joysticks");
        telemetry.addData("Lower C1 Drive Speed:", "C1, L trigger");
        telemetry.addData("Shoot:", "C1, R trigger");
        telemetry.addData("Beacon Swivel:", "C1, L/R bumpers");
        telemetry.addData("Drive (Normal):", "C2, L/R joysticks");
        telemetry.addData("Run Intake:", "C2, L/R triggers");
    }
}