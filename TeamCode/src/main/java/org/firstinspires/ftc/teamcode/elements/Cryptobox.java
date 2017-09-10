package org.firstinspires.ftc.teamcode.elements;

import java.util.Arrays;

/**
 * Wrapper class for cryptoboxes (the things where you store the glyphs) + ciphers.
 * @author Blake Abel, Alex Migala, Nick Clifford
 * @since 9/10/17
 */

public class Cryptobox {
    private Glyph[][] box;

    /**
     * Array of glyph matrices that represent all valid ciphers.
     * Note that the inversions of these ciphers also are valid.
     */
    public static final Glyph[][][] CIPHERS = {
            // Frog
            {
                    {Glyph.GRAY, Glyph.BROWN, Glyph.GRAY},
                    {Glyph.BROWN, Glyph.GRAY, Glyph.BROWN},
                    {Glyph.GRAY, Glyph.BROWN, Glyph.GRAY},
                    {Glyph.BROWN, Glyph.GRAY, Glyph.BROWN}
            },
            // Bird
            {
                    {Glyph.GRAY, Glyph.BROWN, Glyph.GRAY},
                    {Glyph.BROWN, Glyph.GRAY, Glyph.BROWN},
                    {Glyph.BROWN, Glyph.GRAY, Glyph.BROWN},
                    {Glyph.GRAY, Glyph.BROWN, Glyph.GRAY},
            },
            // Snake
            {
                    {Glyph.BROWN, Glyph.GRAY, Glyph.GRAY},
                    {Glyph.BROWN, Glyph.BROWN, Glyph.GRAY},
                    {Glyph.GRAY, Glyph.BROWN, Glyph.BROWN},
                    {Glyph.GRAY, Glyph.GRAY, Glyph.BROWN}
            }
    };

    /**
     * Empty constructor. Sets inner box to all nulls.
     */
    public Cryptobox() {
        this.box = new Glyph[4][3];
    }

    /**
     * Cryptobox constructor
     * @param box Matrix containing all the glyphs
     */
    public Cryptobox(Glyph[][] box) {
        this.box = box;
    }

    /**
     * @return The matrix that holds all the glyphs
     */
    public Glyph[][] getBox() { return this.box; }

    /**
     * Returns the row of the cryptobox at the given index.
     * @param index The index corresponding to the row to return
     * @return The row at the given index
     * TODO: Add index bounds check
     */
    public Glyph[] getRow(int index) {
        return this.box[index];
    }

    /**
     * Returns the column of the cryptobox at the given index.
     * @param index The index corresponding to the column to return
     * @return The column at the given index
     * TODO: Add index bounds check
     */
    public Glyph[] getColumn(int index) {
        Glyph[] result = new Glyph[4];
        for(int i = 0; i < 4; i++) {
            result[i] = this.box[i][index];
        }
        return result;
    }

    /**
     * Sets a glyph in the cryptobox to another value.
     * @param row The row containing the value to change
     * @param column The column containing the value to change
     * @param glyph The new value
     * TODO: Add index bounds check
     */
    public void setGlyph(int row, int column, Glyph glyph) {
        this.box[row][column] = glyph;
    }

    /**
     * @return Whether the current state of the cryptobox represents a completed cipher
     */
    public boolean cipherCompleted() {
        for(Glyph[][] cipher : CIPHERS) {
            Glyph[][] inverted = new Glyph[4][3];
            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 3; j++) {
                    inverted[i][j] = cipher[i][j].invert();
                }
            }

            if(Arrays.deepEquals(this.box, cipher) || Arrays.deepEquals(this.box, inverted)) {
                return true;
            }
        }

        return false;
    }
}
