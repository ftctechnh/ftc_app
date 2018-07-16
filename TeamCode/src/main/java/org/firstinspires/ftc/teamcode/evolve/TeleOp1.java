package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//This is a comment

/*
    Welcome to Evolve!

    This is called a comment, and it allows us to write anything in the code.
    Make a comment using // or /*   */
    /*

    Let's start by setting up our program

    Step 1 - remove the slashes before "@TeleOp"'

    Step 2 - extends LinearOpMode

    Step 3 - add the hardware
        A. - say what you're adding (use DcMotor and TouchSensor)

        B. - name the part (like test)
            example: DcMotor test;'

    Step 4 - public void runOpMode() throws InterruptedException {}

    Step 5 - hardwareMap
        A. - name =

        B. - hardwareMap.part.get("name");
            example: test = hardwareMap.dcMotor.get("test");'

    Step 6 - remove the slashes before "right.setDirection"'

    Step 7 - type "waitForStart();"'

    Step 8 - while(opModeIsActive()){

    Step 9 - controls
        A. - left.setPower(gamepad1.right_stick_y);

        B. - right.setPower(gamepad1.left_stick_y);

    Step 10 - remove slashes before "telemetry"'
     */

//@TeleOp (name = "teleOp1", group = "evolve") //1. Remove the slashes before "@TeleOp"' to start
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

        //right.setDirection(DcMotorSimple.Direction.REVERSE); //6. Remove the slashes

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
