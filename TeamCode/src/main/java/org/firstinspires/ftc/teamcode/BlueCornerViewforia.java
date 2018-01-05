package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous(name="Jewel Park: Blue Corner(Viewforia)", group="Pushbot")
//@Disabled
public class BlueCornerViewforia extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    RelicRecoveryVuMark glyph;
    GlyphArm gilgearmesh = new GlyphArm();

    JewelSystem sensArm = new JewelSystem();
    JewelColor jewelColor = JewelColor.UNKNOWN;
    double time;


    VuforiaLocalizer vuforia;


    @Override
    public void init() {
        telemetry.addData("before init","here");
        telemetry.update();
        robot.init(hardwareMap);
        telemetry.addData("after robot","here");
        telemetry.update();
        gilgearmesh.init(hardwareMap);
        telemetry.addData("after gilgearmesh","here");
        telemetry.update();
        sensArm.init(hardwareMap);
        telemetry.addData("after init","here");
        telemetry.update();


        //code for gripping glyph and moving arm slightly up
        //gilgearmesh.clawPos(1);
        //wait needed? Also... guessed parameters
        //gilgearmesh.armPos(800,.6);
        stateMachineFlow = 0;

        sensArm.colorLED(false);

        //telemetry.addData("Key",glyph);
        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
        telemetry.addData("Color",jewelColor);
        telemetry.addData("Case",stateMachineFlow);
        telemetry.update();
    }

    /*@Override
    public void init_loop(){


        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
        telemetry.addData("Color",jewelColor);
        telemetry.addData("Case",stateMachineFlow);
        telemetry.update();}*/

    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                telemetry.addData("Arm",gilgearmesh.getArmPosition());
                telemetry.addData("Color",jewelColor);
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                time = getRuntime();
                stateMachineFlow++;
                break;

            case 1:
                sensArm.wristPos(WristServoPosition.CENTER);
                sensArm.armPos(ArmServoPosition.BOTTOM);
                sensArm.colorLED(true);
                if (1 < getRuntime() - time ) {
                    stateMachineFlow++;
                }
                telemetry.addData("Arm",gilgearmesh.getArmPosition());
                telemetry.addData("Color",jewelColor);
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                break;

            case 2:
                    if (sensArm.colorSens() == JewelColor.BLUE){
                        jewelColor = JewelColor.BLUE;
                        telemetry.addData("Arm",gilgearmesh.getArmPosition());
                        telemetry.addData("Color",jewelColor);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}
                    else if (sensArm.colorSens() == JewelColor.RED){
                        jewelColor = JewelColor.RED;
                        telemetry.addData("Arm",gilgearmesh.getArmPosition());
                        telemetry.addData("Color",jewelColor);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}

                    stateMachineFlow++;
                    break;
            case 3:
                //knock off correct jewel
                if (jewelColor == JewelColor.BLUE){sensArm.wristPos(WristServoPosition.RIGHT);}
                else if (jewelColor == JewelColor.RED){sensArm.wristPos(WristServoPosition.LEFT);}
                else if (jewelColor == JewelColor.UNKNOWN){sensArm.armPos(ArmServoPosition.TOP);}

                time = getRuntime();

                telemetry.addData("Jewel Arm",sensArm.getArmPosition());
                telemetry.addData("Jewel Wrist",sensArm.getWristPosition());
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                stateMachineFlow++;
                break;
            case 4:
                if (2 < getRuntime() - time){
                    sensArm.wristPos(WristServoPosition.CENTER);
                    sensArm.armPos(ArmServoPosition.TOP);
                    stateMachineFlow++;
                }
                break;
            case 5:
                time = getRuntime();
                stateMachineFlow++;
                break;
            case 6:
                int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
                VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
                parameters.vuforiaLicenseKey = "Ab47Iov/////AAAAGVSivzkE2UEEoiMKAm72knw+f69pC3+FWtnwmp26yNKLBnQ7o48HaEaAIbAMmi4KE/YqAOa1hWE6uV+U5eOZyTSDhJOQQqMhHKtFymevtYLWk+CsXyFA4ipONM9Yfi06TN3sAJUDqqm3sWR8pWgTAvs2M/VoRDw9ZNwg1MzxZPmU5VVmr9ifsv0rGbcoE585jWH+jzTnnnxnRN+3i/AoE1nTthvv9KIq6ZSNpgR2hguJUcBv8B43gg122D0akqbG+pAIGp78TiMn5BZqciaHRSzvZV2JOcIMZzk5FPp96rn7sWhyHZMI5mpUpgA25CG8gTC8e+8NoxMyN277hid7VFubrb4VbsH5qUxDzfDCcmOV";
                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
                vuforia = ClassFactory.createVuforiaLocalizer(parameters);

                VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
                VuforiaTrackable relicTemplate = relicTrackables.get(0);
                relicTemplate.setName("relicVuMarkTemplate");
                relicTrackables.activate();

                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
                while (vuMark == RelicRecoveryVuMark.UNKNOWN && getRuntime() < time + 3) {
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    //viewforia stuff goes here
                    if (vuMark == RelicRecoveryVuMark.CENTER){glyph = RelicRecoveryVuMark.CENTER;
                        telemetry.addData("Key",glyph);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}
                    else if (vuMark == RelicRecoveryVuMark.LEFT){glyph = RelicRecoveryVuMark.LEFT;
                        telemetry.addData("Key",glyph);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}
                    else if (vuMark == RelicRecoveryVuMark.RIGHT){glyph = RelicRecoveryVuMark.RIGHT;
                        telemetry.addData("Key",glyph);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}
                }
                stateMachineFlow++;
                break;
            case 7:
                gilgearmesh.armPos(50,.6);
                if (glyph == RelicRecoveryVuMark.CENTER || glyph == RelicRecoveryVuMark.UNKNOWN){
                    robot.linearDrive(.25,-20);
                }else if (glyph == RelicRecoveryVuMark.LEFT){
                    robot.linearDrive(.25,-17);
                }else if (glyph == RelicRecoveryVuMark.RIGHT){
                    robot.linearDrive(.25,-23);
                }

                stateMachineFlow++;
                break;
            case 8:
                gilgearmesh.armPos(10,.6);
                robot.statTurn(.5,140);
                stateMachineFlow++;
                break;
            case 9:
                robot.linearDrive(.25,-2);
                stateMachineFlow++;
                break;
            case 10:
                robot.linearDrive(.25,4);
                stateMachineFlow++;
                break;
            case 11:
                //end
                break;
        }
    }
}//end of class

