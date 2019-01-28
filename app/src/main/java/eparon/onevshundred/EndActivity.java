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

    int scoreInt, timeInt;
    String answers;

    boolean[] help = new boolean[3];
    String[] helpStr = new String[] {"helpWH", "help50", "helpPH"};

    int cAnswersI = 0;
    double cAnswersD;

    int tScore = 0, timebonus = 0, hScore = 0;

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
        for (int i = 0; i < help.length; i++)
            help[i] = prefs.getBoolean(helpStr[i], help[i]);

        EndM = findViewById(R.id.endMessage);
        TimeM = findViewById(R.id.timeMessage);
        qScore = findViewById(R.id.questionScore);
        timeScore = findViewById(R.id.timeScore);
        helpScore = findViewById(R.id.helpScore);
        totalScore = findViewById(R.id.totalScore);

        int[] answersArray = new int[answers.length()];
        for (int i = 0; i < answers.length(); i++) {
            answersArray[i] = (answers.charAt(i) - '0') / 2;
            cAnswersI += answersArray[i];
        }

        cAnswersI = answers.length() - cAnswersI;
        cAnswersD = (double)Math.round(((cAnswersI / (double)answers.length()) * 100) * 10d) / 10d;

        EndM.setText("עניתם נכון על " + cAnswersI + " מתוך " + answers.length() + " שאלות (" + cAnswersD + "%)");

        int ti1000 = timeInt * 1000;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ti1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ti1000)), TimeUnit.MILLISECONDS.toSeconds(ti1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ti1000)));
        TimeM.setText("זמן: " + hms);

        if (scoreInt != 0) {
            timebonus = (answers.length() * 30 - timeInt) / 3;

            if (help[0] && help[1] && help[2])
                hScore = (scoreInt + timebonus) / MainActivity.NO_HELPS_BONUS_PERCENTAGE;

            tScore = scoreInt + timebonus + hScore;
        }

        qScore.setText("שאלות: " + scoreInt);
        timeScore.setText("בונוס זמן: " + timebonus);
        helpScore.setText("בונוס עזרות: " + hScore);
        totalScore.setText("סכ\"ה: " + tScore);

    }

}
