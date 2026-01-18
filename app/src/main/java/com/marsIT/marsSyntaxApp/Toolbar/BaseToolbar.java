package com.marsIT.marsSyntaxApp.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.marsIT.marsSyntaxApp.MainProgram.MainActivity;
import com.marsIT.marsSyntaxApp.R;

public class BaseToolbar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId()); // Ensure each activity has its layout
        setupToolbar(getToolbarTitle()); // Call setupToolbar in BaseActivity
    }

    protected void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title

                // Check if the current activity is MainActivity and disable the back arrow
                if (this instanceof MainActivity) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
            }

            TextView toolbarTitle = findViewById(R.id.toolbar_title);
            if (toolbarTitle != null) {
                toolbarTitle.setText(title); // Set custom title
            }
        }
    }

    protected int getLayoutResourceId() {
        return R.layout.mainlayout; // Default layout (override in child activities)
    }

    protected String getToolbarTitle() {
        return "Default Title"; // Default title (override in child activities)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kebabmenu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(item.getTitle());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed(); // Navigate back
            return true;
        } else if (item.getItemId() == R.id.action_phoneSettings) {
            openPhoneSettings();
            return true;
        } else if (item.getItemId() == R.id.action_restart) {
            Toast.makeText(this, "MarsApp has restarted", Toast.LENGTH_SHORT).show();
            restartApp();
            return true;
        } else if (item.getItemId() == R.id.action_exit) {
            Toast.makeText(this, "MarsApp is closed", Toast.LENGTH_SHORT).show();
            exitApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Opens Android settings
    private void openPhoneSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    // Restarts the app
    private void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity(); // Close all activities
        }
    }

    // Exits the app
    private void exitApp() {
        finishAffinity();
        System.exit(0);
    }
}
