package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DebugMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    int COLOR_GREY, COLOR_BLACK;

    Switch DebugMode;
    CheckBox infoBox, qrBox;
    Spinner langSpinner;
    EditText noq, qrET;
    TextView[] Text = new TextView[5];

    int qnum, qrnum;
    String lang;
    boolean debugMode, skipInfo, qr;

    boolean temp = false;

    @Override
    public void onBackPressed() {
        if (noq.getText().toString().equals(""))
            noq.setText(getResources().getString(R.string.numberOfQuestions));
        if (qrET.getText().toString().equals(""))
            qrET.setText(getResources().getString(R.string.questionsPerQR));
        qnum = Integer.valueOf(noq.getText().toString());
        qrnum = Integer.valueOf(qrET.getText().toString());
        if (qnum > 40 || qnum < 5)
            Toast.makeText(this, "Questions number should be between 5 to 40", Toast.LENGTH_LONG).show();
        else {
            if (qrnum > 10 || qrnum < 3)
                Toast.makeText(this, "QR number should be between 3 to 10", Toast.LENGTH_LONG).show();
            else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("qnum", qnum);
                editor.putString("lang", lang);
                editor.putBoolean("debugMode", debugMode);
                editor.putBoolean("skipInfo", skipInfo);
                editor.apply();
                Intent a = new Intent(DebugMenu.this, MainActivity.class);
                startActivity(a);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        qnum = prefs.getInt("qnum", qnum);
        qrnum = prefs.getInt("qrnum", qrnum);
        lang = prefs.getString("lang", lang);
        debugMode = prefs.getBoolean("debugMode", debugMode);
        skipInfo = prefs.getBoolean("skipInfo", skipInfo);
        qr = prefs.getBoolean("qr", qr);

        COLOR_GREY = getResources().getColor(R.color.colorGrey);
        COLOR_BLACK = getResources().getColor(R.color.colorBlack);

        DebugMode = findViewById(R.id.dmSwitch);
        DebugMode.setChecked(debugMode);

        infoBox = findViewById(R.id.skipInfo);
        infoBox.setChecked(skipInfo);

        langSpinner = findViewById(R.id.lang_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(adapter);
        langSpinner.setOnItemSelectedListener(this);
        langSpinner.setSelection(adapter.getPosition(lang));

        noq = findViewById(R.id.questions);

        qrBox = findViewById(R.id.qrcb);
        qrBox.setChecked(qr);

        qrET = findViewById(R.id.qret);

        Text[0] = findViewById(R.id.text1);
        Text[1] = findViewById(R.id.text2);
        Text[2] = findViewById(R.id.text3);
        Text[3] = findViewById(R.id.text4);
        Text[4] = findViewById(R.id.text5);

        infoBox.setClickable(debugMode);
        langSpinner.setEnabled(debugMode);
        noq.setClickable(debugMode);
        noq.setLongClickable(debugMode);
        noq.setFocusable(debugMode);
        noq.setFocusableInTouchMode(debugMode);
        qrBox.setClickable(debugMode);
        qrET.setClickable(debugMode);
        qrET.setLongClickable(debugMode);
        qrET.setFocusable(debugMode);
        qrET.setFocusableInTouchMode(debugMode);

        if (!debugMode)
            for (TextView aText : Text)
                aText.setTextColor(COLOR_GREY);

        TextView version = findViewById(R.id.ver);
        version.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
    }

    public void DM (View view) {
        debugMode = !debugMode;
        DebugMode.setChecked(debugMode);

        infoBox.setClickable(debugMode);
        langSpinner.setEnabled(debugMode);
        noq.setClickable(debugMode);
        noq.setLongClickable(debugMode);
        noq.setFocusable(debugMode);
        noq.setFocusableInTouchMode(debugMode);
        qrBox.setClickable(debugMode);
        qrET.setClickable(debugMode);
        qrET.setLongClickable(debugMode);
        qrET.setFocusable(debugMode);
        qrET.setFocusableInTouchMode(debugMode);

        if (debugMode)
            for (TextView aText : Text)
                aText.setTextColor(COLOR_BLACK);
        if (!debugMode)
            for (TextView aText : Text)
                aText.setTextColor(COLOR_GREY);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (temp) {
            lang = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(adapterView.getContext(), lang, Toast.LENGTH_SHORT).show();
        } else
            temp = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    public void infoCheckbox (View view) {
        skipInfo = !skipInfo;
        infoBox.setChecked(skipInfo);
    }

}
