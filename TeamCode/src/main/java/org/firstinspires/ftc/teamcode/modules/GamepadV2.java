package org.firstinspires.ftc.teamcode.modules;

    import com.qualcomm.robotcore.exception.RobotCoreException;
    import com.qualcomm.robotcore.hardware.Gamepad;
    import com.qualcomm.robotcore.util.Range;

public class GamepadV2 extends Gamepad{
    public final static double JOYSTICK_MIN = -1.00F;
    public final static double JOYSTICK_MAX = 1.00F;
    protected float dpadThreshold;
    protected float joystickDeadzone;

    private long bitfield = 0L;

    public GamepadV2(){
        this(0.2F, 0.2F);
    }

    public GamepadV2(float dpadThreshold, float joystickDeadzone){
        this.dpadThreshold = dpadThreshold;
        this.joystickDeadzone = joystickDeadzone;
    }

    /**
     * Copies the contents of the other gamepad, place this in the loop() section of your TeleOp OpMode
     * @param gamepad original gamepad
     */

    public void update(Gamepad gamepad){
        try {
            this.copy(gamepad);
        }
        catch (RobotCoreException e){
            this.reset();
        }
    }

    public double left_stick_x_exponential(double maxPower){
        Range.throwIfRangeIsInvalid(maxPower, 0.00F, 1.00F);
        return exponential(maxPower, this.left_stick_x, this.joystickDeadzone);
    }

    public double left_stick_y_exponential(double maxPower){
        Range.throwIfRangeIsInvalid(maxPower, 0.00F, 1.00F);
        return exponential(maxPower, this.left_stick_y, this.joystickDeadzone);
    }

    public double right_stick_x_exponential(double maxPower){
        Range.throwIfRangeIsInvalid(maxPower, 0.00F, 1.00F);
        return exponential(maxPower, this.right_stick_x, this.joystickDeadzone);
    }

    public double right_stick_y_exponential(double maxPower){
        Range.throwIfRangeIsInvalid(maxPower, 0.00F, 1.00F);
        return exponential(maxPower, this.right_stick_y, this.joystickDeadzone);
    }

    public double triggers_exponential(double maxPower){
        Range.throwIfRangeIsInvalid(maxPower, 0.00F, 1.00F);
        //Find dominant trigger
        double dominant = (this.left_trigger > this.right_trigger)?-this.left_trigger:this.right_trigger;
        return exponential(maxPower, dominant, this.joystickDeadzone);
    }

    public boolean left_trigger(){
        return this.left_trigger > 0F;
    }

    public boolean right_trigger(){
        return this.right_trigger > 0F;
    }

    public boolean a_isReleased(boolean onPress){
        return this.buttonPressed(button.a, onPress);
    }

    public boolean b_isReleased(boolean onPress){
        return this.buttonPressed(button.b, onPress);
    }

    public boolean x_isReleased(boolean onPress){
        return this.buttonPressed(button.x, onPress);
    }

    public boolean y_isReleased(boolean onPress){
        return this.buttonPressed(button.y, onPress);
    }

    public boolean left_bumper_isReleased(boolean onPress){
        return this.buttonPressed(button.left_bumper, onPress);
    }

    public boolean right_bumper_isReleased(boolean onPress){
        return this.buttonPressed(button.right_bumper, onPress);
    }

    public boolean left_trigger_isReleased(boolean onPress){
        return this.buttonPressed(button.left_trigger, onPress);
    }

    public boolean right_trigger_isReleased(boolean onPress){
        return this.buttonPressed(button.right_trigger, onPress);
    }

    public boolean left_stick_button_isReleased(boolean onPress){
        return this.buttonPressed(button.left_stick_button, onPress);
    }

    public boolean right_stick_button_isReleased(boolean onPress){
        return this.buttonPressed(button.right_stick_button, onPress);
    }

    private synchronized boolean buttonPressed(button button, boolean onPress){
        boolean bool = false;
        switch(button){
            case a:
                bool = this.a;
                break;
            case b:
                bool = this.b;
                break;
            case x:
                bool = this.x;
                break;
            case y:
                bool = this.y;
                break;
            case left_bumper:
                bool = this.left_bumper;
                break;
            case right_bumper:
                bool = this.right_bumper;
                break;
            case left_stick_button:
                bool = this.left_stick_button;
                break;
            case right_stick_button:
                bool = this.right_stick_button;
                break;
            case left_trigger:
                bool = this.left_trigger();
                break;
            case right_trigger:
                bool = this.right_trigger();
                break;
        }
        if (bool != ((bitfield & button.getValue()) == button.getValue())){
            bitfield ^= button.getValue();  //Toggle that bit
            if(bool == onPress)
                return true;
        }
        return false;
    }

    public static double exponential(double maxPower, double joystickCurrent, double deadzone){
        if(joystickCurrent == 0) return 0;
        double polarity = joystickCurrent/Math.abs(joystickCurrent);
        return polarity*(maxPower-Math.sqrt(((Math.abs(joystickCurrent)-JOYSTICK_MAX)*(maxPower*maxPower))/(deadzone-JOYSTICK_MAX)));
    }

    public static double linear(double maxPower, double joystickCurrent, double deadzone){
        if(joystickCurrent == 0) return 0;
        double polarity = joystickCurrent/Math.abs(joystickCurrent);
        return polarity*(maxPower-((Math.abs(joystickCurrent)-JOYSTICK_MAX)*maxPower)/(deadzone-JOYSTICK_MAX));
    }

    public enum button{
        a(0), b(1), x(2), y(3), left_bumper(4), right_bumper(5), left_stick_button(6), right_stick_button(7), left_trigger(8), right_trigger(9);
        private final int value;
        private button(final int newValue){
            value = newValue;
        }
        public int getValue(){
            return 1 << value;
        }
    }
}