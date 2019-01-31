package eparon.onevshundred;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    int COLOR_YELLOW, COLOR_DGREY;

    CheckBox Agree;
    FloatingActionButton Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Initializing Resources & Views
        COLOR_YELLOW = getResources().getColor(R.color.colorYellow);
        COLOR_DGREY = getResources().getColor(R.color.colorDGrey);

        String title = getResources().getString(R.string.infoTitle);
        String agree = getResources().getString(R.string.checkBox);
        if (getResources().getString(R.string.Lang).equals("English")) {
            title = getResources().getString(R.string.infoTitleENG);
            agree = getResources().getString(R.string.checkBoxENG);
        }

        Agree = findViewById(R.id.checkbox);
        Next = findViewById(R.id.fab);
        TextView InfoTitle = findViewById(R.id.infoTitle);
        TextView Info = findViewById(R.id.info);
        Info.setMovementMethod(new ScrollingMovementMethod()); // Set the info box to be scrollable

        InfoTitle.setText(title);
        Agree.setText(agree);

        Next.setClickable(false);
        Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
    }

    public void DoAgree(View view) {

        Next.setClickable(Agree.isChecked());

        if (!Agree.isChecked())
            Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_DGREY));
        if (Agree.isChecked())
            Next.setBackgroundTintList(ColorStateList.valueOf(COLOR_YELLOW));
    }

    public void GoNext(View view) {
        if (Agree.isChecked())
            startActivity(new Intent(Info.this, Question.class));
    }

}

