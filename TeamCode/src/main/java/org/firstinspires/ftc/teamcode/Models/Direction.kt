package org.firstinspires.ftc.teamcode.Models

enum class Direction(val intRepr: Int) {
    FORWARD(1),
    BACKWARD(-1),
    SPIN_CW(1),
    SPIN_CCW(-1),
    FORWARD_LEFT(1),
    FORWARD_RIGHT(-1),
    BACKWARD_LEFT(1),
    BACKWARD_RIGHT(-1)
}