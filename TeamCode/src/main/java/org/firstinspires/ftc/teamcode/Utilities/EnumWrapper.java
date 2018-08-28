package org.firstinspires.ftc.teamcode.Utilities;

/**
 * Created by ablauch on 7/2018.
 *
 * this class used to easily move through an enumeration
 *
 * Example:
 *     public enum NewEnumList implements EnumWrapper<NewEnumList> {ITEM1, ITEM2, ITEM3;}
 */

public interface EnumWrapper<E extends Enum<E>> {
    int ordinal();

    /* returns next element in enumeration, return last element if at the end */
    default E getNext() {
        return getNext(false);
    }

    /* returns next element in enumeration, return first element if at the end and parameter is true */
    default E getNext(boolean wrap_around) {
        E[] ies = (E[]) this.getClass().getEnumConstants();
        if (wrap_around)
            return (this.ordinal() < ies.length - 1) ? ies[this.ordinal() + 1] : ies[0];
        else
            return (this.ordinal() < ies.length - 1) ? ies[this.ordinal() + 1] : ies[this.ordinal()];
    }

    /* returns previous element in enumeration, return first element if at the beginning */
    default E getPrev() {
        return getPrev(false);
    }

    /* returns previous element in enumeration, return last element if at the beginning and parameter is true */
    default E getPrev(boolean wrap_around) {
        E[] ies = (E[]) this.getClass().getEnumConstants();
        if (wrap_around)
            return (this.ordinal() > 0) ? ies[this.ordinal() - 1] : ies[ies.length - 1];
        else
            return (this.ordinal() > 0) ? ies[this.ordinal() - 1] : ies[this.ordinal()];
    }

    /* returns first element in enumeration */
    default E getFirst() {
        E[] ies = (E[]) this.getClass().getEnumConstants();
        return ies[0];
    }

    /* returns last element in enumeration */
    default E getLast() {
        E[] ies = (E[]) this.getClass().getEnumConstants();
        return ies[ies.length - 1];
    }
}
