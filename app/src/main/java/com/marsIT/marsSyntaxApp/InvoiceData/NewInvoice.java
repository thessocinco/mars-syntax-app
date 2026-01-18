package com.marsIT.marsSyntaxApp.InvoiceData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.BroadcastReceiver;
import android.content.Context;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.marsIT.marsSyntaxApp.Inventory.AddItem;
import com.marsIT.marsSyntaxApp.InvoiceAdapter.InvoiceAdapter;
import com.marsIT.marsSyntaxApp.InvoiceAdapter.InvoiceProducts;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.MainProgram.MainActivity;
import com.marsIT.marsSyntaxApp.Inventory.PaymentDetails;
import com.marsIT.marsSyntaxApp.R;

//@SuppressWarnings("deprecation")
public class NewInvoice extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    static final String SCAN = "com.google.zxing.client.android.SCAN";
    DatePickerDialog picker;
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
//    private String itemcode;
//    private String withtankloan;
    private String validation;
    private String selectQuery;
    private String longitude;
    private String latitude;
    private Spinner getprefix;
    private EditText getnum;
//    private EditText withtaxamount;
    private EditText osnumber;
    EditText inputSearch;
    EditText barcodeSearch;
    private EditText eddeldate;
    private EditText editRemarks;
    private EditText quantity;
    private EditText csquantity;
    private TextView custid;
    private TextView custname;
    private List<String> lables;
    private List<String> labless;
    private SQLiteDatabase db;
    private TextView tvOlat;
    private TextView tvOlong;
//    private TextView creditlimit;
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
    private TextView qtyavailable;
    private TextView csum;
    private EditText unitprice;
    private EditText csunitprice;
//    private CheckBox withtax;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
//    private Spinner spctype;
    private Spinner spproductnew;
    private Spinner sptermssp;
    private Spinner spdaystermsp;
    private Spinner spoutlet;
    private Spinner spcategory;
    private Button btaddtoinvoice;
    private Button btsendsave;
    private Button btdeleteitem1;
//    private Button btbsinsert;
//    private Button btbsdelete;
    private Button btsendrequest;
    private Button btPayment;
    private Button btbarcode;
    private Button btcleardtl;
    private Button btcsbarcode;
    private Button btadditem;
    private boolean issave;
    private boolean TrapInvQty;
//    private float EndingInv;
    private Integer countpro;
//    private Integer countpro1;
    final Context context = this;

    // excel grid view
//    private TextView qty1;
//    private TextView description;
//    private TextView unitPrice;
//    private TextView totalAmount;
    // excel grid view

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
    private String Salesman_pss;
    private float tinvamount;
//    private boolean gps_enabled = false;
    private boolean loadactivty = true;
//    private boolean network_enabled = false;

    IntentFilter intentFilter;

    // excel grid view
    RecyclerView ProductItemRecycler;
    List<InvoiceProducts> invoiceProducts = new ArrayList<>();
    InvoiceAdapter adapter;
    // excel grid view

//    private BroadcastReceiver smsSentReceiver;
//    private BroadcastReceiver smsDeliveredReceiver;

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // display the SMS received in the TextView

            String syntaxsms = Objects.requireNonNull(intent.getExtras()).getString("sms"); // message received
            String cellphonenumber = intent.getExtras().getString("cellnumber");

            // Toast.makeText(getApplicationContext(), "" + cellphonenumber, Toast.LENGTH_LONG).show();

            Cursor queryvalidnumber = db.rawQuery("select * from validnumber where num = '" + cellphonenumber + "'", null);
            queryvalidnumber.moveToFirst();
            int cnt = queryvalidnumber.getCount();
            if (cnt > 0) {

                String NumberOwner = queryvalidnumber.getString(1);

                // String latval = syntaxsms;
                String stchar = "";
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
                            Toast.makeText(getApplicationContext(), "Price Overide : " + c2.getString(1) + " by : " + NumberOwner, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect Sysntax,Item not found", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Update Receiver number :" + receivernum, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Successfully add the valid number :" + receivernum, Toast.LENGTH_LONG).show();
                    } else if ("UNLOCKXX".equals(stchar)) {
                        String receivernum = "";
                        // String name = "";
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

                        db.execSQL("UPDATE CUSTOMERS SET LOCK = 'U' WHERE CID ='" + receivernum + "';");
                        Toast.makeText(getApplicationContext(), "Successfully UNLOCK THE CUSTOMER BY :" + NumberOwner, Toast.LENGTH_LONG).show();
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
                                    creditlimit = creditlimit + descr; // credit line

                                } else if (counter == 3) {
                                    balcredit = balcredit + descr; // bacredit

                                } else if (counter == 4) {
                                    ocheck = ocheck + descr; // otcheck
                                } else if (counter == 5) {
                                    CreditStatus1 = CreditStatus1 + descr; // Credit Status
                                }
                            }
                        }

                        db.execSQL("Update customers set creditline = '" + Float.parseFloat(creditlimit) + "',balcredit = '" + balcredit + "',ocheck = '" + ocheck + "',CreditStatus = '" + CreditStatus1 + "' where cid = '" + customercode + "';");
                        Toast.makeText(getApplicationContext(), "Successfully Update New Credit line : " + customercode, Toast.LENGTH_LONG).show();

                        float ac = 0;

                        Cursor c1 = db.rawQuery("select * from customers where cid = '" + custid.getText().toString() + "'", null);
                        c1.moveToFirst();
                        int cnt1 = c1.getCount();
                        Toast.makeText(getApplicationContext(), "Updated Customer Credit line : " + custid.getText().toString(), Toast.LENGTH_LONG).show();

                        if (cnt1 > 0) {

                            tvcreditlimit.setText("" + Float.parseFloat(c1.getString(35))); // credit line
                            tvbalcredit.setText("" + Float.parseFloat(c1.getString(36))); // not paid invoice
                            tvocheck.setText("" + c1.getString(37)); // out standing
                            ac = Float.parseFloat(c1.getString(35)) - (Float.parseFloat(c1.getString(37)) + Float.parseFloat(c1.getString(36)));
                            tvavailcredit.setText("" + ac);

                            termstatus.setText("" + c1.getString(38)); // credit status
                        }
                    }
                } // End of valid number check for syntax access
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() { // Register the Receiver
        Log.d("New Invoice", "onResume: Registering the receiver");
        registerReceiver(notificationReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("New Invoice", "onPause: Unregistering the receiver");
        unregisterReceiver(notificationReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newinvoice);

        try {

            setupToolbar("New Invoice");
            // Force menu to appear
            invalidateOptionsMenu();

            // Handle back press using OnBackPressedDispatcher
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(NewInvoice.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            eddeldate = findViewById(R.id.etdeldate);
            // eddeldate.setInputType(InputType.TYPE_NULL);

            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            spproductnew = findViewById(R.id.spproductnew);
            sptermssp = findViewById(R.id.sptermsnew);
            sptermssp.setSelection(2);
            spdaystermsp = findViewById(R.id.spdaystermnew);
            spoutlet = findViewById(R.id.spoutletnewinv);
            spcategory = findViewById(R.id.spcategorynewinv);
//            spcustomer = findViewById(R.id.spcustomerlist);

            db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);

            getnum = findViewById(R.id.etinvoicenumber);
            getnum.setText(getIntent().getExtras().getString("invnumber").toUpperCase());

            transinv = findViewById(R.id.tvtransinv);
            transinv.setText(getIntent().getExtras().getString("transinv").toUpperCase());

            qtyavailable = findViewById(R.id.tvqtyavailable);

            getprefix = findViewById(R.id.spprifix);
            // tempstring = "";
            // if("MCRV".equals(tempstring)){
            getprefix.setSelection(0);

            btPayment = findViewById(R.id.btpaymentnew);
            btPayment.setOnClickListener(this);

            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                getprefix.setSelection(2);
                btPayment.setEnabled(true);
            } else {
                getprefix.setSelection(1);
                getnum.setText(getIntent().getExtras().getString("invnumber").toUpperCase());
                btPayment.setEnabled(false);
            }

            SalesmanID = getIntent().getExtras().getString("salesmanidvalue").toUpperCase();

            Salesman_pss = "";
            Cursor getRouteCode = db.rawQuery("SELECT * FROM SalesmanInstallValue where SalesmanID = '" + SalesmanID + "'", null);
            getRouteCode.moveToFirst();
            int cnt1_rec = getRouteCode.getCount();
            if (cnt1_rec > 0) {
                Salesman_pss = getRouteCode.getString(2);
                Salesman_pss = Salesman_pss.substring(0, 4);
            }

            tvcreditlimit = findViewById(R.id.tvcreditlimitval);
            tvbalcredit = findViewById(R.id.tvbalcreditval);
            tvocheck = findViewById(R.id.tvOutSCheckval);
            tvavailcredit = findViewById(R.id.tvavailblecreditval);

            termstatus = findViewById(R.id.tvtermstatus);
            termstatus.setText(getIntent().getExtras().getString("termstatus").toUpperCase());

            tvcreditlimit.setText(getIntent().getExtras().getString("creditinv").toUpperCase());
            tvbalcredit.setText(getIntent().getExtras().getString("balcreditinv").toUpperCase());
            tvocheck.setText(getIntent().getExtras().getString("ocheckinv").toUpperCase());

            Databasename = getIntent().getExtras().getString("Databasename").toUpperCase();
            Departmentcode = getIntent().getExtras().getString("Departmentcode").toUpperCase();

            float ac = 0;

            ac = Float.parseFloat(getIntent().getExtras().getString("creditinv").toUpperCase()) - (Float.parseFloat(getIntent().getExtras().getString("balcreditinv").toUpperCase()) + Float.parseFloat(getIntent().getExtras().getString("ocheckinv").toUpperCase()));
            String valac = "" + ac;
            tvavailcredit.setText("" + valac);

            issave = false;
            db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
            // tablename
            db.execSQL("CREATE TABLE IF NOT EXISTS TempInvoice" +
                    "(Refid VARCHAR," +
                    "productid VARCHAR," +
                    "Description Varchar," +
                    "Unitprice FLoat," +
                    "Qty integer," +
                    "TotalAmount Float); ");

            // db.execSQL("delete from TempInvoice ; ");

            String ProductQuery; // String ProductQuery = ""; - original

            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                ProductQuery = "select * from products where salesmanid like '" + SalesmanID + "' order by description ";
            } else {
                ProductQuery = "select * from products order by description ";
            }
            Cursor c1 = db.rawQuery(ProductQuery, null);
            c1.moveToFirst();
            int cnt1 = c1.getCount();
            if (cnt1 > 0) {
                lables = new ArrayList<String>();
                if (c1.moveToFirst()) {
                    do {
                        lables.add(c1.getString(1));
                    } while (c1.moveToNext());
                }

                c1.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spproductnew.setAdapter(dataAdapter);
            }

            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                ProductQuery = "select distinct(outlet) as ou from products WHERE SALESMANID = '" + SalesmanID + "'order by description";
            } else {
                ProductQuery = "select distinct(outlet) as ou from products order by description";
            }

            // for out loop
            Cursor c3 = db.rawQuery(ProductQuery, null);
            c3.moveToFirst();
            int cntoutlet = c3.getCount();
            if (cntoutlet > 0) {

                lables = new ArrayList<>();
                if (c3.moveToFirst()) {
                    do {
                        tempstring = c3.getString(0);

                        if ("L".equals(tempstring)) {
                            lables.add("LUBRICANTS");
                        } else if ("A".equals(tempstring)) {
                            lables.add("AUTO-SUPPLY");
                        } else if ("AG".equals(tempstring)) {
                            lables.add("AGRICHEM");
                        } else if ("CC".equals(tempstring)) {
                            lables.add("CENTURY");
                        } else if ("PL".equals(tempstring)) {
                            lables.add("PEERLESS");
                        } else if ("UC".equals(tempstring)) {
                            lables.add("URC");
                        } else if ("UB".equals(tempstring)) {
                            lables.add("URC-BEV");
                        } else if ("MI".equals(tempstring)) {
                            lables.add("MONTOSCO");
                        } else if ("GT".equals(tempstring)) {
                            lables.add("GTAM");
                        } else if ("EQ".equals(tempstring)) {
                            lables.add("EQ");
                        } else if ("LY".equals(tempstring)) {
                            lables.add("LAMOIYAN");
                        } else if ("BL".equals(tempstring)) {
                            lables.add("BELO");
                        } else if ("RM".equals(tempstring)) {
                            lables.add("RAM");
                        } else if ("ZS".equals(tempstring)) {
                            lables.add("ZESTAR");
                        } else if ("CL".equals(tempstring)) {
                            lables.add("COLUMBIA");
                        } else if ("KL".equals(tempstring)) {
                            lables.add("KOHL");
                        }
                    } while (c3.moveToNext());
                }

                c3.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spoutlet.setAdapter(dataAdapter);
            }

            spoutlet.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0) {
                        countpro = 1;
                    } else {

                        tempstring = spoutlet.getSelectedItem().toString();

                        if ("LUBRICANTS".equals(tempstring)) {
                            tempstring = "L";
                            outlettrans = "L";
                        } else if ("AUTO-SUPPLY".equals(tempstring)) {
                            tempstring = "A";
                            outlettrans = "A";
                        } else if ("AGRICHEM".equals(tempstring)) {
                            tempstring = "AG";
                            outlettrans = "AG";
                        } else if ("CENTURY".equals(tempstring)) {
                            tempstring = "CC";
                            outlettrans = "CC";
                        } else if ("PEERLESS".equals(tempstring)) {
                            tempstring = "PL";
                            outlettrans = "PL";
                        } else if ("URC".equals(tempstring)) {
                            tempstring = "UC";
                            outlettrans = "UC";
                        } else if ("URC-BEV".equals(tempstring)) {
                            tempstring = "UB";
                            outlettrans = "UB";
                        } else if ("MONTOSCO".equals(tempstring)) {
                            tempstring = "MI";
                            outlettrans = "MI";
                        } else if ("GTAM".equals(tempstring)) {
                            tempstring = "GT";
                            outlettrans = "GT";
                        } else if ("EQ".equals(tempstring)) {
                            tempstring = "EQ";
                            outlettrans = "EQ";
                        } else if ("LAMOIYAN".equals(tempstring)) {
                            tempstring = "LY";
                            outlettrans = "LY";
                        } else if ("BELO".equals(tempstring)) {
                            tempstring = "BL";
                            outlettrans = "BL";
                        } else if ("RAM".equals(tempstring)) {
                            tempstring = "RM";
                            outlettrans = "RM";
                        } else if ("ZESTAR".equals(tempstring)) {
                            tempstring = "ZS";
                            outlettrans = "ZS";
                        } else if ("COLUMBIA".equals(tempstring)) {
                            tempstring = "CL";
                            outlettrans = "CL";
                        } else if ("KOHL".equals(tempstring)) {
                            tempstring = "KL";
                            outlettrans = "KL";
                        }

                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            selectQuery = "Select * from products where outlet like '" + tempstring + "' and salesmanid = '" + SalesmanID + "'  order by description ";
                        } else {
                            selectQuery = "Select * from products where outlet like '" + tempstring + "' order by description ";
                        }
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                ProductQuery = "select distinct(producttype) as ous from products WHERE salesmanid = '" + SalesmanID + "'  order by producttype";
            } else {
                ProductQuery = "select distinct(producttype) as ous from products order by producttype";
            }

            // for out loop
            Cursor c3t = db.rawQuery(ProductQuery, null);
            c3t.moveToFirst();
            int cntcat = c3t.getCount();
            if (cntcat > 0) {

                labless = new ArrayList<>();
                if (c3t.moveToFirst()) {
                    do {
                        tempstring = c3t.getString(0);
                        labless.add(tempstring);
                    } while (c3t.moveToNext());
                }

                c3t.close();
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labless);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spcategory.setAdapter(dataAdapter);
            }

            spcategory.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0) {
                        countpro = 1;
                    } else {

                        tempstring = spcategory.getSelectedItem().toString();
                        // selectQuery = "Select * from products where producttype like '" + tempstring + "' order by description ";

                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            selectQuery = "select * from products WHERE salesmanid = '" + SalesmanID + "' AND  producttype like '" + tempstring + "' order by description";
                        } else {
                            selectQuery = "select * from products order by producttype";
                        }
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch = findViewById(R.id.etqueryproduct);
            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // MainActivity.this.adapter.getFilter().filter(cs);
                    // Toast.makeText(getApplicationContext(), "" + cs, Toast.LENGTH_LONG).show();

                    // customername = cs.toString();
                    // Select All Query
                    tempstring = spoutlet.getSelectedItem().toString();
                    if ("LUBRICANTS".equals(tempstring)) {
                        outlettrans = "L";
                    } else if ("AUTO-SUPPLY".equals(tempstring)) {
                        outlettrans = "A";
                    } else if ("AGRICHEM".equals(tempstring)) {
                        outlettrans = "AG";
                    } else if ("CENTURY".equals(tempstring)) {
                        outlettrans = "CC";
                    } else if ("LAMOIYAN".equals(tempstring)) {
                        outlettrans = "LY";
                    } else if ("PEERLESS".equals(tempstring)) {
                        outlettrans = "PL";
                    } else if ("URC".equals(tempstring)) {
                        outlettrans = "UC";
                    } else if ("URC-BEV".equals(tempstring)) {
                        outlettrans = "UB";
                    } else if ("MONTOSCO".equals(tempstring)) {
                        outlettrans = "MI";
                    } else if ("GTAM".equals(tempstring)) {
                        outlettrans = "GT";
                    } else if ("EQ".equals(tempstring)) {
                        outlettrans = "EQ";
                    } else if ("BELO".equals(tempstring)) {
                        outlettrans = "BL";
                    } else if ("RAM".equals(tempstring)) {
                        outlettrans = "RM";
                    } else if ("ZESTAR".equals(tempstring)) {
                        outlettrans = "ZS";
                    } else if ("COLUMBIA".equals(tempstring)) {
                        outlettrans = "CL";
                    } else if ("KOHL".equals(tempstring)) {
                        outlettrans = "KL";
                    }

                    if ("VAN-SELLING".equals(transinv.getText().toString())) {
                        selectQuery = "SELECT  description FROM products where description like '%" + cs + "%'  and salesmanid = '" + SalesmanID + "' ORDER BY description";
                    } else {
                        selectQuery = "SELECT  description FROM products where description like '%" + cs + "%'  ORDER BY description";
                    }

                    Cursor c3 = db.rawQuery(selectQuery, null);
                    c3.moveToFirst();
                    int cntoutlet = c3.getCount();
                    if (cntoutlet > 0) {

                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            selectQuery = "SELECT * FROM products where description like '%" + cs + "%'  and salesmanid = '" + SalesmanID + "'    ORDER BY description";
                        } else {
                            selectQuery = "SELECT * FROM products where description like '%" + cs + "%'  ORDER BY description";
                        }

                        // selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                        try {
                            loadlistofproducts();
                        } catch (Exception e) {
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

            barcodeSearch = findViewById(R.id.etquerybarcode);
            barcodeSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    // MainActivity.this.adapter.getFilter().filter(cs);
                    // Toast.makeText(getApplicationContext(), "" + cs, Toast.LENGTH_LONG).show();

                    // customername = cs.toString();
                    // Select All Query
                    tempstring = spoutlet.getSelectedItem().toString();
                    if ("LUBRICANTS".equals(tempstring)) {
                        outlettrans = "L";
                    } else if ("AUTO-SUPPLY".equals(tempstring)) {
                        outlettrans = "A";
                    } else if ("AGRICHEM".equals(tempstring)) {
                        outlettrans = "AG";
                    } else if ("CENTURY".equals(tempstring)) {
                        outlettrans = "CC";
                    } else if ("LAMOIYAN".equals(tempstring)) {
                        outlettrans = "LY";
                    } else if ("PEERLESS".equals(tempstring)) {
                        outlettrans = "PL";
                    } else if ("URC".equals(tempstring)) {
                        outlettrans = "UC";
                    } else if ("URC-BEV".equals(tempstring)) {
                        outlettrans = "UB";
                    } else if ("MONTOSCO".equals(tempstring)) {
                        outlettrans = "MI";
                    } else if ("GTAM".equals(tempstring)) {
                        outlettrans = "GT";
                    } else if ("EQ".equals(tempstring)) {
                        outlettrans = "EQ";
                    } else if ("BELO".equals(tempstring)) {
                        outlettrans = "BL";
                    } else if ("RAM".equals(tempstring)) {
                        outlettrans = "RM";
                    } else if ("ZESTAR".equals(tempstring)) {
                        outlettrans = "ZS";
                    } else if ("COLUMBIA".equals(tempstring)) {
                        outlettrans = "CL";
                    } else if ("KOHL".equals(tempstring)) {
                        outlettrans = "KL";
                    }

                    if ("VAN-SELLING".equals(transinv.getText().toString())) {
                        selectQuery = "SELECT  * FROM products where barcode = '" + cs + "' and  salesmanid = '" + SalesmanID + "' ORDER BY description";
                        //	Toast.makeText(getApplicationContext(), "van " + SalesmanID , Toast.LENGTH_LONG).show();
                    } else {
                        selectQuery = "SELECT  * FROM products where barcode like '" + cs + "'  ORDER BY description";
                    }

                    Cursor c3 = db.rawQuery(selectQuery, null);
                    c3.moveToFirst();
                    int cntoutlet = c3.getCount();
                    if (cntoutlet > 0) {
                    } else {

                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            selectQuery = "SELECT * FROM products where csbarcode like '%" + cs + "%'  and salesmanid = '" + SalesmanID + "'    ORDER BY description";
                        } else {
                            selectQuery = "SELECT * FROM products where csbarcode like '%" + cs + "%'  ORDER BY description";
                        }
                        // selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
                    }

                    try {
                        loadlistofproducts();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
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

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // excel gridview function
            ProductItemRecycler = findViewById(R.id.ProductItemRecycler);
            ProductItemRecycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new InvoiceAdapter(invoiceProducts);
            ProductItemRecycler.setAdapter(adapter);
            // excel gridview function

            osnumber = findViewById(R.id.etosnumbernewinv);

            totalinvqty = findViewById(R.id.tvtotalinvoiceqty);
            totalinvamount = findViewById(R.id.tvtotalinvoicamount);

            btsendsave = findViewById(R.id.btsendsave);
            btsendsave.setOnClickListener(this);

            custid = findViewById(R.id.tvcodenew);
            custname = findViewById(R.id.tvcustomernamenew);

            custid.setText(getIntent().getExtras().getString("cidinv"));
            custname.setText(getIntent().getExtras().getString("cnameinv").toUpperCase());

            um = findViewById(R.id.tvumnewinvoice);
            csum = findViewById(R.id.tvcsumnewinvoice);

            csfactor = findViewById(R.id.tvcsfactornewinvoice);

            countpro = 0;
            countpro = 0;

            quantity = findViewById(R.id.etquantitynew);
            csquantity = findViewById(R.id.etcsquantitynew);

            unitprice = findViewById(R.id.etunitprice);
            csunitprice = findViewById(R.id.etcsunitprice);

            unitprice.setEnabled(true);
            if ("UC".equals(Departmentcode)) {
                //	unitprice.setEnabled(false);
            } else if ("CC".equals(Departmentcode)) {
                //	unitprice.setEnabled(false);
            } else if ("UB".equals(Departmentcode)) {
                //	unitprice.setEnabled(false);
            } else if ("L".equals(Departmentcode)) {
                //	unitprice.setEnabled(false);
            } else if ("AG".equals(Departmentcode)) {
                //	unitprice.setEnabled(false);
            }

            spproductnew.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0) {
                        countpro = 1;
                    } else {
                        tempstring = spproductnew.getSelectedItem().toString();
                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            selectQuery = "Select * from products where description like '" + tempstring + "'  and salesmanid = '" + SalesmanID + "' ";
                        } else {
                            selectQuery = "Select * from products where description like '" + tempstring + "' ";
                        }

                        // selectQuery = "Select * from products where description like '"+ tempstring +"' ";
                        Cursor c1 = db.rawQuery(selectQuery, null);
                        c1.moveToFirst();
                        int cnt = c1.getCount();
                        if (cnt > 0) {
                            // Cursor c3 = db.rawQuery("Select * from itempriceoveride where itemcode like '"+ c1.getString(0) +"' and customerid = '"+ custid.getText().toString() +"'", null);
                            // c3.moveToFirst();
                            try {
                                if ("UC".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("CC".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("LY".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("BL".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("PL".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("UB".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("MI".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("GT".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("EQ".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("RM".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("ZS".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("CL".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("L".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("KL".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else if ("AG".equals(Departmentcode)) {
                                    unitprice.setText(c1.getString(2));
                                    // unitprice.setEnabled(false);
                                } else {
                                    unitprice.setText("0"); // price come from the syntax
                                    // unitprice.setEnabled(true);
                                }

                                if ("0".equals(unitprice.getText().toString())) {
                                    csfactor.setText(c1.getString(8));
                                } else {

                                    csfactor.setText(c1.getString(8));

                                    float cspriceunit = Float.parseFloat(csfactor.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                    DecimalFormat formatter = new DecimalFormat("#.00");
                                    csunitprice.setText(String.valueOf(formatter.format(cspriceunit)));
                                }

                                unitprice.setEnabled(true);
                                um.setText(c1.getString(5));

                                csum.setText(c1.getString(9)); // case unit measure

                                String TempChar = "" + c1.getString(5);

                                if (TempChar.equalsIgnoreCase("" + c1.getString(9))) {

                                    csquantity.setEnabled(false);
                                    csquantity.setText("");
                                } else {
                                    csquantity.setEnabled(true);
                                    csquantity.setText("");
                                }
                                TempChar = "" + csfactor.getText().toString();
                                if (TempChar.equalsIgnoreCase("0")) {
                                    csquantity.setEnabled(false);
                                    csquantity.setText("");
                                } else {
                                    if (TempChar.equalsIgnoreCase("0")) {
                                        csquantity.setEnabled(false);
                                        csquantity.setText("");
                                    } else if (TempChar.equalsIgnoreCase("1")) {
                                        csquantity.setEnabled(false);
                                        csquantity.setText("");
                                    } else {
                                        csquantity.setEnabled(true);
                                        csquantity.setText("");
                                    }
                                }
                                if ("VAN-SELLING".equals(transinv.getText().toString())) {
                                    GetInventory(c1.getString(0));
                                } else {
                                    qtyavailable.setText("0");
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            // spctype.setEnabled(false);
            spproductnew.setSelection(0);
            tempstring = spproductnew.getSelectedItem().toString();

            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                selectQuery = "Select * from products where description like '" + tempstring + "'  and salesmanid = '" + SalesmanID + "' ";
            } else {

                selectQuery = "Select * from products where description like '" + tempstring + "' ";
            }

            Cursor c5 = db.rawQuery(selectQuery, null);
            c5.moveToFirst();
            int cnt = c5.getCount();
            if (cnt > 0) {
                // Toast.makeText(getApplicationContext(), "not found pricelist!" + spctype.getSelectedItem().toString() + " " + c5.getString(0),Toast.LENGTH_LONG).show();
                // unitprice.setText("0");
                um.setText(c5.getString(5));

            }

            btsendrequest = findViewById(R.id.btreqcreditline);
            btsendrequest.setOnClickListener(this);

            btaddtoinvoice = findViewById(R.id.btadddetailsnewinv);
            btaddtoinvoice.setOnClickListener(this);

            btdeleteitem1 = findViewById(R.id.btdeleteitemnew);
            btdeleteitem1.setOnClickListener(this);

            btbarcode = findViewById(R.id.btscanbarcoden);
            btbarcode.setOnClickListener(this);

            btcsbarcode = findViewById(R.id.btscanbarcodencs);
            btcsbarcode.setOnClickListener(this);

            btcleardtl = findViewById(R.id.btcleardetailsnew);
            btcleardtl.setOnClickListener(this);

            btadditem = findViewById(R.id.btadditemnew);
            btadditem.setOnClickListener(this);

            SimpleDateFormat dateday = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String datedayVal = dateday.format(new Date());
            eddeldate.setText(datedayVal);

            eddeldate.setOnClickListener(v -> {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewInvoice.this, (view, year1, monthOfYear, dayOfMonth) -> eddeldate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
                picker.show();
            });

            if (loadactivty) {
                // loadtempinvoit();
                loadactivty = false;
            }
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            Log.e("ON CREATE ERROR", "onCreate: ", e);
        }
    } // End of onCreate method

    private Dialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence Yes, CharSequence No) {
        // TODO Auto-generated method stub
        AlertDialog.Builder download = new AlertDialog.Builder(act);
        download.setTitle(title);
        download.setMessage(message);
        download.setPositiveButton(Yes, (dialog, i) -> {
            // TODO Auto-generated method stub
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent in = new Intent(Intent.ACTION_VIEW, uri);
            try {
                act.startActivity(in);
            } catch (ActivityNotFoundException anfe) {

            }
        });

        download.setNegativeButton(No, (dialog, i) -> {
            // TODO Auto-generated method stub
        });
        return download.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {
        super.onActivityResult(requestCode, resultCode, in);
        // TODO Auto-generated method stub
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = in.getStringExtra("SCAN_RESULT");
                String format = in.getStringExtra("SCAN_RESULT_FORMAT");
                // Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                // toast.show();
                barcodeSearch.setText(contents);
            }
        }
    }

    private void loadtempinvoit() {
        // Make sure to clear the global list, NOT create a new one
        invoiceProducts.clear();
        int totalQty = 0;
        float totalAmount = 0f;

        Cursor ctemp = db.rawQuery("SELECT * FROM TempInvoice", null);
        if (ctemp != null && ctemp.moveToFirst()) {
            do {
                int qty = ctemp.getInt(4);
                String description = ctemp.getString(2);
                float unitPrice = ctemp.getFloat(3);
                float amount = ctemp.getFloat(5);

                totalQty += qty;
                totalAmount += amount;

                invoiceProducts.add(new InvoiceProducts(qty, description, unitPrice, amount));
            } while (ctemp.moveToNext());
            ctemp.close();
        }

        // Update totals
        if (invoiceProducts.size() > 0) {
            totalinvqty.setText(String.valueOf(totalQty));
            DecimalFormat formatter = new DecimalFormat("###,###,###.##");
            totalinvamount.setText(formatter.format(totalAmount));
        } else {
            totalinvqty.setText("");
            totalinvamount.setText("");
        }

        // Notify the adapter that the list has changed
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // inser broken seal

        tinvamount = 0;
        theMessage = "";

        validation = "";

        theMessage = validation;
        Detailsinvoice = "";

        prefixval = getprefix.getSelectedItem().toString();
        Invnumberval = getnum.getText().toString();

        checkvalidation = true;

        if (v.getId() == R.id.btscanbarcoden) {
            try {
                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "PRODUCT_MODE");

                startActivityForResult(in, 0);

            } catch (ActivityNotFoundException e) {
                // TODO: handle exception
                showDialog(NewInvoice.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if (v.getId() == R.id.btscanbarcodencs) {
            try {

                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "INDUSTRIAL_MODE");

                startActivityForResult(in, 0);

            } catch (ActivityNotFoundException e) {
                // TODO: handle exception
                showDialog(NewInvoice.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        try {
            if (v.getId() == R.id.btadditemnew) {
                if (SalesmanID == null || SalesmanID.trim().isEmpty()) {
                    Toast.makeText(this, "Salesman ID is missing.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Intent intent = new Intent(NewInvoice.this, AddItem.class);
                    intent.putExtra("salesmanidvalueadditem", SalesmanID);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "AddItem activity not found.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("ADDD ITEM ERRORR", "ERRORRRR TRAAAPPPEDDDDDD!!!!!", e);
//                e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", "ADD ITEM ERROR: ", e);
        }

        if (v.getId() == R.id.btcleardetailsnew) {

            AlertDialog.Builder builder = new AlertDialog.Builder(NewInvoice.this);
            builder.setIcon(R.drawable.mars_logo); // Add your logo
            builder.setTitle("Confirm Clear");     // Optional title
            builder.setMessage("Are you sure you want to clear the invoice details?");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", (dialog, which) -> {
                try {
                    // 1. Clear DB Table
                    db.execSQL("DELETE FROM TempInvoice");

                    // 2. Refresh UI by reloading data
                    loadtempinvoit(); // Re-fetch data and refresh RecyclerView

//                        // Clear adapter list
//                        invoiceProducts.clear(); // 'invoiceProducts' should be the list used in your adapter
//                        adapter.notifyDataSetChanged(); // 'invoiceAdapter' should be your adapter instance

                    // 3. Clear other related UI elements
                    totalinvqty.setText("");
                    totalinvamount.setText("");

                    Toast.makeText(getApplicationContext(), "Invoice details cleared.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error clearing invoice details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CLEAR_INVOICE_ERROR", "Exception while clearing TempInvoice", e);
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss(); // Just dismiss the dialog
            });

            builder.show(); // Display the dialog
        }

        if (v.getId() == R.id.btreqcreditline) { // request update credit line
            String trapTrans1 = "";
            if ("LUBRICANTS".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "L";
            } else if ("AUTO-SUPPLY".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "A";
            } else if ("AGRICHEM".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "AG";
            } else if ("CENTURY".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "CC";
            } else if ("LAMOIYAN".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "LY";
            } else if ("PEERLESS".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "PL";
            } else if ("URC".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "UC";
            } else if ("URC-BEV".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "UB";
            } else if ("MONTOSCO".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "MI";
            } else if ("GTAM".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "GT";
            } else if ("EQ".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "EQ";
            } else if ("BELO".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "BL";
            } else if ("RAM".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "RM";
            } else if ("ZESTAR".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "ZS";
            } else if ("COLUMBIA".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "CL";
            } else if ("KOHL".equals(spoutlet.getSelectedItem().toString())) {
                trapTrans1 = "KL";
            }

            if ("MARS2".equals(Databasename)) {
                Detailsinvoice = "REQCREL2!" + custid.getText().toString() + "!" + trapTrans1;
            } else {
                Detailsinvoice = "REQCREL!" + custid.getText().toString() + "!" + trapTrans1;
            }

            // Toast.makeText(getApplicationContext(), Detailsinvoice,Toast.LENGTH_LONG).show();

            Integer cnt = 0;
            Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
            c1.moveToFirst();
            cnt = c1.getCount();
            if (cnt > 0) {
                theMobNo = c1.getString(0);
            }
            sendSMS(theMobNo, Detailsinvoice);
        }

        // payment
        if (v.getId() == R.id.btpaymentnew) { // request update credit line
            try {
                String totalAmountStr = totalinvamount.getText().toString().trim();

                if (totalAmountStr.isEmpty() || "0".equals(totalAmountStr) || "0.0".equals(totalAmountStr)) {
                    Toast.makeText(getApplicationContext(), "No invoice details found!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Build the intent to start PaymentDetails activity
                Intent intent = new Intent(NewInvoice.this, PaymentDetails.class);

                // Pass required values
                intent.putExtra("invoicetotalamount", totalAmountStr);

                String customerId = custid.getText().toString().trim();
                String customerName = custname.getText().toString().trim();
                String invoiceNumber = prefixval + Invnumberval;

                intent.putExtra("customerid", customerId);
                intent.putExtra("customername", customerName);
                intent.putExtra("invnumber", invoiceNumber);

                startActivity(intent);

            } catch (Exception e) {
                Log.e("PAYMENT_INTENT_ERROR", "Error launching PaymentDetails", e);
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }
        }

        // delete item
        if (v.getId() == R.id.btdeleteitemnew) {
            if (spproductnew.getSelectedItem() == null) {
                Toast.makeText(getApplicationContext(), "No product selected!", Toast.LENGTH_LONG).show();
                return;
            }

            // Normalize product name
            String productName = spproductnew.getSelectedItem().toString().trim();

            new AlertDialog.Builder(NewInvoice.this)
                    .setIcon(R.drawable.mars_logo)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete \"" + productName + "\" from the invoice?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            // Delete using case-insensitive, trimmed match
                            int deletedRows = db.delete("TempInvoice", "TRIM(description) = ? COLLATE NOCASE", new String[]{productName});

                            Log.d("DELETE_PRODUCT", "Deleted rows: " + deletedRows);

                            if (deletedRows > 0) {
                                Toast.makeText(getApplicationContext(), productName + " deleted from invoice.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "No matching item found to delete.", Toast.LENGTH_LONG).show();
                            }

                            // Refresh invoice list
                            invoiceProducts.clear();
                            tinvamount = 0;
                            int toqty = 0;

                            Cursor ctemp = db.rawQuery("SELECT * FROM TempInvoice ORDER BY description", null);
                            if (ctemp.moveToFirst()) {
                                do {
                                    int qty = ctemp.getInt(4);
                                    String desc = ctemp.getString(2);
                                    float price = ctemp.getFloat(3);
                                    float amount = ctemp.getFloat(5);

                                    invoiceProducts.add(new InvoiceProducts(qty, desc, price, amount));
                                    tinvamount += amount;
                                    toqty += qty;

                                } while (ctemp.moveToNext());
                            }
                            ctemp.close();

                            adapter.notifyDataSetChanged();
                            totalinvqty.setText(String.valueOf(toqty));

                            DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                            totalinvamount.setText(formatter.format(tinvamount));

                        } catch (Exception e) {
                            Log.e("DELETE_ITEM_ERROR", "Error deleting item", e);
                            Toast.makeText(getApplicationContext(), "Error deleting item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }

        if (v.getId() == R.id.btadddetailsnewinv) { // add item
            try {
                float purchaseprice = 0;

                if ("VAN-SELLING".equals(transinv.getText().toString())) {
                    selectQuery = "select * from products where description like '" + spproductnew.getSelectedItem().toString() + "'  and salesmanid = '" + SalesmanID + "' ";
                } else {
                    selectQuery = "select * from products where description like '" + spproductnew.getSelectedItem().toString() + "' ";
                }

                Cursor c = db.rawQuery(selectQuery, null);
                c.moveToFirst();
                int cnt = c.getCount();
                if (cnt > 0) {  // check if product is exist
                    // purchaseprice = Float.parseFloat(c.getString(2));
                    // int invleft = c.getInt(4); // quantity left
                    if ("".equalsIgnoreCase(csquantity.getText().toString())) {
                    } else {
                        quantity.setText("1");
                    }

                    tempstring = quantity.getText().toString();   // check if have quantiy input
                    if ("".equals(tempstring)) {
                        Toast.makeText(getApplicationContext(), "Please input Quantity!", Toast.LENGTH_LONG).show();
                    } else {
                        Cursor c3 = db.rawQuery("select * from TempInvoice where description like '" + spproductnew.getSelectedItem().toString() + "'", null);
                        c3.moveToFirst();
                        cnt = c3.getCount();
                        if (cnt > 2) {   // check if item is already in the list
                            Toast.makeText(getApplicationContext(), "Item is Already in the list!", Toast.LENGTH_LONG).show();
                        } else {
                            String NumID = c.getString(0);       // product id
                            String productDescription = c.getString(1); // product description

                            Integer qty = Integer.parseInt(tempstring); // check if quantity input is not zero
                            if (qty < 1) {
                                Toast.makeText(getApplicationContext(), "Zero Quantity not Allowed!", Toast.LENGTH_LONG).show();
                            } else {
                                if ("VAN-SELLING".equals(transinv.getText().toString())) {
                                    tempstring = qtyavailable.getText().toString();
                                    float AvailableQty = Float.parseFloat(tempstring);

                                    float ServeQty_issued = 0;
                                    if ("".equalsIgnoreCase(csquantity.getText().toString())) {
                                        tempstring = quantity.getText().toString();
                                        ServeQty_issued = Float.parseFloat(tempstring);
                                    } else {
                                        ServeQty_issued = Float.parseFloat(csquantity.getText().toString());
                                        float factor_cs = Float.parseFloat(csfactor.getText().toString());
                                        ServeQty_issued = ServeQty_issued * factor_cs;
                                    }

                                    if (AvailableQty < ServeQty_issued) {
                                        Toast.makeText(getApplicationContext(), "Your Available Inventory of " + spproductnew.getSelectedItem().toString() + " are " + AvailableQty + " is less than to Quantity Issue " + ServeQty_issued + ", Please Check!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                tempstring = unitprice.getText().toString(); // check unit price
                                if (TrapInvQty) {
                                } else if ("".equals(tempstring)) {
                                    Toast.makeText(getApplicationContext(), "Please input Unit Price!", Toast.LENGTH_LONG).show();
                                } else {
                                    Cursor c3E = db.rawQuery("select * from TempInvoice ", null);
                                    c3E.moveToLast();
                                    cnt = c3E.getCount();
                                    if (cnt > 51) {
                                        Toast.makeText(getApplicationContext(), "Only 50 Sku per Syntax!", Toast.LENGTH_LONG).show();
                                    } else {

                                        float totalinvoice;
                                        float cred = 0;
                                        float tamount;

                                        float PRICEORDER;
                                        float QTYORDER;
                                        String csorder = "" + csquantity.getText().toString();
                                        boolean cspricecheck = true;

                                        if ("".equals(csorder)) {
                                        } else {

                                            tempstring = csunitprice.getText().toString(); // check cs unit price
                                            if ("".equals(tempstring)) {
                                                Toast.makeText(getApplicationContext(), "Please Input Case Price", Toast.LENGTH_LONG).show();
                                                cspricecheck = false;
                                            } else {
                                            }
                                        }

                                        if (cspricecheck) {

                                            if ("".equals(csorder)) {
                                                tamount = Float.parseFloat(quantity.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                                PRICEORDER = Float.parseFloat(unitprice.getText().toString());
                                                QTYORDER = Float.parseFloat(quantity.getText().toString());
                                            } else {
                                                tamount = Float.parseFloat(csquantity.getText().toString()) * Float.parseFloat(csunitprice.getText().toString());
                                                PRICEORDER = Float.parseFloat(csunitprice.getText().toString()) / Float.parseFloat(csfactor.getText().toString());
                                                QTYORDER = Float.parseFloat(csquantity.getText().toString()) * Float.parseFloat(csfactor.getText().toString());
                                                quantity.setText("0");
                                            }

                                            Cursor c4 = db.rawQuery("select sum(totalamount) from TempInvoice ", null);
                                            c4.moveToFirst();
                                            cnt = c4.getCount();
                                            if (cnt > 0) {
                                                totalinvoice = tamount + c4.getFloat(0);
                                                cred = Float.parseFloat(tvavailcredit.getText().toString());
                                            } else {
                                                totalinvoice = tamount;
                                            }
                                            // end check credit limit

                                            String trapTrans = "";
                                            if ("VAN-SELLING".equals(transinv.getText().toString())) {
                                                trapTrans = "V";
                                            } else {
                                                trapTrans = "B";
                                                totalinvoice = 0;
                                                cred = 1;
                                            }

                                            float CreditStatustNumber = Float.parseFloat(termstatus.getText().toString());
                                            float CurCreditLine = Float.parseFloat(tvcreditlimit.getText().toString());
                                            String percentadd;

                                            if (CreditStatustNumber <= 5) {
                                                percentadd = "1.2";
                                                CurCreditLine = (CurCreditLine * Float.parseFloat(percentadd)) - CurCreditLine;
                                            } else if (CreditStatustNumber <= 10) {
                                                percentadd = "1.1";
                                                CurCreditLine = (CurCreditLine * Float.parseFloat(percentadd)) - CurCreditLine;
                                            } else {
                                                CurCreditLine = 0;
                                            }

                                            String termifcash = sptermssp.getSelectedItem().toString();
                                            if ("CASH".equals(termifcash)) {
                                                cred = Float.parseFloat("10000000.00");
                                            } else if ("PDC".equals(termifcash)) {
                                                cred = Float.parseFloat("10000000.00");
                                            } else {
                                                cred = cred + CurCreditLine;
                                            }

                                            if (totalinvoice > cred) { // check if credit limit not exceed
                                                totalinvoice = totalinvoice - cred;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(NewInvoice.this);
                                                builder.setTitle("Credit Limit Exceeded");
                                                builder.setMessage(
                                                        "Use only Available Credit Line: " + cred +
                                                                "\nExceed Amount: " + totalinvoice +
                                                                "\n\nAre you sure you want to continue?"
                                                );
                                                builder.setIcon(R.drawable.mars_logo);

                                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing, just dismiss the dialog
                                                        dialog.dismiss();
                                                    }
                                                });

                                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Also dismiss the dialog
                                                        dialog.dismiss();
                                                    }
                                                });

                                                builder.setCancelable(false);
                                                builder.show();

//                                                Toast.makeText(getApplicationContext(), "Credit Limit Exceed, Use only Available Credit Line : " + cred + " , Exceed Amount : " + totalinvoice, Toast.LENGTH_LONG).show();
                                            } else {

                                                tempstring = getnum.getText().toString();
                                                if ("".equals(tempstring)) {
                                                    Toast.makeText(getApplicationContext(), "Please input Invoice Number!", Toast.LENGTH_LONG).show();
                                                } else {

                                                    try {
                                                        // insert into TempInvoice
                                                        db.execSQL("INSERT INTO TempInvoice VALUES('" + getprefix.getSelectedItem().toString() + Invnumberval + "','" +
                                                                NumID + "','" + productDescription + "'," + PRICEORDER + "," + QTYORDER + "," + tamount + ");");

                                                        // now refresh the RecyclerView
                                                        loadtempinvoit();

                                                        // refresh list
                                                        invoiceProducts.clear();
                                                        tinvamount = 0;
                                                        int toqty = 0;

                                                        Cursor ctemp = db.rawQuery("SELECT * FROM TempInvoice ORDER BY description", null);
                                                        if (ctemp.moveToFirst()) {
                                                            do {
                                                                int productQty = ctemp.getInt(4);
                                                                String productDesc = ctemp.getString(2);
                                                                float productPrice = ctemp.getFloat(3);
                                                                float productAmount = ctemp.getFloat(5);

                                                                tinvamount += productAmount;
                                                                toqty += productQty;

                                                                invoiceProducts.add(new InvoiceProducts(productQty, productDesc, productPrice, productAmount));
                                                            } while (ctemp.moveToNext());
                                                        }
                                                        ctemp.close();

                                                        adapter.notifyDataSetChanged();
                                                        totalinvqty.setText(String.valueOf(toqty));
                                                        totalinvamount.setText(new DecimalFormat("###,###,###.##").format(tinvamount));

                                                        csquantity.setText("");
                                                        Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_LONG).show();

                                                    } catch (Exception e) {
                                                        Log.e("ERROR_ADD_ITEM", "Error occurred: ", e);
                                                        Toast.makeText(getApplicationContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        } // end else check if credit limit not exceed
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR_ADD_ITEM", "Error occurred: ", e);
                Toast.makeText(getApplicationContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        try {
            if (v.getId() == R.id.btsendsave) { // save send

                AlertDialog.Builder builder = new AlertDialog.Builder(NewInvoice.this);
                builder.setMessage("Are you sure you want Save this Transaction?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // TODO Auto-generated method stub
                    // MainActivity.this.finish();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
                    String currentDateandTime1 = sdf1.format(new Date());
                    SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    String datetimesyntaxval = datetimesyntax.format(new Date());

                    // Toast.makeText(getApplicationContext(), "" + datetimesyntax,Toast.LENGTH_LONG).show();

                    String refid = "" + prefixval + Invnumberval;

                    Cursor invquery = db.rawQuery("Select * from invoicedtl where refid like '" + prefixval + Invnumberval + "'", null);

                    invquery.moveToFirst();
                    int countrec = invquery.getCount();   // check if invoice already in used!
                    if (countrec > 0) {
//                            Toast.makeText(getApplicationContext(), "Invoice number " + "[ " + refid + " ]" + " is already use!", Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(NewInvoice.this)
                                .setIcon(R.drawable.mars_logo)
                                .setTitle("Duplicate Invoice")
                                .setMessage("Invoice number [ " + refid + " ] is already in use. \n\nPlease double check your\ninvoice number. Thank you!")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dlg, w) -> dlg.dismiss())
                                .show();
                    } else {

                        float tempamout = Float.parseFloat(tvavailcredit.getText().toString());

                        Cursor ctemp = db.rawQuery("select sum(totalamount) from TempInvoice order by description", null);
                        ctemp.moveToFirst();
                        int cnt = ctemp.getCount();
                        if (cnt > 0) {
                            tinvamount = ctemp.getFloat(0);
                        }

                        String trapTrans = "";
                        if ("VAN-SELLING".equals(transinv.getText().toString())) {
                            trapTrans = "V";
                        } else {
                            trapTrans = "B";
                        }

                        if (tinvamount > tempamout) { // check if invoice amount is greater than available credit line
                            if ("B".equals(trapTrans)) {

                            } else {
                                // Toast.makeText(getApplicationContext(), "Credit Limit Exceed!",Toast.LENGTH_LONG).show();
                                // checkvalidation = false;
                            }
                        } else {
                            checkvalidation = true;
                        }

                        if (tinvamount == 0) { // check if have details
                            Toast.makeText(getApplicationContext(), "Unable to save no details!", Toast.LENGTH_LONG).show();
                            checkvalidation = false;
                        } else {
                            checkvalidation = true;
                        }

                        if (Invnumberval.equals("")) { // check if have invoice number input
                            Toast.makeText(getApplicationContext(), "Please input invoice Number!!", Toast.LENGTH_LONG).show();
                            checkvalidation = false;
                        } else {
                            checkvalidation = true;
                        }

                        // Check if there are 11 lines for CENTURY
                        if ("CC".equals(Departmentcode)) {
                            int cnt2 = 0;
                            if ("MARS1".equals(Databasename)) {
                                Cursor invtemp = db.rawQuery("select * from TempInvoice where productid = '35415' or productid = '35468'  or productid = '35425'   or productid = 'CPMC011425'   or productid = 'CPMC011525' or productid = 'CPMC012025' ", null);
                                invtemp.moveToFirst();
                                cnt2 = invtemp.getCount();
                            } else {
                                Cursor invtemp = db.rawQuery("select * from TempInvoice where productid = '317' or productid = '33891'  or productid = '33892'   ", null);
                                invtemp.moveToFirst();
                                cnt2 = invtemp.getCount();
                            }

                            if (cnt2 > 0) {
                                Log.d("11 lines", "Check if there are 11 lines for CENTURY: " + cnt2);
                            } else {
                                Cursor invtemp1 = db.rawQuery("select * from TempInvoice ", null);
                                invtemp1.moveToFirst();
                                int cnt3 = invtemp1.getCount();
                                if (cnt3 < 11) {
                                    Toast.makeText(getApplicationContext(), "You must need 11 lines to Send and Save This Invoice.", Toast.LENGTH_LONG).show();
                                    checkvalidation = false;
                                }
                            }
                        } // End of 11 lines for CENTURY

                        if (checkvalidation) {

                            // load and save the invoice details
                            Detailsinvoice = "";

                            Cursor invtemp = db.rawQuery("select * from TempInvoice order by description", null);
                            invtemp.moveToFirst();
                            int cnt2 = invtemp.getCount();
                            if (cnt2 > 0) {

                                String Tempsendsyntax = "";

                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                                String currentDateandTime = sdf.format(new Date());
//                                float amtt = 0;
                                float tinvamount = 0;
//                                String Productlist = "";

                                int countmsg;
                                int valqtylenth;

                                String Outlettype = "";
                                String SyntaxCodeDept = "";

                                tempstring = spoutlet.getSelectedItem().toString();
                                switch (tempstring) {
                                    case "LUBRICANTS":
                                        Outlettype = "L";
                                        SyntaxCodeDept = "LINV";
                                        break;
                                    case "AUTO-SUPPLY":
                                        Outlettype = "A";
                                        SyntaxCodeDept = "LINV";
                                        break;
                                    case "AGRICHEM":
                                        Outlettype = "AG";
                                        SyntaxCodeDept = "LINV";
                                        break;
                                    case "CENTURY":
                                        Outlettype = "CC";
                                        if ("MARS2".equals(Databasename)) {
                                            SyntaxCodeDept = "C2INV";
                                        } else {
                                            SyntaxCodeDept = "CINV";
                                        }
                                        break;
                                    case "URC":
                                        Outlettype = "UC";
                                        SyntaxCodeDept = "UINV";
                                        break;
                                    case "PEERLESS":
                                        Outlettype = "PL";
                                        SyntaxCodeDept = "PINV";
                                        break;
                                    case "URC-BEV":
                                        Outlettype = "UB";
                                        SyntaxCodeDept = "UINV";
                                        break;
                                    case "MONTOSCO":
                                        Outlettype = "MI";
                                        if ("MARS2".equals(Databasename)) {
                                            SyntaxCodeDept = "C2INV";
                                        } else {
                                            SyntaxCodeDept = "CINV";
                                        }
                                        break;
                                    case "GTAM":
                                        Outlettype = "GT";
                                        SyntaxCodeDept = "C2INV";
                                        break;
                                    case "EQ":
                                        Outlettype = "EQ";
                                        SyntaxCodeDept = "CINV";
                                        break;
                                    case "RAM":
                                        Outlettype = "RM";
                                        SyntaxCodeDept = "TINV";
                                        break;
                                    case "ZESTAR":
                                        Outlettype = "ZS";
                                        SyntaxCodeDept = "TINV";
                                        break;
                                    case "COLUMBIA":
                                        Outlettype = "CL";
                                        SyntaxCodeDept = "TINV";
                                        break;
                                    case "KOHL":
                                        Outlettype = "KL";
                                        SyntaxCodeDept = "TINV";
                                        break;
                                    case "LAMOIYAN":
                                        Outlettype = "LY";
                                        if ("MARS2".equals(Databasename)) {
                                            SyntaxCodeDept = "C2INV";
                                        } else {
                                            SyntaxCodeDept = "CINV";
                                        }
                                        break;
                                    case "BELO":
                                        Outlettype = "BL";
                                        if ("MARS2".equals(Databasename)) {
                                            SyntaxCodeDept = "C2INV";
                                        } else {
                                            SyntaxCodeDept = "CINV";
                                        }
                                        break;
                                }

                                String pp = "";
                                if (Salesman_pss.equals("CPSS")) {
                                    pp = "P";
                                }

                                if ("B".equals(trapTrans)) {
                                    // trapTrans = trapTrans + "D!" + eddeldate.getText().toString();
                                    Detailsinvoice = SyntaxCodeDept + trapTrans + "D" + pp + "!" + eddeldate.getText().toString() + "!" + custid.getText().toString() + "!" + refid + "!" + sptermssp.getSelectedItem().toString() + "!" + spdaystermsp.getSelectedItem().toString() + "!" + Outlettype + "!" + osnumber.getText().toString() + "!" + datetimesyntaxval + "!";
                                } else {
                                    Detailsinvoice = SyntaxCodeDept + trapTrans + "!" + custid.getText().toString() + "!" + refid + "!" + sptermssp.getSelectedItem().toString() + "!" + spdaystermsp.getSelectedItem().toString() + "!" + Outlettype + "!" + osnumber.getText().toString() + "!" + datetimesyntaxval + "!";
                                }

                                Detailsinvoice1 = SyntaxCodeDept + "1" + trapTrans + "!" + refid + "!";
                                Detailsinvoice2 = SyntaxCodeDept + "2" + trapTrans + "!" + refid + "!";
                                Detailsinvoice3 = SyntaxCodeDept + "3" + trapTrans + "!" + refid + "!";
                                Detailsinvoice4 = SyntaxCodeDept + "4" + trapTrans + "!" + refid + "!";
                                Detailsinvoice5 = SyntaxCodeDept + "5" + trapTrans + "!" + refid + "!";
                                Detailsinvoice6 = SyntaxCodeDept + "6" + trapTrans + "!" + refid + "!";
                                Detailsinvoice7 = SyntaxCodeDept + "7" + trapTrans + "!" + refid + "!";
                                Detailsinvoice8 = SyntaxCodeDept + "8" + trapTrans + "!" + refid + "!";
                                Detailsinvoice9 = SyntaxCodeDept + "9" + trapTrans + "!" + refid + "!";
                                Detailsinvoice10 = SyntaxCodeDept + "10" + trapTrans + "!" + refid + "!";
                                Detailsinvoice11 = SyntaxCodeDept + "11" + trapTrans + "!" + refid + "!";
                                Detailsinvoice12 = SyntaxCodeDept + "12" + trapTrans + "!" + refid + "!";
                                Detailsinvoice13 = SyntaxCodeDept + "13" + trapTrans + "!" + refid + "!";
                                Detailsinvoice14 = SyntaxCodeDept + "14" + trapTrans + "!" + refid + "!";
                                Detailsinvoice15 = SyntaxCodeDept + "15" + trapTrans + "!" + refid + "!";
                                Detailsinvoice16 = SyntaxCodeDept + "16" + trapTrans + "!" + refid + "!";
                                Detailsinvoice17 = SyntaxCodeDept + "17" + trapTrans + "!" + refid + "!";
                                Detailsinvoice18 = SyntaxCodeDept + "18" + trapTrans + "!" + refid + "!";
                                Detailsinvoice19 = SyntaxCodeDept + "19" + trapTrans + "!" + refid + "!";
                                Detailsinvoice20 = SyntaxCodeDept + "20" + trapTrans + "!" + refid + "!";
                                Detailsinvoice21 = SyntaxCodeDept + "21" + trapTrans + "!" + refid + "!";
                                Detailsinvoice22 = SyntaxCodeDept + "22" + trapTrans + "!" + refid + "!";
                                Detailsinvoice23 = SyntaxCodeDept + "23" + trapTrans + "!" + refid + "!";
                                Detailsinvoice24 = SyntaxCodeDept + "24" + trapTrans + "!" + refid + "!";

                                countmsg = 0;
                                int countmsglen = 160;
                                do {
                                    tinvamount = tinvamount + invtemp.getFloat(5);

                                    // new
                                    if (Outlettype.equals("L") || Outlettype.equals("AA") || Outlettype.equals("AG")) {
                                        if (countmsg <= 0) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 1) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 2) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 3) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 4) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 5) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 6) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 7) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 8) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 9) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 10) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 11) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 12) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 13) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 14) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 15) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 16) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 17) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 18) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 19) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 20) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 21) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 22) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 23) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 24) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + "/";
                                            }
                                        }
                                    } else {

                                        if (countmsg <= 0) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 1) {
                                            Tempsendsyntax = Detailsinvoice1 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 2) {
                                            Tempsendsyntax = Detailsinvoice2 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 3) {
                                            Tempsendsyntax = Detailsinvoice3 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 4) {
                                            Tempsendsyntax = Detailsinvoice4 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 5) {
                                            Tempsendsyntax = Detailsinvoice5 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 6) {
                                            Tempsendsyntax = Detailsinvoice6 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 7) {
                                            Tempsendsyntax = Detailsinvoice7 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 8) {
                                            Tempsendsyntax = Detailsinvoice8 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 9) {
                                            Tempsendsyntax = Detailsinvoice9 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 10) {
                                            Tempsendsyntax = Detailsinvoice10 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 11) {
                                            Tempsendsyntax = Detailsinvoice11 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 12) {
                                            Tempsendsyntax = Detailsinvoice12 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 13) {
                                            Tempsendsyntax = Detailsinvoice13 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 14) {
                                            Tempsendsyntax = Detailsinvoice14 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 15) {
                                            Tempsendsyntax = Detailsinvoice15 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 16) {
                                            Tempsendsyntax = Detailsinvoice16 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 17) {
                                            Tempsendsyntax = Detailsinvoice17 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 18) {
                                            Tempsendsyntax = Detailsinvoice18 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 19) {
                                            Tempsendsyntax = Detailsinvoice19 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 20) {
                                            Tempsendsyntax = Detailsinvoice20 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 21) {
                                            Tempsendsyntax = Detailsinvoice21 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 22) {
                                            Tempsendsyntax = Detailsinvoice22 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 23) {
                                            Tempsendsyntax = Detailsinvoice23 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        } else if (countmsg <= 24) {
                                            Tempsendsyntax = Detailsinvoice24 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            ;
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > countmsglen) {
                                                Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            } else {
                                                Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":0:" + invtemp.getString(4) + "/";
                                            }
                                        }
                                        // Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        // Productlist = Productlist + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                    }
                                    // save invoice details
                                    db.execSQL("Insert into invoicedtl values" +
                                            "('" + refid + "','" +
                                            custid.getText().toString() + "','" + // customerid
                                            invtemp.getString(1) + "'," + // productid
                                            invtemp.getInt(4) + "," + // quantity
                                            invtemp.getFloat(3) + "," + // unitprice
                                            invtemp.getFloat(5) + ",'" + // total amount
                                            currentDateandTime + "','" + // invoice date
                                            sptermssp.getSelectedItem().toString() + "','" + // terms
                                            spdaystermsp.getSelectedItem().toString() + "','" + // daysterm
                                            datetimesyntaxval + "','" + osnumber.getText().toString() + "','" + Outlettype + "','" + trapTrans + "','" + eddeldate.getText().toString() + "');");                        // syntax date


                                } while (invtemp.moveToNext());
                                // end for Details saved successfully

                                // query receiver number
                                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                                c1.moveToFirst();
                                cnt = c1.getCount();
                                if (cnt > 0) {
                                    theMobNo = c1.getString(0);
                                }

                                db.execSQL("Update ScheduleCustomer set status = 'Y' WHERE customerid = '" + custid.getText().toString() + "';");

                                if (countmsg <= 0) {
                                    sendSMS(theMobNo, Detailsinvoice);
                                } else if (countmsg <= 1) {
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    // Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                                    // Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                                } else if (countmsg <= 2) {
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                } else if (countmsg <= 3) {
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                } else if (countmsg <= 4) {
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                } else if (countmsg <= 5) {
                                    // Toast.makeText(getApplicationContext(), "sms 5" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                } else if (countmsg <= 6) {
                                    // Toast.makeText(getApplicationContext(), "sms 6" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                } else if (countmsg <= 7) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                } else if (countmsg <= 8) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                } else if (countmsg <= 9) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                } else if (countmsg <= 10) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                } else if (countmsg <= 11) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                } else if (countmsg <= 12) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                } else if (countmsg <= 13) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                } else if (countmsg <= 14) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                } else if (countmsg <= 15) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                } else if (countmsg <= 16) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                } else if (countmsg <= 17) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                } else if (countmsg <= 18) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                } else if (countmsg <= 19) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                } else if (countmsg <= 20) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                    sendSMS(theMobNo, Detailsinvoice20);
                                } else if (countmsg <= 21) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                    sendSMS(theMobNo, Detailsinvoice20);
                                    sendSMS(theMobNo, Detailsinvoice21);
                                } else if (countmsg <= 22) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                    sendSMS(theMobNo, Detailsinvoice20);
                                    sendSMS(theMobNo, Detailsinvoice21);
                                    sendSMS(theMobNo, Detailsinvoice22);
                                } else if (countmsg <= 23) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                    sendSMS(theMobNo, Detailsinvoice20);
                                    sendSMS(theMobNo, Detailsinvoice21);
                                    sendSMS(theMobNo, Detailsinvoice22);
                                    sendSMS(theMobNo, Detailsinvoice23);
                                } else if (countmsg <= 24) {
                                    // Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                                    sendSMS(theMobNo, Detailsinvoice);
                                    sendSMS(theMobNo, Detailsinvoice1);
                                    sendSMS(theMobNo, Detailsinvoice2);
                                    sendSMS(theMobNo, Detailsinvoice3);
                                    sendSMS(theMobNo, Detailsinvoice4);
                                    sendSMS(theMobNo, Detailsinvoice5);
                                    sendSMS(theMobNo, Detailsinvoice6);
                                    sendSMS(theMobNo, Detailsinvoice7);
                                    sendSMS(theMobNo, Detailsinvoice8);
                                    sendSMS(theMobNo, Detailsinvoice9);
                                    sendSMS(theMobNo, Detailsinvoice10);
                                    sendSMS(theMobNo, Detailsinvoice11);
                                    sendSMS(theMobNo, Detailsinvoice12);
                                    sendSMS(theMobNo, Detailsinvoice13);
                                    sendSMS(theMobNo, Detailsinvoice14);
                                    sendSMS(theMobNo, Detailsinvoice15);
                                    sendSMS(theMobNo, Detailsinvoice16);
                                    sendSMS(theMobNo, Detailsinvoice17);
                                    sendSMS(theMobNo, Detailsinvoice18);
                                    sendSMS(theMobNo, Detailsinvoice19);
                                    sendSMS(theMobNo, Detailsinvoice20);
                                    sendSMS(theMobNo, Detailsinvoice21);
                                    sendSMS(theMobNo, Detailsinvoice22);
                                    sendSMS(theMobNo, Detailsinvoice23);
                                    sendSMS(theMobNo, Detailsinvoice24);
                                }
                                Toast.makeText(getApplicationContext(), "Successfully Send Thank You", Toast.LENGTH_LONG).show();

                                // Clear the items after the order has been successfully sent
                                db.execSQL("DELETE FROM TempInvoice");
                                loadtempinvoit();
                                // Clear the items after the order has been successfully sent

                                Detailsinvoice = "";
                                // send payment
                                invtemp = db.rawQuery("select * from PaymentDetailsTemp ", null);
                                invtemp.moveToFirst();
                                cnt2 = invtemp.getCount();
                                if (cnt2 > 0) {

                                    if ("MARS2".equals(Databasename)) {
                                        SyntaxCodeDept = "PAYMENT2";
                                    } else {
                                        SyntaxCodeDept = "PAYMENT";
                                    }

                                    Detailsinvoice = SyntaxCodeDept + "!" + refid + "!";
                                    Detailsinvoice1 = SyntaxCodeDept + "1" + "!" + refid + "!";
                                    Detailsinvoice2 = SyntaxCodeDept + "2" + "!" + refid + "!";
                                    Detailsinvoice3 = SyntaxCodeDept + "3" + "!" + refid + "!";
                                    Detailsinvoice4 = SyntaxCodeDept + "4" + "!" + refid + "!";
                                    Detailsinvoice5 = SyntaxCodeDept + "5" + "!" + refid + "!";
                                    Detailsinvoice6 = SyntaxCodeDept + "6" + "!" + refid + "!";
                                    Detailsinvoice7 = SyntaxCodeDept + "7" + "!" + refid + "!";

                                    countmsg = 0;
                                    do {
                                        if (countmsg <= 0) {
                                            Tempsendsyntax = Detailsinvoice + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice = Detailsinvoice + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        } else if (countmsg <= 1) {
                                            Tempsendsyntax = Detailsinvoice1 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        } else if (countmsg <= 2) {
                                            Tempsendsyntax = Detailsinvoice2 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        } else if (countmsg <= 3) {
                                            Tempsendsyntax = Detailsinvoice3 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        } else if (countmsg <= 4) {
                                            Tempsendsyntax = Detailsinvoice4 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        } else if (countmsg <= 5) {
                                            Tempsendsyntax = Detailsinvoice5 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            valqtylenth = Tempsendsyntax.length();
                                            if (valqtylenth > 160) {
                                                countmsg = countmsg + 1;
                                                Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            } else {
                                                Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(0) + ":" + invtemp.getString(1) + ":" + invtemp.getString(2) + ":" + invtemp.getString(3) + ":" + invtemp.getString(4) + ":" + invtemp.getString(5) + "/";
                                            }
                                        }

                                        // Productlist =  Productlist + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        // save invoice details
                                        db.execSQL("Insert into PaymentDetails values" +
                                                "('" + refid + "','" +
                                                invtemp.getString(0) + "','" + // PAYMENT TYPE
                                                invtemp.getString(1) + "','" + // BANK INITIAL
                                                invtemp.getString(2) + "','" + // CHECK NUMBER
                                                invtemp.getString(3) + "','" + // ACCOUNT NUMBER
                                                invtemp.getString(4) + "','" + // CHECK DATE
                                                invtemp.getString(5) + "');"); // AMOUNT

                                    } while (invtemp.moveToNext());

                                    if (countmsg <= 0) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                    } else if (countmsg <= 1) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                        sendSMS(theMobNo, Detailsinvoice1);
                                        Toast.makeText(getApplicationContext(), Detailsinvoice1, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), Detailsinvoice, Toast.LENGTH_LONG).show();
                                    } else if (countmsg <= 2) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                        sendSMS(theMobNo, Detailsinvoice1);
                                        sendSMS(theMobNo, Detailsinvoice2);
                                    } else if (countmsg <= 3) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                        sendSMS(theMobNo, Detailsinvoice1);
                                        sendSMS(theMobNo, Detailsinvoice2);
                                        sendSMS(theMobNo, Detailsinvoice3);
                                    } else if (countmsg <= 4) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                        sendSMS(theMobNo, Detailsinvoice1);
                                        sendSMS(theMobNo, Detailsinvoice2);
                                        sendSMS(theMobNo, Detailsinvoice3);
                                        sendSMS(theMobNo, Detailsinvoice4);
                                    } else if (countmsg <= 5) {
                                        sendSMS(theMobNo, Detailsinvoice);
                                        sendSMS(theMobNo, Detailsinvoice1);
                                        sendSMS(theMobNo, Detailsinvoice2);
                                        sendSMS(theMobNo, Detailsinvoice3);
                                        sendSMS(theMobNo, Detailsinvoice4);
                                        sendSMS(theMobNo, Detailsinvoice5);
                                    }
                                }

                                db.execSQL("delete from PaymentDetailsTemp;");
                                db.execSQL("Delete from CustomerUnlock WHERE customerid = '" + custid.getText().toString() + "';");
                                // Toast.makeText(getApplicationContext(),"" + Detailsinvoice ,Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "No Details Found!", Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {
            Log.e("CATCH ERROR", "ERRORS FOR ISSUE INVOICE: ", e);
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        if (which == DialogInterface.BUTTON_NEUTRAL) {
            editRemarks.setText("Sorry, location is not determined. To fix this please enable location providers");
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @SuppressLint("MissingPermission")
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                // unregister updates only if location manager is not null and permissions are granted
                if (locManager != null) {
                    locManager.removeUpdates(this);
                }

                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());

                tvOlat.setText(latitude);
                tvOlong.setText(longitude);
            } else {
                Toast.makeText(getApplicationContext(), "GPS not enabled or unable to get location!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(getApplicationContext(), "GPS provider disabled!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Deprecated in API 29+, usually not needed
        }
    }

    @SuppressWarnings("unused")
    public void GetInventory(String IDCode) {

        float InvEnding;
        String CountValue;

        Cursor ctemp2 = db.rawQuery("select * from products where salesmanid like '" + SalesmanID + "' and productid = '" + IDCode + "'  order by description ", null);
        ctemp2.moveToFirst();
        int cnt = ctemp2.getCount();
        if (cnt > 0) {
            // Toast.makeText(getApplicationContext(), " 1 check inventory! " + SalesmanID + '-' + IDCode,Toast.LENGTH_LONG).show();

            Cursor invsItem = db.rawQuery("select sum(qty) as quantity,sum(totalamount) as amt from invoicedtl where productid = '" + IDCode + "'	 ", null);
            invsItem.moveToFirst();
            CountValue = "" + invsItem.getInt(0);

            if (CountValue != null) {
                if (invsItem.getInt(0) > 0) {
                    CountValue = "" + invsItem.getInt(0);
                }
                InvEnding = Float.parseFloat(ctemp2.getString(3)) - Float.parseFloat(CountValue);
                // Toast.makeText(getApplicationContext(), "2 " + CountValue,Toast.LENGTH_LONG).show();
            } else {
                InvEnding = Float.parseFloat(ctemp2.getString(3));
            }
        } else {
            InvEnding = 0;
        }
        // Toast.makeText(getApplicationContext(), "3 inventory " + InvEnding + '-' + IDCode,Toast.LENGTH_LONG).show();
        CountValue = "" + (InvEnding);
        // Toast.makeText(getApplicationContext(), "4 show in qtyavailable " + CountValue + '-' + IDCode,Toast.LENGTH_LONG).show();
        qtyavailable.setText(CountValue);
    }

    public void loadlistofproducts() {

        // spinner Drop down elements
        lables = new ArrayList<>();

        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lables.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        // creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spproductnew.setAdapter(dataAdapter);
    }

    // sends an SMS message to another device
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
            Toast.makeText(getApplicationContext(), "Sending Order Please Wait.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!" + ", ERROR :" + e, Toast.LENGTH_LONG).show();
            Log.e("SEND SMS", "SMS MANAGER: " + e.getMessage());
        }
    }
}
