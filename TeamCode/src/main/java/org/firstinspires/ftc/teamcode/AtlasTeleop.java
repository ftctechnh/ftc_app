package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="AtlasTeleop", group="Opmode")

public class AtlasTeleop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        //Inset config file
        AtlasBase base = new AtlasBase(hardwareMap);
        //Declare controller buttons


        waitForStart();

        while (opModeIsActive()) {
            //Update controller inputs



        }
    }
}