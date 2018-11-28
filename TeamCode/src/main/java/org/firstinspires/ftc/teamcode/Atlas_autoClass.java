package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Autonomous", group="Opmode")

public class Atlas_autoClass extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        AtlasClass base = new AtlasClass(hardwareMap);



        waitForStart();

        
    }
}