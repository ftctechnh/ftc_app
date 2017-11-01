package org.firstinspires.ftc.teamcode.ftc2016to2017season;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/5/17.
 */

@Autonomous(name="AutonomousStraightOp")
@Disabled
public class AutonomousFarStraight extends AutonomousGeneral {

    //blue: tangent to blue line
    //red: tangent to red line

    private ElapsedTime runtime = new ElapsedTime();

    static  int             INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode(){

        initiate();

        INITIAL_SHOOTERPOS = shooting_motor.getCurrentPosition();

        waitForStart();

        encoderDrive(0.5, 165, 165, 30);


        telemetry.addData("it is done!", "");
        telemetry.update();
    }
}
