package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;

/**
 * Created by Howard on 10/15/16.
 */
public class HangTask extends TaskThread {
    private HangSlides slides;
    private Telemetry.Item slideStatus;

    public HangTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();

        double power = 1.0;
        String slideString;

        boolean triggerUp,triggerDown;
        while (opMode.opModeIsActive() && this.running) {

            //TRIGGER FUNCTIONS
            triggerUp = opMode.gamepad2.dpad_up ||opMode.gamepad1.dpad_up;
            triggerDown = opMode.gamepad2.dpad_down || opMode.gamepad1.dpad_down;

            if (triggerUp) {
                slides.setPower(power);
                slideString = "UP";
            } else if (triggerDown) {
                slides.setPower(-1 * power);
                slideString = "DOWN";
            } else {
                slides.setPower(0);
                slideString = "IDLE";
            }

            slideStatus.setValue(slideString);
        }
    }

    @Override
    public void initialize() {
        this.slides = new HangSlides(this.opMode);
        slideStatus = opMode.telemetry.addData("Slide", "INITIALIZED");
    }
}


