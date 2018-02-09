package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Eric on 2/3/2018.
 */
@Autonomous (name = "AutoTest", group = "Autos")
@Disabled
public class AutoTest extends RelicAutoMode{
    @Override
    public void runOpMode() {
        //<editor-fold desc="Hardware Map">
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        //</editor-fold>
        //
        configureMotors();
        //
        telemetry.addData("Start", leftBackMotor.getCurrentPosition());
        telemetry.update();
        //
        waitForStartify();
        //
        toPosition(5, .5);
        sleep(10000);
        telemetry.addData("End", leftBackMotor.getCurrentPosition());
        telemetry.update();
        sleep(10000);
    }
}
