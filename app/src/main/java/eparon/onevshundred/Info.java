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


    int COLOR_YELLOW = 0xFFFFFF00;
    int COLOR_DGREY = 0xFF666666;

    TextView Info;
    CheckBox Agree;
    FloatingActionButton Next;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Info = findViewById(R.id.info);
        Agree = findViewById(R.id.checkbox);
        Next = findViewById(R.id.fab);

        Info.setMovementMethod(new ScrollingMovementMethod());

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
        if (Agree.isChecked()) {
            Intent q = new Intent(Info.this, Question.class);
            startActivity(q);
        }
    }

}

