package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.controller.Button;
import org.firstinspires.ftc.teamcode.Hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.Hardware.controller.Handler;
import org.firstinspires.ftc.teamcode.opmodes.debuggers.TeleOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.ArmState;
import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by idiot on 10/11/17.
 */
@TeleOp(name = "TeleOp", group="TeleOp")
public class TeleOpMode extends TeleOpModeDebugger {
    //protected final ConfigParser config;
    private Controller controller1;
    //private Controller controller2;
    private MecanumDriveSystem driveSystem;
    private ArmSystem armSystem;

    //protected Logger logger;

    public TeleOpMode() {
        msStuckDetectLoop = 100000000;
    }

    @Override
    public void start(){
        telemetry.addLine("start worked");
    }

    @Override
    public void run(){
        telemetry.addLine("dogs");
        controller1.handle();
        armSystem.run();

        float rx = controller1.gamepad.right_stick_x;
        float ry = controller1.gamepad.right_stick_y;
        float lx = controller1.gamepad.left_stick_x;
        float ly = controller1.gamepad.left_stick_y;

        driveSystem.mecanumDrive(rx, ry, lx, ly, false);
    }

    @Override
    public void init()
    {
        this.controller1 = new Controller(gamepad1);
        armSystem = new ArmSystem(this);
        this.driveSystem = new MecanumDriveSystem(this);
        initButton();
        telemetry.update();
    }

    @Override
    public void initialize()
    {

    }


    public void initButton() {
        telemetry.addData("buttons", "initialize");
        telemetry.update();
        controller1.a.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                telemetry.addData("Controller 1: ", controller1.a.isPressed.value());
                telemetry.update();
                armSystem.addState(ArmState.WINCH_TOP);
            }
        };

        controller1.b.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                telemetry.addData("Controller 1: ", controller1.b.isPressed.value());
                telemetry.update();
                armSystem.addState(ArmState.WINCH_BOTTOM);
            }
        };

        controller1.dPadDown.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                telemetry.addData("Controller 1: ", controller1.dPadDown.isPressed.value());
                telemetry.update();
                armSystem.addState(ArmState.ROTATE_DOWN);
            }
        };
        controller1.dPadUp.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                telemetry.addData("Controller 1: ", controller1.dPadUp.isPressed.value());
                telemetry.update();
                armSystem.addState(ArmState.ROTATE_UP);
            }
        };
        controller1.dPadUpShifted.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                telemetry.addData("Controller 1: ", controller1.dPadUpShifted.isPressed.value());
                telemetry.update();
                armSystem.addState(ArmState.ROTATE_LATCH);
            }
        };
        telemetry.addData("buttons", "finished initialize");
        telemetry.update();
    }
}
