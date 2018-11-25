package org.firstinspires.ftc.teamcode;

public class ChassisConfig {

    private float rearDiameter;

    private boolean useFourWheelDrive;

    private float rearWheelSpeed;

    protected ChassisConfig(float rearDiameter, boolean useFourWheelDrive, float rearWheelSpeed) {
        this.rearDiameter = rearDiameter;
        this.useFourWheelDrive = useFourWheelDrive;
        this.rearWheelSpeed = rearWheelSpeed;
    }


    public float getRearWheelDiameter() {
        return rearDiameter;
    }

    public boolean getUseFourWheelDrive() {
        return useFourWheelDrive;
    }

    // MAGIC NUMBERS for the motor encoders
    //static final double COUNTS_PER_MOTOR_REV = 560;    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    // 28 cycles per rotation at the main motor, times 20:1 geared down
    //static final double COUNTS_PER_MOTOR_TORKNADO = 1440; // https://asset.pitsco.com/sharedimages/resources/torquenado_dcmotorspecifications.pdf
    // 24 cycles per revolution, times 60:1 geared down.
    public float getRearWheelSpeed() {
        return rearWheelSpeed;
    }

    public static ChassisConfig forMonsieurMallah() {
        return new ChassisConfig(4.9375f, false, 1440);
    }

    public static ChassisConfig forQuickSilver() {
        return new ChassisConfig(4.0f, true, 560);
    }

    public static ChassisConfig forPhatSwipe() {
        return new ChassisConfig(4.0f, false, 560);
    }
}
