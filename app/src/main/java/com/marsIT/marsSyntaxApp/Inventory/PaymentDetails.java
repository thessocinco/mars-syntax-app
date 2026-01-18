package com.marsIT.marsSyntaxApp.Inventory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;

@SuppressWarnings("deprecation")
public class PaymentDetails extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
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
    private Spinner getprefix;
    private EditText getnum;
    private EditText withtaxamount;
    private EditText osnumber;
    EditText inputSearch;
    private EditText editRemarks;
    private EditText quantity;
    private TextView custid;
    private TextView custname;
    private List<String> lables;
    private List<String> lables1;
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
    private TextView invoiceamount;
    private TextView termstatus;
    private TextView um;
    private TextView transinv;
    private EditText unitprice;
    private CheckBox withtax;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private Spinner sppaymenttype;
    private Spinner spBankInitial;
    private Button btaddtodetails;
    private Button btsendsave;
    private Button btdeleteitem;
    private boolean issave;
    private Integer countpro;
    private Integer countpro1;
    final Context context = this;
    private EditText edpaymenttypedtl;
    private EditText edBankInitialdtl;
    private EditText edCheckNumberdtl;
    private EditText edAccountNumberdtl;
    private EditText edCheckDatedtl;
    private EditText edAmountdtl;
    private EditText edpaymenttype;
    private EditText edCheckNumber;
    private EditText edAccountNumber;
    private EditText edCheckDate;
    private EditText edAmount;
    private String Detailsinvoice;
    private String Detailsinvoice1;
    private String Detailsinvoice2;
    private String Detailsinvoice3;
    private String Detailsinvoice4;
    private String Detailsinvoice5;
    private String Databasename;
    private String Departmentcode;
    private String prefixval;
    private String Invnumberval;
    private String outlettrans;
    private String SalesmanID;
    private float tinvamount;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //---display the SMS received in the TextView---

            String syntaxsms = intent.getExtras().getString("sms");       // message received

            String cellphonenumber = intent.getExtras().getString("cellnumber");

            //Toast.makeText(getApplicationContext(), "" + cellphonenumber, Toast.LENGTH_LONG).show();

            Cursor queryvalidnumber = db.rawQuery("select * from validnumber where num = '"+ cellphonenumber +"'", null);
            queryvalidnumber.moveToFirst();
            int cnt =  queryvalidnumber.getCount();
            if(cnt > 0){

                String NumberOwner = queryvalidnumber.getString(1);

                //String latval = syntaxsms;
                String stchar = "";
                int valqtylenth = syntaxsms.length();
                if(valqtylenth > 6 ){
                    for(int i = 0; i < 6; i++){
                        char descr =  syntaxsms.charAt(i);
                        stchar = stchar + descr;
                    }

                    //Toast.makeText(getApplicationContext(), "" + stchar + " by the: " + NumberOwner, Toast.LENGTH_LONG).show();

                    //String charitem = "";
                    String Compaire = "";
                    int counter = 0;
                    valqtylenth = syntaxsms.length();

                    if("UPRICE".equals(stchar)){   //  syntax for unlock price
                        String itemcode = "";
                        String unitprice = "";
                        String customercodep = "";
                        for(int i = 0; i < valqtylenth; i++){
                            char descr =  syntaxsms.charAt(i);
                            //stchar = stchar + descr;

                            Compaire = "" + descr;
                            if("/".equals(Compaire)){
                                counter = counter + 1;
                            }else{
                                if(counter == 1){
                                    itemcode = itemcode + descr;      // get item code
                                }else if(counter == 2){
                                    unitprice = unitprice + descr;    // get unit price
                                }else if(counter == 3){
                                    customercodep = customercodep + descr;    // get unit price
                                }
                            }
                        }

                        Cursor c2 = db.rawQuery("select * from products where productid = '"+ itemcode +"'", null);
                        c2.moveToFirst();
                        cnt =  c2.getCount();
                        if(cnt > 0){
                            db.execSQL("Insert into itempriceoveride values('"+ c2.getString(0) +"','"+ customercodep +"',"+ Double.parseDouble(unitprice) +");");
                            //db.execSQL("Update Products set Status = 'U', overideprice = "+ unitprice +" where productid = '"+ itemcode +"';");
                            Toast.makeText(getApplicationContext(), "Price Overide : " + c2.getString(1) + " by : " + NumberOwner, Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(), "Incorrect Sysntax,Item not found" , Toast.LENGTH_LONG).show();
                        }

                        // end overide price syntax
                    }else if("URECEI".equals(stchar)){
                        String receivernum = "";
                        for(int i = 0; i < valqtylenth; i++){
                            char descr =  syntaxsms.charAt(i);

                            Compaire = "" + descr;
                            if("/".equals(Compaire)){
                                counter = counter + 1;
                            }else{
                                if(counter == 1){
                                    receivernum = receivernum + descr;      // get item code
                                }
                            }
                        }

                        db.execSQL("Update receivernumber set num = '"+ receivernum +"';");
                        Toast.makeText(getApplicationContext(), "Update Receiver number :" + receivernum, Toast.LENGTH_LONG).show();
                        // add valid number
                    }else if("ADDNUM".equals(stchar)){
                        String receivernum = "";
                        String name = "";
                        for(int i = 0; i < valqtylenth; i++){
                            char descr =  syntaxsms.charAt(i);

                            Compaire = "" + descr;
                            if("/".equals(Compaire)){
                                counter = counter + 1;
                            }else{
                                if(counter == 1){
                                    receivernum = receivernum + descr;      // get item code
                                }else if(counter == 2){
                                    name = name + descr;      // get item code
                                }
                            }
                        }

                        db.execSQL("Insert into validnumber values('"+ receivernum +"','"+ name +"');");
                        Toast.makeText(getApplicationContext(), "Successfully add the valid number :" + receivernum, Toast.LENGTH_LONG).show();
                    }else if("UNLOCK".equals(stchar)){
                        String receivernum = "";
                        //String name = "";
                        for(int i = 0; i < valqtylenth; i++){
                            char descr =  syntaxsms.charAt(i);

                            Compaire = "" + descr;
                            if("/".equals(Compaire)){
                                counter = counter + 1;
                            }else{
                                if(counter == 1){
                                    receivernum = receivernum + descr;      // get item code
                                }
                            }
                        }

                        db.execSQL("UPDATE CUSTOMERS SET LOCK = 'U' WHERE CID ='"+ receivernum +"';");
                        Toast.makeText(getApplicationContext(), "Successfully UNLOCK THE CUSTOMER BY :" + NumberOwner, Toast.LENGTH_LONG).show();
                    }else if("CREDIT".equals(stchar)){
                        String customercode = "";
                        String creditlimit = "";
                        String balcredit = "";
                        String ocheck = "";
                        String CreditStatus1 = "0";

                        for(int i = 0; i < valqtylenth; i++){
                            char descr =  syntaxsms.charAt(i);

                            Compaire = "" + descr;
                            if("/".equals(Compaire)){
                                counter = counter + 1;
                            }else{
                                if(counter == 1){
                                    customercode = customercode + descr;      // get item code
                                }else if(counter == 2){
                                    creditlimit = creditlimit + descr;		// credit line

                                }else if(counter == 3){
                                    balcredit = balcredit + descr;			// bacredit

                                }else if(counter == 4){
                                    ocheck = ocheck + descr;				// otcheck
                                }else if(counter == 5){
                                    CreditStatus1 = CreditStatus1 + descr;				// Credit Status
                                }
                            }
                        }

                        db.execSQL("Update customers set creditline = '"+ Float.parseFloat(creditlimit) +"',balcredit = '"+ balcredit +"',ocheck = '"+ ocheck +"',CreditStatus = '"+ CreditStatus1 +"' where cid = '"+ customercode +"';");
                        Toast.makeText(getApplicationContext(), "Successfully Update New Credit line : " + customercode, Toast.LENGTH_LONG).show();

                        float ac = 0;

                        Cursor c1 = db.rawQuery("select * from customers where cid = '"+ custid.getText().toString() +"'", null);
                        c1.moveToFirst();
                        int cnt1 =  c1.getCount();
                        Toast.makeText(getApplicationContext(), "Updated Customer Credit line : " + custid.getText().toString(), Toast.LENGTH_LONG).show();

                        if(cnt1 > 0){

                            tvcreditlimit.setText("" + Float.parseFloat(c1.getString(35)));  // credit line
                            tvbalcredit.setText("" + Float.parseFloat(c1.getString(36)));			// not paid invoice
                            tvocheck.setText("" + c1.getString(37));									// out standing
                            ac = Float.parseFloat(c1.getString(35)) - (Float.parseFloat(c1.getString(37)) + Float.parseFloat(c1.getString(36)));
                            tvavailcredit.setText("" + ac);

                            termstatus.setText("" + c1.getString(38));									// credit status
                        }
                    }
                } // end if of checking valid number to have acces in systax
            }
        }
    };

    @Override
    protected void onResume() {
        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause() {
        //---unregister the receiver---
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdetails);

        setupToolbar("Payment Details");
        // Force menu to appear
        invalidateOptionsMenu();

        try {
            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            sppaymenttype = (Spinner) findViewById(R.id.sppaymenttypepnew);
            spBankInitial = (Spinner) findViewById(R.id.spbankinitialppnew);
            //spcustomer = (Spinner) findViewById(R.id.spcustomerlist);

            edCheckNumber = (EditText) findViewById(R.id.etchecknumberp);
            edAccountNumber = (EditText) findViewById(R.id.etaccountnumberp);
            edCheckDate = (EditText) findViewById(R.id.etcheckdatep);
            edAmount = (EditText) findViewById(R.id.etamountp);

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            getnum = (EditText) findViewById(R.id.etinvoicenumberpnew);
            getnum.setText(getIntent().getExtras().getString("invnumber").toUpperCase());

            String invamountvalue = "" + getIntent().getExtras().getString("invoicetotalamount").toUpperCase();
            //	Toast.makeText(getApplicationContext(), "Please select Payment Type!" + invamountvalue,Toast.LENGTH_LONG).show();

            invoiceamount = (TextView) findViewById(R.id.tvtotalinvamountpnew);
            DecimalFormat formatter1 = new DecimalFormat("###,###,###.##");
            invoiceamount.setText(invamountvalue);

            //Databasename = getIntent().getExtras().getString("Databasename").toUpperCase();

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            edBankInitialdtl = (EditText) findViewById(R.id.etbankinitialpnewdtl);
            edCheckNumberdtl = (EditText) findViewById(R.id.etchecknumberpnewdtl);
            edAccountNumberdtl = (EditText) findViewById(R.id.etaccountpnewdtl);
            edCheckDatedtl = (EditText) findViewById(R.id.etcheckdatepnewdtl);
            edAmountdtl = (EditText) findViewById(R.id.etamountpnewdtl);
            edpaymenttypedtl = (EditText) findViewById(R.id.etpaymenttypepnewdtl);

            totalinvamount = (TextView) findViewById(R.id.tvtotalpaymentpnew);
            //totalinvamount.setText(getIntent().getExtras().getString("totalamount").toUpperCase());
            //details.setText("");

//            btsendsave = (Button) findViewById(R.id.btsendsavepnew);
//            btsendsave.setOnClickListener(this);

            custid = (TextView) findViewById(R.id.tvcodepnew);
            custname = (TextView) findViewById(R.id.tvcustomernamepnew);

            custid.setText(getIntent().getExtras().getString("customerid"));
            custname.setText(getIntent().getExtras().getString("customername").toUpperCase());

            countpro = 0;

            btaddtodetails =  (Button) findViewById(R.id.btadddetailspnewinv);
            btaddtodetails.setOnClickListener(this);

            btdeleteitem = (Button) findViewById(R.id.btdeleteitempnew);
            btdeleteitem.setOnClickListener(this);

            Cursor ctemp = db.rawQuery("select * from PaymentDetailsTemp", null);
            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            if(cnt > 0){

                String paymentTypeval = "";
                String bankinitialval = "";
                String checknumberval = "";
                String accountnumberval = "";
                String checkdateval = "";
                String amountval = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    tinvamount = tinvamount + ctemp.getFloat(5);
                    //	toqty = toqty + ctemp.getInt(4);

                    paymentTypeval = paymentTypeval + ctemp.getString(0) + "\n";
                    bankinitialval = bankinitialval + ctemp.getString(1) + "\n";
                    checknumberval = checknumberval + ctemp.getString(2)  + "\n";
                    accountnumberval = accountnumberval + ctemp.getString(3) + "\n";
                    checkdateval = checkdateval + ctemp.getString(4) + "\n";
                    amountval = amountval + ctemp.getString(5) + "\n";

                } while (ctemp.moveToNext());

                edpaymenttypedtl.setText(paymentTypeval);
                edBankInitialdtl.setText(bankinitialval);
                edCheckNumberdtl.setText(checknumberval);
                edAccountNumberdtl.setText(accountnumberval);
                edCheckDatedtl.setText(checkdateval);
                edAmountdtl.setText(amountval);

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

            }else{
                edpaymenttypedtl.setText("");
                edBankInitialdtl.setText("");
                edCheckNumberdtl.setText("");
                edAccountNumberdtl.setText("");
                edCheckDatedtl.setText("");
                edAmountdtl.setText("");

                totalinvamount.setText("0");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"" + e ,Toast.LENGTH_LONG).show();
        }
    }			// end oncreate

    //private void checkunitprice(){

    //}

    @Override
    public void onClick(View v) {  //
        // TODO Auto-generated method stub
        // inser broken seal

        tinvamount = 0;
        theMessage = "";

        validation = "";

        theMessage = validation;
        Detailsinvoice = "";

        //	prefixval = getprefix.getSelectedItem().toString();
        //	Invnumberval = getnum.getText().toString();

        checkvalidation = true;

        if(v.getId() == R.id.btdeleteitempnew){ //delete item

            db.execSQL("delete from PaymentDetailsTemp where PAYMENTTYPE like '"+ sppaymenttype.getSelectedItem().toString() +"' ;");

            int toqty = 0;

            Toast.makeText(getApplicationContext(), sppaymenttype.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            //	Detailsinvoice = "QTY                    Description                        Unitprice     T-Amount  " + "\n";

            Detailsinvoice = Detailsinvoice + "\n";
            Cursor ctemp = db.rawQuery("select * from PaymentDetailsTemp", null);
            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            if(cnt > 0){

                String paymentTypeval = "";
                String bankinitialval = "";
                String checknumberval = "";
                String accountnumberval = "";
                String checkdateval = "";
                String amountval = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    tinvamount = tinvamount + ctemp.getFloat(5);
                    //	toqty = toqty + ctemp.getInt(4);

                    paymentTypeval = paymentTypeval + ctemp.getString(0) + "\n";
                    bankinitialval = bankinitialval + ctemp.getString(1) + "\n";
                    checknumberval = checknumberval + ctemp.getString(2)  + "\n";
                    accountnumberval = accountnumberval + ctemp.getString(3) + "\n";
                    checkdateval = checkdateval + ctemp.getString(4) + "\n";
                    amountval = amountval + ctemp.getString(5) + "\n";

                } while (ctemp.moveToNext());

                edpaymenttypedtl.setText(paymentTypeval);
                edBankInitialdtl.setText(bankinitialval);
                edCheckNumberdtl.setText(checknumberval);
                edAccountNumberdtl.setText(accountnumberval);
                edCheckDatedtl.setText(checkdateval);
                edAmountdtl.setText(amountval);

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

            }else{
                edpaymenttypedtl.setText("");
                edBankInitialdtl.setText("");
                edCheckNumberdtl.setText("");
                edAccountNumberdtl.setText("");
                edCheckDatedtl.setText("");
                edAmountdtl.setText("");

                totalinvamount.setText("");
            }
        }

        if(v.getId() == R.id.btadddetailspnewinv){  // add item

            float purchaseprice = 0;

            //  	selectQuery = "select * from PaymentDetailsTemp where PaymentType like '"+ spproductnew.getSelectedItem().toString() +"'  and salesmanid = '"+ SalesmanID +"' ";

            tempstring = sppaymenttype.getSelectedItem().toString();   // payment type
            if("".equals(tempstring)){
                Toast.makeText(getApplicationContext(), "Please select Payment Type!",Toast.LENGTH_LONG).show();
            }else {
                checkvalidation = true;

                if("PDC".equals(tempstring)){
                    Cursor ctemp4 = db.rawQuery("select * from PaymentDetailsTemp  where PaymentType like '"+ sppaymenttype.getSelectedItem().toString() +"' and checknumber = '"+ edCheckNumber.getText().toString() +"' ", null);
                    ctemp4.moveToFirst();
                    int cnt3 =  ctemp4.getCount();
                    if(cnt3 > 0){
                        Toast.makeText(getApplicationContext(), "Check Number is already in the list!",Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }

                }else{
                    Cursor ctemp4 = db.rawQuery("select * from PaymentDetailsTemp  where PaymentType like '"+ sppaymenttype.getSelectedItem().toString() +"' ", null);
                    ctemp4.moveToFirst();
                    int cnt3 =  ctemp4.getCount();
                    if(cnt3 > 0){
                        Toast.makeText(getApplicationContext(), "Cash Payment is already in the list!",Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }
                }

                if("PDC".equals(tempstring)){
                    tempstring = spBankInitial.getSelectedItem().toString();   // Bank Initial
                    if("".equals(tempstring)){
                        Toast.makeText(getApplicationContext(), "Please input Bank Initial!",Toast.LENGTH_LONG).show();
                        checkvalidation = false;
                    }else{
                        tempstring = edCheckNumber.getText().toString();   // check number
                        if("".equals(tempstring)){
                            Toast.makeText(getApplicationContext(), "Please inputCheck Number!",Toast.LENGTH_LONG).show();
                            checkvalidation = false;
                        }else{
                            tempstring = edAccountNumber.getText().toString();   // account number
                            if("".equals(tempstring)){
                                Toast.makeText(getApplicationContext(), "Please input Account Number!",Toast.LENGTH_LONG).show();
                                checkvalidation = false;
                            }else{
                                tempstring = edCheckDate.getText().toString();   // account number
                                if("".equals(tempstring)){
                                    Toast.makeText(getApplicationContext(), "Please input Check Date!",Toast.LENGTH_LONG).show();
                                    checkvalidation = false;
                                }else{

                                }
                            }
                        }
                    }
                }

                tempstring = edAmount.getText().toString();   // amount
                if("".equals(tempstring)){
                    Toast.makeText(getApplicationContext(), "Please input Amount!",Toast.LENGTH_LONG).show();
                    checkvalidation = false;
                }

                // ADD TO DETAILS
                if(checkvalidation){

                    // save to invoice temp
                    tempstring = sppaymenttype.getSelectedItem().toString();   // payment type
                    if("PDC".equals(tempstring)){
                        String datecheck = edCheckDate.getText().toString();
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                            String currentDateandTime = sdf.format(datecheck);
                            datecheck = "" + currentDateandTime;
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }

                        // 	Toast.makeText(getApplicationContext(), "Please input Amount!" + datecheck,Toast.LENGTH_LONG).show();
                        db.execSQL("Insert into PaymentDetailsTemp values('"+ sppaymenttype.getSelectedItem().toString() + "','"+ spBankInitial.getSelectedItem().toString() +"','"+ edCheckNumber.getText().toString() +"','"+ edAccountNumber.getText().toString() +"','"+ datecheck +"','"+ edAmount.getText().toString() +"');");
                    }else {
                        db.execSQL("Insert into PaymentDetailsTemp values('"+ sppaymenttype.getSelectedItem().toString() + "','','','','','"+ edAmount.getText().toString() +"');");
                    }

                    Cursor ctemp = db.rawQuery("select * from PaymentDetailsTemp", null);
                    ctemp.moveToFirst();
                    int cnt =  ctemp.getCount();
                    if(cnt > 0){

                        String paymentTypeval = "";
                        String bankinitialval = "";
                        String checknumberval = "";
                        String accountnumberval = "";
                        String checkdateval = "";
                        String amountval = "";

                        do {
                            // 	lables.add(ctemp.getString(1));

                            tinvamount = tinvamount + ctemp.getFloat(5);
                            //	toqty = toqty + ctemp.getInt(4);

                            paymentTypeval = paymentTypeval + ctemp.getString(0) + "\n";
                            bankinitialval = bankinitialval + ctemp.getString(1) + "\n";
                            checknumberval = checknumberval + ctemp.getString(2)  + "\n";
                            accountnumberval = accountnumberval + ctemp.getString(3) + "\n";
                            checkdateval = checkdateval + ctemp.getString(4) + "\n";
                            amountval = amountval + ctemp.getString(5) + "\n";

                        } while (ctemp.moveToNext());

                        edpaymenttypedtl.setText(paymentTypeval);
                        edBankInitialdtl.setText(bankinitialval);
                        edCheckNumberdtl.setText(checknumberval);
                        edAccountNumberdtl.setText(accountnumberval);
                        edCheckDatedtl.setText(checkdateval);
                        edAmountdtl.setText(amountval);

                        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                        totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

                    }else{
                        edpaymenttypedtl.setText("");
                        edBankInitialdtl.setText("");
                        edCheckNumberdtl.setText("");
                        edAccountNumberdtl.setText("");
                        edCheckDatedtl.setText("");
                        edAmountdtl.setText("");

                        totalinvamount.setText("");
                    }
                }
            }
        }

        if(v.getId() == R.id.btsendsave){  // save send
            PaymentDetails.this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
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
                //	String altitiude =  ""+  location.getAltitude();
                //	String accuracy = ""+ location.getAccuracy();
                //	String time =  ""+ location.getTime();

                //Toast.makeText(getApplicationContext(),	"" + longitude + accuracy, Toast.LENGTH_LONG).show();

                //theMobNo = phonenumber.getText().toString();

                //theMessage = theMessage + "/" + latitude + "/" + longitude;

                tvOlat.setText(latitude);
                tvOlong.setText(longitude);

                //progress.setVisibility(View.GONE);
            }else{
                Toast.makeText(getApplicationContext(), "GPS Not Enable or Cant Get Location!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    public void loadlistofproducts() {

        // Spinner Drop down elements
        lables = new ArrayList<String>();

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //spproductnew.setAdapter(dataAdapter);
    }

    //---sends an SMS message to another device---
    public void sendSMS(String phoneNumber, String message) {

        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

            //---when the SMS has been sent---
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
                }, new IntentFilter(SENT), Context.RECEIVER_NOT_EXPORTED);
            }

            //---when the SMS has been delivered---
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
                }, new IntentFilter(DELIVERED), Context.RECEIVER_NOT_EXPORTED);
            }

            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            Toast.makeText(getApplicationContext(), "SMS Sent! " ,
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!" + ", eRROR :" + e  ,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
