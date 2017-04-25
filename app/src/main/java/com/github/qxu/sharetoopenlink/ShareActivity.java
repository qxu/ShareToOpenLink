package com.github.qxu.sharetoopenlink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // make sure this is a share intent
        if (Intent.ACTION_SEND.equals(intent.getAction())) {

            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText == null) {
                // don't modify clipboard if null sharedText
                Toast.makeText(getApplicationContext(), "Null text shared", Toast.LENGTH_SHORT).show();
            } else {
                Uri sharedUri = Uri.parse(sharedText);

                Intent openIntent = new Intent(Intent.ACTION_VIEW, sharedUri);
                startActivity(openIntent);
            }
        }

        finish();
    }
}
