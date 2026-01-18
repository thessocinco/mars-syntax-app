package com.marsIT.marsSyntaxApp.Inventory;

import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.AlertDialog.Builder;
//import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
//import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.BroadcastReceiver;
import android.content.Context;

import androidx.annotation.RequiresApi;

import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;

@SuppressWarnings("deprecation")
public class AddItem extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    static final String SCAN = "com.google.zxing.client.android.SCAN";
    private String tempstring;
    private String selectQuery;
//	private String longitude;
//	private String latitude;

    EditText inputSearch;
    EditText barcodeSearch;
    private EditText editRemarks;
    private EditText quantity;
    private EditText csquantity;
    private TextView custid;
    private TextView salesmannameid;
    private List<String> lables;
    private List<String> labless;
    private SQLiteDatabase	db;
    private TextView um;
    private TextView transinv;
    private TextView csfactor;
    private TextView csum;
    private EditText unitprice;
    private EditText csunitprice;
    private Spinner spproductnew;
    private Spinner spoutlet;
    private Spinner spcategory;
    private Button btsendsave;
    private Button btbarcode;
    private Button btcsbarcode;
    private Integer countpro;
    final Context context = this;
    private String Departmentcode;
    private String outlettrans;
    private String SalesmanID;
    IntentFilter intentFilter;

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

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

                        //	float ac = 0;
                        Cursor c1 = db.rawQuery("select * from customers where cid = '"+ custid.getText().toString() +"'", null);
                        c1.moveToFirst();
                        int cnt1 =  c1.getCount();
                        Toast.makeText(getApplicationContext(), "Updated Customer Credit line : " + custid.getText().toString(), Toast.LENGTH_LONG).show();

                        if(cnt1 > 0){

                            // out standing
                            //	ac = Float.parseFloat(c1.getString(35)) - (Float.parseFloat(c1.getString(37)) + Float.parseFloat(c1.getString(36)));
                            // credit status
                        }
                    }
                } // end if of checking valid number to have acces in systax
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU) // Broadcast Entry - Updated 2025
    @Override
    protected void onResume() { // Register the Receiver
        Log.d("New Invoice", "onResume: Registering the receiver");
        registerReceiver(notificationReceiver, new IntentFilter("SMS_RECEIVED_ACTION"), Context.RECEIVER_EXPORTED);
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
        setContentView(R.layout.additem);

        try {

            setupToolbar("Add Item");
            // Force menu to appear
            invalidateOptionsMenu();

            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            spproductnew = (Spinner) findViewById(R.id.spproductadditem);

            spoutlet= (Spinner) findViewById(R.id.spoutletadditem);
            spcategory= (Spinner) findViewById(R.id.spcategoryadditem);
            //spcustomer = (Spinner) findViewById(R.id.spcustomerlist);

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            transinv  = (TextView) findViewById(R.id.tvtransadditem);
            transinv.setText("Van-selling");

            SalesmanID = getIntent().getExtras().getString("salesmanidvalueadditem").toUpperCase();

            salesmannameid  = (TextView) findViewById(R.id.tvsalesmanidadditem);
            salesmannameid.setText("SalesmanID");

            btsendsave = (Button) findViewById(R.id.btsaveadditemInv);
            btsendsave.setOnClickListener(this);

            um = (TextView) findViewById(R.id.tvumadditem);
            csum = (TextView) findViewById(R.id.tvcsumadditem);

            csfactor = (TextView) findViewById(R.id.tvcsfactoradditem);

            countpro = 0;
            countpro = 0;

            quantity =(EditText) findViewById(R.id.etquantityadditem);
            csquantity =(EditText) findViewById(R.id.etcsquantityadditem);

            unitprice = (EditText) findViewById(R.id.etunitpriceadditem);
            csunitprice = (EditText) findViewById(R.id.etcsunitpriceadditem);

            String ProductQuery; // String ProductQuery = ""; - original

            ProductQuery = "select * from products2 order by description ";

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

            ProductQuery = "select distinct(outlet) as ou from products2 order by description";

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

            spoutlet.setOnItemSelectedListener(new OnItemSelectedListener(){

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

                        selectQuery = "Select * from products2 where outlet like '" + tempstring + "' order by description ";
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            ProductQuery = "select distinct(producttype) as ous from products2 order by producttype";

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

            spcategory.setOnItemSelectedListener(new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{
                        tempstring = spcategory.getSelectedItem().toString();
                        selectQuery = "Select * from products2 where producttype like '" + tempstring + "' order by description ";
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch = (EditText) findViewById(R.id.etqueryproductadditem);
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

                    selectQuery = "SELECT  description FROM products2 where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";

                    Cursor c3 = db.rawQuery(selectQuery, null);
                    c3.moveToFirst();
                    int cntoutlet =  c3.getCount();
                    if(cntoutlet > 0){

                        selectQuery = "SELECT * FROM products2 where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";

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

            barcodeSearch = (EditText) findViewById(R.id.etquerybarcodeadd);
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

            //details.setText("");

            unitprice.setEnabled(true);
            if("UC".equals(Departmentcode)){
                //	unitprice.setEnabled(false);
            }else if("CC".equals(Departmentcode)){
                //	unitprice.setEnabled(false);
            }else if("UB".equals(Departmentcode)){
                //	unitprice.setEnabled(false);
            }

            spproductnew.setOnItemSelectedListener(new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = spproductnew.getSelectedItem().toString();
                        selectQuery = "Select * from products2 where description like '"+ tempstring +"' ";

                        Cursor c1 = db.rawQuery(selectQuery, null);
                        c1.moveToFirst();
                        int cnt =  c1.getCount();
                        if(cnt > 0){
                            //Cursor c3 = db.rawQuery("Select * from itempriceoveride where itemcode like '"+ c1.getString(0) +"' and customerid = '"+ custid.getText().toString() +"'", null);
                            //   c3.moveToFirst();
                            try{
                                unitprice.setText(c1.getString(2));
                                //	unitprice.setEnabled(false);

                                if("0".equals(unitprice.getText().toString())){
                                    csfactor.setText(c1.getString(8));
                                }else{

                                    csfactor.setText(c1.getString(8));

                                    float cspriceunit = Float.parseFloat(csfactor.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                    DecimalFormat formatter = new DecimalFormat("#.00");
                                    csunitprice.setText(String.valueOf(formatter.format(cspriceunit)));

                                }
                                unitprice.setEnabled(true);
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
//            tempstring = spproductnew.getSelectedItem().toString();
            Object selectedItem = spproductnew.getSelectedItem();
            if (selectedItem != null) {
                tempstring = selectedItem.toString();
            } else {
                Toast.makeText(this, "No product selected.", Toast.LENGTH_LONG).show();
                return;
            }

            selectQuery = "Select * from products2 where description like '"+ tempstring +"' ";

            Cursor c5 = db.rawQuery(selectQuery, null);
            c5.moveToFirst();
            int cnt =  c5.getCount();
            if(cnt > 0){
                //	Toast.makeText(getApplicationContext(), "not found pricelist!" + spctype.getSelectedItem().toString() + " " + c5.getString(0),Toast.LENGTH_LONG).show();
                //	unitprice.setText("0");
                um.setText(c5.getString(5));
            }

            btbarcode = (Button) findViewById(R.id.btscanbarcodeadd);
            btbarcode.setOnClickListener(this);

            btcsbarcode = (Button) findViewById(R.id.btscanbarcodeaddcs);
            btcsbarcode.setOnClickListener(this);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"" + e ,Toast.LENGTH_LONG).show();
            Log.e("ERROR", "ADD ITEM ERROR: ", e);
        }
    }			// end oncreate

    //private void checkunitprice(){
    //}

    public void scanbar(View v){

        try{
            Intent in = new Intent(SCAN);
            in.putExtra("SCAN_MODE", "PRODUCT_MODE");

            startActivityForResult(in,0);

        } catch (ActivityNotFoundException e){
            // TODO: handle exception
            showDialog(AddItem.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, in);
        if(requestCode ==0){
            if(resultCode == RESULT_OK){
                String contents = in.getStringExtra("SCAN_RESULT");
                //  String format =  in.getStringExtra("SCAN_RESULT_FORMAT") ;
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

    @Override
    public void onClick(View v) {  //
        // TODO Auto-generated method stub

        // inser broken seal

        if(v.getId() == R.id.btscanbarcodeadd){
            try{

                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "PRODUCT_MODE");

                startActivityForResult(in,0);

            } catch (ActivityNotFoundException e){
                // TODO: handle exception
                showDialog(AddItem.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if(v.getId() == R.id.btscanbarcodeaddcs){
            try{

                Intent in = new Intent(SCAN);
                in.putExtra("SCAN_MODE", "INDUSTRIAL_MODE");

                startActivityForResult(in,0);

            } catch (ActivityNotFoundException e){
                // TODO: handle exception
                showDialog(AddItem.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }

        if(v.getId() == R.id.btsaveadditemInv){  // add item

            AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
            builder.setMessage("Are you sure you want to Add This Product "+ spproductnew.getSelectedItem().toString() +" to your Vanselling Inventory?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    selectQuery = "select * from products where description like '"+ spproductnew.getSelectedItem().toString() +"' AND SALESMANID = '"+ SalesmanID +"' ";

                    boolean TrappingError = true;

                    Cursor c = db.rawQuery(selectQuery, null);
                    c.moveToFirst();
                    int cnt =  c.getCount();
                    if(cnt > 0){  // check if product is exist
                        Toast.makeText(getApplicationContext(), "Product is Already Exist in your Van-selling Inventory!",Toast.LENGTH_LONG).show();
                        TrappingError = false;
                    }

                    float PCInv = 0;
                    float PCInvUnitPrice = 0;

                    //    float csfactorinventory = 0 ;
                    //    String PricePerPC = "";

                    if("".equalsIgnoreCase(csquantity.getText().toString())){
                        if("".equalsIgnoreCase(quantity.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Please Input Inventory PC!",Toast.LENGTH_LONG).show();
                            TrappingError = false;
                        }else{
                            tempstring = unitprice.getText().toString();       // check unit price
                            if("".equals(tempstring)){
                                Toast.makeText(getApplicationContext(), "Please input PC Unit Price!",Toast.LENGTH_LONG).show();
                                TrappingError = false;
                            }else{
                                PCInv = Float.parseFloat(quantity.getText().toString());
                                PCInvUnitPrice = Float.parseFloat(unitprice.getText().toString());
                            }
                        }
                    }else{
                        tempstring = csunitprice.getText().toString();       // check cs unit price
                        if("".equals(tempstring)){
                            Toast.makeText(getApplicationContext(), "Please input CS Unit Price!",Toast.LENGTH_LONG).show();
                            TrappingError = false;
                        }else{
                            //	csfactorinventory = Float.parseFloat(csfactor.getText().toString());
                            //	PCInv = Float.parseFloat(csquantity.getText().toString())  ;
                            //	PCInv = PCInv * csfactorinventory;
                            //	PCInvUnitPrice = Float.parseFloat(csunitprice.getText().toString());
                            //	PCInvUnitPrice = PCInvUnitPrice / csfactorinventory ;

                            //	Toast.makeText(getApplicationContext(), " CS Unit Price!" + csunitprice.getText().toString() ,Toast.LENGTH_LONG).show();
                            PCInvUnitPrice =  Float.parseFloat(csunitprice.getText().toString()) / Float.parseFloat(csfactor.getText().toString());
                            PCInv =Float.parseFloat(csquantity.getText().toString()) * Float.parseFloat(csfactor.getText().toString());

                            if("".equalsIgnoreCase(quantity.getText().toString())){
                            }else{
                                tempstring = quantity.getText().toString();       // check unit price
                                if("0".equals(tempstring)){
                                }else{
                                    PCInv = PCInv + Float.parseFloat(quantity.getText().toString());
                                }
                            }
                            //	Toast.makeText(getApplicationContext(), " CS QTY!" + csquantity.getText().toString() ,Toast.LENGTH_LONG).show();
                            //	Toast.makeText(getApplicationContext(), " CS csfactor!" + csfactor.getText().toString() ,Toast.LENGTH_LONG).show();
                            //	DecimalFormat formatter = new DecimalFormat("#.000000");
                            //	PricePerPC = String.valueOf(formatter.format(PCInvUnitPrice));
                        }
                    }

                    //   Toast.makeText(getApplicationContext(), "pass trap 1!",Toast.LENGTH_LONG).show();
                    if(TrappingError){
                        Cursor c3 = db.rawQuery("select * from products2 where description like '"+ spproductnew.getSelectedItem().toString() +"'", null);
                        c3.moveToFirst();
                        cnt =  c3.getCount();
                        if(cnt < 1){   // check if item is already in the list
                            Toast.makeText(getApplicationContext(), "Item not found!",Toast.LENGTH_LONG).show();
                        }else{

                            //	Toast.makeText(getApplicationContext(), "pass trap 2!",Toast.LENGTH_LONG).show();

                            String ProductID = c3.getString(0);       // product id
                            String productDescription = c3.getString(1); // product description
                            String UM = c3.getString(5);       // product id
                            String itemDept = c3.getString(6);       // product id
                            String CsFactorINv = c3.getString(8);       // product id

                            String CsUOMinv = c3.getString(9);       // product id

                            // save products
                            db.execSQL("Insert into products values" +
                                    "('" + ProductID + "','" +							// productid
                                    productDescription + "','" +  						// product Description
                                    PCInvUnitPrice +"'," + 								// unit price
                                    PCInv +"," + 										// inventory
                                    PCInv +",'"+ 										// inventory
                                    UM +"','"+ 											// pc UOM
                                    itemDept +"','"+ 									// outlet
                                    SalesmanID +"','" +									// SalesmanID
                                    CsFactorINv +"','" +  								// cs factor
                                    CsUOMinv +"','" + c3.getString(10) + "','" + c3.getString(11) + "','" + c3.getString(12) + "');");						// syntax date

                            Toast.makeText(getApplicationContext(), "The Item "+ productDescription +" was successfully add to your inventory!",Toast.LENGTH_LONG).show();
                        }
                    }
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

    public void loadlistofproducts(){

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spproductnew.setAdapter(dataAdapter);
    }
    // sends an SMS message to another device
}
