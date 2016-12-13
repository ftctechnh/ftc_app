package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;


public class AutoVortex extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private StateMachine main;

    private StateMachine shooter;

    @Override
    public void init() {
        robot.init(hardwareMap);

        shooter = new StateMachine(
                new State("off") {
                    @Override
                    public void run() {
                        robot.shooter.setPower(0);
                    }
                },
                new State("on") {
                    @Override
                    public void run() {
                        robot.shooter.setPower(HardwareVortex.SHOOTER_POWER);
                    }
                }
        );
    }

    @Override
    public void start() {
        shooter.start();
    }

    @Override
    public void loop() {
        robot.generateTelemetry(telemetry, true);
    }

    @Override
    public void stop() {
        shooter.stop();
    }
}
