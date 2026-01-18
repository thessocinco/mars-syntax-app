package com.marsIT.marsSyntaxApp.SendReason;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.activity.OnBackPressedCallback;
import com.marsIT.marsSyntaxApp.MainProgram.MainActivity;
import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;

public class SendReason extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    private boolean checkvalidation;
    private String theMobNo;
//    private String validation;
//    private String longitude;
//    private String latitude;
    private String Databasename;
    private String Department;
    private String PressOption ="";
    private EditText editRemarks;
    private TextView custid;
    private TextView custname;
    private List<String> lables;
    private SQLiteDatabase	db;
    private Button btsendsave;
    private EditText otherreason;
    private Spinner reason;
//    private TextView totalinvqty;
//    private TextView totalinvamount;
    private Integer countnoorder;
    private RadioButton optin;
    private RadioButton optout;

    //	private TextView totalamountinv;

//    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_reason);

        try {

            setupToolbar("Send Reason");
            // Force menu to appear
            invalidateOptionsMenu();

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(SendReason.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            optin = findViewById(R.id.rbin);
            optout = findViewById(R.id.rbout);

            btsendsave = findViewById(R.id.btsendreasonnoorder);
            btsendsave.setOnClickListener(this);

            otherreason = findViewById(R.id.etotherreasonnoorder);

            reason = findViewById(R.id.spreasonnoorder);

            Cursor ctemp = db.rawQuery("select count(num) from noOrdercustomerlist", null);
            ctemp.moveToFirst();
            countnoorder =  Integer.parseInt(ctemp.getString(0)) + 1;
            // Toast.makeText(getApplicationContext(), "" + countnoorder, Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(), "Trap", Toast.LENGTH_LONG).show();
            custid = findViewById(R.id.tvcustomeridnoorder);
            custid.setText(getIntent().getExtras().getString("cidinv"));

            custname = findViewById(R.id.tvcustomernamenoorder);
            custname.setText(getIntent().getExtras().getString("cnameinv"));

            Databasename = getIntent().getExtras().getString("Databasename");
            Department  = getIntent().getExtras().getString("Department");

            if("AG".equals(Department)){
            }else if("L".equals(Department)){
            }else {

            }

            // RadioButton rbinpress = (RadioButton) findViewById(R.id.rbin);
            // rbinpress.setOnClickListener(this);
            // RadioButton rboutpress = (RadioButton) findViewById(R.id.rbout);
            // rboutpress.setOnClickListener(this);

        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // getclick = spctype.getSelectedItemPosition();

        String Detailsinvoice = "CUSTNO!";

        checkvalidation = true;

        if(v.getId() == R.id.btsendreasonnoorder){ // save details

            SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String datetimesyntaxval = datetimesyntax.format(new Date());

            String trapOther = "";
            Detailsinvoice = "";

            if("".equals(PressOption)){
                checkvalidation = false;
                Toast.makeText(getApplicationContext(), "Please Select Transaction Type!", Toast.LENGTH_LONG).show();
            }else {

                trapOther = reason.getSelectedItem().toString();

                if("OTHER".equals(trapOther)){
                    trapOther = otherreason.getText().toString();
                    if("".equals(trapOther)){
                        Toast.makeText(getApplicationContext(), "Please Input the Other Reason!", Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }
                }else if("FAILED".equals(trapOther)){
                    trapOther = otherreason.getText().toString();
                    if("".equals(trapOther)){
                        Toast.makeText(getApplicationContext(), "Please Input Failed Reason!", Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }
                }else{
                    // trapOther = "";
                }
            }

            if(checkvalidation){
                try{
                    if("AG".equals(Department)){
                        Detailsinvoice ="CUSTNO!"+ countnoorder + "!" + custid.getText().toString() + "!" + reason.getSelectedItemPosition() + "!" + trapOther + "!" + datetimesyntaxval;

                        db.execSQL("Insert into noOrdercustomerlist values" +
                                "('" + countnoorder + "','" +
                                custid.getText().toString() + "','" +  				// customerid
                                reason.getSelectedItemPosition() +"','"+ trapOther +"','"+ datetimesyntaxval +"');");						// syntax date

                    }else if("L".equals(Department)){
                        Detailsinvoice ="CUSTNO!"+ countnoorder + "!" + custid.getText().toString() + "!" + reason.getSelectedItemPosition() + "!" + trapOther + "!" + datetimesyntaxval;

                        db.execSQL("Insert into noOrdercustomerlist values" +
                                "('" + countnoorder + "','" +
                                custid.getText().toString() + "','" +  				// customerid
                                reason.getSelectedItemPosition() +"','"+ trapOther +"','"+ datetimesyntaxval +"');");						// syntax date
                    }else {

                        trapOther = PressOption + " - " + trapOther;

                        if("MARS2".equals(Databasename)){
                            Detailsinvoice ="CUSTNO2!"+ countnoorder + "!" + custid.getText().toString() + "!4!" + trapOther + "!" + datetimesyntaxval;
                        } else {
                            Detailsinvoice ="CUSTNO!"+ countnoorder + "!" + custid.getText().toString() + "!4!" +  trapOther + "!" + datetimesyntaxval;
                        }

                        db.execSQL("Insert into noOrdercustomerlist values" +
                                "('" + countnoorder + "','" +
                                custid.getText().toString() + "','4','"+ trapOther +"','"+ datetimesyntaxval +"');");						// syntax date
                    }

                    // query receiver number
                    Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                    c1.moveToFirst();
                    int cnt =  c1.getCount();
                    if(cnt > 0){
                        theMobNo = c1.getString(0);
                    }
                    // Toast.makeText(getApplicationContext(), theMobNo + " " + Detailsinvoice, Toast.LENGTH_LONG).show();
                    sendSMS(theMobNo,Detailsinvoice);

                    db.execSQL("Update ScheduleCustomer set status = 'Y' WHERE customerid = '"+ custid.getText().toString() +"';");

                    if("OUT".equalsIgnoreCase(PressOption)){
                        db.execSQL("Delete from CustomerUnlock WHERE customerid = '"+ custid.getText().toString() +"';");
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Get the checked state of the clicked button
        boolean checked = ((RadioButton) view).isChecked();

        // Handle the 'IN' RadioButton click
        if (view.getId() == R.id.rbin && checked) {
            PressOption = "IN";
            // Handle department-specific labels
            updateLabelsForIn();
        }
        // Handle the 'OUT' RadioButton click
        else if (view.getId() == R.id.rbout && checked) {
            PressOption = "OUT";
            // Handle department-specific labels
            updateLabelsForOut();
        }
        // Handle the 'OTHER REASON' RadioButton click
        else if (view.getId() == R.id.rbother && checked) {
            PressOption = "OTHER REASON";
            // Handle department-specific labels
            updateLabelsForOtherReason();
        }
    }

    // Method to handle labels when 'IN' is selected
    private void updateLabelsForIn() {
        if ("AG".equals(Department) || "L".equals(Department)) {
            // Do something for AG or L department (if needed)
        } else {
            lables = new ArrayList<>();
            lables.add("MERCHANDISING");
            lables.add("INVENTORY");
            lables.add("READING BO");
            lables.add("COLLECTION");
            lables.add("COUNTER");
            lables.add("AUDIT");
            lables.add("BOOKING");
            lables.add("DELIVERY");
            lables.add("REPLENISHMENT");
            lables.add("ROUTE VISIT");
            lables.add("PRODUCT RETURNS");
            lables.add("VAN SELLING");
            lables.add("CUSTOMER VISIT");
            lables.add("ORDER TAKING");
            lables.add("PAYMENT FOLLOW-UP");
            lables.add("PRODUCT SAMPLING");
            lables.add("PROMO DEPLOYMENT");
            lables.add("PRICE CHECK");
            lables.add("CHECKLIST MONITORING");
            lables.add("OTHER");

            // Set the adapter for the spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reason.setAdapter(dataAdapter);
        }
    }

    // Method to handle labels when 'OUT' is selected
    private void updateLabelsForOut() {
        if ("AG".equals(Department) || "L".equals(Department)) {
            // Do something for AG or L department (if needed)
        } else {
            lables = new ArrayList<>();
            lables.add("ACCOMPLISHED");
            lables.add("FAILED");
            lables.add("PARTIALLY ACCOMPLISHED");
            lables.add("PENDING");
            lables.add("CANCELLED");
            lables.add("RESCHEDULED");
            lables.add("ON HOLD");
            lables.add("SKIPPED");
            lables.add("IN PROGRESS");
            lables.add("FOR APPROVAL");
            lables.add("APPROVED");
            lables.add("REJECTED");
            lables.add("COMPLETED");
            lables.add("NOT APPLICABLE");
            lables.add("POSTPONED");
            lables.add("OTHER");

            // Set the adapter for the spinner
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reason.setAdapter(dataAdapter1);
        }
    }

    private void updateLabelsForOtherReason() {
        if ("AG".equals(Department) || "L".equals(Department)) {
            // Optional: handle AG or L-specific logic
        } else {
            // Load the reason array from strings.xml
            String[] reasonArray = getResources().getStringArray(R.array.reason);

            // Convert to ArrayList
            lables = new ArrayList<>(Arrays.asList(reasonArray));

            // Set the adapter for the Spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reason.setAdapter(dataAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // Navigate back manually
                finish(); // Closes the current activity
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
