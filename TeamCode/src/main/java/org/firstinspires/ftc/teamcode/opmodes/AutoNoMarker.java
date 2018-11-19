package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Catapault;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Dumper;
import org.firstinspires.ftc.teamcode.robotutil.GoldPosition;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Logger;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto No Marker", group="FinalShit")


public class AutoNoMarker extends LinearOpMode {

    private HangSlides hangSlides;
    private Logger l = new Logger("AUTO GOLD");

    @Override
    public void runOpMode() throws InterruptedException {
        l.log("starting opmode");
        initialize();

        //
        telemetry.addLine("press button to raise robot");
        telemetry.update();
        while(!this.gamepad1.a && !isStopRequested()){
            sleep(100);
        }

        hangSlides.moveSlides(Direction.DOWN,.5,5.15,5);

        waitForStart();
        if (opModeIsActive()) {

            l.log("moving down");
            hangSlides.moveSlides(Direction.UP,.5,5.15,5);


        }
    }


    private void waitForButton(String message){
        telemetry.addLine(message);
        telemetry.update();
        while(!this.gamepad1.a && opModeIsActive() && !isStopRequested()){
            sleep(100);
        }
    }

    private void initialize() {
        hangSlides = new HangSlides(this);
    }



}
