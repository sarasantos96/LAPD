package com.getout.google;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleGlobals {
    public static GoogleSignInOptions options;
    public static GoogleSignInClient client;
    public static GoogleSignInAccount account;

    public static int RC_SIGNIN = 9001;
    public static String DIRECTION_API_KEY = "AIzaSyCG43OULBmiUn6RvLOvcPrY83nwU1mI3lM";
}
