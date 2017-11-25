package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This will be our Autonomous and our first try at a state machine (comment one)
 * Created by Joseph Liang on 10/30/2017.
 */

@Autonomous(name="Relic Recovery: State Machine", group="Pushbot")
//@Disabled
public class RlcRcvryAutoSwitch extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    RelicRecoveryVuMark glyph;
    GlyphArm gilgearmesh = new GlyphArm();

    JewelSystem sensArm = new JewelSystem();
    String jewelColor;

    VuforiaLocalizer vuforia;
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);

    @Override
    public void init() {
        robot.init(hardwareMap);
        parameters.vuforiaLicenseKey = "Ab47Iov/////AAAAGVSivzkE2UEEoiMKAm72knw+f69pC3+FWtnwmp26yNKLBnQ7o48HaEaAIbAMmi4KE/YqAOa1hWE6uV+U5eOZyTSDhJOQQqMhHKtFymevtYLWk+CsXyFA4ipONM9Yfi06TN3sAJUDqqm3sWR8pWgTAvs2M/VoRDw9ZNwg1MzxZPmU5VVmr9ifsv0rGbcoE585jWH+jzTnnnxnRN+3i/AoE1nTthvv9KIq6ZSNpgR2hguJUcBv8B43gg122D0akqbG+pAIGp78TiMn5BZqciaHRSzvZV2JOcIMZzk5FPp96rn7sWhyHZMI5mpUpgA25CG8gTC8e+8NoxMyN277hid7VFubrb4VbsH5qUxDzfDCcmOV";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        //need code for gripping glyph and moving arm slightly up

        gilgearmesh.clawPos(0);
        //wait needed? Also... guessed parameters
        gilgearmesh.armPos(2, 1);
        stateMachineFlow = 0;
        relicTrackables.activate();
    }


    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                stateMachineFlow++;
                break;
            case 1:
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
                while (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    //viewforia stuff goes here
                    if (vuMark == RelicRecoveryVuMark.CENTER){glyph = RelicRecoveryVuMark.CENTER;}
                    else if (vuMark == RelicRecoveryVuMark.LEFT){glyph = RelicRecoveryVuMark.LEFT;}
                    else if (vuMark == RelicRecoveryVuMark.RIGHT){glyph = RelicRecoveryVuMark.RIGHT;}
                }
                stateMachineFlow++;
                break;
            case 2://look at this
                sensArm.armPos(.444);
                if (sensArm.colorSens() == "blue"){
                    jewelColor = "blue";}
                else if (sensArm.colorSens() == "red"){
                    jewelColor = "red";}
                stateMachineFlow++;
                break;
            case 3:
                //this might change depending on what arm attachment used for jewels
                if (jewelColor == "blue"){encoderDrive(TURN_SPEED, 10, -10, 5);}
                else if (jewelColor == "red"){encoderDrive(DRIVE_SPEED, 10, 10, 5);}

                //move forward towards jewel
                //knock off correct jewel

                if (glyph() == 1){encoderDrive(DRIVE_SPEED);}
                else if (glyph() == 2) {encoderDrive(DRIVE_SPEED);}
                else if (glyph() == 3) {encoderDrive(DRIVE_SPEED);}
                stateMachineFlow++;
                break;
            case 4:
                //move off balancing stone and move towards box
                if (glyph == 2) {
                encoderDrive(TURN_SPEED,-24.35, 24.35, 5);//face box
                encoderDrive(DRIVE_SPEED,30, 30, 5); //in position to place glyph
            }
    else if (glyph == 3) {
                encoderDrive(TURN_SPEED, -12.175, 12.175, 5); //turn to face middle
                encoderDrive(DRIVE_SPEED, 18, 18, 5);
                encoderDrive(TURN_SPEED, -12.175, 12.175, 5); //turn to face box
                encoderDrive(DRIVE_SPEED, 30, 3, 5); //in position to place glyph
            }
            else if (glyph == 1) {
                encoderDrive(TURN_SPEED, -24.35, 24.35, 5);//face box
                encoderDrive(DRIVE_SPEED, 20, 20, 5);
                encoderDrive(TURN_SPEED, 12.175, -12.175, 5);//face wall
                encoderDrive(DRIVE_SPEED, 7.63, 7.63, 5);
                encoderDrive(TURN_SPEED, -12.175, 12.175, 5);//face box
                encoderDrive(DRIVE_SPEED, 10, 10, 5); //in position to place glyph
            }
            stateMachineFlow++;
                break;
            case 5:
                gilgearmesh.clawPoss(1);
                stateMachineFlow++;
                break;
            case 6:
                //end?
                break;
        }
    }

    }//end of class

