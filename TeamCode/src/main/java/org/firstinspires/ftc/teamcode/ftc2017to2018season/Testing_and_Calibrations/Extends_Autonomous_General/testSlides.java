package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Extends_Autonomous_General;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George_;

/**
 * Created by Inspiration Team on 2/10/2018.
 */
@TeleOp(name = "testSlides")
@Disabled
public class testSlides extends Autonomous_General_George_ {

    public void runOpMode(){

        initiate(false);
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.x){
                moveUpGlyph(2);
            }
            else if(gamepad1.y){
                moveDownGlyph(2);
            }
        }
    }
}
