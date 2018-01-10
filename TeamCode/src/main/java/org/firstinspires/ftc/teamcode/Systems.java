package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Kaden on 1/3/2018.
 */

public class Systems {
    private DriveMecanum DriveMecanum;
    private AutoDrive AutoDrive;
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private BeehiveVuforia vuforia;
    private JewelArm JewelArm;
    public Systems(DriveMecanum drive, ForkLift ForkLift, RelicClaw RelicClaw) {
        this.DriveMecanum = drive;
        this.ForkLift = ForkLift;
        this.RelicClaw = RelicClaw;
    }
    public Systems(AutoDrive drive, ForkLift ForkLift, JewelArm JewelArm, BeehiveVuforia vuforia) {
        this.AutoDrive = drive;
        this.ForkLift = ForkLift;
        this.JewelArm = JewelArm;
        this.vuforia = vuforia;
    }
    void pushInBlock() {
        ForkLift.openClaw();
        AutoDrive.driveTranslateRotate(0, -AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED,0,4);
        ForkLift.closeClaw();
        ForkLift.moveUntilDown(0.75);
        AutoDrive.driveTranslateRotate(0, AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED,0,10);
    }
    public RelicRecoveryVuMark getMark() {
        RelicRecoveryVuMark vuMark;
        vuMark = vuforia.getMark();
        boolean triedAgain = false;
        if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
            AutoDrive.driveTranslateRotate(0,0,-AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.FIND_VUMARK_DISTANCE);
            vuMark = vuforia.getMark();
            triedAgain = true;
        }
        if (triedAgain) {
            AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.FIND_VUMARK_DISTANCE);
        }
        return vuMark;
    }
    void findJewel(Color target) {
        JewelArm.down();
        ElapsedTime time = new ElapsedTime();
        time.start();
        boolean tryAgain = false;
        while (JewelArm.cs.red() < 2 && JewelArm.cs.blue() < 2) {
            if(time.getElapsedTime() > 3000) {
                tryAgain = true;
                break;
            }
        }
        if (tryAgain) {
            jewelTryAgain(target);
            return;
        }
        int blue = 0;
        int red = 0;
        for (int i = 0; i <= 5; i++) {
            if (JewelArm.cs.blue() > JewelArm.cs.red()) {
                blue += 1;
            } else if (JewelArm.cs.blue() < JewelArm.cs.red()) {
                red += 1;
            }
        }
        if(target == Color.RED) {
            if(red>blue) {
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
            else {
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
        }
        if(target == Color.BLUE) {
            if(blue>red) {
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
            else {
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
        }
    }
<<<<<<< HEAD
    private void jewelTryAgain(Color target) {
        AutoDrive.driveTranslateRotate(0,0,-AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, 1);
        JewelArm.down();
        ElapsedTime time = new ElapsedTime();
        time.start();
        while (JewelArm.cs.red() < 2 && JewelArm.cs.blue() < 2) {
            if(time.getElapsedTime() > 3000) {
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0,0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, 1);
                return;
            }
        }
        int blue = 0;
        int red = 0;
        int none = 0;
        for (int i = 0; i <= 5; i++) {
            if (JewelArm.cs.blue() > JewelArm.cs.red()) {
                blue += 1;
            } else if (JewelArm.cs.blue() < JewelArm.cs.red()) {
                red += 1;
            } else {
                none +=1;
            }
        }
        if(target == Color.RED) {
            if(red>blue) {
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
            else if (blue>red){
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            } else {
                return;
            }
        }
        if(target == Color.BLUE) {
            if(blue>red) {
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            }
            else if (red>blue){
                AutoDrive.driveTranslateRotate(0, 0, -AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
                JewelArm.up();
                AutoDrive.driveTranslateRotate(0, 0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, AutoDrive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            } else {
                return;
            }
        }
        AutoDrive.driveTranslateRotate(0,0, AutoDrive.SPIN_ON_BALANCE_BOARD_SPEED, 1);
=======
    public void grabSecondGlyph() {
        ForkLift.closeClaw();
        ForkLift.moveMotor(1, 750);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 550);
        ForkLift.openClaw();
        DriveMecanum.driveTranslateRotate(0, 1, 0, 500);
        ForkLift.moveUntilDown(0.75);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 750);
        ForkLift.closeClaw();
        sleep(250);
        ForkLift.moveMotor(1, 250);
    }
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
>>>>>>> f9faba2c962814f2a7ae85e9535587b0df76d926
    }
}
