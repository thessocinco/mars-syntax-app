package com.marsIT.marsSyntaxApp.Inventory;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.content.BroadcastReceiver;
import android.content.Context;

import androidx.annotation.RequiresApi;

import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

public class AddItemInventory extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private String selectQuery;
    EditText inputSearch;
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
    private Integer countpro;
    final Context context = this;
    private String Departmentcode;
    private String outlettrans;
    private String SalesmanID;
    IntentFilter intentFilter;

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent == null || intent.getExtras() == null) return;

                Bundle extras = intent.getExtras();
                String syntaxsms = extras.getString("sms");
                String cellphonenumber = extras.getString("cellnumber");

                if (db == null) {
                    db = context.openOrCreateDatabase("MapDb", Context.MODE_PRIVATE, null);
                }

                Cursor queryvalidnumber = db.rawQuery("SELECT * FROM validnumber WHERE num = ?", new String[]{cellphonenumber});
                int cnt = queryvalidnumber.getCount();
                if (cnt > 0 && queryvalidnumber.moveToFirst()) {
                    String NumberOwner = queryvalidnumber.getString(1);
                    String stchar = syntaxsms.length() >= 6 ? syntaxsms.substring(0, 6) : "";

                    int valqtylenth = syntaxsms.length();
                    String Compaire = "";
                    int counter = 0;

                    if ("UPRICE".equals(stchar)) {
                        String itemcode = "", unitprice = "", customercodep = "";

                        for (int i = 6; i < valqtylenth; i++) {
                            char ch = syntaxsms.charAt(i);
                            Compaire = String.valueOf(ch);
                            if ("/".equals(Compaire)) {
                                counter++;
                            } else {
                                if (counter == 1) itemcode += ch;
                                else if (counter == 2) unitprice += ch;
                                else if (counter == 3) customercodep += ch;
                            }
                        }

                        Cursor c2 = db.rawQuery("SELECT * FROM products WHERE productid = ?", new String[]{itemcode});
                        if (c2.getCount() > 0 && c2.moveToFirst()) {
                            db.execSQL("INSERT INTO itempriceoveride VALUES (?, ?, ?)",
                                    new Object[]{c2.getString(0), customercodep, Double.parseDouble(unitprice)});
                            Toast.makeText(context, "Price Override: " + c2.getString(1) + " by: " + NumberOwner, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Incorrect Syntax, Item not found", Toast.LENGTH_LONG).show();
                        }
                    } else if ("URECEI".equals(stchar)) {
                        String receivernum = "";
                        for (int i = 6; i < valqtylenth; i++) {
                            char ch = syntaxsms.charAt(i);
                            if ("/".equals(String.valueOf(ch))) counter++;
                            else if (counter == 1) receivernum += ch;
                        }

                        db.execSQL("UPDATE receivernumber SET num = ?", new Object[]{receivernum});
                        Toast.makeText(context, "Updated Receiver number: " + receivernum, Toast.LENGTH_LONG).show();

                    } else if ("ADDNUM".equals(stchar)) {
                        String receivernum = "", name = "";
                        for (int i = 6; i < valqtylenth; i++) {
                            char ch = syntaxsms.charAt(i);
                            if ("/".equals(String.valueOf(ch))) counter++;
                            else {
                                if (counter == 1) receivernum += ch;
                                else if (counter == 2) name += ch;
                            }
                        }

                        db.execSQL("INSERT OR IGNORE INTO validnumber VALUES (?, ?)", new Object[]{receivernum, name});
                        Toast.makeText(context, "Successfully added valid number: " + receivernum, Toast.LENGTH_LONG).show();

                    } else if ("UNLOCK".equals(stchar)) {
                        String receivernum = "";
                        for (int i = 6; i < valqtylenth; i++) {
                            char ch = syntaxsms.charAt(i);
                            if ("/".equals(String.valueOf(ch))) counter++;
                            else if (counter == 1) receivernum += ch;
                        }

                        db.execSQL("UPDATE CUSTOMERS SET LOCK = 'U' WHERE CID = ?", new Object[]{receivernum});
                        Toast.makeText(context, "Successfully UNLOCKED customer by: " + NumberOwner, Toast.LENGTH_LONG).show();

                    } else if ("CREDIT".equals(stchar)) {
                        String customercode = "", creditlimit = "", balcredit = "", ocheck = "", CreditStatus1 = "0";

                        for (int i = 6; i < valqtylenth; i++) {
                            char ch = syntaxsms.charAt(i);
                            if ("/".equals(String.valueOf(ch))) counter++;
                            else {
                                if (counter == 1) customercode += ch;
                                else if (counter == 2) creditlimit += ch;
                                else if (counter == 3) balcredit += ch;
                                else if (counter == 4) ocheck += ch;
                                else if (counter == 5) CreditStatus1 += ch;
                            }
                        }

                        db.execSQL("UPDATE customers SET creditline = ?, balcredit = ?, ocheck = ?, CreditStatus = ? WHERE cid = ?",
                                new Object[]{Float.parseFloat(creditlimit), balcredit, ocheck, CreditStatus1, customercode});

                        Toast.makeText(context, "Updated credit line for: " + customercode, Toast.LENGTH_LONG).show();

                        if (custid != null && custid.getText() != null) {
                            String cid = custid.getText().toString();
                            Cursor c1 = db.rawQuery("SELECT * FROM customers WHERE cid = ?", new String[]{cid});
                            if (c1.getCount() > 0 && c1.moveToFirst()) {
                                float ac = Float.parseFloat(c1.getString(35)) - (
                                        Float.parseFloat(c1.getString(36)) + Float.parseFloat(c1.getString(37))
                                );
                                Toast.makeText(context, "Customer new credit info: " + cid, Toast.LENGTH_LONG).show();
                            }
                            c1.close();
                        }
                    }
                    queryvalidnumber.close();
                }
            } catch (Exception e) {
                Log.e("SMSReceiver", "Crash in onReceive: " + e.getMessage());
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
        setContentView(R.layout.additeminventory);

        try {

            setupToolbar("Add Item Inventory");
            // Force menu to appear
            invalidateOptionsMenu();

            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            spproductnew = (Spinner) findViewById(R.id.spproductadditemInv);

            spoutlet= (Spinner) findViewById(R.id.spoutletadditemInv);
            spcategory= (Spinner) findViewById(R.id.spcategoryadditemInv);
            //spcustomer = (Spinner) findViewById(R.id.spcustomerlist);

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            transinv  = (TextView) findViewById(R.id.tvtransadditemInv);
            transinv.setText("Van-selling");

            SalesmanID = getIntent().getExtras().getString("salesmanidvalueadditeminv").toUpperCase();

            salesmannameid  = (TextView) findViewById(R.id.tvsalesmanidadditemInv);
            salesmannameid.setText(SalesmanID);

            btsendsave = (Button) findViewById(R.id.btsaveadditemInv);
            btsendsave.setOnClickListener(this);

            um = (TextView) findViewById(R.id.tvumadditemInv);
            csum = (TextView) findViewById(R.id.tvcsumadditemInv);

            csfactor = (TextView) findViewById(R.id.tvcsfactoradditemInv);

            countpro = 0;
            countpro = 0;

            quantity =(EditText) findViewById(R.id.etquantityadditemInv);
            csquantity =(EditText) findViewById(R.id.etcsquantityadditemInv);

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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
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

            inputSearch = (EditText) findViewById(R.id.etqueryproductadditemInv);
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

            //details.setText("");
            //	unitprice.setEnabled(true);
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

                        Object selectedItem = spproductnew.getSelectedItem();

                        if (selectedItem == null) {
                            Toast.makeText(getApplicationContext(), "No product selected.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        tempstring = selectedItem.toString();

//                        tempstring = spproductnew.getSelectedItem().toString();
                        selectQuery = "Select * from products2 where description like '"+ tempstring +"' ";

                        Cursor c1 = db.rawQuery(selectQuery, null);
                        c1.moveToFirst();
                        int cnt =  c1.getCount();
                        if(cnt > 0){
                            // Cursor c3 = db.rawQuery("Select * from itempriceoveride where itemcode like '"+ c1.getString(0) +"' and customerid = '"+ custid.getText().toString() +"'", null);
                            // c3.moveToFirst();
                            try{
                                //		unitprice.setText(c1.getString(2));
                                //	unitprice.setEnabled(false);

                                //	if("0".equals(unitprice.getText().toString())){
                                //		csfactor.setText(c1.getString(8));
                                //	}else{

                                csfactor.setText(c1.getString(8));

                                //		float cspriceunit = Float.parseFloat(csfactor.getText().toString()) * Float.parseFloat(unitprice.getText().toString());
                                //		DecimalFormat formatter = new DecimalFormat("#.00");
                                //		csunitprice.setText(String.valueOf(formatter.format(cspriceunit)));

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
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"" + e ,Toast.LENGTH_LONG).show();
        }
    }			// end oncreate

    // private void checkunitprice(){
    //}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // inser broken seal

        if(v.getId() == R.id.btsaveadditemInv){  // add item

            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemInventory.this);
            builder.setMessage("Are you sure you want to Add Inventory for This Product "+ spproductnew.getSelectedItem().toString() +" to your Vanselling Inventory?");
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
                    }else{
                        Toast.makeText(getApplicationContext(), "Product is not Exist to your Van-selling Inventory!",Toast.LENGTH_LONG).show();
                        TrappingError = false;
                    }

                    float PCInv = 0;

                    float csfactorinventory = 0 ;

                    String PricePerPC = "";

                    if("".equalsIgnoreCase(csquantity.getText().toString())){
                        if("".equalsIgnoreCase(quantity.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Please Input Inventory PC!",Toast.LENGTH_LONG).show();
                            TrappingError = false;
                        }else{

                            PCInv = Float.parseFloat(quantity.getText().toString());
                        }
                    }else{

                        //	csfactorinventory = Float.parseFloat(csfactor.getText().toString());
                        //	PCInv = Float.parseFloat(csquantity.getText().toString())  ;
                        //	PCInv = PCInv * csfactorinventory;
                        //	PCInvUnitPrice = Float.parseFloat(csunitprice.getText().toString());
                        //	PCInvUnitPrice = PCInvUnitPrice / csfactorinventory ;

                        //	Toast.makeText(getApplicationContext(), " CS Unit Price!" + csunitprice.getText().toString() ,Toast.LENGTH_LONG).show();
                        //	PCInvUnitPrice =  Float.parseFloat(csunitprice.getText().toString()) / Float.parseFloat(csfactor.getText().toString());
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

                            float CurInventory = Float.parseFloat(c.getString(3));    // inventory
                            float CurBegInventory = Float.parseFloat(c.getString(4));    // beg inventory

                            CurInventory = CurInventory + PCInv;
                            CurBegInventory = CurBegInventory + PCInv;
                            // save products
                            db.execSQL("Update products set invqty = '" + CurInventory +"', BegInv = '" + CurBegInventory +"' where ProductID = '"+ ProductID +"';");						// syntax date

                            Toast.makeText(getApplicationContext(), "Successfully add to The inventory!",Toast.LENGTH_LONG).show();
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
    //---sends an SMS message to another device---
}
