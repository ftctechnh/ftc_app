package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.Hardware.controller.Button;
import org.firstinspires.ftc.teamcode.Hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.LinearOpModeDebugger;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.TeleOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.IMUSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

import java.util.logging.Handler;

/**
 * Created by idiot on 10/11/17.
 */
@TeleOp(name = "dogs", group="TeleOp")
public class TeleOpMode extends TeleOpModeDebugger {
    //protected final ConfigParser config;
    private Controller controller1;
    private Controller controller2;
    private MecanumDriveSystem driveSystem;
    private ArmSystem armSystem;

    //protected Logger logger;


    @Override
    public void start(){
        telemetry.addLine("start worked");
    }

    @Override
    public void run(){
        telemetry.addLine("dogs");
        controller1.handle();
        controller2.handle();
        telemetry.update();

        if(controller1.a.isPressed.value()){
            armSystem.slideUp();
            armSystem.robotUp();
            telemetry.addData("Controller 1: ", controller1.a.isPressed.value());
        }

        if(controller1.b.isPressed.value()){

            armSystem.robotDown();
            telemetry.addData("Controller 1: ", controller1.b.isPressed.value());
        }


        float rx = controller1.gamepad.right_stick_x;
        float ry = controller1.gamepad.right_stick_y;
        float lx = controller1.gamepad.left_stick_x;
        float ly = controller1.gamepad.left_stick_y;

        driveSystem.mecanumDrive(rx, ry, lx, ly, false);
    }


    @Override
    public void init() {
        this.controller1 = new Controller(gamepad1);
        this.controller2 = new Controller(gamepad2);
        armSystem = new ArmSystem(this);
        this.driveSystem = new MecanumDriveSystem(this);
        initButton();
        telemetry.update();
    }


    public void initButton() {

        boolean aPressed = controller1.a.isPressed.value();

        if(aPressed) {
            armSystem.robotDown();
            telemetry.addData("Button b on controller 1 is pressed", "good");
            telemetry.update();
        } else {
            telemetry.addLine("Not Pressed");
        }


        if(controller1.b.isPressed.value()){
            armSystem.robotDown();
            telemetry.addData("Button b on controller 1 is pressed", "good");
            telemetry.update();

        } else{
            telemetry.addData("Button b is not pressed", "bad");

        }
        if(controller2.a.isPressed.value()){
            armSystem.slideDown();
            telemetry.addLine("Controller 2 A button pressed");
            telemetry.update();
        }
        if(controller2.b.isPressed.value()){
            armSystem.slideUp();
            telemetry.addLine("Controller 2 B button not pressed");
            telemetry.update();
        }
    }
}
