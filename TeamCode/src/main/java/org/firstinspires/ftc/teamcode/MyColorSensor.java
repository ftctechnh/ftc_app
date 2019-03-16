package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MyColorSensor {
    private ColorSensor colorSensor;


    enum Color
    {
        Red(0),
        Scarlet(5),
        OrangeRed(13),
        Vermilion(18),
        InternationalOrange(19),
        SafetyOrange(22),
        DarkOrange(29),
        OrangePeel(36),
        Orange(39),
        SelectiveYellow(42),
        Amber(45),
        GoldenPoppy(46),
        TangerineYellow(48),
        Gold(50),
        SchoolBusYellow(51),
        GoldenYellow(52),
        Yellow(57),
        ChartreuseYellow(64),
        ElectricLime(70),
        SpringBud(77),
        Chartreuse(90),
        Harlequin(105),
        Lime(120),
        FreeSpeechGreen(135),
        SpingGreen(150),
        BrightTurquoise(167),

        Aqua(180),
        DodgerBlue(210),
        Blue(240),
        ElectricIndigo(270),
        Magenta(300);



        double typicalHue;

        Color(double hue)
        {
            typicalHue = hue;
        }
    }

    MyColorSensor(HardwareMap hardwareMap)
    {
        colorSensor = hardwareMap.colorSensor.get("color");
    }

    boolean isColor(Color color)
    {
        double[] hsv = getHSV();
        double hue = hsv[0];
        return Math.abs(color.typicalHue - hue) < 5;
    }

    Color getColor(double hue)
    {
        for (Color c: Color.values()) {
            if(c.typicalHue >= hue)
                return c;
        }
        return Color.Red;
    }

    Color getColor()
    {
        return getColor(getHue());
    }

    double getHue()
    {
        return getHSV()[0];
    }

    double[] getHSV()
    {
        double red = colorSensor.red() / 255.0;
        double green = colorSensor.green() / 255.0;
        double blue = colorSensor.blue() / 255.0;

        double max = MyMath.max(red, green, blue);
        double min = MyMath.min(red, green, blue);
        double delta = max - min;

        double hue;
        if(delta == 0)
            hue = 0;
        else if(max == red)
            hue = ((green - blue) / delta) % 6 * 60;
        else if(max == green)
            hue = ((blue - red) / delta + 2) * 60;
        else //if(max == blue)
            hue = ((red - green) / delta + 4) * 60;

        double saturation;
        if(max == 0)
            saturation = 0;
        else
            saturation = delta / max;

        double value = max;

        return new double[]{hue, saturation, value};
    }
}
