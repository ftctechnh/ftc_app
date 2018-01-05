package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Jewel Park: Red Mid", group="Pushbot")
public class RedMidJewelPark extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    GlyphArm gilgearmesh = new GlyphArm();

    JewelSystem sensArm = new JewelSystem();
    JewelColor jewelColor = JewelColor.UNKNOWN;
    double time;

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

        gilgearmesh.clawPos(1);
        stateMachineFlow = 0;
        sensArm.colorLED(false);

        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
        telemetry.addData("Color",jewelColor);
        telemetry.addData("Case",stateMachineFlow);
        telemetry.update();
    }

    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                gilgearmesh.armPos(50,.6);
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
                break;
            case 2:
                if (sensArm.colorSens() == JewelColor.BLUE){
                    jewelColor = JewelColor.BLUE;
                    telemetry.addData("Color",jewelColor);
                    telemetry.addData("Case",stateMachineFlow);
                    telemetry.update();}
                else if (sensArm.colorSens() == JewelColor.RED){
                    jewelColor = JewelColor.RED;
                    telemetry.addData("Color",jewelColor);
                    telemetry.addData("Case",stateMachineFlow);
                    telemetry.update();}

                stateMachineFlow++;
                break;
            case 3:
                //knock off correct jewel
                if (jewelColor == JewelColor.BLUE){sensArm.wristPos(WristServoPosition.LEFT);}
                else if (jewelColor == JewelColor.RED){sensArm.wristPos(WristServoPosition.RIGHT);}
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
                gilgearmesh.armPos(25,.6);
                robot.pivotTurn(.5, 60, RobotDirection.LEFT);
                robot.linearDrive(.25,20);
                stateMachineFlow++;
                break;
            case 6:
                gilgearmesh.armPos(10,.6);
                robot.statTurn(.5,-45);
                stateMachineFlow++;
                break;
            case 7:
                robot.linearDrive(.25,2);
                stateMachineFlow++;
                break;
            case 8:
                //end?
                break;
        }
    }
}
