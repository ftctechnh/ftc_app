/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-11-02

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core.gamecontroller


import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Toggle


/**
 * Wrapper class for the game pads in OpMode, packs in a bunch of additional functionality
 * such as detecting button presses
 */
@Suppress("unused", "PrivatePropertyName")
class Controller
{
    private val _INPUT_BUFFER = .15

    private var _gamePad: Gamepad? = null

    // Button toggle variables
    private var _aToggle = Toggle()
    private var _bToggle = Toggle()
    private var _xToggle = Toggle()
    private var _yToggle = Toggle()

    private var _leftBumperToggle = Toggle()
    private var _rightBumperToggle = Toggle()

    private var _leftStickButtonToggle = Toggle()
    private var _rightStickButtonToggle = Toggle()

    private var _dUpToggle = Toggle()
    private var _dDownToggle = Toggle()
    private var _dLeftToggle = Toggle()
    private var _dRightToggle = Toggle()


    private var _receivingInput = false

    /**
     * Reads input from a game pad, call this in every iteration of your OpMode loop.
     */
    fun read(GAME_PAD: Gamepad)
    {
        _gamePad = GAME_PAD
        _receivingInput = false     // Assume at first no input is sent to the controller
    }


    /**
     * Returns whether the "a" button is currently pressed or not
     */
    fun a(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.a
        return _gamePad?.a
    }


    /**
     * Returns whether the "a" button has been fully clicked (up and down) or not
     */
    fun aClicked(): Boolean?
    {
        return _aToggle.isPressed(_gamePad!!.a)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun b(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.b
        return _gamePad?.b
    }


    /**
     * Returns whether the "b" button has been fully clicked (up and down) or not
     */
    fun bClicked(): Boolean?
    {
        return _bToggle.isPressed(_gamePad!!.b)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun x(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.x
        return _gamePad?.x
    }


    /**
     * Returns whether the "x" button has been fully clicked (up and down) or not
     */
    fun xClicked(): Boolean?
    {
        return _xToggle.isPressed(_gamePad!!.x)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun y(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.y
        return _gamePad?.y
    }


    /**
     * Returns whether the "y" button has been fully clicked (up and down) or not
     */
    fun yClicked(): Boolean?
    {
        return _yToggle.isPressed(_gamePad!!.y)
    }


    /**
     * Returns whether the left bumper is currently pressed or not
     */
    fun leftBumper(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.left_bumper
        return _gamePad?.left_bumper
    }


    /**
     * Returns whether the left bumper has been fully clicked (up and down) or not
     */
    fun leftBumperClicked(): Boolean?
    {
        return _leftBumperToggle.isPressed(_gamePad!!.left_bumper)
    }


    /**
     * Returns whether the right bumper is currently pressed or not
     */
    fun rightBumper(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.right_bumper
        return _gamePad?.right_bumper
    }


    /**
     * Returns whether the right bumper has been fully clicked (up and down) or not
     */
    fun rightBumperClicked(): Boolean?
    {
        return _rightBumperToggle.isPressed(_gamePad!!.right_bumper)
    }


    /**
     * Returns whether the left stick button is currently pressed or not
     */
    fun leftStickButton(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.left_stick_button
        return _gamePad?.left_stick_button
    }


    /**
     * Returns whether the left stick button has been fully clicked (up and down) or not
     */
    fun leftStickButtonClicked(): Boolean?
    {
        return _leftStickButtonToggle.isPressed(_gamePad!!.left_stick_button)
    }


    /**
     * Returns whether the right stick button is currently pressed or not
     */
    fun rightStickButton(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.right_stick_button
        return _gamePad?.right_stick_button
    }


    /**
     * Returns whether the right stick button has been fully clicked (up and down) or not
     */
    fun rightStickButtonClicked(): Boolean?
    {
        return _rightStickButtonToggle.isPressed(_gamePad!!.right_stick_button)
    }


    /**
     * Returns whether the dpad up button is currently pressed or not
     */
    fun dUp(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.dpad_up
        return _gamePad!!.dpad_up
    }


    /**
     * Returns whether the dpad up button has been fully clicked (up and down) or not
     */
    fun dUpClicked(): Boolean?
    {
        return _dUpToggle.isPressed(_gamePad!!.dpad_up)
    }


    /**
     * Returns whether the dpad down button is currently pressed or not
     */
    fun dDown(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.dpad_down
        return _gamePad!!.dpad_down
    }


    /**
     * Returns whether the dpad down button has been fully clicked (up and down) or not
     */
    fun dDownClicked(): Boolean?
    {
        return _dDownToggle.isPressed(_gamePad!!.dpad_down)
    }


    /**
     * Returns whether the dpad left button is currently pressed or not
     */
    fun dLeft(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.dpad_left
        return _gamePad!!.dpad_left
    }


    /**
     * Returns whether the dpad left button has been fully clicked (up and down) or not
     */
    fun dLeftClicked(): Boolean?
    {
        return _dLeftToggle.isPressed(_gamePad!!.dpad_left)
    }


    /**
     * Returns whether the dpad right button is currently pressed or not
     */
    fun dRight(): Boolean?
    {
        _receivingInput = _receivingInput || _gamePad!!.dpad_right
        return _gamePad!!.dpad_right
    }


    /**
     * Returns whether the dpad right button has been fully clicked (up and down) or not
     */
    fun dRightClicked(): Boolean?
    {
        return _dRightToggle.isPressed(_gamePad!!.dpad_right)
    }


    /**
     * Returns the value of the left stick, x value
     */
    fun leftX(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.left_stick_x!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.left_stick_x!!.toDouble()
    }


    /**
     * Returns the value of the left stick, y value
     */
    fun leftY(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.left_stick_y!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.left_stick_y!!.toDouble()
    }


    /**
     * Returns the value of the right stick, x value
     */
    fun rightX(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.right_stick_x!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.right_stick_x!!.toDouble()
    }


    /**
     * Returns the value of the right stick, y value
     */
    fun rightY(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.right_stick_y!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.right_stick_y!!.toDouble()
    }


    /**
     * Returns the value of the left trigger
     */
    fun leftTrigger(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.left_trigger!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.left_trigger!!.toDouble()
    }


    /**
     * Returns the value of the right trigger
     */
    fun rightTrigger(): Double
    {
        _receivingInput = _receivingInput ||
                Math.abs(_gamePad?.right_trigger!!.toDouble()) >= _INPUT_BUFFER

        return _gamePad?.right_trigger!!.toDouble()
    }


    /**
     * Returns whether or not the controller is receiving input
     */
    fun receivingInput(): Boolean = _receivingInput
}