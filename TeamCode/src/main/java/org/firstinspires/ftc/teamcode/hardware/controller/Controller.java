package org.firstinspires.ftc.teamcode.hardware.controller;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idiot on 11/3/17.
 */

public class Controller
{
    public Gamepad gamepad;

    private float DEFAULT_TRIGGER_VALUE;

    private float rightTriggerValue;
    private float leftTriggerValue;

    private List<Button> uniqueButtons;

    public Button a;
    public Button b;
    public Button x;
    public Button y;
    public Button back;
    public Button start;
    public Button dPadDown;
    public Button dPadUp;
    public Button dPadLeft;
    public Button dPadRight;
    public Button leftStickButton;
    public Button leftTrigger;
    public Button rightBumper;
    public Button rightStickButton;
    public Button rightTrigger;

    public Button aShifted;
    public Button bShifted;
    public Button xShifted;
    public Button yShifted;
    public Button backShifted;
    public Button startShifted;
    public Button dPadDownShifted;
    public Button dPadUpShifted;
    public Button dPadLeftShifted;
    public Button dPadRightShifted;
    public Button leftStickButtonShifted;
    public Button leftTriggerShifted;
    public Button rightBumperShifted;
    public Button rightStickButtonShifted;
    public Button rightTriggerShifted;

    public Controller(final Gamepad gamepad)
    {
        //this.parser = new ConfigParser("Controller");
        //DEFAULT_TRIGGER_VALUE = parser.getFloat("default_trigger");
        //this.rightTriggerValue = DEFAULT_TRIGGER_VALUE;
        //this.leftTriggerValue = DEFAULT_TRIGGER_VALUE;
        this.gamepad = gamepad;
        uniqueButtons = new ArrayList<Button>();

        a = new Button();
        b = new Button();
        x = new Button();
        y = new Button();
        back = new Button();
        start = new Button();
        dPadDown = new Button();
        dPadUp = new Button();
        dPadLeft = new Button();
        dPadRight = new Button();
        leftStickButton = new Button();
        leftTrigger = new Button();
        rightBumper = new Button();
        rightStickButton = new Button();
        rightTrigger = new Button();

        a.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.a && !gamepad.left_bumper;
            }
        };

        b.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.b && !gamepad.left_bumper;
            }
        };

        x.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.x && !gamepad.left_bumper;
            }
        };

        y.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.y && !gamepad.left_bumper;
            }
        };

        back.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.back && !gamepad.left_bumper;
            }
        };

        start.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.start && !gamepad.left_bumper;
            }
        };

        dPadDown.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_down && !gamepad.left_bumper;
            }
        };

        dPadLeft.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_left && !gamepad.left_bumper;
            }
        };

        dPadRight.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_right && !gamepad.left_bumper;
            }
        };

        dPadUp.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_up && !gamepad.left_bumper;
            }
        };

        leftStickButton.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.left_stick_button && !gamepad.left_bumper;
            }
        };

        rightBumper.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_bumper;
            }
        };

        rightStickButton.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_stick_button && !gamepad.left_bumper;
            }
        };

        rightTrigger.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_trigger > rightTriggerValue && !gamepad.left_bumper;
            }
        };

        leftTrigger.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.left_trigger > leftTriggerValue && !gamepad.left_bumper;
            }
        };

        aShifted = new Button();
        bShifted = new Button();
        xShifted = new Button();
        yShifted = new Button();
        backShifted = new Button();
        startShifted = new Button();
        dPadDownShifted = new Button();
        dPadUpShifted = new Button();
        dPadLeftShifted = new Button();
        dPadRightShifted = new Button();
        leftStickButtonShifted = new Button();
        leftTriggerShifted = new Button();
        rightBumperShifted = new Button();
        rightStickButtonShifted = new Button();
        rightTriggerShifted = new Button();

        aShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.a && gamepad.left_bumper;
            }
        };

        bShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.b && gamepad.left_bumper;
            }
        };

        xShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.x && gamepad.left_bumper;
            }
        };

        yShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.y && gamepad.left_bumper;
            }
        };

        backShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.back && gamepad.left_bumper;
            }
        };

        startShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.start && gamepad.left_bumper;
            }
        };

        dPadDownShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_down && gamepad.left_bumper;
            }
        };

        dPadLeftShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_left && gamepad.left_bumper;
            }
        };

        dPadRightShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_right && gamepad.left_bumper;
            }
        };

        dPadUpShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.dpad_up && gamepad.left_bumper;
            }
        };

        leftStickButtonShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.left_stick_button && gamepad.left_bumper;
            }
        };

        rightBumperShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_bumper && gamepad.left_bumper;
            }
        };

        rightStickButtonShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_stick_button && gamepad.left_bumper;
            }
        };

        rightTriggerShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.right_trigger > rightTriggerValue && gamepad.left_bumper;
            }
        };

        leftTriggerShifted.isPressed = new Func<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return gamepad.left_trigger > leftTriggerValue && gamepad.left_bumper;
            }
        };
    }

    public void setTriggerValue(TriggerType type, float value)
    {
        switch (type)
        {
            case RIGHT:
                this.rightTriggerValue = value;
                break;
            case LEFT:
                this.leftTriggerValue = value;
                break;
            default:
                throw new IllegalArgumentException("Unknown Trigger Type");
        }
    }

    public void addButton(Button button) {
        this.uniqueButtons.add(button);
    }

    public void handle()
    {
        if (gamepad == null) {
            return;
        }
        a.testAndHandle();
        b.testAndHandle();
        x.testAndHandle();
        y.testAndHandle();
        back.testAndHandle();
        start.testAndHandle();
        dPadDown.testAndHandle();
        dPadUp.testAndHandle();
        dPadLeft.testAndHandle();
        dPadRight.testAndHandle();
        leftStickButton.testAndHandle();
        leftTrigger.testAndHandle();
        rightBumper.testAndHandle();
        rightStickButton.testAndHandle();
        rightTrigger.testAndHandle();
        aShifted.testAndHandle();
        bShifted.testAndHandle();
        xShifted.testAndHandle();
        yShifted.testAndHandle();
        backShifted.testAndHandle();
        startShifted.testAndHandle();
        dPadDownShifted.testAndHandle();
        dPadUpShifted.testAndHandle();
        dPadLeftShifted.testAndHandle();
        dPadRightShifted.testAndHandle();
        leftStickButtonShifted.testAndHandle();
        leftTriggerShifted.testAndHandle();
        rightBumperShifted.testAndHandle();
        rightStickButtonShifted.testAndHandle();
        rightTriggerShifted.testAndHandle();
        for (Button button : uniqueButtons) {
            button.testAndHandle();
        }
    }
}
