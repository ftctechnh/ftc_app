package com.tbdftc.opmodes.autonomous;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.tbdftc.fieldhandlers.AllianceColor;
import com.tbdftc.robothandlers.Drivetrain;
import com.tbdftc.robothandlers.Shooter;
import com.tbdftc.robothandlers.Sweeper;
import com.tbdftc.viewhandlers.options.NumberCategory;
import com.tbdftc.viewhandlers.options.OptionMenu;
import com.tbdftc.viewhandlers.options.SingleSelectCategory;

import static com.tbdftc.fieldhandlers.AllianceColor.BLUE_ALLIANCE;
import static com.tbdftc.fieldhandlers.AllianceColor.RED_ALLIANCE;

@Autonomous(name="Auto: Basic", group="Auto")
public class AutonomousBasic extends OpMode {

    //General flow variables.
    private ElapsedTime runtime = new ElapsedTime();
    private int stateMachineFlow;

    //Localized method variables.
    private double lastReadRuntime;
    private int lastReadRunState;

    //Parametrized variables.
    OptionMenu autonomousParamsMenu;
    private AllianceColor allianceColor;
    private int particlesToShoot;
    private int delayTime;

    //Robot hardware variables.
    private Sweeper sweeper;
    private Drivetrain drivetrain;
    private Shooter shooter;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap.dcMotor.get("wheelLeftFront"), hardwareMap.dcMotor.get("wheelLeftRear"), hardwareMap.dcMotorController.get("wheelsLeft"),
                hardwareMap.dcMotor.get("wheelRightFront"), hardwareMap.dcMotor.get("wheelRightRear"), hardwareMap.dcMotorController.get("wheelsRight"));
        shooter = new Shooter(hardwareMap.dcMotor.get("shooterLeft"), hardwareMap.dcMotor.get("shooterRight"),
                              hardwareMap.dcMotorController.get("particleShooter"), hardwareMap.servo.get("shooterKam"));
        sweeper = new Sweeper(hardwareMap.dcMotor.get("particleSweeper"), hardwareMap.dcMotorController.get("particleSweeperController"));

        OptionMenu.Builder autonomousParamsMenuBuilder = new OptionMenu.Builder(hardwareMap.appContext);
        SingleSelectCategory allianceSelectorMenuCategories = new SingleSelectCategory("Alliance");
        SingleSelectCategory particlesToShootSelectorMenuCategories = new SingleSelectCategory("Particles to Shoot");
        NumberCategory delaySelectorMenu = new NumberCategory("Delay (Sec.) [Not Inc'l Launch]");
        allianceSelectorMenuCategories.addOption("Red");
        allianceSelectorMenuCategories.addOption("Blue");
        particlesToShootSelectorMenuCategories.addOption("2 Particles");
        particlesToShootSelectorMenuCategories.addOption("1 Particle");
        particlesToShootSelectorMenuCategories.addOption("0 Particles");
        autonomousParamsMenuBuilder.addCategory(allianceSelectorMenuCategories);
        autonomousParamsMenuBuilder.addCategory(particlesToShootSelectorMenuCategories);
        autonomousParamsMenuBuilder.addCategory(delaySelectorMenu);
        autonomousParamsMenu = autonomousParamsMenuBuilder.create();
        autonomousParamsMenu.show();

        lastReadRuntime = 0.0;
        lastReadRunState = 0;

        stateMachineFlow = 0;
    }

    @Override
    public void loop() {
        switch(stateMachineFlow) {

            case 0:
                //Read run parameters and set initial runtime.
                readMenuParameters();
                runtime.reset();
                stateMachineFlow++;
                break;

            case 1:
                //Drive forward.
                drivetrain.setPower(0.5,0.5);
                stateMachineFlow++;
                break;

            case 2:
                //Stop driving after time. :(
                if(getStateRuntime() > 0.375) stateMachineFlow++;
                break;

            case 3:
                //Start shooter, if if launching at least one particle.
                drivetrain.stop();
                if(particlesToShoot != 0) shooter.setPower(-1.0);
                stateMachineFlow++;
                break;

            case 4:
                //Wait for shooter to normalize at full speed, if launching at least one particle.
                if(particlesToShoot == 0 || getStateRuntime() > 0.5) stateMachineFlow++;
                break;

            case 5:
                //Launch particle, if at least one is remaining to launch.
                if(particlesToShoot > 0) shooter.setPositionShooting();
                if(particlesToShoot == 0 || getStateRuntime() > 1.5) stateMachineFlow++;
                break;

            case 6:
                //Return to loading position, if at least one is remaining to launch.
                if(particlesToShoot > 0) shooter.setPositionLoading();
                if(particlesToShoot == 0 || getStateRuntime() > 1.5) stateMachineFlow++;
                break;

            case 7:
                //Decrement particles to launch, then shoot remaining particles.
                particlesToShoot--;
                if(particlesToShoot > 0) stateMachineFlow = 5;
                else stateMachineFlow++;
                break;

            case 8:
                //Stop shooter.
                shooter.setPower(0.0);
                stateMachineFlow++;
                break;

            case 9:
                //Wait for user-specified delay. (Note that balls launch before delay!)
                if(runtime.seconds() > delayTime) stateMachineFlow++;
                break;

            default:
                //IDK fam...
                break;

        }

        telemetry.addData("Runtime", runtime.seconds());
        telemetry.addData("State Runtime", getStateRuntime());
        telemetry.addData("Run State", stateMachineFlow);
        telemetry.addLine("------------------------");
        telemetry.addData("Alliance", allianceColor.toString());
        telemetry.addData("Particles to Launch", particlesToShoot);
    }

    @Override
    public void stop() {
        drivetrain.stop();
        shooter.stop();
        sweeper.stop();
    }

    public double getStateRuntime() {
        if(lastReadRunState != stateMachineFlow) {
            lastReadRuntime = runtime.seconds();
            lastReadRunState = stateMachineFlow;
        }

        return runtime.seconds() - lastReadRuntime;
    }

    public void readMenuParameters() {
        try {
            delayTime = Integer.parseInt(autonomousParamsMenu.selectedOption("Delay (Sec.) [Not Inc'l Launch]"));
        } catch (NumberFormatException e) {
            delayTime = 0;
        }

        particlesToShoot = 2; //Assume 2 unless otherwise specified.
        if(autonomousParamsMenu.selectedOption("Particles to Shoot").equals("1 Particle")) {
            particlesToShoot = 1;
        }
        if(autonomousParamsMenu.selectedOption("Particles to Shoot").equals("0 Particles")) {
            particlesToShoot = 0;
        }

        allianceColor = RED_ALLIANCE; //Assume red unless otherwise specified.
        if(autonomousParamsMenu.selectedOption("Alliance").equals("Blue")) {
            allianceColor = BLUE_ALLIANCE;
        }
    }

}
