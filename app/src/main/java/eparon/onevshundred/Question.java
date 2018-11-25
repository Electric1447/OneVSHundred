package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";

    private TextView countdownTimerText;
    private static CountDownTimer countDownTimer;

    Drawable colorLGreen;
    Drawable colorGrey;

    TextView score;

    TextView Title;
    TextView Question;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    FloatingActionButton btnhw;
    FloatingActionButton btnh50;
    FloatingActionButton btnhp;

    int scoreInt;
    int timeInt;
    String questionsString;
    int questionnInt;

    int helpw;
    int help50;
    int helpp;

    int helpInt;

    String timeString;

    int btnMain;
    int btnA;
    int btnB;
    int btnC;
    int btnD;

    int currentScore;

    int lastQuestion;
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Resources res = getResources();
        String[] qMain = res.getStringArray(R.array.qmain);
        String[] qA = res.getStringArray(R.array.qa);
        String[] qB = res.getStringArray(R.array.qb);
        String[] qC = res.getStringArray(R.array.qc);
        String[] qD = res.getStringArray(R.array.qd);

        SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences scorei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences timei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences questions = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences questionn = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = scorei.getInt("scoreInt", scoreInt);
        timeInt = timei.getInt("timeInt", timeInt);
        questionsString = questions.getString("questionsString", questionsString);
        questionnInt = questionn.getInt("questionnInt", questionnInt);
        helpw = help.getInt("helpw", helpw);
        helpp = help.getInt("helpp", helpp);
        help50 = help.getInt("help50", help50);

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

        switch(questionnInt){
            case 1:
                btnMain = 1234;
                break;
            case 2:
                btnMain = 4213;
                break;
            case 3:
                btnMain = 2143;
                break;
            case 4:
                btnMain = 1234;
                break;
            case 5:
                btnMain = 4231;
                break;
            case 6:
                btnMain = 1234;
                break;
            case 7:
                btnMain = 2341;
                break;
            case 8:
                btnMain = 4321;
                break;
            case 9:
                btnMain = 1234;
                break;
            case 10:
                btnMain = 1432;
                break;
            case 11:
                btnMain = 1243;
                break;
            case 12:
                btnMain = 3214;
                break;
            case 13:
                btnMain = 2413;
                break;
            case 14:
                btnMain = 2431;
                break;
            case 15:
                btnMain = 2143;
                break;
            case 16:
                btnMain = 1234;
                break;
            case 17:
                btnMain = 1234;
                break;
            case 18:
                btnMain = 1234;
                break;
            case 19:
                btnMain = 1234;
                break;
            case 20:
                btnMain = 1234;
                break;
        }

        Title.setText("שאלה " + questionnInt + " - " + currentScore + " נקודות");
        Question.setText(qMain[questionnInt - 1]);
        btn1.setText(qA[questionnInt - 1]);
        btn2.setText(qB[questionnInt - 1]);
        btn3.setText(qC[questionnInt - 1]);
        btn4.setText(qD[questionnInt - 1]);

        btnA = btnMain / 1000;
        btnB = (btnMain / 100) - (btnA * 10);
        btnC = (btnMain / 10) - (btnA * 100 + btnB * 10);
        btnD = btnMain - (btnA * 1000 + btnB * 100 + btnC * 10);

        score.setText(scoreInt + " נקודות");

        if(helpw == 1){
            btnhw.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnhw.setClickable(false);
        }
        if(help50 == 1){
            btnh50.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnh50.setClickable(false);
        }
        if(helpp == 1){
            btnhp.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnhp.setClickable(false);
        }

        int noOfMinutes = 30000;
        startTimer(noOfMinutes);
    }

    public void answer(int type){
        String answerString = String.valueOf(type);
        timeString = countdownTimerText.getText().toString();
        timeInt += (31 - Integer.parseInt(timeString));
        SharedPreferences timei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = timei.edit();
        editor1.putInt("timeInt", timeInt);
        editor1.apply();
        questionnInt += 1;
        SharedPreferences questionn = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = questionn.edit();
        editor2.putInt("questionnInt", questionnInt);
        editor2.apply();
        questionsString += answerString;
        SharedPreferences questions = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = questions.edit();
        editor3.putString("questionsString", questionsString);
        editor3.apply();
        scoreInt += currentScore * (1 - type / 2);
        SharedPreferences scorei = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor4 = scorei.edit();
        editor4.putInt("scoreInt", scoreInt);
        editor4.apply();
        countDownTimer.cancel();
        PopUp();
    }

    public void answerButton1 (View view){
        answer((btnA + 1) / 3 + 1);
    }

    public void answerButton2 (View view){
        answer((btnB + 1) / 3 + 1);
    }

    public void answerButton3 (View view){
        answer((btnC + 1) / 3 + 1);
    }

    public void answerButton4 (View view){
        answer((btnD + 1) / 3 + 1);
    }

    public void helpWheel (View view){
        if(helpInt != 1) {

            helpInt = 1;

            btnhw.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnhw.setClickable(false);

            helpw = 1;
            SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = help.edit();
            editor1.putInt("helpw", helpw);
            editor1.apply();

            answer(1);
        }
    }

    public void help50 (View view){
        if(helpInt != 1) {
            setButtonValue(3, colorGrey, 1);
            setButtonValue(4, colorGrey, 1);

            helpInt = 1;

            btnh50.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnh50.setClickable(false);

            help50 = 1;
            SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = help.edit();
            editor1.putInt("help50", help50);
            editor1.apply();
        }
    }

    public void helpPhone (View view){
        if(helpInt != 1) {
            Random d = new Random();
            double phoneHelpDouble = d.nextDouble();
            phoneHelpDouble *= 100;
            int phoneHelpInt = (int) phoneHelpDouble;
            phoneHelpInt /= 15;
            if(phoneHelpInt > 2){
                phoneHelpInt = 2;
            }
            phoneHelpInt = 3 - phoneHelpInt;

            setButtonValue(phoneHelpInt, colorLGreen, 0);

            setButtonValue(4, colorGrey, 1);

            helpInt = 1;

            btnhp.setBackgroundTintList(ColorStateList.valueOf(0xFF666666));
            btnhp.setClickable(false);

            helpp = 1;
            SharedPreferences help = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = help.edit();
            editor1.putInt("helpp", helpp);
            editor1.apply();
        }
    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdownTimerText.setText(hms);
            }

            public void onFinish() {
                answer(3);
            }
        }.start();
    }

    public void setButtonValue (int buttonType, Drawable color, int setClickable){

        if (btnA == buttonType){btn1.setBackground(color);}
        if (btnB == buttonType){btn2.setBackground(color);}
        if (btnC == buttonType){btn3.setBackground(color);}
        if (btnD == buttonType){btn4.setBackground(color);}

        if (setClickable == 1){
            if (btnA == buttonType){btn1.setClickable(false);}
            if (btnB == buttonType){btn2.setClickable(false);}
            if (btnC == buttonType){btn3.setClickable(false);}
            if (btnD == buttonType){btn4.setClickable(false);}
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

        Resources res = getResources();
        String noq = res.getString(R.string.numberOfQuestions);
        lastQuestion = Integer.parseInt(noq) + 1;

        switch (answersArray[questionnInt - 2]) {
            case 1:
                Message.setText("נכון!");
                Message.setTextColor(0xFF00FF00);
                break;
            case 2:
                Message.setText("לא נכון!");
                Message.setTextColor(0xFFFF0000);
                break;
            case 3:
                Message.setText("עבר הזמן!");
                Message.setTextColor(0xFFFF0000);
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