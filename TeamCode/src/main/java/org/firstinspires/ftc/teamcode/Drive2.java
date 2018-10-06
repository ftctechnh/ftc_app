package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;
/* Copy of the original drive Class, with changes made to simplify.
 * also for experimentation :-)
 * Created by howard on 12/21/17.
 */
@SuppressWarnings("WeakerAccess")
public class Drive2
{
    static final float MAX_TURN_TIME = (float)4000;
    static final float BUMP_TIME     = (float)2000;
    static final float MOTORMAX = 1;
    static final float MOTORMIN = -1;
    BotMotors autonomousCrab(CrabVec cv) {
       BotMotors m = new BotMotors();
       m.leftFront = cv.forward;
       m.status = 0;
       return m;
    }
    BotMotors driveIt(Gamepad cx)
    {
        BotMotors m = new BotMotors();
        float maxDrive;
        float frontMax;
        float rearMax;
        //   Sum the vectors and hope for the best :-)
        //                 turn=left.x,      fwd=left.y,      crab=right.x
        m.leftFront  =  cx.left_stick_x + cx.left_stick_y + cx.right_stick_x;
        m.rightFront = -cx.left_stick_x + cx.left_stick_y - cx.right_stick_x;
        m.leftRear   =  cx.left_stick_x + cx.left_stick_y - cx.right_stick_x;
        m.rightRear  = -cx.left_stick_x + cx.left_stick_y + cx.right_stick_x;

        // must keep things proportional when the sum of any x or y > 1
        frontMax =  Math.max(Math.abs(m.leftFront), Math.abs(m.rightFront));
        rearMax = Math.max(Math.abs(m.leftRear),  Math.abs(m.rightRear));
        maxDrive = Math.max(frontMax, rearMax);
        maxDrive = (maxDrive < MOTORMAX) ? maxDrive : MOTORMAX;

        // scale and clip the front motors, then the aft
        m.leftFront = m.leftFront/maxDrive;
        m.leftFront = Range.clip(m.leftFront, MOTORMIN, MOTORMAX);
        m.rightFront = m.rightFront/maxDrive;
        m.rightFront = Range.clip(m.rightFront, MOTORMIN, MOTORMAX);

        m.leftRear = m.leftRear/maxDrive;
        m.leftRear = Range.clip(m.leftRear, MOTORMIN, MOTORMAX);
        m.rightRear = m.rightRear/maxDrive;
        m.rightRear = Range.clip(m.rightRear, MOTORMIN, MOTORMAX);

        return m;

    }

    public BotMotors fwd6(float pwr, long goal, long stageTime){
        BotMotors mPower = new BotMotors();
        if (goal < stageTime){
            mPower.leftFront = pwr;
            mPower.rightFront = pwr;
            mPower.leftRear = pwr;
            mPower.rightRear = pwr;
            mPower.status = -1;
        }
        else
        {
            mPower.leftFront = 0;
            mPower.rightFront = 0;
            mPower.leftRear = 0;
            mPower.rightRear = 0;
            mPower.status = 0;
        }
        return mPower;
    }
    /***********************************************************
     * gyroturn5
     *  startHeading  - input. heading when this "state" started
     *  currHeading   - input. what is the heading when gt5 invoked
     *  newHeading    - input. Destination heading
     *  turnPwr       - input. -100 to 100, percent power. negative means counterclockwise
     *  turnTime      - input. how long this "state" has been in play
     * @return mPower - multiply the pwrSet by -1 for the starboard motor in the calling routine.
     *   Note: The gyroturn5 will quit after MAX_TURN_TIME, and will give a "bump" to increase turn
     *   power after BUMP_TIME.  The idea is to juice the power
     */
    public BotMotors gyroTurn5(int startH, int currH, int newH, float pwrSet, float turnTime)
    {
        BotMotors mPower = new BotMotors();
        int accumTurn = Math.abs(startH - currH);
        accumTurn = (accumTurn > 360)? (360-accumTurn):accumTurn;
        int cw = (pwrSet < 0)? -1: 1;
        int transit = (((currH > newH) && (cw > 0)) ||
                ((currH < newH) && (cw < 0))) ?  360 : 0;
        int desiredRotation = Math.abs(transit + (cw*newH) + ((-1*cw)*currH));
        desiredRotation = (desiredRotation > 360) ? desiredRotation - 360 : desiredRotation;
        if((accumTurn < desiredRotation) && (turnTime < MAX_TURN_TIME)){
            pwrSet = (turnTime > BUMP_TIME)? pwrSet: (float)1.0;
        }
        else
        {
            pwrSet = 0;
        }
        mPower.leftFront = pwrSet;
        mPower.rightFront = pwrSet;
        mPower.leftRear = pwrSet;
        mPower.rightRear = pwrSet;
        return mPower;
    }
}
