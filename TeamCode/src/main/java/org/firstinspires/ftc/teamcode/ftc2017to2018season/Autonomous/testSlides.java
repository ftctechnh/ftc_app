package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Inspiration Team on 2/10/2018.
 */
@TeleOp(name = "testSlides")
public class testSlides extends Autonomous_General_George {

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
