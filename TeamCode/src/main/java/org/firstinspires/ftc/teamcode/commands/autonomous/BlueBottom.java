package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ColorSensorSystem;
import org.firstinspires.ftc.teamcode.systems.tools.Direction;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by Mahim on 1/13/2018.
 */
@Autonomous(name = "blue alliance bottom", group = "blue alliance")
public class BlueBottom extends LinearOpMode {
    private MecanumDriveSystem mecanumDriveSystem;
    private ColorSensorSystem colorSensorSystem;
    private ElapsedTime runtime = new ElapsedTime();

    private void initialize() {
        this.mecanumDriveSystem = new MecanumDriveSystem(hardwareMap);
        this.colorSensorSystem  = new ColorSensorSystem(hardwareMap);
        this.colorSensorSystem.setInitPosition();
    }

    private void knockDownRedJewel() {
        this.colorSensorSystem.goDown();
        sleep(1500);
        if(colorSensorSystem.isRed()) {
            this.mecanumDriveSystem.drive(0.5, 0.5, Direction.REVERSE);
            sleep(250);
        } else if(colorSensorSystem.isBlue()) {
            this.mecanumDriveSystem.drive(0.5, 0.5, Direction.FORWARD);
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
            knockDownRedJewel();
            mecanumDriveSystem.stop();
            sleep(1000);
            this.mecanumDriveSystem.drive(0.5, 0.3, Direction.REVERSE);
            sleep(1000);
            mecanumDriveSystem.stop();
            count++;
        }
    }
}
