package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

//10-28-17
@Autonomous(name="Autonomois Slide Test")
@Disabled
public class autoSlideUpDownTest extends Autonomous_General_George {


    @Override
    public void runOpMode() {

        initiate(false);

        waitForStart();

        //moveUpGlyph(0.5);
        //
        // sleep(1000);
        moveDownGlyph(0.5);

    }
}