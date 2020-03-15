package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    Locale l = Locale.getDefault();

    private int buttons = 4;
    private static int[] btnIDArray = new int[] {R.id.button1, R.id.button2, R.id.button3, R.id.button4};

    private TextView countdownTimerText;
    private static CountDownTimer countDownTimer;

    int COLOR_GREEN, COLOR_RED, COLOR_WHITE, COLOR_DGREY;

    Resources res;
    String[] qMain;
    TypedArray ta;
    String[][] qArr;

    Drawable BUTTON_COLOR_LGREEN, BUTTON_COLOR_GREY;
    TextView score, Title, Question;
    Button[] btn = new Button[buttons];
    FloatingActionButton[] btnh = new FloatingActionButton[3];

    boolean debugMode, qr;
    int questionInt, answers, scoreInt, timeInt, currentScore, qnum, qrnum;
    String lang;

    boolean[] help = new boolean[3];
    String[] helpStr = new String[] {"helpWH", "help50", "helpPH"};

    int[] btnMain = new int[buttons];

    String[] ans = new String[3];

    @Override
    public void onBackPressed () {
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Getting Resources
        res = getResources();
        qMain = res.getStringArray(R.array.qmain);
        ta = res.obtainTypedArray(R.array.qarr);
        qArr = new String[ta.length()][];
        for (int i = 0; i < ta.length(); ++i)
            if ((double)ta.getResourceId(i, 0) > 0)
                qArr[i] = res.getStringArray(ta.getResourceId(i, 0));
        ta.recycle();

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        qr = prefs.getBoolean("qr", qr);
        qnum = prefs.getInt("qnum", qnum);
        qrnum = prefs.getInt("qrnum", qrnum);
        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        answers = prefs.getInt("answers", answers);
        questionInt = prefs.getInt("questionInt", questionInt);
        lang = prefs.getString("lang", lang);
        for (int i = 0; i < help.length; i++) help[i] = prefs.getBoolean(helpStr[i], help[i]);

        COLOR_GREEN = res.getColor(R.color.colorGreen);
        COLOR_RED = res.getColor(R.color.colorRed);
        COLOR_WHITE = res.getColor(R.color.colorWhite);
        COLOR_DGREY = res.getColor(R.color.colorDGrey);
        BUTTON_COLOR_LGREEN = res.getDrawable(R.drawable.custombutton_lgreen);
        BUTTON_COLOR_GREY = res.getDrawable(R.drawable.custombutton_grey);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode) dmMsg.setVisibility(View.GONE);

        // Setting objects
        Title = findViewById(R.id.top);
        Question = findViewById(R.id.question);
        countdownTimerText = findViewById(R.id.countdownText);
        score = findViewById(R.id.score);
        for (int i = 0; i < buttons; i++) btn[i] = findViewById(btnIDArray[i]);

        btnh[0] = findViewById(R.id.fab1); // Help Wheel
        btnh[1] = findViewById(R.id.fab2); // Help 50
        btnh[2] = findViewById(R.id.fab3); // Help Phone

        currentScore = ((questionInt - 1) / 4 + 1) * 20; // Setting the Score
        if (qr) currentScore = 50;

        if (qArr[3][questionInt - 1].equals("EMPTY")) {
            buttons = 3;
            btn[3].setVisibility(View.GONE);
            if (qArr[2][questionInt - 1].equals("EMPTY")) {
                buttons = 2;
                btn[2].setVisibility(View.GONE);
            }
        }

        // Randomizing the button order
        for (int i = 0; i < buttons; i++) {
            int ir;
            do {
                Random r = new Random();
                double dr = r.nextDouble() * buttons;
                ir = (int)dr + 1;
            } while (ArrayUtils.contains(btnMain, ir));
            btnMain[i] = ir;
        }

        for (int i = 0; i < help.length; i++) setHelpButtonProps(i, help[i]);

        if (buttons != 4) {
            setHelpButtonProps(1, false);
            setHelpButtonProps(2, false);
        }

        String qs = res.getString(R.string.qText);
        String ps = res.getString(R.string.pText);
        ans = new String[] {res.getString(R.string.correct), res.getString(R.string.incorrect), res.getString(R.string.timesup)};
        if (lang.equals("English")) {
            qs = res.getString(R.string.qTextENG);
            ps = res.getString(R.string.pTextENG);
            ans = new String[] {res.getString(R.string.correct), res.getString(R.string.incorrect), res.getString(R.string.timesup)};
            score.setText(String.format(l, "%s %d", res.getString(R.string.pText2ENG), scoreInt));
        } else score.setText(String.format(l, "%d %s", scoreInt, ps));
        Title.setText(String.format(l, "%s %d - %d %s", qs, questionInt, currentScore, ps));
        Question.setText(qMain[questionInt - 1]);
        setButtonsText();

        startTimer();
    }

    public void answerButton (View view) {
        for (int i = 0; i < buttons; i++)
            if (view.getId() == btnIDArray[i])
                answer((btnMain[i] + 1) / 3 + 1);
    }

    public void help (View view) {
        // Disabling the help buttons
        for (int i = 0; i < 3; i++) setHelpButtonProps(i, false);

        int i = 0; // Checking which button was pressed

        switch (view.getId()) {
            case R.id.fab1: // Help Wheel
                answer(1);
                break;
            case R.id.fab2: // Help 50/50
                i = 1;
                setButtonProps(3, BUTTON_COLOR_GREY, false);
                setButtonProps(4, BUTTON_COLOR_GREY, false);
                break;
            case R.id.fab3: // Help Phone
                i = 2;
                Random d = new Random();
                int phoneHelpInt = (int)(d.nextDouble() * 100) / 15;

                if (phoneHelpInt > 2) phoneHelpInt = 2;
                phoneHelpInt = 3 - phoneHelpInt;

                setButtonProps(phoneHelpInt, BUTTON_COLOR_LGREEN, true);
                setButtonProps(4, BUTTON_COLOR_GREY, false);
                break;
        }

        // Making that button used in the SharedPrefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(helpStr[i], false);
        editor.apply();
    }

    private void startTimer () {
        countDownTimer = new CountDownTimer(((MainActivity.NUMBER_OF_SECONDS + 1) * 1000), 1000) {
            public void onTick (long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format(l, "%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                countdownTimerText.setText(hms);

                // Timer blinks red in the last 10 seconds
                if (Integer.parseInt(hms) <= 10 && Integer.parseInt(hms) % 2 == 0)
                    countdownTimerText.setTextColor(COLOR_RED);
                else countdownTimerText.setTextColor(COLOR_WHITE);
            }

            public void onFinish () {
                answer(3);
            }
        }.start();
    }

    private void setButtonsText () {
        for (int i = 0; i < buttons; i++)
            btn[i].setText(qArr[btnMain[i] - 1][questionInt - 1]);
    }

    private void setButtonProps (int buttonType, Drawable buttonColor, boolean setClickable) {
        // Checking all the Answer Buttons if they are the type requested.
        for (int i = 0; i < buttons; i++)
            if (btnMain[i] == buttonType) {
                btn[i].setBackground(buttonColor);
                btn[i].setClickable(setClickable);
            }
    }

    private void setHelpButtonProps (int buttonType, boolean setValue) {
        btnh[buttonType].setClickable(setValue); // Setting the help button value.
        if (!setValue) btnh[buttonType].setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
    }

    private void answer (int type) {

        // Setting the variables of the Time, Score, etc
        timeInt += (MainActivity.NUMBER_OF_SECONDS - Integer.parseInt(countdownTimerText.getText().toString()));

        if (countdownTimerText.getText().toString().equals(String.valueOf(MainActivity.NUMBER_OF_SECONDS)))
            timeInt++;

        questionInt++;
        scoreInt += currentScore * (1 - type / 2);
        answers += 1 - type / 2;

        // Saving those variables to the SharedPrefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionInt", questionInt);
        editor.putInt("scoreInt", scoreInt);
        editor.putInt("answers", answers);
        editor.apply();

        countDownTimer.cancel();

        // Disabling all of the buttons
        for (int i = 0; i < buttons; i++)
            btn[i].setClickable(false);
        for (int i = 0; i < help.length; i++)
            setHelpButtonProps(i, false);

        // Creating the custom Popup message
        View popupView = View.inflate(this, R.layout.popup_layout, null);

        PopupWindow pw = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setElevation(5.0f);
        pw.showAtLocation(findViewById(R.id.cl), Gravity.CENTER, 0, 0);

        // Setting the Text and the Text Color by the Answer
        TextView Message = popupView.findViewById(R.id.message);
        Message.setText(ans[type - 1]);
        int[] temp_color = new int[] {COLOR_GREEN, COLOR_RED};
        Message.setTextColor(temp_color[type / 2]);

        new Handler().postDelayed(() -> {
            if ((questionInt - 1) % qrnum == 0 && qr) // If QR is enabled & the next activity is a QR activity, go to the QR activity,
                startActivity(new Intent(Question.this, QRActivity.class));
            if (questionInt == qnum + 1) // If the question is the last Question GOTO the End screen
                startActivity(new Intent(Question.this, EndActivity.class));
            else
                startActivity(new Intent(Question.this, Question.class));
            finish();
        }, 1000);
    }

}