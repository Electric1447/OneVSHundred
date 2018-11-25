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

        SharedPreferences scorei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences timei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences questions = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences questionn = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = scorei.getInt("scoreInt", scoreInt);
        timeInt = timei.getInt("timeInt", timeInt);
        questionsString = questions.getString("questionsString", questionsString);
        questionnInt = questionn.getInt("questionnInt", questionnInt);
        helpw = help.getInt("helpw", helpw);
        helpp = help.getInt("helpp", helpp);
        help50 = help.getInt("help50", help50);

        TextView Welcome = findViewById(R.id.welcome);
        String appname = getResources().getString(R.string.app_name);
        Welcome.setText("ברוכים הבאים ל: " + appname + "!");

    }

    public void StartGame (View view){
        scoreInt = 0;
        SharedPreferences scorei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = scorei.edit();
        editor1.putInt("scoreInt", scoreInt);
        editor1.apply();
        timeInt = 0;
        SharedPreferences timei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = timei.edit();
        editor2.putInt("timeInt", timeInt);
        editor2.apply();
        questionnInt = 1;
        SharedPreferences questionn = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = questionn.edit();
        editor3.putInt("questionnInt", questionnInt);
        editor3.apply();
        questionsString = "";
        SharedPreferences questions = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor4 = questions.edit();
        editor4.putString("questionsString", questionsString);
        editor4.apply();

        helpw = 0;
        helpp = 0;
        help50 = 0;
        SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor5 = help.edit();
        editor5.putInt("helpw", helpw);
        editor5.putInt("helpp", helpp);
        editor5.putInt("help50", help50);
        editor5.apply();

        Intent q = new Intent(MainActivity.this, Info.class);
        startActivity(q);
    }

}
