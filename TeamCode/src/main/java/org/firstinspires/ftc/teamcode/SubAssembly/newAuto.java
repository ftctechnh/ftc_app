package org.firstinspires.ftc.teamcode.SubAssembly;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous(name = "newAuto", group = "Drive")
public class newAuto extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        resetClock();
        DriveControl Drive = new DriveControl(this);
        Drive.moveForward(1.0);

    }
}
