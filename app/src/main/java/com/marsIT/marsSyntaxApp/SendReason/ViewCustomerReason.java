package com.marsIT.marsSyntaxApp.SendReason;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
//import android.view.Menu;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import android.R.integer;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.view.LayoutInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.annotation.RequiresApi;
import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;

@SuppressWarnings("deprecation")
public class ViewCustomerReason extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    private boolean checkvalidation;
    private String theMobNo;
//    private String validation;
//    private String longitude;
//    private String latitude;
    private String DatabaseName;
    private EditText editRemarks;
    private TextView custid;
    private TextView custname;
    private TextView datetimesyn;
//    private List<String> lables;
    private SQLiteDatabase	db;
    private Button btsendsave;
    private TextView otherreason;
    private TextView reasonselected;
    private TextView reasondescription;
//    private TextView totalinvqty;
//    private TextView totalinvamount;
//    private Integer countnoorder;
    private String Syntaxdatetime;
    private String numreasonrecord;

    //	private TextView totalamountinv;

//    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcustomerreason);
        // Show the Up button in the action bar.
        db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

        try {

            setupToolbar("View Customer" +
                    " Reason");
            // Force menu to appear
            invalidateOptionsMenu();

            numreasonrecord = "";
            numreasonrecord = getIntent().getExtras().getString("numreason");

            Cursor ctemp = db.rawQuery("select l.num,l.customerid,reasonselect,reason,syntaxdatetime,c.cname from noOrdercustomerlist l inner join CUSTOMERS c on l.customerid = c.cid where num = '"+ numreasonrecord +"'", null);
            ctemp.moveToFirst();
            btsendsave = findViewById(R.id.btsendreasonnoorderview);
            btsendsave.setOnClickListener(this);

            otherreason = findViewById(R.id.tvotherreasonview);
            otherreason.setText(ctemp.getString(3));

            reasonselected = findViewById(R.id.tvreasonselectedview);
            reasonselected.setText(ctemp.getString(2));

            reasondescription = findViewById(R.id.tvreasondescription);

            if("0".equals(ctemp.getString(2))){
                reasondescription.setText("- High Inventory");
            }else if("1".equals(ctemp.getString(2))){
                reasondescription.setText("- Uncollected Due/Collection");
            }else if("2".equals(ctemp.getString(2))){
                reasondescription.setText("- Owner is Out");
            }else if("3".equals(ctemp.getString(2))){
                reasondescription.setText("- Purchaser is Out");
            }else if("4".equals(ctemp.getString(2))){
                reasondescription.setText("- Other");
            }

            custid = findViewById(R.id.tvcustomeridnoorderview);
            custid.setText(ctemp.getString(1));

            custname = findViewById(R.id.tvcustomernamenoorderview);
            custname.setText(ctemp.getString(5));

            Syntaxdatetime = "" + ctemp.getString(4);
            datetimesyn = findViewById(R.id.tvdatetimenoorderview);
            datetimesyn.setText(ctemp.getString(4));

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 =  c2.getCount();
            if(cnt2 > 0){
                DatabaseName = c2.getString(6); // database name
            }else{
                DatabaseName = "";
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        // getclick = spctype.getSelectedItemPosition();
        String Detailsinvoice = "";

        if("MARS2".equals(DatabaseName)){
            Detailsinvoice = "CUSTNO2!";
        }else{
            Detailsinvoice = "CUSTNO!";
        }

        checkvalidation = true;

        if(v.getId() == R.id.btsendreasonnoorderview){ // save details


            try{
                Cursor ctemp = db.rawQuery("select l.num,l.customerid,reasonselect,reason,syntaxdatetime,c.cname from noOrdercustomerlist l inner join CUSTOMERS c on l.customerid = c.cid where num = '"+ numreasonrecord +"'", null);
                ctemp.moveToFirst();

                Detailsinvoice = Detailsinvoice + numreasonrecord + "!" + custid.getText().toString() + "!" + ctemp.getString(2) + "!" + ctemp.getString(3) + "!" + Syntaxdatetime;

                // query receiver number
                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                int cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }
                // Toast.makeText(getApplicationContext(), theMobNo + " " + Detailsinvoice, Toast.LENGTH_LONG).show();
                sendSMS(theMobNo,Detailsinvoice);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            // Navigate back manually
            finish(); // Closes the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

        if(which == DialogInterface.BUTTON_NEUTRAL){
            editRemarks.setText("Sorry, location is not determined. To fix this please enable location providers");
        }else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    // sends an SMS message to another device
    @SuppressLint({"InlinedApi", "UnspecifiedRegisterReceiverFlag"})
    public void sendSMS(String phoneNumber, String message) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

            BroadcastReceiver sentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            BroadcastReceiver deliveredReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            IntentFilter sentFilter = new IntentFilter(SENT);
            IntentFilter deliveredFilter = new IntentFilter(DELIVERED);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
                registerReceiver(sentReceiver, sentFilter, Context.RECEIVER_NOT_EXPORTED);
                registerReceiver(deliveredReceiver, deliveredFilter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(sentReceiver, sentFilter);
                registerReceiver(deliveredReceiver, deliveredFilter);
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

            Toast.makeText(getApplicationContext(), "Sending Reason, Please Wait!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later! ERROR: " + e, Toast.LENGTH_LONG).show();
            Log.e("ERROR sendSMS", "SMS failed, please try again later! ERROR: ", e);
        }
    }
}
