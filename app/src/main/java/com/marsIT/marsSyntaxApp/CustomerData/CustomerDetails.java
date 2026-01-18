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
import android.widget.CheckBox;
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
public class CustomerDetails extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String theMessage1;
    private String theMessage2;
    private String validation;
//    private ProgressBar progress;
    private String longitude;
    private String latitude;
    private String Databasename;
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
    private EditText subpocket;
    private EditText others;
    private EditText phonenumber;
    private List<String> lables;
    private List<String> lablesb;
//    private ArrayAdapter<String> adapter;
    private SQLiteDatabase	db;
    private TextView tvOlat;
    private TextView tvOlong;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private CheckBox caltex;
    private CheckBox mobil;
    private CheckBox castrol;
    private CheckBox petron;
    private CheckBox caf;
    private CheckBox dti;
    private CheckBox bpermit;
    private CheckBox signage;
    private CheckBox poster;
    private CheckBox streamer;
    private CheckBox sticker;
    private CheckBox disrack;
    private CheckBox prodisplay;
    private Spinner spctype;
    private Spinner spatype;
    private Spinner spterms;
    private Spinner province;
    private Spinner barangay;
    private Spinner spmunicipality;
    private Button buttonsms;
    private Button buttonsms2;
    private Button buttonsms3;
    private Button buttonsms4;
//    private Button buttonGenM;
    private Button buttonsavecurrentdtl;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerdetails);

        try {

            setupToolbar("Customer Details");
            // Force menu to appear
            invalidateOptionsMenu();

            longitude = "";
            latitude = "";

            // progress = (ProgressBar) findViewById(R.id.progressBar1);
            buttonsms2 = findViewById(R.id.btsendupdates2);
            buttonsms2.setEnabled(false);

            buttonsms3 = findViewById(R.id.btsendupdates3);
            buttonsms3.setEnabled(false);

            buttonsms4 = findViewById(R.id.btsendupdates4);

            // customer id
            custid = findViewById(R.id.etCustomerID);
            custid.setText(getIntent().getExtras().getString("cid"));
            // customer name
            custname = findViewById(R.id.etCustomerName);
            custname.setText(getIntent().getExtras().getString("cname"));
            custname.setEnabled(false);

            Databasename = getIntent().getExtras().getString("Databasename");

            phonenumber = findViewById(R.id.etReceiverNumber);

            // CUSTOMER TYPE VALUE
            tempstring = "" + getIntent().getExtras().getString("ctype").toUpperCase();
            spctype = findViewById(R.id.SpinnerCustomerType);

            OtherCustType1 = findViewById(R.id.etOtherCustType1);

            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if ("IWS-BIKE".equals(tempstring)) {
                spctype.setSelection(0);
            } else {

                if ("IWS-AUTO".equals(tempstring)) {
                    spctype.setSelection(1);
                } else {

                    if ("HARDWARE".equals(tempstring)) {
                        spctype.setSelection(2);
                    } else {

                        if ("MOTORPARTS".equals(tempstring)) {
                            spctype.setSelection(3);
                        } else {

                            if ("BIKES SHOP".equals(tempstring)) {
                                spctype.setSelection(4);
                            } else {

                                if ("GENERAL MERCHANDISE".equals(tempstring)) {
                                    spctype.setSelection(6);
                                } else {

                                    if ("SHELL STATION".equals(tempstring)) {
                                        spctype.setSelection(7);
                                    } else {
                                        if ("CALTEX STATION".equals(tempstring)) {
                                            spctype.setSelection(8);
                                        } else {
                                            if ("PETRON STATION".equals(tempstring)) {
                                                spctype.setSelection(9);
                                            } else {
                                                if ("PHOENIX STATION".equals(tempstring)) {
                                                    spctype.setSelection(10);
                                                } else {

                                                    if ("WHITE STATION".equals(tempstring)) {
                                                        spctype.setSelection(11);
                                                    } else {

                                                        if ("TRANSPORT".equals(tempstring)) {
                                                            spctype.setSelection(12);
                                                        } else {

                                                            if ("CONSTRUCTION".equals(tempstring)) {
                                                                spctype.setSelection(13);
                                                            } else {

                                                                if ("GENERAL MANUFACTURING".equals(tempstring)) {
                                                                    spctype.setSelection(14);
                                                                } else {

                                                                    if ("WHOLESALES".equals(tempstring)) {
                                                                        spctype.setSelection(15);
                                                                    } else {

                                                                        if ("MINING".equals(tempstring)) {
                                                                            spctype.setSelection(16);
                                                                        } else {
                                                                            if ("AUTO SHOP".equals(tempstring)) {
                                                                                spctype.setSelection(5);
                                                                            } else {

                                                                                spctype.setSelection(17);
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

            // Customer Account Type
            tempstring = "" + getIntent().getExtras().getString("atype").toUpperCase();
            spatype = findViewById(R.id.SpinnerAccountType);
            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if ("PDC".equals(tempstring)) {
                spatype.setSelection(0);
            } else if ("CASH ON DUE".equals(tempstring)) {
                spatype.setSelection(1);
            } else if ("CASH/COD".equals(tempstring)) {
                spatype.setSelection(2);
            } else {
                spatype.setSelection(2);
            }
            spatype.setEnabled(false);

            // CUSTOMER TERMS
            tempstring = "" + getIntent().getExtras().getString("terms").toUpperCase();
            spterms = findViewById(R.id.SpinnerCustomerTerm);
            // Toast.makeText(getApplicationContext(), "" + tempstring, Toast.LENGTH_LONG).show();
            if ("A30".equals(tempstring)) {
                spterms.setSelection(0);
            } else if ("B30".equals(tempstring)) {
                spterms.setSelection(1);
            } else if ("C30".equals(tempstring)) {
                spterms.setSelection(2);
            } else if ("D30".equals(tempstring)) {
                spterms.setSelection(3);
            } else if ("E30".equals(tempstring)) {
                spterms.setSelection(4);
            } else if ("F30".equals(tempstring)) {
                spterms.setSelection(5);
            } else if ("15".equals(tempstring)) {
                spterms.setSelection(6);
            } else if ("7".equals(tempstring)) {
                spterms.setSelection(7);
            } else {
                spterms.setSelection(8);
            }
            spterms.setEnabled(false);

            // Contact Person
            conperson = findViewById(R.id.etContactPerson);
            conperson.setText(getIntent().getExtras().getString("cperson"));

            // Contact Person bday
            conbday = findViewById(R.id.etBdaycontact);
            conbday.setText(getIntent().getExtras().getString("cbday"));

            // Contact Person tellnumber
            contellnum = findViewById(R.id.ettellnumcon);
            contellnum.setText(getIntent().getExtras().getString("ctellnum"));

            // Contact Person cellnumber
            concellnum = findViewById(R.id.etCellNumCon);
            concellnum.setText(getIntent().getExtras().getString("ccellnum"));

            // owner Person
            ownperson = findViewById(R.id.etOwnerName);
            ownperson.setText(getIntent().getExtras().getString("owner"));

            // owner Person bday
            ownbday = findViewById(R.id.etbdayowner);
            ownbday.setText(getIntent().getExtras().getString("obday"));

            // owner Person tellnumber
            owntellnum = findViewById(R.id.etTellnumOwner);
            owntellnum.setText(getIntent().getExtras().getString("otellnum"));

            // owner Person cellnumber
            owncellnum = findViewById(R.id.etCellNumOwn);
            owncellnum.setText(getIntent().getExtras().getString("ocellnum"));

            // street
            street = findViewById(R.id.etStreet);
            street.setText(getIntent().getExtras().getString("street"));

            // load municipality ang barangay

            db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

            barangay = findViewById(R.id.SpinnerBarangay);
            spmunicipality = findViewById(R.id.SpinnerMunicipality);

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
            province = findViewById(R.id.SpinnerProvince);

            tempstring = (getIntent().getExtras().getString("province").toUpperCase());
            if ("0".equals(tempstring)) {
                province.setSelection(0);
            } else if ("1".equals(tempstring)) {
                province.setSelection(1);
            } else if ("2".equals(tempstring)) {
                province.setSelection(2);
            } else if ("3".equals(tempstring)) {
                province.setSelection(3);
            } else if ("4".equals(tempstring)) {
                province.setSelection(4);
            } else if ("5".equals(tempstring)) {
                province.setSelection(5);
            } else if ("6".equals(tempstring)) {
                province.setSelection(6);
            } else if ("7".equals(tempstring)) {
                province.setSelection(7);
            } else if ("8".equals(tempstring)) {
                province.setSelection(8);
            } else if ("9".equals(tempstring)) {
                province.setSelection(9);
            } else if ("10".equals(tempstring)) {
                province.setSelection(10);
            } else if ("11".equals(tempstring)) {
                province.setSelection(11);
            } else {
                province.setSelection(0);
            }

            countpro = 0;
            province.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0) {
                        countpro = 1;
                    } else {

                        tempstring = province.getSelectedItem().toString();
                        selectQuerymunicipality = "Select distinct(Municipality) as municipal from barangay where province like '" + tempstring + "' ";
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
                        selectQueryBarangay = "Select distinct(barangay) as barangay from barangay where municipality like '" + tempstring + "' and province like '%" + province.getSelectedItem().toString() + "%' ";
                        loadSpinnerDataBarangay();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            // subpocket
            subpocket = findViewById(R.id.etSubPocket);
            subpocket.setText(getIntent().getExtras().getString("subpocket"));

            // Competetors Brand
            // caltex
            caltex = findViewById(R.id.cbCaltex);
            tempstring = (getIntent().getExtras().getString("caltex"));
            if ("1".equals(tempstring)) {
                caltex.setChecked(true);
            } else {
                caltex.setChecked(false);
            }

            mobil = findViewById(R.id.cbMobil);
            tempstring = (getIntent().getExtras().getString("mobil"));
            if ("1".equals(tempstring)) {
                mobil.setChecked(true);
            } else {
                mobil.setChecked(false);
            }

            castrol = findViewById(R.id.cbCastrol);
            tempstring = (getIntent().getExtras().getString("castrol"));
            if ("1".equals(tempstring)) {
                castrol.setChecked(true);
            } else {
                castrol.setChecked(false);
            }

            petron = findViewById(R.id.cbPetron);
            tempstring = (getIntent().getExtras().getString("petron"));
            if ("1".equals(tempstring)) {
                petron.setChecked(true);
            } else {
                petron.setChecked(false);
            }

            // others competetor
            others = findViewById(R.id.etOtherCompetetor);
            others.setText(getIntent().getExtras().getString("others"));

            // caf
            caf = findViewById(R.id.cbCAF);
            tempstring = (getIntent().getExtras().getString("caf"));
            if ("1".equals(tempstring)) {
                caf.setChecked(true);
            } else {
                caf.setChecked(false);
            }

            // dti
            dti = findViewById(R.id.cbDTI);
            tempstring = (getIntent().getExtras().getString("dti"));
            if ("1".equals(tempstring)) {
                dti.setChecked(true);
            } else {
                dti.setChecked(false);
            }

            // Business Permit
            bpermit = findViewById(R.id.cbBusinessPermit);
            tempstring = (getIntent().getExtras().getString("bpermit"));
            if ("1".equals(tempstring)) {
                bpermit.setChecked(true);
            } else {
                bpermit.setChecked(false);
            }

            // Signage
            signage = findViewById(R.id.cbSignage);
            tempstring = (getIntent().getExtras().getString("signage"));
            if ("1".equals(tempstring)) {
                signage.setChecked(true);
            } else {
                signage.setChecked(false);
            }

            // poster
            poster = findViewById(R.id.cbPoster);
            tempstring = (getIntent().getExtras().getString("poster"));
            if ("1".equals(tempstring)) {
                poster.setChecked(true);
            } else {
                poster.setChecked(false);
            }

            // poster
            streamer = findViewById(R.id.cbStreamer);
            tempstring = (getIntent().getExtras().getString("streamer"));
            if ("1".equals(tempstring)) {
                streamer.setChecked(true);
            } else {
                streamer.setChecked(false);
            }

            // sticker
            sticker = findViewById(R.id.cbSticker);
            tempstring = (getIntent().getExtras().getString("sticker"));
            if ("1".equals(tempstring)) {
                sticker.setChecked(true);
            } else {
                sticker.setChecked(false);
            }

            // Display Rack
            disrack = findViewById(R.id.cbDisplayRack);
            tempstring = (getIntent().getExtras().getString("disrack"));
            if ("1".equals(tempstring)) {
                disrack.setChecked(true);
            } else {
                disrack.setChecked(false);
            }

            // Display Rack
            prodisplay = findViewById(R.id.cbDisplayProduct);
            tempstring = (getIntent().getExtras().getString("prodisplay"));
            if ("1".equals(tempstring)) {
                prodisplay.setChecked(true);
            } else {
                prodisplay.setChecked(false);
            }

            // latitude
            tvOlat = findViewById(R.id.tvolat);
            tvOlat.setText(getIntent().getExtras().getString("lati"));
            // longitude
            tvOlong = findViewById(R.id.tvolong);
            tvOlong.setText(getIntent().getExtras().getString("longi"));
            // progress.setVisibility(View.GONE);

            buttonsms = findViewById(R.id.btsendupdates);
            buttonsms.setOnClickListener(this);

            buttonsms2 = findViewById(R.id.btsendupdates2);
            buttonsms2.setOnClickListener(this);

            buttonsms3 = findViewById(R.id.btsendupdates3);
            buttonsms3.setOnClickListener(this);

            buttonsms4 = findViewById(R.id.btsendupdates4);
            buttonsms4.setOnClickListener(this);

            buttonsavecurrentdtl = findViewById(R.id.btsavecurrentdtl);
            buttonsavecurrentdtl.setOnClickListener(this);

            tempstring = (getIntent().getExtras().getString("lati"));
            if ("".equals(tempstring)) {
                buttonsms4.setEnabled(false);
            } else {
                buttonsms4.setEnabled(true);
            }

            // spctype.setOnClickListener(this);
            String comp = tvOlat.getText().toString();
            if ("0".equals(comp)) {
                buttonsms4.setEnabled(false);
            } else {
                buttonsms4.setEnabled(true);
            }
            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            Log.e("ONCREATE ERROR", "onCreate: ", e);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // getclick = spctype.getSelectedItemPosition();

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
                if(ct > 15){
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

        if("MARS2".equals(Databasename)){
            theMessage = "CMAP12!" + theMessage ;
            theMessage2 = "CMAP32!" + custid.getText().toString() + "!" + conperson.getText().toString() + "!" + conbday.getText().toString() + "!" + contellnum.getText().toString() + "!"	+ concellnum.getText().toString()	+ "!" + ownperson.getText().toString() + "!" + ownbday.getText().toString() + "!" + owntellnum.getText().toString() + "!" + owncellnum.getText().toString();

        }else{
            theMessage = "CMAP1!" + theMessage ;
            theMessage2 = "CMAP3!" + custid.getText().toString() + "!" + conperson.getText().toString() + "!" + conbday.getText().toString() + "!" + contellnum.getText().toString() + "!"	+ concellnum.getText().toString()	+ "!" + ownperson.getText().toString() + "!" + ownbday.getText().toString() + "!" + owntellnum.getText().toString() + "!" + owncellnum.getText().toString();
        }

        validation = street.getText().toString();
        if("".equals(validation)){
            checkvalidation = false;
            Toast.makeText(getApplicationContext(),	"Please Input Street Address!", Toast.LENGTH_LONG).show();
        }

        if(caltex.isChecked()){
        }else{
            if(mobil.isChecked()){
            }else{
                if(castrol.isChecked()){
                }else{
                    if(petron.isChecked()){
                    }else{
                        validation = others.getText().toString();
                        if("".equals(validation)){
                            checkvalidation = false;
                            Toast.makeText(getApplicationContext(),	"Please Select Competitor or Type Other!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        if("MARS2".equals(Databasename)){
            theMessage1 = "CMAP22!" + custid.getText().toString() +"!" + street.getText().toString() + "!" + barangay.getSelectedItem().toString() +"!" + spmunicipality.getSelectedItem().toString() + "!" + province.getSelectedItemPosition();
        }else {
            theMessage1 = "CMAP2!" + custid.getText().toString() +"!" + street.getText().toString() + "!" + barangay.getSelectedItem().toString() +"!" + spmunicipality.getSelectedItem().toString() + "!" + province.getSelectedItemPosition();
        }

        if(caltex.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(mobil.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(castrol.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(petron.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        validation = others.getText().toString();
        if("".equals(validation)){
            theMessage1 = theMessage1 + "!" + "";
        }else{
            theMessage1 = theMessage1 + "!" + validation;
        }

        if(caf.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(dti.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(bpermit.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(signage.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(poster.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(streamer.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(sticker.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(disrack.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
        }

        if(prodisplay.isChecked()){
            theMessage1 = theMessage1 + "!" + "1";
        }else{
            theMessage1 = theMessage1 + "!" + "0";
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

        getlenthmessage = (theMessage2.length());
        if(getlenthmessage >= 160){
            Toast.makeText(getApplicationContext(),	"Message is to long! lenght : " + getlenthmessage, Toast.LENGTH_LONG).show();
            checkvalidation = false;
        }

        if(v.getId() == R.id.btsavecurrentdtl){

            if (checkvalidation) {

                String getcustid = custid.getText().toString();
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
                                ", province = '"+ province.getSelectedItemPosition() +"', subpocket = '"+ subpocket.getText().toString() +"',others = '"+ others.getText().toString() +"'" +
                                ",status = 'M' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                                ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                                ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                                ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                                ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                                ", province = '"+ province.getSelectedItemPosition() +"', subpocket = '"+ subpocket.getText().toString() +"',others = '"+ others.getText().toString() +"'" +
                                ",status = 'M' where cid = '"+ getcustid +"';");
                    }

                    if(caltex.isChecked()){
                        db.execSQL("Update Customers set caltex = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set caltex = '0' where cid = '"+ getcustid +"';");
                    }

                    if(mobil.isChecked()){
                        db.execSQL("Update Customers set mobil = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set mobil = '0' where cid = '"+ getcustid +"';");
                    }

                    if(castrol.isChecked()){
                        db.execSQL("Update Customers set castrol = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set castrol = '0' where cid = '"+ getcustid +"';");
                    }

                    if(petron.isChecked()){
                        db.execSQL("Update Customers set petron = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set petron = '0' where cid = '"+ getcustid +"';");
                    }

                    if(caf.isChecked()){
                        db.execSQL("Update Customers set caf = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set caf = '0' where cid = '"+ getcustid +"';");
                    }

                    if(dti.isChecked()){
                        db.execSQL("Update Customers set dti = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set dti = '0' where cid = '"+ getcustid +"';");
                    }

                    if(bpermit.isChecked()){
                        db.execSQL("Update Customers set bpermit = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set bpermit = '0' where cid = '"+ getcustid +"';");
                    }

                    if(signage.isChecked()){
                        db.execSQL("Update Customers set signage = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set signage = '0' where cid = '"+ getcustid +"';");
                    }

                    if(poster.isChecked()){
                        db.execSQL("Update Customers set poster = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set poster = '0' where cid = '"+ getcustid +"';");
                    }

                    if(streamer.isChecked()){
                        db.execSQL("Update Customers set streamer = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set streamer = '0' where cid = '"+ getcustid +"';");
                    }

                    if(sticker.isChecked()){
                        db.execSQL("Update Customers set sticker = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set sticker = '0' where cid = '"+ getcustid +"';");
                    }

                    if(disrack.isChecked()){
                        db.execSQL("Update Customers set disrack = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set disrack = '0' where cid = '"+ getcustid +"';");
                    }

                    if(prodisplay.isChecked()){
                        db.execSQL("Update Customers set prodisplay = '1' where cid = '"+ getcustid +"';");
                    }else{
                        db.execSQL("Update Customers set prodisplay = '0' where cid = '"+ getcustid +"';");
                    }

                    Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();

                    db.close();
                }
            }
        }

        if(v.getId() == R.id.btsendupdates){
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

        if(v.getId() == R.id.btsendupdates2){

            getlenthmessage = (theMessage1.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Other Details is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){
                theMobNo = phonenumber.getText().toString();

                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                int cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }

                sendSMS(theMobNo, theMessage1);
                buttonsms3.setEnabled(true);
            }
        }

        if(v.getId() == R.id.btsendupdates3){
            getlenthmessage = (theMessage2.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Message is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){
                theMobNo = phonenumber.getText().toString();

                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                int cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }
                sendSMS(theMobNo, theMessage2);
            }
        }

        if(v.getId() == R.id.btsendupdates4){
            getlenthmessage = (theMessage.length());
            if(getlenthmessage >= 160){
                Toast.makeText(getApplicationContext(),	"Message is to long!", Toast.LENGTH_LONG).show();
                checkvalidation = false;
            }

            if(checkvalidation){
                try{
                    SQLiteDatabase	db1 = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
                    Cursor c1 = db1.rawQuery("select * from CUSTOMERS where cid = '"+ custid.getText().toString() +"'", null);
                    c1.moveToFirst();
                    if(c1.getCount() > 0){

                        latitude =  "" + c1.getString(32);
                        longitude =  "" + c1.getString(33);
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
                        buttonsms3.setEnabled(true);
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

                theMobNo = phonenumber.getText().toString();
                theMessage = theMessage + "!" + latitude + "!" + longitude;

                Cursor c12 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c12.moveToFirst();
                int cnt =  c12.getCount();
                if(cnt > 0){
                    theMobNo = c12.getString(0);
                }

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
                        ", province = '"+ province.getSelectedItemPosition() +"', subpocket = '"+ subpocket.getText().toString() +"',others = '"+ others.getText().toString() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set CNAME = '"+ custname.getText().toString() +"',ctype = '"+ spctype.getSelectedItem().toString() +"',atype = '"+ spatype.getSelectedItem().toString() + "'" +
                        ", terms = '"+ spterms.getSelectedItem().toString() +"', cperson = '"+ conperson.getText().toString() + "', cbday = '"+ conbday.getText().toString() +"'" +
                        ", ctellnum = '"+ contellnum.getText().toString() +"',ccellnum = '"+ concellnum.getText().toString() +"', owner = '"+ ownperson.getText().toString() +"'" +
                        ", obday = '"+ ownbday.getText().toString() +"',otellnum = '"+ owntellnum.getText().toString() +"',ocellnum = '"+ owncellnum.getText().toString() +"'" +
                        ", street = '"+ street.getText().toString() +"', barangay = '"+ barangay.getSelectedItem().toString() +"', municipality = '"+ spmunicipality.getSelectedItem().toString() +"'" +
                        ", province = '"+ province.getSelectedItemPosition() +"', subpocket = '"+ subpocket.getText().toString() +"',others = '"+ others.getText().toString() +"'" +
                        ",latitude = '"+ latitude +"',longitude = '"+ longitude +"',status = 'M' where cid = '"+ getcustid +"';");
            }

            if(caltex.isChecked()){
                db.execSQL("Update Customers set caltex = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set caltex = '0' where cid = '"+ getcustid +"';");
            }

            if(mobil.isChecked()){
                db.execSQL("Update Customers set mobil = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set mobil = '0' where cid = '"+ getcustid +"';");
            }

            if(castrol.isChecked()){
                db.execSQL("Update Customers set castrol = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set castrol = '0' where cid = '"+ getcustid +"';");
            }

            if(petron.isChecked()){
                db.execSQL("Update Customers set petron = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set petron = '0' where cid = '"+ getcustid +"';");
            }

            if(caf.isChecked()){
                db.execSQL("Update Customers set caf = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set caf = '0' where cid = '"+ getcustid +"';");
            }

            if(dti.isChecked()){
                db.execSQL("Update Customers set dti = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set dti = '0' where cid = '"+ getcustid +"';");
            }

            if(bpermit.isChecked()){
                db.execSQL("Update Customers set bpermit = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set bpermit = '0' where cid = '"+ getcustid +"';");
            }

            if(signage.isChecked()){
                db.execSQL("Update Customers set signage = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set signage = '0' where cid = '"+ getcustid +"';");
            }

            if(poster.isChecked()){
                db.execSQL("Update Customers set poster = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set poster = '0' where cid = '"+ getcustid +"';");
            }

            if(streamer.isChecked()){
                db.execSQL("Update Customers set streamer = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set streamer = '0' where cid = '"+ getcustid +"';");
            }

            if(sticker.isChecked()){
                db.execSQL("Update Customers set sticker = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set sticker = '0' where cid = '"+ getcustid +"';");
            }

            if(disrack.isChecked()){
                db.execSQL("Update Customers set disrack = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set disrack = '0' where cid = '"+ getcustid +"';");
            }

            if(prodisplay.isChecked()){
                db.execSQL("Update Customers set prodisplay = '1' where cid = '"+ getcustid +"';");
            }else{
                db.execSQL("Update Customers set prodisplay = '0' where cid = '"+ getcustid +"';");
            }

            Toast.makeText(getApplicationContext(), "Customer Update Successfull!",Toast.LENGTH_LONG).show();

            buttonsms2.setEnabled(true);
            db.close();

        }else{
            Toast.makeText(getApplicationContext(), "Customer Not Found, Update Not Successfull!",Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerDataMunicipality() {
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

    private void loadSpinnerDataBarangay() {
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
