package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

/**
 * Created by Steven on 1/6/2018.
 */

public class TestServoMethods extends Autonomous_General{

    @Override
    public void runOpMode(){
        openGlyphManipulator();
        jewelServo.setPosition(0);

        waitForStart();
        closeGlyphManipulator();
        jewelServo.setPosition(1);
    }

}
