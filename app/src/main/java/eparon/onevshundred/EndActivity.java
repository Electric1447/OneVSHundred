package eparon.onevshundred;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    Locale l = Locale.getDefault();

    boolean debugMode;
    int scoreInt, timeInt;
    String lang;
    String answers;

    boolean[] help = new boolean[3];
    String[] helpStr = new String[] {"helpWH", "help50", "helpPH"};

    int tScore = 0, timebonus = 0, hScore = 0, cAnswersI = 0;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        lang = prefs.getString("lang", lang);
        answers = prefs.getString("answers", answers);
        for (int i = 0; i < help.length; i++)
            help[i] = prefs.getBoolean(helpStr[i], help[i]);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode)
            dmMsg.setVisibility(View.GONE);

        String title = getResources().getString(R.string.endTitle);
        String score = getResources().getString(R.string.score);
        String et1 = getResources().getString(R.string.endText1);
        String et2 = getResources().getString(R.string.endText2);
        String et3 = getResources().getString(R.string.endText3);
        String time = getResources().getString(R.string.endTime);
        String eqs = getResources().getString(R.string.q);
        String ets = getResources().getString(R.string.bt);
        String ehs = getResources().getString(R.string.bh);
        String total = getResources().getString(R.string.total);
        if (lang.equals("English")) {
            title = getResources().getString(R.string.endTitleENG);
            score = getResources().getString(R.string.scoreENG);
            et1 = getResources().getString(R.string.endText1ENG);
            et2 = getResources().getString(R.string.endText2ENG);
            et3 = getResources().getString(R.string.endText3ENG);
            time = getResources().getString(R.string.endTimeENG);
            eqs = getResources().getString(R.string.qENG);
            ets = getResources().getString(R.string.btENG);
            ehs = getResources().getString(R.string.bhENG);
            total = getResources().getString(R.string.totalENG);
        }

        TextView Title = findViewById(R.id.view1);
        TextView Score = findViewById(R.id.score);
        TextView EndM = findViewById(R.id.endMessage);
        TextView TimeM = findViewById(R.id.timeMessage);
        TextView qScore = findViewById(R.id.questionScore);
        TextView timeScore = findViewById(R.id.timeScore);
        TextView helpScore = findViewById(R.id.helpScore);
        TextView totalScore = findViewById(R.id.totalScore);

        Title.setText(title);
        Score.setText(score);

        // Getting the answers from a String to an Integer Array.
        int[] answersArray = new int[answers.length()];
        for (int i = 0; i < answers.length(); i++) {
            answersArray[i] = (answers.charAt(i) - '0') / 2; // Converting timed-out to incorrect.
            cAnswersI += answersArray[i];
        }

        cAnswersI = answers.length() - cAnswersI; // Correct answers calculation
        double cAnswersD = (double)Math.round(((cAnswersI / (double)answers.length()) * 100) * 10d) / 10d; // Calculating the presentage of correct answers.

        EndM.setText(String.format(l,"%s %d %s %d %s (%s%%)", et1, cAnswersI, et2, answers.length(), et3, cAnswersD));

        int ti1000 = timeInt * 1000;
        String hms = String.format(l,"%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ti1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ti1000)), TimeUnit.MILLISECONDS.toSeconds(ti1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ti1000)));
        TimeM.setText(String.format("%s %s", time, hms));

        if (scoreInt != 0) { // Checking if the Score isn't zero
            timebonus = (answers.length() * MainActivity.NUMBER_OF_SECONDS - timeInt) / 3;

            if (help[0] && help[1] && help[2]) // Checking if Helps have been used
                hScore = (scoreInt + timebonus) / MainActivity.NO_HELPS_BONUS_PERCENTAGE;

            tScore = scoreInt + timebonus + hScore; // Calculating the total score
        }

        qScore.setText(String.format(l,"%s %d", eqs, scoreInt));
        timeScore.setText(String.format(l,"%s %d", ets, timebonus));
        helpScore.setText(String.format(l,"%s %d", ehs, hScore));
        totalScore.setText(String.format(l,"%s %d", total, tScore));
    }

}
