package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.CrystalArm9330;
import org.firstinspires.ftc.teamcode.subsystems.Clamps9330;
import org.firstinspires.ftc.teamcode.subsystems.GlyphLift9330;
import org.firstinspires.ftc.teamcode.subsystems.RelicPickup9330;

/**
 * Created by robot on 9/25/2017.
 */

@TeleOp(name="TeleOp9330", group="Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class TeleOp9330 extends OpMode {
    Hardware9330 robotMap = new Hardware9330();
    Clamps9330 clamps;
    CrystalArm9330 crystalArm;
    GlyphLift9330 glyphLift9330;
    RelicPickup9330 relicPickup;


    float yPower = 0;
    float spinPower = 0;
    float liftSpeed = 50;
    boolean aBtnHeld = false;
    boolean bBtnHeld = false;
    boolean xBtnHeld = false;
    boolean lBumpHeld = false;
    boolean rBumpHeld = false;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robotMap.init(hardwareMap);
        clamps = new Clamps9330(robotMap);
        crystalArm = new CrystalArm9330(robotMap);
        glyphLift9330 = new GlyphLift9330(robotMap);
        relicPickup = new RelicPickup9330(robotMap);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        yPower = gamepad1.left_stick_y;
        spinPower = gamepad1.right_stick_x;

        //Set powers of the motors
        Hardware9330.leftMotor.setPower(-yPower - spinPower);
        Hardware9330.rightMotor.setPower(yPower - spinPower);

        //If "A" is pressed on the gamepad toggle the lower clamp
        if(gamepad2.a) {
            if (aBtnHeld == true) return;
            telemetry.addData("Program", "Low Clamp toggled!");
            clamps.toggleLowClamp();
            aBtnHeld = true;
        } else {
            aBtnHeld = false;
        }

        //If "B" is pressed on the gamepad toggle the high clamp
        if(gamepad2.b) {
            if (bBtnHeld == true) return;
            telemetry.addData("Program", "High Clamp toggled!");
            clamps.toggleHighClamp();
            bBtnHeld = true;
        } else {
            bBtnHeld = false;
        }

        //If up on dpad is pressed, move motor up
        //If down on dpad is pressed, move motor down
        //Otherwise motor is not moving
        if(gamepad2.dpad_up) {
            telemetry.addData("Program", "Lift rising!");
            glyphLift9330.liftUp();
        } else if (gamepad2.dpad_down) {
            telemetry.addData("Program", "Lift falling!");
            glyphLift9330.liftDown();
        } else {
            glyphLift9330.liftStop();
        }

        //If "X" is pressed on the gamepad toggle the crystal arm servo
        if(gamepad2.x){
            if(xBtnHeld == true) return;
            telemetry.addData("Program", "Arm toggled!");
            crystalArm.toggleArmServo();
            xBtnHeld = true;
        } else xBtnHeld = false;

        //If left is pressed on the dpad toggle the hand servo
        if(gamepad2.left_bumper){
            if(lBumpHeld == true) return;
            relicPickup.toggleHand();
            telemetry.addData("Program", "Hand toggled!!!!!!!!");
            lBumpHeld = true;
        }else lBumpHeld = false;

        //If right is pressed on the dpad toggle the wrist servo
        if (gamepad2.right_bumper){
            if(rBumpHeld == true) return;
            relicPickup.toggleWrist();
            telemetry.addData("Program", "Wrist toggled!!! WOW!");
            rBumpHeld = true;
        }else rBumpHeld = false;

        telemetry.update();
}

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        Hardware9330.leftMotor.setPower(0);
        Hardware9330.rightMotor.setPower(0);
    }
}