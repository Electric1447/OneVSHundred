package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Info extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    int COLOR_YELLOW, COLOR_DGREY;

    TextView cbText;
    FloatingActionButton Next;
    CheckBox Agree;

    boolean debugMode;
    String lang;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        lang = prefs.getString("lang", lang);

        // Initializing Resources & Views
        COLOR_YELLOW = getResources().getColor(R.color.colorYellow);
        COLOR_DGREY = getResources().getColor(R.color.colorDGrey);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode) dmMsg.setVisibility(View.GONE);

        String title = getResources().getString(R.string.infoTitle);
        String agree = getResources().getString(R.string.checkBox);
        if (lang.equals("English")) {
            title = getResources().getString(R.string.infoTitleENG);
            agree = getResources().getString(R.string.checkBoxENG);
        }

        cbText = findViewById(R.id.checkboxText);
        Agree = findViewById(R.id.checkbox);
        Next = findViewById(R.id.fab);
        TextView InfoTitle = findViewById(R.id.infoTitle);
        TextView Info = findViewById(R.id.info);
        Info.setMovementMethod(new ScrollingMovementMethod()); // Set the info box to be scrollable

        InfoTitle.setText(title);
        cbText.setText(agree);

        Next.setClickable(false);
        Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
    }

    public void DoAgree (View view) {
        if (view.getId() == R.id.cbView) Agree.setChecked(!Agree.isChecked());

        Next.setClickable(Agree.isChecked());

        if (!Agree.isChecked()) Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        if (Agree.isChecked())  Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_YELLOW));
    }

    public void GoNext (View view) {
        if (Agree.isChecked()) startActivity(new Intent(Info.this, Question.class));
    }

}