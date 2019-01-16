package eparon.onevshundred;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    TextView EndM, TimeM, totalScore, qScore, timeScore, helpScore;

    int scoreInt;
    int timeInt;
    String answers;

    int cAnswersI = 0;
    double cAnswersD;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        answers = prefs.getString("answers", answers);

        EndM = findViewById(R.id.endMessage);
        TimeM = findViewById(R.id.timeMessage);
        totalScore = findViewById(R.id.totalScore);
        qScore = findViewById(R.id.questionScore);
        timeScore = findViewById(R.id.timeScore);
        helpScore = findViewById(R.id.helpScore);

        int[] answersArray = new int[answers.length()];
        for (int i = 0; i < answers.length(); i++) {
            answersArray[i] = answers.charAt(i) - '0';
            answersArray[i] /= 2;
            cAnswersI += answersArray[i];
        }

        cAnswersI = answers.length() - cAnswersI;

        cAnswersD = (cAnswersI / (double)answers.length()) * 100;
        cAnswersD = (double)Math.round(cAnswersD * 10d) / 10d;

        EndM.setText("עניתם נכון על " + cAnswersI + " מתוך " + answers.length() + " שאלות (" + cAnswersD + "%)");

        int ti1000 = timeInt * 1000;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ti1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ti1000)), TimeUnit.MILLISECONDS.toSeconds(ti1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ti1000)));
        TimeM.setText("זמן: " + hms);

        int timebonus = (answers.length() * 30 - timeInt) / 3 ;
        int tScore = scoreInt + timebonus;
        qScore.setText("שאלות: " + scoreInt);
        timeScore.setText("בונוס זמן: " + timebonus);
        helpScore.setText("בונוס עזרות: " + 0);
        totalScore.setText("סכ\"ה: " + tScore);

    }

}
