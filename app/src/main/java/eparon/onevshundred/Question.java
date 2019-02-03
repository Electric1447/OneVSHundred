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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    Locale l = Locale.getDefault();

    private static int noOfSeconds = (MainActivity.NUMBER_OF_SECONDS + 1) * 1000;
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

    int qnum, qrnum;
    String lang;
    boolean qr;

    int scoreInt, timeInt;
    int questionInt;
    String answers;
    String timeString;

    boolean[] help = new boolean[3];
    String[] helpStr = new String[] {"helpWH", "help50", "helpPH"};

    int[] btnMain = new int[buttons];

    int currentScore;

    String ac, aic, atu;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Getting Resources
        res = getResources();
        qMain = res.getStringArray(R.array.qmain);
        ta = res.obtainTypedArray(R.array.qarr);
        qArr = new String[ta.length()][];
        for (int i = 0; i < ta.length(); ++i) {
            int id = ta.getResourceId(i, 0);
            if (id > 0)
                qArr[i] = res.getStringArray(id);
        }
        ta.recycle();

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        qnum = prefs.getInt("qnum", qnum);
        qrnum = prefs.getInt("qrnum", qrnum);
        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionInt = prefs.getInt("questionInt", questionInt);
        qr = prefs.getBoolean("qr", qr);
        lang = prefs.getString("lang", lang);
        answers = prefs.getString("answers", answers);
        for (int i = 0; i < help.length; i++)
            help[i] = prefs.getBoolean(helpStr[i], help[i]);

        COLOR_GREEN = res.getColor(R.color.colorGreen);
        COLOR_RED = res.getColor(R.color.colorRed);
        COLOR_WHITE = res.getColor(R.color.colorWhite);
        COLOR_DGREY = res.getColor(R.color.colorDGrey);
        BUTTON_COLOR_LGREEN = res.getDrawable(R.drawable.custombutton_lgreen);
        BUTTON_COLOR_GREY = res.getDrawable(R.drawable.custombutton_grey);

        // Setting objects
        Title = findViewById(R.id.top);
        Question = findViewById(R.id.question);
        countdownTimerText = findViewById(R.id.countdownText);
        score = findViewById(R.id.score);

        for (int i = 0; i < buttons; i++)
            btn[i] = findViewById(btnIDArray[i]);

        btnh[0] = findViewById(R.id.fab1); // Help Wheel
        btnh[1] = findViewById(R.id.fab2); // Help 50
        btnh[2] = findViewById(R.id.fab3); // Help Phone

        currentScore = ((questionInt - 1) / 4 + 1) * 20; // Setting the Score
        if (qr)
            currentScore = 50;

        if (qArr[2][questionInt - 1].equals("EMPTY") && qArr[3][questionInt - 1].equals("EMPTY")) {
            buttons = 2;
            btn[2].setVisibility(View.GONE);
            btn[3].setVisibility(View.GONE);
        }
        if (!qArr[2][questionInt - 1].equals("EMPTY") && qArr[3][questionInt - 1].equals("EMPTY")) {
            buttons = 3;
            btn[3].setVisibility(View.GONE);
        }

        // Randomizing the button order
        for (int i = 0; i < buttons; i++) {
            int ir;
            do {
                Random r = new Random();
                double dr = r.nextDouble() * buttons;
                ir = (int) dr + 1;
            } while (ArrayUtils.contains(btnMain, ir));
            btnMain[i] = ir;
        }

        for (int i = 0; i < help.length; i++)
            setHelpButtonProps(i, help[i]);

        if (buttons != 4) {
            setHelpButtonProps(1, false);
            setHelpButtonProps(2, false);
        }

        String qs = res.getString(R.string.qText);
        String ps = res.getString(R.string.pText);
        ac = res.getString(R.string.correct);
        aic = res.getString(R.string.incorrect);
        atu = res.getString(R.string.timesup);
        if (lang.equals("English")) {
            qs = res.getString(R.string.qTextENG);
            ps = res.getString(R.string.pTextENG);
            ac = res.getString(R.string.correctENG);
            aic = res.getString(R.string.incorrectENG);
            atu = res.getString(R.string.timesupENG);
            score.setText(String.format(l,"%s %d", res.getString(R.string.pText2ENG), scoreInt));
        } else
            score.setText(String.format(l,"%d %s", scoreInt, ps));
        Title.setText(String.format(l,"%s %d - %d %s", qs, questionInt, currentScore, ps));
        Question.setText(qMain[questionInt - 1]);
        setButtonsText();

        startTimer();
    }

    public void answer (int type) {
        // Setting the variables of the Time, Score, etc
        timeString = countdownTimerText.getText().toString();
        timeInt += (noOfSeconds / 1000 - 1 - Integer.parseInt(timeString));
        questionInt++;
        answers += String.valueOf(type);
        scoreInt += currentScore * (1 - type / 2);

        // Saving those variables to the SharedPrefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionInt", questionInt);
        editor.putString("answers", answers);
        editor.putInt("scoreInt", scoreInt);
        editor.apply();

        countDownTimer.cancel();
        PopUp();
    }

    public void answerButton (View view) {
        for (int i = 0; i < buttons; i++)
            if (view.getId() == btnIDArray[i])
                answer((btnMain[i] + 1) / 3 + 1);
    }

    public void help (View view) {
        // Disabling the help buttons
        for (int i = 0; i < 3; i++)
            setHelpButtonProps(i, false);

        // Checking which button was pressed
        int i = 0;
        if (view.getId() == R.id.fab2) i = 1;
        if (view.getId() == R.id.fab3) i = 2;

        // Making that button used in the SharedPrefs
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(helpStr[i], false);
        editor.apply();

        switch (i) {
            case 0:
                // Help Wheel
                answer(1);
                break;
            case 1:
                // Help 50/50
                setButtonProps(3, BUTTON_COLOR_GREY, false);
                setButtonProps(4, BUTTON_COLOR_GREY, false);
                break;
            case 2:
                // Help Phone
                Random d = new Random();
                int phoneHelpInt = (int)(d.nextDouble() * 100) / 15;

                if (phoneHelpInt > 2)
                    phoneHelpInt = 2;

                phoneHelpInt = 3 - phoneHelpInt;

                setButtonProps(phoneHelpInt, BUTTON_COLOR_LGREEN, true);
                setButtonProps(4, BUTTON_COLOR_GREY, false);
                break;
        }
    }

    private void startTimer () {
        countDownTimer = new CountDownTimer(noOfSeconds, 1000) {
            public void onTick (long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format(l, "%02d",TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                countdownTimerText.setText(hms);

                // Timer blinks red in the last 10 seconds
                if (Integer.parseInt(hms) <= 10 && Integer.parseInt(hms) % 2 == 0)
                    countdownTimerText.setTextColor(COLOR_RED);
                else
                    countdownTimerText.setTextColor(COLOR_WHITE);
            }
            public void onFinish () { answer(3); }
        }.start();
    }

    private void setButtonsText () {
        for (int i = 0; i < buttons; i++)
            btn[i].setText(qArr[btnMain[i] - 1][questionInt - 1]);
    }

    public void setButtonProps (int buttonType, Drawable buttonColor, boolean setClickable) {
        for (int i = 0; i < buttons; i++) { // Checking all the Answer Buttons if they are the type requested.
            if (btnMain[i] == buttonType) {
                btn[i].setBackground(buttonColor);
                btn[i].setClickable(setClickable);
            }
        }
    }

    public void setHelpButtonProps (int buttonType, boolean setValue) {
        btnh[buttonType].setClickable(setValue); // Setting the help button value.
        if (!setValue)
            btnh[buttonType].setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
    }

    public void PopUp () {

        // Disabling all of the buttons
        for (int i = 0; i < buttons; i++)
            btn[i].setClickable(false);
        for (int i = 0; i < help.length; i++)
            setHelpButtonProps(i, false);

        // Creating the custom Popup message
        View customView = View.inflate(this, R.layout.popup_layout,null);

        PopupWindow myPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPopupWindow.setElevation(5.0f);
        myPopupWindow.showAtLocation(findViewById(R.id.cl), Gravity.CENTER,0,0);

        TextView Message = customView.findViewById(R.id.message);

        // Setting the Text and the Text Color by the Answer
        switch (answers.charAt(questionInt - 2) - '0') {
            case 1:
                Message.setText(ac);
                Message.setTextColor(COLOR_GREEN);
                break;
            case 2:
                Message.setText(aic);
                Message.setTextColor(COLOR_RED);
                break;
            case 3:
                Message.setText(atu);
                Message.setTextColor(COLOR_RED);
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                Intent a = new Intent(Question.this, Question.class);
                if ((questionInt - 1) % qrnum == 0 && qr) // If QR is enabled & the next activity is a QR activity, go to the QR activity,
                    a = new Intent(Question.this, QRActivity.class);
                if (questionInt == qnum + 1) // If the question is the last Question GOTO the End screen
                    a = new Intent(Question.this, EndActivity.class);
                startActivity(a);
                finish();
            }
        }, 1000);
    }

}