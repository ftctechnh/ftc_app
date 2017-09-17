package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;

public class Step_TurnLeft extends Step_TurnRight {

    // Constructor, called to create an instance of this class.
    public Step_TurnLeft(double angleDegrees) {
        super(angleDegrees);
    }


    @Override

    public String getLogKey() {
        return "Step_TurnLeft";
    }


    @Override
    protected double remainingAngle() {


        return this.targetAngle - robot.gyroSensor.getIntegratedZValue();
    }


    @Override
    protected double determinePower() {

        return -super.determinePower();

    }

}
