package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Catapault;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Dumper;
import org.firstinspires.ftc.teamcode.robotutil.GoldPosition;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Hook;
import org.firstinspires.ftc.teamcode.robotutil.Logger;
import org.firstinspires.ftc.teamcode.robotutil.Options;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto No DT", group="FinalShit")


public class AutoNoDT extends LinearOpMode {

    private HangSlides hangSlides;
    private Catapault straightCatapault;
    private Hook hook;
    private Catapault horizontalCatapult;
    private Logger l = new Logger("AUTO GOLD");

    private boolean depotSide;

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

        hook.attach();

        hangSlides.moveSlides(Direction.DOWN,.5,4.45,5);

        waitForStart();
        if (opModeIsActive()) {

            l.log("moving down");
            hangSlides.moveSlides(Direction.UP,.5,4.45,5);
            l.log("flicking down");
            hook.retract();



            straight`atapault.flick(0.5,500);
            Utils.waitFor(1000);
            straightCatapault.flick(-.3,800);


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
        straightCatapault = new Catapault(this,"straightCatapult");
        horizontalCatapult = new Catapault(this, "horizontalCatapult");
        hook = new Hook(this);
        depotSide = true;
    }



}
