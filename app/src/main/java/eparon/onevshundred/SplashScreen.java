package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    boolean debugMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);

        boolean skipInfo = false, qr = false;
        if (getResources().getString(R.string.skipInfo).equals("True"))
            skipInfo = true;
        if (getResources().getString(R.string.qr).equals("True"))
            qr = true;

        // Resetting the game variables
        SharedPreferences.Editor editor = prefs.edit();

        if (!debugMode) {
            editor.putInt("qnum", Integer.valueOf(getResources().getString(R.string.numberOfQuestions)));
            editor.putInt("qrnum", Integer.valueOf(getResources().getString(R.string.questionsPerQR)));
            editor.putString("lang", getResources().getString(R.string.Lang));
            editor.putBoolean("skipInfo", skipInfo);
            editor.putBoolean("qr", qr);
        }

        editor.putInt("scoreInt", 0);
        editor.putInt("timeInt", 0);
        editor.putInt("questionInt", 1);
        editor.putString("answers", "");
        editor.putBoolean("helpWH", true);
        editor.putBoolean("help50", true);
        editor.putBoolean("helpPH", true);

        editor.apply();

        Intent a = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(a);
    }

}
