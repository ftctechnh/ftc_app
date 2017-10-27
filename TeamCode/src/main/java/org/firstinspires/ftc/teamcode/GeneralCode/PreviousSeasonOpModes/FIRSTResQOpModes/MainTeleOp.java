package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.FIRSTResQOpModes;

//Importing various useful classes
    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.Servo;

public class MainTeleOp extends OpMode {
    //Declaring motor variables
    DcMotor left;
    DcMotor right;
    DcMotor arm;
    Servo servoTape;
    Servo servoTape2;

    //Setting up global variables
    float speed = - .4f;
    float servoSpeed = .02f;
    float position = .5f;
    float armspeed = 0.5f;
    boolean servoMove = false;

    @Override
    public void init() {
        //Setting up the motors
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        arm = hardwareMap.dcMotor.get("arm");

        //Reversing right motor so that they both go the same way
        left.setDirection(DcMotor.Direction.REVERSE);

        //Setting up servos
        servoTape = hardwareMap.servo.get("servoTape");
        servoTape2 = hardwareMap.servo.get("servoTape2");

        //Reversing one servo so that they go the same way
        servoTape2.setDirection(Servo.Direction.REVERSE);

        //Moving the servos to their starting position
        servoTape.setPosition(position);
        servoTape2.setPosition(position);
    }

    @Override
    public void loop() {
        //Calling the functions defined below
        MoveWheels();
        MoveServo();
        MoveArm();
        Telemetry();
    }

    public void MoveWheels() {
        //Resetting the power so that the robot stops if the button isn't pressed
        float leftPower = 0;
        float rightPower = 0;

        //If x is pressed, both wheels go forward at the same slow pace
        if(gamepad1.x) {
            leftPower = 0.4f * speed;
            rightPower = 0.4f * speed;
        }

        //Collecting joystick data
        //Note: Joysticks yield negative values in forward positions, so the values must be reversed
        leftPower = - speed * ((gamepad1.left_stick_y * gamepad1.left_stick_y) * (Math.abs(gamepad1.left_stick_y) / gamepad1.left_stick_y));
        rightPower = - speed * ((gamepad1.right_stick_y * gamepad1.right_stick_y) * (Math.abs(gamepad1.right_stick_y) / gamepad1.right_stick_y));

        //Moving the motors according to input
        left.setPower(leftPower);
        right.setPower(rightPower);
    }


    public void MoveServo() {
        //Clipping the input values
        if (position > 1)
            position = 1f;

        else if (position < 0)
            position = 0f;

        //Moving servos according to input
        servoTape.setPosition(position);
        servoTape2.setPosition(position);

        //Collecting input from the gamepad; y makes it go up and a makes it go down
        if (gamepad1.y && !servoMove) {
            position = position + servoSpeed;
            servoMove = true;
        }
        else if (gamepad1.a && !servoMove) {
            position = position - servoSpeed;
            servoMove = true;
        }
        else
            servoMove = false;
    }

    public void MoveArm() {
        //Collecting input from the gamepad and setting the arm motor accordingly; right trigger makes it go out and left trigger makes it go in
        if (gamepad1.left_trigger > 0)
            arm.setPower(- armspeed * gamepad1.left_trigger);

        else if (gamepad1.right_trigger > 0)
            arm.setPower(armspeed * gamepad1.right_trigger);

        else
            arm.setPower(0);
    }

    public void Telemetry() {
        //Using telemetry to send debugging data to the phones
        telemetry.addData("Servo Value", position);
    }
}
