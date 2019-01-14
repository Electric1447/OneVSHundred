package eparon.onevshundred;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    TextView EndM;
    TextView ScoreM;
    TextView TimeM;

    int scoreInt;
    int timeInt;
    String questionsString;
    int questionnInt;

    int cAnswersI = 0;
    double cAnswersD;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionsString = prefs.getString("questionsString", questionsString);
        questionnInt = prefs.getInt("questionnInt", questionnInt);

        EndM = findViewById(R.id.endMessage);
        ScoreM = findViewById(R.id.scoreMessage);
        TimeM = findViewById(R.id.timeMessage);

        int[] answersArray = new int[questionsString.length()];
        for (int i = 0; i < questionsString.length(); i++) {
            answersArray[i] = questionsString.charAt(i) - '0';
            if (answersArray[i] == 3){
                answersArray[i] = 2;
            }
            answersArray[i] -= 1;
            cAnswersI += answersArray[i];
        }

        cAnswersI = questionsString.length() - cAnswersI;

        cAnswersD = (cAnswersI / (double)questionsString.length()) * 100;
        cAnswersD = (double)Math.round(cAnswersD * 10d) / 10d;

        EndM.setText("עניתם נכון על " + cAnswersI + " מתוך " + questionsString.length() + " שאלות (" + cAnswersD + "%)");

        int ti1000 = timeInt * 1000;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ti1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ti1000)), TimeUnit.MILLISECONDS.toSeconds(ti1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ti1000)));
        TimeM.setText("זמן: " + hms);

        int timebonus = (questionsString.length() * 30 - timeInt) / 3 ;
        int totalScore = scoreInt + timebonus;
        ScoreM.setText("שאלות: " + scoreInt + "\n" + "בונוס זמן: " + timebonus + "\n" + "סכ\"ה: " + totalScore);

    }

}
