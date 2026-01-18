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
public class AgrichemCustomerDetails extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
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
    private EditText conbday;
    private EditText contellnum;
    private EditText concellnum;
    private EditText ownperson;
    private EditText ownbday;
    private EditText owntellnum;
    private EditText owncellnum;
    private EditText street;
    private EditText OtherCustType1;
//    private EditText subpocket;
    private List<String> lables;
    private List<String> lablesb;
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase db;
    private TextView tvOlat;
    private TextView tvOlong;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
//    private CheckBox caltex;
//    private CheckBox mobil;
//    private CheckBox castrol;
//    private CheckBox petron;
//    private CheckBox caf;
//    private CheckBox dti;
//    private CheckBox bpermit;
//    private CheckBox signage;
//    private CheckBox poster;
//    private CheckBox streamer;
//    private CheckBox sticker;
//    private CheckBox disrack;
//    private CheckBox prodisplay;
    private Spinner spctype;
    private Spinner spatype;
    private Spinner spterms;
    private Spinner province;
    private Spinner barangay;
    private Spinner spmunicipality;
    private Button buttonsms;
    private Button buttonsms2;
//    private Button buttonsms3;
    private Button buttonsms4;
//    private Button buttonGenM;
    private Button buttonsavecurrentdtl;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agrichemcustomerdetails);

        try {

            setupToolbar("AG Customer Details");
            // Force menu to appear
            invalidateOptionsMenu();

            longitude = "";
            latitude = "";

            // progress = (ProgressBar) findViewById(R.id.progressBar1);
            buttonsms2 = findViewById(R.id.btsendupdates2conag);
            buttonsms2.setEnabled(false);

            buttonsms4 = findViewById(R.id.btsendupdates4conag);

            // customer id
            custid = findViewById(R.id.etCustomerIDconag);
            custid.setText(getIntent().getExtras().getString("cid"));
            // customer name
            custname = findViewById(R.id.etCustomerNameconag);
            custname.setText(getIntent().getExtras().getString("cname"));
            custname.setEnabled(false);

            // CUSTOMER TYPE VALUE
            tempstring = "" + getIntent().getExtras().getString("ctype").toUpperCase();
            spctype = findViewById(R.id.SpinnerCustomerTypeconag);

            OtherCustType1 = findViewById(R.id.etOtherCustType1conag);

            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if("IBG-INDIVIDUAL BANANA GROWERS".equals(tempstring)){
                spctype.setSelection(0);
            }else{
                if("COOPERATIVE GROWERS".equals(tempstring)){
                    spctype.setSelection(1);
                }else{
                    if("PLANTATION".equals(tempstring)){
                        spctype.setSelection(2);
                    }else{
                        if("AGRI SUPPLY".equals(tempstring)){
                            spctype.setSelection(3);
                        }else{
                            if("BUY AND SELL".equals(tempstring)){
                                spctype.setSelection(4);
                            }else{
                                if("FINANCIER".equals(tempstring)){
                                    spctype.setSelection(5);
                                }else{
                                    if("BIG LAND OWNER".equals(tempstring)){
                                        spctype.setSelection(6);
                                    }else{
                                        spctype.setSelection(7);
                                        OtherCustType1.setText(getIntent().getExtras().getString("ctype").toUpperCase());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Customer Account Type
            tempstring = "" + getIntent().getExtras().getString("atype").toUpperCase();
            spatype = findViewById(R.id.SpinnerAccountTypeconag);
            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if("PDC".equals(tempstring)){
                spatype.setSelection(0);
            }else if("CASH ON DUE".equals(tempstring)){
                spatype.setSelection(1);
            }else if("CASH/COD".equals(tempstring)){
                spatype.setSelection(2);
            }else{
                spatype.setSelection(2);
            }
            spatype.setEnabled(false);

            // CUSTOMER TERMS
            tempstring = "" + getIntent().getExtras().getString("terms").toUpperCase();
            spterms = findViewById(R.id.SpinnerCustomerTermconag);
            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if("A30".equals(tempstring)){
                spterms.setSelection(0);
            }else if("B30".equals(tempstring)){
                spterms.setSelection(1);
            }else if("C30".equals(tempstring)){
                spterms.setSelection(2);
            }else if("D30".equals(tempstring)){
                spterms.setSelection(3);
            }else if("E30".equals(tempstring)){
                spterms.setSelection(4);
            }else if("F30".equals(tempstring)){
                spterms.setSelection(5);
            }else if("15".equals(tempstring)){
                spterms.setSelection(6);
            }else if("7".equals(tempstring)){
                spterms.setSelection(7);
            }else{
                spterms.setSelection(8);
            }
            spterms.setEnabled(false);

            // Contact Person
            conperson = findViewById(R.id.etContactPersonconag);
            conperson.setText(getIntent().getExtras().getString("cperson"));

            // Contact Person bday
            conbday = findViewById(R.id.etBdaycontactconag);
            conbday.setText(getIntent().getExtras().getString("cbday"));

            // Contact Person tellnumber
            contellnum = findViewById(R.id.ettellnumconconag);
            contellnum.setText(getIntent().getExtras().getString("ctellnum"));

            // Contact Person cellnumber
            concellnum = findViewById(R.id.etCellNumConconag);
            concellnum.setText(getIntent().getExtras().getString("ccellnum"));

            // owner Person
            ownperson = findViewById(R.id.etOwnerNameconag);
            ownperson.setText(getIntent().getExtras().getString("owner"));

            // owner Person bday
            ownbday = findViewById(R.id.etbdayownerconag);
            ownbday.setText(getIntent().getExtras().getString("obday"));

            // owner Person tellnumber
            owntellnum = findViewById(R.id.etTellnumOwnerconag);
            owntellnum.setText(getIntent().getExtras().getString("otellnum"));

            // owner Person cellnumber
            owncellnum = findViewById(R.id.etCellNumOwnconag);
            owncellnum.setText(getIntent().getExtras().getString("ocellnum"));

            // street
            street = findViewById(R.id.etStreetconag);
            street.setText(getIntent().getExtras().getString("street"));

            // load municipality ang barangay

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            barangay = findViewById(R.id.SpinnerBarangayconag);
            spmunicipality = findViewById(R.id.SpinnerMunicipalityconag);

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

            Cursor c11 = db.rawQuery("Select * from RECEIVERNUMBER", null);
            c11.moveToFirst();
            int cnt1 =  c11.getCount();
            if(cnt1 > 0) {
                theMobNo = c11.getString(0);
            }

            // province spinner
            province = findViewById(R.id.SpinnerProvinceconag);

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
                case "11":
                    province.setSelection(11);
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
                    } else {

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
                    } else {

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
            tvOlat = findViewById(R.id.tvolatconag);
            tvOlat.setText(getIntent().getExtras().getString("lati"));
            // longitude
            tvOlong = findViewById(R.id.tvolongconag);
            tvOlong.setText(getIntent().getExtras().getString("longi"));
            // progress.setVisibility(View.GONE);

            buttonsms = findViewById(R.id.btsendupdatesconag);
            buttonsms.setOnClickListener(this);

            buttonsms2 = findViewById(R.id.btsendupdates2conag);
            buttonsms2.setOnClickListener(this);

            buttonsms4 = findViewById(R.id.btsendupdates4conag);
            buttonsms4.setOnClickListener(this);

            buttonsavecurrentdtl = findViewById(R.id.btsavecurrentdtlconag);
            buttonsavecurrentdtl.setOnClickListener(this);

            tempstring = (getIntent().getExtras().getString("lati"));
            if("".equals(tempstring)){
                buttonsms4.setEnabled(false);
            } else {
                buttonsms4.setEnabled(true);
            }

            // spctype.setOnClickListener(this);
            String comp = tvOlat.getText().toString();
            if("0".equals(comp)) {
                buttonsms4.setEnabled(false);
            } else {
                buttonsms4.setEnabled(true);
            }

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            Log.e("ONCREATE ERROR", "onCreate: ", e);
        }
    }

    @Override
    public void onClick(View v) {

        theMessage = "";
        validation = "";
        validation = custid.getText().toString();
        theMessage = validation;

        checkvalidation = true;

        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Customer Code!", Toast.LENGTH_LONG).show();
        }else{
            validation = custname.getText().toString();
            theMessage = theMessage + "!"; // add string for syntax code & name"
            if("".equals(validation)){
                checkvalidation = false;
                Toast.makeText(getApplicationContext(),	"Please Input Customer Name!", Toast.LENGTH_LONG).show();
            }else {
                validation = conperson.getText().toString();

                Integer ct = spctype.getSelectedItemPosition();
                if(ct > 6){
                    validation = OtherCustType1.getText().toString();
                    if("".equals(validation)){
                        checkvalidation = false;
                        Toast.makeText(getApplicationContext(),	"Please Input Other Customer Type!", Toast.LENGTH_LONG).show();
                    }
                    theMessage = theMessage + "!" + OtherCustType1.getText().toString() + "!" + spatype.getSelectedItemPosition() + "!" + spterms.getSelectedItem().toString();

                }else{
                    theMessage = theMessage + "!" + spctype.getSelectedItemPosition() + "!" + spatype.getSelectedItemPosition() + "!" + spterms.getSelectedItem().toString();
                }

                if("".equals(validation)){
                    checkvalidation = false;
                    Toast.makeText(getApplicationContext(),	"Please Input Contact Person!", Toast.LENGTH_LONG).show();
                }else{
                    validation = ownperson.getText().toString();
                    if("".equals(validation)){
                        checkvalidation = false;
                        Toast.makeText(getApplicationContext(),	"Please Input Owner Name!", Toast.LENGTH_LONG).show();
                    }else{
                        validation = contellnum.getText().toString();
                        if("".equals(validation)){
                            checkvalidation = false;
                            Toast.makeText(getApplicationContext(),	"Please Input Contact Person Tell Number!", Toast.LENGTH_LONG).show();
                        }else{
                            validation = owntellnum.getText().toString();
                            if("".equals(validation)){
                                checkvalidation = false;
                                Toast.makeText(getApplicationContext(),	"Please Input Owner Tell Number!", Toast.LENGTH_LONG).show();
                            }else{
                                validation = owncellnum.getText().toString();
                                if("".equals(validation)){
                                    checkvalidation = false;
                                    Toast.makeText(getApplicationContext(),	"Please Input Owner Cell Number!", Toast.LENGTH_LONG).show();
                                }else{
                                    validation = concellnum.getText().toString();
                                    if("".equals(validation)){
                                        checkvalidation = false;
                                        Toast.makeText(getApplicationContext(),	"Please Input Contact Cell Number!", Toast.LENGTH_LONG).show();
                                    }else{
                                        validation = conbday.getText().toString();
                                        if("".equals(validation)){
                                            checkvalidation = false;
                                            Toast.makeText(getApplicationContext(),	"Please Input Contact person Birth Day!", Toast.LENGTH_LONG).show();
                                        }else{
                                            validation = ownbday.getText().toString();
                                            if("".equals(validation)){
                                                checkvalidation = false;
                                                Toast.makeText(getApplicationContext(),	"Please Input Owner Birth Day!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // check barangay is inputed
        String barangayCode = "";
        db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select * from barangay where province like '"+  province.getSelectedItem().toString() +"%' and municipality like '"+ spmunicipality.getSelectedItem().toString() +"%' and barangay like '"+ barangay.getSelectedItem().toString() +"%'", null);
        c1.moveToFirst();
        int cnt =  c1.getCount();
        if(cnt > 0){
            barangayCode = c1.getString(0);
            // Toast.makeText(getApplicationContext(),  c1.getString(1) + barangayCode + "", Toast.LENGTH_LONG).show();
            // checkvalidation = false;
        }else{
            Toast.makeText(getApplicationContext(), "Barangay Not Found!", Toast.LENGTH_LONG).show();
            checkvalidation = false;
        }

        theMessage = "AMAP1!" + theMessage ;

        validation = street.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Street Address!", Toast.LENGTH_LONG).show();
        }

        theMessage1 = "AMAP2!" + custid.getText().toString() + "!" + conperson.getText().toString() + "!" + conbday.getText().toString() + "!" + contellnum.getText().toString() + "!"	+ concellnum.getText().toString()	+ "!" + ownperson.getText().toString() + "!" + ownbday.getText().toString() + "!" + owntellnum.getText().toString() + "!" + owncellnum.getText().toString() + "!" + barangayCode + "!" + validation;

        getlenthmessage = (theMessage1.length());

        if(getlenthmessage >= 160){
            Toast.makeText(getApplicationContext(),	"Other Details is to long!", Toast.LENGTH_LONG).show();
            checkvalidation = false;
        }

        getlenthmessage = (theMessage.length());
        if(getlenthmessage >= 160){
            Toast.makeText(getApplicationContext(),	"Message is to long! lenght : " + getlenthmessage, Toast.LENGTH_LONG).show();
            checkvalidation = false;
        }

        if(v.getId() == R.id.btsavecurrentdtlconag){
            if (checkvalidation){

                String getcustid = custid.getText().toString();
                SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                Cursor c = db.rawQuery("select * from CUSTOMERS where cid = '"+ getcustid +"'", null);
                c.moveToFirst();
                if(c.getCount() > 0){
                    Integer ctype = spctype.getSelectedItemPosition();
                    if(ctype > 6){
                        db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ OtherCustType1.getText().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                                ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                                ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                                ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                                ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                                ", province = '"+ province.getSelectedItemPosition() +"'" +
                                " where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                                ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                                ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                                ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                                ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                                ", province = '"+ province.getSelectedItemPosition() +"'" +
                                " where cid = '"+ getcustid +"';");
                    }

                    Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        }

        if(v.getId() == R.id.btsendupdatesconag){
            if (checkvalidation){

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

        if(v.getId() == R.id.btsendupdates2conag){
            getlenthmessage = (theMessage1.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Other Details is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }
            if(checkvalidation){
                sendSMS(theMobNo, theMessage1);
                // Toast.makeText(getApplicationContext(),   "sEND ADDRESS", Toast.LENGTH_LONG).show();
                // buttonsms3.setEnabled(true);
            }
        }

        if(v.getId() == R.id.btsendupdates4conag){
            getlenthmessage = (theMessage.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Message is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){
                try{
                    SQLiteDatabase	db1 = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                    Cursor c123 = db1.rawQuery("select * from CUSTOMERS where cid = '"+ custid.getText().toString() +"'", null);
                    c123.moveToFirst();
                    if(c123.getCount() > 0){

                        latitude =  "" + c123.getString(32);
                        longitude =  "" + c123.getString(33);

                        theMessage = theMessage + "!" + latitude + "!" + longitude;

                        sendSMS(theMobNo, theMessage);
                        buttonsms2.setEnabled(true);
                        // buttonsms3.setEnabled(true);
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
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {

                locManager.removeUpdates(locListener);

                longitude = ""+ location.getLongitude();
                latitude =  ""+ location.getLatitude();
//                String altitiude =  ""+  location.getAltitude();
//                String accuracy = ""+ location.getAccuracy();
//                String time =  ""+ location.getTime();
                // Toast.makeText(getApplicationContext(),	"" + longitude + accuracy, Toast.LENGTH_LONG).show();

                theMessage = theMessage + "!" + latitude + "!" + longitude;

                sendSMS(theMobNo, theMessage);
                buttonsms.setEnabled(false);
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
        Cursor c = db.rawQuery("select * from CUSTOMERS where cid = '"+ getcustid +"'", null);
        c.moveToFirst();
        if(c.getCount() > 0){
            Integer ctype = spctype.getSelectedItemPosition();
            if(ctype > 15){
                db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ OtherCustType1.getText().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                        ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                        ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                        ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                        ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                        ", province = '"+ province.getSelectedItemPosition() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                        ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                        ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                        ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                        ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                        ", province = '"+ province.getSelectedItemPosition() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' where cid = '"+ getcustid +"';");
            }

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
