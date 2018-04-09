/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
3/28/18                                   Rohan                     Removed a temporary commenting out on slideIncrement(). There is a fail safe preventing the robot from crashing if the lift goes to far up so this is unnecessary
3/28/18                                   Pahel                     Changed strafing from joysticks to the dpad and resolved strafing issue.
3/29/18                                   Rohan                     Added a new object called constants and started replacing constant values in the program with the object.
3/30/18                                   Rohan                     All numerical values are replaced by an object value. All values defined in Constants_For_TeleOp_Rolly.
4/1/18                                    Rohan                     Added in the name of the program that this is a competition program.

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp.In_Use;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants.Constants_For_TeleOp_Rolly;


/**
 * Created by Team Inspiration on 1/21/18.
 */
@TeleOp(name = "1 George Roll-y RUN FOR COMPETITION")
public class curious_georgeROLLI_teleop extends OpMode{
Constants_For_TeleOp_Rolly constants = new Constants_For_TeleOp_Rolly();

    /*Delta_TeleOp is designed for and tested with the Tile Runner robot. If this program is used with another robot it may not worked.
* This is specificly made for the Tile Runner and not another pushbot or competiotion robot. However, this program is the basic design for
* simple program and could work on a different robot with simple debugging and configuration.*/

    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here
    */
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    Servo glyphLeft;
    Servo glyphRight;
    DcMotor intakeLeft;
    DcMotor intakeRight;
    DcMotor relicMotor;
    Servo relicMain;
    Servo relicClaw;
    DcMotor slideMotor;
    public int IVFSM;

    ElapsedTime runtime = new ElapsedTime();



    @Override
    public void init() {
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        intakeLeft = hardwareMap.dcMotor.get("glyphIntakeLeft");
        intakeRight = hardwareMap.dcMotor.get("glyphIntakeRight");

        glyphRight = hardwareMap.servo.get("glyphServoRight");
        glyphLeft = hardwareMap.servo.get("glyphServoLeft");

        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        IVFSM = slideMotor.getCurrentPosition();
        relicMain = hardwareMap.servo.get("relicMain");
        relicClaw = hardwareMap.servo.get("relicClaw");
        relicMotor = hardwareMap.dcMotor.get("relicMotor");

        leftWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
//changed values (glyphRight:1)(glyphLeft:0)
        glyphRight.setPosition(0.245);
        glyphLeft.setPosition(0.66);
        relicMain.setPosition(constants.relicMainDownPosition);

    }

    @Override
    public void loop() {
        glyphIntake();
        glyphManipulate();
        Drive();
        Slides();
        Relic();
    }

    public void glyphManipulate() {
        if (gamepad1.right_trigger != 0) {
            glyphLeft.setPosition(glyphLeft.getPosition() + constants.glyphIncrementValue);
            glyphRight.setPosition(glyphRight.getPosition() - constants.glyphIncrementValue);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
        else if (gamepad1.left_trigger != 0) {
            glyphLeft.setPosition(glyphLeft.getPosition() - constants.glyphIncrementValue);
            glyphRight.setPosition(glyphRight.getPosition() + constants.glyphIncrementValue);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
        else if (gamepad1.y) {
            glyphLeft.setPosition(0.53);
            glyphRight.setPosition(0.42);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
        else if (gamepad1.b) {
            glyphLeft.setPosition(0.2);
            glyphRight.setPosition(0.8);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
        else if (gamepad1.a) {
            glyphLeft.setPosition(0.75);
            glyphRight.setPosition(0.17);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
        else if (gamepad1.x) {
            glyphLeft.setPosition(0.66);
            glyphRight.setPosition(0.245);
            telemetry.addData("Left Servo Pos", glyphLeft.getPosition());
            telemetry.addData("Right Servo Pos", glyphRight.getPosition());
            telemetry.update();
            sleep(200);
        }
    }

    public void glyphIntake() {
        if (gamepad1.right_bumper) {
            intakeLeft.setPower(constants.glyphMotorPower);
            intakeRight.setPower(-constants.glyphMotorPower);
        }
        else if (gamepad1.left_bumper) {
            intakeLeft.setPower(-constants.glyphMotorPower);
            intakeRight.setPower(constants.glyphMotorPower);
        }
        else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }


    public void FourWheelDrive() {
        /*

        read the gamepad values and put into variables
         */
        double threshold = constants.fourWheelDriveThreshhold;
        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (-gamepad1.right_stick_y);
        telemetry.addData("right power input", rightY_gp1);
        telemetry.addData("left power input", leftY_gp1);
//changing from  else if (gamepad1.right_stick_x>0&&gamepad1.left_stick_x>0)  to  else if (gamepad1.dpad_left)    3/28/18
// if (gamepad1.left_stick_x<0&&gamepad1.right_stick_x<0)

    if (gamepad1.dpad_left) {

        setPowerAll(-constants.leftTopStrafeSpeed, constants.leftBottomStrafeSpeed, constants.rightTopStrafeSpeed, -constants.rightBottomStrafeSpeed);

//changing from  else if (gamepad1.left_stick_x>0&&gamepad1.right_stick_x>0)  to  else if (gamepad1.dpad_right)  3/28/18
//else if (gamepad1.left_stick_x>0&&gamepad1.right_stick_x>0)

    }
else if (gamepad1.dpad_right){
        setPowerAll(constants.leftTopStrafeSpeed,-constants.leftBottomStrafeSpeed,-constants.rightTopStrafeSpeed,constants.rightBottomStrafeSpeed);
    }

   // setPowerAll(1,-1,-1,1);

else if (gamepad1.dpad_down){
    
    setPowerAll(-constants.slowMovementMotorPower,-constants.slowMovementMotorPower,-constants.slowMovementMotorPower,-constants.slowMovementMotorPower);
}
else if (gamepad1.dpad_up){
    setPowerAll(constants.slowMovementMotorPower,constants.slowMovementMotorPower,constants.slowMovementMotorPower,constants.slowMovementMotorPower);
}
//if
        else if (Math.abs(gamepad1.left_stick_y) > threshold || Math.abs(gamepad1.right_stick_y) > threshold){
           setPowerAll(leftY_gp1,leftY_gp1,rightY_gp1,rightY_gp1);
        }
        else{
            leftWheelMotorBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftWheelMotorFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightWheelMotorBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightWheelMotorFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftWheelMotorFront.setPower(0);
            leftWheelMotorBack.setPower(0);
            rightWheelMotorFront.setPower(0);
            rightWheelMotorBack.setPower(0);
        }
    }

    public void setPowerAll(double leftTop, double leftBottom, double rightTop, double rightBottom){
        leftWheelMotorFront.setPower(leftTop);
        leftWheelMotorBack.setPower(leftBottom);
        rightWheelMotorFront.setPower(rightTop);
        rightWheelMotorBack.setPower(rightBottom);
    }

    public void sleep(long ms) {
        long startTime = System.currentTimeMillis();

        while (startTime + ms > System.currentTimeMillis()) {

        }
    }

    public void Slides(){
        slideMove();
       slideIncrement();
        telemetry.addData("Slide Motor Value is ", slideMotor.getCurrentPosition());
        telemetry.update();
    }
    public void Drive(){
        FourWheelDrive();
    }
    public void Relic() {
        relicManipulator();
    }
    public void Glyph() {
        glyphIntake();

    }

    public void slideMove() {

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IVFSM = slideMotor.getCurrentPosition();

        if (gamepad2.right_stick_y != 0) {
            slideMotor.setPower(-gamepad2.right_stick_y);

        } else {
            slideMotor.setPower(0);
        }
    }

    public void slideIncrement() {

        if (gamepad2.dpad_up) {
            moveUpInch(constants.dpad_upIncrementValue);
        } else if (gamepad2.dpad_right) {
            moveUpInch(constants.d_padRightIncrementValue);
        } else if (gamepad2.dpad_down){
            moveUpInch(constants.d_padDownIncrementValue);
        }
    }

    public void relicManipulator() {
        if (gamepad2.a) {
            relicMain.setPosition(constants.relicMainDownPosition);
        }
        else if (gamepad2.x) {
            relicMain.setPosition(constants.relicMainMiddlePosition);
            }
        else if (gamepad2.y) {
            relicMain.setPosition(constants.relicMainUpPosition);
        }
        else if (gamepad2.left_bumper) {
                relicClaw.setPosition(constants.relicOpenClaw);
        } else if (gamepad2.right_bumper) {
                relicClaw.setPosition(constants.relicCloseClaw);
        } else if (gamepad2.left_bumper && gamepad2.right_bumper) {
                relicClaw.setPosition(constants.relicMiddleClaw);
        } else {
                relicMotor.setPower(-(gamepad2.left_stick_y));
            }
    }

    public void moveUpInch(double cm) {
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double target_Position;
        double countsPerCM = constants.slideCPCM;
        double finalTarget = cm * countsPerCM;
        target_Position = slideMotor.getCurrentPosition() - finalTarget;

        slideMotor.setTargetPosition((int) target_Position);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideMotor.setPower(-constants.slideMotorPowerIncrementing);
        double begintime = runtime.seconds();
        while (slideMotor.isBusy() && (runtime.seconds() - begintime) < constants.slideIncrementFailSafeSec) {
            telemetry.addData("In while loop in moveUpInch", slideMotor.getTargetPosition());
            telemetry.update();

        }

        slideMotor.setPower(0);

    }

}