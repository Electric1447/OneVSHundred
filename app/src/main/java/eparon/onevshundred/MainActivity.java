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

    int scoreInt;
    int timeInt;
    String questionsString;
    int questionnInt;

    int helpw;
    int help50;
    int helpp;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionsString = prefs.getString("questionsString", questionsString);
        questionnInt = prefs.getInt("questionnInt", questionnInt);
        helpw = prefs.getInt("helpw", helpw);
        helpp = prefs.getInt("helpp", helpp);
        help50 = prefs.getInt("help50", help50);

        TextView Welcome = findViewById(R.id.welcome);
        String appname = getResources().getString(R.string.app_name);
        Welcome.setText("ברוכים הבאים ל: " + appname + "!");

    }

    public void StartGame (View view){
        scoreInt = 0;
        timeInt = 0;
        questionnInt = 1;
        questionsString = "";
        helpw = 0;
        helpp = 0;
        help50 = 0;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("scoreInt", scoreInt);
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionnInt", questionnInt);
        editor.putString("questionsString", questionsString);
        editor.putInt("helpw", helpw);
        editor.putInt("helpp", helpp);
        editor.putInt("help50", help50);
        editor.apply();


        Intent q = new Intent(MainActivity.this, Info.class);
        startActivity(q);
    }

}
