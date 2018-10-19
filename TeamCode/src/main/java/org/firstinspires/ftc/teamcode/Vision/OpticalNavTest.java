package org.firstinspires.ftc.teamcode.Vision;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.Drivers.OpticalNavDriver;


@TeleOp(name="Optical Testing", group="Optical")

public class OpticalNavTest extends LinearOpMode {
    OpticalNavDriver opticalnav;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Quinn's Sketch Code - Optical Nav Test");

        //preliminary things
        opticalnav = hardwareMap.get(OpticalNavDriver.class, "opticalNav");
        telemetry.addData("Manufacturer" , opticalnav.getManufacturerIDRaw());


        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Position" , opticalnav.getDeltaXY());
        }

        //end code
    }
}
