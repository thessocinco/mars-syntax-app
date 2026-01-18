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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

@SuppressWarnings("deprecation")
public class AgrichemNewOtherCustomer extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String theMessage1;
    private String CustID;
    private String validation;
//    private ProgressBar progress;
    private String longitude;
    private String latitude;
//    private String CustomerGroupType;
    private String MapperCode;
    private String selectQuerymunicipality;
    private String selectQueryBarangay;
//    private int getlenthmessage;
    private int countpro;
    private int countmun;
    private int custcount;
//    private int rbuttons1;
//    private int rbuttons2;
//    private int rbuttons3;
    private EditText editRemarks;
    private EditText custname;
    private EditText conperson;
//    private EditText conbday;
    private EditText contellnum;
    private EditText concellnum;
    private EditText street;
    private EditText phonenumber;
    private EditText OtherCustTypeOther;
    private List<String> lables;
    private List<String> lablesb;
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase	db;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private Spinner spctype;
    private Spinner province;
    private Spinner barangay;
    private Spinner spmunicipality;
    private Button buttonsms;
    private Button buttonsms2;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agrichemnewothercustomer);

        try {
            setupToolbar("AG Customer Details");
            // Force menu to appear
            invalidateOptionsMenu();

            longitude = "";
            latitude = "";

            // progress = (ProgressBar) findViewById(R.id.progressBar1);

            // CustomerGroupType = "S1";
            MapperCode = tempstring = (getIntent().getExtras().getString("mapcode").toUpperCase());
            // customer name
            custname = findViewById(R.id.etCustomerNameNewOtherconag);
            // custname.setText(getIntent().getExtras().getString("cname"));

            phonenumber = findViewById(R.id.etReceiverNumberNewOtherconag);

            // CUSTOMER TYPE VALUE
            // tempstring = "" + getIntent().getExtras().getString("ctype").toUpperCase();
            spctype = findViewById(R.id.SpinnerCustomerTypeNewOtherconag);

            // Contact Person
            conperson = findViewById(R.id.etContactPersonNewOtherconag);

            // Contact Person tellnumber
            contellnum = findViewById(R.id.ettellnumconNewOtherconag);
            // Contact Person cellnumber
            concellnum = findViewById(R.id.etCellNumConNewOtherconag);
            // street
            street = findViewById(R.id.etStreetNewOtherconag);

            OtherCustTypeOther = findViewById(R.id.etCustomerOtherTypeconag);

            // load municipality ang barangay

            db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

            barangay = findViewById(R.id.SpinnerBarangayNewOtherconag);
            spmunicipality = findViewById(R.id.SpinnerMunicipalityNewOtherconag);

            lables = new ArrayList<>();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

            // tempstring = (getIntent().getExtras().getString("municipality").toUpperCase());
            // lables.add(tempstring);
            // Drop down layout style - list view with radio button

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            // municipality
            spmunicipality.setAdapter(dataAdapter);

            lablesb = new ArrayList<>();
            tempstring = "";
            lablesb.add(tempstring);

            ArrayAdapter<String> dataAdapterb = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lablesb);

            barangay.setAdapter(dataAdapterb);

            barangay.setSelection(0);
            // spmunicipality.setSelection(0);

            // province spinner
            province = findViewById(R.id.SpinnerProvinceNewOtherconag);

            countpro = 0;
            province.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0) {
                        countpro = 1;
                    } else {

                        tempstring = province.getSelectedItem().toString();
                        selectQuerymunicipality = "Select distinct(Municipality) as municipal from barangay where province like '" + tempstring + "' order by municipality ";
                        loadSpinnerDataMunicipality();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            countmun = 0;
            spmunicipality.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countmun == 0) {
                        countmun = 1;
                    } else {

                        tempstring = spmunicipality.getSelectedItem().toString();
                        selectQueryBarangay = "Select distinct(barangay) as barangay from barangay where municipality like '" + tempstring + "' and province like '%" + province.getSelectedItem().toString() + "%' order by barangay ";
                        loadSpinnerDataBarangay();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            buttonsms = findViewById(R.id.btsendupdatesNewOtherconag);
            buttonsms.setOnClickListener(this);

            buttonsms2 = findViewById(R.id.btsendupdates2NewOtherconag);
            buttonsms2.setEnabled(false);
            buttonsms2.setOnClickListener(this);

            // Toast.makeText(getApplicationContext(), "load", Toast.LENGTH_LONG).show();
            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            Log.e("ONCREATE ERROR", "onCreate: ", e);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        // getclick = spctype.getSelectedItemPosition();
        // Toast.makeText(getApplicationContext(), "load", Toast.LENGTH_LONG).show();
        theMessage = "";
        validation = "";
        // validation = custid.getText().toString();
        // theMessage = validation;

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
        Integer ct = spctype.getSelectedItemPosition();
        if(ct > 6){
            validation = OtherCustTypeOther.getText().toString();
            if("".equals(validation)){
                checkvalidation = false;
                Toast.makeText(getApplicationContext(),	"Please Input Other Customer Type!", Toast.LENGTH_LONG).show();
            }

            theMessage =  custname.getText().toString() + "!" + OtherCustTypeOther.getText().toString() + "!" + conperson.getText().toString() + "!" + contellnum.getText().toString() + "!" + concellnum.getText().toString() ;

        }else{

            theMessage =  custname.getText().toString() + "!" + spctype.getSelectedItem().toString() + "!" + conperson.getText().toString() + "!" + contellnum.getText().toString() + "!" + concellnum.getText().toString()  ;
        }

        validation = street.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Street Address!", Toast.LENGTH_LONG).show();
        }

        validation = "" + barangay.getSelectedItem().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Select Barangay!", Toast.LENGTH_LONG).show();
        }

        // syntax CMAP5!CUSTID!STREET!BARANGAY!MUNICIPALITY!PROVINCE
        if(v.getId() == R.id.btsendupdates2NewOtherconag){
            theMessage1 = "AMAP4!" + CustID +"!" + street.getText().toString() + "!" + barangay.getSelectedItem().toString() +"!" + spmunicipality.getSelectedItem().toString() + "!" + province.getSelectedItemPosition();
            theMobNo = phonenumber.getText().toString();
            Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
            c1.moveToFirst();
            int cnt =  c1.getCount();
            if(cnt > 0){
                theMobNo = c1.getString(0);
            }
            sendSMS(theMobNo, theMessage1);
            // Toast.makeText(getApplicationContext(), "" + theMessage1, Toast.LENGTH_LONG).show();
        }

        if(v.getId() == R.id.btsendupdatesNewOtherconag){
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
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {

                locManager.removeUpdates(locListener);

                longitude = ""+ location.getLongitude();
                latitude =  ""+ location.getLatitude();
//                String altitiude =  ""+  location.getAltitude();
//                String accuracy = ""+ location.getAccuracy();
//                String time =  ""+ location.getTime();
                String idMap = MapperCode;
                // Toast.makeText(getApplicationContext(),	"" + longitude + accuracy, Toast.LENGTH_LONG).show();
                SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                Cursor c = db.rawQuery("select * from otherCUSTOMERs where cid like '"+ idMap +"%'", null);
                c.moveToFirst();
                if(c.getCount() > 0){
                    custcount = c.getCount() + 1;
                }else{
                    custcount = 1;
                }

                theMobNo = phonenumber.getText().toString();
                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                int cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }

                CustID = idMap + custcount;
                theMessage = "AMAP3!" + CustID + "!" + theMessage + "!" + latitude + "!" + longitude  ;
                // Toast.makeText(getApplicationContext(), "Sent" + theMessage , Toast.LENGTH_LONG).show();
                sendSMS(theMobNo, theMessage);
                // Toast.makeText(getApplicationContext(), "Sent" + theMessage , Toast.LENGTH_LONG).show();
                // String getcustid = custname.getText().toString();
                UpdateDatabase(latitude,longitude);
                buttonsms.setEnabled(false);
                buttonsms2.setEnabled(true);
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

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later! ERROR: " + e, Toast.LENGTH_LONG).show();
            Log.e("ERROR sendSMS", "SMS failed, please try again later! ERROR: ", e);
        }
    }

    private void UpdateDatabase(String LatMap, String LongMap) {
        // TODO Auto-generated method stub

        SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from otherCUSTOMERs where latitude = '"+ LatMap +"' and longitude = '"+ LongMap +"'", null);
        c.moveToFirst();
        if(c.getCount() > 0){
            Toast.makeText(getApplicationContext(), "Customer location is already Exist, Update Not Successfull!",Toast.LENGTH_LONG).show();

        }else{

            db.execSQL("INSERT INTO otherCustomers(CID,CNAME,CTYPE,CPERSON" +
                    ",CTELLNUM,CCELLNUM,STREET" +
                    ",BARANGAY,MUNICIPALITY,PROVINCE,Latitude,Longitude,Grouptype) values('"+ CustID +"','"+ custname.getText().toString() + "'" +
                    ",'"+ spctype.getSelectedItemPosition() +"','"+ conperson.getText().toString() +"','"+ contellnum.getText().toString() +"'"+
                    ",'"+ concellnum.getText().toString() +
                    "','" + street.getText().toString() +"','"+ barangay.getSelectedItem().toString() +"','"+ spmunicipality.getSelectedItem().toString() +"'" +
                    ",'"+ province.getSelectedItemPosition() +"','"+ latitude +"','"+ longitude +"', '');");

            Toast.makeText(getApplicationContext(), "Customer Others Was Successfull Save!",Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerDataMunicipality(){
        lables = new ArrayList<>();

        // Toast.makeText(getApplicationContext(), "load municipality",Toast.LENGTH_LONG).show();
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
