package com.marsIT.marsSyntaxApp.VanSelling;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

@SuppressWarnings("deprecation")
public class VansellingLoadingPlan extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String itemcode;
    private String withtankloan;
    private String DatabaseName;
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
    private TextView termstatus;
    private TextView um;
    private TextView transinv;
    private EditText unitprice;
    private CheckBox withtax;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private Spinner spctype;
    private Spinner spproductnew;
    private Spinner sptermssp;
    private Spinner spdaystermsp;
    private Spinner spoutlet;
    private Button btaddtoinvoice;
    private Button btsendsave;
    private Button btdeleteitem1;
    private Button btbsinsert;
    private Button btbsdelete;
    private Button btsendrequest;
    private boolean issave;
    private Integer countpro;
    private Integer countpro1;
    final Context context = this;
    private EditText qty1;
    private EditText prodesc;
    private EditText itemprice;
    private EditText tamount1;
    private String Detailsinvoice;
    private String Detailsinvoice1;
    private String Detailsinvoice2;
    private String Detailsinvoice3;
    private String Detailsinvoice4;
    private String Detailsinvoice5;
    private String Detailsinvoice6;
    private String Detailsinvoice7;
    private String prefixval;
    private String Invnumberval;
    private String outlettrans;
    private Integer InvNum;
    private float tinvamount;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vansellingloadingplan);

        setupToolbar("VanSelling Loading PLan");
        // Force menu to appear
        invalidateOptionsMenu();

        try {
            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            spproductnew = (Spinner) findViewById(R.id.spproductnewvl);

            spoutlet= (Spinner) findViewById(R.id.spoutletnewinvvl);
            //spcustomer = (Spinner) findViewById(R.id.spcustomerlist);

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            Cursor ctemp = db.rawQuery("select count(refid) from loadingplan", null);
            ctemp.moveToFirst();
            InvNum =  Integer.parseInt(ctemp.getString(0)) + 2;

            issave = false;
            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);
            // tablename
            db.execSQL("CREATE TABLE IF NOT EXISTS TempInvoice" +
                    "(Refid VARCHAR," +
                    "productid VARCHAR," +
                    "Description Varchar," +
                    "Unitprice FLoat," +
                    "Qty integer," +
                    "TotalAmount Float); ");

            db.execSQL("delete from TempInvoice ; ");

            Cursor c1 = db.rawQuery("select * from products order by description", null);
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

            // for out loop
            Cursor c3 = db.rawQuery("select distinct(outlet) as ou from products order by description", null);
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
                        }else if("CC".equals(tempstring)){
                            lables.add("CENTURY");
                        }else if("UC".equals(tempstring)){
                            lables.add("URC");
                        }else if("UB".equals(tempstring)){
                            lables.add("URC-BEV");
                        }

                    } while (c3.moveToNext());
                }

                c1.close();
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

                        selectQuery = "Select * from products where outlet like '" + tempstring + "' order by description ";
                        loadlistofproducts();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch = (EditText) findViewById(R.id.etqueryproductvl);
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
                    }else if("CENTURY".equals(tempstring)){
                        outlettrans = "CC";
                    }else if("URC".equals(tempstring)){
                        outlettrans = "UC";
                    }else if("URC-BEV".equals(tempstring)){
                        outlettrans = "UB";
                    }

                    Cursor c3 = db.rawQuery("SELECT  description FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description", null);
                    c3.moveToFirst();
                    int cntoutlet =  c3.getCount();
                    if(cntoutlet > 0){
                        selectQuery = "SELECT * FROM products where description like '%"+ cs  +"%' and outlet = '"+ outlettrans +"' ORDER BY description";
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

            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            qty1 = (EditText) findViewById(R.id.etqtynewvl);
            prodesc = (EditText) findViewById(R.id.etdecriptionnewvl);
            itemprice = (EditText) findViewById(R.id.etunitpricenewvl);
            tamount1 = (EditText) findViewById(R.id.etamountnewvl);

            //osnumber = (EditText) findViewById(R.id.etosnumbernewinv);

            totalinvqty = (TextView) findViewById(R.id.tvtotalinvoiceqtyvl);
            totalinvamount = (TextView) findViewById(R.id.tvtotalinvoicamountvl);

            //details.setText("");

            btsendsave = (Button) findViewById(R.id.btsendsavevl);
            btsendsave.setOnClickListener(this);

            //	custid = (TextView) findViewById(R.id.tvcodenew);
            //	custname = (TextView) findViewById(R.id.tvcustomernamenew);

            //	custid.setText(getIntent().getExtras().getString("cidinv"));
            //	custname.setText(getIntent().getExtras().getString("cnameinv").toUpperCase());

            um = (TextView) findViewById(R.id.tvumnewinvoicevl);

            countpro = 0;
            countpro = 0;

            //}

            //	if("CASA".equals(tempstring)){
            //		getprefix.setSelection(1);
            //	}
            //if("CRE".equals(tempstring)){
            //	getprefix.setSelection(2);
            //	}
            //	if("CHI".equals(tempstring)){
            //		getprefix.setSelection(3);
            //	}

            quantity =(EditText) findViewById(R.id.etquantitynewvl);
            unitprice = (EditText) findViewById(R.id.etunitpricevl);
            unitprice.setEnabled(true);

            spproductnew.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (countpro == 0){
                        countpro = 1;
                    }else{

                        tempstring = spproductnew.getSelectedItem().toString();

                        Cursor c1 = db.rawQuery("Select * from products where description like '"+ tempstring +"' ", null);
                        c1.moveToFirst();
                        int cnt =  c1.getCount();
                        if(cnt > 0){
                            //Cursor c3 = db.rawQuery("Select * from itempriceoveride where itemcode like '"+ c1.getString(0) +"' and customerid = '"+ custid.getText().toString() +"'", null);
                            //   c3.moveToFirst();
                            try{
                                unitprice.setText("0");  // price come from the syntax
                                um.setText(c1.getString(5));
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
            Cursor c5 = db.rawQuery("Select * from products where description like '"+ tempstring +"' ", null);
            c5.moveToFirst();
            int cnt =  c5.getCount();
            if(cnt > 0){

                //	Toast.makeText(getApplicationContext(), "not found pricelist!" + spctype.getSelectedItem().toString() + " " + c5.getString(0),Toast.LENGTH_LONG).show();
                unitprice.setText("0");
                um.setText(c5.getString(5));
            }

            btaddtoinvoice =  (Button) findViewById(R.id.btadddetailsnewinvvl);
            btaddtoinvoice.setOnClickListener(this);

            btdeleteitem1 = (Button) findViewById(R.id.btdeleteitemnewvl);
            btdeleteitem1.setOnClickListener(this);

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 =  c2.getCount();
            if(cnt2 > 0){
                DatabaseName = c2.getString(6);	      // database name
            }else{
                DatabaseName = "";
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

        if(v.getId() == R.id.btdeleteitemnewvl){ //delete item

            db.execSQL("delete from TempInvoice where description like '"+ spproductnew.getSelectedItem().toString() +"';");

            int toqty = 0;

            Toast.makeText(getApplicationContext(), spproductnew.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            //	Detailsinvoice = "QTY                    Description                        Unitprice     T-Amount  " + "\n";

            Detailsinvoice = Detailsinvoice + "\n";
            Cursor ctemp = db.rawQuery("select * from TempInvoice", null);
            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            if(cnt > 0){

                String qtytemp = "";
                String desctemp = "";
                String pricetemp = "";
                String amounttemp = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    tinvamount = tinvamount + ctemp.getFloat(5);
                    toqty = toqty + ctemp.getInt(4);

                    String sb = "";
                    qtytemp = qtytemp + ctemp.getString(4) + "\n";
                    Detailsinvoice = Detailsinvoice + ctemp.getString(4);
                    int valqtylenth = 0;

                    String latval = ctemp.getString(2);
                    valqtylenth = latval.length();
                    for(int i = 0; i < 40; i++)
                    {
                        if(valqtylenth < 34){
                            if(valqtylenth - 1 >= i){
                                char descr =  latval.charAt(i);
                                sb = sb + descr;
                            }else{
                                //sb = sb + " ";
                            }
                        }else if(valqtylenth >34){
                            if(i < 34){
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
                    pricetemp = pricetemp + ctemp.getString(3) + "\n";
                    amounttemp = amounttemp + ctemp.getString(5) + "\n";

                } while (ctemp.moveToNext());

                qty1.setText(qtytemp);
                prodesc.setText(desctemp);
                itemprice.setText(pricetemp);
                tamount1.setText(amounttemp);

                totalinvqty.setText(String.valueOf(toqty));

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

            }else{
                qty1.setText("");
                prodesc.setText("");
                itemprice.setText("");
                tamount1.setText("");

                totalinvqty.setText("");
                totalinvamount.setText("");
            }
        }

        if(v.getId() == R.id.btadddetailsnewinvvl){  // add item

            float purchaseprice = 0;

            Cursor c = db.rawQuery("select * from products where description like '"+ spproductnew.getSelectedItem().toString() +"' ", null);
            c.moveToFirst();
            int cnt =  c.getCount();
            if(cnt > 0){  // check if product is exist
                //purchaseprice = Float.parseFloat(c.getString(2));
                //int invleft = c.getInt(4); // quantity left
                tempstring = quantity.getText().toString();   // check if have quantiy input
                if("".equals(tempstring)){
                    Toast.makeText(getApplicationContext(), "Please input Quantity!",Toast.LENGTH_LONG).show();
                }else{
                    Cursor c3 = db.rawQuery("select * from TempInvoice where description like '"+ spproductnew.getSelectedItem().toString() +"'", null);
                    c3.moveToFirst();
                    cnt =  c3.getCount();
                    if(cnt > 2){   // check if item is already in the list
                        Toast.makeText(getApplicationContext(), "Item is Already in the list!",Toast.LENGTH_LONG).show();
                    }else{
                        String NumID = c.getString(0);       // product id
                        String productDescription = c.getString(1); // product description

                        Integer qty = Integer.parseInt(tempstring); // check if quantity input is not zero
                        if(qty < 0){
                            Toast.makeText(getApplicationContext(), "Zero Quantity not Allowed!",Toast.LENGTH_LONG).show();
                        }else{
                            tempstring = unitprice.getText().toString();       // check unit price
                            if("".equals(tempstring)){
                                Toast.makeText(getApplicationContext(), "Please input Unit Price!",Toast.LENGTH_LONG).show();
                            }else{

                                //	float unitpriceinput = 0;
                                //	unitpriceinput =Float.parseFloat(unitprice.getText().toString());
                                //if(purchaseprice < unitpriceinput ){
                                //		Toast.makeText(getApplicationContext(), "Please input Unit Price!",Toast.LENGTH_LONG).show();
                                //	}
                                // check credit limit check

                                float totalinvoice = 0;
                                float cred = 1;
                                float tamount = Float.parseFloat(quantity.getText().toString()) * Float.parseFloat(unitprice.getText().toString());

                                if(totalinvoice > cred) {  // check if credit limit not exceed
                                    Toast.makeText(getApplicationContext(), "Credit Limit Exceed! Temporary total : " + totalinvoice,Toast.LENGTH_LONG).show();
                                }else{

                                    tempstring = "VL" + InvNum;
                                    if("".equals(tempstring)){
                                        Toast.makeText(getApplicationContext(), "Please input Invoice Number!",Toast.LENGTH_LONG).show();
                                    }else{

                                        // save to invoice temp
                                        db.execSQL("Insert into TempInvoice values('"+ tempstring + "','"+ NumID +"','"+ productDescription +"',"+ Float.parseFloat(unitprice.getText().toString()) +","+ Integer.parseInt(quantity.getText().toString()) +","+ tamount +");");

                                        Cursor ctemp = db.rawQuery("select * from TempInvoice order by description", null);
                                        ctemp.moveToFirst();
                                        cnt =  ctemp.getCount();
                                        if(cnt > 0){

                                            String qtytemp = "";
                                            String desctemp = "";
                                            String pricetemp = "";
                                            String amounttemp = "";
                                            Integer toqty = 0;

                                            do {
                                                // 	lables.add(ctemp.getString(1));

                                                tinvamount = tinvamount + ctemp.getFloat(5);  // total amount per item
                                                toqty = toqty + ctemp.getInt(4);				// quantity

                                                String sb = "";
                                                String desc = ctemp.getString(4);

                                                //	Toast.makeText(getApplicationContext(), desc,Toast.LENGTH_LONG).show();

                                                qtytemp = qtytemp + desc + "\n";
                                                //Detailsinvoice = Detailsinvoice + ctemp.getString(4);
                                                int valqtylenth = 0;

                                                String latval = ctemp.getString(2);		// product description
                                                valqtylenth = latval.length();			// length of product description
                                                for(int i = 0; i < 40; i++)
                                                {
                                                    if(valqtylenth < 34){
                                                        if(valqtylenth - 1 >= i){
                                                            char descr =  latval.charAt(i);
                                                            sb = sb + descr;
                                                        }else{
                                                            //sb = sb + " ";
                                                        }
                                                    }else if(valqtylenth > 34){
                                                        if(i < 34){
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
                                                pricetemp = pricetemp + ctemp.getString(3) + "\n";
                                                amounttemp = amounttemp + String.valueOf(ctemp.getFloat(5)) + "\n";

                                            } while (ctemp.moveToNext());
                                            //Toast.makeText(getApplicationContext(), "trap!"  ,Toast.LENGTH_LONG).show();

                                            ctemp.close();
                                            qty1.setText(qtytemp);
                                            prodesc.setText(desctemp);
                                            itemprice.setText(pricetemp);
                                            tamount1.setText(amounttemp);

                                            totalinvqty.setText(String.valueOf(toqty));
                                            DecimalFormat formatter = new DecimalFormat("###,###,###.##");

                                            totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));
                                        }
                                    } // end if else have invoice number input
                                }// end else check if credit limit not exceed
                            }
                        }
                    }
                }
            }
        }

        if(v.getId() == R.id.btsendsavevl){  // save send

            AlertDialog.Builder builder = new AlertDialog.Builder(VansellingLoadingPlan.this);
            builder.setMessage("Are you sure you want Save this Transaction?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    //MainActivity.this.finish();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
                    String currentDateandTime1 = sdf1.format(new Date());
                    SimpleDateFormat datetimesyntax = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    String datetimesyntaxval = datetimesyntax.format(new Date());

                    //	Toast.makeText(getApplicationContext(), "" + datetimesyntax,Toast.LENGTH_LONG).show();

                    String refid = "VL"  + InvNum;

                    Cursor invquery = db.rawQuery("Select * from loadingPlan where refid like '"+ refid +"'", null);

                    invquery.moveToFirst();
                    int countrec =  invquery.getCount();   // check if invoice already in used!
                    if(countrec > 0){
                        Toast.makeText(getApplicationContext(), "Invoice number is already used!",Toast.LENGTH_LONG).show();
                    }else{

                        if(checkvalidation){

                            // load and save the invoice details
                            Detailsinvoice = "";

                            Cursor invtemp = db.rawQuery("select * from TempInvoice order by description", null);
                            invtemp.moveToFirst();
                            int cnt2 =  invtemp.getCount();
                            if(cnt2 > 0){

                                String Tempsendsyntax = "";

                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                                String currentDateandTime = sdf.format(new Date());
                                float amtt = 0;
                                float tinvamount= 0;
                                String Productlist = "";

                                int countmsg = 0;
                                int valqtylenth = 0;

                                String Outlettype = "";

                                tempstring = spoutlet.getSelectedItem().toString();
                                if("LUBRICANTS".equals(tempstring)){
                                    Outlettype = "L";
                                }else if("AUTO-SUPPLY".equals(tempstring)){
                                    Outlettype = "A";
                                }else if("CENTURY".equals(tempstring)){
                                    Outlettype = "CC";
                                }else if("URC".equals(tempstring)){
                                    Outlettype = "UC";
                                }else if("URC-BEV".equals(tempstring)){
                                    Outlettype = "UB";
                                }

                                if("MARS2".equals(DatabaseName)){
                                    Detailsinvoice = "V2LOAD"  + "!" + refid + "!" + Outlettype + "!" + datetimesyntaxval + "!";
                                    Detailsinvoice1 = "V2LOAD1"  + "!" + refid + "!" ;
                                    Detailsinvoice2 = "V2LOAD2"  + "!" + refid + "!" ;
                                    Detailsinvoice3 = "V2LOAD3"  + "!" + refid + "!" ;
                                    Detailsinvoice4 = "V2LOAD4"  + "!" + refid + "!" ;
                                    Detailsinvoice5 = "V2LOAD5"  + "!" + refid + "!" ;
                                    Detailsinvoice6 = "V2LOAD6"  + "!" + refid + "!" ;
                                }else {
                                    Detailsinvoice = "VLOAD"  + "!" + refid + "!" + Outlettype + "!" + datetimesyntaxval + "!";
                                    Detailsinvoice1 = "VLOAD1"  + "!" + refid + "!" ;
                                    Detailsinvoice2 = "VLOAD2"  + "!" + refid + "!" ;
                                    Detailsinvoice3 = "VLOAD3"  + "!" + refid + "!" ;
                                    Detailsinvoice4 = "VLOAD4"  + "!" + refid + "!" ;
                                    Detailsinvoice5 = "VLOAD5"  + "!" + refid + "!" ;
                                    Detailsinvoice6 = "VLOAD6"  + "!" + refid + "!" ;
                                }

                                do {
                                    //    		amtt = amtt + ctemp.getFloat(5);

                                    //  		Cursor ctemp1 = db.rawQuery("select * from products where productid = '" + invtemp.getString(0) + "'", null);
                                    //		ctemp1.moveToFirst();
                                    //    cnt =  ctemp1.getCount();
                                    //  if(cnt > 0){
                                    //	// update the inventory of the item
                                    //int qtyleft = Integer.parseInt(ctemp1.getString(4)) - Integer.parseInt(ctemp.getString(4));
                                    //}

                                    tinvamount = tinvamount + invtemp.getFloat(5);
                                    if(countmsg <= 0){
                                        Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 1){
                                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 2){
                                        Tempsendsyntax = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 3){
                                        Tempsendsyntax = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 4){
                                        Tempsendsyntax = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 5){
                                        Tempsendsyntax = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }else if(countmsg <= 6){
                                        Tempsendsyntax = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;;
                                        valqtylenth =  Tempsendsyntax.length();
                                        if(valqtylenth > 160){
                                            countmsg = countmsg + 1;
                                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }else{
                                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                                        }
                                    }

                                    //Productlist =  Productlist + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;

                                    // save invoice details
                                    db.execSQL("Insert into LOADINGPLAN values" +
                                            "('" + refid + "','" +
                                            invtemp.getString(1) +"'," + 						// productid
                                            invtemp.getInt(4) +"," + 							// quantity
                                            invtemp.getFloat(3)+","+ 							// unitprice
                                            invtemp.getFloat(5) +",'"+ 							// total amount
                                            currentDateandTime +"','"+ 							// invoice date
                                            datetimesyntaxval +"','"  + Outlettype + "');");						// syntax date

                                } while (invtemp.moveToNext());
                                //   end save details

                                // query receiver number
                                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                                c1.moveToFirst();
                                int cnt =  c1.getCount();
                                if(cnt > 0){
                                    theMobNo = c1.getString(0);
                                }

                                if(countmsg <= 0){
                                    sendSMS(theMobNo,Detailsinvoice);
                                }else if(countmsg <= 1){
                                    sendSMS(theMobNo,Detailsinvoice);
                                    sendSMS(theMobNo,Detailsinvoice1);
                                    Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                                }else if(countmsg <= 2){
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
                                    sendSMS(theMobNo,Detailsinvoice);
                                    sendSMS(theMobNo,Detailsinvoice1);
                                    sendSMS(theMobNo,Detailsinvoice2);
                                    sendSMS(theMobNo,Detailsinvoice3);
                                    sendSMS(theMobNo,Detailsinvoice4);
                                    sendSMS(theMobNo,Detailsinvoice5);
                                }else if(countmsg <= 6){
                                    sendSMS(theMobNo,Detailsinvoice);
                                    sendSMS(theMobNo,Detailsinvoice1);
                                    sendSMS(theMobNo,Detailsinvoice2);
                                    sendSMS(theMobNo,Detailsinvoice3);
                                    sendSMS(theMobNo,Detailsinvoice4);
                                    sendSMS(theMobNo,Detailsinvoice5);
                                    sendSMS(theMobNo,Detailsinvoice6);
                                }

                                db.execSQL("delete from TempInvoice;");
                                qty1.setText("");
                                prodesc.setText("");
                                itemprice.setText("");
                                tamount1.setText("");

                                totalinvqty.setText("");
                                totalinvamount.setText("");

                                VansellingLoadingPlan.this.finish();

                                //    Toast.makeText(getApplicationContext(),"" + Detailsinvoice ,Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "No Details Found!",Toast.LENGTH_LONG).show();
                            }
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
        spproductnew.setAdapter(dataAdapter);
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
