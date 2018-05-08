package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;


@Autonomous(name = "0267BLUECORNERAUTO",group = "Pushbot")
public class BlueBoardAuto extends LinearOpMode {
    private String startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    ;
    private ElapsedTime runtime = new ElapsedTime();
    Hardware267Bot robot = new Hardware267Bot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        robot.spinnerMotor.setPower(1);
        // Step 1:  getting off the balance board
        robot.leftMotor.setPower(0.5);
        robot.rightMotor.setPower(0.5);
        robot.belts.setPower(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            telemetry.addData("Path", "Moving forward off of balancing stone.");
            telemetry.update();
        }

        // Step 2:  turning to the right
        robot.leftMotor.setPower(0.6);
        robot.rightMotor.setPower(-0.6);
        robot.belts.setPower(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.55)) {
            telemetry.addData("Path", "Turning Left");
            telemetry.update();
        }
        // Step 3: Backpedal
        robot.leftMotor.setPower(-0.5);
        robot.rightMotor.setPower(-0.5);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.4)) {
            telemetry.addData("Path", "Backing Up");
            telemetry.update();

        }
        // Step 4:  Stop.
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        //robot.leftClaw.setPosition(1.0);
        //robot.rightClaw.setPosition(0.0);

        telemetry.addData("Path", "Stopping");
        telemetry.update();
        sleep(1000);



        // Step 4: Turn Belt
        robot.belts.setPower(-1.0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Turning Belt");
            telemetry.update();
        }



                // Step 4:  Stop.
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        //robot.leftClaw.setPosition(1.0);
        //robot.rightClaw.setPosition(0.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
