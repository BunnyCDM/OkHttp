package com.example.sample;

import android.util.Log;

/**
 * Created by mac on 2017/10/6.
 */

public class L {

    private static final String TAG=L.class.getSimpleName();

    private static boolean debug=true;

    public static void e(String msg)
    {
        if(debug)
            Log.e(TAG, msg);
    }
}
