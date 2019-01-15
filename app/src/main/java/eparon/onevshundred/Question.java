package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.os.CountDownTimer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    private static int SPLASH_TIME_OUT = 1000;
    private static int noOfMinutes = 31000;

    private TextView countdownTimerText;
    private static CountDownTimer countDownTimer;

    int COLOR_GREEN = 0xFF00FF00;
    int COLOR_RED = 0xFFFF0000;
    int COLOR_WHITE = 0xFFFFFFFF;
    int COLOR_DGREY = 0xFF666666;

    Resources res;
    String[] qMain;
    String[] qA, qB, qC, qD;

    Drawable colorLGreen, colorGrey;
    TextView score, Title, Question;
    Button btn1, btn2, btn3, btn4;
    FloatingActionButton btnhw, btnh50, btnhp;

    int scoreInt;
    int timeInt;
    int questionnInt;
    String questionsString;
    String timeString;

    boolean helpWH, help50, helpPH;
    boolean helpAlreadyUsed = false;

    int[] btnMain = new int[4];

    int currentScore;
    int lastQuestion;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        res = getResources();
        qMain = res.getStringArray(R.array.qmain);
        qA = res.getStringArray(R.array.qa);
        qB = res.getStringArray(R.array.qb);
        qC = res.getStringArray(R.array.qc);
        qD = res.getStringArray(R.array.qd);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionnInt = prefs.getInt("questionnInt", questionnInt);
        questionsString = prefs.getString("questionsString", questionsString);
        helpWH = prefs.getBoolean("helpWH", helpWH);
        helpPH = prefs.getBoolean("helpPH", helpPH);
        help50 = prefs.getBoolean("help50", help50);

        colorLGreen = getResources().getDrawable(R.drawable.custombutton_lgreen);
        colorGrey = getResources().getDrawable(R.drawable.custombutton_grey);

        Title = findViewById(R.id.top);
        Question = findViewById(R.id.question);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);

        btnhw = findViewById(R.id.fab1);
        btnh50 = findViewById(R.id.fab2);
        btnhp = findViewById(R.id.fab3);

        currentScore = ((questionnInt - 1) / 4 + 1) * 20;

        countdownTimerText = findViewById(R.id.countdownText);
        score = findViewById(R.id.score);

        for (int i = 0; i < 4; i++){
            Random r = new Random();
            double dr = r.nextDouble() * 4;
            int ir = (int)dr + 1;
            while (ir == btnMain[0] || ir == btnMain[1] || ir == btnMain[2] || ir == btnMain[3]) {
                dr = r.nextDouble() * 4;
                ir = (int) dr + 1;
            }
            btnMain[i] = ir;
        }

        Title.setText("שאלה " + questionnInt + " - " + currentScore + " נקודות");
        Question.setText(qMain[questionnInt - 1]);
        btn1.setText(ButtonText(btnMain[0]));
        btn2.setText(ButtonText(btnMain[1]));
        btn3.setText(ButtonText(btnMain[2]));
        btn4.setText(ButtonText(btnMain[3]));

        score.setText(scoreInt + " נקודות");

        btnhw.setClickable(helpWH);
        btnh50.setClickable(help50);
        btnhp.setClickable(helpPH);
        if(!helpWH)
            btnhw.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        if(!help50)
            btnh50.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        if(!helpPH)
            btnhp.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));


        startTimer();
    }

    public void answer(int type){
        String answerString = String.valueOf(type);
        timeString = countdownTimerText.getText().toString();
        timeInt += (noOfMinutes / 1000 - 1 - Integer.parseInt(timeString));
        questionnInt += 1;
        questionsString += answerString;
        scoreInt += currentScore * (1 - type / 2);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionnInt", questionnInt);
        editor.putString("questionsString", questionsString);
        editor.putInt("scoreInt", scoreInt);
        editor.apply();

        countDownTimer.cancel();
        PopUp();
    }

    public void answerButton1 (View view){ answer((btnMain[0] + 1) / 3 + 1); }

    public void answerButton2 (View view){ answer((btnMain[1] + 1) / 3 + 1); }

    public void answerButton3 (View view){ answer((btnMain[2] + 1) / 3 + 1); }

    public void answerButton4 (View view){ answer((btnMain[3] + 1) / 3 + 1); }

    public void helpWheel (View view){
        if(!helpAlreadyUsed) {

            useHelp();

            helpWH = false;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("helpWH", helpWH);
            editor.apply();

            answer(1);
        }
    }

    public void help50 (View view){
        if(!helpAlreadyUsed) {
            setButtonValue(3, colorGrey, false);
            setButtonValue(4, colorGrey, false);

            useHelp();

            help50 = false;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("help50", help50);
            editor.apply();
        }
    }

    public void helpPhone (View view){
        if(!helpAlreadyUsed) {
            Random d = new Random();
            double phoneHelpDouble = d.nextDouble();
            phoneHelpDouble *= 100;
            int phoneHelpInt = (int) phoneHelpDouble;
            phoneHelpInt /= 15;

            if(phoneHelpInt > 2)
                phoneHelpInt = 2;

            phoneHelpInt = 3 - phoneHelpInt;

            setButtonValue(phoneHelpInt, colorLGreen, true);
            setButtonValue(4, colorGrey, false);

            useHelp();

            helpPH = false;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("helpPH", helpPH);
            editor.apply();
        }
    }

    public void useHelp (){
        helpAlreadyUsed = true;
        btnhw.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        btnhw.setClickable(false);
        btnh50.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        btnh50.setClickable(false);
        btnhp.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        btnhp.setClickable(false);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                countdownTimerText.setText(hms);
                int hmsInt = Integer.parseInt(hms);

                if (hmsInt <= 10 && hmsInt % 2 == 0)
                    countdownTimerText.setTextColor(COLOR_RED);
                else
                    countdownTimerText.setTextColor(COLOR_WHITE);
            }

            public void onFinish() {
                answer(3);
            }
        }.start();
    }

    private String ButtonText (int btnInt){
        String returnStr = "";
        switch (btnInt){
            case 1:
                returnStr = qA[questionnInt - 1];
                break;
            case 2:
                returnStr = qB[questionnInt - 1];
                break;
            case 3:
                returnStr = qC[questionnInt - 1];
                break;
            case 4:
                returnStr = qD[questionnInt - 1];
                break;
        }

        return returnStr;
    }

    public void setButtonValue (int buttonType, Drawable color, boolean setClickable){
        if (btnMain[0] == buttonType){
            btn1.setBackground(color);
            btn1.setClickable(setClickable);
        }
        if (btnMain[1] == buttonType){
            btn2.setBackground(color);
            btn2.setClickable(setClickable);
        }
        if (btnMain[2] == buttonType){
            btn3.setBackground(color);
            btn3.setClickable(setClickable);
        }
        if (btnMain[3] == buttonType){
            btn4.setBackground(color);
            btn4.setClickable(setClickable);
        }
    }

    public void PopUp (){

        btn1.setClickable(false);
        btn2.setClickable(false);
        btn3.setClickable(false);
        btn4.setClickable(false);
        btnhw.setClickable(false);
        btnh50.setClickable(false);
        btnhp.setClickable(false);

        CoordinatorLayout mCoordinatorLayout = findViewById(R.id.cl);
        Context mContext = getApplicationContext();
        PopupWindow aPopupWindow;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.popup_layout,null);

        aPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        aPopupWindow.setElevation(5.0f);
        aPopupWindow.showAtLocation(mCoordinatorLayout, Gravity.CENTER,0,0);

        TextView Message = customView.findViewById(R.id.message);

        int[] answersArray = new int[questionsString.length()];
        for (int i = 0; i < questionsString.length(); i++) {
            answersArray[i] = questionsString.charAt(i) - '0';
        }

        String noq = res.getString(R.string.numberOfQuestions);
        lastQuestion = Integer.parseInt(noq) + 1;

        switch (answersArray[questionnInt - 2]) {
            case 1:
                Message.setText("נכון!");
                Message.setTextColor(COLOR_GREEN);
                break;
            case 2:
                Message.setText("לא נכון!");
                Message.setTextColor(COLOR_RED);
                break;
            case 3:
                Message.setText("עבר הזמן!");
                Message.setTextColor(COLOR_RED);
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(questionnInt == lastQuestion){
                    Intent a = new Intent(Question.this, EndActivity.class);
                    startActivity(a);
                } else {
                    Intent a = new Intent(Question.this, Question.class);
                    startActivity(a);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }


}