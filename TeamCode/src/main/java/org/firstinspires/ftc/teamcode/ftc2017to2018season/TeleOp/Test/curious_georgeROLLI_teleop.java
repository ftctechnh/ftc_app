/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
3/28/18                                   Rohan                     Removed a temporary commenting out on slideIncrement(). There is a fail safe preventing the robot from crashing if the lift goes to far up so this is unnecessary
3/28/18                                   Pahel                     Changed strafing from joysticks to the dpad and resolved strafing issue.
3/29/18                                   Rohan                     Added a new object called constants and started replacing constant values in the program with the object.
3/30/18                                   Rohan                     All numerical values are replaced by an object value. All values defined in Constants_For_TeleOp_Rolly.

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp.Test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants.Constants_For_TeleOp_Rolly;


/**
 * Created by Team Inspiration on 1/21/18.
 */
@TeleOp(name = "TestRelic GeorgeRolly")
public class curious_georgeROLLI_teleop extends OpMode{
Constants_For_TeleOp_Rolly constants = new Constants_For_TeleOp_Rolly();

    /*Delta_TeleOp is designed for and tested with the Tile Runner robot. If this program is used with another robot it may not worked.
* This is specificly made for the Tile Runner and not another pushbot or competiotion robot. However, this program is the basic design for
* simple program and could work on a different robot with simple debugging and configuration.*/

    /*
        ---------------------------------------------------------------------------------------------

       Define the actuators we use in the robot here */
    DcMotor relicMotor;
    Servo relicMain;
    Servo relicClaw;


    ElapsedTime runtime = new ElapsedTime();



    @Override
    public void init() {

        relicMain = hardwareMap.servo.get("relicMain");
        relicClaw = hardwareMap.servo.get("relicClaw");
        relicMotor = hardwareMap.dcMotor.get("relicMotor");


    }

    @Override
    public void loop() {
        Relic();
    }

    public void Relic() {
        relicManipulator();
    }



    public void relicManipulator() {
        if (gamepad2.a){
            relicMain.setPosition(constants.relicMainDownPosition);
        }
        else if (gamepad2.x){
            relicMain.setPosition(constants.relicMainMiddlePosition);
        }
        else if (gamepad2.y){
            relicMain.setPosition(constants.relicMainUpPosition);
        }
        else if (gamepad2.left_bumper){
            relicClaw.setPosition(constants.relicOpenClaw);
        }
        else if (gamepad2.right_bumper){
            relicClaw.setPosition(constants.relicCloseClaw);
        }
        else if (gamepad2.left_bumper&&gamepad2.right_bumper){
            relicClaw.setPosition(constants.relicMiddleClaw);
        }
        else{
            relicMotor.setPower(-(gamepad2.left_stick_y));
        }
    }


}