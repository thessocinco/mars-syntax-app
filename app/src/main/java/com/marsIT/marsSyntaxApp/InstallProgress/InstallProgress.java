//package com.marsIT.marsapp.InstallProgress;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.marsIT.marsapp.MainProgram.MainActivity;
//import com.marsIT.marsapp.R;
//
//public class InstallProgress extends AppCompatActivity {
//    private ProgressBar progressBar;
//    private TextView progressText;
//    private Handler handler;
//    private SQLiteDatabase db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.install_progress);
//
//        progressBar = findViewById(R.id.progressBar);
//        progressText = findViewById(R.id.progressText);
//        handler = new Handler(getMainLooper());
//
//        db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
//
//        startInstallDataTask();
//    }
//
//    private void startInstallDataTask() {
//        new Thread(() -> {
//            try {
//                runProgress("Installing Products...", 0, 20);
//                Saveproducts();
//
//                runProgress("Installing Customers...", 21, 40);
//                SaveCustomer();
//
//                runProgress("Installing Schedules...", 41, 60);
//                SaveScheduleCustomerlist();
//
//                runProgress("Installing Barangays...", 61, 80);
//                SaveBarangay();
//
//                runProgress("Setting Receiver Info...", 81, 99);
//                insertreceivernum();
//
//                // Ensure OtherCUSTOMERS table exists before querying it
//                db.execSQL("CREATE TABLE IF NOT EXISTS OtherCUSTOMERS(" +
//                        "CID VARCHAR, CNAME VARCHAR, CTYPE VARCHAR, CPERSON VARCHAR, " +
//                        "CTELLNUM VARCHAR, CCELLNUM VARCHAR, STREET VARCHAR, " +
//                        "BARANGAY VARCHAR, MUNICIPALITY VARCHAR, PROVINCE VARCHAR, " +
//                        "Latitude varchar, Longitude varchar, Grouptype varchar, RouteCOde varchar, Source varchar);");
//
//                Cursor cLUBS = db.rawQuery("SELECT * FROM OtherCUSTOMERS", null);
//                if (cLUBS.getCount() <= 0) {
//                    // SaveOtherCustomer(); // Not included
//                }
//                cLUBS.close();
//
//                db.execSQL("UPDATE InstallValue SET statvalue = 'Y'");
//
//                // Post final 100% progress on UI thread
//                handler.post(() -> {
//                    progressBar.setProgress(100);
//                    progressText.setText("Installation Complete!");
//
//                    // Delay and start main activity
//                    handler.postDelayed(() -> {
//                        try {
//                            Intent intent = new Intent(InstallProgress.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            if (db != null && db.isOpen()) {
//                                db.close(); // Close only after everything finishes
//                            }
//                        }
//                    }, 1000);
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                handler.post(() -> progressText.setText("Installation failed: " + e.getMessage()));
//            }
//        }).start();
//    }
//
//
//    private void runProgress(String label, int startPercent, int endPercent) throws InterruptedException {
//        for (int i = startPercent; i <= endPercent; i++) {
//            Thread.sleep(25);
//            int finalI = i;
//            handler.post(() -> {
//                progressBar.setProgress(finalI);
//                progressText.setText(label + " " + finalI + "%");
//            });
//        }
//    }
//
//    private void Saveproducts() { /* implement if needed */ }
//
//    private void SaveCustomer() { /* implement if needed */ }
//
//    private void SaveScheduleCustomerlist() { /* implement if needed */ }
//
//    private void SaveBarangay() { /* implement if needed */ }
//
//    private void insertreceivernum() { /* implement if needed */ }
//}
