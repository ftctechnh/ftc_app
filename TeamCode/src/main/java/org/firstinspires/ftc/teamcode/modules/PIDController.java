package org.firstinspires.ftc.teamcode.modules;

//we stole this from https://github.com/71104/simulejos/blob/master/Framework/src/lejos/util/PIDController.java via an ftc forum

/**
 *  Proportional <tt>&lt;P&gt;</tt>, Integral <tt>&lt;I&gt;</tt>, Derivative <tt>&lt;D&gt;</tt> controller implementation.
 *
 *  <code>P</code> depends on the present error, <code>I</code> on the accumulation of past errors, and <code>D</code> is a 
 *  prediction of future errors, based on 
 *  current rate of change. 
 * <p> <b>Proportional gain, Kp:</b>
 * Larger values typically mean faster response since the larger the error, the larger the proportional term compensation. 
 * An excessively large proportional gain will lead to process instability and oscillation.<br>
 * <p><b>Integral gain, Ki:</b>
 * Larger values imply steady state errors are eliminated more quickly. The trade-off is larger overshoot: any negative error integrated during transient response must be integrated away by positive error before reaching steady state.
 * <p><b>Derivative gain, Kd:</b>
 * Larger values decrease overshoot, but slow down transient response and may lead to instability due to signal noise amplification in the differentiation of the error.
 *  <p><b>Definitions:</b>
 *  <ul>
 *  <li>MV - Manipulated Variable. What the PID controller calculates to be used as the input to the process (i.e. motor speed).
 *  <LI>PV - Process Variable. The measurement of the process value.
 *  <LI>SP - Setpoint. The desired value that the PID controller works to achieve through process changes by MV.
 *  <li>error - The difference between PV and MV.
 *  <li>dt - Time delta in milliseconds between calls to <tt>{@link #doPID}</tt>.
 *  </ul>
 *
 *  The proportional term <code>P</code> (sometimes called gain) makes a change to the output that is proportional to the 
 *  current error value. The proportional response is adjusted by multiplying the error by Kp. A high proportional gain
 *  results in a large change in the output for a given change in the error.
 *  <P>
 *  The integral term <code>I</code> (sometimes called reset) accelerates the movement of the process towards the setpoint and eliminates the residual 
 *  steady-state error that 
 *  occurs with a proportional only controller. However, since the integral term is responding to accumulated errors from the past, 
 *  it can cause the present value to overshoot the setpoint value. The magnitude of the contribution of the integral term to the 
 *  overall control action is determined by the integral gain, Ki. This implementation uses <code>I += Ki * error * dt</code>. 
 *  If error is potentially large, using a small Ki gain 
 *  value may be necessary as the amplitude of <code>I</code> may cause instability.
 *  <p>
 *  Integral windup is basically what happens when the controller is pushed into a state where it has reached the maximum output 
 *  power but the set point has not been reached. In this situation the integral term will continue to grow even though it can 
 *  no longer have any impact on the controller output. The problem is that when the controller finally manages to get to the 
 *  set point the integral term may have grown very large and this will cause an overshoot that can take a relatively long time 
 *  to correct (as the integral value now needs to unwind). Two classic examples are:  
 *  <ul>
 * <li>A large change in the set point. This will take time for the motor or whatever to move to that point meanwhile the integral 
 * term builds up, and eventually will cause an overshoot. 
 * <li>The motor is stalled for a short period of time, at full power, again the integral term will continue to grow and will 
 * cause an overshoot. 
 * </ul>
 * This class provides two methods to manage integral windup:
 * <ol><li>
 * Disabling the integral function until the PV has entered the controllable region 
 * <li>Preventing the integral term from accumulating above or below pre-determined bounds
 * </ol>
 *
 * The derivative term <code>D</code> (sometimes called rate) slows the rate of change of the controller output and this effect is most noticeable
 * close to the controller setpoint. Hence, derivative control is used to reduce the magnitude of the overshoot produced by the 
 * integral component (<code>I</code>) and improve the combined controller-process stability. The rate of change of the process error is 
 * calculated by determining the slope of the error over time and multiplying this rate of change by the derivative gain Kd.  
 * The D term in the controller is highly sensitive to noise in the error term, and can cause a process to become unstable if 
 * the noise and the derivative gain Kd are sufficiently large.
 *  <p>
 *  It is important to tune the PID controller with an implementation of a consistent delay between calls to <code>doPID()</code>
 *  because the MV calc in a PID controller is time-dependent by definition. This implementation provides an optional delay (set
 *  in the constructor) and calculates the time delta (<code>dt</code>) between 
 *  calls to <code>{@link #doPID}</code> in milliseconds.
 *  <p>
 *  Reference: Wikipedia- <a href="http://en.wikipedia.org/wiki/PID_controller" target="_blank">http://en.wikipedia.org/wiki/PID_controller</a>
 *
 *  @author Kirk Thompson, 2/5/2011 &lt;lejos@mosen.net&gt;
 */
public class PIDController {
    /**
     * Proportional term ID
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_KP = 0;
    /**
     * Integral term ID. The <code>I</code> accumulator is an <tt>int</tt> so any decimal places are rounded in the calc: 
     * <code>I += Ki * error * dt;</code>
     *
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_KI = 1;
    /**
     * Derivitive term ID
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_KD = 2;
    /**
     * The Ramping Exponential value ID. Used for output (MV) ramping/attenuation which determines ramp shape. 1.0=linear, Set to 0.0 to disable 
     * output ramping. Larger values &gt;= 1 create steeper ramping curves. 0&lt;PID_RAMP_POWER&lt;1 will invert the curve
     * which will cause exponential MV amplification the closer we get to the SP (would this ever be useful?).
     *
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_RAMP_POWER = 3;
    /**
     * The Ramping Threshold value ID. Used for output ramping. When the PID Manipulated Variable (MV) is within this range (-+), output ramping is
     * applied to MV before it is returned from <code>{@link #doPID}</code>. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_RAMP_THRESHOLD = 4;
    /**
     * The deadband value ID. Used for output clipping. If MV within +- this range relative to zero, MV of zero is returned.
     * Set to zero to effectively disable. This is useful to avoid hunting around the SP when there is a lot
     * of slop in whatever the controller is controlling i.e. gear & link lash. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>. Using deadband is process actuator/control-specific and by definition, decreases accuracy 
     *     of reaching the <code>SP</code>.
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_DEADBAND = 5;

    /**
     *     The MV high limit cutoff value ID. Use for high limit cutoff for Manipulated Variable (<code>MV</code>). Set to a large value to 
     *     effectively disable. This is applied to <code>MV</code> before any ramping. Default is 900 at instantiation. The value 
     *     passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     *
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_LIMITHIGH = 6;

    /**
     * The MV low limit cutoff value ID. Use for low limit cutoff for Manipulated Variable (<code>MV</code>). Set to a large negative value to 
     *     effectively disable. This is applied to <code>MV</code> before any ramping. Default is -900. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     * @see #setPIDParam
     * @see #getPIDParam
     */
    public static final int PID_LIMITLOW = 7;

    /**
     * The Setpoint value ID. This is the value the PID controller works toward by changing MV is response to the error 
     * when <tt>doPID()</tt> is called. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     * @see #setPIDParam
     * @see #getPIDParam
     * @see #doPID
     */
    public static final int PID_SETPOINT = 8;

    /**
     * The Integral low limit cutoff value ID. Use for limiting accumulation of <code>I (Ki * error * dt)</code> less than a defined value. Set to zero
     *     to disable. Default is 0. Setting this clears the <code>I</code> term accumulator. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     * <P>    This is one methodology to manage integral windup.
     * @see #setPIDParam
     * @see #getPIDParam
     * @see #freezeIntegral
     * @see #PID_I_LIMITHIGH
     */
    public static final int PID_I_LIMITLOW = 9;

    /**
     * The Integral high limit cutoff value ID. Use for limiting accumulation of <code>I (Ki * error * dt)</code> greater than a defined value. Set to zero
     *     to disable. Default is 0. Setting this clears the <code>I</code> term accumulator. The value passed to <tt>setPIDParam()</tt>
     *     is cast to an <tt>int</tt>.
     * <P>    This is one methodology to manage integral windup.
     * @see #setPIDParam
     * @see #getPIDParam
     * @see #freezeIntegral
     * @see #PID_I_LIMITLOW
     */
    public static final int PID_I_LIMITHIGH = 10;

    /** The integral accumulator <code>I</code> value. Read-only.Calling <tt>setPIDParam()</tt> with this is ignored. The <tt>I</tt> value is the
     * accumulator for <code>Ki * error * dt</code>. 
     */
    public static final int PID_I = 11;

    /** The process variable (<code>PV</code>) value. Read-only.Calling <tt>setPIDParam()</tt> with this is ignored. The <tt>PV</tt> value is the
     * last value passed to <code>doPID()</code>.
     *
     * @see #doPID
     */
    public static final int PID_PV = 12;

    // Our default Constants for the PID controller 
    private float Kp=1.0f;          // proportional value determines the reaction to the current error
    private float Ki=0.0f;      // integral value determines the reaction based on the sum of recent errors
    private float Kd=0.0f;        // derivative value determines the reaction based on the rate at which the error has been changing
    private int highLimit = 900;            // assuming control of motor speed and thereby max would be 900 deg/sec
    private int lowLimit = -highLimit;
    private int previous_error = 0;
    private int deadband = 0;
    private int dt = 0;                     // cycle time, ms
    private long cycleTime=0;               // used to calc the time between each call (dt) to doPID()
    private int setpoint;                   // The setpoint to strive for
    private int error;                      // proportional term
    private int integral = 0;               // integral term
    private float derivative;               // derivitive term
    private int integralHighLimit = 0;
    private int integralLowLimit = 0;
    private boolean integralLimited = false;
    private boolean disableIntegral = false;
    private float power = 0;
    private int rampThresold = 0;
    private double rampExtent = 1;
    private int msdelay;
    private int cycleCount=0;
    private int PV;

    /**
     * Construct a PID controller instance using passed setpoint (SP) and millisecond delay (used before returning from a call to
     * <code>doPID()</code>).
     * @param setpoint The goal of the MV 
     * @param msdelay The delay in milliseconds. Set to 0 to disable any delay.
     * @see #doPID
     * @see #setDelay
     */
    public PIDController(int setpoint, int msdelay) {
        this.setpoint = setpoint;
        this.msdelay = msdelay;
    }

    /**
     * Set PID controller parameters.
     * @param paramID What parameter to set. See the constant definitions for this class.
     * @param value The value to set it to. Note that some values are cast to <tt>int</tt> depending on the particular <tt>paramID</tt> value used.
     * @see #getPIDParam
     */
    public void setPIDParam(int paramID, float value) {
        switch (paramID) {
            case PIDController.PID_KP:
                this.Kp = value;
                break;
            case PIDController.PID_KI:
                this.Ki = value;
                break;
            case PIDController.PID_KD:
                this.Kd = value;
                break;
            case PIDController.PID_RAMP_POWER:
                this.power = value;
                rampExtent = Math.pow(this.rampThresold, this.power);
                break;
            case PIDController.PID_RAMP_THRESHOLD:
                this.rampThresold = (int)value;
                if (this.rampThresold==0) break;
                rampExtent = Math.pow(this.rampThresold, this.power);
                break;
            case PIDController.PID_DEADBAND:
                this.deadband = (int)value;
                break;
            case PIDController.PID_LIMITHIGH:
                this.highLimit = (int)value;
                break;
            case PIDController.PID_LIMITLOW:
                this.lowLimit = (int)value;
                break;
            case PIDController.PID_SETPOINT:
                this.setpoint = (int)value;
                this.cycleTime = 0;
                break;
            case PIDController.PID_I_LIMITLOW:
                this.integralLowLimit = (int)value;
                this.integralLimited = (this.integralLowLimit!=0);
                break;
            case PIDController.PID_I_LIMITHIGH:
                this.integralHighLimit = (int)value;
                this.integralLimited = (this.integralHighLimit!=0);
                break;
            default:
                return;
        }
        // zero the Ki accumulator
        integral = 0;
    }

    /** Get PID controller parameters.
     * @param paramID What parameter to get. See the constant definitions for this class.
     * @return The requested parameter value
     *  @see #setPIDParam
     */
    public float getPIDParam(int paramID) {
        float retval =0.0f;
        switch (paramID) {
            case PIDController.PID_KP:
                retval=this.Kp;
                break;
            case PIDController.PID_KI:
                retval=this.Ki;
                break;
            case PIDController.PID_KD:
                retval=this.Kd;
                break;
            case PIDController.PID_RAMP_POWER:
                retval=this.power;
                break;
            case PIDController.PID_RAMP_THRESHOLD:
                retval=this.rampThresold;
                break;
            case PIDController.PID_DEADBAND:
                retval = this.deadband;
                break;
            case PIDController.PID_LIMITHIGH:
                retval = this.highLimit;
                break;
            case PIDController.PID_LIMITLOW:
                retval = this.lowLimit;
                break;
            case PIDController.PID_SETPOINT:
                retval = this.setpoint;
                break;
            case PIDController.PID_I_LIMITLOW:
                retval = this.integralLowLimit ;
                break;
            case PIDController.PID_I_LIMITHIGH:
                retval = this.integralHighLimit;
                break;
            case PID_I:
                retval = this.integral;
                break;
            case PID_PV:
                retval = this.PV;
                break;
            default:
        }
        return retval;
    }

    /** Freeze or resume integral accumulation. If frozen, any pre-existing integral accumulation is still used in the MV calculation. This
     * is useful for disabling the integral function until the PV has entered the controllable region [as defined by your process
     * requirements].
     * <P>This is one methodology to manage integral windup. This is <tt>false</tt> by default at instantiation.
     *
     * @param status <tt>true</tt> to freeze, <tt>false</tt> to thaw
     * @see #isIntegralFrozen
     */
    public void freezeIntegral(boolean status){
        this.disableIntegral = status;
    }

    /**
     *
     * @return <code>true</code> if the integral accumulation is frozen
     * @see #freezeIntegral
     */
    public boolean isIntegralFrozen() {
        return this.disableIntegral;
    }

    /**
     * Do the PID calc for a single iteration. Your implementation must provide the delay between calls to this method if you have
     * not set one with <code>setDelay()</code> or in the constructor.
     * @param processVariable The PV value from the process (sensor reading, etc.). 
     * @see #setDelay
     * @return The Manipulated Variable <code>MV</code> to input into the process (motor speed, etc.)
     */
    public int doPID(int processVariable){
        int outputMV;
        int delay=0;
        this.PV = processVariable;

        if (this.cycleTime==0) {
            this.cycleTime = System.currentTimeMillis();
            return 0;
        }
        error = setpoint - processVariable;
        error = Math.abs(error)<=deadband?0:error;
        if (!disableIntegral) integral += Ki * error * dt;
        if (integralLimited){
            if (integral>integralHighLimit) integral = integralHighLimit;
            if (integral<integralLowLimit) integral = integralLowLimit;
        }
        derivative = ((float)(error - previous_error))/dt;
        outputMV = (int)(Kp*error + integral + Kd*derivative);

        if (outputMV>highLimit) outputMV=highLimit;
        if (outputMV<lowLimit) outputMV=lowLimit;
        previous_error = error;
        outputMV=rampOut(outputMV);

        // delay the difference of desired cycle time and actual cycle time
        if (this.msdelay>0) {
            delay = this.msdelay-((int)(System.currentTimeMillis() - this.cycleTime)); // desired cycle time minus actual time
            if (delay>0) {
                try {
                    wait(delay);
                }
                catch (InterruptedException e) {
                }
            }
        }

        cycleCount++;
        // global time it took to get back to this statement
        dt = (int)(System.currentTimeMillis() - this.cycleTime);
        this.cycleTime = System.currentTimeMillis();
        return outputMV;
    }

    private int rampOut(int ov){
        if (power==0 || rampThresold==0) return ov;
        if (Math.abs(ov)>rampThresold) return ov;
        int workingOV;
        workingOV=(int)(Math.pow(Math.abs(ov), power) / rampExtent * rampThresold);
        return (ov<0)?-1*workingOV:workingOV;
    }

    /**
     * Set the desired delay before <code>doPID()</code> returns. Set to zero to effectively disable.
     *
     * @param msdelay Delay in milliseconds
     * @see #getDelay
     */
    public void setDelay(int msdelay) {
        this.msdelay = msdelay;
    }

    /**
     * Returns the <code>doPID()</code> timing delay. 
     *
     * @return The delay set by <code>setDelay()</code>
     * @see #setDelay
     */
    public int getDelay() {
        return this.msdelay;
    }
}