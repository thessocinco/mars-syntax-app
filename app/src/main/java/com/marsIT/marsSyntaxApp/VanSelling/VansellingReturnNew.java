package com.marsIT.marsSyntaxApp.VanSelling;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import android.R.integer;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
//import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.marsIT.marsSyntaxApp.Inventory.AddItem;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

@SuppressWarnings("deprecation")
public class VansellingReturnNew extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    static final String SCAN = "com.google.zxing.client.android.SCAN";
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String itemcode;
    private String withtankloan;
    private String validation;
    private String selectQuery;
    private String longitude;
    private String latitude;
    private Spinner spreturntype;
    private EditText getnum;
    private EditText withtaxamount;
    private EditText osnumber;
    EditText inputSearch;
    EditText barcodeSearch;
    private EditText editRemarks;
    private EditText quantity;
    private EditText csquantity;
    private TextView custid;
    private TextView salesmanassigned;
    private TextView expirydate;
    private List<String> lables;
    private List<String> labless;
    private SQLiteDatabase	db;
    private TextView tvOlat;
    private TextView tvOlong;
    private TextView creditlimit;
    private TextView tvcreditlimit;
    private TextView tvbalcredit;
    private TextView tvocheck;
    private TextView tvavailcredit;
    private TextView totalinvqty;
    private TextView totalinvamount;
    private TextView termstatus;
    private TextView um;
    private TextView transinv;
    private TextView csfactor;
    private TextView csum;
    private EditText unitprice;
    private EditText csunitprice;
    private CheckBox withtax;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private Spinner spctype;
    private Spinner spproductnew;
    private Spinner sptermssp;
    private Spinner spdaystermsp;
    private Spinner spoutlet;
    private Spinner spcategory;
    private Button btaddtoinvoice;
    private Button btsendsave;
    private Button btdeleteitem1;
    private Button btbsinsert;
    private Button btbexpirydate;
    private Button btsendrequest;
    private Button btPayment;
    private Button btbarcode;
    private Button btcleardtl;
    private Button btcsbarcode;
    private Button additem;
    private boolean issave;
    private Integer countpro;
    private Integer countpro1;
    final Context context = this;
    private EditText qty1;
    private EditText prodesc;
    private EditText umdtls;
    private EditText expirydtls;
    private String Detailsinvoice;
    private String Detailsinvoice1;
    private String Detailsinvoice2;
    private String Detailsinvoice3;
    private String Detailsinvoice4;
    private String Detailsinvoice5;
    private String Detailsinvoice6;
    private String Detailsinvoice7;
    private String Detailsinvoice8;
    private String Detailsinvoice9;
    private String Detailsinvoice10;
    private String Detailsinvoice11;
    private String Detailsinvoice12;
    private String Detailsinvoice13;
    private String Detailsinvoice14;
    private String Detailsinvoice15;
    private String Detailsinvoice16;
    private String Detailsinvoice17;
    private String Detailsinvoice18;
    private String Detailsinvoice19;
    private String Detailsinvoice20;
    private String Detailsinvoice21;
    private String Detailsinvoice22;
    private String Detailsinvoice23;
    private String Detailsinvoice24;
    private String Databasename;
    private String Departmentcode;
    private String prefixval;
    private String Invnumberval;
    private String outlettrans;
    private String SalesmanID;
    private float tinvamount;
    private boolean loadactivty = true;
    private boolean network_enabled = false;

    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vansellingreturnnew);

        setupToolbar("VanSelling Return New");
        // Force menu to appear
        invalidateOptionsMenu();

        try {
            expirydate = (TextView) findViewById(R.id.tvexpirydatenewvr);

            //dateView = (TextView) findViewById(R.id.tvexpirydatenewvr);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);

            spproductnew = (Spinner) findViewById(R.id.spproductnewvr);
            spreturntype= (Spinner) findViewById(R.id.sprettypevr);

            spoutlet= (Spinner) findViewById(R.id.spoutletnewinvvr);
            spcategory= (Spinner) findViewById(R.id.spcategorynewinvvr);
            //spcustomer = (Spinner) findViewById(R.id.spcustomerlist);

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            SalesmanID = getIntent().getExtras().getString("salesmanid").toUpperCase();
            salesmanassigned = (TextView) findViewById(R.id.tvsalesmanvanretnew);
            salesmanassigned.setText(SalesmanID);

            Databasename = getIntent().getExtras().getString("Databasename").toUpperCase();
            Departmentcode = getIntent().getExtras().getString("Departmentcode").toUpperCase();

            issave = false;
            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
            // tablename
            db.execSQL("CREATE TABLE IF NOT EXISTS TempInvoice2" +
                    "(Refid VARCHAR," +
                    "productid VARCHAR," +
                    "Description Varchar," +
                    "um Varchar," +
                    "Qty integer," +
                    "expiry Varchar); ");

            //   db.execSQL("delete from TempInvoice2 ; ");

            String ProductQuery; // String ProductQuery = ""; - original

            //  if("VAN-SELLING".equals(transinv.getText().toString())){
            ProductQuery = "select * from products where salesmanid like '"+ SalesmanID + "' order by description ";
            //  }else{
            //  	ProductQuery = "select * from products order by description ";
            //  }

            Cursor c1 = db.rawQuery(ProductQuery, null);
            c1.moveToFirst();
            int cnt1 =  c1.getCount();
            if(cnt1 > 0){
                lables = new ArrayList<String>();
                if (c1.moveToFirst()) {
                    do {
                        lables.add(c1.getString(1));
                    } while (c1.moveToNext());
                }

                c1.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spproductnew.setAdapter(dataAdapter);
            }

            //  if("VAN-SELLING".equals(transinv.getText().toString())){
            ProductQuery = "select distinct(outlet) as ou from products WHERE SALESMANID = '"+ SalesmanID +"'order by description";
            //  }else{
            //  	ProductQuery = "select distinct(outlet) as ou from products order by description";
            //  }

            // for out loop
            Cursor c3 = db.rawQuery(ProductQuery, null);
            c3.moveToFirst();
            int cntoutlet =  c3.getCount();
            if(cntoutlet > 0){

                lables = new ArrayList<String>();
                if (c3.moveToFirst()) {
                    do {
                        tempstring = c3.getString(0);

                        if("L".equals(tempstring)){
                            lables.add("LUBRICANTS");
                        }else if("A".equals(tempstring)){
                            lables.add("AUTO-SUPPLY");
                        }else if("AG".equals(tempstring)){
                            lables.add("AGRICHEM");
                        }else if("CC".equals(tempstring)){
                            lables.add("CENTURY");
                        }else if("UC".equals(tempstring)){
                            lables.add("URC");
                        }else if("UB".equals(tempstring)){
                            lables.add("URC-BEV");
                        }
                    } while (c3.moveToNext());
                }

                c3.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spoutlet.setAdapter(dataAdapter);
            }

            spoutlet.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = spoutlet.getSelectedItem().toString();

                        if("LUBRICANTS".equals(tempstring)){
                            tempstring = "L";
                            outlettrans = "L";
                        }else if("AUTO-SUPPLY".equals(tempstring)){
                            tempstring = "A";
                            outlettrans = "A";
                        }else if("AGRICHEM".equals(tempstring)){
                            tempstring = "AG";
                            outlettrans = "AG";
                        }else if("CENTURY".equals(tempstring)){
                            tempstring = "CC";
                            outlettrans = "CC";
                        }else if("URC".equals(tempstring)){
                            tempstring = "UC";
                            outlettrans = "UC";
                        }else if("URC-BEV".equals(tempstring)){
                            tempstring = "UB";
                            outlettrans = "UB";
                        }

                        //	if("VAN-SELLING".equals(transinv.getText().toString())){
                        selectQuery = "Select * from products where outlet like '" + tempstring + "' and salesmanid = '"+ SalesmanID +"'  order by description ";
                        //   }else{
                        //   	selectQuery = "Select * from products where outlet like '" + tempstring + "' order by description ";
                        //   }
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            //   if("VAN-SELLING".equals(transinv.getText().toString())){
            ProductQuery = "select distinct(producttype) as ous from products WHERE salesmanid = '"+ SalesmanID +"'  order by producttype";
            //   }else{
            //  	ProductQuery = "select distinct(producttype) as ous from products order by producttype";
            //  }

            // for out loop
            Cursor c3t = db.rawQuery(ProductQuery, null);
            c3t.moveToFirst();
            int cntcat =  c3t.getCount();
            if(cntcat > 0){

                labless = new ArrayList<String>();
                if (c3t.moveToFirst()) {
                    do {
                        tempstring = c3t.getString(0);
                        labless.add(tempstring);
                    } while (c3t.moveToNext());
                }

                c3t.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, labless);

                // Drop down layout style - list view with radio button
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spcategory.setAdapter(dataAdapter);
            }

            spcategory.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = spcategory.getSelectedItem().toString();

                        //  selectQuery = "Select * from products where producttype like '" + tempstring + "' order by description ";

                        //  if("VAN-SELLING".equals(transinv.getText().toString())){
                        selectQuery = "select * from products WHERE salesmanid = '"+ SalesmanID +"' AND  producttype like '" + tempstring + "' order by description";
                        //  }else{
                        //  	selectQuery = "select * from products order by producttype";
                        // }

                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch = (EditText) findViewById(R.id.etqueryproductvr);
            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // MainActivity.this.adapter.getFilter().filter(cs);
                    // Toast.makeText(getApplicationContext(), "" + cs, Toast.LENGTH_LONG).show();

                    //  customername = cs.toString();
                    // Select All Query
                    tempstring = spoutlet.getSelectedItem().toString();
                    if("LUBRICANTS".equals(tempstring)){
                        outlettrans = "L";
                    }else if("AUTO-SUPPLY".equals(tempstring)){
                        outlettrans = "A";
                    }else if("AGRICHEM".equals(tempstring)){
                        outlettrans = "AG";
                    }else if("CENTURY".equals(tempstring)){
                        outlettrans = "CC";
                    }else if("URC".equals(tempstring)){
                        outlettrans = "UC";
                    }else if("URC-BEV".equals(tempstring)){
                        outlettrans = "UB";
                    }

                    //	if("VAN-SELLING".equals(transinv.getText().toString())){
                    selectQuery = "SELECT  description FROM products where description like '%"+ cs  +"%'  and salesmanid = '"+ SalesmanID +"' ORDER BY description";
                    //    }else{
                    //    	selectQuery = "SELECT  description FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                    //   }

                    Cursor c3 = db.rawQuery(selectQuery, null);
                    c3.moveToFirst();
                    int cntoutlet =  c3.getCount();
                    if(cntoutlet > 0){

                        //	if("VAN-SELLING".equals(transinv.getText().toString())){
                        selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%'  and salesmanid = '"+ SalesmanID +"'    ORDER BY description";
                        //    }else{
                        //    	selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                        //    }

                        //selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";

                        try{
                            loadlistofproducts();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                        }
                    }
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

            barcodeSearch = (EditText) findViewById(R.id.etquerybarcodevr);
            barcodeSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // MainActivity.this.adapter.getFilter().filter(cs);
                    // Toast.makeText(getApplicationContext(), "" + cs, Toast.LENGTH_LONG).show();

                    //  customername = cs.toString();
                    // Select All Query
                    tempstring = spoutlet.getSelectedItem().toString();
                    if("LUBRICANTS".equals(tempstring)){
                        outlettrans = "L";
                    }else if("AUTO-SUPPLY".equals(tempstring)){
                        outlettrans = "A";
                    }else if("AGRICHEM".equals(tempstring)){
                        outlettrans = "AG";
                    }else if("CENTURY".equals(tempstring)){
                        outlettrans = "CC";
                    }else if("URC".equals(tempstring)){
                        outlettrans = "UC";
                    }else if("URC-BEV".equals(tempstring)){
                        outlettrans = "UB";
                    }

                    //	if("VAN-SELLING".equals(transinv.getText().toString())){
                    selectQuery = "SELECT  * FROM products where barcode like '"+ cs  +"' and salesmanid = '"+ SalesmanID +"' ORDER BY description";
                    //    }else{
                    //    	selectQuery = "SELECT  description FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                    //   }

                    Cursor c3 = db.rawQuery(selectQuery, null);
                    c3.moveToFirst();
                    int cntoutlet =  c3.getCount();
                    if(cntoutlet > 0){
                    }else{

                        //if("VAN-SELLING".equals(transinv.getText().toString())){
                        selectQuery = "SELECT * FROM products where csbarcode like '"+ cs  +"'  and salesmanid = '"+ SalesmanID +"'    ORDER BY description";
                        //    }else{
                        //    	selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                        //    }

                        //selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                    }

                    try{
                        loadlistofproducts();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                    }
                    //  }
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

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            qty1 = (EditText) findViewById(R.id.etqtynewvr);
            prodesc = (EditText) findViewById(R.id.etdecriptionnewvr);
            umdtls = (EditText) findViewById(R.id.etumnewvr);
            expirydtls = (EditText) findViewById(R.id.etexpirynewvr);

            //osnumber = (EditText) findViewById(R.id.etosnumbernewinv);

            totalinvqty = (TextView) findViewById(R.id.tvtotalinvoiceqtyvr);
            //	totalinvamount = (TextView) findViewById(R.id.tvtotalinvoicamount);

            //details.setText("");

            um = (TextView) findViewById(R.id.tvumnewinvoicevr);
            csum = (TextView) findViewById(R.id.tvcsumnewinvoicevr);

            csfactor = (TextView) findViewById(R.id.tvcsfactornewinvoicevr);

            countpro = 0;
            countpro = 0;

            quantity =(EditText) findViewById(R.id.etquantitynewvr);
            csquantity =(EditText) findViewById(R.id.etcsquantitynewvr);

            spproductnew.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = spproductnew.getSelectedItem().toString();

                        //		if("VAN-SELLING".equals(transinv.getText().toString())){
                        selectQuery = "Select * from products where description like '"+ tempstring +"'  and salesmanid = '"+ SalesmanID +"' ";
                        //    }else{
                        //    	selectQuery = "Select * from products where description like '"+ tempstring +"' ";
                        //    }

                        //   selectQuery = "Select * from products where description like '"+ tempstring +"' ";

                        Cursor c1 = db.rawQuery(selectQuery, null);
                        c1.moveToFirst();
                        int cnt =  c1.getCount();
                        if(cnt > 0){
                            //Cursor c3 = db.rawQuery("Select * from itempriceoveride where itemcode like '"+ c1.getString(0) +"' and customerid = '"+ custid.getText().toString() +"'", null);
                            //   c3.moveToFirst();
                            try{
                                if("UC".equals(Departmentcode)){
                                    //		unitprice.setText(c1.getString(2));
                                    //	unitprice.setEnabled(false);
                                }else if("CC".equals(Departmentcode)){
                                    //	unitprice.setText(c1.getString(2));
                                    //	unitprice.setEnabled(false);
                                }else if("UB".equals(Departmentcode)){
                                    //	unitprice.setText(c1.getString(2));
                                    //	unitprice.setEnabled(false);
                                }else{
                                    //		unitprice.setText("0");  // price come from the syntax
                                    //	unitprice.setEnabled(true);
                                }

                                //	if("0".equals(unitprice.getText().toString())){
                                //		csfactor.setText(c1.getString(8));
                                //	}else{

                                csfactor.setText(c1.getString(8));

                                //float cspriceunit = Float.parseFloat(csfactor.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                //	DecimalFormat formatter = new DecimalFormat("#.00");
                                //	csunitprice.setText(String.valueOf(formatter.format(cspriceunit)));

                                //	}
                                //	unitprice.setEnabled(true);
                                um.setText(c1.getString(5));

                                csum.setText(c1.getString(9));  // case unit measure

                                String TempChar = "" + c1.getString(5);

                                if(TempChar.equalsIgnoreCase("" + c1.getString(9))){

                                    csquantity.setEnabled(false);
                                    csquantity.setText("");
                                }else{
                                    csquantity.setEnabled(true);
                                    csquantity.setText("");
                                }

                                TempChar = "" + csfactor.getText().toString();
                                if(TempChar.equalsIgnoreCase("0")){
                                    csquantity.setEnabled(false);
                                    csquantity.setText("");
                                }else{
                                    if(TempChar.equalsIgnoreCase("0")){
                                        csquantity.setEnabled(false);
                                        csquantity.setText("");
                                    }else if(TempChar.equalsIgnoreCase("1")){
                                        csquantity.setEnabled(false);
                                        csquantity.setText("");
                                    }else{
                                        csquantity.setEnabled(true);
                                        csquantity.setText("");
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"" + e ,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            //	spctype.setEnabled(false);

            spproductnew.setSelection(0);
            tempstring = spproductnew.getSelectedItem().toString();

            //	if("VAN-SELLING".equals(transinv.getText().toString())){
            selectQuery = "Select * from products where description like '"+ tempstring +"'  and salesmanid = '"+ SalesmanID +"' ";
            //    }else{
            //    	selectQuery = "Select * from products where description like '"+ tempstring +"' ";
            //    }

            Cursor c5 = db.rawQuery(selectQuery, null);
            c5.moveToFirst();
            int cnt =  c5.getCount();
            if(cnt > 0){

                //	Toast.makeText(getApplicationContext(), "not found pricelist!" + spctype.getSelectedItem().toString() + " " + c5.getString(0),Toast.LENGTH_LONG).show();
                //	unitprice.setText("0");
                um.setText(c5.getString(5));
            }

            btaddtoinvoice =  (Button) findViewById(R.id.btadddetailsnewinvvr);
            btaddtoinvoice.setOnClickListener(this);

            btdeleteitem1 = (Button) findViewById(R.id.btdeleteitemnewvr);
            btdeleteitem1.setOnClickListener(this);

            btbexpirydate = (Button) findViewById(R.id.btexpirydatenewvr);
            btbexpirydate.setOnClickListener(this);

            btsendsave = (Button) findViewById(R.id.btsendsavevr);
            btsendsave.setOnClickListener(this);

            btbarcode = (Button) findViewById(R.id.btscanbarcode);
            btbarcode.setOnClickListener(this);

            btcsbarcode = (Button) findViewById(R.id.btscanbarcodecs);
            btcsbarcode.setOnClickListener(this);

            btcleardtl = (Button) findViewById(R.id.btcleardetailsvr);
            btcleardtl.setOnClickListener(this);

            additem = (Button) findViewById(R.id.btadditemvr);
            additem.setOnClickListener(this);

            if(loadactivty){
                loadtempinvoit();
                loadactivty = false;
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"" + e ,Toast.LENGTH_LONG).show();
        }
    }			// end oncreate

    //private void checkunitprice(){

    //}

    public void scanbar(View v) {

        try{
            Intent in = new Intent(SCAN);
            in.putExtra("SCAN_MODE", "PRODUCT_MODE");

            startActivityForResult(in,0);

        } catch (ActivityNotFoundException e){
            // TODO: handle exception
            showDialog(VansellingReturnNew.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private Dialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence Yes,  CharSequence No) {
        // TODO Auto-generated method stub

        AlertDialog.Builder download = new AlertDialog.Builder(act);
        download.setTitle(title);
        download.setMessage(message);
        download.setPositiveButton(Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i){
                // TODO Auto-generated method stub
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                try{
                    act.startActivity(in);
                }catch(ActivityNotFoundException anfe){

                }
            }
        });

        download.setNegativeButton(No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
            }
        });
        return download.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {
        super.onActivityResult(requestCode, resultCode, in);
        // TODO Auto-generated method stub
        if(requestCode ==0){
            if(resultCode == RESULT_OK){
                String contents = in.getStringExtra("SCAN_RESULT");
                String format =  in.getStringExtra("SCAN_RESULT_FORMAT") ;
                //Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                // toast.show();

                barcodeSearch.setText(contents);

                //  selectQuery = "SELECT  * FROM products where barcode like '"+ contents  +"' and salesmanid = '"+ SalesmanID +"' ORDER BY description";

                //   try{
                // 	  loadlistofproducts();
                //   }catch (Exception e){
                // 	  Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                //   }
            }
        }
    }

    // updated
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    // Handle the selected date (selectedYear, selectedMonth, selectedDay)
                    Toast.makeText(getApplicationContext(),
                            "Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear,
                            Toast.LENGTH_SHORT).show();
                },
                year, month, day
        );

        datePickerDialog.show();
    }
    // updated

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        expirydate.setText(new StringBuilder().append(month).append("-")
                .append(day).append("-").append(year));
    }

    private void loadtempinvoit(){

        Detailsinvoice = Detailsinvoice + "\n";
        Cursor ctemp = db.rawQuery("select * from TempInvoice2", null);
        ctemp.moveToFirst();
        int cnt =  ctemp.getCount();
        int toqty = 0;

        if(cnt > 0){

            String qtytemp = "";
            String desctemp = "";
            String umdtl = "";
            String expirydtl = "";

            do {
                // 	lables.add(ctemp.getString(1));

                //tinvamount = tinvamount + ctemp.getFloat(5);
                toqty = toqty + ctemp.getInt(4);

                String sb = "";
                qtytemp = qtytemp + ctemp.getString(4) + "\n";
                Detailsinvoice = Detailsinvoice + ctemp.getString(4);
                int valqtylenth = 0;

                String latval = ctemp.getString(2);
                valqtylenth = latval.length();
                for(int i = 0; i < 55; i++)
                {
                    if(valqtylenth < 54){
                        if(valqtylenth - 1 >= i){
                            char descr =  latval.charAt(i);
                            sb = sb + descr;
                        }else{
                            //sb = sb + " ";
                        }
                    }else if(valqtylenth >54){
                        if(i < 54){
                            if(valqtylenth - 1 >= i){
                                char descr =  latval.charAt(i);
                                sb = sb + descr;
                            }else{
                                //sb = sb + " ";
                            }
                        }else{
                            //sb = sb + " ";
                        }
                    }
                } // end for loop description

                desctemp = desctemp + sb + "\n";

                sb = "";
                umdtl = umdtl + ctemp.getString(3) + "\n";
                expirydtl = expirydtl + ctemp.getString(5) + "\n";

            } while (ctemp.moveToNext());

            qty1.setText(qtytemp);
            prodesc.setText(desctemp);
            umdtls.setText(umdtl);
            expirydtls.setText(expirydtl);

            totalinvqty.setText(String.valueOf(toqty));

            // DecimalFormat formatter = new DecimalFormat("###,###,###.##");
            //  totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

        }else{
            qty1.setText("");
            prodesc.setText("");
            umdtls.setText("");
            expirydtls.setText("");

            totalinvqty.setText("");
            //    totalinvamount.setText("");
        }
    }

    @Override
    public void onClick(View v) {  //
        // TODO Auto-generated method stub

        // inser broken seal

        tinvamount = 0;
        theMessage = "";

        validation = "";

        theMessage = validation;
        Detailsinvoice = "";

        prefixval = "";
        Invnumberval = "";

        checkvalidation = true;

        if(v.getId() == R.id.btexpirydatenewvr) {

            // updated
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Handle the selected date
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        Toast.makeText(getApplicationContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    },
                    year, month, day
            );

            datePickerDialog.show();

            // showDialog(999);
            //Toast.makeText(getApplicationContext(), "ca",
            //        Toast.LENGTH_SHORT)
            //   .show();
        }

        if(v.getId() == R.id.btcleardetailsvr){

            AlertDialog.Builder builder = new AlertDialog.Builder(VansellingReturnNew.this);
            builder.setMessage("Are you sure you want to Clear the details?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    db.execSQL("delete from TempInvoice2 ; ");
                    loadtempinvoit();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });

            AlertDialog Alert = builder.create();
            Alert.show();
        }

        if(v.getId() == R.id.btscanbarcode){

            try{
                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "PRODUCT_MODE");

                startActivityForResult(in,0);

            } catch (ActivityNotFoundException e){
                // TODO: handle exception
                showDialog(VansellingReturnNew.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if(v.getId() == R.id.btadditemvr){

            try{
                Intent intent = new Intent(VansellingReturnNew.this, AddItem.class);

                String tempstring = SalesmanID;    		// cust type
                intent.putExtra("salesmanidvalueadditem",tempstring);

                startActivity(intent);

            } catch (ActivityNotFoundException e){
                // TODO: handle exception
                // showDialog(VansellingReturnNew.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if(v.getId() == R.id.btscanbarcodecs){

            try{
                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "INDUSTRIAL_MODE");

                startActivityForResult(in,0);

            } catch (ActivityNotFoundException e){
                // TODO: handle exception
                showDialog(VansellingReturnNew.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if(v.getId() == R.id.btdeleteitemnewvr){ //delete item

            db.execSQL("delete from TempInvoice2 where description like '"+ spproductnew.getSelectedItem().toString() +"';");

            int toqty = 0;

            Toast.makeText(getApplicationContext(), spproductnew.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            //	Detailsinvoice = "QTY                    Description                        Unitprice     T-Amount  " + "\n";

            Detailsinvoice = Detailsinvoice + "\n";
            Cursor ctemp = db.rawQuery("select * from TempInvoice2", null);
            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            if(cnt > 0){

                String qtytemp = "";
                String desctemp = "";
                String umdtl = "";
                String expirydtl = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    //tinvamount = tinvamount + ctemp.getFloat(5);
                    toqty = toqty + ctemp.getInt(4);

                    String sb = "";
                    qtytemp = qtytemp + ctemp.getString(4) + "\n";
                    Detailsinvoice = Detailsinvoice + ctemp.getString(4);
                    int valqtylenth = 0;

                    String latval = ctemp.getString(2);
                    valqtylenth = latval.length();
                    for(int i = 0; i < 55; i++)
                    {
                        if(valqtylenth < 54){
                            if(valqtylenth - 1 >= i){
                                char descr =  latval.charAt(i);
                                sb = sb + descr;
                            }else{
                                //sb = sb + " ";
                            }
                        }else if(valqtylenth > 54){
                            if(i < 54){
                                if(valqtylenth - 1 >= i){
                                    char descr =  latval.charAt(i);
                                    sb = sb + descr;
                                }else{
                                    //sb = sb + " ";
                                }
                            }else{
                                //sb = sb + " ";
                            }
                        }
                    } // end for loop description

                    desctemp = desctemp + sb + "\n";

                    sb = "";
                    umdtl = umdtl + ctemp.getString(3) + "\n";
                    expirydtl = expirydtl + ctemp.getString(5) + "\n";

                } while (ctemp.moveToNext());

                qty1.setText(qtytemp);
                prodesc.setText(desctemp);
                umdtls.setText(umdtl);
                expirydtls.setText(expirydtl);

                totalinvqty.setText(String.valueOf(toqty));

                // DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                //  totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

            }else{
                qty1.setText("");
                prodesc.setText("");
                umdtls.setText("");
                expirydtls.setText("");

                totalinvqty.setText("");
                //    totalinvamount.setText("");
            }
        }

        if(v.getId() == R.id.btadddetailsnewinvvr){  // add item

            float purchaseprice = 0;

            //	if("VAN-SELLING".equals(transinv.getText().toString())){
            selectQuery = "select * from products where description like '"+ spproductnew.getSelectedItem().toString() +"'  and salesmanid = '"+ SalesmanID +"' ";
            //   }else{
            //   	selectQuery = "select * from products where description like '"+ spproductnew.getSelectedItem().toString() +"' ";
            //   }

            Cursor c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            int cnt =  c.getCount();
            if(cnt > 0){  // check if product is exist
                //purchaseprice = Float.parseFloat(c.getString(2));
                //int invleft = c.getInt(4); // quantity left
                if("".equalsIgnoreCase(csquantity.getText().toString())){
                }else{
                    quantity.setText("1");
                }

                tempstring = quantity.getText().toString();   // check if have quantiy input
                if("".equals(tempstring)){
                    Toast.makeText(getApplicationContext(), "Please input Quantity!",Toast.LENGTH_LONG).show();
                }else{

                    Cursor c3 = db.rawQuery("select * from TempInvoice2 where description like '"+ spproductnew.getSelectedItem().toString() +"'", null);
                    c3.moveToFirst();
                    cnt =  c3.getCount();
                    if(cnt > 3){   // check if item is already in the list
                        Toast.makeText(getApplicationContext(), "Item is Already in the list!",Toast.LENGTH_LONG).show();
                    }else{
                        String NumID = c.getString(0);       // product id
                        String productDescription = c.getString(1); // product description
                        String um = c.getString(5);       // unit of measure

                        Integer qty = Integer.parseInt(tempstring); // check if quantity input is not zero
                        if(qty < 1){
                            Toast.makeText(getApplicationContext(), "Zero Quantity not Allowed!",Toast.LENGTH_LONG).show();
                        }else{

                            Cursor c3E = db.rawQuery("select * from TempInvoice2 ", null);
                            c3E.moveToLast();
                            cnt =  c3E.getCount();
                            if(cnt > 510){
                                Toast.makeText(getApplicationContext(), "Only 50 Sku per Syntax!",Toast.LENGTH_LONG).show();
                            }else{

                                float totalinvoice = 0;
                                //	float cred = 0;

                                //	float tamount = 0;

                                //	float PRICEORDER = 0;
                                float QTYORDER = 0;

                                String csorder = "" + csquantity.getText().toString();

                                boolean cspricecheck = true;

                                tempstring = expirydate.getText().toString();
                                if("1/01/2018".equalsIgnoreCase(tempstring)){
                                    Toast.makeText(getApplicationContext(), "Please Input Expiration date!",Toast.LENGTH_LONG).show();
                                    cspricecheck = false;
                                }

                                if(cspricecheck){

                                    if("".equals(csorder)){
                                        // tamount = Float.parseFloat(quantity.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                        // PRICEORDER = Float.parseFloat(unitprice.getText().toString());
                                        QTYORDER =Float.parseFloat(quantity.getText().toString());
                                    }else{
                                        // tamount = Float.parseFloat(csquantity.getText().toString()) * Float.parseFloat(csunitprice.getText().toString());
                                        // PRICEORDER =  Float.parseFloat(csunitprice.getText().toString()) / Float.parseFloat(csfactor.getText().toString());
                                        QTYORDER =Float.parseFloat(csquantity.getText().toString()) * Float.parseFloat(csfactor.getText().toString());
                                        quantity.setText("0");
                                    }

                                    //	Cursor c4 = db.rawQuery("select sum(totalamount) from TempInvoice ", null);
                                    //    c4.moveToFirst();
                                    //   cnt =  c4.getCount();
                                    //    if(cnt > 0){
                                    //    	totalinvoice = tamount + c4.getFloat(0);
                                    ///   	cred = Float.parseFloat(tvavailcredit.getText().toString());
                                    //   }else{
                                    //   	totalinvoice = tamount;
                                    //   }
                                    // end check credit limit

                                    //  String trapTrans = "";
                                    //	/if("VAN-SELLING".equals(transinv.getText().toString())){
                                    //	trapTrans = "V";
                                    //	}else {
                                    //	trapTrans = "B";
                                    //	totalinvoice = 0;
                                    //	cred = 1;
                                    //}

                                    // save to invoice temp
                                    db.execSQL("Insert into TempInvoice2 values('RETV','"+ NumID +"','"+ productDescription +"','"+ um +"',"+ QTYORDER +",'"+ expirydate.getText().toString() +"');");

                                    Cursor ctemp = db.rawQuery("select * from TempInvoice2 order by description", null);
                                    ctemp.moveToFirst();
                                    cnt =  ctemp.getCount();
                                    if(cnt > 0){

                                        StringBuilder qtytemp = new StringBuilder();
                                        String desctemp = "";
                                        String umdtl = "";
                                        String expirydtl = "";

                                        Integer toqty = 0;
                                        do {
                                            // lables.add(ctemp.getString(1));

                                            // tinvamount = tinvamount + ctemp.getFloat(5);  // total amount per item
                                            toqty = toqty + ctemp.getInt(4);				// quantity

                                            StringBuilder sb = new StringBuilder();
                                            String desc = ctemp.getString(4);

                                            // Toast.makeText(getApplicationContext(), desc,Toast.LENGTH_LONG).show();

                                            qtytemp.append(desc).append("\n");
                                            // Detailsinvoice = Detailsinvoice + ctemp.getString(4);
                                            int valqtylenth;

                                            String latval = ctemp.getString(2);		// product description
                                            valqtylenth = latval.length();			// length of product description
                                            for(int i = 0; i < 55; i++)
                                            {
                                                if(valqtylenth < 54) {
                                                    char descr = 0;
                                                    if (valqtylenth - 1 >= i) {
                                                        descr = latval.charAt(i);
                                                        sb.append(descr);
                                                    } else {
                                                        //sb = sb + " ";
                                                        Log.d("product description", "Appending char: " + descr + " at index: " + i);
                                                    }
                                                }else if(valqtylenth > 54){
                                                    if(i < 54) {
                                                        char descr = 0;
                                                        if (valqtylenth - 1 >= i) {
                                                            descr = latval.charAt(i);
                                                            sb.append(descr);
                                                        } else {
                                                            //sb = sb + " ";
                                                            Log.d("product description", "Appending char: " + descr + " at index: " + i);
                                                        }
                                                    }else{
                                                        //sb = sb + " ";
                                                        Log.d("product description", "Padding space at index: " + i);
                                                    }
                                                }
                                            } // end for loop description

                                            desctemp = desctemp + sb + "\n";

                                            sb = new StringBuilder();
                                            umdtl = umdtl + ctemp.getString(3) + "\n";
                                            expirydtl = expirydtl + ctemp.getString(5) + "\n";
                                            // Toast.makeText(getApplicationContext(), "trap!" + ctemp.getFloat(5)  ,Toast.LENGTH_LONG).show();

                                        } while (ctemp.moveToNext());
                                        // Toast.makeText(getApplicationContext(), "trap!"  ,Toast.LENGTH_LONG).show();

                                        ctemp.close();
                                        qty1.setText(qtytemp.toString());
                                        prodesc.setText(desctemp);
                                        umdtls.setText(umdtl);
                                        expirydtls.setText(expirydtl);

                                        totalinvqty.setText(String.valueOf(toqty));
                                        DecimalFormat formatter = new DecimalFormat("###,###,###.##");

                                        // totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));
                                    }

                                    // quantity.setText("");
                                    csquantity.setText("");
                                    Toast.makeText(getApplicationContext(), "Add to Dtls!",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        }

        if(v.getId() == R.id.btsendsavevr){  // save send

            AlertDialog.Builder builder = new AlertDialog.Builder(VansellingReturnNew.this);
            builder.setMessage("Are you sure you want Send and Save this Transaction?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    // MainActivity.this.finish();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy", Locale.getDefault());
                    String currentDateandTime1 = sdf1.format(new Date());
                    SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                    String datetimesyntaxval = datetimesyntax.format(new Date());
                    SimpleDateFormat datetimesyntax2 = new SimpleDateFormat("MMddyy", Locale.getDefault());
                    String datetimesyntaxval2 = datetimesyntax2.format(new Date());

                    // Toast.makeText(getApplicationContext(), "" + datetimesyntax,Toast.LENGTH_LONG).show();

                    String refid = "RETV" + SalesmanID ;

                    @SuppressLint("Recycle")
                    Cursor invquery = db.rawQuery("Select distinct(refid) from Returndtl ", null);

                    invquery.moveToFirst();
                    int countrec =  invquery.getCount();   // check if invoice already in used!
                    if(countrec > 0){
                        countrec = countrec + 1;
                    }else{
                        countrec = 1;
                    }

                    refid = refid + datetimesyntaxval2 + " " +  countrec;

                    // float tempamout = Float.parseFloat(tvavailcredit.getText().toString());

                    @SuppressLint("Recycle")
                    Cursor ctemp = db.rawQuery("select sum(qty) from TempInvoice2 order by description", null);
                    ctemp.moveToFirst();
                    int cnt =  ctemp.getCount();
                    if(cnt > 0){
                        tinvamount = 1;
                    }else{
                        tinvamount = 0;
                    }

//                    String trapTrans = ""; original
                    String trapTrans;

                    if("VAN RETURN BAD STOCK".equals(spreturntype.getSelectedItem().toString())){
                        trapTrans = "B";
                    }else {
                        trapTrans = "G";
                    }

                    if(tinvamount == 0){ 	// check if have details
                        Toast.makeText(getApplicationContext(), "Unable to save no details!",Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }else{
                        checkvalidation = true;
                    }

                    if(checkvalidation){
                        // load and save the invoice details
                        Detailsinvoice = "";

                        @SuppressLint("Recycle")
                        Cursor invtemp = db.rawQuery("select * from TempInvoice2 order by description", null);
                        invtemp.moveToFirst();
                        int cnt2 =  invtemp.getCount();
                        if(cnt2 > 0){

//                            String Tempsendsyntax = ""; original
                            String Tempsendsyntax;

                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());
                            float amtt = 0;
                            float tinvamount= 0;
                            String Productlist = "";

                            int countmsg = 0;
//                            int valqtylenth = 0;
                            int valqtylenth;

                            String Outlettype = "";
                            String SyntaxCodeDept = "";

                            if("VAN RETURN BAD STOCK".equals(spreturntype.getSelectedItem().toString())){
                                trapTrans = "B";
                            }else {
                                trapTrans = "G";
                            }

                            tempstring = spoutlet.getSelectedItem().toString();
                            if("LUBRICANTS".equals(tempstring)){
                                Outlettype = "L";
                                SyntaxCodeDept = "VRET";
                            }else if("AUTO-SUPPLY".equals(tempstring)){
                                Outlettype = "A";
                                SyntaxCodeDept = "VRET";
                            }else if("AGRICHEM".equals(tempstring)){
                                Outlettype = "AG";
                                SyntaxCodeDept = "VRET";
                            }else if("CENTURY".equals(tempstring)){
                                Outlettype = "CC";
                                if("MARS2".equals(Databasename)){
                                    SyntaxCodeDept = "VRET2";
                                }else{
                                    SyntaxCodeDept = "VRET";
                                }
                            }else if("URC".equals(tempstring)){
                                Outlettype = "UC";
                                SyntaxCodeDept = "VRET";
                            }else if("URC-BEV".equals(tempstring)){
                                Outlettype = "UB";
                                SyntaxCodeDept = "VRET";
                            }

                            // SyntaxCodeDept = "VRET";

                            Detailsinvoice = SyntaxCodeDept +  "!" + SalesmanID + "!" + refid  + "!"  + trapTrans + "!" + datetimesyntaxval + "!";
                            Detailsinvoice1 = SyntaxCodeDept + "1" +  "!" + refid + "!" ;
                            Detailsinvoice2 = SyntaxCodeDept + "2" +  "!" + refid + "!" ;
                            Detailsinvoice3 = SyntaxCodeDept + "3" +  "!" + refid + "!" ;
                            Detailsinvoice4 = SyntaxCodeDept + "4" +  "!" + refid + "!" ;
                            Detailsinvoice5 = SyntaxCodeDept + "5" +  "!" + refid + "!" ;
                            Detailsinvoice6 = SyntaxCodeDept + "6" +  "!" + refid + "!" ;
                            Detailsinvoice7 = SyntaxCodeDept + "7" +  "!" + refid + "!" ;
                            Detailsinvoice8 = SyntaxCodeDept + "8" +  "!" + refid + "!" ;
                            Detailsinvoice9 = SyntaxCodeDept + "9" +  "!" + refid + "!" ;
                            Detailsinvoice10 = SyntaxCodeDept + "10" +  "!" + refid + "!" ;
                            Detailsinvoice11 = SyntaxCodeDept + "11" +  "!" + refid + "!" ;
                            Detailsinvoice12 = SyntaxCodeDept + "12" +  "!" + refid + "!" ;
                            Detailsinvoice13 = SyntaxCodeDept + "13" +  "!" + refid + "!" ;
                            Detailsinvoice14 = SyntaxCodeDept + "14" +  "!" + refid + "!" ;
                            Detailsinvoice15 = SyntaxCodeDept + "15" +  "!" + refid + "!" ;
                            Detailsinvoice16 = SyntaxCodeDept + "16" +  "!" + refid + "!" ;
                            Detailsinvoice17 = SyntaxCodeDept + "17" +  "!" + refid + "!" ;
                            Detailsinvoice18 = SyntaxCodeDept + "18" +  "!" + refid + "!" ;
                            Detailsinvoice19 = SyntaxCodeDept + "19" +  "!" + refid + "!" ;
                            Detailsinvoice20 = SyntaxCodeDept + "20" +  "!" + refid + "!" ;
                            Detailsinvoice21 = SyntaxCodeDept + "21" +  "!" + refid + "!" ;
                            Detailsinvoice22 = SyntaxCodeDept + "22" +  "!" + refid + "!" ;
                            Detailsinvoice23 = SyntaxCodeDept + "23" +  "!" + refid + "!" ;
                            Detailsinvoice24 = SyntaxCodeDept + "24" +  "!" + refid + "!" ;

                            do {
                                // amtt = amtt + ctemp.getFloat(5);
                                // Cursor ctemp1 = db.rawQuery("select * from products where productid = '" + invtemp.getString(0) + "'", null);
                                // ctemp1.moveToFirst();
                                // cnt =  ctemp1.getCount();
                                // if(cnt > 0){
                                // update the inventory of the item
                                // int qtyleft = Integer.parseInt(ctemp1.getString(4)) - Integer.parseInt(ctemp.getString(4));
                                //}

                                tinvamount = tinvamount + invtemp.getFloat(5);
                                if(countmsg <= 0){
                                    Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice = Detailsinvoice + invtemp.getString(1) +  ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 1){
                                    Tempsendsyntax = Detailsinvoice1 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 2){
                                    Tempsendsyntax = Detailsinvoice2 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 3){
                                    Tempsendsyntax = Detailsinvoice3 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 4){
                                    Tempsendsyntax = Detailsinvoice4 + invtemp.getString(1) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 5){
                                    Tempsendsyntax = Detailsinvoice5 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 6){
                                    Tempsendsyntax = Detailsinvoice6 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 7){
                                    Tempsendsyntax = Detailsinvoice7 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 8){
                                    Tempsendsyntax = Detailsinvoice8 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 9){
                                    Tempsendsyntax = Detailsinvoice9 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 10){
                                    Tempsendsyntax = Detailsinvoice10 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 11){
                                    Tempsendsyntax = Detailsinvoice11 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 12){
                                    Tempsendsyntax = Detailsinvoice12 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 13){
                                    Tempsendsyntax = Detailsinvoice13 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 14){
                                    Tempsendsyntax = Detailsinvoice14 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 15){
                                    Tempsendsyntax = Detailsinvoice15 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 16){
                                    Tempsendsyntax = Detailsinvoice16 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 17){
                                    Tempsendsyntax = Detailsinvoice17 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 18){
                                    Tempsendsyntax = Detailsinvoice18 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 19){
                                    Tempsendsyntax = Detailsinvoice19 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){
                                        countmsg = countmsg + 1;
                                        Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 20){
                                    Tempsendsyntax = Detailsinvoice20 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){

                                        Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 21){
                                    Tempsendsyntax = Detailsinvoice21 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){

                                        Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 22){
                                    Tempsendsyntax = Detailsinvoice22 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){

                                        Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 23){
                                    Tempsendsyntax = Detailsinvoice23 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){

                                        Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }else if(countmsg <= 24){
                                    Tempsendsyntax = Detailsinvoice24 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;;
                                    valqtylenth =  Tempsendsyntax.length();
                                    if(valqtylenth > 160){

                                        Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1)  + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }else{
                                        Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":"  + invtemp.getString(4) + ":" + invtemp.getString(5) + "/" ;
                                    }
                                }

                                // Productlist =  Productlist + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;

                                // save invoice details
                                db.execSQL("Insert into ReturnDtl values" +
                                        "('" + refid + "','" +
                                        SalesmanID + "','" +  					// salesmanid
                                        invtemp.getString(1) +"'," + 			// productid
                                        invtemp.getInt(4) +",'" + 				// quantity
                                        invtemp.getString(5)+"','"+ 				// expiry date
                                        currentDateandTime +"','" +  		// invoice date
                                        datetimesyntaxval +"','" + 				// syntax date time
                                        Outlettype +"','" +						// out let
                                        trapTrans + "');");						// return type

                            } while (invtemp.moveToNext());
                            // end save details

                            // query receiver number
                            @SuppressLint("Recycle")
                            Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                            c1.moveToFirst();
                            cnt =  c1.getCount();
                            if(cnt > 0){
                                theMobNo = c1.getString(0);
                            }

                            //  db.execSQL("Update ScheduleCustomer set status = 'Y' WHERE customerid = '"+ custid.getText().toString() +"';");
                            //Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                            if(countmsg <= 0){
                                sendSMS(theMobNo,Detailsinvoice);
                            }else if(countmsg <= 1){
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                //	Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                                //	Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                            }
                            else if(countmsg <= 2){
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                            }else if(countmsg <= 3){
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                            }else if(countmsg <= 4){
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                            }else if(countmsg <= 5){
                                //    	Toast.makeText(getApplicationContext(), "sms 5" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                            }else if(countmsg <= 6){
                                //    	Toast.makeText(getApplicationContext(), "sms 6" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                            }else if(countmsg <= 7){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                            }else if(countmsg <= 8){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                            }else if(countmsg <= 9){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                            }else if(countmsg <= 10){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                            }else if(countmsg <= 11){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                            }else if(countmsg <= 12){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                            }else if(countmsg <= 13){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                            }else if(countmsg <= 14){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                            }else if(countmsg <= 15){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                            }else if(countmsg <= 16){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                            }else if(countmsg <= 17){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                            }else if(countmsg <= 18){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                            }else if(countmsg <= 19){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                            }else if(countmsg <= 20){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                                sendSMS(theMobNo,Detailsinvoice20);
                            }else if(countmsg <= 21){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                                sendSMS(theMobNo,Detailsinvoice20);
                                sendSMS(theMobNo,Detailsinvoice21);
                            }else if(countmsg <= 22){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                                sendSMS(theMobNo,Detailsinvoice20);
                                sendSMS(theMobNo,Detailsinvoice21);
                                sendSMS(theMobNo,Detailsinvoice22);
                            }else if(countmsg <= 23){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                                sendSMS(theMobNo,Detailsinvoice20);
                                sendSMS(theMobNo,Detailsinvoice21);
                                sendSMS(theMobNo,Detailsinvoice22);
                                sendSMS(theMobNo,Detailsinvoice23);
                            }else if(countmsg <= 24){
                                //   	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                sendSMS(theMobNo,Detailsinvoice);
                                sendSMS(theMobNo,Detailsinvoice1);
                                sendSMS(theMobNo,Detailsinvoice2);
                                sendSMS(theMobNo,Detailsinvoice3);
                                sendSMS(theMobNo,Detailsinvoice4);
                                sendSMS(theMobNo,Detailsinvoice5);
                                sendSMS(theMobNo,Detailsinvoice6);
                                sendSMS(theMobNo,Detailsinvoice7);
                                sendSMS(theMobNo,Detailsinvoice8);
                                sendSMS(theMobNo,Detailsinvoice9);
                                sendSMS(theMobNo,Detailsinvoice10);
                                sendSMS(theMobNo,Detailsinvoice11);
                                sendSMS(theMobNo,Detailsinvoice12);
                                sendSMS(theMobNo,Detailsinvoice13);
                                sendSMS(theMobNo,Detailsinvoice14);
                                sendSMS(theMobNo,Detailsinvoice15);
                                sendSMS(theMobNo,Detailsinvoice16);
                                sendSMS(theMobNo,Detailsinvoice17);
                                sendSMS(theMobNo,Detailsinvoice18);
                                sendSMS(theMobNo,Detailsinvoice19);
                                sendSMS(theMobNo,Detailsinvoice20);
                                sendSMS(theMobNo,Detailsinvoice21);
                                sendSMS(theMobNo,Detailsinvoice22);
                                sendSMS(theMobNo,Detailsinvoice23);
                                sendSMS(theMobNo,Detailsinvoice24);
                            }

                            db.execSQL("delete from TempInvoice2;");
                            qty1.setText("");
                            prodesc.setText("");
                            umdtls.setText("");
                            expirydtls.setText("");
                            expirydate.setText("1/01/2018");
                            totalinvqty.setText("0");
                            // totalinvamount.setText("0");
                            Detailsinvoice = "";

                            // Toast.makeText(getApplicationContext(),"" + Detailsinvoice ,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "No Details Found!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                // TODO Auto-generated method stub
                dialog.cancel();
            });

            AlertDialog Alert = builder.create();
            Alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //

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

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {

// original
//            if (location != null) {
//
//                locManager.removeUpdates(locListener);
//
//                longitude = ""+ location.getLongitude();
//                latitude =  ""+ location.getLatitude();
//                //	String altitiude =  ""+  location.getAltitude();
//                //	String accuracy = ""+ location.getAccuracy();
//                //	String time =  ""+ location.getTime();
//
//                // Toast.makeText(getApplicationContext(), "" + longitude + accuracy, Toast.LENGTH_LONG).show();
//
//                // theMobNo = phonenumber.getText().toString();
//
//                // theMessage = theMessage + "/" + latitude + "/" + longitude;
//
//                tvOlat.setText(latitude);
//                tvOlong.setText(longitude);
//
//                // progress.setVisibility(View.GONE);
//            }else{
//                Toast.makeText(getApplicationContext(), "GPS Not Enable or Cant Get Location!", Toast.LENGTH_LONG).show();
//            }

            locManager.removeUpdates(locListener);

            longitude = ""+ location.getLongitude();
            latitude = ""+ location.getLatitude();
            //	String altitiude =  ""+  location.getAltitude();
            //	String accuracy = ""+ location.getAccuracy();
            //	String time =  ""+ location.getTime();

            // Toast.makeText(getApplicationContext(), "" + longitude + accuracy, Toast.LENGTH_LONG).show();

            // theMobNo = phonenumber.getText().toString();

            // theMessage = theMessage + "/" + latitude + "/" + longitude;

            tvOlat.setText(latitude);
            tvOlong.setText(longitude);

            // progress.setVisibility(View.GONE);
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

    public void loadlistofproducts() {

        // Spinner Drop down elements
        lables = new ArrayList<>();

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lables.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spproductnew.setAdapter(dataAdapter);
    }

    // sends an SMS message to another device
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void sendSMS(String phoneNumber, String message) {

        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

            // when the SMS has been sent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(new BroadcastReceiver(){

                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "service",Toast.LENGTH_SHORT).show();
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
                }, new IntentFilter(SENT), Context.RECEIVER_EXPORTED);
            }

            // when the SMS has been delivered
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode())
                        {
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
                }, new IntentFilter(DELIVERED), Context.RECEIVER_EXPORTED);
            }

            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null); original
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            Toast.makeText(getApplicationContext(), "SMS Sent! " , Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!" + ", eRROR :" + e , Toast.LENGTH_LONG).show();
//            e.printStackTrace(); original
            Log.e("SMS_ERROR", "Failed to send SMS", e);
        }
    }
}
