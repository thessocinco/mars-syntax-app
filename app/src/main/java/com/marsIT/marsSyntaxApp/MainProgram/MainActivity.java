package com.marsIT.marsSyntaxApp.MainProgram;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.marsIT.marsSyntaxApp.BankMap.BankMap;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.CustomerData.AgrichemCustomerDetails;
import com.marsIT.marsSyntaxApp.CustomerData.AgrichemNewOtherCustomer;
import com.marsIT.marsSyntaxApp.CustomerData.ConsumerCustomerDetails;
import com.marsIT.marsSyntaxApp.CustomerData.ConsumerNewOtherCustomer;
import com.marsIT.marsSyntaxApp.CustomerData.CustomerDetails;
import com.marsIT.marsSyntaxApp.CustomerData.NewOtherCustomer;
import com.marsIT.marsSyntaxApp.CustomerData.OtherCustomer;
import com.marsIT.marsSyntaxApp.Inventory.SalesInventory;
import com.marsIT.marsSyntaxApp.InvoiceData.NewInvoice;
import com.marsIT.marsSyntaxApp.MarsAppStartUp.Startup;
import com.marsIT.marsSyntaxApp.SendReason.SendReason;
import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.AgriChemData;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.BeloData;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.MultiLinesData;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.LubricantsData;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.RamData;
import com.marsIT.marsSyntaxApp.VanSelling.VansellingLoadingPlan;
import com.marsIT.marsSyntaxApp.VanSelling.VansellingReturn;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.Century1Data;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.Century2Data;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.MontoscoData;
import com.marsIT.marsSyntaxApp.UpdateSQLpackages.UrcData;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

@SuppressLint("Recycle")
@SuppressWarnings("deprecation")
public class MainActivity extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    private final Handler mHandler = new Handler();

    @SuppressLint("StaticFieldLeak")
    protected static final Activity view = null;
    private String querycust;
    private String selectQuery;
    //    private String selectQuerymunicipality;
//    private String selectQuerysubpocket;
//    private String provincenum;
    private String municipalitychar;
    private String subpocketchar;
    private String customername;

    private String transinv;
    private String DeptCode;
    private String MapperCode;
    private String WeekSchedval;
    private String SalesmanIDval;
    private String Databasename;
    private String lockloc;

    int countcust;
//    Context context;
//    LayoutInflater inflater;

    private List<String> lables;
    private Spinner spcustomer;
//    private Spinner spprovince;
//    private Spinner spmunicipality;
//    private Spinner spsubpocket;

    private EditText editRemarks;
    private TextView tvaddress;
    private TextView tvschedcustomer;
    private TextView tvschedcustomeradd;

    private TextView custidselect;

//    List view
//    private ListView lv;

    private String theMobNo = "";
    private String TheMessages = "";

    private double startLatitude; //= 7.0137417;
    private double startLongitude; //= 125.4941049;
    private double endLatitude; //= 7.0136241;
    private double endLongitude; // = 125.4921331;
    private String custdistance_meter; // = 125.4921331;
    private TextView customerdistance;

    //    Listview Adapter
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private SQLiteDatabase db3;

    private Boolean loaddata;

    private Boolean PressLocation = false;
    private Boolean LoopGetL = false;
    private Boolean inrangelocation = false;

    // Search EditText
    EditText inputSearch;

//    ArrayList for Listview
//    ArrayList<HashMap<String, String>> productList;

    IntentFilter intentFilter;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    private String NewlongLoop = "0";
    private String NewLatLoop = "0";

    private String Newlong = "0";
    private String NewLat = "0";

    private String NewlongAdd = "0";
    private String NewLatAdd = "0";

    private String NewlongLess = "0";
    private String NewLatLess = "0";

    private String longitude = "";
    private String latitude = "";
    private String schedprocess = "";
    private String schedday = "";

    private LocationManager locManager;
    private final LocationListener locListener = new MyLocationListener();

    public String[] products = {"2KS MOTOR PARTS",};

    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // display the SMS received in the TextView
            // don't change or remove this lines
            String syntaxsms = Objects.requireNonNull(intent.getExtras()).getString("sms"); // message received
            String cellphonenumber = intent.getExtras().getString("cellnumber");

            Toast.makeText(getApplicationContext(), "RECEIVED! " + cellphonenumber, Toast.LENGTH_LONG).show();
            Log.d("BroadcastReceiver", "Received: " + cellphonenumber);

            try {
                Cursor queryvalidnumber = db.rawQuery("select * from validnumber where num = '" + cellphonenumber + "'", null);
                queryvalidnumber.moveToFirst();
                int cnt = queryvalidnumber.getCount();
                if (cnt > 0) {

                    String NumberOwner = queryvalidnumber.getString(1);

                    // String latval = syntaxsms;
                    String stchar = "";
                    assert syntaxsms != null;
                    int valqtylenth = syntaxsms.length();
                    if (valqtylenth > 6) {
                        for (int i = 0; i < 6; i++) {
                            char descr = syntaxsms.charAt(i);
                            stchar = stchar + descr;
                        }

                        // Toast.makeText(getApplicationContext(), "" + stchar + " by the: " + NumberOwner, Toast.LENGTH_LONG).show();

                        // String charitem = "";
                        String Compaire = "";
                        int counter = 0;
                        valqtylenth = syntaxsms.length();

                        if ("UPRICE".equals(stchar)) { // syntax for unlock price
                            String itemcode = "";
                            String unitprice = "";
                            String customercodep = "";
                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);
                                // stchar = stchar + descr;

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        itemcode = itemcode + descr; // get item code
                                    } else if (counter == 2) {
                                        unitprice = unitprice + descr; // get unit price
                                    } else if (counter == 3) {
                                        customercodep = customercodep + descr; // get unit price
                                    }
                                }
                            }

                            Cursor c2 = db.rawQuery("select * from products where productid = '" + itemcode + "'", null);
                            c2.moveToFirst();
                            cnt = c2.getCount();
                            if (cnt > 0) {
                                db.execSQL("Insert into itempriceoveride values('" + c2.getString(0) + "','" + customercodep + "'," + Double.parseDouble(unitprice) + ");");
                                // db.execSQL("Update Products set Status = 'U', overideprice = "+ unitprice +" where productid = '"+ itemcode +"';");
                                Toast.makeText(getApplicationContext(), "Price Overide " + c2.getString(1) + " by " + NumberOwner, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect Syntax, Item not found", Toast.LENGTH_LONG).show();
                            }

                            // end overide price syntax
                        } else if ("URECEI".equals(stchar)) {
                            String receivernum = "";
                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        receivernum = receivernum + descr; // get item code
                                    }
                                }
                            }

                            db.execSQL("Update receivernumber set num = '" + receivernum + "';");
                            Toast.makeText(getApplicationContext(), "Update Receiver number " + receivernum, Toast.LENGTH_LONG).show();

                            // DELSCH/FRI
                        } else if ("DELSCH".equals(stchar)) {
                            String daysDel = "";
                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        daysDel = daysDel + descr; // get item code
                                    }
                                }
                            }

                            db.execSQL("delete from  ScheduleCustomer where daysched = '" + daysDel + "';");
                            Toast.makeText(getApplicationContext(), "Schedule for " + daysDel + " was successfully deleted.", Toast.LENGTH_LONG).show();

                            // add valid number
                        } else if ("ADDNUM".equals(stchar)) {
                            String receivernum = "";
                            String name = "";
                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        receivernum = receivernum + descr; // get item code
                                    } else if (counter == 2) {
                                        name = name + descr; // get item code
                                    }
                                }
                            }

                            db.execSQL("Insert into validnumber values('" + receivernum + "','" + name + "');");
                            Toast.makeText(getApplicationContext(), "Successfully added the valid number " + receivernum, Toast.LENGTH_LONG).show();

                        } else if ("UNLOCK".equals(stchar)) {
                            String receivernum = "";
                            // String name = "";
                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("X".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        receivernum = receivernum + descr; // get item code
                                    }
                                }
                            }

                            db.execSQL("Insert into CustomerUnlock values('" + receivernum + "');");

                            Cursor QueryCust = db.rawQuery("select * from CUSTOMERS where cid = '" + receivernum + "'", null);
                            QueryCust.moveToFirst();
                            int cnt1 = QueryCust.getCount();
                            if (cnt1 > 0) {
//                                Toast.makeText(getApplicationContext(), "Customer ``"+ QueryCust.getString(1) +"`` was successfully Unlock", Toast.LENGTH_LONG).show();

                                String customerName = QueryCust.getString(1);

                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.drawable.mars_logo)  // Set the icon for the dialog
                                        .setTitle("Customer Unlock Status")  // Set the title of the dialog
                                        .setMessage("Customer \"" + customerName + "\" was successfully unlocked") // Concatenate the customer name into the message
                                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())  // Positive button to close the dialog
                                        .show();  // Display the dialog

                                selectQuery = "Select cname,CID from Customers where cid = '" + receivernum + "' ORDER BY CNAME";
                                loadSpinnerData();

                            } else {
//                                Toast.makeText(getApplicationContext(), "Customer was successfully Unlock", Toast.LENGTH_LONG).show();
                            }

                        } else if ("CREDIT".equals(stchar)) {
                            String customercode = "";
                            String creditlimit = "";
                            String balcredit = "";
                            String ocheck = "";
                            String CreditStatus1 = "0";

                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        customercode = customercode + descr; // get item code
                                    } else if (counter == 2) {
                                        creditlimit = creditlimit + descr;

                                    } else if (counter == 3) {
                                        balcredit = balcredit + descr;

                                    } else if (counter == 4) {
                                        ocheck = ocheck + descr;
                                    } else if (counter == 5) {
                                        CreditStatus1 = CreditStatus1 + descr; // Credit Status
                                    }
                                }
                            }

                            db.execSQL("Update customers set creditline = '" + Float.parseFloat(creditlimit) + "',balcredit = '" + balcredit + "',ocheck = '" + ocheck + "',CreditStatus = '" + CreditStatus1 + "' where cid = '" + customercode + "';");
                            Toast.makeText(getApplicationContext(), "Successfully Update New Credit line " + customercode, Toast.LENGTH_LONG).show();

                            // update long lat
                            // UPLOLA/CUSTOMER ID /7.12344/125.232323
                        } else if ("UPLOLA".equals(stchar)) {
                            String customercode = "";
                            String latitudeval = "";
                            String longitudeval = "";

                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        customercode = customercode + descr; // get item code
                                    } else if (counter == 2) {
                                        latitudeval = latitudeval + descr;

                                    } else if (counter == 3) {
                                        longitudeval = longitudeval + descr;
                                    }
                                }
                            }

                            db3 = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                            db3.execSQL("Update customers set latitude = '" + latitudeval + "' ,longitude = '" + longitudeval + "',status = 'M' WHERE CID = '" + customercode + "';");
                            db3.close();

                            Cursor QueryCust = db.rawQuery("select * from CUSTOMERS where cid = '" + customercode + "'", null);
                            QueryCust.moveToFirst();
                            int cnt1 = QueryCust.getCount();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Update Successful");
                            if (cnt1 > 0) {
//                                Toast.makeText(getApplicationContext(), "Customer Location Successfully Update ``" + QueryCust.getString(1), Toast.LENGTH_LONG).show();
                                builder.setMessage("Customer Location Successfully Updated " + QueryCust.getString(1));
                            } else {
//                                Toast.makeText(getApplicationContext(), "Customer Location Successfully Update: " + customercode, Toast.LENGTH_LONG).show();
                                builder.setMessage("Customer Location Successfully Updated " + customercode);
                            }
                            builder.setIcon(R.drawable.mars_logo);
                            builder.setPositiveButton("OK", null);
                            builder.show();

                            selectQuery = "Select cname,CID from Customers where cid = '" + customercode + "' ORDER BY CNAME";
                            loadSpinnerData();

                            // CUSTA/CUSTOMER ID /CUSTOMER NAME/STREET/municipality/LONG/LAT
                        } else if ("CUSTAD".equals(stchar)) {
                            String customercode_s = "";
                            String customer_name = "";
                            String STREET = "";
                            String municipality = "";
                            String latitudeval = "";
                            String longitudeval = "";

                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        customercode_s = customercode_s + descr; // get item code
                                    } else if (counter == 2) { // name
                                        customer_name = customer_name + descr;

                                    } else if (counter == 3) { // street
                                        STREET = STREET + descr;

                                    } else if (counter == 4) { // municipality
                                        municipality = municipality + descr;

                                    } else if (counter == 5) {
                                        longitudeval = longitudeval + descr;

                                    } else if (counter == 6) {
                                        latitudeval = latitudeval + descr;
                                    }
                                }
                            } // end for

                            // add customer
                            Cursor cd = db.rawQuery("select * from customers where CID like '" + customercode_s + "'", null);
                            countcust = cd.getCount();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Update Successful");
                            String message;
                            if (countcust > 0) {
                                db.execSQL("Update customers set street = '" + STREET + "',municipality = '" + municipality + "',Latitude = '" + latitudeval + "',Longitude = '" + longitudeval + "',status = 'M' where cid = '" + customercode_s + "';");
//                                Toast.makeText(getApplicationContext(), "Successfully Update Customer : " + customer_name, Toast.LENGTH_LONG).show();
                                message = "Successfully Added Customer " + customer_name;
                            } else {
                                db.execSQL("Insert into CUSTOMERS values('" + customercode_s + "','" + customer_name + "','SARI-SARI STORE','CASH on DUE','0','','1980-12-13','.','','','','.','','" + STREET + "','','" + municipality + "','','" + municipality + "','0','0','0','0','','0','0','0','0','0','0','0','0','0','" + latitudeval + "','" + longitudeval + "','M','15000.00','0.00','0.00','0');");
//                                Toast.makeText(getApplicationContext(), "Successfully Add Customer " + customer_name, Toast.LENGTH_LONG).show();
                                message = "Successfully Added Customer " + customer_name;
                            }
                            builder.setMessage(message);
                            builder.setIcon(R.drawable.mars_logo);
                            builder.setPositiveButton("OK", null);
                            builder.show();

                        } else if ("INSTAL".equals(stchar)) { // syntax for unlock price
                            String salesmanid = "";
                            String sellingtype = "";
                            String mapperid = "";
                            String departmentIns = "";
                            String weeksched = "";
                            String dataname = "";
                            String loclock = "";
                            String schedprocessS = "";
                            String schedday = "";

                            for (int i = 0; i < valqtylenth; i++) {
                                char descr = syntaxsms.charAt(i);
                                // stchar = stchar + descr;

                                Compaire = "" + descr;
                                if ("/".equals(Compaire)) {
                                    counter = counter + 1;
                                } else {
                                    if (counter == 1) {
                                        salesmanid = salesmanid + descr; // salesmanid
                                    } else if (counter == 2) {
                                        sellingtype = sellingtype + descr; // selling type van or booking
                                    } else if (counter == 3) {
                                        mapperid = mapperid + descr; // mapper id
                                    } else if (counter == 4) {
                                        departmentIns = departmentIns + descr; // department
                                    } else if (counter == 5) {
                                        weeksched = weeksched + descr; // week sched
                                    } else if (counter == 6) {
                                        dataname = dataname + descr; // if mars 1 or mars 2
                                    } else if (counter == 7) {
                                        loclock = loclock + descr; // location lock
                                    } else if (counter == 8) {
                                        schedprocessS = schedprocessS + descr; // SCHED PROCESS
                                    } else if (counter == 9) {
                                        schedday = schedday + descr; // SCHED day lock
                                    }
                                }
                            }

                            // db.execSQL("CREATE TABLE IF NOT EXISTS InstallValue(SalesmanID Varchar,SellingType varchar,MapperID Varchar,department varchar,weeksched varchar,statvalue varchar,databasename varchar, lock varchar,schedstatus varchar,schedday varchar);");
                            db.execSQL("Update InstallValue set SalesmanID = '" + salesmanid + "', SellingType = '" + sellingtype + "'," +
                                    " MapperID = '" + mapperid + "', department = '" + departmentIns + "', weeksched = '" + weeksched + "', statvalue = 'N', databasename = '" + dataname + "', lock = '" + loclock + "', schedstatus = '" + schedprocessS + "', schedday = '" + schedday + "';");


//                            Toast.makeText(getApplicationContext(), "Successfully Update Settings " + salesmanid + "','" + sellingtype + "'," +
//                                    "'" + mapperid + "','" + departmentIns + "','" + weeksched + "','N','" + dataname + "','" + loclock + "','" + schedprocessS, Toast.LENGTH_LONG).show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Settings Updated");

                            String message = "Successfully Updated Settings:\n\n"
                                    + "Salesman ID: " + salesmanid + "\n"
                                    + "Selling Type: " + sellingtype + "\n"
                                    + "Mapper ID: " + mapperid + "\n"
                                    + "Department: " + departmentIns + "\n"
                                    + "Week Schedule: " + weeksched + "\n"
                                    + "Flag: N\n"
                                    + "Data Name: " + dataname + "\n"
                                    + "Location Lock: " + loclock + "\n"
                                    + "Schedule Process: " + schedprocessS;

                            builder.setMessage(message);
                            builder.setIcon(R.drawable.mars_logo); // optional, if logo is available
                            builder.setPositiveButton("OK", null);
                            builder.show();

                            // db.execSQL("Insert into itempriceoveride values('"+ c2.getString(0) +"','"+ customercodep +"',"+ Double.parseDouble(unitprice) +");");
                        }
                    } // end if of checking valid number to have acces in systax
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() { // Register the Receiver
        super.onResume();
        registerReceiver(intentReceiver, intentFilter);

        ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS
        // Check again when the activity resumes in case developer options were turned on during the app usage
        if (isDeveloperOptionsEnabled(this)) {
            showBlockDeveloperOptionsDialog(this);
        }
        ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS

    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() { // Unregister the Receiver
        super.onPause();
        unregisterReceiver(intentReceiver);

        ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS
        // Recheck each time the app starts to ensure developer options are disabled
        if (isDeveloperOptionsEnabled(this)) {
            showBlockDeveloperOptionsDialog(this);
        }
        ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS

    }

//    private void checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
//                    1);
//        }
//    }

    ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS
    // Function to check if developer options are enabled
    @SuppressLint("NewApi") public static boolean isDeveloperOptionsEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // API 17+
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1;
        } else { // For API < 17, assume developer options are disabled
            return false;
        }
    }

    // Show a dialog to warn user and provide options
    public void showBlockDeveloperOptionsDialog(final Context context) {
        if (!(context instanceof Activity)) return;

        Activity activity = (Activity) context;
        if (activity.isFinishing() || activity.isDestroyed()) return;

        new Handler(Looper.getMainLooper()).post(() -> {
            if (activity.isFinishing() || activity.isDestroyed()) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(R.drawable.mars_logo);
            builder.setTitle("Developer Options Enabled!");
            builder.setMessage("Developer Options must be disabled to use this app. Please disable them in Settings.");

            builder.setPositiveButton("Go to Settings", (dialog, which) -> {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    context.startActivity(intent);
                    activity.finish(); // Finish after launching settings
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            builder.setNegativeButton("Exit App", (dialog, which) -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    activity.finish(); // Finish the activity on exit
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            builder.setCancelable(false);
            builder.show();
        });
    }

    ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        try {

            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            setupToolbar("MAVCI SYNTAX");
            // Force menu to appear
            invalidateOptionsMenu();

            ImageView searchfIcon = findViewById(R.id.searchfIcon);
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.icons8_search)
                    .into(searchfIcon);

            db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
            db2 = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            EditText etsam = findViewById(R.id.etsearch);

            tvschedcustomer = findViewById(R.id.tvschedcust);
            tvschedcustomeradd = findViewById(R.id.tvschedcustadd);

            custidselect = findViewById(R.id.tvCustIDm);
            custidselect.setText("");

            customerdistance = findViewById(R.id.customerdistance);
            customerdistance.setText("");

            db.execSQL("CREATE TABLE IF NOT EXISTS InstallValue(SalesmanID Varchar,SellingType varchar,MapperID Varchar,department varchar,weeksched varchar,statvalue varchar,databasename varchar, lock varchar,schedstatus varchar,schedday varchar);");

            SalesmanIDval = ""; // salesmanid
            transinv = ""; // selling type
            MapperCode = ""; // mapper code
            DeptCode = ""; // department code
            WeekSchedval = ""; // week schedule

            inputSearch = findViewById(R.id.etsearch);

            municipalitychar = "";
            subpocketchar = "";
            customername = "";

            spcustomer = findViewById(R.id.spcustomer);

            Button bshowschedule = findViewById(R.id.btshowschedule);
            bshowschedule.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                selectQuery = "Select CName,CUSTOMERID  from ScheduleCustomer S LEFT JOIN CUSTOMERS C ON s.CUSTOMERID = C.cid where S.salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "'  ORDER BY OrderVisit";
                loadSpinnerData();
            });

            Button bshowschedulenotvisited = findViewById(R.id.btshowschedulenotvisited);
            bshowschedulenotvisited.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                selectQuery = "Select CName,CUSTOMERID  from ScheduleCustomer S LEFT JOIN CUSTOMERS C ON s.CUSTOMERID = C.cid where status = '' and salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "' ORDER BY OrderVisit";
                loadSpinnerData();
            });

            // button for sched for the day
            Button bschedfortheday = findViewById(R.id.btshowschedulefortheday);
            bschedfortheday.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                // original
//                String dayss = "";
//                SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
//                String datetimesyntaxval = currentday.format(new Date());
//                dayss = datetimesyntaxval.toUpperCase();

                // updated
                SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                currentday.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dayss = currentday.format(new Date()).toUpperCase();

//                    Toast.makeText(getApplicationContext(), "Scheduled Day: " + dayss + ", " + WeekSchedval, Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.mars_logo)
                        .setTitle("Scheduled Day Information")
                        .setMessage("Scheduled Day: " + dayss + ", " + WeekSchedval)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();

                selectQuery = "Select CName,CUSTOMERID  from ScheduleCustomer S LEFT JOIN CUSTOMERS C ON s.CUSTOMERID = C.cid where salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "' and daysched = '" + dayss + "' ORDER BY OrderVisit";
                loadSpinnerData();
            });

            ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS
            // Check if developer options are enabled when the app starts
            if (isDeveloperOptionsEnabled(this)) {
                showBlockDeveloperOptionsDialog(this);
            }
            ///////////////////////////////////////////////////////////////// CODE DETECTION FOR DEVELOPER OPTIONS

            // function for google maps - opens google maps with a route (walking mode) - shows distance & path
            Button btnViewOnMap = findViewById(R.id.btnViewOnMap);
            btnViewOnMap.setOnClickListener(v -> {
                custgetdistance(); // fetch distance first
                openGoogleMaps(); // open google maps with stored customer location
            });

            // sp.setAdapter(adaptersp);
            // Toast.makeText(getApplicationContext(), "Load Data" , Toast.LENGTH_LONG).show();
            spcustomer.setOnItemSelectedListener(new OnItemSelectedListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (loaddata) {
                        try {
                            String tempstr = "" + spcustomer.getSelectedItem().toString();
                            if ("".equals(tempstr)) {
                                custidselect.setText(" ");
                            } else {
                                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                                Cursor c2 = db.rawQuery("select street,barangay,municipality,province,CID from Customers where cname like '" + tempstr + "' order by cname", null);
                                c2.moveToFirst();
                                int cnt2 = c2.getCount();
                                if (cnt2 > 0) {
                                    // Toast.makeText(getApplicationContext(), "" + tempstr, Toast.LENGTH_LONG).show();

                                    // spprovince.setSelection(Integer.parseInt(c2.getString(3)));
                                    String prov = "";
                                    prov = "" + c2.getString(3);

                                    if ("0".equals(prov)) {
                                        prov = "Davao Del Sur";
                                    } else if ("1".equals(prov)) {
                                        prov = "Davao Del Norte";
                                    } else if ("2".equals(prov)) {
                                        prov = "Davao Oriental";
                                    } else if ("3".equals(prov)) {
                                        prov = "Compostela Valley";
                                    } else if ("4".equals(prov)) {
                                        prov = "North Cotabato";
                                    } else if ("5".equals(prov)) {
                                        prov = "South Cotabato";
                                    } else if ("6".equals(prov)) {
                                        prov = "Sarangani";
                                    } else if ("7".equals(prov)) {
                                        prov = "Sultan Kudarat";
                                    } else if ("8".equals(prov)) {
                                        prov = "Maguindanao";
                                    } else if ("9".equals(prov)) {
                                        prov = "Agusan Del Sur";
                                    } else if ("10".equals(prov)) {
                                        prov = "Surigao Del Sur";
                                    } else {
                                        prov = "DAVAO DEL SUR";
                                    }

                                    // Toast.makeText(getApplicationContext(), "" + prov, Toast.LENGTH_LONG).show();
                                    String addresscust = "" + prov;
                                    tvaddress = (TextView) findViewById(R.id.tvaddress);
                                    custidselect.setText("" + c2.getString(4));
                                    // + c2.getString(0) + ", " + c2.getString(1) + ", " + c2.getString(2) + ", "
                                    tvaddress.setText("(" + c2.getString(4) + ") " + c2.getString(0) + ", " + c2.getString(1) + ", " + c2.getString(2) + ", " + addresscust);
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // MainActivity.this.adapter.getFilter().filter(cs);
                    // Toast.makeText(getApplicationContext(), "" + cs, Toast.LENGTH_LONG).show();

                    // customername = cs.toString();
                    // Select All Query

                    selectQuery = "SELECT  cname FROM customers where cname like '%" + cs + "%' ORDER BY CNAME";

                    loadSpinnerData();
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });

            // new invoice
            try {
                Button btnissuedinvoice = findViewById(R.id.btnIssueInvoice);
                btnissuedinvoice.setOnClickListener(v -> {
                    try {
                        boolean rangeMap = true;
                        SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                        SQLiteDatabase db2 = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

//                    EditText custidselect = findViewById(R.id.custidselect);
                        Spinner spcustomer = findViewById(R.id.spcustomer);
//                    TextView tvschedcustomer = findViewById(R.id.tvschedcustomer);

                        if (custidselect == null || spcustomer == null || tvschedcustomer == null) {
                            Toast.makeText(getApplicationContext(), "UI error: missing view(s)", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // original
//                        SimpleDateFormat datetimesyntax1 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
//                        String datetimesyntaxval2 = datetimesyntax1.format(new Date());

                        // updated
                        SimpleDateFormat datetimesyntax1 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                        datetimesyntax1.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String datetimesyntaxval2 = datetimesyntax1.format(new Date());

                        Cursor c23 = db2.rawQuery("SELECT * FROM DateTimeLastSyntax WHERE DTimeSyntax <= ?", new String[]{datetimesyntaxval2});
                        if (c23 != null && c23.moveToFirst()) {
                            db2.execSQL("UPDATE DateTimeLastSyntax SET DTimeSyntax = ?", new Object[]{datetimesyntaxval2});
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(R.drawable.mars_logo)
                                    .setTitle("Invalid Date/Time")
                                    .setMessage("Invalid System Date Time " + datetimesyntaxval2)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                    .show();
                            rangeMap = false;
                        }
                        if (c23 != null) c23.close();

                        querycust = custidselect.getText().toString();
                        Cursor cd = db.rawQuery("SELECT * FROM customers WHERE cid LIKE ? ORDER BY cname", new String[]{querycust});
                        countcust = cd != null ? cd.getCount() : 0;

                        if (countcust > 0 && cd.moveToFirst()) {
                            // === ORIGINAL LOGIC START ===
                            // No logic has been altered from here on

                            if ("M".equals(cd.getString(34))) {
                                int countcusts = 0;

                                if ("Y".equalsIgnoreCase(lockloc)) {
                                    Cursor QueryUnlock = db.rawQuery("SELECT * FROM customerUnlock WHERE CustomerID LIKE ?", new String[]{cd.getString(0)});
                                    countcusts = QueryUnlock.getCount();
                                    if (QueryUnlock != null) QueryUnlock.close();

                                    if (countcusts == 0) {
                                        Cursor cd3 = db.rawQuery("SELECT * FROM customers WHERE cid LIKE ? AND (" +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) OR " +
                                                        " (latitude LIKE ? AND longitude LIKE ?) ) ORDER BY CNAME",
                                                new String[]{
                                                        querycust,
                                                        "%" + NewLat + "%", "%" + Newlong + "%",
                                                        "%" + NewLatAdd + "%", "%" + Newlong + "%",
                                                        "%" + NewLatLess + "%", "%" + Newlong + "%",
                                                        "%" + NewLat + "%", "%" + NewlongAdd + "%",
                                                        "%" + NewLat + "%", "%" + NewlongLess + "%",
                                                        "%" + NewLatAdd + "%", "%" + NewlongAdd + "%",
                                                        "%" + NewLatAdd + "%", "%" + NewlongLess + "%",
                                                        "%" + NewLatLess + "%", "%" + NewlongAdd + "%",
                                                        "%" + NewLatLess + "%", "%" + NewlongLess + "%"
                                                });
                                        countcusts = cd3.getCount();
                                        if (cd3 != null) cd3.close();

                                        if ("IN".equals(custdistance_meter)) {
                                            if ("Y".equalsIgnoreCase(schedprocess)) {
                                                String schedCusttrans = tvschedcustomer.getText().toString();
                                                Cursor cd33 = db.rawQuery("SELECT * FROM ScheduleCustomer WHERE customerid LIKE ? AND weeksched = ? AND status = 'Y'", new String[]{querycust, WeekSchedval});
                                                countcusts = cd33.getCount();
                                                if (cd33 != null) cd33.close();

                                                if (countcusts == 0 && !"".equalsIgnoreCase(schedCusttrans) && !schedCusttrans.equalsIgnoreCase(cd.getString(1))) {
                                                    new AlertDialog.Builder(MainActivity.this)
                                                            .setIcon(R.drawable.mars_logo)
                                                            .setTitle("Incomplete Transaction")
                                                            .setMessage("Please complete the transaction for customer " + schedCusttrans + " before moving on to the next customer.")
                                                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                            .show();
                                                    rangeMap = false;
                                                }
                                            } else if ("Y".equalsIgnoreCase(schedday)) {
                                                String schedCusttrans = spcustomer.getSelectedItem().toString();
                                                SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                                                String dayss = currentday.format(new Date()).toUpperCase();

                                                Cursor cd333 = db.rawQuery("SELECT * FROM ScheduleCustomer WHERE salesmanid = ? AND weeksched = ? AND daysched = ? AND STATUS = ''",
                                                        new String[]{SalesmanIDval, WeekSchedval, dayss});
                                                countcusts = cd333.getCount();
                                                if (cd333 != null) cd333.close();

                                                if (countcusts > 0) {
                                                    Cursor cd33 = db.rawQuery("SELECT * FROM ScheduleCustomer WHERE CustomerID LIKE ? AND weeksched = ? AND daysched = ?",
                                                            new String[]{querycust, WeekSchedval, dayss});
                                                    if (!cd33.moveToFirst()) {
                                                        new AlertDialog.Builder(MainActivity.this)
                                                                .setIcon(R.drawable.mars_logo)
                                                                .setTitle("Not Scheduled")
                                                                .setMessage("This Customer " + schedCusttrans + " is not part of your scheduled day " + dayss + " visit, please check MCP.")
                                                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                                .show();
                                                        rangeMap = false;
                                                    }
                                                    if (cd33 != null) cd33.close();
                                                }
                                            }
                                        } else {
                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setIcon(R.drawable.mars_logo)
                                                    .setTitle("Out of Range")
                                                    .setMessage("You are out of range. You need to be within 5 meters of the customer before you can proceed with the transaction.")
                                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                    .show();
                                            rangeMap = false;
                                        }
                                    }
                                } else {
                                    if ("Y".equalsIgnoreCase(schedday)) {
                                        String schedCusttrans = spcustomer.getSelectedItem().toString();
                                        SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                                        String dayss = currentday.format(new Date()).toUpperCase();

                                        Cursor cd333 = db.rawQuery("SELECT * FROM ScheduleCustomer WHERE salesmanid = ? AND weeksched = ? AND daysched = ? AND STATUS = ''",
                                                new String[]{SalesmanIDval, WeekSchedval, dayss});
                                        countcusts = cd333.getCount();
                                        if (cd333 != null) cd333.close();

                                        if (countcusts > 0) {
                                            Cursor cd33 = db.rawQuery("SELECT * FROM ScheduleCustomer WHERE CustomerID LIKE ? AND weeksched = ? AND daysched = ?",
                                                    new String[]{querycust, WeekSchedval, dayss});
                                            if (!cd33.moveToFirst()) {
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setIcon(R.drawable.mars_logo)
                                                        .setTitle("Not Scheduled")
                                                        .setMessage("This Customer " + schedCusttrans + " is not part of your scheduled day " + dayss + " visit, please check MCP.")
                                                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                        .show();
                                                rangeMap = false;
                                            }
                                            if (cd33 != null) cd33.close();
                                        }
                                    }
                                }

                                if (rangeMap) {
                                    if ("26".equals(cd.getString(38))) {
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setIcon(R.drawable.mars_logo)
                                                .setTitle("Credit Line Blocked")
                                                .setMessage("This customer's credit line is blocked. Cash transaction only!")
                                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                .show();
                                    }

                                    Intent intent = new Intent(MainActivity.this, NewInvoice.class);
                                    db.execSQL("DELETE FROM PaymentDetailsTEMP");

                                    // original
//                                    String datetimesyntaxval = new SimpleDateFormat("Mddyyyy", Locale.getDefault()).format(new Date());

                                    // updated
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("Mddyyyy", Locale.getDefault());
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    String datetimesyntaxval = dateFormat.format(new Date());

                                    intent.putExtra("invnumber", "Booking".equals(transinv) ? datetimesyntaxval : "");
                                    intent.putExtra("termstatus", cd.getString(38));
                                    intent.putExtra("cidinv", cd.getString(0));
                                    intent.putExtra("cnameinv", cd.getString(1));
                                    intent.putExtra("termsinv", cd.getString(4));
                                    intent.putExtra("creditinv", cd.getString(35));
                                    intent.putExtra("balcreditinv", cd.getString(36));
                                    intent.putExtra("ocheckinv", cd.getString(37));
                                    intent.putExtra("transinv", transinv);
                                    intent.putExtra("salesmanidvalue", SalesmanIDval);
                                    intent.putExtra("Departmentcode", DeptCode);
                                    intent.putExtra("Databasename", Databasename);

                                    mHandler.removeCallbacks(loopgetlocation);

                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Proceeding to invoice", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.drawable.mars_logo)
                                        .setTitle("Customer Not Mapped")
                                        .setMessage("The Customer is not yet mapped. Please map the customer before you can issue an invoice.")
                                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                        .show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "No matching customer found.", Toast.LENGTH_LONG).show();
                        }

                        if (cd != null) cd.close();
                        db.close();
                        db2.close();

                    } catch (Exception e) {
                        Log.e("btnIssueInvoice", "Error: ", e);
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
//                throw new RuntimeException("ERROR FOR INVOICE", e);
                Log.e("ERROR FOR INVOICE", "ERROR FOR INVOICE", e);
            }

            Button btnviewinvoice = findViewById(R.id.btnviewinvoice);
            btnviewinvoice.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, SalesInventory.class);

                intent.putExtra("transaction", transinv); // cust id
                intent.putExtra("salesmanidvalue", SalesmanIDval); // oout salesmanid
                intent.putExtra("sellingtype", transinv); // oout salesmanid
                intent.putExtra("Departmentcode", DeptCode); // oout salesmanid
                // intent.putExtra("Databasename",Databasename); // oout salesmanid

                // mHandler.removeCallbacks(loopgetlocation);

                startActivity(intent);
            });

            // VANSELLING RETURNS
            Button btvansellingret = findViewById(R.id.btnvanreturn);
            btvansellingret.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if ("Van-selling".equalsIgnoreCase(transinv)) {
                    Intent intent = new Intent(MainActivity.this, VansellingReturn.class);

                    intent.putExtra("transaction", transinv); // cust id
                    intent.putExtra("salesmanidvalue", SalesmanIDval); // oout salesmanid
                    intent.putExtra("sellingtype", transinv); // oout salesmanid
                    intent.putExtra("Departmentcode", DeptCode); // oout salesmanid
                    intent.putExtra("Databasename", Databasename); // oout salesmanid

                    startActivity(intent);
                }
            });

            // button click customer no order
            Button btnoorder = findViewById(R.id.btnsendreason);
            btnoorder.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                try {
                    boolean rangeMap = true;

                    // original
//                    SimpleDateFormat datetimesyntax1 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
//                    String datetimesyntaxval2 = datetimesyntax1.format(new Date());

                    // updated
                    SimpleDateFormat datetimesyntax1 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                    datetimesyntax1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String datetimesyntaxval2 = datetimesyntax1.format(new Date());

                    // datetimesyntaxval2 = "07-26-2019 06:59:00";
                    SQLiteDatabase db2 = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

                    Cursor c23 = db2.rawQuery("select * from DateTimeLastSyntax WHERE DTimeSyntax <= '" + datetimesyntaxval2 + "'", null);
                    c23.moveToFirst();
                    int cnt = c23.getCount();
                    if (cnt > 0) {
                        // Toast.makeText(getApplicationContext(), "TRAP 1 UPDATE TIME" + datetimesyntaxval , Toast.LENGTH_LONG).show();

                        db2.execSQL("Update DateTimeLastSyntax set DTimeSyntax = '" + datetimesyntaxval2 + "' ;");
                    } else {
//                            Toast.makeText(getApplicationContext(), "Invalid System Date Time " + datetimesyntaxval2 , Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.mars_logo)
                                .setTitle("Invalid Date/Time")
                                .setMessage("Invalid System Date Time " + datetimesyntaxval2)
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                        rangeMap = false;
                    }

                    querycust = "" + custidselect.getText().toString();
                    Cursor cd = db.rawQuery("select * from customers where cid like '" + querycust + "'", null);
                    countcust = cd.getCount();

                    if (countcust > 0) {

                        cd.moveToFirst();
                        if ("M".equals(cd.getString(34))) {

                            int countcusts = 0;

                            if ("Y".equalsIgnoreCase(lockloc)) {
                                Cursor QueryUnlock = db.rawQuery("select * from customerUnlock where CustomerID like '" + cd.getString(0) + "'", null);
                                countcusts = QueryUnlock.getCount();
                                if (countcusts > 0) {
                                } else {
                                    Cursor cd3 = db.rawQuery("select * from customers where cid like '" + querycust + "' and ( " +
                                            "  (latitude like '%" + NewLat + "%' and longitude like '%" + Newlong + "%') or " +
                                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + Newlong + "%') or " +
                                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + Newlong + "%') or " +
                                            " (latitude like '%" + NewLat + "%' and longitude like '%" + NewlongAdd + "%') or " +
                                            " (latitude like '%" + NewLat + "%' and longitude like '%" + NewlongLess + "%') or " +
                                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + NewlongAdd + "%') or " +
                                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + NewlongLess + "%') or " +
                                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + NewlongAdd + "%') or " +
                                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + NewlongLess + "%') )  " +
                                            " ORDER BY CNAME ", null);
                                    countcusts = cd3.getCount();
                                    // if(countcusts > 0 || custdistance_meter == "IN"  ){
                                    if (custdistance_meter == "IN") {

                                        if ("Y".equalsIgnoreCase(schedprocess)) {
                                            String schedCusttrans = "";
                                            schedCusttrans = tvschedcustomer.getText().toString();

                                            // selectQuery = "Select * from ScheduleCustomer  where cname like '" + querycust + "' and salesmanid = '"+ SalesmanIDval +"' and weeksched = '"+ WeekSchedval +"' and daysched = '"+ dayss +"' and status = 'Y' ";

                                            Cursor cd33 = db.rawQuery("Select * from ScheduleCustomer  where CustomerID like '" + querycust + "' and weeksched = '" + WeekSchedval + "' and status = 'Y' ", null);
                                            countcusts = cd33.getCount();
                                            if (countcusts > 0) {
                                            } else {

                                                if ("".equalsIgnoreCase(schedCusttrans)) {
                                                    // Toast.makeText(getApplicationContext(), " No customer.", Toast.LENGTH_LONG).show();
                                                } else {
                                                    if (schedCusttrans.equalsIgnoreCase(cd.getString(1))) {
                                                    } else {
//                                                            Toast.makeText(getApplicationContext(), "Please complete the transaction for customer " + schedCusttrans + " before moving on to the next customer.", Toast.LENGTH_LONG).show();
                                                        new AlertDialog.Builder(MainActivity.this)
                                                                .setIcon(R.drawable.mars_logo)
                                                                .setTitle("Incomplete Transaction")
                                                                .setMessage("Please complete the transaction for customer " + schedCusttrans + " before moving on to the next customer.")
                                                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                                .show();
                                                        rangeMap = false;
                                                    }
                                                }
                                            }
                                        } else if ("Y".equalsIgnoreCase(schedday)) {
                                            String schedCusttrans = "";
                                            schedCusttrans = spcustomer.getSelectedItem().toString();

                                            // selectQuery = "Select * from ScheduleCustomer  where cname like '" + querycust + "' and salesmanid = '"+ SalesmanIDval +"' and weeksched = '"+ WeekSchedval +"' and daysched = '"+ dayss +"' and status = 'Y' ";

                                            // original
//                                            String dayss = "";
//                                            SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
//                                            String datetimesyntaxval = currentday.format(new Date());
//                                            dayss = datetimesyntaxval.toUpperCase();

                                            // updated
                                            SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                                            currentday.setTimeZone(TimeZone.getTimeZone("UTC"));
                                            String dayss = currentday.format(new Date()).toUpperCase();

                                            Cursor cd333 = db.rawQuery("Select * from ScheduleCustomer  where  salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "'  and daysched = '" + dayss + "' AND STATUS = '' ", null);
                                            countcusts = cd333.getCount();
                                            if (countcusts > 0) {

                                                Cursor cd33 = db.rawQuery("Select * from ScheduleCustomer  where CustomerID like '" + querycust + "' and weeksched = '" + WeekSchedval + "'  and daysched = '" + dayss + "' ", null);
                                                countcusts = cd33.getCount();
                                                Toast.makeText(getApplicationContext(), "This Customer  " + querycust, Toast.LENGTH_LONG).show();
                                                if (countcusts > 0) {
                                                } else {
//                                                        Toast.makeText(getApplicationContext(), "This Customer " + schedCusttrans + " not part of your scheduled day " + dayss +" visit, please check mcp.", Toast.LENGTH_LONG).show();
                                                    new AlertDialog.Builder(MainActivity.this)
                                                            .setIcon(R.drawable.mars_logo)
                                                            .setTitle("Not Scheduled")
                                                            .setMessage("This Customer " + schedCusttrans + " is not part of your scheduled day " + dayss + " visit, please check MCP.")
                                                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                            .show();
                                                    rangeMap = false;
                                                }
                                            }
                                        } // end day sched lock

                                    } else {
//                                            Toast.makeText(getApplicationContext(), "You are out of Range, You need to be within 5 meters of the customer before you can proceed with the transaction.", Toast.LENGTH_LONG).show();
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setIcon(R.drawable.mars_logo)
                                                .setTitle("Out of Range")
                                                .setMessage("You are out of range. You need to be within 5 meters of the customer before you can proceed with the transaction.")
                                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                .show();
                                        rangeMap = false;
                                    }
                                }
                            } else {

                                if ("Y".equalsIgnoreCase(schedday)) {
                                    String schedCusttrans = "";
                                    schedCusttrans = spcustomer.getSelectedItem().toString();

                                    // selectQuery = "Select * from ScheduleCustomer  where cname like '" + querycust + "' and salesmanid = '"+ SalesmanIDval +"' and weeksched = '"+ WeekSchedval +"' and daysched = '"+ dayss +"' and status = 'Y' ";

                                    // original
//                                    String dayss = "";
//                                    SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
//                                    String datetimesyntaxval = currentday.format(new Date());
//                                    dayss = datetimesyntaxval.toUpperCase();

                                    // updated
                                    SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                                    currentday.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    String dayss = currentday.format(new Date()).toUpperCase();

                                    Cursor cd333 = db.rawQuery("Select * from ScheduleCustomer  where  salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "'  and daysched = '" + dayss + "' AND STATUS = '' ", null);
                                    countcusts = cd333.getCount();
                                    if (countcusts > 0) {
                                        Cursor cd33 = db.rawQuery("Select * from ScheduleCustomer  where CustomerID like '" + querycust + "' and weeksched = '" + WeekSchedval + "'  and daysched = '" + dayss + "' ", null);
                                        countcusts = cd33.getCount();
                                        if (countcusts > 0) {
                                        } else {
//                                                Toast.makeText(getApplicationContext(), "This Customer " + schedCusttrans + " not part of your scheduled day " + dayss +" visit, please check mcp.", Toast.LENGTH_LONG).show();
                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setIcon(R.drawable.mars_logo)
                                                    .setTitle("Not Scheduled")
                                                    .setMessage("This Customer " + schedCusttrans + " is not part of your scheduled day " + dayss + " visit, please check MCP.")
                                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                                    .show();
                                            rangeMap = false;
                                        }
                                    }
                                } // end day sched lock
                            }

                            if (rangeMap) {

                                mHandler.removeCallbacks(loopgetlocation);

                                Intent intent = new Intent(MainActivity.this, SendReason.class);

                                querycust = cd.getString(0);
                                intent.putExtra("cidinv", querycust); // cust id
                                querycust = cd.getString(1);
                                intent.putExtra("cnameinv", querycust); // name
                                intent.putExtra("Databasename", Databasename); // oout Databasename
                                intent.putExtra("Department", DeptCode); // oout Databasename
                                startActivity(intent);

                                // MainActivity.this.finish();
                            }
                        } else {
                            if ("VISITOTHER".equalsIgnoreCase(cd.getString(0))) {
                                Intent intent = new Intent(MainActivity.this, SendReason.class);

                                querycust = cd.getString(0);
                                intent.putExtra("cidinv", querycust); // cust id
                                querycust = cd.getString(1);
                                intent.putExtra("cnameinv", querycust); // name
                                intent.putExtra("Databasename", Databasename); // oout Databasename
                                intent.putExtra("Department", DeptCode); // oout Databasename
                                startActivity(intent);
                            } else {
//                                    Toast.makeText(getApplicationContext(),"The Customer not yet been Map, Please Map the Customer before you can send reason!" ,Toast.LENGTH_LONG).show();
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Customer Not Mapped")
                                        .setMessage("The customer has not been mapped yet. Please map the customer before you can send a reason.")
                                        .setIcon(R.drawable.mars_logo)
                                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                        .show();
                            }
                        }
                    } else {
//                            Toast.makeText(getApplicationContext(),"Customer Not Found!" ,Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.mars_logo)
                                .setTitle("Customer Not Found")
                                .setMessage("No customer selected. Please select and try again.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                }
            });

            // Toast.makeText(getApplicationContext(), "Trap", Toast.LENGTH_LONG).show();
            Button bdetails = findViewById(R.id.btnShowDetails);
            bdetails.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                // startActivity(new Intent(MainActivity.this, CustomerDetails.class));
                // adapter = new ArrayAdapter<String>(this, R.layout.search, R.id.textsearch, products);

                // lv.setAdapter(adapter);

                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                // db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                querycust = "" + spcustomer.getSelectedItem().toString();
                Cursor cd = db.rawQuery("select * from customers where cname like '" + querycust + "'", null);
                countcust = cd.getCount();

                if (countcust > 0) {
                    cd.moveToFirst();
                    if ("L".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, CustomerDetails.class);

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        querycust = cd.getString(17);            // Sub Pocket
                        intent.putExtra("subpocket", querycust);
                        querycust = cd.getString(18);            // if have caltex
                        intent.putExtra("caltex", querycust);
                        querycust = cd.getString(19);            // if have Mobil
                        intent.putExtra("mobil", querycust);
                        querycust = cd.getString(20);            // if have Castrol
                        intent.putExtra("castrol", querycust);
                        querycust = cd.getString(21);            // if have petron
                        intent.putExtra("petron", querycust);
                        querycust = cd.getString(22);            // if have others
                        intent.putExtra("others", querycust);
                        querycust = cd.getString(23);            // if have caf
                        intent.putExtra("caf", querycust);
                        querycust = cd.getString(24);            // if have dti
                        intent.putExtra("dti", querycust);
                        querycust = cd.getString(25);            // if have business permit
                        intent.putExtra("bpermit", querycust);
                        querycust = cd.getString(26);            // if have signage
                        intent.putExtra("signage", querycust);
                        querycust = cd.getString(27);            // if have poster
                        intent.putExtra("poster", querycust);
                        querycust = cd.getString(28);            // if have streamer
                        intent.putExtra("streamer", querycust);
                        querycust = cd.getString(29);            // if have sticker
                        intent.putExtra("sticker", querycust);
                        querycust = cd.getString(30);            // if have display rack
                        intent.putExtra("disrack", querycust);
                        querycust = cd.getString(31);            // if have product display
                        intent.putExtra("prodisplay", querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);

                        startActivity(intent);
                    } else if ("AG".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, AgrichemCustomerDetails.class);

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        //	querycust = cd.getString(17);    		// Sub Pocket
                        //	intent.putExtra("subpocket",querycust);
                        //	querycust = cd.getString(18);    		// if have caltex
                        //	intent.putExtra("caltex",querycust);
                        //	querycust = cd.getString(19);    		// if have Mobil
                        //	intent.putExtra("mobil",querycust);
                        //	querycust = cd.getString(20);    		// if have Castrol
                        //	intent.putExtra("castrol",querycust);
                        //	querycust = cd.getString(21);    		// if have petron
                        //	intent.putExtra("petron",querycust);
                        //		querycust = cd.getString(22);    	// if have others
                        //	intent.putExtra("others",querycust);
                        //	querycust = cd.getString(23);    		// if have caf
                        //		intent.putExtra("caf",querycust);
                        //	querycust = cd.getString(24);    		// if have dti
                        //		intent.putExtra("dti",querycust);
                        //		querycust = cd.getString(25);    	// if have business permit
                        //	intent.putExtra("bpermit",querycust);
                        //	querycust = cd.getString(26);    		// if have signage
                        //	intent.putExtra("signage",querycust);
                        //	querycust = cd.getString(27);    		// if have poster
                        //	intent.putExtra("poster",querycust);
                        //	querycust = cd.getString(28);    		// if have streamer
                        //	intent.putExtra("streamer",querycust);
                        //	querycust = cd.getString(29);    		// if have sticker
                        //	intent.putExtra("sticker",querycust);
                        //	querycust = cd.getString(30);    		// if have display rack
                        //	intent.putExtra("disrack",querycust);
                        //	querycust = cd.getString(31);    		// if have product display
                        //	intent.putExtra("prodisplay",querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);

                        startActivity(intent);

                    } else if ("UC".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, ConsumerCustomerDetails.class);

                        if ("Y".equalsIgnoreCase(lockloc)) {
                            if ("M".equals(cd.getString(34))) {
                                intent.putExtra("DisableToSend", "Y");
                            } else {
                                intent.putExtra("DisableToSend", "N");
                            }
                        } else {
                            intent.putExtra("DisableToSend", "N");
                        }

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        //	querycust = cd.getString(17);    		// Sub Pocket
                        //	intent.putExtra("subpocket",querycust);
                        //	querycust = cd.getString(18);    		// if have caltex
                        //	intent.putExtra("caltex",querycust);
                        //	querycust = cd.getString(19);    		// if have Mobil
                        //	intent.putExtra("mobil",querycust);
                        //	querycust = cd.getString(20);    		// if have Castrol
                        //	intent.putExtra("castrol",querycust);
                        //	querycust = cd.getString(21);    		// if have petron
                        //	intent.putExtra("petron",querycust);
                        querycust = cd.getString(22);            // if have others
                        intent.putExtra("others", querycust);
                        //	querycust = cd.getString(23);    		// if have caf
                        //		intent.putExtra("caf",querycust);
                        //	querycust = cd.getString(24);    		// if have dti
                        //		intent.putExtra("dti",querycust);
                        //		querycust = cd.getString(25);    	// if have business permit
                        //	intent.putExtra("bpermit",querycust);
                        //	querycust = cd.getString(26);    		// if have signage
                        //	intent.putExtra("signage",querycust);
                        //	querycust = cd.getString(27);    		// if have poster
                        //	intent.putExtra("poster",querycust);
                        //	querycust = cd.getString(28);    		// if have streamer
                        //	intent.putExtra("streamer",querycust);
                        //	querycust = cd.getString(29);    		// if have sticker
                        //	intent.putExtra("sticker",querycust);
                        //	querycust = cd.getString(30);    		// if have display rack
                        //	intent.putExtra("disrack",querycust);
                        //	querycust = cd.getString(31);    		// if have product display
                        //	intent.putExtra("prodisplay",querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);
                        intent.putExtra("Databasename", Databasename); // oout Databasename

                        startActivity(intent);

                    } else if ("PL".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, ConsumerCustomerDetails.class);

                        if ("Y".equalsIgnoreCase(lockloc)) {
                            if ("M".equals(cd.getString(34))) {
                                intent.putExtra("DisableToSend", "Y");
                            } else {
                                intent.putExtra("DisableToSend", "N");
                            }
                        } else {
                            intent.putExtra("DisableToSend", "N");
                        }

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        //	querycust = cd.getString(17);    		// Sub Pocket
                        //	intent.putExtra("subpocket",querycust);
                        //	querycust = cd.getString(18);    		// if have caltex
                        //	intent.putExtra("caltex",querycust);
                        //	querycust = cd.getString(19);    		// if have Mobil
                        //	intent.putExtra("mobil",querycust);
                        //	querycust = cd.getString(20);    		// if have Castrol
                        //	intent.putExtra("castrol",querycust);
                        //	querycust = cd.getString(21);    		// if have petron
                        //	intent.putExtra("petron",querycust);
                        querycust = cd.getString(22);            // if have others
                        intent.putExtra("others", querycust);
                        //	querycust = cd.getString(23);    		// if have caf
                        //		intent.putExtra("caf",querycust);
                        //	querycust = cd.getString(24);    		// if have dti
                        //		intent.putExtra("dti",querycust);
                        //		querycust = cd.getString(25);    	// if have business permit
                        //	intent.putExtra("bpermit",querycust);
                        //	querycust = cd.getString(26);    		// if have signage
                        //	intent.putExtra("signage",querycust);
                        //	querycust = cd.getString(27);    		// if have poster
                        //	intent.putExtra("poster",querycust);
                        //	querycust = cd.getString(28);    		// if have streamer
                        //	intent.putExtra("streamer",querycust);
                        //	querycust = cd.getString(29);    		// if have sticker
                        //	intent.putExtra("sticker",querycust);
                        //	querycust = cd.getString(30);    		// if have display rack
                        //	intent.putExtra("disrack",querycust);
                        //	querycust = cd.getString(31);    		// if have product display
                        //	intent.putExtra("prodisplay",querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);
                        intent.putExtra("Databasename", Databasename); // oout Databasename

                        startActivity(intent);

                    } else if ("CC".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, ConsumerCustomerDetails.class);

                        if ("Y".equalsIgnoreCase(lockloc)) {
                            if ("M".equals(cd.getString(34))) {
                                intent.putExtra("DisableToSend", "Y");
                            } else {
                                intent.putExtra("DisableToSend", "N");
                            }
                        } else {
                            intent.putExtra("DisableToSend", "N");
                        }

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        //	querycust = cd.getString(17);    		// Sub Pocket
                        //	intent.putExtra("subpocket",querycust);
                        //	querycust = cd.getString(18);    		// if have caltex
                        //	intent.putExtra("caltex",querycust);
                        //	querycust = cd.getString(19);    		// if have Mobil
                        //	intent.putExtra("mobil",querycust);
                        //	querycust = cd.getString(20);    		// if have Castrol
                        //	intent.putExtra("castrol",querycust);
                        //	querycust = cd.getString(21);    		// if have petron
                        //	intent.putExtra("petron",querycust);
                        querycust = cd.getString(22);            // if have others
                        intent.putExtra("others", querycust);
                        //	querycust = cd.getString(23);    		// if have caf
                        //		intent.putExtra("caf",querycust);
                        //	querycust = cd.getString(24);    		// if have dti
                        //		intent.putExtra("dti",querycust);
                        //		querycust = cd.getString(25);    	// if have business permit
                        //	intent.putExtra("bpermit",querycust);
                        //	querycust = cd.getString(26);    		// if have signage
                        //	intent.putExtra("signage",querycust);
                        //	querycust = cd.getString(27);    		// if have poster
                        //	intent.putExtra("poster",querycust);
                        //	querycust = cd.getString(28);    		// if have streamer
                        //	intent.putExtra("streamer",querycust);
                        //	querycust = cd.getString(29);    		// if have sticker
                        //	intent.putExtra("sticker",querycust);
                        //	querycust = cd.getString(30);    		// if have display rack
                        //	intent.putExtra("disrack",querycust);
                        //	querycust = cd.getString(31);    		// if have product display
                        //	intent.putExtra("prodisplay",querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);
                        intent.putExtra("Databasename", Databasename); // oout Databasename

                        startActivity(intent);

                    } else if ("MI".equals(DeptCode)) {

                        querycust = cd.getString(0);
                        // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, ConsumerCustomerDetails.class);

                        if ("Y".equalsIgnoreCase(lockloc)) {
                            if ("M".equals(cd.getString(34))) {
                                intent.putExtra("DisableToSend", "Y");
                            } else {
                                intent.putExtra("DisableToSend", "N");
                            }
                        } else {
                            intent.putExtra("DisableToSend", "N");
                        }

                        intent.putExtra("cid", querycust);
                        querycust = cd.getString(1);
                        intent.putExtra("cname", querycust);
                        querycust = cd.getString(2);            // customer type
                        intent.putExtra("ctype", querycust);
                        querycust = cd.getString(3);            // account type
                        intent.putExtra("atype", querycust);
                        querycust = cd.getString(4);            // Terms
                        intent.putExtra("terms", querycust);
                        querycust = cd.getString(5);            // Contact Person
                        intent.putExtra("cperson", querycust);
                        querycust = "" + cd.getString(6);        // Contact person Bday
                        intent.putExtra("cbday", querycust);
                        querycust = cd.getString(7);            // Contact Person tell Number
                        intent.putExtra("ctellnum", querycust);
                        querycust = cd.getString(8);            // Contact Person Cell Number
                        intent.putExtra("ccellnum", querycust);
                        querycust = cd.getString(9);            // Owner Name
                        intent.putExtra("owner", querycust);
                        querycust = "" + cd.getString(10);      // Owner Name  bday
                        intent.putExtra("obday", querycust);
                        querycust = cd.getString(11);            // Owner Name  tell number
                        intent.putExtra("otellnum", querycust);
                        querycust = cd.getString(12);            // Owner Name
                        intent.putExtra("ocellnum", querycust);
                        querycust = cd.getString(13);            // Street
                        intent.putExtra("street", querycust);
                        querycust = cd.getString(14);            // Barangay
                        intent.putExtra("barangay", querycust);
                        querycust = cd.getString(15);            // Municipality
                        intent.putExtra("municipality", querycust);
                        querycust = cd.getString(16);            // Province
                        intent.putExtra("province", querycust);
                        //	querycust = cd.getString(17);    		// Sub Pocket
                        //	intent.putExtra("subpocket",querycust);
                        //	querycust = cd.getString(18);    		// if have caltex
                        //	intent.putExtra("caltex",querycust);
                        //	querycust = cd.getString(19);    		// if have Mobil
                        //	intent.putExtra("mobil",querycust);
                        //	querycust = cd.getString(20);    		// if have Castrol
                        //	intent.putExtra("castrol",querycust);
                        //	querycust = cd.getString(21);    		// if have petron
                        //	intent.putExtra("petron",querycust);
                        querycust = cd.getString(22);            // if have others
                        intent.putExtra("others", querycust);
                        //	querycust = cd.getString(23);    		// if have caf
                        //		intent.putExtra("caf",querycust);
                        //	querycust = cd.getString(24);    		// if have dti
                        //		intent.putExtra("dti",querycust);
                        //		querycust = cd.getString(25);    	// if have business permit
                        //	intent.putExtra("bpermit",querycust);
                        //	querycust = cd.getString(26);    		// if have signage
                        //	intent.putExtra("signage",querycust);
                        //	querycust = cd.getString(27);    		// if have poster
                        //	intent.putExtra("poster",querycust);
                        //	querycust = cd.getString(28);    		// if have streamer
                        //	intent.putExtra("streamer",querycust);
                        //	querycust = cd.getString(29);    		// if have sticker
                        //	intent.putExtra("sticker",querycust);
                        //	querycust = cd.getString(30);    		// if have display rack
                        //	intent.putExtra("disrack",querycust);
                        //	querycust = cd.getString(31);    		// if have product display
                        //	intent.putExtra("prodisplay",querycust);

                        querycust = cd.getString(32);            // latitude
                        intent.putExtra("lati", querycust);

                        querycust = cd.getString(33);            // longitude
                        intent.putExtra("longi", querycust);
                        intent.putExtra("Databasename", Databasename); // oout Databasename

                        startActivity(intent);
                    }
                    // startActivity(new Intent(MainActivity.this, CustomerDetails.class));
                }
                db.close();
            });

            Button btNewOtherCustomer = findViewById(R.id.btnNewOtherCustomer);
            btNewOtherCustomer.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                if ("L".equals(DeptCode)) {
                    Intent intent = new Intent(MainActivity.this, NewOtherCustomer.class);
                    intent.putExtra("mapcode", MapperCode);
                    startActivity(intent);
                } else if ("UC".equals(DeptCode)) {
                    Intent intent = new Intent(MainActivity.this, ConsumerNewOtherCustomer.class);
                    intent.putExtra("mapcode", MapperCode);
                    startActivity(intent);
                } else if ("CC".equals(DeptCode)) {
                    Intent intent = new Intent(MainActivity.this, ConsumerNewOtherCustomer.class);
                    intent.putExtra("mapcode", MapperCode);
                    startActivity(intent);
                } else if ("PL".equals(DeptCode)) {
                    Intent intent = new Intent(MainActivity.this, ConsumerNewOtherCustomer.class);
                    intent.putExtra("mapcode", MapperCode);
                    startActivity(intent);
                } else if ("AG".equals(DeptCode)) {
                    Intent intent = new Intent(MainActivity.this, AgrichemNewOtherCustomer.class);
                    intent.putExtra("mapcode", MapperCode);
                    startActivity(intent);
                }
            });

            // VANSEELING LOADING PLAN
            // btnvansellingloadingplan
            Button btnvansellingplan = findViewById(R.id.btnvansellingloadingplan);
            btnvansellingplan.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, VansellingLoadingPlan.class);
                startActivity(intent);
            });

            Button btnbankmap = findViewById(R.id.btnbankmaps);
            btnvansellingplan.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, BankMap.class);
                startActivity(intent);
            });

            Button botherdetails = findViewById(R.id.btnOtherCustomerdtl);
            botherdetails.setOnClickListener(v -> {
                // TODO Auto-generated method stub

                // startActivity(new Intent(MainActivity.this, CustomerDetails.class));
                // adapter = new ArrayAdapter<String>(this, R.layout.search, R.id.textsearch, products);
                // lv.setAdapter(adapter);

                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                // db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                querycust = "" + spcustomer.getSelectedItem().toString();
                // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                Cursor cd = db.rawQuery("select * from othercustomers where cname like '%" + querycust + "%'", null);
                countcust = cd.getCount();

                if (countcust > 0) {
                    cd.moveToFirst();
                    querycust = cd.getString(0);
                    // Toast.makeText(getApplicationContext(), "" + querycust, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, OtherCustomer.class);

                    intent.putExtra("cid", querycust);
                    querycust = cd.getString(1);
                    intent.putExtra("cname", querycust);
                    querycust = cd.getString(2);            // customer type
                    intent.putExtra("ctype", querycust);

                    querycust = cd.getString(3);            // Contact Person
                    intent.putExtra("cperson", querycust);

                    querycust = cd.getString(4);            // Contact Person tell Number
                    intent.putExtra("ctellnum", querycust);
                    querycust = cd.getString(5);            // Contact Person Cell Number
                    intent.putExtra("ccellnum", querycust);
                    querycust = cd.getString(6);            // Street
                    intent.putExtra("street", querycust);
                    querycust = cd.getString(7);            // Barangay
                    intent.putExtra("barangay", querycust);
                    querycust = cd.getString(8);            // Municipality
                    intent.putExtra("municipality", querycust);
                    querycust = cd.getString(9);            // Province
                    intent.putExtra("province", querycust);

                    querycust = cd.getString(10);            // latitude
                    intent.putExtra("lati", querycust);

                    querycust = cd.getString(11);            // longitude
                    intent.putExtra("longi", querycust);

                    startActivity(intent);
                    // startActivity(new Intent(MainActivity.this, CustomerDetails.class));
                }
                db.close();
            });

            // if(v.getId() == R.id.btnShowDetails){
            //		startActivity(new Intent(MainActivity.this, CustomerDetails.class));
            // }

            // Toast.makeText(getApplicationContext(), "Record " , Toast.LENGTH_LONG).show();
            db2.execSQL("CREATE TABLE IF NOT EXISTS DateTimeLastSyntax(DTimeSyntax datetime); ");

            Cursor c5 = db2.rawQuery("select * from DateTimeLastSyntax", null);
            c5.moveToFirst();
            int cnt = c5.getCount();
            // Toast.makeText(getApplicationContext(), "Record 1 " , Toast.LENGTH_LONG).show();
            if (cnt > 0) {
                // Toast.makeText(getApplicationContext(), "Record " + cnt, Toast.LENGTH_LONG).show();

                // original
//                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
//                String datetimesyntaxval = datetimesyntax.format(new Date());

                // updated
                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                datetimesyntax.setTimeZone(TimeZone.getTimeZone("UTC"));
                String datetimesyntaxval = datetimesyntax.format(new Date());

                // datetimesyntaxval = "07-24-2019 00:00:00";

                Cursor c23 = db2.rawQuery("select * from DateTimeLastSyntax WHERE DTimeSyntax <= '" + datetimesyntaxval + "'", null);
                c23.moveToFirst();
                cnt = c23.getCount();
                if (cnt > 0) {
                    // Toast.makeText(getApplicationContext(), "TRAP 1 UPDATE TIME" + datetimesyntaxval , Toast.LENGTH_LONG).show();

                    db2.execSQL("Update DateTimeLastSyntax set DTimeSyntax = '" + datetimesyntaxval + "' ;");
                } else {
//                    Toast.makeText(getApplicationContext(), "Invalid System Date Time " + datetimesyntaxval , Toast.LENGTH_LONG).show();

                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.mars_logo)
                            .setTitle("Invalid Date/Time")
                            .setMessage("Invalid System Date Time " + datetimesyntaxval)
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                }

            } else {
                // original
//                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
//                String datetimesyntaxval = datetimesyntax.format(new Date());

                // updated
                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                datetimesyntax.setTimeZone(TimeZone.getTimeZone("UTC"));
                String datetimesyntaxval = datetimesyntax.format(new Date());

                // Toast.makeText(getApplicationContext(), "TRAP 2 NEWTIME POSTED" + datetimesyntaxval, Toast.LENGTH_LONG).show();

                db2.execSQL("Insert into DateTimeLastSyntax values('" + datetimesyntaxval + "');");
                // Toast.makeText(getApplicationContext(), "TRAP 2 NEWTIME POSTED" + datetimesyntaxval, Toast.LENGTH_LONG).show();

                // db2.execSQL("Insert into receivernumber values('+639068828881');");
            }

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 = c2.getCount();

            // Toast.makeText(getApplicationContext(), "trap error", Toast.LENGTH_LONG).show();

            if (cnt2 > 0) {
                // transinv = "Van-Selling";
                transinv = "Booking";
                SalesmanIDval = c2.getString(0); // salesmanid
                transinv = c2.getString(1); // selling type
                MapperCode = c2.getString(2); // mapper code
                DeptCode = c2.getString(3); // department code
                WeekSchedval = c2.getString(4); // week schedule
                Databasename = c2.getString(6); // database name
                lockloc = c2.getString(7); // lock map
                schedprocess = c2.getString(8); // lock sched day sequence
                schedday = c2.getString(9); // sched day lock

//                btloaddata.setEnabled(false);

                loopgetlocation.run();
                String loadTraps = "";

                // Toast.makeText(getApplicationContext(), "trap error" + transinv, Toast.LENGTH_LONG).show();

                loadTraps = c2.getString(5);
                if ("N".equals(loadTraps)) {
                    Toast.makeText(getApplicationContext(), "Installing Please Wait", Toast.LENGTH_LONG).show();
                    // listoftable
                    db.execSQL("CREATE TABLE IF NOT EXISTS CUSTOMERS(CID VARCHAR,CNAME VARCHAR,CTYPE VARCHAR,ATYPE VARCHAR,TERMS VARCHAR,CPERSON VARCHAR" +
                            ",CBDAY DATE,CTELLNUM VARCHAR,CCELLNUM VARCHAR,OWNER VARCHAR,OBDAY DATE,OTELLNUM VARCHAR,OCELLNUM VARCHAR,STREET VARCHAR" +
                            ",BARANGAY VARCHAR,MUNICIPALITY VARCHAR,PROVINCE VARCHAR,SUBPOCKET VARCHAR,CALTEX VARCHAR(1),MOBIL VARCHAR(1),CASTROL VARCHAR(1),PETRON VARCHAR(1)" +
                            ",OTHER VARCHAR,CAF VARCHAR(1),DTI VARCHAR(1),BPERMIT VARCHAR(1),SIGNAGE VARCHAR(1),POSTER VARCHAR(1),STREAMER VARCHAR(1),STICKER VARCHAR(1),DISRACK VARCHAR(1),PRODISPLAY VARCHAR(1),Latitude varchar,Longitude varchar,status varchar(1),creditline varchar,balCredit varchar,OCheck varchar,CreditStatus varchar); ");
                    // OTHERS VARCHAR - ORIGINAL

                    db.execSQL("CREATE TABLE IF NOT EXISTS ScheduleCustomer(customerid [varchar],[CustomerName] [varchar], [OrderVisit] [Interger],status [varchar],salesmanid varchar,weeksched varchar,daysched varchar); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS OtherCUSTOMERS(CID VARCHAR,CNAME VARCHAR,CTYPE VARCHAR,CPERSON VARCHAR" +
                            ",CTELLNUM VARCHAR,CCELLNUM VARCHAR,STREET VARCHAR" +
                            ",BARANGAY VARCHAR,MUNICIPALITY VARCHAR,PROVINCE VARCHAR,Latitude varchar,Longitude varchar,Grouptype varchar,RouteCOde varchar,Source varchar); ");

                    // db.execSQL("CREATE TABLE IF NOT EXISTS BankMap(BankInitial [varchar],[BankName] [varchar], [Street] [varchar],Barangay [varchar],Municipality varchar,Province varchar,Latitude varchar,Longitude varchar  ); ");

                    // BankMapSave();
                    db.execSQL("CREATE TABLE IF NOT EXISTS CustomerUnlock(CustomerID [varchar]); ");

                    // Department
                    db.execSQL("CREATE TABLE IF NOT EXISTS Department([Dept] [varchar]); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS PaymentDetails([Refid] [varchar],PaymentType varchar,BankInitial varchar,checkNumber varchar,accountNumber varchar,checkdate varchar,amount varchar); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS PaymentDetailsTemp(PaymentType varchar,BankInitial varchar,checkNumber varchar,accountNumber varchar,checkdate varchar,amount varchar); ");

                    // Toast.makeText(getApplicationContext(), "" + DeptCode, Toast.LENGTH_LONG).show();
                    // load barangay
                    db.execSQL("CREATE TABLE IF NOT EXISTS Barangay(" +
                            "[CODE] [varchar](20)," +
                            "[BARANGAY] [varchar](100)," +
                            "[MUNICIPALITY_CODE] [varchar](100)," +
                            "[MUNICIPALITY] [varchar](100)," +
                            "[PROVINCE_CODE] [varchar](100)," +
                            "[PROVINCE] [varchar](100)); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS noOrdercustomerlist(Num Interger,customerid varchar,reasonselect varchar,reason varchar,syntaxdatetime varchar); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS receivernumber(NUM varchar); ");
                    insertreceivernum();

                    // db.execSQL("CREATE TABLE IF NOT EXISTS products(productid varchar,description varchar,unitprice float,invqty integer,begInv integer,UM VARCHAR,OUTLET VARCHAR(2),SALESMANID VARCHAR); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS products(productid varchar,description varchar,unitprice float,invqty integer,begInv integer,UM VARCHAR,OUTLET VARCHAR(2),SALESMANID VARCHAR,CSFactor VARCHAR,CSUnit VARCHAR,ProductType Varchar,barcode varchar,CSbarcode varchar); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS products2(productid varchar,description varchar,unitprice float,invqty integer,begInv integer,UM VARCHAR,OUTLET VARCHAR(2),SALESMANID VARCHAR,CSFactor VARCHAR,CSUnit VARCHAR,ProductType Varchar,barcode varchar,CSbarcode varchar); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS invoices(prefix varchar, num varchar, totalamount float,status varchar(1)); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS Invoicedtl(Refid VARCHAR,customerid VARCHAR,productid varchar,Qty integer,Unitprice float,TotalAmount Float,InvoiceDate varchar,terms varchar,daysterm varchar,syntaxdatetime varchar,OSNUM VARCHAR,OUTLET VARCHAR,transinv varchar(1),deldate varchar(20)); ");
                    //													1				2				3					4			5				6					7				8				9				10						11				12			13					14
                    db.execSQL("CREATE TABLE IF NOT EXISTS LoadingPlan(Refid VARCHAR,productid varchar,Qty integer,Unitprice float,TotalAmount Float,InvoiceDate varchar,syntaxdatetime varchar,OUTLET VARCHAR); ");

                    db.execSQL("CREATE TABLE IF NOT EXISTS Returndtl(Refid VARCHAR,Salesmanid VARCHAR,productid varchar,Qty integer,expirydate varchar,InvoiceDate varchar,syntaxdatetime varchar,OUTLET VARCHAR,returntype varchar(1)); ");

                    SaveBarangay();

                    // InsertValidNumber();
                    // Toast.makeText(getApplicationContext(), "Record " + Databasename, Toast.LENGTH_LONG).show();

                    if ("AG".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, AgriChemData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("BL".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, BeloData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("CC".equals(DeptCode)) {
                        if ("MARS2".equals(Databasename)) {
                            Intent intent = new Intent(MainActivity.this, Century2Data.class);
                            intent.putExtra("transinv", transinv);
                            startActivity(intent);

                        } else if ("MARS1".equals(Databasename)) {
                            Intent intent = new Intent(MainActivity.this, Century1Data.class);
                            intent.putExtra("transinv", transinv);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Not Found Database " + Databasename, Toast.LENGTH_LONG).show();
                        }
                    } else if ("LY".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, MultiLinesData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("L".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, LubricantsData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("MI".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, MontoscoData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("PL".equals(DeptCode)) {
                        if ("MARS2".equals(Databasename)) {
                            Intent intent = new Intent(MainActivity.this, Century2Data.class);
                            intent.putExtra("transinv", transinv);
                            intent.putExtra("DeptCode", DeptCode);
                            startActivity(intent);

                        } else if ("MARS1".equals(Databasename)) {
                            Intent intent = new Intent(MainActivity.this, Century1Data.class);
                            intent.putExtra("transinv", transinv);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Not Found Database " + Databasename, Toast.LENGTH_LONG).show();
                        }
                    } else if ("UC".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, UrcData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else if ("RM".equals(DeptCode)) {
                        Intent intent = new Intent(MainActivity.this, RamData.class);
                        intent.putExtra("transinv", transinv);
                        startActivity(intent);
                    } else {
//                    SaveCustomer();
                        SaveScheduleCustomerlist();
                    }
//                   Saveproducts();

                    // Intent intent = new Intent(MainActivity.this, CustomerData.class);
                    // intent.putExtra("mapcode",MapperCode);
                    // startActivity(intent);

                    db.execSQL("Update InstallValue set statvalue = 'Y';");
                    Toast.makeText(getApplicationContext(), "Installation Finished", Toast.LENGTH_LONG).show();
                    MainActivity.this.finish();
                }

                // original
//                String dayss = "";
//                SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
//                String datetimesyntaxval = currentday.format(new Date());
//                dayss = datetimesyntaxval.toUpperCase();

                // updated
                SimpleDateFormat currentday = new SimpleDateFormat("EEE", Locale.getDefault());
                currentday.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dayss = currentday.format(new Date()).toUpperCase();

                SQLiteDatabase db4 = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

                if ("Y".equalsIgnoreCase(schedprocess)) {
                    selectQuery = "Select * from ScheduleCustomer  where salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "' and status = '' ORDER BY OrderVisit limit 1";
                } else if ("Y".equalsIgnoreCase(schedday)) {
                    selectQuery = "Select * from ScheduleCustomer  where salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "' and daysched = '" + dayss + "' and status = '' ORDER BY OrderVisit limit 1";
                } else {
                    selectQuery = "Select * from ScheduleCustomer  where salesmanid = '" + SalesmanIDval + "' and weeksched = '" + WeekSchedval + "' and daysched = '" + dayss + "' and status = '' ORDER BY OrderVisit limit 1";
                }

                Toast.makeText(getApplicationContext(), "Scheduled Week " + WeekSchedval, Toast.LENGTH_LONG).show();
                loopgetlocation.run();

                // displaying the schedule
                Cursor c2x = db4.rawQuery(selectQuery, null);

                if (c2x != null && c2x.moveToFirst()) {
                    // Customer schedule found
                    String customerId = c2x.getString(0).trim();  // make sure no trailing spaces

                    SQLiteDatabase db4x = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                    String selectQueryx = "SELECT CNAME, street, barangay, municipality, province " +
                            "FROM Customers WHERE CID = '" + customerId + "'";

                    Cursor c2xx = db4x.rawQuery(selectQueryx, null);

                    if (c2xx != null && c2xx.moveToFirst()) {
                        tvschedcustomer.setText(c2xx.getString(0));  // CNAME
                        tvschedcustomeradd.setText("(Address) "
                                + c2xx.getString(1) + " "   // street
                                + c2xx.getString(2) + " "   // barangay
                                + c2xx.getString(3) + " "   // municipality
                                + c2xx.getString(4));       // province
                    } else {
                        // No matching customer record
                        tvschedcustomer.setText("");
                        tvschedcustomeradd.setText("");
                    }
                    if (c2xx != null) c2xx.close();
                    db4x.close();

                } else {
                    // No schedule found
                    tvschedcustomer.setText("");
                    tvschedcustomeradd.setText("");
                }
                if (c2x != null) c2x.close();
                // development test... for displaying the schedule

            } else { // call back the startup to open the activity layout
                mHandler.removeCallbacks(loopgetlocation);
                Intent intent = new Intent(MainActivity.this, Startup.class);
                startActivity(intent);
//                btloaddata.setEnabled(true);
                MainActivity.this.finish();
            }

            if ("UC".equals(DeptCode)) {
                // btnoorder.setEnabled(false);
                // btnviewinvoice.setEnabled(false);
                // btnissuedinvoice.setEnabled(false);
                // btnvansellingplan.setEnabled(false);
                // .setEnabled(false);
            }

            if ("CC".equals(DeptCode)) {
            }

            if ("AG".equals(DeptCode)) {
                btnvansellingplan.setEnabled(false);
                botherdetails.setEnabled(false);
            }

            loaddata = true;
            Button BGetLocation = findViewById(R.id.btgetlocation);
            BGetLocation.setOnClickListener(this);

//            btloaddata.setVisibility(View.GONE);

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            Log.e("ONCREATE ERROR", "onCreate: ", e);
        }
    }

    protected void startactivity() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Start Activity", Toast.LENGTH_LONG).show();
        Log.d("startactivity", "running startactivity");
        loopgetlocation.run();
    }

    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

        if (which == DialogInterface.BUTTON_NEUTRAL) {
            editRemarks.setText(getString(R.string.location_error_message)); // string.xml
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void custgetdistance() {
        try {
            String tempstr = "" + spcustomer.getSelectedItem().toString();
            if (tempstr.isEmpty()) {
                custdistance_meter = "OUT";
                customerdistance.setText("No customer selected.");
                return;
            }

//            // Show immediate status while fetching
//            customerdistance.setText("Fetching distance...");

            // Initialize location client
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                customerdistance.setText("Location permission not granted!");
                custdistance_meter = "OUT";
                return;
            }

            // Fetch current location
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location == null) {
                            customerdistance.setText("Unable to fetch current location.");
                            custdistance_meter = "OUT";
                            return;
                        }

                        startLatitude = location.getLatitude();
                        startLongitude = location.getLongitude();

                        // Query customer data
                        SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                        Cursor c2 = db.rawQuery("SELECT street, barangay, municipality, province, cid, status, Latitude, Longitude FROM Customers WHERE cname = ?", new String[]{tempstr});

                        if (c2.moveToFirst()) {
                            String status = c2.getString(5);
                            if ("M".equals(status)) {
                                try {
                                    endLatitude = Double.parseDouble(c2.getString(6));
                                    endLongitude = Double.parseDouble(c2.getString(7));

                                    if (endLatitude == 0.0 || endLongitude == 0.0) {
                                        customerdistance.setText("Invalid customer location.");
                                        custdistance_meter = "OUT";
                                        return;
                                    }

                                    float[] results = new float[1];
                                    Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
                                    float distance = results[0];

                                    custdistance_meter = (distance > 0 && distance < 6) ? "IN" : "OUT";
                                    String dis = distance >= 1000 ? (distance / 1000) + " Kilometers" : distance + " Meters";
                                    customerdistance.setText(dis);

                                } catch (NumberFormatException e) {
                                    customerdistance.setText("Invalid coordinates.");
                                    custdistance_meter = "OUT";
                                }
                            } else {
                                customerdistance.setText("Customer is not mapped.");
                                custdistance_meter = "OUT";
                            }
                        } else {
                            customerdistance.setText("Customer not found.");
                            custdistance_meter = "OUT";
                        }

                        c2.close();
                    })
                    .addOnFailureListener(e -> {
                        customerdistance.setText("Error retrieving location.");
                        custdistance_meter = "OUT";
                    });

        } catch (Exception e) {
//            customerdistance.setText("Unexpected error.");
//            customerdistance.setText("Fetching Customer Distance...");
            custdistance_meter = "OUT";
        }
    }

    // function for google maps - opens google maps with a route (walking mode) - shows distance & path

    /**
     * this function gets the current location and opens Google Maps for navigation.
     */
    private void openGoogleMaps() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        // Use getCurrentLocation for more accurate data
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double currentLat = location.getLatitude();
                        double currentLng = location.getLongitude();

                        Log.d("DEBUG", "Current Location: Lat=" + currentLat + ", Lng=" + currentLng);
                        Log.d("DEBUG", "Destination Location: Lat=" + endLatitude + ", Lng=" + endLongitude);

                        if (endLatitude == 0.0 || endLongitude == 0.0) {
//                            Toast.makeText(this, "No valid customer location found!", Toast.LENGTH_SHORT).show();

                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(R.drawable.mars_logo)
                                    .setTitle("Location Missing")
                                    .setMessage("No valid customer location found!")
                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                    .show();
                            return;
                        }

                        float[] results = new float[1];
                        Location.distanceBetween(currentLat, currentLng, endLatitude, endLongitude, results);
                        float distance = results[0];

//                        String distanceText = distance >= 1000 ? (distance / 1000) + " KM" : distance + " M";
                        @SuppressLint("DefaultLocale") String distanceText = distance >= 1000
                                ? String.format("%.2f KM", distance / 1000)
                                : String.format("%.0f M", distance);

                        Toast.makeText(this, "Distance to Customer " + distanceText, Toast.LENGTH_LONG).show();

//                        new AlertDialog.Builder(MainActivity.this)
//                                .setIcon(R.drawable.mars_logo)
//                                .setTitle("Distance to Customer")
//                                .setMessage("Distance " + distanceText)
//                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                                .show();

                        // Open Google Maps Immediately
                        String uri = "https://www.google.com/maps/dir/?api=1&origin=" + currentLat + "," + currentLng
                                + "&destination=" + endLatitude + "," + endLongitude + "&travelmode=walking";

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } else {
//                        Toast.makeText(this, "Failed to get current location!", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.mars_logo)
                                .setTitle("Location Failed")
                                .setMessage("Failed to get current location!")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                })
                .addOnFailureListener(e -> Log.e("ERROR", "Failed to get current location", e));
    }

    private final Runnable loopgetlocation = new Runnable() {
        @Override
        public void run() {

            LoopGetL = true;
            try {
                gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                Log.e("LoopGetL", "GPS ENABLED: " + e.getMessage(), e);
            }

            try {
                network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                Log.e("LoopGetL", "NETWORK ENABLED: " + e.getMessage(), e);
            }

            // don't start listeners if no provider is enabled
            if (!gps_enabled && !network_enabled) {
                Log.d("GPS PROVIDER", "run() returned: " + false);
                Log.d("NETWORK PROVIDER", "run() returned: " + network_enabled);
            }

            if (gps_enabled) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // request permissions if not granted
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                    return;
                }

                // remove previous updates to avoid conflicts
                locManager.removeUpdates(locListener);
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, locListener);

                // update distance faster
                custgetdistance();
            }
            mHandler.postDelayed(this, 1000); // run every 1 second for faster updates
        }
    };

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                locManager.removeUpdates(locListener);

                // locManager.removeUpdates(locListener);
                longitude = "" + location.getLongitude();
                latitude = "" + location.getLatitude();
//                String altitiude = "" + location.getAltitude();
                String accuracy = "" + location.getAccuracy();
//                String time = "" + location.getTime();

                // Toast.makeText(getApplicationContext(), "" + longitude + accuracy, Toast.LENGTH_LONG).show();
                int valqtylenth;

                double LLatValAdd;
                double LLatValLess;

                // float lessLatlong = 0.0001;
                double LLongValAdd;
                double LLongValLess;

                String sb = "";

                String latval = latitude;
                startLatitude = Double.parseDouble(latval);

                valqtylenth = latval.length();
                for (int i = 0; i < 7; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewLat = sb;

                LLatValAdd = Double.parseDouble(NewLat);
                // LLatValAdd = LLatValAdd + 0.0001;

                LLatValLess = Double.parseDouble(NewLat);
                // LLatValLess = LLatValLess - 0.0001;

                sb = "";

                latval = latitude;
                valqtylenth = latval.length();
                for (int i = 0; i < 5; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewLat = sb;

                NewLatAdd = "" + LLatValAdd;
                NewLatLess = "" + LLatValLess;

                // Toast.makeText(getApplicationContext(), "sample New Lat "+ NewLat +", latadd " +  NewLatAdd + ", less add" + NewLatLess , Toast.LENGTH_LONG).show();

                sb = "";

                latval = NewLatAdd;
                valqtylenth = latval.length();
                for (int i = 0; i < 7; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewLatAdd = sb;

                sb = "";

                latval = NewLatLess;
                valqtylenth = latval.length();
                for (int i = 0; i < 7; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewLatLess = sb;

                if (LoopGetL) {
                    // Toast.makeText(getApplicationContext(), "gps loop New Lat "+ NewLat +", latadd " +  NewLatAdd + ", less add" + NewLatLess , Toast.LENGTH_LONG).show();
                }

                LoopGetL = false;

                sb = "";

                String longval = longitude;
                startLongitude = Double.parseDouble(longval);

                valqtylenth = longval.length();
                for (int i = 0; i < 10; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = longval.charAt(i);
                        sb = sb + descr;
                    }
                    // sb = sb + " ";
                } // end for loop description

                Newlong = sb;

                LLongValAdd = Double.parseDouble(Newlong);
                // LLongValAdd = LLongValAdd + 0.0001;

                LLongValLess = Double.parseDouble(Newlong);
                // LLongValLess = LLongValLess - 0.0001;

                // adjust logngitude
                sb = "";

                longval = longitude;
                valqtylenth = longval.length();
                for (int i = 0; i < 7; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = longval.charAt(i);
                        sb = sb + descr;
                    }
                    // sb = sb + " ";
                } // end for loop description

                Newlong = sb;

                NewlongAdd = "" + LLongValAdd;
                NewlongLess = "" + LLongValLess;

                sb = "";

                latval = NewlongAdd;
                valqtylenth = latval.length();
                for (int i = 0; i < 8; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewlongAdd = sb;

                sb = "";

                latval = NewlongLess;
                valqtylenth = latval.length();
                for (int i = 0; i < 8; i++) {
                    if (valqtylenth - 1 >= i) {
                        char descr = latval.charAt(i);
                        sb = sb + descr;
                    }
                } // end for loop description

                NewlongLess = sb;

                DecimalFormat formatter = new DecimalFormat("###.0000");

                NewlongLess = formatter.format(LLongValLess);
                NewlongAdd = formatter.format(LLongValAdd);

                NewLatLess = formatter.format(LLatValLess);
                NewLatAdd = formatter.format(LLatValAdd);

                // Toast.makeText(getApplicationContext(), "sample New lONG "+ Newlong +", LONGadd " +  NewlongAdd + ", less LONG" + NewlongLess , Toast.LENGTH_LONG).show();
                // Toast.makeText(getApplicationContext(), "sample New Lat "+ NewLat +", latadd " +  NewLatAdd + ", less add" + NewLatLess , Toast.LENGTH_LONG).show();

                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                int cnt = c1.getCount();
                if (cnt > 0) {
                    theMobNo = c1.getString(0);
                }

                if ("MARS2".equals(Databasename)) {
                    TheMessages = "C2LOC!";
                } else {
                    TheMessages = "CLOC!";
                }

                String TempLoc = "";

                // original
//                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
//                String datetimesyntaxval = datetimesyntax.format(new Date());

                // updated
                SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                datetimesyntax.setTimeZone(TimeZone.getTimeZone("UTC"));
                String datetimesyntaxval = datetimesyntax.format(new Date());

                TheMessages = TheMessages + latitude + "!" + longitude + "!" + datetimesyntaxval;

                if (PressLocation) {
//                    Toast.makeText(getApplicationContext(), NewLat + ", " + NewLatAdd + "," + NewLatLess + ", " +  latitude  + ", " + Newlong + ", " + NewlongAdd + ", " + NewlongLess + ", " + longitude + ", Accuracy " + accuracy, Toast.LENGTH_LONG).show();

                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.mars_logo)
                            .setTitle("Location Details")
                            .setMessage(
                                    "LATITUDE DETAILS " + "\n" +
                                            "New Latitude: " + NewLat + "\n" +
                                            "New Latitude Add: " + NewLatAdd + "\n" +
                                            "New Latitude Less: " + NewLatLess + "\n\n" +

                                            "LONGITUDE DETAILS " + "\n" +
                                            "New Longitude: " + Newlong + "\n" +
                                            "New Longitude Add: " + NewlongAdd + "\n" +
                                            "New Longitude Less: " + NewlongLess + "\n\n" +

                                            "YOUR CURRENT LOCATION " + "\n" +
                                            "Latitude: " + latitude + "\n" +
                                            "Longitude: " + longitude + "\n\n" +
                                            "Accuracy: " + accuracy
                            )
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();

                    sendSMS(theMobNo, TheMessages);

                    selectQuery = " SELECT cname FROM customers " +
                            " where (latitude like '%" + NewLat + "%' and longitude like '%" + Newlong + "%') or " +
                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + Newlong + "%') or " +
                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + Newlong + "%') or " +
                            " (latitude like '%" + NewLat + "%' and longitude like '%" + NewlongAdd + "%') or " +
                            " (latitude like '%" + NewLat + "%' and longitude like '%" + NewlongLess + "%') or " +
                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + NewlongAdd + "%') or " +
                            " (latitude like '%" + NewLatAdd + "%' and longitude like '%" + NewlongLess + "%') or " +
                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + NewlongAdd + "%') or " +
                            " (latitude like '%" + NewLatLess + "%' and longitude like '%" + NewlongLess + "%') " +
                            " ORDER BY CNAME";

                    loadSpinnerData();

                    loopgetlocation.run();
                    PressLocation = false;

                } else {
                    // inrangelocation
                }

                // locManager.removeUpdates(locListener);

                // Toast.makeText(getApplicationContext(), "sample  " +  LLatVal, Toast.LENGTH_LONG).show();

                // progress.setVisibility(View.GONE);
            } else {
//                Toast.makeText(getApplicationContext(), "GPS Not Enable or Cant Get Location!", Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.mars_logo)
                        .setTitle("Location Error")
                        .setMessage("GPS not enabled or can't get location!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();

                // check permissions before requesting network updates
                if (network_enabled) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        // request permissions if not granted
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1001); // request code for permissions
                        return;
                    }
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                }
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(@NonNull String provider, int status, @NonNull Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v.getId() == R.id.btgetlocation) {
            PressLocation = true;
            mHandler.removeCallbacks(loopgetlocation);

            try {
                gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                Log.e("GPS ENABLED", "onClick: ", e);
            }

            try {
                network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                Log.e("NETWORK ENABLED", "onClick: ", e);
            }

            // Don't start listeners if no provider is enabled
            if (!gps_enabled && !network_enabled) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attention!");
                builder.setMessage("Sorry, location is not determined. Please enable location providers!");
                builder.setPositiveButton("OK", this);
                builder.setNeutralButton("Cancel", this);
                builder.create().show();
                return;
            }

            // Check permissions before requesting location updates
            if (gps_enabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // Request permissions if not granted
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1001); // Request code for permissions
                    return;
                }
                // Request location updates
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            }

            // Check permissions before requesting network updates
//        if (network_enabled) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                // Request permissions if not granted
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        1001); // Request code for permissions
//                return;
//            }
//            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
//            }
        }
    }

    private void SaveScheduleCustomerlist() {

        //
        db.execSQL("Insert into ScheduleCustomer values('ADCCP371','JADELINE DEVELOPMENT CORPORATION','16','','VERANO','W4','FRI');");
        //
    }

    @SuppressLint("SetTextI18n")
    private void loadSpinnerData() {

        // spinner Drop down elements
        lables = new ArrayList<>();

        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) == null) {
                    lables.add(cursor.getString(1));
                } else {
                    lables.add(cursor.getString(0));
                }
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spcustomer.setAdapter(dataAdapter);
        custidselect.setText("");

        try {
            String tempstr = "" + spcustomer.getSelectedItem().toString();
            if ("".equals(tempstr)) {
                // handle the case where tempstr is an empty string
                Log.d("StringCheck", "tempstr is empty");
            } else {
                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                Cursor c2 = db.rawQuery("select street,barangay,municipality,province,CID from Customers where cname like '" + tempstr + "' order by cname", null);
                c2.moveToFirst();
                int cnt2 = c2.getCount();
                if (cnt2 > 0) {
                    // Toast.makeText(getApplicationContext(), "" + tempstr, Toast.LENGTH_LONG).show();

                    // spprovince.setSelection(Integer.parseInt(c2.getString(3)));
                    String prov;
                    prov = "" + c2.getString(3);

                    switch (prov) {
                        case "0":
                            prov = "Davao Del Sur";
                            break;
                        case "1":
                            prov = "Davao Del Norte";
                            break;
                        case "2":
                            prov = "Davao Oriental";
                            break;
                        case "3":
                            prov = "Compostela Valley";
                            break;
                        case "4":
                            prov = "North Cotabato";
                            break;
                        case "5":
                            prov = "South Cotabato";
                            break;
                        case "6":
                            prov = "Sarangani";
                            break;
                        case "7":
                            prov = "Sultan Kudarat";
                            break;
                        case "8":
                            prov = "Maguindanao";
                            break;
                        case "9":
                            prov = "Agusan Del Sur";
                            break;
                        case "10":
                            prov = "Surigao Del Sur";
                            break;
                        default:
                            prov = "DAVAO DEL SUR";
                            break;
                    }

                    // Toast.makeText(getApplicationContext(), "" + prov, Toast.LENGTH_LONG).show();
                    String addresscust = "" + prov;
                    tvaddress = findViewById(R.id.tvaddress);

                    custidselect.setText("" + c2.getString(4));

                    // + c2.getString(0) + ", " + c2.getString(1) + ", " + c2.getString(2) + ", "
                    tvaddress.setText("(" + c2.getString(4) + ") " + c2.getString(0) + ", " + c2.getString(1) + ", " + c2.getString(2) + ", " + addresscust);
                    Log.d("address", "adrs" + tvaddress);
                }
            }
        } catch (Exception e) {
            // log the exception message and stack trace
            Log.e("ExceptionTag", "An error occurred: " + e.getMessage());
            Log.e("ExceptionTag", "Stack Trace:", e);
        }
    }

    // barangay List
    private void SaveBarangay() {
        Cursor c2 = db.rawQuery("select * from Barangay", null);
        c2.moveToFirst();
        int cnt2 = c2.getCount();
        if (cnt2 > 0) {
            // Toast.makeText(getApplicationContext(), "Record " + cnt, Toast.LENGTH_LONG).show();
            Log.d("SaveBarangay", "Record " + cnt2);
        } else {

            db.execSQL("Insert into Barangay values('112301004','Binancian','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301006','Buan','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301007','Buclad','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301008','Cabaywa','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301009','Camansa','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301010','Camoning','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301011','Canatan','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301012','Concepcion','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301013','Doña Andrea','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301026','Magatos','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301028','Napungas','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301029','New Bantayan','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301030','New Santiago','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301031','Pamacaun','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301032','Cambanogoy (Pob.)','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301034','Sagayen','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301036','San Vicente','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301037','Santa Filomena','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301039','Sonlon','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112301040','New Loon','112301000','ASUNCION (SAUG)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303001','Alejal','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303002','Anibongan','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303003','Asuncion (Cuatro-Cuatro)','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303005','Cebulano','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303006','Guadalupe','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303007','Ising (Pob.)','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303008','La Paz','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303010','Mabaus','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303011','Mabuhay','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303012','Magsaysay','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303014','Mangalcal','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303015','Minda','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303016','New Camiling','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303018','San Isidro','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303019','Santo Niño','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303020','Tibulao','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303021','Tubod','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303022','Tuganay','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303023','Salvacion','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112303024','Taba','112303000','CARMEN','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305003','Semong (Sampao)','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305005','Florida','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305006','Gabuyan','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305007','Gupitan','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305008','Capungagan','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305009','Katipunan','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305012','Luna','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305013','Mabantao','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305015','Mamacao','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305018','Pag-asa','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305021','Maniki (Poblacion)','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305022','Sampao (Bienvenida)','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305026','Sua-on','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112305028','Tiburcia','112305000','KAPALONG','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314001','Cabidianan','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314002','Carcor','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314003','Del Monte','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314004','Del Pilar','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314005','El Salvador','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314006','Limba-an','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314007','Macgum','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314008','Mambing','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314009','Mesaoy','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314010','New Bohol','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314011','New Cortez','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314012','New Sambog','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314013','Patrocenio','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314014','Poblacion','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314015','San Roque','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314016','Santa Cruz','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314017','Santa Fe','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314018','Santo Niño','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314019','Suawon','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112314020','San Jose','112314000','NEW CORELLA','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315001','A. O. Floirendo','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315002','Datu Abdul Dadia','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315003','Buenavista','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315004','Cacao','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315005','Cagangohan','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315006','Consolacion','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315007','Dapco','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315009','Gredu (Pob.)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315010','J.P. Laurel','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315011','Kasilak','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315012','Katipunan','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315013','Katualan','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315014','Kauswagan','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315015','Kiotoy','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315016','Little Panay','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315017','Lower Panaga (Roxas)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315018','Mabunao','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315019','Maduao','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315020','Malativas','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315021','Manay','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315022','Nanyo','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315023','New Malaga (Dalisay)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315024','New Malitbog','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315025','New Pandan (Pob.)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315026','New Visayas','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315027','Quezon','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315028','Salvacion','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315029','San Francisco (Pob.)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315030','San Nicolas','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315031','San Roque','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315032','San Vicente','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315033','Santa Cruz','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315034','Santo Niño (Pob.)','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315035','Sindaton','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315036','Southern Davao','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315037','Tagpore','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315038','Tibungol','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315039','Upper Licanan','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315040','Waterfall','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112315041','San Pedro','112315000','CITY OF PANABO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317001','Adecor','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317002','Anonang','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317003','Aumbay','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317004','Aundanao','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317005','Balet','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317006','Bandera','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317007','Caliclic (Dangca-an)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317008','Camudmud','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317009','Catagman','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317010','Cawag','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317011','Cogon','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317012','Cogon (Talicod)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317013','Dadatan','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317014','Del Monte','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317015','Guilon','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317016','Kanaan','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317017','Kinawitnon','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317018','Libertad','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317019','Libuak','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317020','Licup','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317021','Limao','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317022','Linosutan','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317023','Mambago-A','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317024','Mambago-B','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317025','Miranda (Pob.)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317026','Moncado (Pob.)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317027','Pangubatan (Talicod I)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317028','Peñaplata (Pob.)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317029','Poblacion (Kaputian)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317030','San Agustin','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317031','San Antonio','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317032','San Isidro (Babak)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317033','San Isidro (Kaputian)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317034','San Jose (San Lapuz)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317035','San Miguel (Magamono)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317036','San Remigio','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317037','Santa Cruz (Talicod II)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317038','Santo Niño','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317039','Sion (Zion)','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317040','Tagbaobo','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317041','Tagbay','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317042','Tagbitan-ag','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317043','Tagdaliao','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317044','Tagpopongan','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317045','Tambo','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112317046','Toril','112317000','ISLAND GARDEN CITY OF SAMAL','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318001','Balagunan','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318002','Bobongon','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318003','Esperanza','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318004','Kimamon','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318005','Kinamayan','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318006','La Libertad','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318007','Lungaog','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318008','Magwawa','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318009','New Katipunan','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318010','Pantaron','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318011','Tibal-og (Pob.)','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318013','San Jose','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318014','San Miguel','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318015','Talomo','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318016','Casig-Ang','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318017','New Visayas','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318018','Salvacion','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318019','San Vicente','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112318020','Tulalian','112318000','SANTO TOMAS','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319001','Apokon','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319003','Bincungan','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319004','Busaon','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319005','Canocotan','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319006','Cuambogan','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319007','La Filipina','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319008','Liboganon','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319009','Madaum','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319010','Magdum','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319011','Mankilam','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319012','New Balamban','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319013','Nueva Fuerza','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319014','Pagsabangan','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319015','Pandapan','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319016','Magugpo Poblacion','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319018','San Agustin','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319019','San Isidro','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319020','San Miguel (Camp 4)','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319022','Visayan Village','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319023','Magugpo East','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319024','Magugpo North','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319025','Magugpo South','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112319026','Magugpo West','112319000','CITY OF TAGUM (Capital)','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112322001','Dagohoy','112322000','TALAINGOD','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112322002','Palma Gil','112322000','TALAINGOD','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112322003','Santo Niño','112322000','TALAINGOD','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112323001','Cabay-Angan','112323000','BRAULIO E. DUJALI','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112323002','Dujali','112323000','BRAULIO E. DUJALI','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112323003','Magupising','112323000','BRAULIO E. DUJALI','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112323004','New Casay','112323000','BRAULIO E. DUJALI','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112323005','Tanglaw','112323000','BRAULIO E. DUJALI','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324001','Dacudao','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324002','Datu Balong','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324003','Igangon','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324004','Kipalili','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324005','Libuton','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324006','Linao','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324007','Mamangan','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324008','Monte Dujali','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324009','Pinamuno','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324010','Sabangan','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324011','San Miguel','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324012','Santo Niño','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112324013','Sawata','112324000','SAN ISIDRO','112300000','DAVAO DEL NORTE');");
            db.execSQL("Insert into Barangay values('112401001','Alegre','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401002','Alta Vista','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401003','Anonang','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401004','Bitaug','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401005','Bonifacio','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401006','Buenavista','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401007','Darapuay','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401008','Dolo','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401009','Eman','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401010','Kinuskusan','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401011','Libertad','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401012','Linawan','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401013','Mabuhay','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401014','Mabunga','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401015','Managa','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401016','Marber','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401017','New Clarin (Miral)','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401018','Poblacion','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401019','Rizal','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401020','Santo Niño','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401021','Sibayan','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401022','Tinongtongan','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401023','Tubod','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401024','Union','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112401025','Poblacion Dos','112401000','BANSALAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402001','Acacia','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402002','Agdao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402003','Alambre','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402004','Atan-Awe','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402005','Bago Gallera','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402006','Bago Oshiro','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402007','Baguio (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402009','Balengaeng','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402010','Baliok','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402012','Bangkas Heights','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402013','Baracatan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402014','Bato','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402015','Bayabas','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402016','Biao Escuela','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402017','Biao Guianga','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402018','Biao Joaquin','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402019','Binugao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402020','Bucana','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402021','Buhangin (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402022','Bunawan (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402023','Cabantian','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402024','Cadalian','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402026','Calinan (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402027','Callawa','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402028','Camansi','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402029','Carmen','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402030','Catalunan Grande','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402031','Catalunan Pequeño','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402032','Catigan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402033','Cawayan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402034','Colosas','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402035','Communal','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402036','Crossing Bayabas','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402037','Dacudao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402038','Dalag','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402039','Dalagdag','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402040','Daliao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402041','Daliaon Plantation','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402042','Dominga','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402043','Dumoy','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402044','Eden','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402045','Fatima (Benowang)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402047','Gatungan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402048','Gumalang','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402049','Ilang','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402050','Indangan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402051','Kilate','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402052','Lacson','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402053','Lamanan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402054','Lampianao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402055','Langub','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402056','Alejandra Navarro (Lasang)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402057','Lizada','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402058','Los Amigos','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402059','Lubogan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402060','Lumiad','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402061','Ma-a','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402062','Mabuhay','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402063','Magtuod','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402064','Mahayag','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402065','Malabog','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402066','Malagos','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402067','Malamba','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402068','Manambulan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402069','Mandug','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402070','Manuel Guianga','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402071','Mapula','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402072','Marapangi','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402073','Marilog','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402074','Matina Aplaya','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402075','Matina Crossing','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402077','Matina Pangi','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402078','Matina Biao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402079','Mintal','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402080','Mudiang','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402081','Mulig','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402082','New Carmen','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402083','New Valencia','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402086','Pampanga','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402087','Panacan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402088','Panalum','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402089','Pandaitan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402090','Pangyan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402091','Paquibato (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402092','Paradise Embak','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402097','Riverside','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402098','Salapawan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402099','Salaysay','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402100','San Isidro (Licanan)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402101','Sasa','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402102','Sibulan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402104','Sirawan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402105','Sirib','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402106','Suawan (Tuli)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402107','Subasta','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402108','Sumimao','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402110','Tacunan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402112','Tagakpan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402113','Tagluno','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402114','Tagurano','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402115','Talandang','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402116','Talomo (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402117','Talomo River','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402118','Tamayong','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402119','Tambobong','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402120','Tamugan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402121','Tapak','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402122','Tawan-tawan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402123','Tibuloy','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402124','Tibungco','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402125','Tigatto','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402126','Toril (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402127','Tugbok (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402128','Tungakalan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402129','Ula','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402131','Wangan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402133','Wines','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402134','Barangay 1-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402135','Barangay 2-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402136','Barangay 3-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402137','Barangay 4-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402138','Barangay 5-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402139','Barangay 6-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402140','Barangay 7-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402141','Barangay 8-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402142','Barangay 9-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402143','Barangay 10-A (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402144','Barangay 11-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402145','Barangay 12-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402146','Barangay 13-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402147','Barangay 14-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402148','Barangay 15-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402149','Barangay 16-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402150','Barangay 17-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402151','Barangay 18-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402152','Barangay 19-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402153','Barangay 20-B (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402154','Barangay 21-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402155','Barangay 22-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402156','Barangay 23-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402157','Barangay 24-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402158','Barangay 25-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402159','Barangay 26-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402160','Barangay 27-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402161','Barangay 28-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402162','Barangay 29-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402163','Barangay 30-C (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402164','Barangay 31-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402165','Barangay 32-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402166','Barangay 33-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402167','Barangay 34-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402168','Barangay 35-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402169','Barangay 36-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402170','Barangay 37-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402171','Barangay 38-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402172','Barangay 39-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402173','Barangay 40-D (Pob.)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402174','Angalan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402175','Baganihan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402176','Bago Aplaya','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402177','Bantol','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402178','Buda','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402179','Centro (San Juan)','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402180','Datu Salumay','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402181','Gov. Paciano Bangoy','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402182','Gov. Vicente Duterte','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402183','Gumitan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402184','Inayangan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402185','Kap. Tomas Monteverde, Sr.','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402186','Lapu-lapu','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402187','Leon Garcia, Sr.','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402188','Magsaysay','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402189','Megkawayan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402190','Rafael Castillo','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402191','Saloy','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402192','San Antonio','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402193','Santo Niño','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402194','Ubalde','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402195','Waan','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402196','Wilfredo Aquino','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402197','Alfonso Angliongto Sr.','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112402198','Vicente Hizon Sr.','112402000','DAVAO CITY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403001','Aplaya','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403002','Balabag','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403003','San Jose (Balutakay)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403004','Binaton','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403005','Cogon','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403006','Colorado','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403007','Dawis','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403008','Dulangan','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403009','Goma','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403010','Igpit','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403011','Kiagot','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403012','Lungag','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403013','Mahayahay','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403014','Matti','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403019','Kapatagan (Rizal)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403020','Ruparan','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403021','San Agustin','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403022','San Miguel (Odaca)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403023','San Roque','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403024','Sinawilan','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403025','Soong','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403026','Tiguman','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403027','Tres De Mayo','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403028','Zone 1 (Pob.)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403029','Zone 2 (Pob.)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112403030','Zone 3 (Pob.)','112403000','CITY OF DIGOS (Capital)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404002','Balutakay','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404005','Clib','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404006','Guihing Aplaya','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404007','Guihing','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404008','Hagonoy Crossing','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404009','Kibuaya','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404010','La Union','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404011','Lanuro','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404012','Lapulabao','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404013','Leling','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404014','Mahayahay','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404017','Malabang Damsite','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404018','Maliit Digos','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404019','New Quezon','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404020','Paligue','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404021','Poblacion','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404022','Sacub','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404023','San Guillermo','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404024','San Isidro','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404025','Sinayawan','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112404026','Tologan','112404000','HAGONOY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405001','Buguis','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405002','Balangonan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405006','Bukid','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405007','Butuan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405008','Butulan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405009','Caburan Big','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405010','Caburan Small (Pob.)','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405011','Camalian','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405012','Carahayan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405013','Cayaponga','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405014','Culaman','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405015','Kalbay','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405016','Kitayo','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405019','Magulibas','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405020','Malalan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405021','Mangile','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405022','Marabutuan','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405023','Meybio','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405024','Molmol','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405025','Nuing','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405026','Patulang','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405027','Quiapo','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405028','San Isidro','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405033','Sugal','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405034','Tabayon','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112405035','Tanuman','112405000','JOSE ABAD SANTOS (TRINIDAD)','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406001','Abnate','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406002','Bagong Negros','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406003','Bagong Silang','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406004','Bagumbayan','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406005','Balasiao','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406007','Bonifacio','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406008','Bunot','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406009','Cogon-Bacaca','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406010','Dapok','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406011','Ihan','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406012','Kibongbong','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406013','Kimlawis','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406015','Kisulan','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406016','Lati-an','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406017','Manual','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406018','Maraga-a','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406019','Molopolo','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406020','New Sibonga','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406021','Panaglib','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406022','Pasig','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406023','Poblacion','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406024','Pocaleel','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406025','San Isidro','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406026','San Jose','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406027','San Pedro','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406028','Santo Niño','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406029','Tacub','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406030','Tacul','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406031','Waterfall','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112406032','Bulol-Salo','112406000','KIBLAWAN','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407001','Bacungan','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407002','Balnate','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407003','Barayong','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407004','Blocon','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407005','Dalawinon','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407006','Dalumay','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407007','Glamang','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407008','Kanapulo','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407009','Kasuga','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407010','Lower Bala','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407011','Mabini','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407012','Malawanit','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407013','Malongon','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407014','New Ilocos','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407015','Poblacion','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407016','San Isidro','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407017','San Miguel','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407018','Tacul','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407019','Tagaytay','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407020','Upper Bala','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407021','Maibo','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112407022','New Opon','112407000','MAGSAYSAY','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408002','Baybay','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408004','Bolton','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408005','Bulacan','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408006','Caputian','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408007','Ibo','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408009','Kiblagon','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408010','Lapu-Lapu (Lapla)','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408011','Mabini','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408014','New Baclayon','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408015','Pitu','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408016','Poblacion','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408018','Tagansule','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408019','Bagumbayan','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408020','Rizal (Parame)','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112408021','San Isidro','112408000','MALALAG','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409001','Bito','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409002','Bolila','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409003','Buhangin','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409004','Culaman','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409005','Datu Danwata','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409006','Demoloc','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409007','Felis','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409008','Fishing Village (Fishermans Village)','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409010','Kibalatong','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409011','Kidalapong','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409012','Kilalag','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409013','Kinangan','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409016','Lacaron','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409017','Lagumit','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409018','Lais','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409022','Little Baguio','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409023','Macol','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409024','Mana','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409025','Manuel Peralta','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409027','New Argao','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409030','Pangian','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409031','Pinalpalan','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409032','Poblacion','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409033','Sangay','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409036','Talogoy','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409037','Tical','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409038','Ticulon','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409039','Tingolo','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409040','Tubalan','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112409041','Pangaleon','112409000','MALITA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410001','Asbang','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410002','Asinan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410003','Bagumbayan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410004','Bangkal','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410005','Buas','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410006','Buri','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410007','Camanchiles','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410008','Ceboza','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410009','Colonsabak','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410010','Dongan-Pekong','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410012','Kabasagan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410013','Kapok','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410014','Kauswagan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410015','Kibao','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410016','La Suerte','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410017','Langa-an','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410019','Lower Marber','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410021','Cabligan (Managa)','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410022','Manga','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410023','New Katipunan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410024','New Murcia','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410025','New Visayas','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410026','Poblacion','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410027','Saboy','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410028','San Jose','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410029','San Miguel','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410030','San Vicente','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410031','Saub','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410032','Sinaragan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410033','Sinawilan','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410034','Tamlangon','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410035','Towak','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112410036','Tibongbong','112410000','MATANAO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411001','Almendras (Pob.)','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411002','Don Sergio Osmeña, Sr.','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411003','Harada Butai','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411004','Lower Katipunan','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411005','Lower Limonzo','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411006','Lower Malinao','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411007','N C Ordaneza District (Pob.)','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411008','Northern Paligue','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411009','Palili','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411010','Piape','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411011','Punta Piape','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411012','Quirino District (Pob.)','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411013','San Isidro','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411014','Southern Paligue','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411015','Tulogan','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411016','Upper Limonzo','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112411017','Upper Malinao','112411000','PADADA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412001','Astorga','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412002','Bato','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412003','Coronon','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412004','Darong','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412005','Inawayan','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412006','Jose Rizal','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412009','Matutungan','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412010','Melilia','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412011','Zone I (Pob.)','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412013','Saliducon','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412014','Sibulan','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412015','Sinoron','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412016','Tagabuli','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412017','Tibolo','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412018','Tuban','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412019','Zone II (Pob.)','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412020','Zone III (Pob.)','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112412021','Zone IV (Pob.)','112412000','SANTA CRUZ','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413001','Basiawan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413002','Buca','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413003','Cadaatan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413004','Kidadan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413005','Kisulad','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413006','Malalag Tubig','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413007','Mamacao','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413008','Ogpao','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413009','Poblacion','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413010','Pongpong','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413011','San Agustin','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413012','San Antonio','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413013','San Isidro','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413014','San Juan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413015','San Pedro','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413016','San Roque','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413018','Tanglad','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413019','Santo Niño','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413020','Santo Rosario','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413021','Datu Daligasao','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413022','Datu Intan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112413023','Kinilidan','112413000','SANTA MARIA','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414001','Balasinon','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414002','Buguis','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414003','Carre','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414004','Clib','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414005','Harada Butai','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414006','Katipunan','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414007','Kiblagon','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414008','Labon','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414009','Laperas','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414010','Lapla','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414011','Litos','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414012','Luparan','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414013','Mckinley','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414014','New Cebu','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414015','Osmeña','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414016','Palili','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414017','Parame','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414018','Poblacion','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414020','Roxas','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414021','Solongvale','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414022','Tagolilong','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414023','Tala-o','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414024','Talas','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414025','Tanwalang','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112414026','Waterfall','112414000','SULOP','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415001','Batuganding','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415002','Konel','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415003','Lipol','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415004','Mabila (Pob.)','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415005','Patuco (Sarangani Norte)','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415006','Laker (Sarangani Sur)','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415007','Tinina','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415008','Camahual','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415009','Camalig','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415010','Gomtago','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415011','Tagen','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112415012','Tucal','112415000','SARANGANI','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416001','Calian','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416002','Kiobog','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416003','North Lamidan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416004','Lawa (Pob.)','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416005','Nueva Villa','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416006','Talagutong (Pob.)','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416007','Baluntaya','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416008','Dalupan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416009','Kinanga','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416010','Lanao','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416011','Lapuan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416012','Linadasan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416013','Mabuhay','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416014','South Lamidan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112416015','West Lamidan','112416000','DON MARCELINO','112400000','DAVAO DEL SUR');");
            db.execSQL("Insert into Barangay values('112501001','Baculin','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501002','Banao','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501003','Batawan','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501004','Batiano','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501005','Binondo','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501006','Bobonao','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501007','Campawan','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501008','Central (Pob.)','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501009','Dapnan','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501010','Kinablangan','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501011','Lambajon','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501012','Mahanub','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501013','Mikit','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501014','Salingcomot','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501015','San Isidro','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501016','San Victor','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501017','Lucod','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112501018','Saoquegue','112501000','BAGANGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502001','Cabangcalan','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502002','Caganganan','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502003','Calubihan','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502004','Causwagan','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502005','Punta Linao','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502006','Mahayag','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502007','Maputi','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502008','Mogbongcogon','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502009','Panikian','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502010','Pintatagan','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502011','Piso Proper','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502012','Poblacion','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502013','San Vicente','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112502014','Rang-ay','112502000','BANAYBANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503001','Cabasagan','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503002','Caatihan','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503003','Cawayanan','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503004','Poblacion','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503005','San Jose','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503006','Sibajay','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503007','Carmen','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112503008','Simulao','112503000','BOSTON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504001','Alvar','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504002','Caningag','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504004','Don Leon Balante','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504005','Lamiawan','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504006','Manorigao','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504007','Mercedes','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504008','Palma Gil','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504009','Pichon','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504010','Poblacion','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504011','San Antonio','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504012','San Jose','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504013','San Luis','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504014','San Miguel','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504015','San Pedro','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504016','Santa Fe','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504017','Santiago','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112504018','Sobrecarey','112504000','CARAGA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505001','Abijod','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505002','Alegria','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505003','Aliwagwag','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505004','Aragon','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505005','Baybay','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505009','Maglahus','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505010','Mainit','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505011','Malibago','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505012','San Alfonso','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505013','San Antonio','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505014','San Miguel','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505015','San Rafael','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505016','San Vicente','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505017','Santa Filomena','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505018','Taytayan','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112505019','Poblacion','112505000','CATEEL','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506001','Anitap','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506002','Manuel Roxas','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506003','Don Aurelio Chicote','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506004','Lavigan','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506005','Luzon','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506006','Magdug','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506007','Monserrat','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506008','Nangan','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506009','Oregon','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506010','Poblacion','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506011','Pundaguitan','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506012','Sergio Osmeña','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506013','Surop','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506014','Tagabebe','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506015','Tamban','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506016','Tandang Sora','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506017','Tibanban','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506018','Tiblawan','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506019','Upper Tibanban','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112506020','Crispin Dela Cruz','112506000','GOVERNOR GENEROSO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507001','Bagumbayan','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507003','Cabadiangan','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507006','Calapagan','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507008','Cocornon','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507009','Corporacion','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507010','Don Mariano Marcos','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507011','Ilangay','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507013','Langka','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507014','Lantawan','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507015','Limbahan','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507016','Macangao','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507017','Magsaysay','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507019','Mahayahay','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507021','Maragatas','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507022','Marayag','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507024','New Visayas','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507028','Poblacion','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507029','San Isidro','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507030','San Jose','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507032','Tagboa','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112507033','Tagugpo','112507000','LUPON','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508001','Capasnan','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508002','Cayawan','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508003','Central (Pob.)','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508004','Concepcion','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508006','Del Pilar','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508007','Guza','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508008','Holy Cross','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508009','Mabini','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508010','Manreza','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508011','Old Macopa','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508012','Rizal','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508013','San Fermin','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508014','San Ignacio','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508015','San Isidro','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508016','New Taokanga','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508017','Zaragosa','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112508018','Lambog','112508000','MANAY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509001','Badas','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509002','Bobon','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509003','Buso','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509004','Cabuaya','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509005','Central (Pob.)','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509006','Culian','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509007','Dahican','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509008','Danao','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509009','Dawan','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509010','Don Enrique Lopez','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509011','Don Martin Marundan','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509012','Don Salvador Lopez, Sr.','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509013','Langka','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509015','Lawigan','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509016','Libudon','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509017','Luban','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509018','Macambol','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509019','Mamali','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509020','Matiao','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509021','Mayo','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509022','Sainz','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509023','Sanghay','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509024','Tagabakid','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509025','Tagbinonga','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509026','Taguibo','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112509027','Tamisan','112509000','CITY OF MATI  (Capital)','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510001','Baon','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510003','Bitaogan','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510004','Cambaleon','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510005','Dugmanon','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510006','Iba','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510007','La Union','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510008','Lapu-lapu','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510009','Maag','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510010','Manikling','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510011','Maputi','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510012','Batobato (Pob.)','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510013','San Miguel','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510014','San Roque','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510015','Santo Rosario','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510016','Sudlon','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112510017','Talisay','112510000','SAN ISIDRO','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511001','Cabagayan','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511002','Central (Pob.)','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511003','Dadong','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511004','Jovellar','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511005','Limot','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511006','Lucatan','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511007','Maganda','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511008','Ompao','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511010','Tomoaong','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112511011','Tubaon','112511000','TARRAGONA','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582101','Anitap','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582102','Crispin Dela Cruz','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582103','Don Aurelio Chicote','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582104','Lavigan','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582105','Luzon','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582106','Magdug','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582107','Manuel Roxas','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582108','Montserrat','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582109','Nangan','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582110','Oregon','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582111','Poblacion (Sigaboy)','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582112','Pundaguitan','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582113','Sergio Osmeña','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582114','Surop','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582115','Tagabebe','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582116','Tamban','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582117','Tandang Sora','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582118','Tibanban','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582119','Tiblawan','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('112582120','Upper Tibanban','112582100','SIGABOY','112500000','DAVAO ORIENTAL');");
            db.execSQL("Insert into Barangay values('118201001','Bagongon','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201002','Gabi','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201003','Lagab','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201004','Mangayon','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201005','Mapaca','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201006','Maparat','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201007','New Alegria','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201008','Ngan','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201009','Osmeña','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201010','Panansalan','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201011','Poblacion','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201012','San Jose','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201013','San Miguel','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201014','Siocon','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201015','Tamia','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118201016','Aurora','118201000','COMPOSTELA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202001','Aguinaldo','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202002','Banbanon','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202003','Binasbas','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202004','Cebulida','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202005','Il Papa','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202006','Kaligutan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202007','Kapatagan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202008','Kidawa','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202009','Kilagding','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202010','Kiokmay','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202011','Langtud','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202012','Longanapan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202013','Naga','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202014','Laac (Pob.)','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202015','San Antonio','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202016','Amor Cruz','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202017','Ampawid','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202018','Andap','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202019','Anitap','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202020','Bagong Silang','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202021','Belmonte','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202022','Bullucan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202023','Concepcion','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202024','Datu Ampunan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202025','Datu Davao','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202026','Doña Josefa','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202027','El Katipunan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202028','Imelda','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202029','Inacayan','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202030','Mabuhay','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202031','Macopa','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202032','Malinao','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202033','Mangloy','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202034','Melale','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202035','New Bethlehem','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202036','Panamoren','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202037','Sabud','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202038','Santa Emilia','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202039','Santo Niño','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118202040','Sisimon','118202000','LAAK (SAN VICENTE)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203002','Cadunan','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203006','Pindasan','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203007','Cuambog (Pob.)','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203011','Tagnanan (Mampising)','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203012','Anitapan','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203013','Cabuyuan','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203014','Del Pilar','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203015','Libodon','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203016','Golden Valley (Maraut)','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203017','Pangibiran','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118203018','San Antonio','118203000','MABINI (DOÑA ALICIA)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204001','Anibongan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204002','Anislagan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204003','Binuangan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204004','Bucana','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204005','Calabcab','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204006','Concepcion','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204007','Dumlan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204008','Elizalde (Somil)','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204009','Pangi (Gaudencio Antonio)','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204010','Gubatan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204011','Hijo','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204012','Kinuban','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204013','Langgam','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204014','Lapu-lapu','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204015','Libay-libay','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204016','Limbo','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204017','Lumatab','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204018','Magangit','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204019','Malamodao','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204020','Manipongol','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204021','Mapaang','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204022','Masara','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204023','New Asturias','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204024','Panibasan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204025','Panoraon','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204026','Poblacion','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204027','San Juan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204028','San Roque','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204029','Sangab','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204030','Taglawig','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204031','Mainit','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204032','New Barili','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204033','New Leyte','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204034','New Visayas','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204035','Panangan','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204036','Tagbaros','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118204037','Teresa','118204000','MACO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205001','Bagong Silang','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205002','Mapawa','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205003','Maragusan (Pob.)','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205004','New Albay','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205005','Tupaz','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205006','Bahi','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205007','Cambagang','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205008','Coronobe','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205009','Katipunan','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205010','Lahi','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205011','Langgawisan','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205012','Mabugnao','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205013','Magcagong','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205014','Mahayahay','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205015','Mauswagon','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205016','New Katipunan','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205017','New Man-ay','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205018','New Panay','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205019','Paloc','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205020','Pamintaran','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205021','Parasanon','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205022','Talian','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205023','Tandik','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118205024','Tigbao','118205000','MARAGUSAN (SAN MARIANO)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206001','Andili','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206002','Bawani','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206003','Concepcion','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206004','Malinawon','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206005','Nueva Visayas','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206006','Nuevo Iloco','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206007','Poblacion','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206008','Salvacion','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206009','Saosao','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206010','Sawangan','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118206011','Tuboran','118206000','MAWAB','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207001','Awao','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207002','Babag','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207003','Banlag','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207004','Baylo','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207005','Casoon','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207006','Inambatan','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207007','Haguimitan','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207008','Macopa','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207009','Mamunga','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207010','Naboc','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207011','Olaycon','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207012','Pasian (Santa Filomena)','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207013','Poblacion','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207014','Rizal','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207015','Salvacion','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207016','San Isidro','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207017','San Jose','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207019','Tubo-tubo (New Del Monte)','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207020','Upper Ulip','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207021','Union','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118207022','Mount Diwata','118207000','MONKAYO','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208001','Banagbanag','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208002','Banglasan','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208003','Bankerohan Norte','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208004','Bankerohan Sur','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208005','Camansi','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208006','Camantangan','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208007','Concepcion','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208008','Dauman','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208009','Canidkid','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208010','Lebanon','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208011','Linoan','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208012','Mayaon','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208014','New Calape','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208015','New Dalaguete','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208016','New Cebulan (Sambayon)','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208017','New Visayas','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208019','Prosperidad','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208020','San Jose (Pob.)','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208021','San Vicente','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118208022','Tapia','118208000','MONTEVISTA','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209001','Anislagan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209002','Antiquera','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209003','Basak','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209004','Cabacungan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209005','Cabidianan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209006','Katipunan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209007','Libasan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209008','Linda','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209009','Magading','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209010','Magsaysay','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209011','Mainit','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209012','Manat','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209013','Matilo','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209014','Mipangi','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209015','New Dauis','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209016','New Sibonga','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209017','Ogao','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209018','Pangutosan','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209019','Poblacion','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209020','San Isidro','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209021','San Roque','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209022','San Vicente','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209023','Santa Maria','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209024','Santo Niño (Kao)','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209025','Sasa','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209026','Tagnocon','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209027','Bayabas','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118209028','Bukal','118209000','NABUNTURAN (Capital)','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210001','Bantacan','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210002','Batinao','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210003','Camanlangan','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210004','Cogonon','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210005','Fatima','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210006','Katipunan','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210007','Magsaysay','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210008','Magangit','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210009','Pagsabangan','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210010','Panag','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210011','Cabinuangan (Pob.)','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210012','San Roque','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210013','Andap','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210014','Kahayag','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210015','Manurigao','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118210016','Tandawan','118210000','NEW BATAAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211001','Bongabong','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211002','Bongbong','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211003','P. Fuentes','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211004','Kingking (Pob.)','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211005','Magnaga','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211006','Matiao','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211007','Napnapan','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211009','Tagdangua','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211010','Tambongon','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211011','Tibagon','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211012','Las Arenas','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211013','Araibo','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('118211014','Tag-Ugpo','118211000','PANTUKAN','118200000','DAVAO DE ORO');");
            db.execSQL("Insert into Barangay values('124701001','Bao','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701002','Barangiran','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701003','Camansi','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701004','Dado','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701005','Guiling','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701006','Kitacubong (Pob.)','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701007','Macabasa','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701008','Malitubog','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701009','Mapurok','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701010','Pacao','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701011','Paruayan','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701012','Pigcawaran','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701013','Polayagan','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701014','Rangayen','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701015','Lower Dado','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701016','Mirasol','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124701017','Raradangan','124701000','ALAMADA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702001','Aroman','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702003','Bentangan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702005','Cadiis','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702007','General Luna','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702008','Katanayanan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702010','Kib-Ayao','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702011','Kibenes','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702012','Kibugtongan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702013','Kilala','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702014','Kimadzil','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702015','Kitulaan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702016','Langogan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702017','Lanoon','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702019','Liliongan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702020','Ugalingan (Lumayong)','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702021','Macabenban','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702022','Malapag','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702024','Manarapan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702025','Manili','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702027','Nasapian','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702028','Palanggalan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702030','Pebpoloan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702032','Poblacion','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702034','Ranzo','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702035','Tacupan','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702036','Tambad','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702037','Tonganon','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124702039','Tupig','124702000','CARMEN(NC)','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703001','Aringay','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703002','Bangilan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703003','Bannawag','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703004','Buluan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703005','Cuyapon','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703006','Dagupan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703007','Katidtuan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703008','Kayaga','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703009','Kilagasan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703010','Magatos','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703011','Malamote','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703012','Malanduague','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703013','Nanga-an','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703014','Osias','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703015','Paatan Lower','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703016','Paatan Upper','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703017','Pedtad','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703018','Pisan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703019','Poblacion','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703020','Salapungan','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703021','Sanggadong','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703022','Simbuhay','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703023','Simone','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124703024','Tamped','124703000','KABACAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704001','Amas','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704002','Amazion','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704003','Balabag','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704004','Balindog','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704005','Benoligan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704006','Berada','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704007','Gayola','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704008','Ginatilan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704009','Ilomavis','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704010','Indangan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704011','Junction','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704012','Kalaisan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704013','Kalasuyan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704014','Katipunan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704015','Lanao','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704016','Linangcob','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704017','Luvimin','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704018','Macabolig','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704020','Malinan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704021','Manongol','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704022','Marbel (Embac)','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704023','Mateo','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704024','Meochao','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704025','Mua-an','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704026','New Bohol','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704027','Nuangan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704028','Onica','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704029','Paco','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704030','Patadon (Patadon East)','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704031','Perez','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704032','Poblacion','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704033','San Isidro','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704034','San Roque','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704035','Santo Niño','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704036','Sibawan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704037','Sikitan','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704038','Singao','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704039','Sudapin','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704040','Sumbao','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124704041','Magsaysay','124704000','Kidapawan','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705001','Abaga','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705003','Baguer','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705004','Barongis','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705005','Batiocan','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705006','Cabaruyan','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705007','Cabpangi','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705010','Demapaco','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705011','Grebona','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705013','Gumaga','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705014','Kapayawi','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705015','Kiloyao','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705017','Kitubod','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705019','Malengen','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705023','Montay','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705024','Nica-an','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705025','Palao','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705029','Poblacion','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705032','Sinapangan','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705033','Sinawingan','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124705034','Ulamian','124705000','LIBUNGAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706001','Alibayon','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706005','Bagumbayan','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706006','Bangkal','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706007','Bantac','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706008','Basak','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706009','Binay','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706010','Bongolanon','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706014','Datu Celo','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706015','Del Pilar','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706016','Doles','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706021','Gubatan','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706022','Ilian','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706023','Inac','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706024','Kamada','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706026','Kauswagan','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706027','Kisandal','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706029','Magcaalam','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706030','Mahongcog','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706033','Manobo','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706037','Noa','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706038','Owas','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706039','Pangao-an','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706040','Poblacion','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706042','Sallab','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706043','Tagbac','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706044','Temporan','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706048','Amabel','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706049','Balete','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706050','Don Panaca','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706051','Imamaling','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706052','Kinarum','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124706053','Manobisa','124706000','MAGPET','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707001','Batasan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707002','Bato','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707003','Biangan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707004','Buena Vida','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707005','Buhay','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707006','Bulakanon','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707007','Cabilao','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707008','Concepcion','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707009','Dagupan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707010','Garsika','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707011','Guangan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707012','Indangan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707013','Jose Rizal','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707014','Katipunan II','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707015','Kawayanon','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707016','Kisante','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707017','Leboce','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707018','Libertad','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707019','Luayon','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707020','Luna Norte','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707021','Luna Sur','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707022','Malabuan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707023','Malasila','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707024','Malungon','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707025','New Baguio','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707026','New Bulatukan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707027','New Cebu','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707028','Old Bulatukan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707029','Poblacion','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707030','Rodero','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707031','Saguing','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707032','San Vicente','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707033','Santa Felomina','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707034','Santo Niño','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707035','Sinkatulan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707036','Taluntalunan','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707037','Villaflores','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124707038','New Israel','124707000','MAKILALA','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708001','New Alimodian','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708003','Arakan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708004','Bangbang','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708005','Bato','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708006','Central Malamote','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708007','Dalapitan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708008','Estado','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708009','Ilian','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708010','Kabulacan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708012','Kibia','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708013','Kibudoc','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708014','Kidama','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708015','Kilada','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708017','Lampayan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708018','Latagan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708019','Linao','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708020','Lower Malamote','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708025','Manubuan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708026','Manupal','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708027','Marbel','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708028','Minamaing','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708029','Natutungan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708030','New Bugasong','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708031','New Pandan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708033','Patadon West','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708034','Poblacion','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708035','Salvacion','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708036','Santa Maria','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708037','Sarayan','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708038','Taculen','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708039','Taguranao','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708040','Tamped (Tampad)','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708041','New Abra','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124708042','Pinamaton','124708000','MATALAM','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709002','Agriculture','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709003','Anonang','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709004','Arizona','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709005','Bagumba','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709006','Baliki','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709007','Bitoka','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709008','Bual Norte','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709009','Bual Sur','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709010','Central Bulanan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709011','Damatulan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709012','Central Glad','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709013','Ilbocean','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709014','Kadigasan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709015','Kadingilan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709016','Kapinpilan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709017','Central Katingawan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709018','Kimagango','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709019','Kudarangan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709020','Central Labas','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709021','Lagumbingan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709022','Lomopog','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709023','Lower Glad','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709024','Lower Katingawan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709025','Macasendeg','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709026','Malamote','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709027','Malingao','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709028','Milaya','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709029','Mudseng','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709030','Nabalawag','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709031','Nalin','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709032','Nes','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709033','Olandang','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709034','Patindeguen','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709035','Barangay Poblacion 1','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709036','Barangay Poblacion 2','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709037','Barangay Poblacion 3','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709038','Barangay Poblacion 4','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709039','Barangay Poblacion 5','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709040','Barangay Poblacion 6','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709041','Barangay Poblacion 7','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709042','Barangay Poblacion 8','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709043','Palongoguen','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709044','Rangaban','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709045','Sadaan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709046','Salunayan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709047','Sambulawan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709048','San Isidro','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709049','Santa Cruz','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709050','Tugal','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709051','Tumbras','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709052','Bulanan Upper','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709053','Upper Glad I','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709054','Upper Glad II','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709055','Upper Labas','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709056','Villarica','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709057','Kiwanan','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124709058','San Pedro','124709000','MIDSAYAP','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710001','Bagontapay','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710002','Bialong','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710003','Buayan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710004','Calunasan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710005','Dalipe','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710006','Dagong','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710007','Dungo-an','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710008','Gaunan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710009','Inas','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710010','Katipunan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710011','La Fortuna','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710012','La Suerte','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710013','Langkong','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710014','Lepaga','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710015','Liboo','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710016','Lika','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710017','Luz Village','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710018','Magallon','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710019','Malayan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710020','New Antique','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710021','New Barbaza','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710022','New Kalibo','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710024','New Consolacion','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710025','New Esperanza','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710026','New Janiuay','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710027','New Lawa-an','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710028','New Rizal','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710029','Nueva Vida','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710030','Pag-asa','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710031','Poblacion','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710032','Pulang-lupa','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710033','Sangat','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710034','Tawantawan','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710035','Tibao','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710036','Ugpay','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710037','Palma-Perez','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124710038','Poblacion B','124710000','MLANG','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711001','Anick (Upper Balogo)','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711002','Upper Baguer (Baguer)','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711003','Balacayon','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711004','Balogo','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711006','Banucagon','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711007','Bulucaon','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711008','Buluan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711009','Buricain','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711010','Capayuran','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711011','Central Panatan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711012','Datu Binasing','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711013','Datu Mantil','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711014','Kadingilan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711015','Kimarayang','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711016','Libungan Torreta','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711017','Lower Baguer','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711018','Lower Pangangkalan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711019','Malagakit','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711020','Maluao','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711021','North Manuangan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711022','Matilac','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711023','Midpapan I','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711024','Mulok','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711025','New Culasi','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711026','New Igbaras','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711027','New Panay','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711028','Upper Pangangkalan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711029','Patot','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711030','Payong-payong','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711031','Poblacion I','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711032','Poblacion II','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711033','Poblacion III','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711034','Presbitero','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711035','Renibon','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711036','Simsiman','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711037','South Manuangan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711038','Tigbawan','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711039','Tubon','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711040','Midpapan II','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124711041','Cabpangi','124711000','PIGKAWAYAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712001','Bagoaingud (Bagoinged)','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712003','Balabak','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712004','Balatican','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712005','Balong','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712006','Balungis','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712007','Barungis','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712008','Batulawan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712009','Bualan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712010','Buliok','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712011','Bulod','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712012','Bulol','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712013','Calawag','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712015','Dalingaoen (Lalingaon)','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712016','Damalasak','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712019','Fort Pikit','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712020','Ginatilan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712021','Gligli','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712022','Gokoton (Gokotan)','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712023','Inug-ug','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712024','Kabasalan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712025','Kalacacan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712026','Katilacan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712028','Kolambog','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712029','Ladtingan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712030','Lagunde','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712031','Langayen','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712034','Macabual','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712035','Macasendeg','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712037','Manaulanan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712040','Nabundas','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712041','Nalapaan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712044','Nunguan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712046','Paidu Pulangi','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712048','Panicupan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712050','Poblacion','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712051','Punol','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712052','Rajah Muda','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712055','Silik','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712056','Takipan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712057','Talitay','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712060','Tinutulan','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124712062','Pamalian','124712000','PIKIT','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713001','Alegria','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713002','Bato-bato','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713003','Del Carmen','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713005','F. Cajelo (New Maasin)','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713006','Idaoman','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713007','Ilustre','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713008','Kamarahan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713009','Camasi','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713011','Kisupaan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713012','La Esperanza','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713013','Labu-o','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713014','Lamalama','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713016','Lomonay','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713022','New Cebu','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713024','Poblacion','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713026','Sagcungan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713027','Salat','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713028','Sarayan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713029','Tuael','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713030','Greenhill','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713031','Cabangbangan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713032','Datu Indang','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713033','Datu Sandongan','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713034','Kimaruhing','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124713035','Mabuhay','124713000','PRESIDENT ROXAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714001','Bagumbayan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714002','Banayal','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714003','Batang','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714004','Bituan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714005','Bual','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714006','Daig','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714007','Damawato','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714008','Dungos','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714009','Kanibong','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714010','La Esperanza','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714011','Lampagang','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714012','Bunawan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714013','Magbok','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714014','Maybula','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714015','Minapan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714016','New Caridad','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714017','New Culasi','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714018','New Panay','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714019','Paraiso','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714020','Poblacion','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714021','Popoyon','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714022','Sibsib','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714023','Tambac','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714024','Tuburan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714026','F. Cajelo','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714027','Bacong','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714028','Galidan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714029','Genoveva Baynosa','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124714030','Nabundasan','124714000','TULUNAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715001','Camutan','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715002','Canaan','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715003','Dolores','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715004','Kiyaab','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715005','Luhong','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715006','Magsaysay','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715007','Malangag','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715008','Malatad','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715009','Malire','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715010','New Pontevedra','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715011','Poblacion','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715012','B. Cadungon','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124715013','Datu Agod','124715000','ANTIPAS','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716001','Banisilan Poblacion','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716002','Busaon','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716003','Capayangan','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716004','Carugmanan','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716005','Gastay','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716006','Kalawaig','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716007','Kiaring','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716008','Malagap','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716009','Malinao','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716010','Miguel Macasarte','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716011','Pantar','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716012','Paradise','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716013','Pinamulaan','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716014','Poblacion II','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716015','Puting-bato','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716016','Solama','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716017','Thailand','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716018','Tinimbacan','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716019','Tumbao-Camalig','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124716020','Wadya','124716000','BANISILAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717001','Bagolibas','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717002','Cawilihan','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717003','Dualing','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717004','Dunguan','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717005','Katalicanan','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717006','Lawili','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717007','Lower Mingading','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717008','Luanan','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717009','Malapang','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717010','New Leon','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717011','New Panay','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717012','Pagangan','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717013','Palacat','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717014','Pentil','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717015','San Mateo','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717016','Santa Cruz','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717017','Tapodoc','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717018','Tomado','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124717019','Upper Mingading','124717000','ALEOSAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718001','Allab','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718002','Anapolon','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718003','Badiangon','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718004','Binoongan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718005','Dallag','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718006','Datu Ladayon','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718007','Datu Matangkil','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718008','Doroluman','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718009','Gambodes','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718010','Ganatan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718011','Greenfield','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718012','Kabalantian','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718013','Katipunan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718014','Kinawayan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718015','Kulaman Valley','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718016','Lanao Kuran','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718017','Libertad','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718018','Makalangot','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718019','Malibatuan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718020','Maria Caridad','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718021','Meocan','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718022','Naje','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718023','Napalico','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718024','Salasang','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718025','San Miguel','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718026','Santo Niño','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718027','Sumalili','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('124718028','Tumanding','124718000','ARAKAN','124700000','NORTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302002','Benitez (Pob.)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302003','Cabudian','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302004','Cabuling','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302005','Cinco (Barrio 5)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302006','Derilon','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302007','El Nonok','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302008','Improgo Village (Pob.)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302009','Kusan (Barrio 8)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302010','Lam-Apos','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302011','Lamba','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302012','Lambingi','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302013','Lampari','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302014','Liwanay (Barrio 1)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302015','Malaya (Barrio 9)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302016','Punong Grande (Barrio 2)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302017','Rang-ay (Barrio 4)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302018','Reyes (Pob.)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302019','Rizal (Barrio 3)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302020','Rizal Poblacion','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302021','San Jose (Barrio 7)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302022','San Vicente (Barrio 6)','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126302024','Yangco Poblacion','126302000','BANGA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303003','Baluan','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303004','Buayan','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303005','Bula','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303006','Conel','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303007','Dadiangas East (Pob.)','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303009','Katangawan','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303011','Lagao (1st & 3rd)','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303012','Labangal','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303015','Ligaya','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303016','Mabuhay','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303023','San Isidro (Lagao 2nd)','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303024','San Jose','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303026','Sinawal','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303027','Tambler','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303028','Tinagacan','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303029','Apopong','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303030','Siguel','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303031','Upper Labay','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303032','Batomelong','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303033','Calumpang','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303034','City Heights','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303035','Dadiangas North','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303036','Dadiangas South','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303037','Dadiangas West','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303038','Fatima','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126303039','Olympog','126303000','GENERAL SANTOS CITY (DADIANGAS)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306001','Assumption (Bulol)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306002','Avanceña (Bo. 3)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306003','Cacub','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306004','Caloocan','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306005','Carpenter Hill','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306006','Concepcion (Bo. 6)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306007','Esperanza','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306008','General Paulino Santos (Bo. 1)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306009','Mabini','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306010','Magsaysay','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306011','Mambucal','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306012','Morales','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306013','Namnama','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306014','New Pangasinan (Bo. 4)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306015','Paraiso','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306016','Zone I (Pob.)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306017','Zone II (Pob.)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306018','Zone III (Pob.)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306019','Zone IV (Pob.)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306020','Rotonda','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306021','San Isidro','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306022','San Jose (Bo. 5)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306023','San Roque','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306024','Santa Cruz','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306025','Santo Niño (Bo. 2)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306026','Sarabia (Bo. 8)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126306027','Zulueta (Bo. 7)','126306000','CITY OF KORONADAL (Capital)','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311002','Dumaguil','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311003','Esperanza','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311006','Kibid','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311007','Lapuz','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311008','Liberty','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311009','Lopez Jaena','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311011','Matapol','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311013','Poblacion','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311014','Puti','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311016','San Jose','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311017','San Miguel','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311020','Simsiman','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311021','Tinago','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126311022','Benigno Aquino, Jr.','126311000','NORALA','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312001','Bentung','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312002','Crossing Palkan','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312003','Glamang','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312004','Kinilis','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312005','Klinan 6','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312006','Koronadal Proper','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312007','Lam-Caliaf','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312008','Landan','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312009','Lumakil','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312011','Maligo','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312013','Palkan','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312014','Poblacion','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312015','Polo','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312016','Magsaysay','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312017','Rubber','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312018','Silway 7','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312019','Silway 8','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312020','Sulit','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312021','Sumbakil','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312022','Upper Klinan','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312023','Lapu','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312024','Cannery Site','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126312025','Pagalungan','126312000','POLOMOLOK','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313002','Buenavista','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313003','Centrala','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313004','Colongulo','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313005','Dajay','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313006','Duengas','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313008','Canahay (Godwino)','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313014','Lambontong','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313017','Lamian','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313018','Lamsugod','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313019','Libertad (Pob.)','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313020','Little Baguio','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313023','Moloy','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313024','Naci (Doce)','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313028','Talahik','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313030','Tubiala','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313031','Upper Sepaka','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126313032','Veterans','126313000','SURALLAH','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314001','Albagan','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314002','Kipalbig','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314003','Lambayong','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314004','Liberty','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314005','Maltana','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314006','Poblacion','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314007','Tablu','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314008','Buto','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314009','Lampitak','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314010','Palo','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314011','Pula-bato','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314012','Danlag','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314013','San Isidro','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126314014','Santa Cruz','126314000','TAMPAKAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315001','Bukay Pait','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315002','Cabuling','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315003','Dumadalig','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315004','Libas','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315005','Magon','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315006','Maibo','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315007','Mangilala','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315008','New Iloilo','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315009','New Lambunao','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315010','Poblacion','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315011','San Felipe','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315012','New Cuyapo','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126315013','Tinongcop','126315000','TANTANGAN','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316001','Basag','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316002','Edwards (Pob.)','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316004','Kematu','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316005','Laconon','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316008','Lamsalome','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316011','New Dumangas','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316012','Sinolon','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316014','Lambangan','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316016','Maan','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316017','Afus','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316018','Lambuling','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316019','Lamhako','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316020','Poblacion','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316021','Talcon','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316022','Talufo','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316023','Tudok','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316024','Aflek','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316025','Datal Bob','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316026','Desawo','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316027','Dlanag','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316028','Lemsnolon','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316029','Malugong','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316030','Mongocayo','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316031','Salacafe','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126316032','T`bolok','126316000','T-BOLI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317001','Acmonan','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317002','Bololmala','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317003','Bunao','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317004','Cebuano','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317005','Crossing Rubber','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317006','Kablon','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317007','Kalkam','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317009','Linan','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317010','Lunen','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317011','Miasong','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317012','Palian','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317013','Poblacion','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317014','Polonuling','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317015','Simbo','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126317016','Tubeng','126317000','TUPI','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318001','Ambalgan','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318002','Guinsang-an','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318003','Katipunan','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318004','Manuel Roxas','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318005','Panay','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318006','Poblacion','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318007','San Isidro','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318008','San Vicente','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318009','Teresita','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126318010','Sajaneba','126318000','SANTO NIÑO','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319001','Bacdulong','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319002','Denlag','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319003','Halilan','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319004','Hanoon','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319005','Klubi','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319006','Lake Lahit','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319007','Lamcade','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319008','Lamdalag','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319009','Lamfugon','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319010','Lamlahak','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319011','Lower Maculan','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319012','Luhib','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319013','Ned','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319014','Poblacion','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319015','Siluton','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319016','Talisay','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319017','Takunel','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319018','Upper Maculan','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126319019','Tasiman','126319000','LAKE SEBU','126300000','SOUTH COTABATO');");
            db.execSQL("Insert into Barangay values('126501001','Bai Sarifinang','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501002','Biwang','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501003','Busok','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501004','Daguma','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501005','Kapaya','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501006','Kinayao','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501008','Masiag','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501009','Poblacion','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501010','South Sepaka','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501011','Tuka','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501012','Chua','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501013','Daluga','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501014','Kabulanan','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501015','Kanulay','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501016','Monteverde','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501017','Santo Niño','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501018','Sumilil','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501019','Titulok','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126501020','Sison','126501000','BAGUMBAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502002','Bantangan (Lasak)','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502009','Datablao','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502010','Eday','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502011','Elbebe','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502015','Libertad','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502016','Lomoyon','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502017','Makat (Sumali Pas)','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502019','Maligaya','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502021','Mayo','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502022','Natividad','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502023','Poblacion','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502024','Polomolok','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502027','Sinapulan','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502028','Sucob','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502030','Telafas','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126502032','Lasak','126502000','COLUMBIO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503001','Ala','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503003','Daladap','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503005','Dukay','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503007','Guiamalia','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503008','Ilian','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503011','Kangkong','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503016','Margues','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503018','New Panay','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503020','Numo','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503021','Pamantingan','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503022','Poblacion','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503023','Sagasa','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503024','Salabaca','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503032','Villamor','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503033','Laguinding','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503034','Magsaysay','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503035','Paitan','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503036','Saliao','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126503037','Salumping','126503000','ESPERANZA','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504003','Bambad','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504005','Bual','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504007','D`Lotilla','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504009','Dansuli','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504010','Impao','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504011','Kalawag I (Pob.)','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504012','Kalawag II (Pob.)','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504013','Kalawag III (Pob.)','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504016','Kenram','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504018','Kudanding','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504019','Kolambog','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504020','Lagandang','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504021','Laguilayan','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504022','Mapantig','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504024','New Pangasinan','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504026','Sampao','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126504028','Tayugo','126504000','ISULAN (Capital)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505001','Bantogon (Santa Clara)','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505004','Cadiz','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505006','Dumangas Nuevo','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505007','Hinalaan','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505011','Limulan','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505013','Obial','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505014','Paril','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505015','Poblacion','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505016','Sangay','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505017','Santa Maria','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505018','Datu Ito Andong','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505019','Datu Wasay','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505020','Nalilidan','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505021','Sabanal','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126505022','Pag-asa','126505000','KALAMANSIG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506001','Barurao','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506003','Basak','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506004','Bululawan','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506005','Capilan','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506006','Christiannuevo','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506007','Datu Karon','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506008','Kalamongog','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506009','Keytodac','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506010','Kinodalan','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506012','New Calinog','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506013','Nuling','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506014','Pansud','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506015','Pasandalan','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506016','Poblacion','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506017','Poloy-poloy','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506018','Purikay','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506019','Ragandang','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506020','Salaman','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506021','Salangsang','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506022','Taguisa','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506023','Tibpuan','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506024','Tran','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506025','Villamonte','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506026','Barurao II','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506027','Aurelio F. Freires (Poblacion II)','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126506028','Poblacion III','126506000','LEBAK','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507001','Antong','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507002','Bayasong','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507003','Blingkong','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507004','Lutayan Proper','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507005','Maindang','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507006','Mamali','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507007','Manili','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507008','Sampao','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507009','Sisiman','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507010','Tamnag (Pob.)','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126507011','Palavilla','126507000','LUTAYAN','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508001','Caridad (Cuyapon)','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508002','Didtaras','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508004','Gansing (Bilumen)','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508005','Kabulakan','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508006','Kapingkong','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508007','Katitisan','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508008','Lagao','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508009','Lilit','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508010','Madanding','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508011','Maligaya','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508012','Mamali','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508013','Matiompong','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508014','Midtapok','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508015','New Cebu','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508016','Palumbi','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508017','Pidtiguian','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508018','Pimbalayan','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508019','Pinguiaman','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508020','Poblacion (Lambayong)','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508021','Sadsalan','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508022','Seneben','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508023','Sigayan','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508024','Tambak','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508025','Tinumigues','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508027','Tumiao (Tinaga)','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126508028','Udtong','126508000','LAMBAYONG (MARIANO MARCOS)','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509001','Akol','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509002','Badiangon','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509003','Baliango','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509004','Baranayan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509005','Barongis','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509006','Batang-baglas','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509007','Butril','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509009','Domolol','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509010','Kabuling','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509011','Kalibuhan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509013','Kanipaan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509014','Kisek','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509015','Kidayan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509016','Kiponget','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509017','Kulong-kulong','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509018','Kraan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509020','Langali','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509021','Libua','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509022','Lumitan','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509023','Maganao','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509024','Maguid','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509025','Malatuneng (Malatunol)','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509026','Malisbong','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509027','Milbuk','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509028','Molon','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509029','Namat Masla','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509030','Napnapon','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509031','Poblacion','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509032','San Roque','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509033','Colobe (Tagadtal)','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509034','Tibuhol (East Badiangon)','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509035','Wal','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509036','Bambanen','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509038','Lopoken (Lepolon)','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509039','Mina','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509040','Medol','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509041','Wasag','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509042','Balwan (Bulan)','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509043','Ligao','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126509044','Datu Maguiales','126509000','PALIMBANG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510001','Bagumbayan','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510003','Bannawag','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510004','Bayawa','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510005','Estrella','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510007','Kalanawe I','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510008','Kalanawe II','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510009','Katico','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510012','Malingon','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510013','Mangalen','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510014','C. Mangilala','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510015','Pedtubo','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510016','Poblacion (Sambulawan)','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510018','Romualdez','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510020','San Jose','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510021','Sinakulay','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510022','Suben','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510023','Tinaungan','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510025','Tual (Liguasan)','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126510026','San Pedro (Tuato)','126510000','PRESIDENT QUIRINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511001','Baras','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511002','Buenaflor','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511003','Calean','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511004','Carmen','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511005','D`Ledesma','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511006','Gansing','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511007','Kalandagan','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511008','Lower Katungal','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511010','New Isabela','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511011','New Lagao','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511012','New Passi','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511013','Poblacion','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511014','Rajah Nuda','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511015','San Antonio','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511016','San Emmanuel','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511019','San Pablo','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511020','Upper Katungal','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511021','Tina','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511022','San Rafael','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126511023','Lancheta','126511000','CITY OF TACURONG','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512001','Banali','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512002','Basag','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512003','Buenaflores','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512004','Bugso','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512005','Buklod','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512006','Gapok','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512007','Kadi','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512008','Kapatagan','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512009','Kiadsam','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512010','Kuden','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512011','Kulaman','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512012','Lagubang','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512013','Langgal','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512014','Limuhay','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512015','Malegdeg','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512016','Midtungok','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512017','Nati','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512018','Sewod','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512019','Tacupis','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('126512020','Tinalon','126512000','SEN. NINOY AQUINO','126500000','SULTAN KUDARAT');");
            db.execSQL("Insert into Barangay values('128001001','Alegria','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001002','Bagacay','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001003','Baluntay','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001004','Datal Anggas','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001005','Domolok','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001006','Kawas','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001007','Maribulan','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001008','Pag-Asa','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001009','Paraiso','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001010','Poblacion (Alabel)','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001011','Spring','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128001012','Tokawal','128001000','ALABEL (Capital)','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002001','Baliton','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002002','Batotuling','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002003','Batulaki','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002004','Big Margus','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002005','Burias','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002006','Cablalan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002007','Calabanit','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002008','Calpidong','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002009','Congan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002010','Cross','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002011','Datalbukay','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002012','E. Alegado','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002013','Glan Padidu','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002014','Gumasa','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002015','Ilaya','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002016','Kaltuad','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002017','Kapatan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002018','Lago','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002019','Laguimit','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002020','Mudan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002021','New Aklan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002022','Pangyan','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002023','Poblacion','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002024','Rio Del Pilar','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002025','San Jose','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002026','San Vicente','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002027','Small Margus','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002028','Sufatubo','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002029','Taluya','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002030','Tango','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128002031','Tapon','128002000','GLAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003001','Badtasan','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003002','Datu Dani','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003003','Gasi','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003004','Kapate','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003005','Katubao','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003006','Kayupo','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003007','Kling (Lumit)','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003008','Lagundi','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003009','Lebe','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003010','Lomuyon','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003011','Luma','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003012','Maligang','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003013','Nalus','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003014','Poblacion','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003015','Salakit','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003016','Suli','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003017','Tablao','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003018','Tamadang','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128003019','Tambilil','128003000','KIAMBA','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004001','Amsipit','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004002','Bales','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004003','Colon','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004004','Daliao','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004005','Kabatiol','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004006','Kablacan','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004007','Kamanga','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004008','Kanalo','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004009','Lumasal','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004010','Lumatil','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004011','Malbang','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004012','Nomoh','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004013','Pananag','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004014','Poblacion (Maasim)','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004015','Seven Hills','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128004016','Tinoto','128004000','MAASIM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005001','Bati-an','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005002','Kalaneg','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005003','Kalaong','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005004','Kiambing','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005005','Kiayap','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005006','Mabay','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005007','Maguling','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005008','Malalag (Pob.)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005009','Mindupok','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005010','New La Union','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005011','Old Poblacion (Maitum)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005012','Pangi (Linao)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005013','Pinol','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005014','Sison (Edenton)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005015','Ticulab','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005016','Tuanadatu','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005017','Upo (Lanao)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005018','Wali (Kambuhan)','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128005019','Zion','128005000','MAITUM','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006001','Daan Suyan','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006002','Kihan','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006003','Kinam','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006004','Libi','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006005','Lun Masla','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006006','Lun Padidu','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006007','Patag','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006008','Poblacion (Malapatan)','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006009','Sapu Masla','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006010','Sapu Padidu','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006011','Tuyan','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128006012','Upper Suyan','128006000','MALAPATAN','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007001','Alkikan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007002','Ampon','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007003','Atlae','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007004','Banahaw','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007005','Banate','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007006','B`Laan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007007','Datal Batong','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007008','Datal Bila','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007009','Datal Tampal','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007010','J.P. Laurel','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007011','Kawayan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007012','Kibala','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007013','Kiblat','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007014','Kinabalan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007015','Lower Mainit','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007016','Lutay','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007017','Malabod','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007018','Malalag Cogon','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007019','Malandag','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007020','Malungon Gamay','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007021','Nagpan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007022','Panamin','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007023','Poblacion','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007024','San Juan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007025','San Miguel','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007026','San Roque','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007027','Talus','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007028','Tamban','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007029','Upper Biangan','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007030','Upper Lumabat','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('128007031','Upper Mainit','128007000','MALUNGON','128000000','SARANGANI');");
            db.execSQL("Insert into Barangay values('129804001','Bagua Proper','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804002','Bagua I','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804003','Bagua II','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804004','Bagua III','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804005','Kalanganan','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804006','Kalanganan I','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804007','Kalanganan II','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804008','Poblacion Proper','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804009','Poblacion I','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804010','Poblacion II','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804011','Poblacion III','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804012','Poblacion IV','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804013','Poblacion V','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804014','Poblacion VI','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804015','Poblacion VII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804016','Poblacion VIII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804017','Poblacion IX','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804018','Rosary Heights Proper','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804019','Rosary Heights I','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804020','Rosary Heights II','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804021','Rosary Heights III','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804022','Rosary Heights IV','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804023','Rosary Heights V','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804024','Rosary Heights VI','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804025','Rosary Heights VII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804026','Rosary Heights VIII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804027','Rosary Heights IX','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804028','Rosary Heights X','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804029','Rosary Heights XI','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804030','Rosary Heights XII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804031','Rosary Heights XIII','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804032','Tamontaka Proper','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804033','Tamontaka I','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804034','Tamontaka II','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804035','Tamontaka III','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804036','Tamontaka IV','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804037','Tamontaka V','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('129804038','Tamontaka VI','129804000','COTABATO CITY','129804000','COTABATO CITY (Not a Province)');");
            db.execSQL("Insert into Barangay values('153801004','Dicalongan (Pob.)','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801011','Kakal','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801012','Kamasi','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801014','Kapinpilan','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801015','Kauran','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801018','Malatimon','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801022','Matagabong','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801028','Saniag','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801031','Tomicor','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801032','Tubak','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153801035','Salman','153801000','AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802001','Ampuan','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802002','Aratuc','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802005','Cabayuan','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802007','Calaan (Pob.)','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802008','Karim','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802009','Dinganen','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802011','Edcor (Gallego Edcor)','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802012','Kulimpang','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802017','Mataya','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802018','Minabay','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802020','Nuyo','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802021','Oring','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802022','Pantawan','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802023','Piers','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153802025','Rumidas','153802000','BULDON','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803008','Digal','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803023','Lower Siling','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803030','Maslabeng','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803039','Poblacion','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803040','Popol','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803045','Talitay','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153803054','Upper Siling','153803000','BULUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805001','Alip (Pob.)','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805003','Damawato','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805004','Katil','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805006','Malala','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805007','Mangadeg','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805008','Manindolo','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805010','Puya','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805011','Sepaka','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805013','Lomoyon','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805014','Kalumenga (Kalumanga)','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805015','Palao sa Buto','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805016','Damalusay','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805017','Bonawan','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805018','Bulod','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805019','Datang','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805020','Elbebe','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805021','Lipao','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805022','Madidis','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805023','Makat','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805024','Mao','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805025','Napok','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805026','Poblacion','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153805027','Salendab','153805000','DATU PAGLAS','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806001','Alonganan','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806002','Ambadao','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806005','Balanakan','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806006','Balong','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806008','Buayan','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806010','Dado','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806011','Damabalas','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806015','Duaminanga','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806021','Kalipapa','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806025','Liong','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806028','Magaslong','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806029','Masigay','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806030','Montay','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806034','Poblacion (Dulawan)','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806035','Reina Regente','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153806039','Kanguan','153806000','DATU PIANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807001','Ambolodto','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807002','Awang','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807003','Badak','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807004','Bagoenged','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807005','Baka','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807006','Benolen','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807007','Bitu','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807008','Bongued','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807009','Bugawas','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807010','Capiton','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807011','Dados','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807013','Dalican Poblacion','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807014','Dinaig Proper','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807015','Dulangan','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807017','Kakar','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807018','Kenebeka','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807019','Kurintem','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807020','Kusiong','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807021','Labungan','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807022','Linek','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807023','Makir','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807024','Margues','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807025','Nekitan','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807026','Mompong','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807027','Sapalan','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807028','Semba','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807029','Sibuto','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807030','Sifaren (Sifaran)','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807031','Tambak','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807032','Tamontaka','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807033','Tanuel','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807034','Tapian','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807035','Taviran','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153807036','Tenonggos','153807000','DATU ODIN SINSUAT (DINAIG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808002','Bagong','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808004','Bialong','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808011','Kuloy','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808012','Labu-labu','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808013','Lapok (Lepok)','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808023','Malingao','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808034','Poblacion','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808037','Satan','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808038','Tapikan','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808039','Timbangan','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808040','Tina','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808046','Poblacion I','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153808047','Poblacion II','153808000','SHARIFF AGUAK (MAGANOY) (Capital)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809001','Bayanga Norte','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809002','Bayanga Sur','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809003','Bugasan Norte','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809004','Bugasan Sur (Pob.)','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809005','Kidama','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809007','Sapad','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809008','Langco','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153809009','Langkong','153809000','MATANOG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810001','Bagoenged','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810004','Buliok','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810006','Damalasak','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810008','Galakit','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810009','Inug-ug','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810010','Kalbugan','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810013','Kilangan','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810014','Kudal','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810015','Layog','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810017','Linandangan','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810021','Poblacion','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153810025','Dalgan','153810000','PAGALUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811008','Gadungan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811009','Gumagadong Calawag','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811010','Guiday T. Biruar','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811012','Landasan (Sarmiento)','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811013','Limbayan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811014','Bongo Island (Litayen)','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811015','Magsaysay','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811016','Making','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811017','Nituan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811018','Orandang','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811019','Pinantao','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811020','Poblacion','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811021','Polloc','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811023','Tagudtongan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811024','Tuca-Maror','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811025','Manion','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811026','Macasandag','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811027','Cotongan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811028','Poblacion II','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811029','Samberen','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811030','Kabuan','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811031','Campo Islam','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811032','Datu Macarimbang Biruar','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811033','Gadunganpedpandaran','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153811034','Moro Point','153811000','PARANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812001','Alamada','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812003','Banatin','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812004','Banubo','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812006','Bulalo','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812007','Bulibod','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812009','Calsada','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812010','Crossing Simuay','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812012','Dalumangcob (Pob.)','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812013','Darapanan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812014','Gang','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812015','Inawan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812016','Kabuntalan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812017','Kakar','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812018','Kapimpilan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812019','Katidtuan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812020','Katuli','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812023','Ladia','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812024','Limbo','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812026','Maidapa','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812027','Makaguiling','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812028','Katamlangan (Matampay)','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812029','Matengen','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812030','Mulaug','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812031','Nalinan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812033','Nekitan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812034','Olas','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812035','Panatan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812036','Pigcalagan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812037','Pigkelegan (Ibotegen)','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812038','Pinaring','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812039','Pingping','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812040','Raguisi','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812041','Rebuken','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812042','Salimbao','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812043','Sambolawan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812044','Senditan','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812051','Ungap','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812052','Damaniog','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153812053','Nara','153812000','SULTAN KUDARAT (NULING)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813001','Angkayamat','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813003','Barurao','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813004','Bulod','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813006','Darampua','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813008','Gadungan','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813013','Kulambog','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813015','Langgapanan','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813021','Masulot','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813028','Papakan','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813045','Tugal','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813046','Tukanakuden','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153813050','Paldong','153813000','SULTAN SA BARONGIS (LAMBAYONG)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814001','Bagumbayan','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814003','Dadtumog (Dadtumeg)','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814005','Gambar','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814006','Ganta','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814008','Katidtuan','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814009','Langeban','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814011','Liong','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814012','Maitong','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814013','Matilak','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814014','Pagalungan','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814015','Payan','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814016','Pened','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814017','Pedtad','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814018','Poblacion','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814019','Upper Taviran','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814026','Buterin','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153814028','Lower Taviran','153814000','KABUNTALAN (TUMBAO)','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815002','Borongotan','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815003','Bayabas','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815004','Blensong','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815005','Bugabungan','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815006','Bungcog','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815008','Darugao','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815009','Ganasi','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815010','Kabakaba','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815012','Kibleg','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815013','Kibucay','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815014','Kiga','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815017','Kinitan (Kinitaan)','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815022','Mirab','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815023','Nangi','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815025','Nuro Poblacion','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815028','Bantek','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815029','Ranao Pilayan','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815030','Rempes','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815031','Renede','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815032','Renti','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815034','Rifao','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815036','Sefegefen','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153815042','Tinungkaan','153815000','UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816005','Boboguiron','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816007','Damablac','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816008','Fugotan','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816009','Fukol','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816013','Katibpuan','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816014','Kedati','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816018','Lanting','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816019','Linamunan','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816025','Marader','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816028','Binangga North','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816032','Binangga South','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816033','Talayan','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816035','Tamar','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816036','Tambunan I','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153816038','Timbaluan','153816000','TALAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817001','Kuya','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817002','Biarong','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817003','Bongo','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817004','Itaw','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817005','Kigan','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817006','Lamud','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817007','Looy','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817008','Pandan','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817009','Pilar','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817010','Romangaob (Pob.)','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153817011','San Jose','153817000','SOUTH UPI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818001','Barira (Pob.)','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818002','Bualan','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818003','Gadung','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818004','Liong','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818005','Lipa','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818006','Lipawan','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818007','Marang','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818008','Nabalawag','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818009','Rominimbang','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818010','Togaig','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818011','Minabay','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818012','Korosoyan','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818013','Lamin','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153818014','Panggao','153818000','BARIRA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819001','Badak','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819002','Bulod','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819005','Kaladturan','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819006','Kulasi','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819007','Lao-lao','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819008','Lasangan','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819009','Lower Idtig','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819010','Lumabao','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819011','Makainis','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819012','Midconding','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819013','Midpandacan','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819015','Panosolen','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819016','Ramcor','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819017','Tonggol','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819018','Pidtiguian','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819019','Quipolot','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819020','Sadangin','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819021','Sumakubay','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153819022','Upper Lasangan','153819000','GEN. S. K. PENDATUN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820001','Bagumbong','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820002','Dabenayan','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820003','Daladap','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820004','Dasikil','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820006','Liab','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820007','Libutan','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820009','Lusay','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820010','Mamasapano','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820011','Manongkaling','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820013','Pidsandawan','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820014','Pimbalakan','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820016','Sapakan','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820017','Tuka','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153820018','Tukanalipao','153820000','MAMASAPANO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821002','Bintan (Bentan)','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821003','Gadungan','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821004','Kiladap','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821005','Kilalan','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821006','Kuden','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821007','Makadayon','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821008','Manggay','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821011','Pageda','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153821012','Talitay','153821000','TALITAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822001','Balatungkayo (Batungkayo)','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822002','Bulit','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822003','Bulod','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822004','Dungguan','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822005','Limbalud','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822006','Maridagao','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822007','Nabundas','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822008','Pagagawan','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822009','Talapas','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822010','Talitay','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153822011','Tunggol','153822000','PAGAGAWAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823001','Damakling','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823002','Damalusay','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823003','Paglat','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823004','Upper Idtig','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823005','Campo','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823006','Kakal','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823007','Salam','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153823008','Tual','153823000','PAGLAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824001','Balut','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824002','Boliok','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824003','Bungabong','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824004','Dagurongan','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824005','Kirkir','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824006','Macabico (Macabiso)','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824007','Namuken','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824008','Simuay/Seashore','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824009','Solon','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824010','Tambo','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824011','Tapayan','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824012','Tariken','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153824013','Tuka','153824000','SULTAN MASTURA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825001','Ahan','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825002','Bagan','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825003','Datalpandan','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825004','Kalumamis','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825005','Kateman','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825006','Lambayao','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825007','Macasampen','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825008','Muslim','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825009','Muti','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825010','Sampao','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153825011','Tambunan II','153825000','GUINDULUNGAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826002','Dapiawan','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826003','Elian','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826005','Gawang','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826007','Kabengi','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826008','Kitango','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826009','Kitapok','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826010','Madia','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153826013','Salbu','153826000','DATU SAUDI-AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827001','Bulayan','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827002','Iganagampong','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827003','Macalag','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827004','Maitumaig','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827005','Malangog','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827006','Meta','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827008','Panangeti','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153827010','Tuntungan','153827000','DATU UNSAY','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828001','Banaba','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828002','Dimampao','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828003','Guinibon','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828004','Kaya-kaya','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828005','Maganoy','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828006','Mao','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828007','Maranding','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828008','Sugadol','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828009','Talisawa','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153828010','Tukanolocong (Tukanologong)','153828000','DATU ABDULLAH SANGKI','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829001','Baital','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829002','Bakat','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829003','Dapantis','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829004','Gaunan','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829005','Malibpolok','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829006','Mileb','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829007','Panadtaban','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829008','Pidsandawan','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829009','Sampao','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829010','Sapakan (Pob.)','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153829011','Tabungao','153829000','RAJAH BUAYAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830001','Kinimi','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830002','Laguitan','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830003','Lapaken','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830004','Matuber','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830005','Meti','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830006','Nalkan','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830007','Penansaran','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830008','Resa','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830009','Sedem','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830010','Sinipak','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830011','Tambak','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830012','Tubuan','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153830013','Pura','153830000','DATU BLAH T. SINSUAT','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831001','Adaon','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831002','Brar','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831003','Mapayag','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831004','Midtimbang (Pob.)','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831005','Nunangan (Nunangen)','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831006','Tugal','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153831007','Tulunan','153831000','DATU ANGGAL MIDTIMBANG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832001','Daladagan','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832002','Kalian','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832003','Luayan','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832004','Paitan','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832005','Panapan','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832006','Tenok','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832007','Tinambulan','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153832008','Tumbao','153832000','MANGUDADATU','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833001','Kabuling','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833002','Kayaga','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833003','Kayupo (Cuyapo)','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833004','Lepak','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833005','Lower Dilag','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833006','Malangit','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833007','Pandag','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153833008','Upper Dilag','153833000','PANDAG','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834001','Balong','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834002','Damatog','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834003','Gayonga','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834004','Guiawa','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834005','Indatuan','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834006','Kapimpilan','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834007','Libungan','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834008','Montay','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834009','Paulino Labio','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834010','Sabaken','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153834011','Tumaguinting','153834000','NORTHERN KABUNTALAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835001','Kubentong','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835002','Labu-labu I','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835003','Labu-labu II','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835004','Limpongo','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835005','Sayap','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835006','Taib','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835007','Talibadok','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835008','Tuayan','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835009','Tuayan I','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835010','Macalag','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153835011','Tuntungan','153835000','DATU HOFFER AMPATUAN','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836001','Alonganan','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836002','Andavit','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836003','Balanakan','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836004','Buayan','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836005','Butilen','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836006','Dado','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836007','Damabalas','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836008','Duaminanga','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836009','Kalipapa','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836010','Liong','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836011','Magaslong','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836012','Masigay','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836013','Pagatin','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836014','Pandi','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836015','Penditen','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836016','Sambulawan','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153836017','Tee','153836000','DATU SALIBO','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837001','Bakat','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837002','Dale-Bong','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837003','Dasawao','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837004','Datu Bakal','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837005','Datu Kilay','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837006','Duguengen','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837007','Ganta','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837008','Inaladan','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837009','Linantangan','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837010','Nabundas','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837011','Pagatin','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837012','Pamalian','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837013','Pikeg','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837014','Pusao','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837015','Libutan','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153837016','Pagatin (Pagatin I)','153837000','SHARIFF SAYDONA MUSTAPHA','153800000','MAGUINDANAO');");
            db.execSQL("Insert into Barangay values('153983001','Bucto','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983002','Burboanan','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983003','Caguyao','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983004','Coleto','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983005','Cumawas','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983006','Kahayag','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983007','Labisma','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983008','Lawigan','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983009','Maharlika','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983010','Mangagoy (City Downtown)','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983011','Mone','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983012','Pamanlinan','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983013','Pamaypayan','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983014','Poblacion (Bislig Proper)','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983015','San Antonio','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983016','San Fernando','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983017','San Isidro (Bagnan)','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983018','San Jose','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983019','San Roque (Cadanglasan)','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983020','San Vicente','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983021','Santa Cruz','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983022','Sibaroy','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983023','Tabon','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983024','Tumanan','153983110','BISLIG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983025','Baculin','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983026','Bigaan','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983027','Cambatong','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983028','Campa','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983029','Dugmanon','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983030','Harip','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983031','La Casa (Pob.)','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983032','Loyola','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983033','Maligaya','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983034','Pagtigui-an (Bitoon)','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983035','Pocto','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983036','Port Lamon','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983037','Roxas','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983038','San Juan','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983039','Sasa','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983040','Tagasaka','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983041','Tagbobonga','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983042','Talisay','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983043','Tarusan','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983044','Tidman','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983045','Tiwi','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983046','Zone I Benigno Aquino (Pob.)','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983047','Zone II Sto. Niño (Pob.)','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983048','Zone III Maharlika (Pob.)','153983100','HINATUAN','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983049','Awasian (Tandag Airport)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983050','Bag-ong Lungsod (Poblacion)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983051','Bioto','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983052','Bongtud (Poblacion)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983053','Buenavista (includes Mahayag)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983054','Dagocdoc (Poblacion)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983055','Mabua (Poblacion)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983056','Mabuhay','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983057','Maitum','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983058','Maticdum','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983059','Pandanon','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983060','Pangi','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983061','Quezon','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983062','Rosario','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983063','Salvacion','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983064','San Agustin Norte','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983065','San Agustin Sur a.k.a. Dawis','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983066','San Antonio','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983067','San Isidro','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983068','San Jose','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('153983069','Telaje (Poblacion)','153983000','TANDAG CITY','153900000','SURIGAO DEL SUR');");
            db.execSQL("Insert into Barangay values('154085080','Berseba','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085081','Bucac','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085082','Cagbas','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085083','Calaitan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085084','Canayugan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085085','Charito','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085086','Claro Cortez','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085087','Fili','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085088','Gamao','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085089','Getsemane','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085090','Grace Estate','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085091','Hamogaway','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085092','Katipunan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085093','Mabuhay','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085094','Magkiangkang','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085095','Mahayag','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085096','Marcelina','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085097','Maygatasan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085098','Montivesta','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085099','Mt. Ararat','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085100','Mt. Carmel','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085101','Mt. Olive','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085102','New Salem','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085103','Noli','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085104','Osmeña','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085105','Panaytay','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085106','Pinagalaan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085107','Poblacion','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085108','Sagmone','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085109','Saguma','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085110','Salvacion','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085111','San Agustin','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085112','San Isidro','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085113','San Juan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085114','Santa Irene','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085115','Santa Teresita','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085116','Santo Niño','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085117','Taglatawan','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085118','Taglibas','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085119','Tagubay','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085120','Verdu','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085121','Villa Ondayon','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085122','Wawa','154085070','BAYUGAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085123','Bunawan Brook','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085124','Consuelo','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085125','Imelda','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085126','Libertad','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085127','Mambalili','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085128','Nueva Era','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085129','Poblacion','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085130','San Andres','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085131','San Marcos','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085132','San Teodoro','154085080','BUNAWAN','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085133','Agsabu','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085134','Aguinaldo','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085135','Anolingan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085136','Bakingking','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085137','Balubo','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085138','Bentahon','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085139','Bunaguit','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085140','Catmonon','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085141','Cebulan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085142','Concordia','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085143','Crossing Luna','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085144','Cubo','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085145','Dakutan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085146','Duangan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085147','Guadalupe','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085148','Guibonon','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085149','Hawilian','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085150','Kalabuan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085151','Kinamaybay','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085152','Labao','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085153','Langag','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085154','Ling-ling','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085155','Maasin','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085156','Mac-Arthur','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085157','Mahagcot','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085158','Maliwanag','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085159','Milagros','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085160','Nato','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085161','New Gingoog','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085162','Odiong','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085163','Oro','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085164','Piglawigan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085165','Poblacion','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085166','Remedios','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085167','Salug','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085168','San Isidro','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085169','San Jose','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085170','San Toribio','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085171','San Vicente','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085172','Santa Fe','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085173','Segunda','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085174','Sinakungan','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085175','Tagabase','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085176','Taganahaw','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085177','Tagbalili','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085178','Tahina','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085179','Tandang Sora','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085180','Valentina','154085090','ESPERANZA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085181','Angeles','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085182','Bataan','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085183','Comota','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085184','Halapitan','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085185','Kasapa II','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085186','Langasian','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085187','Lydia','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085188','Osmeña, Sr.','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085189','Panagangan','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085190','Poblacion','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085191','Sabang Adgawan','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085192','Sagunto','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085193','San Patricio','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085194','Valentina','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085195','Villa Paz','154085100','LA PAZ','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085196','Binucayan','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085197','Johnson','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085198','Kasapa','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085199','Katipunan','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085200','Kauswagan','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085201','Magaud','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085202','Nueva Gracia','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085203','Poblacion','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085204','Sabud','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085205','San Isidro','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085206','San Mariano','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085207','San Vicente','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085208','Santa Teresa','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085209','Santo Niño','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085210','Santo Tomas','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085211','Violanta','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085212','Waloe','154085110','LORETO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085213','Aurora','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085214','Awa','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085215','Azpetia','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085216','La Caridad','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085217','La Perian','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085218','La Purisima','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085219','La Suerte','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085220','La Union','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085221','Las Navas','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085222','Libertad','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085223','Los Arcos','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085224','Lucena','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085225','Mabuhay','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085226','Magsaysay','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085227','Mapaga','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085228','Napo','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085229','New Maug','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085230','Patin-ay','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085231','Poblacion','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085232','Pukengkay','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085233','Salimbogaon','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085234','Salvacion','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085235','San Joaquin','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085236','San Jose','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085237','San Lorenzo','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085238','San Martin','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085239','San Pedro','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085240','San Rafael','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085241','San Roque','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085242','San Salvador','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085243','San Vicente','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085244','Santa Irene','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085245','Santa Maria','154085120','PROSPERIDAD','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085246','Bayugan 3','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085247','Cabantao','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085248','Cabawan','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085249','Libuac','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085250','Maligaya','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085251','Marfil','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085252','Novele','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085253','Poblacion','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085254','Santa Cruz','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085255','Tagbayagan','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085256','Wasi-an','154085130','ROSARIO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085257','Alegria','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085258','Barangay 1 (Pob.)','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085259','Barangay 2 (Pob.)','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085260','Barangay 3 (Pob.)','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085261','Barangay 4 (Pob.)','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085262','Barangay 5 (Pob.)','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085263','Bayugan 2','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085264','Bitan-agan','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085265','Borbon','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085266','Buenasuerte','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085267','Caimpugan','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085268','Das-agan','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085269','Ebro','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085270','Hubang','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085271','Karaos','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085272','Ladgadan','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085273','Lapinigan','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085274','Lucac','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085275','Mate','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085276','New Visayas','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085277','Ormaca','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085278','Pasta','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085279','Pisa-an','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085280','Rizal','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085281','San Isidro','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085282','Santa Ana','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085283','Tagapua','154085010','SAN FRANSICO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085284','Anislagan','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085285','Balit','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085286','Baylo','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085287','Binicalan','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085288','Cecilia','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085289','Coalicion','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085290','Culi','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085291','Dimasalang','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085292','Don Alejandro','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085293','Don Pedro','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085294','Doña Flavia','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085295','Doña Maxima','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085296','Mahagsay','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085297','Mahapag','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085298','Mahayahay','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085299','Muritula','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085300','Nuevo Trabajo','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085301','Poblacion','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085302','Policarpo','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085303','San Isidro','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085304','San Pedro','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085305','Santa Ines','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085306','Santa Rita','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085307','Santiago','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085308','Wegguam','154085140','SAN LUIS','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085309','Angas','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085310','Aurora','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085311','Awao','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085312','Conception','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085313','Pag-asa','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085314','Patrocinio','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085315','Poblacion','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085316','San Jose','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085317','Santa Isabel','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085318','Sayon','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085319','Tapz','154085060','SANTA JOSEFA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085320','Afga','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085321','Anahawan','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085322','Banagbanag','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085323','Del Rosario','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085324','El Rio','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085325','Ilihan','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085326','Kauswagan','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085327','Kioya','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085328','Kolambugan','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085329','Magkalape','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085330','Magsaysay','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085331','Mahayahay','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085332','New Tubigon','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085333','Padiay','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085334','Perez','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085335','Poblacion','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085336','San Isidro','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085337','San Vicente','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085338','Santa Cruz','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085339','Santa Maria','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085340','Sinai','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085341','Tabon-tabon','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085342','Tag-uyango','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085343','Villangit','154085150','SIBAGAT','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085344','Batucan','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085345','Buena Gracia','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085346','Causwagan','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085347','Culi','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085348','Del Monte','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085349','Desamparados','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085350','La Flora','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085351','Labnig','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085352','Maharlika','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085353','Marbon','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085354','Sabang Gibung','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085355','San Agustin (Pob.)','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085356','San Isidro (Pob.)','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085357','San Nicolas (Pob.)','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085358','Zamora','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085359','Zillovia','154085160','TALACOGON','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085360','baryo lagubo','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085361','Basa','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085362','Cebolin','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085363','Cuevas','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085364','Kapatungan','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085365','Langkila-an','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085366','Manat','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085367','New Visayas','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085368','Pangyan','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085369','Poblacion','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085370','Pulang-lupa','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085371','Salvacion','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085372','San Ignacio','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085373','San Isidro','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085374','San Roque','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085375','Santa Maria','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085376','Tribu AKRO','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085377','Tudela','154085050','TRENTO','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085378','Anitap','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085379','Bacay II','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085380','Binongan','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085381','Caigangan','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085382','Candiis','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085383','Del Monte','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085384','Don Mateo','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085385','Katipunan','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085386','La Fortuna','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085387','Limot','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085388','Magsaysay','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085389','Masayan','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085390','Poblacion','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085391','Sampaguita','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085392','San Gabriel','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085393','Santa Cruz','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085394','Santa Emelia','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085395','Sawagan','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085396','Sinobong','154085170','VERUELA','154000000','AGUSAN DEL SUR');");
            db.execSQL("Insert into Barangay values('154085397','Sisimon','154085170','VERUELA','154000000','AGUSAN DEL SUR');");

        }
        // db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
    }

    private void insertreceivernum() {

        Cursor c = db.rawQuery("select * from receivernumber", null);
        c.moveToFirst();
        int cnt = c.getCount();
        if (cnt > 0) {
            // Toast.makeText(getApplicationContext(), "Record " + cnt, Toast.LENGTH_LONG).show();
            Log.d("insertreceivernum", "Record " + cnt);
        } else {

            if ("UC".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177034043');");
            } else if ("RM".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177034043');");
            } else if ("CL".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177034043');");
            } else if ("MI".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177034043');");
            } else if ("LY".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177105906');");
            } else if ("CC".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177105906');");
            } else if ("PL".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177105906');");
            } else if ("SO".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177105906');");
            } else if ("EQ".equals(DeptCode)) {
                db.execSQL("Insert into receivernumber values('+639177105906');");
            } else {
                db.execSQL("Insert into receivernumber values('+639177105901');");
            }
            // db.execSQL("Insert into receivernumber values('+639068828881');");
        }
    }

    // -------------------- sendSMS with proper actions & flags --------------------
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public void sendSMS(String phoneNumber, String message) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), android.app.PendingIntent.FLAG_IMMUTABLE);

            registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "Service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!" + ", ERROR :" + e, Toast.LENGTH_LONG).show();
            Log.e("SEND SMS", "SMS MANAGER: " + e.getMessage());
        }
    }
}

