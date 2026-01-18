package com.marsIT.marsSyntaxApp.CustomerData;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

@SuppressWarnings("deprecation")
public class OtherCustomer extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String theMessage1;
//    private String theMessage2;
    private String validation;
//    private ProgressBar progress;
    private String longitude;
    private String latitude;
    private String selectQuerymunicipality;
    private String selectQueryBarangay;
//    private String provincenum;
    private int getlenthmessage;
    private int countpro;
    private int countmun;
    private EditText editRemarks;
    private TextView custid;
    private EditText custname;
    private EditText conperson;
//    private EditText conbday;
    private EditText contellnum;
    private EditText concellnum;
//    private EditText ownperson;
//    private EditText ownbday;
//    private EditText owntellnum;
//    private EditText owncellnum;
    private EditText street;
//    private EditText subpocket;
//    private EditText others;
    private EditText phonenumber;
    private List<String> lables;
    private List<String> lablesb;
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase	db;
    private TextView tvOlat;
    private TextView tvOlong;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private Spinner spctype;
    private Spinner province;
    private Spinner barangay;
    private Spinner spmunicipality;
    private Button buttonsms;
    private Button buttonsms2;
    private Button buttonsms3;
    private Button buttonsms4;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.othercustomer);

        try {

            setupToolbar("Other Customer");
            // Force menu to appear
            invalidateOptionsMenu();

            longitude = "";
            latitude = "";

            // customer id
            custid = findViewById(R.id.etCustomerIDOtherDtl);
            custid.setText(getIntent().getExtras().getString("cid"));
            // customer name
            custname = findViewById(R.id.etCustomerNameOtherDtl);
            custname.setText(getIntent().getExtras().getString("cname"));

            phonenumber = findViewById(R.id.etReceiverNumberOtherdtl);

            // CUSTOMER TYPE VALUE
            tempstring = "" + getIntent().getExtras().getString("ctype").toUpperCase();
            spctype = findViewById(R.id.SpinnerCustomerTypeOtherDtl);

            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if("HARDWARE".equals(tempstring)){
                spctype.setSelection(0);
            }if("MOTORPARTS".equals(tempstring)){
                spctype.setSelection(1);
            }
            if("AUTOPARTS".equals(tempstring)){
                spctype.setSelection(2);
            }
            if("BIKES SHOP".equals(tempstring)){
                spctype.setSelection(3);
            }
            if("SHELL STATION".equals(tempstring)){
                spctype.setSelection(4);
            }
            if("CALTEX STATION".equals(tempstring)){
                spctype.setSelection(5);
            }
            if("PETRON STATION".equals(tempstring)){
                spctype.setSelection(6);
            }
            if("PHOENIX STATION".equals(tempstring)){
                spctype.setSelection(7);
            }
            if("OTHER STATION".equals(tempstring)){
                spctype.setSelection(8);
            }
            if("END-USER".equals(tempstring)){
                spctype.setSelection(9);
            }

            // Customer Account Type
            // Contact Person
            conperson = findViewById(R.id.etContactPersonOtherDtl);
            conperson.setText(getIntent().getExtras().getString("cperson"));

            // Contact Person tellnumber
            contellnum = findViewById(R.id.ettellnumconOtherDtl);
            contellnum.setText(getIntent().getExtras().getString("ctellnum"));

            // Contact Person cellnumber
            concellnum = findViewById(R.id.etCellNumConOtherDtl);
            concellnum.setText(getIntent().getExtras().getString("ccellnum"));

            // street
            street = findViewById(R.id.etStreetOtherDtl);
            street.setText(getIntent().getExtras().getString("street"));

            // load municipality ang barangay

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            barangay = findViewById(R.id.SpinnerBarangayOtherDtl);
            spmunicipality = findViewById(R.id.SpinnerMunicipalityOtherDtl);

            lables = new ArrayList<>();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

            tempstring = (getIntent().getExtras().getString("municipality").toUpperCase());
            lables.add(tempstring);
            // Drop down layout style - list view with radio button

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            // municipality
            spmunicipality.setAdapter(dataAdapter);

            lablesb = new ArrayList<>();
            tempstring = (getIntent().getExtras().getString("barangay").toUpperCase());
            lablesb.add(tempstring);

            ArrayAdapter<String> dataAdapterb = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lablesb);

            barangay.setAdapter(dataAdapterb);

            barangay.setSelection(0);
            spmunicipality.setSelection(0);

            // province spinner
            province = findViewById(R.id.SpinnerProvinceOtherdtl);

            tempstring = (getIntent().getExtras().getString("province").toUpperCase());
            switch (tempstring) {
                case "0":
                    province.setSelection(0);
                    break;
                case "1":
                    province.setSelection(1);
                    break;
                case "2":
                    province.setSelection(2);
                    break;
                case "3":
                    province.setSelection(3);
                    break;
                case "4":
                    province.setSelection(4);
                    break;
                case "5":
                    province.setSelection(5);
                    break;
                case "6":
                    province.setSelection(6);
                    break;
                case "7":
                    province.setSelection(7);
                    break;
                case "8":
                    province.setSelection(8);
                    break;
                case "9":
                    province.setSelection(9);
                    break;
                case "10":
                    province.setSelection(10);
                    break;
                default:
                    province.setSelection(0);
                    break;
            }

            countpro = 0;
            province.setOnItemSelectedListener(new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = province.getSelectedItem().toString();
                        selectQuerymunicipality = "Select distinct(Municipality) as municipal from barangay where province like '"+ tempstring +"' ";
                        loadSpinnerDataMunicipality();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            countmun = 0;
            spmunicipality.setOnItemSelectedListener(new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countmun == 0){
                        countmun = 1;
                    }else{

                        tempstring = spmunicipality.getSelectedItem().toString();
                        selectQueryBarangay = "Select distinct(barangay) as barangay from barangay where municipality like '"+ tempstring +"' and province like '%"+ province.getSelectedItem().toString() +"%' ";
                        loadSpinnerDataBarangay();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            // latitude
            tvOlat = findViewById(R.id.tvlatOtherdtl);
            tvOlat.setText(getIntent().getExtras().getString("lati"));
            // longitude
            tvOlong = findViewById(R.id.tvlongOtherDtl);
            tvOlong.setText(getIntent().getExtras().getString("longi"));
            // progress.setVisibility(View.GONE);

            buttonsms = findViewById(R.id.btsendupdatelocation);
            buttonsms.setOnClickListener(this);

            buttonsms2 = findViewById(R.id.btsendaddressOther);
            buttonsms2.setOnClickListener(this);

            buttonsms3 = findViewById(R.id.btsavecurrentdtl);
            buttonsms3.setOnClickListener(this);

            buttonsms4 = findViewById(R.id.btsendcurrentotherdtl);
            buttonsms4.setOnClickListener(this);

            // spctype.setOnClickListener(this);
            String comp = "" + tvOlat.getText().toString();
            if("".equals(comp)){
                buttonsms4.setEnabled(false);
            }else{
                buttonsms4.setEnabled(true);
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),	"" + e, Toast.LENGTH_LONG).show();
        }
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        // getclick = spctype.getSelectedItemPosition();
        theMessage = "";

        validation = "";
        // validation = custid.getText().toString();
        theMessage = validation;

        checkvalidation = true;
        validation = "" + custname.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Customer Name!", Toast.LENGTH_LONG).show();
        }else{
            validation = "" + conperson.getText().toString();
            if("".equals(validation)){
                checkvalidation = false;
                Toast.makeText(getApplicationContext(),	"Please Input Contact Person!", Toast.LENGTH_LONG).show();
            }else{

                validation = "" + contellnum.getText().toString();
                if("".equals(validation)){
                    checkvalidation = false;
                    Toast.makeText(getApplicationContext(),	"Please Input Contact Person Telephone Number!", Toast.LENGTH_LONG).show();
                }else{
                    validation = "" + concellnum.getText().toString();
                    if("".equals(validation)){
                        checkvalidation = false;
                        Toast.makeText(getApplicationContext(),	"Please Input Contact Person CellPhone Number!", Toast.LENGTH_LONG).show();
                    }else{

                    }
                }
            }
        }

        // syntax CMAP4!CUSTID!CUSTOMER NAME!CUST TYPE!CON PERSON!CON TELL!CON CELL!LAT!LONG
        theMessage = "CMAP4!" + custid.getText().toString() + "!" + custname.getText().toString() + "!" + spctype.getSelectedItemPosition() + "!" + conperson.getText().toString() + "!" + contellnum.getText().toString() + "!" + concellnum.getText().toString() ;

        validation = street.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Street Address!", Toast.LENGTH_LONG).show();
        }

        validation = "" + barangay.getSelectedItem().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Select Barangay!", Toast.LENGTH_LONG).show();
        }

        // syntax CMAP5!CUSTID!STREET!BARANGAY!MUNICIPALITY!PROVINCE

        if(v.getId() == R.id.btsendaddressOther){
            theMessage1 = "CMAP5!" + custid.getText().toString() +"!" + street.getText().toString() + "!" + barangay.getSelectedItem().toString() +"!" + spmunicipality.getSelectedItem().toString() + "!" + province.getSelectedItemPosition();

            Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
            c12.moveToFirst();
            int cnt =  c12.getCount();
            if(cnt > 0){
                theMobNo = c12.getString(0);
            }
            // theMobNo = phonenumber.getText().toString();
            // Toast.makeText(getApplicationContext(), "" + theMessage1, Toast.LENGTH_LONG).show();
            sendSMS(theMobNo, theMessage1);
        }

        if(v.getId() == R.id.btsendupdatelocation){
            if (checkvalidation)	{

                // progress.setVisibility(View.VISIBLE);

                // exceptions will be thrown if provider is not permitted.
                try {
                    gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                // don't start listeners if no provider is enabled
                if (!gps_enabled && !network_enabled) {
                    AlertDialog.Builder builder = new Builder(this);
                    builder.setTitle("Attention!");
                    builder.setMessage("Sorry, location is not determined. Please enable location providers");
                    builder.setPositiveButton("OK", this);
                    builder.setNeutralButton("Cancel", this);
                    builder.create().show();
                    // progress.setVisibility(View.GONE);
                }

                if (gps_enabled) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                }
                if (network_enabled) {
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                }
            }
        }

        if(v.getId() == R.id.btsavecurrentOtherdtl){
            if (checkvalidation)	{

                String getcustid = custid.getText().toString();
                SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                Cursor c = db.rawQuery("select * from otherCUSTOMERS where cid = '"+ getcustid +"'", null);
                c.moveToFirst();
                if(c.getCount() > 0){

                    db.execSQL("Update otherCustomers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"'" +
                            ", cperson = '"+ conperson.getText().toString() + "'" +
                            ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"'" +
                            ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                            ", province = '"+ province.getSelectedItemPosition() +"'" +
                            " where cid = '"+ getcustid +"';");

                    Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();

                    db.close();
                }
            }
        }

        if(v.getId() == R.id.btsendcurrentotherdtl){
            getlenthmessage = (theMessage.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Message is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){
                try{
                    SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                    Cursor c = db.rawQuery("select * from otherCUSTOMERS where cid = '"+ custid.getText().toString() +"'", null);
                    c.moveToFirst();
                    if(c.getCount() > 0){

                        latitude =  "" + c.getString(10);
                        longitude =  "" + c.getString(11);
                        theMobNo = phonenumber.getText().toString();
                        theMessage = theMessage + "!" + latitude + "!" + longitude;

                        Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                        c12.moveToFirst();
                        int cnt =  c12.getCount();
                        if(cnt > 0){
                            theMobNo = c12.getString(0);
                        }

                        sendSMS(theMobNo, theMessage);
                        buttonsms2.setEnabled(true);

                    }
                }	catch(Exception e){
                    Toast.makeText(getApplicationContext(),	"" + e + " " + theMobNo + longitude, Toast.LENGTH_LONG).show();
                }
            }
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

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                locManager.removeUpdates(locListener);

                longitude = ""+ location.getLongitude();
                latitude =  ""+ location.getLatitude();
//                String altitiude =  ""+  location.getAltitude();
//                String accuracy = ""+ location.getAccuracy();
//                String time =  ""+ location.getTime();
                // Toast.makeText(getApplicationContext(), "" + longitude + accuracy, Toast.LENGTH_LONG).show();

                theMobNo = phonenumber.getText().toString();
                theMessage = theMessage + "!" + latitude + "!" + longitude;
                Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c12.moveToFirst();
                int cnt =  c12.getCount();
                if(cnt > 0){
                    theMobNo = c12.getString(0);
                }
                sendSMS(theMobNo, theMessage);

                // progress.setVisibility(View.GONE);
            }else{
                Toast.makeText(getApplicationContext(), "GPS Not Enable or Cant Get Location!", Toast.LENGTH_LONG).show();
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
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
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
            Toast.makeText(getApplicationContext(), "Sending Customer Details, Please Wait!", Toast.LENGTH_LONG).show();

            String getcustid = custid.getText().toString();
            UpdateDatabase(getcustid);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later! ERROR: " + e, Toast.LENGTH_LONG).show();
            Log.e("ERROR sendSMS", "SMS failed, please try again later! ERROR: ", e);
        }
    }

    private void UpdateDatabase(String getcustid) {
        // TODO Auto-generated method stub

        SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from OTHERCUSTOMERS where cid = '"+ getcustid +"'", null);
        c.moveToFirst();
        if(c.getCount() > 0){

            db.execSQL("Update OTHERCustomers set cname = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"'" +
                    ",  cperson = '"+ conperson.getText().toString() + "'" +
                    ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() + "'" +
                    ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                    ", province = '"+ province.getSelectedItemPosition() + "'" +
                    ",latitude = '"+ latitude +"',longitude = '"+ longitude +"' where cid = '"+ getcustid +"';");

            Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();

            buttonsms2.setEnabled(true);
            db.close();

        }else{
            Toast.makeText(getApplicationContext(), "Customer Not Found, Update Not Successfull!",Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerDataMunicipality(){
        lables = new ArrayList<>();

        Toast.makeText(getApplicationContext(), "load municipality",Toast.LENGTH_LONG).show();
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuerymunicipality, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lables.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // lables.add("ALL");
        // closing connection
        cursor.close();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spmunicipality.setAdapter(dataAdapter);
    }

    private void loadSpinnerDataBarangay(){
        lables = new ArrayList<>();

        // Toast.makeText(getApplicationContext(), "load municipality",Toast.LENGTH_LONG).show();
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQueryBarangay, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lables.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // lables.add("ALL");
        // closing connection
        cursor.close();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        barangay.setAdapter(dataAdapter);
    }
}
