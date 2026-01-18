package com.marsIT.marsSyntaxApp.MarsAppStartUp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.BroadcastReceiver;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi; // Target Higher Api - Updated 2025
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.marsIT.marsapp.InstallProgress.InstallProgress;
import com.marsIT.marsSyntaxApp.MainProgram.MainActivity;
import com.marsIT.marsSyntaxApp.R;

public class Startup extends AppCompatActivity implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 101;

    // Query string for database selection
    private String selectQuery;

    private List<String> lables;

    // SQLite database instance
    private SQLiteDatabase db;

    // UI components for selecting various details such as:
    Spinner salesmanidins;      // Salesman ID
    Spinner sellingtypeins;     // Selling Type
    EditText mepperidins;       // Mapper ID
    EditText passins;           // Password
    Spinner departmentins;      // Department
    Spinner weekschedins;       // Week Schedule
    Spinner databasenameins;    // Database Name
    Spinner locklocationins;    // Lock Location
    Spinner schedprocess;       // Schedule Process
    Spinner scheddays;          // Schedule Day

    // IntentFilter for capturing specific intents (e.g., broadcasts)
    IntentFilter intentFilter;

    private String salesmanName = "";
    private String department = "";


    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle extras = intent.getExtras();
                if (extras == null) return;

                String syntaxsms = extras.getString("sms", "");
                String cellphonenumber = extras.getString("cellnumber", "");

                if (syntaxsms.isEmpty() || cellphonenumber.isEmpty()) return;

                Cursor queryvalidnumber = db.rawQuery("SELECT * FROM validnumber WHERE num = ?", new String[]{cellphonenumber});
                if (queryvalidnumber != null && queryvalidnumber.moveToFirst()) {

                    if (syntaxsms.length() > 6) {
                        String stchar = syntaxsms.substring(0, 6);

                        if ("INSTAL".equals(stchar)) {
                            String salesmanid = "";
                            String sellingtype = "";
                            String mapperid = "";
                            String departmentIns = "";
                            String weeksched = "";
                            String dataname = "";
                            String loclock = "";
                            String schedprocessS = "";
                            String schedday = "";

                            int counter = 0;
                            for (int i = 6; i < syntaxsms.length(); i++) {
                                char descr = syntaxsms.charAt(i);
                                if (descr == '/') {
                                    counter++;
                                    continue;
                                }
                                switch (counter) {
                                    case 1: salesmanid += descr; break;
                                    case 2: sellingtype += descr; break;
                                    case 3: mapperid += descr; break;
                                    case 4: departmentIns += descr; break;
                                    case 5: weeksched += descr; break;
                                    case 6: dataname += descr; break;
                                    case 7: loclock += descr; break;
                                    case 8: schedprocessS += descr; break;
                                    case 9: schedday += descr; break;
                                }
                            }

                            db.execSQL("INSERT INTO InstallValue (salesmanid, sellingtype, mapperid, departmentIns, weeksched, locked, dataname, loclock, schedprocessS, schedday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                    new Object[]{salesmanid, sellingtype, mapperid, departmentIns, weeksched, "N", dataname, loclock, schedprocessS, schedday});

                            Toast.makeText(context.getApplicationContext(), "Successfully Installed: " + salesmanid, Toast.LENGTH_LONG).show();

                            Intent newIntent = new Intent(context, MainActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(newIntent);
                        }
                    }
                }
                if (queryvalidnumber != null) {
                    queryvalidnumber.close();
                }
            } catch (Exception e) {
                Log.e("SMSReceiver", "Error in onReceive", e);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU) // Broadcast Entry - Updated 2025
    @Override
    protected void onResume() { // Register the Receiver
        Log.d("StartupActivity", "onResume: Registering the receiver");
        registerReceiver(notificationReceiver, intentFilter, RECEIVER_NOT_EXPORTED); // Inserted -- Context.RECEIVER_NOT_EXPORTED
        super.onResume();
//        checkAndEnableGPS();
    }

    @Override
    protected void onPause() { // Unregister the Receiver
        Log.d("StartupActivity", "onPause: Unregistering the receiver");
        unregisterReceiver(notificationReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        try {

            // Check and request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissions();
            }

            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            // db.execSQL("CREATE TABLE IF NOT EXISTS InstallValue(SalesmanID Varchar,SellingType varchar,MapperID Varchar,department varchar,weeksched varchar,statvalue varchar,databasename varchar, lock varchar);");

            db.execSQL("CREATE TABLE IF NOT EXISTS validnumber(NUM varchar,Name Varchar); ");
            InsertValidNumber();

            db.execSQL("CREATE TABLE IF NOT EXISTS salesman(SalesmanID Varchar);");

            db.execSQL("CREATE TABLE IF NOT EXISTS SalesmanInstallValue(SalesmanID Varchar,SellingType varchar,MapperID Varchar,department varchar,weeksched varchar,statvalue varchar,databasename varchar, lock varchar,schedstatus varchar,schedday varchar);");

            Cursor cSalesman = db.rawQuery("select * from salesman", null);
            cSalesman.moveToFirst();
            int cntSalesman =  cSalesman.getCount();
            if(cntSalesman > 0){
//                Toast.makeText(getApplicationContext(), "Selected Salesman: " + cntSalesman, Toast.LENGTH_LONG).show();
                Log.d("SALESMAN", "Selected Salesman: " + cntSalesman);
            }else{
                SalesmanList();
            }
            cSalesman.close();

            SalesmanListINS();
            salesmanidins = findViewById(R.id.spsalesmanst);
            selectQuery = "Select salesmanid from salesman ORDER BY salesmanid";
            loadSpinnerData();

            Button btloaddata = findViewById(R.id.btokinstall);
            btloaddata.setOnClickListener(v -> {
                // TODO Auto-generated method stub

//                String salesmanName = salesmanidins.getSelectedItem().toString();
//                String department = departmentins.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(), salesmanidins.getSelectedItem().toString() + " " + "Salesman are Successfully Install", Toast.LENGTH_LONG).show();

                Cursor cInstallValue = db.rawQuery("select * from SalesmanInstallValue WHERE SALESMANID = '"+ salesmanidins.getSelectedItem().toString() +"'", null);
                cInstallValue.moveToFirst();
                int cnt1 =  cInstallValue.getCount();
                if(cnt1 > 0){
                    db.execSQL("Insert into InstallValue values('"+ cInstallValue.getString(0) +"','"+ cInstallValue.getString(1) +"',"
                            + "'"+ cInstallValue.getString(2) +"','"+ cInstallValue.getString(3) +"','"+ cInstallValue.getString(4)
                            +"','N','"+ cInstallValue.getString(6) +"','"+ cInstallValue.getString(7) +"','"+ cInstallValue.getString(8)
                            + "','"+ cInstallValue.getString(9) + "');");

                    // Send notification
//                    sendInstallNotification(salesmanName, department);
                    Startup.this.finish();
                }else{
                    if("888000".equals(passins.getText().toString())){
                        db.execSQL("Insert into InstallValue values('"+ salesmanidins.getSelectedItem().toString() +"','"+  sellingtypeins.getSelectedItem().toString() +"'," +
                                "'"+ mepperidins.getText().toString() +"','"+ departmentins.getSelectedItem().toString() +"','"+ weekschedins.getSelectedItem().toString() +"','N','"+ databasenameins.getSelectedItem().toString() +"','"+ locklocationins.getSelectedItem().toString() +"','"+ schedprocess.getSelectedItem().toString() + "','"+ scheddays.getSelectedItem().toString() + "');");

                        // Send notification
//                        sendInstallNotification(salesmanName, department);
                        Startup.this.finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Invalid Key Password", Toast.LENGTH_LONG).show();
                    }
                }
                cInstallValue.close();
            });

            // development for install progress
//            Button btloaddata = findViewById(R.id.btokinstall);
//
//            btloaddata.setOnClickListener(v -> {
//                Toast.makeText(getApplicationContext(),
//                        salesmanidins.getSelectedItem().toString() + " Salesman is Successfully Installed",
//                        Toast.LENGTH_LONG).show();
//
//                Cursor cInstallValue = db.rawQuery("SELECT * FROM SalesmanInstallValue WHERE SALESMANID = '"
//                        + salesmanidins.getSelectedItem().toString() + "'", null);
//
//                cInstallValue.moveToFirst();
//                int cnt1 = cInstallValue.getCount();
//
//                if (cnt1 > 0) {
//                    db.execSQL("INSERT INTO InstallValue VALUES('" + cInstallValue.getString(0) + "','" + cInstallValue.getString(1) + "',"
//                            + "'" + cInstallValue.getString(2) + "','" + cInstallValue.getString(3) + "','" + cInstallValue.getString(4)
//                            + "','N','" + cInstallValue.getString(6) + "','" + cInstallValue.getString(7) + "','" + cInstallValue.getString(8)
//                            + "','" + cInstallValue.getString(9) + "');");
//
//                    // Start InstallProgress screen after inserting
//                    Intent intent = new Intent(Startup.this, InstallProgress.class);
//                    startActivity(intent);
//                    Startup.this.finish();
//
//                } else {
//                    if ("222111".equals(passins.getText().toString())) {
//                        db.execSQL("INSERT INTO InstallValue VALUES('" + salesmanidins.getSelectedItem().toString() + "',"
//                                + "'" + sellingtypeins.getSelectedItem().toString() + "',"
//                                + "'" + mepperidins.getText().toString() + "',"
//                                + "'" + departmentins.getSelectedItem().toString() + "',"
//                                + "'" + weekschedins.getSelectedItem().toString() + "',"
//                                + "'N','" + databasenameins.getSelectedItem().toString() + "',"
//                                + "'" + locklocationins.getSelectedItem().toString() + "',"
//                                + "'" + schedprocess.getSelectedItem().toString() + "',"
//                                + "'" + scheddays.getSelectedItem().toString() + "');");
//
//                        // Start InstallProgress screen after inserting
//                        Intent intent = new Intent(Startup.this, InstallProgress.class);
//                        startActivity(intent);
//                        Startup.this.finish();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Invalid Key Password", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                cInstallValue.close();
//            });

            sellingtypeins = findViewById(R.id.spsellingtypest);
            mepperidins = findViewById(R.id.etInsmapperid);
            departmentins = findViewById(R.id.spdepartmentst);
            weekschedins = findViewById(R.id.spweekschedst);
            databasenameins = findViewById(R.id.spdatabasenamest);
            locklocationins = findViewById(R.id.sploclocationst);
            schedprocess= findViewById(R.id.spschedprocessst);
            scheddays= findViewById(R.id.spschedday);

            passins = findViewById(R.id.etpasswordins);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"" + e,Toast.LENGTH_LONG).show();
            Log.e("ON CREATE", "On Create Error", e);
        }
    }

//    private void sendInstallNotification(String salesmanName, String department) {
//        String phoneNumber = "+639156112060";
//        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//
//        String message = "SALESMAN INSTALL\n" +
//                "Name: " + salesmanName + "\n" +
//                "Department: " + department + "\n" +
//                "Time: " + time;
//
//        try {
//            SmsManager smsManager;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                smsManager = getSystemService(SmsManager.class);
//            } else {
//                smsManager = SmsManager.getDefault();
//            }
//
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            Toast.makeText(this, "Install SMS sent", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

    // Function to check and request permissions
    private final ActivityResultLauncher<Intent> gpsActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        askToEnableGPS();
                    }
                } catch (Exception e) {
                    Log.e("GPSLauncher", "Error checking GPS after result", e);
                }
            });

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // For Android 5.1 and below, permissions are granted at install time.
            checkAndEnableGPS();
            return;
        }

        try {
            String[] permissions = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            };

            List<String> permissionsToRequest = new ArrayList<>();

            // Base permissions
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }

            // POST_NOTIFICATIONS for Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(
                        this,
                        permissionsToRequest.toArray(new String[0]),
                        PERMISSION_REQUEST_CODE
                );
            } else {
                checkAndEnableGPS();
            }
        } catch (Exception e) {
            Log.e("PermissionCheck", "Error checking/requesting permissions", e);
        }
    }

    private void checkAndEnableGPS() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                askToEnableGPS();
            }
        } catch (Exception e) {
            Log.e("GPSCheck", "Error checking GPS status", e);
        }
    }

    private void askToEnableGPS() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.mars_logo)
                    .setTitle("Enable GPS")
                    .setMessage("GPS is required for location services. Please turn on GPS.")
                    .setPositiveButton("Turn On", (dialog, which) -> {
                        try {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            gpsActivityResultLauncher.launch(intent);
                        } catch (Exception e) {
                            Log.e("GPSIntent", "Error launching location settings", e);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            Log.e("GPSDialog", "Error showing GPS enable dialog", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            try {
                boolean locationGranted = false;
                boolean smsGranted = false;
                boolean allPermissionsGranted = true;

                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        Log.w("PermissionResult", "Permission denied: " + permission);
                    }

                    if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locationGranted = true;
                    }

                    if ((Manifest.permission.RECEIVE_SMS.equals(permission)
                            || Manifest.permission.READ_SMS.equals(permission)
                            || Manifest.permission.SEND_SMS.equals(permission))
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        smsGranted = true;
                    }

                    if (Manifest.permission.POST_NOTIFICATIONS.equals(permission)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.i("PermissionResult", "Notification permission granted");
                        } else {
                            Log.w("PermissionResult", "Notification permission denied");
                        }
                    }
                }

                if (locationGranted) checkAndEnableGPS();
                if (smsGranted) askToBeDefaultSmsApp();

                if (!allPermissionsGranted) {
                    Toast.makeText(this, "Some permissions were denied. Please enable them in settings.", Toast.LENGTH_LONG).show();
//                    showPermissionSettingsDialog(); // <-- Call this when permanently denied
                    openAppSettings();
                } else {
                    Toast.makeText(this, "All required permissions granted.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("PermissionResult", "Error processing permission result", e);
            }
        }
    }

    private void askToBeDefaultSmsApp() {
        // Android 10–13 Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+
            RoleManager roleManager = (RoleManager) getSystemService(Context.ROLE_SERVICE);
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                    Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                    startActivity(intent); // or startActivityForResult() if you want a result callback
                }
            }
        } else {
            // Android 14+ RoleManager (ROLE_SMS)
            // Fallback for Android 4.4 (KitKat) to Android 9
            String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(this);
            if (!getPackageName().equals(defaultSmsPackage)) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                startActivity(intent);
            }
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null); // returns com.marsIT.marsapp
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional but recommended
        startActivity(intent);
    }

    private void loadSpinnerData() {

        try {
            // Spinner Drop down elements
            lables = new ArrayList<>();

            // SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    lables.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            // closing connection
            cursor.close();
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            salesmanidins.setAdapter(dataAdapter);

        } catch (Exception e) {
            Log.e("LOAD SPINNER ERROR", "Load spinner data ERROR: ", e);
        }
    }

    private void SalesmanListINS(){

        Cursor cSalesmanListINS = db.rawQuery("select * from SalesmanInstallValue", null);
        cSalesmanListINS.moveToFirst();
        int cnt1SalesmanListINS = cSalesmanListINS.getCount();
        if(cnt1SalesmanListINS > 0){
//            Toast.makeText(getApplicationContext(), "Salesman IN: " + cSalesmanListINS, Toast.LENGTH_LONG).show();
            Log.d("SALESMAN LIST IN", "Salesman IN: " + cSalesmanListINS);
        }else{

            // URC SALESMAN
            String Weekschedule = "";
            Weekschedule = "W3";

            // URC EXT

            db.execSQL("Insert into SalesmanInstallValue values('CO-A','Van-selling','URC EXT1','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('GETUTUA','Booking','URC EXT2','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('PELIÑO','Booking','URC EXT3','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BARCELONIA','Van-selling','URC EXT4','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LLAMELO','Van-selling','URC EXT5','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TARONGOY','Van-selling','URC EXT6','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ELARDE','Booking','URC EXT7','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BASAÑEZ','Booking','URC EXT8','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('PUNO','Booking','URC EXT9','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('COBOL','Booking','URC EXT10','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('','Booking','URC EXT11','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ENALDO','Van-selling','URC EXT12','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('SISON-C','Van-selling','URC EXT13','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BURA-AY','Van-selling','URC EXT14','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('DORONILA','Van-selling','URC EXT15','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MATIN','Van-selling','URC EXT16','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('EBO','Van-selling','URC EXT15','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BERNARDO','Van-selling','URC EXT17','UC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            // BOOKING UC
            db.execSQL("Insert into SalesmanInstallValue values('BINGIL','Booking','UC','UC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SEPARA','Booking','UC','UC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('IGNACIO','Booking','URC-A002','UC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('TADEJA','Booking','URC-A005','UC','"+ Weekschedule + "','N','MARS1','Y','N','N');");

            // CENTURY SALESMAN MARS 1
            Weekschedule = "W3";

            // BOOKING CJKAS
            db.execSQL("Insert into SalesmanInstallValue values('FRANCISCO','Booking','CJKAS01','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MANDANAS','Booking','CJKAS02','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('BENLOT','Booking','CJKAS03','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ATCHECOSO','Booking','CJKAS04','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('SALUD','Booking','CJKAS05','CC','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ATANQUE','Booking','CJKAS06','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ASPILLA','Booking','CJKAS07','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('SENOLOS','Booking','CJKAS08','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('DELA PEÑA','Booking','CJKAS09','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('PEÑAFIEL','Booking','CJKAS10','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");

            // VAN SELLING CRSD CC1
            db.execSQL("Insert into SalesmanInstallValue values('BALANCIO','Van-selling','CRSD01','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BIAG','Van-selling','CRSD02','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LARA','Van-selling','CRSD03','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('','Van-selling','CRSD04','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('JACO','Van-selling','CRSD05','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('CABRADILLA','Van-selling','CRSD06','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('CEBALLOS','Van-selling','CRSD07','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BUREROS-G','Van-selling','CRSD08','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            // BOOKING CBK
            db.execSQL("Insert into SalesmanInstallValue values('ABING','Booking','CBK-01','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SARAMOSING','Booking','CBK-02','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('BIOC','Booking','CBK-03','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('PO','Booking','CBK-04','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CRUEL','Booking','CBK-05','CC','"+ Weekschedule + "','N','MARS1','Y','N','N');");

            //
            db.execSQL("Insert into SalesmanInstallValue values('NAPOLES','Booking','CC','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('SAMBAS','Booking','CPSS08','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TAYOTO','Van-selling','CRSD05','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ESPARAGOZA-R','Booking','CPSS05','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('CRISTE','Van-selling','CRSD01','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('FEGURO','Booking','CPSS06','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('JP-LLIDO','Booking','CC','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TIAGA','Van-selling','CRSD03','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            // VAN SELLING MONTOSCO
            db.execSQL("Insert into SalesmanInstallValue values('DELOS-REYES','Van-selling','MI','MI','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            // BOOKING
            db.execSQL("Insert into SalesmanInstallValue values('ELCAMEL','Booking','CC','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LALLEN','Booking','CC','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            // PSS
            db.execSQL("Insert into SalesmanInstallValue values('ELCAMEL','Booking','CPSS-001','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LALLEN','Booking','CPSS-002','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ELCAMEL','Booking','CPSS-003','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LALLEN','Booking','CPSS-004','CC','"+ Weekschedule + "','N','MARS1','Y','N','Y');");

            db.execSQL("Insert into SalesmanInstallValue values('OFFICE-LY','Booking','CPSS-005','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");

            // CENTURY SALESMAN MARS 2
            Weekschedule = "W3";

            // BOOKING CBK
            db.execSQL("Insert into SalesmanInstallValue values('CATAYOC','Booking','CBK-01','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CULASTE','Booking','CBK-02','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ABAD','Booking','CBK-03','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('DATORIN','Booking','CBK-04','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SARCIA','Booking','CBK-05','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SAMONTE','Booking','CBK-06','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CODINO','Booking','CBK-07','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");

            // BOOKING CJKAS
            db.execSQL("Insert into SalesmanInstallValue values('CARBILLEDO','Booking','CJKAS-01','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('GARCIA','Booking','CJKAS-02','CC','"+ Weekschedule + "','N','MARS2','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('TANGGA-AN','Booking','CJKAS-03','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MANATO','Booking','CJKAS-04','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('PAGPAGUITAN','Booking','CJKAS-05','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MEDEL J','Booking','CJKAS-06','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('YUNGAO','Booking','CJKAS-07','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('RETARDO','Booking','CJKAS-08','CC','"+ Weekschedule + "','N','MARS2','Y','N','N');");

            // VAN SELLING CRSD CC2
            db.execSQL("Insert into SalesmanInstallValue values('FERNANDEZ-M','Van-selling','CRSD-01','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('CABIG','Van-selling','CRSD-02','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TAN','Van-selling','CRSD-03','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ESGRA','Van-selling','CRSD-04','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LOGRONIO','Van-selling','CRSD-05','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('BALBUENA','Van-selling','CRSD-06','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ANGON','Van-selling','CRSD-07','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TANUDTANUD','Van-selling','CRSD-08','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('RODRIGUEZ','Van-selling','CRSD-09','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ROXAS','Van-selling','CRSD-10','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('YCOT','Van-selling','CRSD-11','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");

            db.execSQL("Insert into SalesmanInstallValue values('AYON','Booking','CFAV-02','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('CULASTE','Booking','B-ROUT2','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ECHAVEZ','Booking','CPSS-07','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('FEROLIN','Booking','CPSS-06','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('GONZALES','Booking','CC','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('IBAÑEZ','Booking','CPSS-08','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LABOR','Van-selling','PL-02','PL','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LIBRADILLA','Booking','CC','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LEONES','Booking','B-ROUT4','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MAHAY','Van-selling','CC','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('MARZAN','Booking','B-ROUT3','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('OMILA','Booking','CC','CC','"+ Weekschedule + "','N','MARS','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('ROLLO','Van-selling','CC','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('TIMONIO','Booking','CPSS-01','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");

            // PSS
            db.execSQL("Insert into SalesmanInstallValue values('ELCAMEL','Booking','CPSS-001','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LALLEN','Booking','CPSS-002','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('','Booking','CPSS-003','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('LALLEN','Booking','CPSS-004','CC','"+ Weekschedule + "','N','MARS2','Y','N','Y');");

            // LAMOIYAN SALESMAN
            Weekschedule = "W3";

            // VAN SELLING EQ-EX
            db.execSQL("Insert into SalesmanInstallValue values('CABAYA','Van-selling','RSD01','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('FALSARIO','Van-selling','RSD02','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");

            // BOOKING EQ-BTDT
            db.execSQL("Insert into SalesmanInstallValue values('MESA','Booking','BTDT01','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SALAZAR','Booking','BTDT02','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('BERAO','Booking','BTDT03','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SAJO','Booking','BTDT04','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ESCOTE','Booking','KAS01','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('FERRER','Booking','KAS02','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('BLANCO-I','Booking','KAS03','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('AQUINO','Booking','BTDT07','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('DELA CHINA','Booking','BTDT07','LY','"+ Weekschedule + "','N','MARS1','N','N','N');");

            // RAM SALESMAN
            Weekschedule = "W3";

            // VAN SELLING RM-EX
            db.execSQL("Insert into SalesmanInstallValue values('OFFICE-RAM1','Booking','CR-RSD01','RM','"+ Weekschedule + "','N','MARS1','N','N','N');");

            // BOOKING RM-BTDT
            db.execSQL("Insert into SalesmanInstallValue values('BAGAY','Booking','CR-BTDT01','RM','"+ Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CATIPAY','Booking','CR-BTDT02','RM','"+ Weekschedule + "','N','MARS1','N','N','N');");

            // PEERLESS SALESMAN
            Weekschedule = "W3";

            // VAN SELLING PL
            db.execSQL("Insert into SalesmanInstallValue values('PARBA','Van-selling','PL-01','PL','"+ Weekschedule + "','N','MARS2','Y','N','Y');");
            db.execSQL("Insert into SalesmanInstallValue values('PATAYON','Van-selling','PL-04','PL','"+ Weekschedule + "','N','MARS2','Y','N','Y');");

            db.execSQL("Insert into SalesmanInstallValue values('CRISTOBAL','Booking','EQ-BK02','PL','"+ Weekschedule + "','N','MARS2','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('GOMITAS','Booking','EQ-BK03','PL','"+ Weekschedule + "','N','MARS2','Y','N','N');");

            // LUBRICANTS SALESMAN
            Weekschedule = "W3";

            db.execSQL("Insert into SalesmanInstallValue values('CAAB','Van-selling','LDSAR8','L','" + Weekschedule + "','N','MARS1','Y','N','Y');");

            db.execSQL("Insert into SalesmanInstallValue values('ASUBE','Van-selling','LDSAR7','L','"+ Weekschedule + "','DSAR7','0','Y','N','N');");

            db.execSQL("Insert into SalesmanInstallValue values('PASION','Booking','LDSAR4','L','"+ Weekschedule + "','DSAR4','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ASID','Booking','LDSAR_B2B5','L','"+ Weekschedule + "','DSAR4','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('MONDERO','Booking','LDSAR3','L','"+ Weekschedule + "','DSAR4','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CAÑAS','Booking','LDSAR5','L','"+ Weekschedule + "','DSAR5','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CABAÑAS','Booking','LDSAR3B','L','"+ Weekschedule + "','DSAR3B','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ESTELLORE','Booking','LDSAR2','L','"+ Weekschedule + "','DSAR2','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('LANSADIRAS','Booking','LDSAR9','L','"+ Weekschedule + "','DSAR2','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('ASTACA-AN','Booking','LDSAR1','L','"+ Weekschedule + "','DSAR2','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('PARTORIZA','Booking','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('APOLINARIO','Booking','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SANDICO','Booking','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('CABAÑAS','Van-selling','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','N');");

//            db.execSQL("Insert into SalesmanInstallValue values('GUZMAN','Booking','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','N');");
//            db.execSQL("Insert into SalesmanInstallValue values('CAAB','Van-selling','LDSAR8','L','"+ Weekschedule + "','DSAR8','0','Y','N','Y');");
//            db.execSQL("Insert into SalesmanInstallValue values('BARTE','Van-selling','LDSAR','L','"+ Weekschedule + "','DSAR','0','Y','N','Y');");

            // AGRICHEM SALESMAN
            Weekschedule = "W3";
            db.execSQL("Insert into SalesmanInstallValue values('BENANING','Booking','AGRIPLA-3','AG','" + Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('SON','Booking','AGRIGEN-5','AG','" + Weekschedule + "','N','MARS1','N','N','N');");
            db.execSQL("Insert into SalesmanInstallValue values('NARVACAN','Booking','AGRIPLA-1','AG','" + Weekschedule + "','N','MARS1','N','N','N');");
        }
        cSalesmanListINS.close();
    }

    private void SalesmanList() {

        Cursor cSalesman = db.rawQuery("select * from salesman", null);
        cSalesman.moveToFirst();
        int cnt1 =  cSalesman.getCount();
        if(cnt1 > 0) {
            Toast.makeText(getApplicationContext(), "Salesman already install", Toast.LENGTH_LONG).show();
        } else {

            // CENTURY MARS1 & MARS2, UCR SALESMAN
            db.execSQL("Insert into salesman values('ALBUNA');");
            db.execSQL("Insert into salesman values('ALIM');");
            db.execSQL("Insert into salesman values('AMIL');");
            db.execSQL("Insert into salesman values('ANSA');");
            db.execSQL("Insert into salesman values('ABAD');");
            db.execSQL("Insert into salesman values('ASPILLA');");
            db.execSQL("Insert into salesman values('ATCHECOSO');");
            db.execSQL("Insert into salesman values('ATANQUE');");
            db.execSQL("Insert into salesman values('ABING');");
            db.execSQL("Insert into salesman values('AGADIA');");
            db.execSQL("Insert into salesman values('ANTONIO');");
            db.execSQL("Insert into salesman values('ANDICO');");
            db.execSQL("Insert into salesman values('ANGON');");
            db.execSQL("Insert into salesman values('ANTONIO-E');");
            db.execSQL("Insert into salesman values('ALDEGUER');");
            db.execSQL("Insert into salesman values('ALOLOR');");
            db.execSQL("Insert into salesman values('ANONAS');");
            db.execSQL("Insert into salesman values('ALBIA');");
            db.execSQL("Insert into salesman values('DESTACAMENTO');");
            db.execSQL("Insert into salesman values('ALLAWAN');");
            db.execSQL("Insert into salesman values('ANTO');");
            db.execSQL("Insert into salesman values('A-PALMA');");
            db.execSQL("Insert into salesman values('ASID');");
            db.execSQL("Insert into salesman values('AQUINO');");
            db.execSQL("Insert into salesman values('BARCELONIA');");
            db.execSQL("Insert into salesman values('BALBUENA');");
            db.execSQL("Insert into salesman values('BIOC');");
            db.execSQL("Insert into salesman values('BASAÑEZ');");
            db.execSQL("Insert into salesman values('BALANCIO');");
            db.execSQL("Insert into salesman values('BELINO');");
            db.execSQL("Insert into salesman values('BEMOS');");
            db.execSQL("Insert into salesman values('BERNARDO');");
            db.execSQL("Insert into salesman values('BIAOCO');");
            db.execSQL("Insert into salesman values('BIAG');");
            db.execSQL("Insert into salesman values('BINGIL');");
            db.execSQL("Insert into salesman values('BUSTALEÑO');");
            db.execSQL("Insert into salesman values('BUREROS-G');");
            db.execSQL("Insert into salesman values('BURA-AY');");
            db.execSQL("Insert into salesman values('BORBON');");
            db.execSQL("Insert into salesman values('BOYONAS');");
            db.execSQL("Insert into salesman values('BENLOT');");
            db.execSQL("Insert into salesman values('CABAÑAS');");
            db.execSQL("Insert into salesman values('CABATUAN');");
            db.execSQL("Insert into salesman values('CABRADILLA');");
            db.execSQL("Insert into salesman values('CRISTOBAL');");
            db.execSQL("Insert into salesman values('CAÑETE-M');");
            db.execSQL("Insert into salesman values('CASTRO');");
            db.execSQL("Insert into salesman values('CRISTE');");
            db.execSQL("Insert into salesman values('CENON');");
            db.execSQL("Insert into salesman values('CEBALLOS');");
            db.execSQL("Insert into salesman values('CLAROS');");
            db.execSQL("Insert into salesman values('CO-A');");
            db.execSQL("Insert into salesman values('COBOL');");
            db.execSQL("Insert into salesman values('COUNTER');");
            db.execSQL("Insert into salesman values('CRUEL');");
            db.execSQL("Insert into salesman values('CATAYOC');");
            db.execSQL("Insert into salesman values('CARBILLEDO');");
            db.execSQL("Insert into salesman values('CABIG');");
            db.execSQL("Insert into salesman values('CODINO');");
            db.execSQL("Insert into salesman values('CULASTE');");
            db.execSQL("Insert into salesman values('CATEQUISTA');");
            db.execSQL("Insert into salesman values('CRIBELLO');");
            db.execSQL("Insert into salesman values('DANLAG');");
            db.execSQL("Insert into salesman values('DAVIN');");
            db.execSQL("Insert into salesman values('DATORIN');");
            db.execSQL("Insert into salesman values('DEMAULO');");
            db.execSQL("Insert into salesman values('DE ASIS');");
            db.execSQL("Insert into salesman values('DECENA');");
            db.execSQL("Insert into salesman values('DELOS REYES');");
            db.execSQL("Insert into salesman values('DELA CHINA');");
            db.execSQL("Insert into salesman values('DELA PEÑA');");
            db.execSQL("Insert into salesman values('DORONILA');");
            db.execSQL("Insert into salesman values('GAMENG');");
            db.execSQL("Insert into salesman values('ELCAMEL');");
            db.execSQL("Insert into salesman values('ECHAVEZ');");
            db.execSQL("Insert into salesman values('ELARDE');");
            db.execSQL("Insert into salesman values('ENALDO');");
            db.execSQL("Insert into salesman values('ENERO');");
            db.execSQL("Insert into salesman values('ENRIQUEZ');");
            db.execSQL("Insert into salesman values('ESPARAGOZA-R');");
            db.execSQL("Insert into salesman values('ESTIGOY');");
            db.execSQL("Insert into salesman values('ELAT');");
            db.execSQL("Insert into salesman values('ATANQUE');");
            db.execSQL("Insert into salesman values('ESGRA');");
            db.execSQL("Insert into salesman values('FEGURO');");
            db.execSQL("Insert into salesman values('PEÑAFIEL');");
            db.execSQL("Insert into salesman values('FEROLIN');");
            db.execSQL("Insert into salesman values('FERNANDEZ-M');");
            db.execSQL("Insert into salesman values('FUEGO');");
            db.execSQL("Insert into salesman values('FRANCISCO');");
            db.execSQL("Insert into salesman values('GARCES');");
            db.execSQL("Insert into salesman values('GARCIA');");
            db.execSQL("Insert into salesman values('GABUT');");
            db.execSQL("Insert into salesman values('GETUTUA');");
            db.execSQL("Insert into salesman values('GEMARINO');");
            db.execSQL("Insert into salesman values('GONZALES');");
            db.execSQL("Insert into salesman values('GOMITAS');");
            db.execSQL("Insert into salesman values('GREGORIO');");
            db.execSQL("Insert into salesman values('GUMBAN');");
            db.execSQL("Insert into salesman values('GUZMAN');");
            db.execSQL("Insert into salesman values('HENSON');");
            db.execSQL("Insert into salesman values('IBAÑEZ');");
            db.execSQL("Insert into salesman values('IGNACIO');");
            db.execSQL("Insert into salesman values('JACO');");
            db.execSQL("Insert into salesman values('JB-VERANO');");
            db.execSQL("Insert into salesman values('JIMENEZ');");
            db.execSQL("Insert into salesman values('JP-LLIDO');");
            db.execSQL("Insert into salesman values('JORDAN');");
            db.execSQL("Insert into salesman values('LALLEN');");
            db.execSQL("Insert into salesman values('LAWAG');");
            db.execSQL("Insert into salesman values('LIBRADILLA');");
            db.execSQL("Insert into salesman values('LISTAM');");
            db.execSQL("Insert into salesman values('LLAMELO');");
            db.execSQL("Insert into salesman values('LINSAG');");
            db.execSQL("Insert into salesman values('LAROSCAIN');");
            db.execSQL("Insert into salesman values('LARANANG');");
            db.execSQL("Insert into salesman values('LARA');");
            db.execSQL("Insert into salesman values('LABOR');");
            db.execSQL("Insert into salesman values('LEONES');");
            db.execSQL("Insert into salesman values('LABADO');");
            db.execSQL("Insert into salesman values('LIZONDRA');");
            db.execSQL("Insert into salesman values('LOGRONIO');");
            db.execSQL("Insert into salesman values('LUBGUBAN');");
            db.execSQL("Insert into salesman values('LUDOVICE');");
            db.execSQL("Insert into salesman values('LAPAZ');");
            db.execSQL("Insert into salesman values('MANDANAS');");
            db.execSQL("Insert into salesman values('MARS');");
            db.execSQL("Insert into salesman values('MOCHEL');");
            db.execSQL("Insert into salesman values('M-VILLANUEVA');");
            db.execSQL("Insert into salesman values('MANCIO');");
            db.execSQL("Insert into salesman values('MANATO');");
            db.execSQL("Insert into salesman values('MARZAN');");
            db.execSQL("Insert into salesman values('MEDEL J');");
            db.execSQL("Insert into salesman values('MENDOZA');");
            db.execSQL("Insert into salesman values('MONCADA');");
            db.execSQL("Insert into salesman values('MULIT');");
            db.execSQL("Insert into salesman values('MATIN');");
            db.execSQL("Insert into salesman values('MONDERO');");
            db.execSQL("Insert into salesman values('MONTERO');");
            db.execSQL("Insert into salesman values('MAKILAN');");
            db.execSQL("Insert into salesman values('NAPOLES');");
            db.execSQL("Insert into salesman values('NEREZ');");
            db.execSQL("Insert into salesman values('NORBE');");
            db.execSQL("Insert into salesman values('NAVAREZ');");
            db.execSQL("Insert into salesman values('OMILA');");
            db.execSQL("Insert into salesman values('OSORNO');");
            db.execSQL("Insert into salesman values('OFFICE BASE');");
            db.execSQL("Insert into salesman values('OFFICE-GOV');");
            db.execSQL("Insert into salesman values('OMALAY');");
            db.execSQL("Insert into salesman values('ORTIZO');");
            db.execSQL("Insert into salesman values('PAPASIN');");
            db.execSQL("Insert into salesman values('PELIÑO');");
            db.execSQL("Insert into salesman values('PEPITO');");
            db.execSQL("Insert into salesman values('PERALTA-E');");
            db.execSQL("Insert into salesman values('PLANTATION');");
            db.execSQL("Insert into salesman values('PO');");
            db.execSQL("Insert into salesman values('PUERTO');");
            db.execSQL("Insert into salesman values('PUNO');");
            db.execSQL("Insert into salesman values('PADICIO');");
            db.execSQL("Insert into salesman values('PASAGUI');");
            db.execSQL("Insert into salesman values('PARBA');");
            db.execSQL("Insert into salesman values('PAGPAGUITAN');");
            db.execSQL("Insert into salesman values('PATAYON');");
            db.execSQL("Insert into salesman values('PALAR');");
            db.execSQL("Insert into salesman values('PESADO');");
            db.execSQL("Insert into salesman values('RASAY');");
            db.execSQL("Insert into salesman values('REQUINTO');");
            db.execSQL("Insert into salesman values('RETARDO');");
            db.execSQL("Insert into salesman values('ROXAS');");
            db.execSQL("Insert into salesman values('RODRIGUEZ');");
            db.execSQL("Insert into salesman values('SALUD');");
            db.execSQL("Insert into salesman values('SAMBAS');");
            db.execSQL("Insert into salesman values('SAN JUAN');");
            db.execSQL("Insert into salesman values('SARAMOSING');");
            db.execSQL("Insert into salesman values('SEBLOS');");
            db.execSQL("Insert into salesman values('SENOLOS');");
            db.execSQL("Insert into salesman values('SEPARA');");
            db.execSQL("Insert into salesman values('SISON-C');");
            db.execSQL("Insert into salesman values('SON');");
            db.execSQL("Insert into salesman values('SARONG');");
            db.execSQL("Insert into salesman values('SALUDARES');");
            db.execSQL("Insert into salesman values('SAMONTE');");
            db.execSQL("Insert into salesman values('SANTOS');");
            db.execSQL("Insert into salesman values('SERANIA');");
            db.execSQL("Insert into salesman values('SARENO');");
            db.execSQL("Insert into salesman values('SARCIA');");
            db.execSQL("Insert into salesman values('SUAREZ-A');");
            db.execSQL("Insert into salesman values('TADEJA');");
            db.execSQL("Insert into salesman values('TAN');");
            db.execSQL("Insert into salesman values('TANUDTANUD');");
            db.execSQL("Insert into salesman values('TANGGA-AN');");
            db.execSQL("Insert into salesman values('TARAY');");
            db.execSQL("Insert into salesman values('TARONGOY');");
            db.execSQL("Insert into salesman values('TRAZONA');");
            db.execSQL("Insert into salesman values('TIMONIO');");
            db.execSQL("Insert into salesman values('PAROHINOG');");
            db.execSQL("Insert into salesman values('TIAGA');");
            db.execSQL("Insert into salesman values('TORINO');");
            db.execSQL("Insert into salesman values('TURQUEZA');");
            db.execSQL("Insert into salesman values('TAYOTO');");
            db.execSQL("Insert into salesman values('YUNGAO');");
            db.execSQL("Insert into salesman values('YCOT');");
            db.execSQL("Insert into salesman values('EBO');");

            // LUBRICANTS SALESMAN
            db.execSQL("Insert into salesman values('ASID');");
            db.execSQL("Insert into salesman values('ASUBE');");
            db.execSQL("Insert into salesman values('ALVIAR');");
            db.execSQL("Insert into salesman values('ALMACIN');");
            db.execSQL("Insert into salesman values('ALEGARBES');");
            db.execSQL("Insert into salesman values('ASTACA-AN');");
            db.execSQL("Insert into salesman values('APOLINARIO');");
            db.execSQL("Insert into salesman values('BAYLON');");
            db.execSQL("Insert into salesman values('BALILI');");
            db.execSQL("Insert into salesman values('BUENAVENTURA');");
            db.execSQL("Insert into salesman values('BERTOLANO');");
            db.execSQL("Insert into salesman values('BASIGSIG');");
            db.execSQL("Insert into salesman values('BARTE');");
            db.execSQL("Insert into salesman values('CABAÑAS');");
            db.execSQL("Insert into salesman values('CAAB');");
            db.execSQL("Insert into salesman values('CANONIGO');");
            db.execSQL("Insert into salesman values('CAÑAS');");
            db.execSQL("Insert into salesman values('CENOJAS');");
            db.execSQL("Insert into salesman values('CLAROS');");
            db.execSQL("Insert into salesman values('CLARIZA');");
            db.execSQL("Insert into salesman values('CALOPE');");
            db.execSQL("Insert into salesman values('CASTILLONES');");
            db.execSQL("Insert into salesman values('DELACRUZ-ED');");
            db.execSQL("Insert into salesman values('ESTELLORE');");
            db.execSQL("Insert into salesman values('FUENTES');");
            db.execSQL("Insert into salesman values('GREGORIO');");
            db.execSQL("Insert into salesman values('GUZMAN');");
            db.execSQL("Insert into salesman values('HINAYON');");
            db.execSQL("Insert into salesman values('JABERTO');");
            db.execSQL("Insert into salesman values('JIMENEZ');");
            db.execSQL("Insert into salesman values('JEMINEZ');");
            db.execSQL("Insert into salesman values('LEGASPI');");
            db.execSQL("Insert into salesman values('LANSADIRAS');");
            db.execSQL("Insert into salesman values('MANACIO');");
            db.execSQL("Insert into salesman values('MILLOR');");
            db.execSQL("Insert into salesman values('MOCHEL');");
            db.execSQL("Insert into salesman values('MONDERO');");
            db.execSQL("Insert into salesman values('OMAPAS');");
            db.execSQL("Insert into salesman values('OLIVA');");
            db.execSQL("Insert into salesman values('ORTEGA');");
            db.execSQL("Insert into salesman values('ORILLO');");
            db.execSQL("Insert into salesman values('PARTORIZA');");
            db.execSQL("Insert into salesman values('PASION');");
            db.execSQL("Insert into salesman values('PETALLAR');");
            db.execSQL("Insert into salesman values('PODUNAS');");
            db.execSQL("Insert into salesman values('QUISMONDO');");
            db.execSQL("Insert into salesman values('ROMERO-K');");
            db.execSQL("Insert into salesman values('ROCHA');");
            db.execSQL("Insert into salesman values('SANDICO');");
            db.execSQL("Insert into salesman values('TABADA');");
            db.execSQL("Insert into salesman values('TARAY');");
            db.execSQL("Insert into salesman values('VELASCO');");
            db.execSQL("Insert into salesman values('VEGA');");

            // AGRICHEMICALS SALESMAN
            db.execSQL("Insert into salesman values('NARVACAN');");
            db.execSQL("Insert into salesman values('SON');");
            db.execSQL("Insert into salesman values('DUJALI');");
            db.execSQL("Insert into salesman values('CENON');");
            db.execSQL("Insert into salesman values('ALIMES');");
            db.execSQL("Insert into salesman values('SINOY');");
            db.execSQL("Insert into salesman values('ESPARAGOZA');");
            db.execSQL("Insert into salesman values('BERNARDINO');");
            db.execSQL("Insert into salesman values('BENANING');");
            db.execSQL("Insert into salesman values('VERANO');");
            db.execSQL("Insert into salesman values('LAGUIBAN');");
            db.execSQL("Insert into salesman values('LISTAM');");
            db.execSQL("Insert into salesman values('ALBUNA');");
            db.execSQL("Insert into salesman values('MACAILING');");
            db.execSQL("Insert into salesman values('MAGONCIA');");
            db.execSQL("Insert into salesman values('ALBUNA');");
            db.execSQL("Insert into salesman values('DANTE');");
            db.execSQL("Insert into salesman values('OBISO');");

            // SOLANE SALESMAN
            db.execSQL("Insert into salesman values('BIRAD');");
            db.execSQL("Insert into salesman values('FABROQUEZ');");
            db.execSQL("Insert into salesman values('ROXAS');");
            db.execSQL("Insert into salesman values('UBAS');");
            db.execSQL("Insert into salesman values('UY');");
            db.execSQL("Insert into salesman values('AGON');");
            db.execSQL("Insert into salesman values('LAMBAGO');");
            db.execSQL("Insert into salesman values('PINTON');");
            db.execSQL("Insert into salesman values('GILDORE');");
            db.execSQL("Insert into salesman values('AUTIDA');");
            db.execSQL("Insert into salesman values('GLORIA');");

            // MULTILINE SALESMANS
            // BTDT
            db.execSQL("Insert into salesman values('MESA');");
            db.execSQL("Insert into salesman values('SALAZAR');");
            db.execSQL("Insert into salesman values('BERAO');");
            db.execSQL("Insert into salesman values('SAJO');");
            db.execSQL("Insert into salesman values('BLANCO-I');");

            // RSD
            db.execSQL("Insert into salesman values('FALSARIO');");
            db.execSQL("Insert into salesman values('CABAYA');");

            // KAS01
            db.execSQL("Insert into salesman values('ESCOTE');");
            db.execSQL("Insert into salesman values('FERRER');");
            db.execSQL("Insert into salesman values('DEL ROSARIO');");

            // SUB SHOW ROOM
            db.execSQL("Insert into salesman values('MAA');");
            db.execSQL("Insert into salesman values('MINTAL');");

            db.execSQL("Insert into salesman values('OFFICE-LY');");

            // RAM
            db.execSQL("Insert into salesman values('OFFICE-RAM1');");
            db.execSQL("Insert into salesman values('OFFICE-RAM2');");
            db.execSQL("Insert into salesman values('BAGAY');");
            db.execSQL("Insert into salesman values('CATIPAY');");

        }
        cSalesman.close();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    private void InsertValidNumber() {

        Cursor cInsertVN = db.rawQuery("select * from validnumber", null);
        cInsertVN .moveToFirst();
        int cntInsertVN =  cInsertVN .getCount();
        if(cntInsertVN > 0) {
//            Toast.makeText(getApplicationContext(), "Inserted Valid Number: " + cntInsertVN, Toast.LENGTH_LONG).show();
            Log.d("VALID NUMBER", "Inserted Valid Number: " + cntInsertVN);
        } else {
            db.execSQL("Insert into validnumber values('+639173054435','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639177105901','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639177105906','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639177034043','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639177034045','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639177105900','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639758924251','PROGRAMMER');");
            db.execSQL("Insert into validnumber values('+639912127473','PROGRAMMER');");
        }
        cInsertVN.close();
    }
}



