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

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    private static int SPLASH_TIME_OUT = 1000;
    private static int noOfMinutes = 31000;
    private int buttons = 4;

    private TextView countdownTimerText;
    private static CountDownTimer countDownTimer;

    int COLOR_GREEN = 0xFF00FF00;
    int COLOR_RED = 0xFFFF0000;
    int COLOR_WHITE = 0xFFFFFFFF;
    int COLOR_DGREY = 0xFF666666;

    Resources res;
    String[] qMain;
    TypedArray ta;
    String[][] qArr;

    Drawable colorLGreen, colorGrey;
    TextView score, Title, Question;
    Button[] btn = new Button[buttons];
    FloatingActionButton[] btnh = new FloatingActionButton[3];

    int scoreInt, timeInt;
    int questionInt;
    String answers;
    String timeString;

    boolean helpWH, help50, helpPH;
    boolean helpUsed;

    int[] btnMain = new int[buttons];

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
        ta = res.obtainTypedArray(R.array.qarr);
        qArr = new String[ta.length()][];
        for (int i = 0; i < ta.length(); ++i) {
            int id = ta.getResourceId(i, 0);
            if (id > 0)
                qArr[i] = res.getStringArray(id);
        }
        ta.recycle();

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        scoreInt = prefs.getInt("scoreInt", scoreInt);
        timeInt = prefs.getInt("timeInt", timeInt);
        questionInt = prefs.getInt("questionInt", questionInt);
        answers = prefs.getString("answers", answers);
        helpUsed = prefs.getBoolean("helpUsed", helpUsed);
        helpWH = prefs.getBoolean("helpWH", helpWH);
        help50 = prefs.getBoolean("help50", help50);
        helpPH = prefs.getBoolean("helpPH", helpPH);

        colorLGreen = getResources().getDrawable(R.drawable.custombutton_lgreen);
        colorGrey = getResources().getDrawable(R.drawable.custombutton_grey);

        Title = findViewById(R.id.top);
        Question = findViewById(R.id.question);

        btn[0] = findViewById(R.id.button1);
        btn[1] = findViewById(R.id.button2);
        btn[2] = findViewById(R.id.button3);
        btn[3] = findViewById(R.id.button4);

        btnh[0] = findViewById(R.id.fab1); // Help Wheel
        btnh[1] = findViewById(R.id.fab2); // Help 50
        btnh[2] = findViewById(R.id.fab3); // Help Phone

        currentScore = ((questionInt - 1) / 4 + 1) * 20;

        countdownTimerText = findViewById(R.id.countdownText);
        score = findViewById(R.id.score);

        if (qArr[2][questionInt - 1].equals("EMPTY") && qArr[3][questionInt - 1].equals("EMPTY")) {
            buttons = 2;
            btn[2].setVisibility(View.GONE);
            btn[3].setVisibility(View.GONE);
        }
        if (!qArr[2][questionInt - 1].equals("EMPTY") && qArr[3][questionInt - 1].equals("EMPTY")) {
            buttons = 3;
            btn[3].setVisibility(View.GONE);
        }

        for (int i = 0; i < buttons; i++){
            int ir;
            do {
                Random r = new Random();
                double dr = r.nextDouble() * buttons;
                ir = (int) dr + 1;
            } while (ArrayUtils.contains(btnMain, ir));
            btnMain[i] = ir;
        }

        setHelpButtonProps(0, helpWH);
        setHelpButtonProps(1, help50);
        setHelpButtonProps(2, helpPH);

        if (buttons != 4) {
            setHelpButtonProps(1, false);
            setHelpButtonProps(2, false);
        }

        Title.setText("שאלה " + questionInt + " - " + currentScore + " נקודות");
        Question.setText(qMain[questionInt - 1]);
        score.setText(scoreInt + " נקודות");
        setButtonsText();

        startTimer();
    }

    public void answer (int type) {
        timeString = countdownTimerText.getText().toString();
        timeInt += (noOfMinutes / 1000 - 1 - Integer.parseInt(timeString));
        questionInt++;
        answers += String.valueOf(type);
        scoreInt += currentScore * (1 - type / 2);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeInt", timeInt);
        editor.putInt("questionInt", questionInt);
        editor.putString("answers", answers);
        editor.putInt("scoreInt", scoreInt);
        editor.apply();

        countDownTimer.cancel();
        PopUp();
    }

    public void answerButton1 (View view) { answer((btnMain[0] + 1) / 3 + 1); }

    public void answerButton2 (View view) { answer((btnMain[1] + 1) / 3 + 1); }

    public void answerButton3 (View view) { answer((btnMain[2] + 1) / 3 + 1); }

    public void answerButton4 (View view) { answer((btnMain[3] + 1) / 3 + 1); }

    public void helpWheel (View view){
        useHelp();

        helpWH = false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("helpWH", helpWH);
        editor.apply();

        answer(1);
    }

    public void help50 (View view){
        setButtonProps(3, colorGrey, false);
        setButtonProps(4, colorGrey, false);

        useHelp();

        help50 = false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("help50", help50);
        editor.apply();
    }

    public void helpPhone (View view){
        Random d = new Random();
        double phoneHelpDouble = d.nextDouble() * 100;
        int phoneHelpInt = (int)phoneHelpDouble / 15;

        if(phoneHelpInt > 2)
            phoneHelpInt = 2;

        phoneHelpInt = 3 - phoneHelpInt;

        setButtonProps(phoneHelpInt, colorLGreen, true);
        setButtonProps(4, colorGrey, false);

        useHelp();

        helpPH = false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("helpPH", helpPH);
        editor.apply();
    }

    public void useHelp (){
        for (int i = 0; i < 3; i++) {
            setHelpButtonProps(i, false);
        }

        helpUsed = true;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("helpUsed", helpUsed);
        editor.apply();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                countdownTimerText.setText(hms);

                if (Integer.parseInt(hms) <= 10 && Integer.parseInt(hms) % 2 == 0)
                    countdownTimerText.setTextColor(COLOR_RED);
                else
                    countdownTimerText.setTextColor(COLOR_WHITE);
            }

            public void onFinish() { answer(3); }
        }.start();
    }

    private void setButtonsText() {
        for (int i = 0; i < buttons; i++){
            btn[i].setText(qArr[btnMain[i] - 1][questionInt - 1]);
        }
    }

    public void setButtonProps(int buttonType, Drawable color, boolean setClickable){
        for (int i = 0; i < buttons; i++){
            if (btnMain[i] == buttonType){
                btn[i].setBackground(color);
                btn[i].setClickable(setClickable);
            }
        }
    }

    public void setHelpButtonProps(int buttonType, boolean setValue){
        btnh[buttonType].setClickable(setValue);
        if (!setValue)
            btnh[buttonType].setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
    }

    public void PopUp (){

        for (int i = 0; i < buttons; i++) {
            btn[i].setClickable(false);
        }
        for (int i = 0; i < 3; i++) {
            setHelpButtonProps(i, false);
        }

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

        lastQuestion = Integer.parseInt(res.getString(R.string.numberOfQuestions)) + 1;

        switch (answers.charAt(questionInt - 2) - '0') {
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
                Intent a = new Intent(Question.this, Question.class);
                if(questionInt == lastQuestion)
                    a = new Intent(Question.this, EndActivity.class);
                startActivity(a);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}