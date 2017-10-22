package edu.usrobotics.opmode.mecanumbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import edu.usrobotics.opmode.LoggedOp;
import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.compbot.CompbotHardware;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.Goal;
import edu.usrobotics.opmode.task.MotorTask;
import edu.usrobotics.opmode.task.Task;
import edu.usrobotics.opmode.task.TaskType;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public abstract class MecanumBotAuto extends LinearOpMode {

    private MecanumBotHardware robot = new MecanumBotHardware();

    private boolean isBlue;

    private VuforiaLocalizer vuforia;
    private VuforiaTrackables cryptogramPictures;
    private List<VuforiaTrackable> allTrackables;

    public MecanumBotAuto(boolean color){

        isBlue = color;

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ab+0cBX/////AAAAGdHqAMTnx0GLk99ODKi2npU8fZTSoYRz3NVvSFAK0EFk6cVF8RTzBiLbhxPYq7ux9X+ATW+W0EXwTqTJYv7a2DyHhScsxg9fzafjr2Ddgdu75ltwpjE/EtNQWfKrSIQJIAespD3AiYczKRK/nQ9txHF9nE9DYht++su01GmV4Hr1KWSwF5H+ZeCTz3Au8NiSGUEPWv6zGmocyTjg00+TcRzAJdf9AFrrZFe1OeiY59egxotwJi7gnYUSfrqL/Mvc79BdDxUENl8FttSNkGxgjtiwjdZBIao7DjYnI21xvIvde98e2i26BOQAuQbn/4eov3Y6G4or0nJUDUIjAzcA0Y6whdiE5qwfd5wdzy9Bkq3J";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        cryptogramPictures = vuforia.loadTrackablesFromAsset("FTC2017-18");

        VuforiaTrackable leftPicture = cryptogramPictures.get(0);
        leftPicture.setName("leftPicture");

        VuforiaTrackable rightPicture  = cryptogramPictures.get(1);
        rightPicture.setName("rightPicture");

        VuforiaTrackable centerPicture  = cryptogramPictures.get(2);
        centerPicture.setName("centerPicture");

        allTrackables = new ArrayList<>();
        allTrackables.addAll(cryptogramPictures);

    }

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Wait for the game to start
        waitForStart();

        cryptogramPictures.activate();

        for (VuforiaTrackable trackable : allTrackables) {

            telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible() ? "Visible" : "Not Visible");

            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();

        }

        robot.bringDownBallKnocker();

        sleep(500);

        float motorPower = 0f;

        telemetry.addData("Red", robot.colorSensor.red());
        telemetry.addData("Blue", robot.colorSensor.blue());
        telemetry.update();

        if(isBlue){

            if(robot.readingBlue()){

                motorPower = -0.5f;

            }

            else {

                motorPower = 0.5f;

            }

        }

        else{

            if(robot.readingBlue()){

                motorPower = 0.5f;

            }

            else{

                motorPower = -0.5f;

            }

        }

        robot.setPower(motorPower);

        sleep(200);

        robot.setPower(0f);

        sleep(100);

        if(motorPower < 0){

            robot.setPower(0.5f);
            sleep(400);
            robot.setPower(0f);

        }

    }

}