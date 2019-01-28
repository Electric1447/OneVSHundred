package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    private static boolean skipInfo = false; // Skips the Info Screen
    public static int NUMBER_OF_SECONDS = 30; //
    public static int NO_HELPS_BONUS_PERCENTAGE = 10;
    public static boolean QR_ENABLED = false;
    public static int QUESTIONS_PER_QR = 5;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        TextView Welcome = findViewById(R.id.welcome);
        String appname = getResources().getString(R.string.app_name);
        Welcome.setText(String.format("ברוכים הבאים ל: %s!", appname));
    }

    public void StartGame (View view){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("scoreInt", 0);
        editor.putInt("timeInt", 0);
        editor.putInt("questionInt", 1);
        editor.putString("answers", "");
        //editor.putBoolean("helpUsed", false);
        editor.putBoolean("helpWH", true);
        editor.putBoolean("help50", true);
        editor.putBoolean("helpPH", true);
        editor.apply();

        Intent a = new Intent(MainActivity.this, Info.class);
        if(skipInfo)
            a = new Intent(MainActivity.this, Question.class);
        startActivity(a);
    }

}
