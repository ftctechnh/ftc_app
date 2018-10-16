package org.firstinspires.ftc.teamcode.components.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public interface Drivable {

    public void run(double power);

    public void setDirection(DcMotorSimple.Direction direction);

    public MotorConfigurationType getMotorType();

    public void setMotorType(MotorConfigurationType motorType);

    public DcMotorController getController();

    public int getPortNumber();

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior);

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior();

    public void setTargetPosition(int position);

    public int getTargetPosition();

    public boolean isBusy();

    public int getCurrentPosition();

    public void setMode(DcMotor.RunMode mode);

    public DcMotor.RunMode getMode();
}
