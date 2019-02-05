package eparon.onevshundred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRActivity extends AppCompatActivity {

    public String PREFS_OVH = "OVHPrefsFile";
    SharedPreferences prefs;

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    TextView Title, Pin;

    boolean debugMode;
    int questionInt, qrnum;
    String lang;
    String[] qrMain, qrCodes;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        prefs = getSharedPreferences(PREFS_OVH, Context.MODE_PRIVATE);

        debugMode = prefs.getBoolean("debugMode", debugMode);
        qrnum = prefs.getInt("qrnum", qrnum);
        questionInt = prefs.getInt("questionInt", questionInt);
        lang = prefs.getString("lang", lang);

        qrMain = getResources().getStringArray(R.array.qrmain);
        qrCodes = getResources().getStringArray(R.array.qrcodes);

        TextView dmMsg = findViewById(R.id.dmm);
        if (!debugMode) dmMsg.setVisibility(View.GONE);

        Title = findViewById(R.id.nextLocationText);
        Pin = findViewById(R.id.digits);

        String nls = getResources().getString(R.string.qrNextLocation);
        if (lang.equals("English")) nls = getResources().getString(R.string.qrNextLocationENG);
        Title.setText(String.format("%s\n%s", nls, qrMain[(questionInt - 1) / qrnum - 1]));
    }

    public void SumbitPin (View view) {
        if (Pin.getText().toString().equals(qrCodes[(questionInt - 1) / qrnum - 1]))
            startActivity(new Intent(QRActivity.this, Question.class));
        else Toast.makeText(this, "Please enter a valid pin", Toast.LENGTH_LONG).show();
    }

    public void scanBarcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if (result.getContents() == null)
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        else {
            if (result.getContents().equals("OneVSHundered_HASH:_" + qrCodes[(questionInt - 1) / qrnum - 1]))
                startActivity(new Intent(QRActivity.this, Question.class));
            else Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
        }
    }

}