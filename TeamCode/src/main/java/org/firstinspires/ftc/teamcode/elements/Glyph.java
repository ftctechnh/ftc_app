package org.firstinspires.ftc.teamcode.elements;

/**
 * Wrapper enum for glyphs. Even though it only has two values, I felt that an enum would be more
 * expressive than just a boolean `isGray` or `isBrown`.
 * @author Blake Abel, ALex Migala, Nick Clifford
 * @since 9/10/17
 */

public enum Glyph {
    GRAY, BROWN;

    /**
     * @return The other Glyph color
     */
    public Glyph invert() {
        if(this == GRAY) return BROWN;
        if(this == BROWN) return GRAY;

        // Execution should never reach here.
        // This is just to make the compiler happy.
        throw new RuntimeException("Case not implemented");
    }
}
