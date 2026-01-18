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
public class ConsumerCustomerDetails extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
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
    private String DatabaseName;
    private String selectQuerymunicipality;
    private String selectQueryBarangay;
//    private String provincenum;
    private String DisableToSendUpdate;
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
    private EditText latlong;
    private EditText etprogrammerpass;
    private EditText OtherCustType1;
//    private EditText subpocket;
    private List<String> lables;
    private List<String> lablesb;
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase	db;
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
    private Spinner routecode;
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
        setContentView(R.layout.consumercustomerdetails);

        try {

            setupToolbar("Consumer Customer Details");
            // Force menu to appear
            invalidateOptionsMenu();

            longitude = "";
            latitude = "";

            // progress = (ProgressBar) findViewById(R.id.progressBar1);
            buttonsms2 = findViewById(R.id.btsendupdates2con);
            buttonsms2.setEnabled(false);

            buttonsms4 = findViewById(R.id.btsendupdates4con);

            // customer id
            custid = findViewById(R.id.etCustomerIDcon);
            custid.setText(getIntent().getExtras().getString("cid"));
            // customer name
            custname = findViewById(R.id.etCustomerNamecon);
            custname.setText(getIntent().getExtras().getString("cname"));
            custname.setEnabled(false);

            // CUSTOMER TYPE VALUE
            tempstring = "" + getIntent().getExtras().getString("ctype").toUpperCase();
            spctype = findViewById(R.id.SpinnerCustomerTypecon);

            OtherCustType1 = findViewById(R.id.etOtherCustType1con);

            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if("AT WORK CANTEEN".equals(tempstring)){
                spctype.setSelection(0);
            }else{
                if("BAKESHOP".equals(tempstring)){
                    spctype.setSelection(1);
                }else{

                    if("CASUAL DINING".equals(tempstring)){
                        spctype.setSelection(2);
                    }else{

                        if("CATERER, ETC.".equals(tempstring)){
                            spctype.setSelection(3);
                        }else{

                            if("CONVENIENCE STORE/ MINIMART".equals(tempstring)){
                                spctype.setSelection(4);
                            }else{

                                if("DRUGSTORE".equals(tempstring)){
                                    spctype.setSelection(5);
                                }else{
                                    if("EATERY / CARINDERIA".equals(tempstring)){
                                        spctype.setSelection(6);
                                    }else{
                                        if("FOOD STALL AND KIOSK".equals(tempstring)){
                                            spctype.setSelection(7);
                                        }else{
                                            if("GROCERIES".equals(tempstring)){
                                                spctype.setSelection(8);
                                            }else{
                                                if("HYPERMARKET".equals(tempstring)){
                                                    spctype.setSelection(9);
                                                }else{
                                                    if("INDIVIDUAL".equals(tempstring)){
                                                        spctype.setSelection(10);
                                                    }else{

                                                        if("INTERNET CAFE".equals(tempstring)){
                                                            spctype.setSelection(11);
                                                        }else{

                                                            if("KEY SUPERMARKET".equals(tempstring)){
                                                                spctype.setSelection(12);
                                                            }else{

                                                                if("MARKET STALL RETAIL".equals(tempstring)){
                                                                    spctype.setSelection(13);
                                                                }else{

                                                                    if("MARKET STALL WHOLESALES".equals(tempstring)){
                                                                        spctype.setSelection(14);
                                                                    }else{
                                                                        if("PUBS, BARS, CAFE".equals(tempstring)){
                                                                            spctype.setSelection(15);
                                                                        }else{
                                                                            if("RECREATION-RESORTS,MOVIEHOUSE,CLUBS,ETC.".equals(tempstring)){
                                                                                spctype.setSelection(16);
                                                                            }else{
                                                                                if("RESTAURANT QUICK SERVICE RESTAURANT".equals(tempstring)){
                                                                                    spctype.setSelection(17);
                                                                                }else{
                                                                                    if("SARI-SARI STORE".equals(tempstring)){
                                                                                        spctype.setSelection(18);
                                                                                    }else{
                                                                                        if("SCHOOL CANTEEN-COLLEGE".equals(tempstring)){
                                                                                            spctype.setSelection(19);
                                                                                        }else{
                                                                                            if("SCHOOL CANTEEN-ELEMENTARY".equals(tempstring)){
                                                                                                spctype.setSelection(20);
                                                                                            }else{
                                                                                                if("SCHOOL CANTEEN-HIGH SCHOOL".equals(tempstring)){
                                                                                                    spctype.setSelection(21);
                                                                                                }else{
                                                                                                    if("SEC. SUPERMARKET".equals(tempstring)){
                                                                                                        spctype.setSelection(22);
                                                                                                    }else{
                                                                                                        if("SPORTS, FITNESS CENTERS".equals(tempstring)){
                                                                                                            spctype.setSelection(23);
                                                                                                        }else{
                                                                                                            if("SUPERMARKET WITH 16 AND ABOVE COCS".equals(tempstring)){
                                                                                                                spctype.setSelection(24);
                                                                                                            }else{
                                                                                                                if("SUPERMARKET WITH 3-7 COCS".equals(tempstring)){
                                                                                                                    spctype.setSelection(25);
                                                                                                                }else{
                                                                                                                    if("SUPERMARKET WITH 8-15 COCS".equals(tempstring)){
                                                                                                                        spctype.setSelection(26);
                                                                                                                    }else{
                                                                                                                        if("TERMINAL (BUS, AIRPORT, SHIP, ETC.)".equals(tempstring)){
                                                                                                                            spctype.setSelection(27);
                                                                                                                        }else{
                                                                                                                            if("WHOLESALER WITH PEDDLING".equals(tempstring)){
                                                                                                                                spctype.setSelection(28);
                                                                                                                            }else{
                                                                                                                                if("WHOLESALER WITH SELLING AREA".equals(tempstring)){
                                                                                                                                    spctype.setSelection(29);
                                                                                                                                }else{
                                                                                                                                    spctype.setSelection(30);
                                                                                                                                    OtherCustType1.setText(getIntent().getExtras().getString("ctype").toUpperCase());
                                                                                                                                    // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
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

            // Customer Account Type
            tempstring = "" + getIntent().getExtras().getString("atype").toUpperCase();
            spatype = findViewById(R.id.SpinnerAccountTypecon);
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
            spterms = findViewById(R.id.SpinnerCustomerTermcon);
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
            conperson = findViewById(R.id.etContactPersoncon);
            conperson.setText(getIntent().getExtras().getString("cperson"));

            // Contact Person bday
            conbday = findViewById(R.id.etBdaycontactcon);
            conbday.setText(getIntent().getExtras().getString("cbday"));

            // Contact Person tellnumber
            contellnum = findViewById(R.id.ettellnumconcon);
            contellnum.setText(getIntent().getExtras().getString("ctellnum"));

            // Contact Person cellnumber
            concellnum = findViewById(R.id.etCellNumConcon);
            concellnum.setText(getIntent().getExtras().getString("ccellnum"));

            // owner Person
            ownperson = findViewById(R.id.etOwnerNamecon);
            ownperson.setText(getIntent().getExtras().getString("owner"));

            // owner Person bday
            ownbday = findViewById(R.id.etbdayownercon);
            ownbday.setText(getIntent().getExtras().getString("obday"));

            // owner Person tellnumber
            owntellnum = findViewById(R.id.etTellnumOwnercon);
            owntellnum.setText(getIntent().getExtras().getString("otellnum"));

            // owner Person cellnumber
            owncellnum = findViewById(R.id.etCellNumOwncon);
            owncellnum.setText(getIntent().getExtras().getString("ocellnum"));

            // street
            street = findViewById(R.id.etStreetcon);
            street.setText(getIntent().getExtras().getString("street"));

            // programmer pass
            etprogrammerpass = findViewById(R.id.etprogrammerpass);

            // route code
            routecode = findViewById(R.id.Sproutecodecon);
            tempstring = "" + (getIntent().getExtras().getString("others").toUpperCase());

            switch (tempstring) {
                case "ROUTE1":
                    routecode.setSelection(0);
                    break;
                case "ROUTE2":
                    routecode.setSelection(1);
                    break;
                case "ROUTE3":
                    routecode.setSelection(2);
                    break;
                case "ROUTE4":
                    routecode.setSelection(3);
                    break;
                case "ROUTE5":
                    routecode.setSelection(4);
                    break;
                case "ROUTE6":
                    routecode.setSelection(5);
                    break;
                case "ROUTE7":
                    routecode.setSelection(6);
                    break;
                case "ROUTE8":
                    routecode.setSelection(7);
                    break;
                case "ROUTE9":
                    routecode.setSelection(8);
                    break;
                case "ROUTE10":
                    routecode.setSelection(9);
                    break;
                case "ROUTE11":
                    routecode.setSelection(10);
                    break;
                case "ROUTE12":
                    routecode.setSelection(11);
                    break;
                case "ROUTE13":
                    routecode.setSelection(12);
                    break;
                case "ROUTE14":
                    routecode.setSelection(13);
                    break;
                case "ROUTE15":
                    routecode.setSelection(14);
                    break;
                case "BOOKING1":
                    routecode.setSelection(15);
                    break;
                case "BOOKING2":
                    routecode.setSelection(16);
                    break;
                case "BOOKING3":
                    routecode.setSelection(17);
                    break;
                case "BOOKING4":
                    routecode.setSelection(18);
                    break;
                case "BOOKING5":
                    routecode.setSelection(19);
                    break;
                default:
                    routecode.setSelection(20);
                    break;
            }
            // route code

            // load municipality ang barangay
            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            barangay = findViewById(R.id.SpinnerBarangaycon);
            spmunicipality = findViewById(R.id.SpinnerMunicipalitycon);

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
            province = findViewById(R.id.SpinnerProvincecon);

            tempstring = (getIntent().getExtras().getString("province").toUpperCase());
            if("0".equals(tempstring)){
                province.setSelection(0);
            }else if("1".equals(tempstring)){
                province.setSelection(1);
            }else if("2".equals(tempstring)){
                province.setSelection(2);
            }else if("3".equals(tempstring)){
                province.setSelection(3);
            }else if("4".equals(tempstring)){
                province.setSelection(4);
            }else if("5".equals(tempstring)){
                province.setSelection(5);
            }else if("6".equals(tempstring)){
                province.setSelection(6);
            }else if("7".equals(tempstring)){
                province.setSelection(7);
            }else if("8".equals(tempstring)){
                province.setSelection(8);
            }else if("9".equals(tempstring)){
                province.setSelection(9);
            }else if("10".equals(tempstring)){
                province.setSelection(10);
            }else if("11".equals(tempstring)){
                province.setSelection(11);
            }else{
                province.setSelection(0);
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

            // subpocket
            // subpocket = (EditText) findViewById(R.id.etSubPocket);
            // subpocket.setText(getIntent().getExtras().getString("subpocket"));

            // Competetors Brand
            // caltex

            // latitude
            tvOlat = findViewById(R.id.tvolatcon);
            tvOlat.setText(getIntent().getExtras().getString("lati"));
            // longitude
            tvOlong = findViewById(R.id.tvolongcon);
            tvOlong.setText(getIntent().getExtras().getString("longi"));
            // progress.setVisibility(View.GONE);

            latlong = findViewById(R.id.etlonglatcon);
            latlong.setText("" + tvOlat.getText().toString() + "," + tvOlong.getText().toString());

            buttonsms = findViewById(R.id.btsendupdatescon);
            buttonsms.setOnClickListener(this);

            DisableToSendUpdate = getIntent().getExtras().getString("DisableToSend");

            if("Y".equalsIgnoreCase(DisableToSendUpdate)){
                buttonsms.setEnabled(false);
            }

            buttonsms2 = findViewById(R.id.btsendupdates2con);
            buttonsms2.setOnClickListener(this);

            // buttonsms3 =(Button) findViewById(R.id.btsendupdates3con);
            // buttonsms3.setOnClickListener(this);

            buttonsms4 = findViewById(R.id.btsendupdates4con);
            buttonsms4.setOnClickListener(this);

            buttonsavecurrentdtl = findViewById(R.id.btsavecurrentdtlcon);
            buttonsavecurrentdtl.setOnClickListener(this);

            tempstring = (getIntent().getExtras().getString("lati"));
            if("".equals(tempstring)){
                buttonsms4.setEnabled(false);
            }else{
                buttonsms4.setEnabled(true);
            }

            // spctype.setOnClickListener(this);
            String comp = tvOlat.getText().toString();
            if("0".equals(comp)){
                buttonsms4.setEnabled(false);
            }else{
                buttonsms4.setEnabled(true);
            }

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 =  c2.getCount();
            if(cnt2 > 0){
                DatabaseName = c2.getString(6); // database name
            }else{
                DatabaseName = "";
            }

        }catch (Exception e){
//            Toast.makeText(getApplicationContext(),	"" + e, Toast.LENGTH_LONG).show();
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
                if(ct > 29){
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

        validation = routecode.getSelectedItem().toString();
        if("SELLECT ROUTE CODE".equals(validation)){
            // checkvalidation = false;
            // Toast.makeText(getApplicationContext(),	"PLEASE SELLECT ROUTE CODE!", Toast.LENGTH_LONG).show();
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

        if("MARS2".equals(DatabaseName)){
            theMessage = "U2MAP1!" + theMessage  ;
        }else {
            theMessage = "UMAP1!" + theMessage  ;
        }

        validation = street.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Street Address!", Toast.LENGTH_LONG).show();
        }

        if("MARS2".equals(DatabaseName)){
            theMessage1 = "U2MAP2!" + custid.getText().toString() + "!" + conperson.getText().toString() + "!" + conbday.getText().toString() + "!" + contellnum.getText().toString() + "!"	+ concellnum.getText().toString()	+ "!" + ownperson.getText().toString() + "!" + ownbday.getText().toString() + "!" + owntellnum.getText().toString() + "!" + owncellnum.getText().toString() + "!" + barangayCode + "!" + validation;
        }else {
            theMessage1 = "UMAP2!" + custid.getText().toString() + "!" + conperson.getText().toString() + "!" + conbday.getText().toString() + "!" + contellnum.getText().toString() + "!"	+ concellnum.getText().toString()	+ "!" + ownperson.getText().toString() + "!" + ownbday.getText().toString() + "!" + owntellnum.getText().toString() + "!" + owncellnum.getText().toString() + "!" + barangayCode + "!" + validation;
        }
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

        String TempPass = "" + etprogrammerpass.getText().toString();

        if("222111".equals(TempPass)){
            // checkvalidation = true;
        }else{
            Toast.makeText(getApplicationContext(),	"Invalid Programmer Pass!", Toast.LENGTH_LONG).show();
            checkvalidation = false;
        }

        if(v.getId() == R.id.btsavecurrentdtlcon){
            if (checkvalidation)	{

                String getcustid = custid.getText().toString();
                SQLiteDatabase	db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                Cursor c = db.rawQuery("select * from CUSTOMERS where cid = '"+ getcustid +"'", null);
                c.moveToFirst();
                if(c.getCount() > 0){
                    Integer ctype = spctype.getSelectedItemPosition();
                    if(ctype > 29){
                        db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ OtherCustType1.getText().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                                ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                                ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                                ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                                ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                                ", province = '"+ province.getSelectedItemPosition() + "', others = '"+ routecode.getSelectedItem().toString() +"'" +
                                " where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                                ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                                ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                                ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                                ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                                ", province = '"+ province.getSelectedItemPosition() +"', others = '"+ routecode.getSelectedItem().toString() +"'" +
                                " where cid = '"+ getcustid +"';");
                    }

                    Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        }

        if(v.getId() == R.id.btsendupdatescon){
            String TempPass1 = "" + etprogrammerpass.getText().toString();

            if("222111".equals(TempPass1)){
                checkvalidation = true;
            }else{
                Toast.makeText(getApplicationContext(),	"Invalid Programmer Pass!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if (checkvalidation){
                // progress.setVisibility(View.VISIBLE);
                // exceptions will be thrown if provider is not permitted.
                try {
                    gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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

        if(v.getId() == R.id.btsendupdates2con){
            String TempPass1 = "" + etprogrammerpass.getText().toString();

            if("222111".equals(TempPass1)){
                checkvalidation = true;
            }else{
                Toast.makeText(getApplicationContext(),	"Invalid Programmer Pass!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            getlenthmessage = (theMessage1.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Other Details is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){

                Cursor c11 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c11.moveToFirst();
                int cnt1 =  c11.getCount();
                if(cnt1 > 0){
                    theMobNo = c11.getString(0);
                }
                sendSMS(theMobNo, theMessage1);
                // Toast.makeText(getApplicationContext(), "sEND ADDRESS", Toast.LENGTH_LONG).show();
                // buttonsms3.setEnabled(true);
            }
        }

        if(v.getId() == R.id.btsendupdates4con){
            String TempPass1 = "" + etprogrammerpass.getText().toString();

            if("222111".equals(TempPass1)){
                checkvalidation = true;
            }else{
                Toast.makeText(getApplicationContext(),	"Invalid Programmer Pass!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }
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

                        theMessage = theMessage + "!" + latitude + "!" + longitude  + "!" + routecode.getSelectedItem().toString();

                        Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                        c12.moveToFirst();
                        int cnt23 =  c12.getCount();
                        if(cnt23 > 0){
                            theMobNo = c12.getString(0);
                        }

                        sendSMS(theMobNo, theMessage);
                        sendSMS(theMobNo, theMessage1);
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

                // Toast.makeText(getApplicationContext(), "" + longitude + accuracy, Toast.LENGTH_LONG).show();
                theMessage = theMessage + "!" + latitude + "!" + longitude + "!" + routecode.getSelectedItem().toString() ;

                Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c12.moveToFirst();
                int cnt =  c12.getCount();
                if(cnt > 0){
                    theMobNo = c12.getString(0);
                }

                sendSMS(theMobNo, theMessage);
                sendSMS(theMobNo, theMessage1);
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
            if(ctype > 29){
                db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ OtherCustType1.getText().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                        ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                        ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                        ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                        ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                        ", province = '"+ province.getSelectedItemPosition() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' , others = '"+ routecode.getSelectedItem().toString() + "' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                        ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                        ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                        ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                        ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                        ", province = '"+ province.getSelectedItemPosition() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' , others = '"+ routecode.getSelectedItem().toString() + "' where cid = '"+ getcustid +"';");
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
