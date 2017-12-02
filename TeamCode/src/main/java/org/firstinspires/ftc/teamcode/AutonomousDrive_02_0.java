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
                            counter = timeFromDistance(36.0 + 7.63);
                            break;
                        case CENTER:
                            counter = timeFromDistance(36.0);
                            break;
                        case RIGHT:
                            counter = timeFromDistance(36.0 - 7.63);
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
            }else if(timer.time() < counter+.5 + timeFromDistance(11)) {
                x = 0;
                y = .5;
            }else if(timer.time() < counter+.5+timeFromDistance(11) + timeFromDistance(7)){
                x = 0;
                y = -.5;
            }else{
                x = 0;
                y = 0;
            }

            engine.drive(x,y);


        }

    }

    /**
     *
     * @param x
     * @return inverse hyperbolic cosine of x
     */
    private double acosh(double x){
        return Math.log(x + Math.sqrt(x*x-1));
    }

    double vMax = 25; //inches per second
    double dragConstant = 44.7;

    /**
     * Time Bogg needs to go a distance at .5 power
     * @param distance
     * @return time
     */
    public double timeFromDistance(double distance){
        double t = vMax/dragConstant * acosh(Math.exp(dragConstant/(vMax*vMax)));
        return t;
    }
}

