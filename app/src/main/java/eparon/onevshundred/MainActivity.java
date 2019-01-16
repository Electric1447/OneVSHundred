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

    int scoreInt;
    int timeInt;
    int questionInt;
    String answers;

    boolean helpWH, help50, helpPH;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionInt = prefs.getInt("questionInt", questionInt);
        answers = prefs.getString("answers", answers);
        helpWH = prefs.getBoolean("helpWH", helpWH);
        helpPH = prefs.getBoolean("helpPH", helpPH);
        help50 = prefs.getBoolean("help50", help50);

        TextView Welcome = findViewById(R.id.welcome);
        String appname = getResources().getString(R.string.app_name);
        Welcome.setText("ברוכים הבאים ל: " + appname + "!");

    }

    public void StartGame (View view){
        scoreInt = 0;
        timeInt = 0;
        questionInt = 1;
        answers = "";
        helpWH = true;
        helpPH = true;
        help50 = true;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("scoreInt", scoreInt);
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionInt", questionInt);
        editor.putString("answers", answers);
        editor.putBoolean("helpWH", helpWH);
        editor.putBoolean("helpPH", helpPH);
        editor.putBoolean("help50", help50);
        editor.apply();

        if(!skipInfo) {
            Intent q = new Intent(MainActivity.this, Info.class);
            startActivity(q);
        } else {
            Intent q = new Intent(MainActivity.this, Question.class);
            startActivity(q);
        }
    }

}
