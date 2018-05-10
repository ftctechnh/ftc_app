package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Noah on 5/9/2018.
 */

@TeleOp(name = "Velocity Latency")
public class VelocityLatency extends OpMode {
    private LynxModule module;
    private long lastMs;
    private DcMotorEx backMotor;
    private DcMotorEx frontMotor;


    public void init() {
        module = hardwareMap.get(LynxModule.class, "m");
        backMotor = hardwareMap.get(DcMotorEx.class, "bl");
        frontMotor = hardwareMap.get(DcMotorEx.class, "fl");
        lastMs = System.currentTimeMillis();
    }

    public void init_loop() {
        long time = System.currentTimeMillis();

        telemetry.addData("Position 0", backMotor.getCurrentPosition());
        telemetry.addData("Position 1", frontMotor.getCurrentPosition());
        telemetry.addData("Velocity 0", backMotor.getVelocity(AngleUnit.RADIANS));
        telemetry.addData("Velocity 1", frontMotor.getVelocity(AngleUnit.RADIANS));
        telemetry.addData("Latency", time - lastMs);
        lastMs = time;
    }

    public void start() {

    }

    public void loop() {
        try {
            LynxGetBulkInputDataCommand command = new LynxGetBulkInputDataCommand(module);
            LynxGetBulkInputDataResponse resp = command.sendReceive();
            long time = System.currentTimeMillis();

            telemetry.addData("Position 0", resp.getEncoder(0));
            telemetry.addData("Position 1", resp.getEncoder(1));
            telemetry.addData("Velocity 0", resp.getVelocity(0));
            telemetry.addData("Velocity 1", resp.getVelocity(1));
            telemetry.addData("Latency", time - lastMs);
            lastMs = time;
        }
        catch (Exception e) { /* hmmmm */ }
    }

    public void stop() {

    }
}
