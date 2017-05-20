package org.firstinspires.ftc.teamcode.fieldtracking;

import android.support.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.utilities.Units;

/**
 * Created by ROUS on 3/3/2017.
 * Holds vuforia target observation and calculates robot orientation at locations
 */
public class VuforiaTarget {

    // Constants
    static public final VectorF XAxis = new VectorF(1,0,0,1);
    static public final VectorF PhoneOnBot = new VectorF(9f,2f,6f,0f);


    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    protected String _trackableName;
    protected OpenGLMatrix _trackableLoc;
    protected OpenGLMatrix _fieldLoc;
    protected OpenGLMatrix _poseLoc;

    protected VectorF _trackPos;
    protected VectorF _robotPos;
    protected VectorF _robotDir;
    protected VectorF _observedDir;
    protected VectorF _camPos1;
    protected VectorF _camPos2;
    protected double  _ccwDirectionRefXAxis;
    protected boolean _bValid;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    /**
     * Creates a new observation with the given trackable and field _position data
     */
    public VuforiaTarget(final String name, final OpenGLMatrix trackableLoc, final OpenGLMatrix fieldLoc, final OpenGLMatrix poseLoc ) {
        _trackableName = new String( name );
        _trackableLoc = new OpenGLMatrix( trackableLoc );
        _fieldLoc = new OpenGLMatrix( fieldLoc );
        _poseLoc = new OpenGLMatrix( poseLoc );
        _ccwDirectionRefXAxis = 0.0;

        calculate();
    }

    private @Nullable OpenGLMatrix getFieldLocation(final OpenGLMatrix phoneLocOnRobotInverted)
    {
        // Capture the location in order to avoid races with concurrent updates
        OpenGLMatrix trackableLocationOnField = this._trackableLoc;
        OpenGLMatrix pose = this._poseLoc;
        if (pose != null && trackableLocationOnField != null && phoneLocOnRobotInverted != null)
        {
            /**
             * The target is visible and we know the target's location on the field. Put
             * together the overall transformation matrix that computes the robot's
             * location on the field.
             */
            OpenGLMatrix result =
                    trackableLocationOnField
                            .multiplied(pose.inverted())
                            .multiplied(phoneLocOnRobotInverted);
            return result;
        } else {
            return null;
        }
    }

    public boolean calculate(){
        this._bValid = false;
        if (       null != this._trackableName && !this._trackableName.isEmpty()
                && null != this._trackableLoc
                && null != this._fieldLoc
                && null != this._poseLoc ){


            this._trackPos = this._trackableLoc.getTranslation().normalized3D();
            this._robotPos = this._fieldLoc.getTranslation().normalized3D();

            VectorF rawDiffObserved = this._trackPos.subtracted(this._robotPos).normalized3D();
            this._observedDir = rawDiffObserved;

            /**
             * inverted phone transform
             */
            /**Front center of bot on the x axis forward*/
            OpenGLMatrix phoneLoc1OnRobotInv = OpenGLMatrix
                    .translation(Units.inchtomm(8f), 0f, 0f )
                    .multiplied(
                            Orientation.getRotationMatrix(
                                    AxesReference.EXTRINSIC, AxesOrder.YZY,
                                    AngleUnit.DEGREES, -90f, 0f, 0f)).inverted();
            /**back center of bot on the x axis backwards*/
            OpenGLMatrix phoneLoc2OnRobotInv = OpenGLMatrix
                    .translation( Units.inchtomm(-8f), 0f, 0f )
                    .multiplied(
                            Orientation.getRotationMatrix(
                                    AxesReference.EXTRINSIC, AxesOrder.YZY,
                                    AngleUnit.DEGREES, -90f, 0f, 0f)).inverted();

            OpenGLMatrix cv1 = getFieldLocation(phoneLoc1OnRobotInv);
            OpenGLMatrix cv2 = getFieldLocation(phoneLoc2OnRobotInv);

            this._camPos1 = cv1.getTranslation();
            this._camPos2 = cv2.getTranslation();
            VectorF cvDelta = cv2.getTranslation().subtracted(cv1.getTranslation());

            this._robotDir = cvDelta;
            this._ccwDirectionRefXAxis = VectorFUtil.CalculateXYAngleCCW(this._robotDir, XAxis); /**rad angle */
            this._bValid = true;
        }
        return this._bValid;
    }

    public Boolean isValid() { return this._bValid; }

    public String formatAsString() {

        if ( this._bValid ) {
             return String.format("%s. [%.04f\",%.04f\",%.03fd]",this._trackableName
                    , Units.mmtoinch(this._robotPos.get(0))
                    , Units.mmtoinch(this._robotPos.get(1))
                    , Math.toDegrees(this._ccwDirectionRefXAxis));
        } else {
            return new String("");
        }

    }

    public Vector2d getRobotPos() {
        return new Vector2d( (double)_robotPos.get(0), (double)_robotPos.get(1) );
    }

    public Vector2d getRobotDir() {
        return new Vector2d( _robotDir.get(0), _robotDir.get(1));
    }
}
