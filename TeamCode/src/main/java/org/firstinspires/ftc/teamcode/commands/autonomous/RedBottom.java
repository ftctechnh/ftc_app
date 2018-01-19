package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.ColorSensorSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by Mahim on 1/13/2018.
 */
@Autonomous(name = "red alliance bottom", group = "red alliance")
public class RedBottom extends LinearOpMode {
    private MecanumDriveSystem mecanumDriveSystem;
    private ArmSystem armSystem;
    private ColorSensorSystem colorSensorSystem;
    private ElapsedTime runtime = new ElapsedTime();

    private void initialize() {
        this.mecanumDriveSystem = new MecanumDriveSystem(hardwareMap);
        this.armSystem          = new ArmSystem(hardwareMap);
        this.colorSensorSystem  = new ColorSensorSystem(hardwareMap);
        this.colorSensorSystem.setInitPosition();
    }

    private void knockDownBlueJewel() {
        this.colorSensorSystem.goDown();
        sleep(1500);
        if(colorSensorSystem.isBlue()) {
            this.mecanumDriveSystem.driveBackwards(0.5, 0.5);
            sleep(250);
        } else if(colorSensorSystem.isRed()) {
            this.mecanumDriveSystem.driveForward(0.5, 0.5);
            sleep(250);
        } else {
            mecanumDriveSystem.stop();
        }
        this.colorSensorSystem.setInitPosition();
        sleep(1000);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        runtime.reset();
        int count = 0;

        while (opModeIsActive() && (count < 1)) {
            knockDownBlueJewel();
            mecanumDriveSystem.stop();
            sleep(1000);
            this.mecanumDriveSystem.drive(0.5, 0.5);
            sleep(1000);
            mecanumDriveSystem.stop();
            count++;
        }
    }
}
