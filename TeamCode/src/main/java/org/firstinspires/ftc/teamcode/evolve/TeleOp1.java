package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//This is a comment/

/*
    Welcome to Evolve!

    This is called a comment, and it allows us to write anything in the code.
    Make a comment using // or /*   */
    /*

    Let's start by setting up our program

    Step 1 - name the program
        type @TeleOp (name="TeleOp1",group="evolve"

    Step 2 -  Identifies the type of program as linear
        already done:  extends LinearOpMode


    Step 3 - add the hardware
        A. - say what you're adding (use DcMotor and TouchSensor)

        B. - name the part (like test)
            example: DcMotor test;'

    Step 4 - mark the start of the program
        type public void runOpMode() throws InterruptedException

    Step 5 - match your names to the real hardware
        A. - name =

        B. - hardwareMap.part.get("name");
            example: test = hardwareMap.dcMotor.get("test");'

    Step 6 - reverse one of the motor
        type "right.setDirection(DcMotorSimple.Direction.REVERSE);"'

    Step 7 - mark where the program runs
        type "waitForStart();"'

    Step 8 - make sure the program is active
        while(opModeIsActive()){

    Step 9 - use the controls
        A. - left.setPower(gamepad1.right_stick_y);

        B. - right.setPower(gamepad1.left_stick_y);

    Step 10 - add debugging output by uncommenting
        remove slashes before "telemetry"'
     */

//1. Type here
public class TeleOp1 extends LinearOpMode{ //2. /

    //3. example: DcMotor test;'
    //right
    //left
    //wall

    public void runOpMode() throws InterruptedException {//4. /

        //5. example: test = hardwareMap.dcMotor.get("test");'
        //right
        //left
        //wall

        //6. Type here

        //7. type here

        while (opModeIsActive()){//8. /

            //9. example: test.setPower(gamepad1.right_stick_y);'
            //left
            //right

            //telemetry.addData("Wall: ", wall.isPressed()); //10. /
            //telemetry.update(); //10. /

        }//8. /

    }//4. /
}
