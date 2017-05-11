package org.firstinspires.ftc.teamcode.Libs;

public final class Enum_Libs {

    /**
     * 枚举 运行方向，非全向移动
     */
    public enum RunDirection{
        /**
         * 向前运动
         */
        Forward,
        /**
         * 向后运动
         */
        Backward,
        /**
         * 向左转向
         */
        Left,
        /**
         * 向右转向
         */
        Right
    }

    /**
     * 全向移动方向枚举
     */
    public enum OmnidirectionalRun{
        /**
         * 向左
         */
        Leftward,
        /**
         * 向右
         */
        Rightward,
        /**
         * 向前
         */
        Forward,
        /**
         * 向后
         */
        Backward,
        /**
         * 左前
         */
        ForwardAndLeft,
        /**
         * 右前
         */
        ForwardAndRight,
        /**
         * 左后
         */
        BackwardAndLeft,
        /**
         * 右后
         */
        BackwardAndRight
    }
    /**
     * 联盟颜色枚举
     */
    public enum TeamColor{
        /**
         * 红色联盟
         */
        Red,
        /**
         * 蓝色联盟
         */
        Blue,
        /**
         * 表示未知或不显示联盟色
         */
        Unknown
    }

    /**
     * 灯色枚举
     */
    public enum LightColor{
        Red,Blue,Green,Unknown
    }

    /**
     * 当前操作模式枚举
     */
    public enum OpMode{
        /**
         * 手动模式
         */
        Manual,
        /**
         * 自动模式
         */
        Autonomous
    }

    /**
     * 全彩LED模块的颜色
     */
    public enum LEDColor {
        /**
         * 红色 只点亮 R 通道
         */
        Red,
        /**
         * 绿色 只点亮 G 通道
         */
        Green,
        /**
         * 红色 只点亮 B 通道
         */
        Blue,
        /**
         * 黄色 点亮 R 和 G 通道
         */
        Yellow,
        /**
         * 淡蓝色 点亮 R 和 B 通道
         */
        Cyan,
        /**
         * 紫色 点亮 G 和 B 通道
         */
        Purple,
        /**
         * 白色 RGB 模块完全点亮
         */
        White,
        /**
         * 关闭 不点亮任何通道
         */
        Off}
}
