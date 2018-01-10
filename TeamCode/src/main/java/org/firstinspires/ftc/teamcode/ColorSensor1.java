
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ColorSensor1 extends LinearOpMode {

    ColorSensor s;
    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        s = hardwareMap.get(ColorSensor.class, "Color_Sensor");
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            double r,g,b;
            
            r = s.red();
            g = s.green();
            b = s.blue();
            
            String c = "Light detected:" + s.alpha() + "\nRed:" + r + "\nBlue:" + b + "\nGreen:" + g;
            telemetry.addData("Colors:",c);
            telemetry.update();

        }
    }
}
