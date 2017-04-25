package com.github.singleunderscore.sharetoopenlink;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.util.Arrays;

public class OpenLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // make sure this is a share intent
        if (!Intent.ACTION_SEND.equals(intent.getAction())) {
            finish();
            return;
        }

        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText == null) {
            // don't modify clipboard if null sharedText
            String nullTextMsg = getResources().getString(R.string.null_text_msg);
            Toast.makeText(getApplicationContext(), nullTextMsg, Toast.LENGTH_SHORT).show();
        } else {
            String[] words = sharedText.split("\\s+");
            if (words.length == 0) {
                String emptyTextMsg = getResources().getString(R.string.empty_text_msg);
                Toast.makeText(getApplicationContext(), emptyTextMsg, Toast.LENGTH_SHORT).show();
            } else {
                boolean validLinkFound = false;
                for (String word : words) {
                    if (URLUtil.isValidUrl(word)) {
                        Uri sharedUri = Uri.parse(word);

                        Intent openIntent = new Intent(Intent.ACTION_VIEW, sharedUri);

                        try {
                            startActivity(openIntent);
                            validLinkFound = true;
                            break;
                        } catch (ActivityNotFoundException e) {
                            // continue and try next word
                        }
                    }
                }
                if (!validLinkFound) {
                    String noActivityMsg = getResources().getString(R.string.no_activity_msg);
                    String fullMsg = noActivityMsg + "\n" + sharedText;
                    Toast.makeText(getApplicationContext(), fullMsg, Toast.LENGTH_LONG).show();
                }
            }
        }


        finish();
    }
}
