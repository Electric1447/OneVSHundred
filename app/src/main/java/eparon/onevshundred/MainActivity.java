package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    public static int NUMBER_OF_SECONDS = 30; // Number of Second per Question
    public static int NO_HELPS_BONUS_PERCENTAGE = 10; // Bonus % for not using helps

    boolean debugMode, skipInfo;
    String lang;

    int a = 0, b = 0;

    @Override
    public void onBackPressed () {
        this.finishAffinity();
        this.finishAndRemoveTask();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        skipInfo = prefs.getBoolean("skipInfo", skipInfo);
        lang = prefs.getString("lang", lang);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode) dmMsg.setVisibility(View.GONE);

        String wstr = getResources().getString(R.string.welcome);
        String[] hs = new String[]{getResources().getString(R.string.helpWheel), getResources().getString(R.string.help50), getResources().getString(R.string.helpPhone)};
        String start = getResources().getString(R.string.start);
        if (lang.equals("English")) {
            wstr = getResources().getString(R.string.welcomeENG);
            hs = new String[]{getResources().getString(R.string.helpWheelENG), getResources().getString(R.string.help50ENG), getResources().getString(R.string.helpPhoneENG)};
            start = getResources().getString(R.string.startENG);
        }

        TextView Welcome = findViewById(R.id.welcome);
        TextView HW = findViewById(R.id.ht1);
        TextView H50 = findViewById(R.id.ht2);
        TextView HP = findViewById(R.id.ht3);
        TextView Start = findViewById(R.id.start);
        String appname = getResources().getString(R.string.app_name);
        Welcome.setText(String.format("%s\n%s!", wstr, appname));
        HW.setText(hs[0]);
        H50.setText(hs[1]);
        HP.setText(hs[2]);
        Start.setText(start);
    }

    public void StartGame (View view) {
        Intent a = new Intent(MainActivity.this, Info.class);
        if (skipInfo) a = new Intent(MainActivity.this, Question.class);
        startActivity(a);
    }

    public void debug1 (View view) {
        if (b == 0 || b == 4) a++;
    }

    public void debug2 (View view) {
        if (a == 2) b++;
    }

    public void debug3 (View view) {
        if ((a == 3 && b == 4) || debugMode) startActivity(new Intent(MainActivity.this, DebugMenu.class));
        a = 0;
        b = 0;
    }

}