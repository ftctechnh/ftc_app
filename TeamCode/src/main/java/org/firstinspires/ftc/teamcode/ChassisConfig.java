package org.firstinspires.ftc.teamcode;

public class ChassisConfig {

    private float rearDiameter;
    private boolean useFourWheelDrive;
    private float rearWheelSpeed;
    private float turnSpeed;
    private float moveSpeed;
    private boolean leftMotorsReversed;
    private boolean rightMotorsReversed;
    private boolean hasWalle;

    protected ChassisConfig(float rearDiameter, boolean useFourWheelDrive, float rearWheelSpeed,
                            float turnSpeed, float moveSpeed, boolean leftMotorsReversed, boolean rightMotorsReversed, boolean hasWalle) {
        this.rearDiameter = rearDiameter;
        this.useFourWheelDrive = useFourWheelDrive;
        this.rearWheelSpeed = rearWheelSpeed;
        this.turnSpeed = turnSpeed;
        this.moveSpeed = moveSpeed;
        this.leftMotorsReversed = leftMotorsReversed;
        this.rightMotorsReversed = rightMotorsReversed;
        this.hasWalle = hasWalle;
    }

    public float getRearWheelDiameter() {
        return rearDiameter;
    }
    public boolean getUseFourWheelDrive() {
        return useFourWheelDrive;
    }
    public float getRearWheelSpeed() {
        return rearWheelSpeed;
    }
    public float getTurnSpeed() { return turnSpeed; }
    public float getMoveSpeed() { return moveSpeed; }
    public boolean isLeftMotorReversed() { return leftMotorsReversed; }
    public boolean isRightMotorReversed() { return rightMotorsReversed; }
    public boolean getHasWalle() {return hasWalle; }


    // https://www.wikihow.com/Determine-Gear-Ratio
    // 20  -> 26
    static final float QUICK_SILVER_CHAIN_GEAR = 1.3f;

    // MAGIC NUMBERS for the motor encoders
    // https://asset.pitsco.com/sharedimages/resources/torquenado_dcmotorspecifications.pdf
    static final float COUNTS_PER_MOTOR_TORKNADO = 1440;  // 24 cycles per revolution, times 60:1 geared down.
    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    static final float COUNTS_PER_MOTOR_REV_HDHEX_40  = 1120; // 28 cycles per rotation at the main motor, times 40:1 geared down
    static final float COUNTS_PER_MOTOR_REV_HDHEX_20 = 560; // 28 cycles per rotation at the main motor, times 20:1 geared down

    public static ChassisConfig forMonsieurMallah() {
        return new ChassisConfig(
                4.9375f,
                false,
                COUNTS_PER_MOTOR_TORKNADO,
                0.5f,
                0.5f,
                true,
                false,
                false);
    }

    // Note: whole robot is going to run backwards
    public static ChassisConfig forQuickSilver() {
        return new ChassisConfig(
                4.0f,
                true,
                COUNTS_PER_MOTOR_REV_HDHEX_20 * QUICK_SILVER_CHAIN_GEAR,
                0.5f,
                0.75f,
                false,
                true,
                true);
    }

    public static ChassisConfig forPhatSwipe() {
        return new ChassisConfig(
                4.0f,
                true,
                COUNTS_PER_MOTOR_REV_HDHEX_40,
                0.3f,
                0.8f,
                true,
                false,
                false);
    }
}
