package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.HangSlides;
import org.firstinspires.ftc.teamcode.robotutil.Sweeper;

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


        while (opMode.opModeIsActive() && this.running) {
            if (opMode.gamepad2.dpad_up) {
                slides.setPower(power);
                slideString = "UP";
            } else if (opMode.gamepad2.dpad_down) {
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
        DcMotor hangTop = opMode.hardwareMap.dcMotor.get("hangTop");
        DcMotor hangBottom = opMode.hardwareMap.dcMotor.get("hangBottom");

        this.slides = new HangSlides(this.opMode);
        slideStatus = opMode.telemetry.addData("Slide", "INITIALIZED");
    }
}


