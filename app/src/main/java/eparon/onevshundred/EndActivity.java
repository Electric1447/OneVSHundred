package eparon.onevshundred;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    Locale l = Locale.getDefault();

    boolean debugMode;
    int questionInt, answers, scoreInt, timeInt;
    String lang;

    boolean[] help = new boolean[3];
    String[] helpStr = new String[]{"helpWH", "help50", "helpPH"};

    int tScore = 0, timeBonus = 0, hScore = 0;

    @Override
    public void onBackPressed () {
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        answers = prefs.getInt("answers", answers);
        questionInt = prefs.getInt("questionInt", questionInt);
        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        lang = prefs.getString("lang", lang);
        for (int i = 0; i < help.length; i++) help[i] = prefs.getBoolean(helpStr[i], help[i]);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode) dmMsg.setVisibility(View.GONE);

        String title = getResources().getString(R.string.endTitle);
        String score = getResources().getString(R.string.score);
        String time = getResources().getString(R.string.endTime);
        String total = getResources().getString(R.string.total);
        String[] et = new String[]{getResources().getString(R.string.endText1), getResources().getString(R.string.endText2), getResources().getString(R.string.endText3)};
        String[] es = new String[]{getResources().getString(R.string.q), getResources().getString(R.string.bt), getResources().getString(R.string.bh)};
        if (lang.equals("English")) {
            title = getResources().getString(R.string.endTitleENG);
            score = getResources().getString(R.string.scoreENG);
            time = getResources().getString(R.string.endTimeENG);
            total = getResources().getString(R.string.totalENG);
            et = new String[]{getResources().getString(R.string.endText1ENG), getResources().getString(R.string.endText2ENG), getResources().getString(R.string.endText3ENG)};
            es = new String[]{getResources().getString(R.string.qENG), getResources().getString(R.string.btENG), getResources().getString(R.string.bhENG)};
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

        double cAnswersD = (double)Math.round(((answers / (double)(questionInt - 1)) * 100) * 10d) / 10d; // Calculating the percentage of correct answers.

        EndM.setText(String.format(l, "%s %d %s %d %s (%s%%)", et[0], answers, et[1], (questionInt - 1), et[2], cAnswersD));

        String hms = String.format(l, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInt * 1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInt * 1000)),
                TimeUnit.MILLISECONDS.toSeconds(timeInt * 1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInt * 1000)));
        TimeM.setText(String.format("%s %s", time, hms));

        // Checking if the Score isn't zero
        if (scoreInt != 0) {
            timeBonus = ((questionInt - 1) * MainActivity.NUMBER_OF_SECONDS - timeInt) / 3;

            // Checking if Helps have been used
            if (help[0] && help[1] && help[2]) hScore = (scoreInt + timeBonus) / MainActivity.NO_HELPS_BONUS_PERCENTAGE;

            tScore = scoreInt + timeBonus + hScore; // Calculating the total score
        }

        qScore.setText(String.format(l, "%s %d", es[0], scoreInt));
        timeScore.setText(String.format(l, "%s %d", es[1], timeBonus));
        helpScore.setText(String.format(l, "%s %d", es[2], hScore));
        totalScore.setText(String.format(l, "%s %d", total, tScore));
    }

}