package org.lasarobotics.library.options;

import android.content.Context;
import android.view.View;


/**
 * Holds a set of options for a menu type
 */
public interface Category {
    //Random number to prevent id collision
    int ID_OFFSET = 102223;

    /**
     * Get the generated view for a certain category
     *
     * @param context Context object to help view creation
     * @return Constructed category view
     */
    View getView(Context context);

    /**
     * Get option selected after options menu is submitted
     *
     * @return String value corresponding to the selected option
     */
    String getResult();

    /**
     * Get category name
     *
     * @return Category Name
     */
    String getName();
}
