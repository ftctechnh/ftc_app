package org.firstinspires.ftc.teamcode.opmodes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "EducayshunOpMode", group = "Bot")
public class EducayshunOpMode extends BaseAutonomousOpMode {

    public EducayshunOpMode() {
        super("EducayshunOpMode");
    }

    @Override
    public void runOpMode()
    {

        telem("About to init systems");
        this.initSystems();

        ////
        waitForStart();
        ////

        driveSystem.tankDrive(0.6, -0.6);
        sleep(1000);

        telem("About to driveXY(0,1)");
        driveSystem.mecanumDriveXY(0,1);
        sleep(2000);

        telem("Just driveXYd(0,1), about to driveXY(1,0)");
        driveSystem.mecanumDriveXY(1, 0);
        sleep(2000);

        telem("About to turn 90 deg at power 1");
        driveSystem.turn(90, 1);

        sleep(2000);
        telem("About to drive to position inches 2000 ticks at power 1");
        driveSystem.driveToPositionInches(2000, 1);

        //telem("just drove 2000, about to find with eye for 10 sec");
        //eye.find(10);

        telem("done finding for 10 seconds");

        stop();
    }

    private void telem(String message) {
        telemetry.addLine(message);
        telemetry.update();
        sleep(1000);
    }
}
