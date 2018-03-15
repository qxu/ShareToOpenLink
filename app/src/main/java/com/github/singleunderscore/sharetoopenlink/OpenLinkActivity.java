package com.github.singleunderscore.sharetoopenlink;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.Toast;

public class OpenLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // make sure this is a share intent
        if (!Intent.ACTION_SEND.equals(intent.getAction())) {
            showTextToast(R.string.share_err_msg);
        } else {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText == null) {
                // don't modify clipboard if null sharedText
                showTextToast(R.string.null_text_msg);
            } else {
                String[] words = sharedText.split("\\s+");
                if (words.length == 0) {
                    showTextToast(R.string.empty_text_msg);
                } else {
                    boolean validLinkFound = false;
                    for (String word : words) {
                        if (URLUtil.isValidUrl(word)) {
                            Uri targetUri = Uri.parse(word);
                            if (handleTargetUri(targetUri)) {
                                validLinkFound = true;
                                break;
                            }
                        }
                    }
                    if (!validLinkFound) {
                        String noActivityMsg = getString(R.string.no_activity_msg);
                        String fullMsg = noActivityMsg + "\n" + sharedText;
                        showTextToast(fullMsg);
                    }
                }
            }
        }

        finish();
    }

    private void showTextToast(int stringResId) {
        showTextToast(getString(stringResId));
    }

    private void showTextToast(String text) {
        Context context = getApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    private boolean handleTargetUri(Uri targetUri) {
        Intent targetIntent = new Intent(Intent.ACTION_VIEW, targetUri);

        PackageManager packageManager = getPackageManager();

        Intent chooserIntent = Intent.createChooser(targetIntent, targetUri.toString());
        if (chooserIntent.resolveActivity(packageManager) != null) {
            showTextToast(targetUri.toString());
            startActivity(chooserIntent);
            return true;
        } else {
            return false;
        }
    }
}
