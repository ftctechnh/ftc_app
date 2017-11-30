package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Preston on 11/3/17.
 */

@Autonomous(name="AutonomousDrive_02_0", group="Testing")
public class AutonomousDrive_02_0 extends LinearOpMode
{
    DriveEngine engine;
    Camera camera;
    double x = 0;
    double y = 0;
    double pinch = .7;

    Servo pincherL;
    Servo pincherR;

    ElapsedTime timer = new ElapsedTime();

    RelicRecoveryVuMark mark;
    double counter = 0;

    void updateScreen()
    {
        telemetry.addData("Mark: ", mark);
        telemetry.addData("Counter: ", counter);
        telemetry.update();
        idle();
    }

    @Override
    public void runOpMode()
    {
        engine = new DriveEngine(hardwareMap);
        camera = new Camera(hardwareMap, telemetry);
        mark = RelicRecoveryVuMark.UNKNOWN;
        waitForStart();

        pincherL = hardwareMap.servo.get("pincherL");
        pincherR = hardwareMap.servo.get("pincherR");

        pincherL.setPosition(1);
        pincherR.setPosition(1);

        double counter = 0;

        while (opModeIsActive())
        {
            while(mark == RelicRecoveryVuMark.UNKNOWN) {
                mark = camera.identify();
                if (mark != RelicRecoveryVuMark.UNKNOWN) {
                    switch (mark) {
                        case LEFT:
                            counter = 2.0;
                            break;
                        case CENTER:
                            counter = 2.3;
                            break;
                        case RIGHT:
                            counter = 2.5;
                            break;
                        default:
                            counter = 0;
                    }
                    timer.reset();
                    break;
                }
                updateScreen();
            }

            if(timer.time() < counter){
                x = -.5;
                y = 0;
            }
            else if(timer.time() < counter + .5){
                x = 0;
                y = 0;
            }else if(timer.time() < counter + 1.5) {
                x = 0;
                y = .5;
            }else if(timer.time() < counter + 2.5){
                x = 0;
                y = 0;
                pincherL.setPosition(0);
                pincherR.setPosition(0);
            }else if(timer.time() < counter + 4){
                x = 0;
                y = -.5;
            }else{
                x = 0;
                y = 0;
            }

            engine.drive(x,y);


        }

    }
}

