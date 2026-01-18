package com.marsIT.marsSyntaxApp.InvoiceData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
//import android.R.integer;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
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
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

public class InvoiceDtl extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;

//	private String validation;

    //	private String longitude;
//	private String latitude;
    private TextView getinvoice;

    //	private String selectQueryProduct;
    private String Deptcode;
    private String DatabaseName;
    private EditText editRemarks;
    private TextView custid;
    private TextView custname;
    private TextView terms;
    private TextView daysterms;
    private TextView syntaxdatetime;
    private List<String> lables;
    private SQLiteDatabase	db;

//	private TextView amount;

    private Button btsendsave;
    private Button btresendpayment;
    private EditText quantitydtl;
    private EditText productdescriptiondtl;
    private EditText unitpricedtl;
    private EditText totalamountitemdtl;
    private EditText edpaymenttypedtl;
    private EditText edBankInitialdtl;
    private EditText edCheckNumberdtl;
    private EditText edAccountNumberdtl;
    private EditText edCheckDatedtl;
    private EditText edAmountdtl;
    private TextView totalinvqty;
    private TextView tvdeldate;
    private TextView totalinvamount;
    private TextView totalpaymentamount;
//	private TextView totalamountinv;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoicedtl);

        try {

            setupToolbar("Invoice Details");
            // Force menu to appear
            invalidateOptionsMenu();

            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            //Get widgets reference from XML layout
            //	GridView gView = (GridView) findViewById(R.id.gvinvoicedtl);

            btsendsave = (Button) findViewById(R.id.btresendsavedtl);
            btsendsave.setOnClickListener(this);

            btresendpayment = (Button) findViewById(R.id.btresendpaymentview);
            btresendpayment.setOnClickListener(this);

            quantitydtl = (EditText) findViewById(R.id.etqtyviewinvdtl);
            productdescriptiondtl = (EditText) findViewById(R.id.etdecriptionviewinvdtl);
            unitpricedtl = (EditText) findViewById(R.id.etunitpriceviewinvdtl);
            totalamountitemdtl = (EditText) findViewById(R.id.ettotalamountitemviewinvdtl);

            totalinvqty = (TextView) findViewById(R.id.tvtotalinvoiceqtyviewinvdtl);
            totalinvamount = (TextView) findViewById(R.id.tvtotalinvoiceamountdtlviewinvdtl);

            // payment details
            edBankInitialdtl = (EditText) findViewById(R.id.etbankinitialpnewdtlr);
            edCheckNumberdtl = (EditText) findViewById(R.id.etchecknumberpnewdtlr);
            edAccountNumberdtl = (EditText) findViewById(R.id.etaccountpnewdtlr);
            edCheckDatedtl = (EditText) findViewById(R.id.etcheckdatepnewdtlr);
            edAmountdtl = (EditText) findViewById(R.id.etamountpnewdtlr);
            edpaymenttypedtl = (EditText) findViewById(R.id.etpaymenttypepnewdtlr);

            totalpaymentamount = (TextView) findViewById(R.id.tvtotalpaymentpnewr);
            tvdeldate = (TextView) findViewById(R.id.tvdeldate);

            tempstring = (getIntent().getExtras().getString("refidview").toUpperCase());

            float tpaymentamount = 0;

            Cursor ctemppay = db.rawQuery("select * from PaymentDetails where refid = '"+ tempstring +"'", null);
            ctemppay.moveToFirst();
            int cnt23 =  ctemppay.getCount();
            if(cnt23 > 0){

                String paymentTypeval = "";
                String bankinitialval = "";
                String checknumberval = "";
                String accountnumberval = "";
                String checkdateval = "";
                String amountval = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    tpaymentamount = tpaymentamount + ctemppay.getFloat(6);
                    //	toqty = toqty + ctemp.getInt(4);

                    paymentTypeval = paymentTypeval + ctemppay.getString(1) + "\n";
                    bankinitialval = bankinitialval + ctemppay.getString(2) + "\n";
                    checknumberval = checknumberval + ctemppay.getString(3)  + "\n";
                    accountnumberval = accountnumberval + ctemppay.getString(4) + "\n";
                    checkdateval = checkdateval + ctemppay.getString(5) + "\n";
                    amountval = amountval + ctemppay.getString(6) + "\n";

                } while (ctemppay.moveToNext());

                edpaymenttypedtl.setText(paymentTypeval);
                edBankInitialdtl.setText(bankinitialval);
                edCheckNumberdtl.setText(checknumberval);
                edAccountNumberdtl.setText(accountnumberval);
                edCheckDatedtl.setText(checkdateval);
                edAmountdtl.setText(amountval);

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                totalpaymentamount.setText(String.valueOf(formatter.format(tpaymentamount)));

            }else{
                edpaymenttypedtl.setText("");
                edBankInitialdtl.setText("");
                edCheckNumberdtl.setText("");
                edAccountNumberdtl.setText("");
                edCheckDatedtl.setText("");
                edAmountdtl.setText("");

                totalinvamount.setText("0");
            }

            String Detailsinvoice = "";
            String SalesmanIDval = "";

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 =  c2.getCount();
            if(cnt2 > 0){
                SalesmanIDval = c2.getString(0);	      // salesmanid
                Deptcode = c2.getString(3);	      // Department
                DatabaseName = c2.getString(6);	      // database name
            }else{
                SalesmanIDval = "";
            }

            //  Toast.makeText(getApplicationContext(), "Trap OUTLET" + Deptcode, Toast.LENGTH_LONG).show();
            String querystring; // String querystring = ""; - original

            if(c2.getString(1).equals("Booking")){
                querystring = "select description,invoicedtl.terms,daysterm,cid,cname,invoicedtl.unitprice,qty,totalamount,refid,syntaxdatetime,invoicedtl.deldate " +
                        "from (invoicedtl inner join customers  on invoicedtl.customerid = customers.cid ) inner join products on invoicedtl.productid = products.productid  " +
                        " where refid = '"+ tempstring +"'  order by description";

            }else{
                querystring = "select description,invoicedtl.terms,daysterm,cid,cname,invoicedtl.unitprice,qty,totalamount,refid,syntaxdatetime,invoicedtl.deldate " +
                        "from (invoicedtl inner join customers  on invoicedtl.customerid = customers.cid ) inner join products on invoicedtl.productid = products.productid  " +
                        " where refid = '"+ tempstring +"' and products.SALESMANID = '"+ SalesmanIDval +"' order by description";
            }

            Cursor ctemp = db.rawQuery(querystring, null);

            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            int toqty = 0;
            float tinvamount = 0;
            //   Toast.makeText(getApplicationContext(), "Trap", Toast.LENGTH_LONG).show();
            custid = (TextView) findViewById(R.id.tvcustomeridviewinvdtl);
            custname = (TextView) findViewById(R.id.tvcustomernameviewinvdtl);

            tvdeldate.setText(ctemp.getString(3));

            custid.setText(ctemp.getString(3));
            custname.setText(ctemp.getString(4));

            getinvoice = (TextView) findViewById(R.id.tvinvoicenumberviewinvdtl);
            getinvoice.setText(ctemp.getString(8));
            terms = (TextView) findViewById(R.id.tvtermsviewinvdtl);
            terms.setText(ctemp.getString(1));

            daysterms = (TextView) findViewById(R.id.tvdaystermviewinvdtl);
            daysterms.setText(ctemp.getString(2));

            syntaxdatetime = (TextView) findViewById(R.id.tvsyntaxdatetime);
            syntaxdatetime.setText(ctemp.getString(9));

            tvdeldate.setText(ctemp.getString(10));

            lables = new ArrayList<String>();

            int countrec = 0;

            if(cnt > 0){

                String qtytemp = "";
                String desctemp = "";
                String pricetemp = "";
                String amounttemp = "";

                do {
                    //   	lables.add(ctemp.getString(1));

                    tinvamount = tinvamount + ctemp.getFloat(7);    // item total amount
                    toqty = toqty + ctemp.getInt(6);				// total quantity

                    String sb = "";
                    qtytemp = qtytemp + ctemp.getString(6) + "\n";		// insert to text view

                    lables.add(ctemp.getString(6));			// quantity

                    //    	if("".equals(ListDtl)){
                    //    	}else{
                    //    		ListDtl = ListDtl + ",";
                    //   	}

                    //   	ListDtl = ListDtl + ctemp.getString(6);

                    Detailsinvoice = Detailsinvoice + ctemp.getString(6);		//
                    int valqtylenth = 0;

                    String latval = ctemp.getString(0);   // product description
                    valqtylenth = latval.length();
                    for(int i = 0; i < 40; i++)
                    {
                        if(valqtylenth < 20){
                            if(valqtylenth - 1 >= i){
                                char descr =  latval.charAt(i);
                                sb = sb + descr;
                            }else{
                                //sb = sb + " ";
                            }
                        }else if(valqtylenth > 20){
                            if(i < 20){
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

                    lables.add(sb);						// productdescription details

                    //	Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_SHORT).show();

                    desctemp = desctemp + sb + "\n";

                    sb = "";
                    pricetemp = pricetemp + ctemp.getString(5) + "\n";

                    lables.add(ctemp.getString(5)); 			// unit price

                    amounttemp = amounttemp + ctemp.getString(7) + "\n";
                    lables.add(ctemp.getString(7));				// amount

                    countrec = countrec + 4;

                } while (ctemp.moveToNext());

                quantitydtl.setText(qtytemp);
                productdescriptiondtl.setText(desctemp);
                unitpricedtl.setText(pricetemp);
                totalamountitemdtl.setText(amounttemp);

                totalinvqty.setText(String.valueOf(toqty));

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");

                totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));

            }
            // 	Toast.makeText(getApplicationContext(), "" + countrec, Toast.LENGTH_SHORT).show();

            //	    final int lablessize = countrec;
            //	    final String[] numbersArray = new String[lablessize];
            //	    for (int i=0;i<lablessize ;i++){
            //	    	numbersArray[i] = lables.get(i);
            //Toast.makeText(getApplicationContext(), "" + lables.get(i), Toast.LENGTH_SHORT).show();
            //	    }

            //Initialize an ArrayAdapter and data bind with a String Array
            //	 ArrayAdapter<String> adapter = new ArrayAdapter<String>
            //	   (this,android.R.layout.simple_list_item_1,numbersArray);

            //Data bind GridView with ArrayAdapter
            //	 gView.setAdapter(adapter);

            //Set an Item Click Listener for GridView items
            //		 gView.setOnItemClickListener(new OnItemClickListener(){
            //onItemClick() callback method
            ///	  public void onItemClick(AdapterView<?> parent, View v, int position, long id){
            //Get GridView clicked item's corresponded Array element value
            //	   String clickedItemValue = Arrays.asList(numbersArray).get(position);

            //Generate a Toast message
            //	   String toastMessage = "Position : "+position + " || Value : " + clickedItemValue;

            //Do something in response to the GridView item click
            //	   Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            //	  }
            //	 });

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //getclick = spctype.getSelectedItemPosition();

        theMessage = "";
        String Detailsinvoice = "";
        String Detailsinvoice1 = "";
        String Detailsinvoice2 = "";
        String Detailsinvoice3 = "";
        String Detailsinvoice4 = "";
        String Detailsinvoice5 = "";
        String Detailsinvoice6 = "";
        String Detailsinvoice7 = "";
        String Detailsinvoice8 = "";
        String Detailsinvoice9 = "";
        String Detailsinvoice10 = "";
        String Detailsinvoice11 = "";
        String Detailsinvoice12 = "";
        String Detailsinvoice13 = "";
        String Detailsinvoice14 = "";
        String Detailsinvoice15 = "";
        String Detailsinvoice16 = "";
        String Detailsinvoice17 = "";
        String Detailsinvoice18 = "";
        String Detailsinvoice19 = "";
        String Detailsinvoice20 = "";
        String Detailsinvoice21 = "";
        String Detailsinvoice22 = "";
        String Detailsinvoice23 = "";
        String Detailsinvoice24 = "";

        checkvalidation = true;
        if(v.getId() == R.id.btresendpaymentview){  // resend paymentsave details

            String Tempsendsyntax = "";
            String refid = getinvoice.getText().toString();
            String SyntaxCodeDept = "";

            int countmsg = 0;
            int valqtylenth = 0;

            // query receiver number
            Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
            c1.moveToFirst();
            int cnt =  c1.getCount();
            if(cnt > 0){
                theMobNo = c1.getString(0);
            }

            Cursor invtemp = db.rawQuery("select * from PaymentDetails ", null);
            invtemp.moveToFirst();
            int cnt2 =  invtemp.getCount();
            if(cnt2 > 0){

                if("MARS2".equals(DatabaseName)){
                    SyntaxCodeDept = "PAYMENT2";
                }else{
                    SyntaxCodeDept = "PAYMENT";
                }

                Detailsinvoice = SyntaxCodeDept + "!" + refid + "!";
                Detailsinvoice1 = SyntaxCodeDept + "1" + "!" + refid + "!" ;
                Detailsinvoice2 = SyntaxCodeDept + "2" +  "!" + refid + "!" ;
                Detailsinvoice3 = SyntaxCodeDept + "3" +  "!" + refid + "!" ;
                Detailsinvoice4 = SyntaxCodeDept + "4" +  "!" + refid + "!" ;
                Detailsinvoice5 = SyntaxCodeDept + "5" + "!" + refid + "!" ;
                Detailsinvoice6 = SyntaxCodeDept + "6" + "!" + refid + "!" ;
                Detailsinvoice7 = SyntaxCodeDept + "7" + "!" + refid + "!" ;
                Detailsinvoice8 = SyntaxCodeDept + "8" + "!" + refid + "!" ;
                Detailsinvoice9 = SyntaxCodeDept + "9" + "!" + refid + "!" ;
                Detailsinvoice10 = SyntaxCodeDept + "10" + "!" + refid + "!" ;
                Detailsinvoice11 = SyntaxCodeDept + "11" + "!" + refid + "!" ;
                Detailsinvoice12 = SyntaxCodeDept + "12" + "!" + refid + "!" ;
                Detailsinvoice13 = SyntaxCodeDept + "13" + "!" + refid + "!" ;
                Detailsinvoice14 = SyntaxCodeDept + "14" + "!" + refid + "!" ;
                Detailsinvoice15 = SyntaxCodeDept + "15" + "!" + refid + "!" ;
                Detailsinvoice16 = SyntaxCodeDept + "16" + "!" + refid + "!" ;
                Detailsinvoice17 = SyntaxCodeDept + "17" + "!" + refid + "!" ;
                Detailsinvoice18 = SyntaxCodeDept + "18" + "!" + refid + "!" ;
                Detailsinvoice19 = SyntaxCodeDept + "19" + "!" + refid + "!" ;
                Detailsinvoice20 = SyntaxCodeDept + "20" + "!" + refid + "!" ;
                Detailsinvoice21 = SyntaxCodeDept + "21" + "!" + refid + "!" ;
                Detailsinvoice22 = SyntaxCodeDept + "22" + "!" + refid + "!" ;
                Detailsinvoice23 = SyntaxCodeDept + "23" + "!" + refid + "!" ;
                Detailsinvoice24 = SyntaxCodeDept + "24" + "!" + refid + "!" ;

                countmsg = 0;

                do {
                    if(countmsg <= 0){
                        Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 1){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 2){
                        Tempsendsyntax = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 3){
                        Tempsendsyntax = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 4){
                        Tempsendsyntax = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 5){
                        Tempsendsyntax = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 6){
                        Tempsendsyntax = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 7){
                        Tempsendsyntax = Detailsinvoice7 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 8){
                        Tempsendsyntax = Detailsinvoice8 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 9){
                        Tempsendsyntax = Detailsinvoice9 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 10){
                        Tempsendsyntax = Detailsinvoice10 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 11){
                        Tempsendsyntax = Detailsinvoice11 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 12){
                        Tempsendsyntax = Detailsinvoice12 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 13){
                        Tempsendsyntax = Detailsinvoice13 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg <= 14){
                        Tempsendsyntax = Detailsinvoice14 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 15){
                        Tempsendsyntax = Detailsinvoice15 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 16){
                        Tempsendsyntax = Detailsinvoice16 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 17){
                        Tempsendsyntax = Detailsinvoice17 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 18){
                        Tempsendsyntax = Detailsinvoice18 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 19){
                        Tempsendsyntax = Detailsinvoice19 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 20){
                        Tempsendsyntax = Detailsinvoice20 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 21){
                        Tempsendsyntax = Detailsinvoice21 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 22){
                        Tempsendsyntax = Detailsinvoice22 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 23){
                        Tempsendsyntax = Detailsinvoice23 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }else if(countmsg >= 24){
                        Tempsendsyntax = Detailsinvoice24 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){

                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }else{
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(1) + ":" +  invtemp.getString(2) + ":"  + invtemp.getString(3) + ":"  + invtemp.getString(4) + ":"  + invtemp.getString(5) + ":"  + invtemp.getString(6)  + "/" ;
                        }
                    }
                } while (invtemp.moveToNext());

                if(countmsg <= 0){
                    sendSMS(theMobNo,Detailsinvoice);
                    //	Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                }else if(countmsg <= 1){
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    //	Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                    //	Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
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
                    //  	Toast.makeText(getApplicationContext(), "sms 5" ,Toast.LENGTH_LONG).show();
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                }else if(countmsg <= 6){
                    // 	Toast.makeText(getApplicationContext(), "sms 6" ,Toast.LENGTH_LONG).show();
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                    sendSMS(theMobNo,Detailsinvoice6);
                }else if(countmsg <= 7){
                    //	Toast.makeText(getApplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                    sendSMS(theMobNo,Detailsinvoice6);
                    sendSMS(theMobNo,Detailsinvoice7);
                }else if(countmsg <= 8){
                    //	Toast.makeText(getApplicationContext(), "sms 8" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
            }
        }

        if(v.getId() == R.id.btresendsavedtl){  // save details

            // load and save the invoice details

            Detailsinvoice = "";

            Cursor invtemp = db.rawQuery("select * from INVOICEDTL where refid = '"+ getinvoice.getText().toString() +"'", null);
            invtemp.moveToFirst();
            int cnt =  invtemp.getCount();
            if(cnt > 0){

                String Tempsendsyntax = "";

                int countmsg = 0;
                int valqtylenth = 0;

                // LINV! CUSTOMERID ! REFID ! INVOICEDATE ! TERMS ! DAYS TERMS !
                String SellingType = "";

                SellingType = invtemp.getString(12);

                if(SellingType.equals("B")){
                    SellingType = "B";
                }else{
                    SellingType = "V";
                }

                String SyntaxCodeDept = "";

                Deptcode = invtemp.getString(11);

                //tempstring = spoutlet.getSelectedItem().toString();
                if("L".equals(Deptcode)){
                    //		Outlettype = "L";
                    SyntaxCodeDept = "LINV";
                }else if("AA".equals(Deptcode)){
                    //Outlettype = "A";
                    SyntaxCodeDept = "LINV";
                }else if("AG".equals(Deptcode)){
                    //	Outlettype = "AG";
                    SyntaxCodeDept = "LINV";
                }else if("CC".equals(Deptcode)){
                    //Outlettype = "CC";
                    if("MARS2".equals(DatabaseName)){
                        SyntaxCodeDept = "C2INV";
                    }else{
                        SyntaxCodeDept = "CINV";
                    }
                }else if("EQ".equals(Deptcode)){
                    SyntaxCodeDept = "CINV";
                }else if("PL".equals(Deptcode)){		    		//Outlettype = "UC";
                    SyntaxCodeDept = "PINV";
                }else if("UC".equals(Deptcode)){
                    //Outlettype = "UC";
                    SyntaxCodeDept = "UINV";

                }else if("UB".equals(Deptcode)){
                    //Outlettype = "UC";
                    SyntaxCodeDept = "UINV";

                }else if("MI".equals(Deptcode)){
                    //Outlettype = "UC";
                    if("MARS2".equals(DatabaseName)){
                        SyntaxCodeDept = "C2INV";
                    }else{
                        SyntaxCodeDept = "CINV";
                    }
                }else if("GT".equals(Deptcode)){
                    //Outlettype = "UC";
                    SyntaxCodeDept = "C2INV";

                } else if("LY".equals(Deptcode)){
                    //Outlettype = "UC";
                    if("MARS2".equals(DatabaseName)){
                        SyntaxCodeDept = "C2INV";
                    }else{
                        SyntaxCodeDept = "CINV";
                    }
                } else if("BL".equals(Deptcode)){
                    //Outlettype = "UC";
                    if("MARS2".equals(DatabaseName)){
                        SyntaxCodeDept = "C2INV";
                    }else{
                        SyntaxCodeDept = "CINV";
                    }
                }
                //	Toast.makeText(getApplicationContext(), SyntaxCodeDept + "-" + Deptcode ,Toast.LENGTH_LONG).show();

                String refid = getinvoice.getText().toString();

                if(SellingType.equals("B")){
                    //trapTrans = trapTrans + "D!" + eddeldate.getText().toString();
                    //Detailsinvoice = SyntaxCodeDept + SellingType + "D!" + tvdeldate.getText().toString() + "!" + custid.getText().toString() + "!" + refid + "!" + sptermssp.getSelectedItem().toString() + "!" + spdaystermsp.getSelectedItem().toString() + "!" + Outlettype + "!" + osnumber.getText().toString() + "!" + datetimesyntaxval + "!";
                    Detailsinvoice = SyntaxCodeDept + SellingType + "D!" + tvdeldate.getText().toString() + "!" + invtemp.getString(1) + "!" + refid + "!" + invtemp.getString(7)+ "!" + invtemp.getString(8) + "!"  + invtemp.getString(11) + "!"  + invtemp.getString(10) + "!" + invtemp.getString(9) + "!";
                }else{
                    Detailsinvoice = SyntaxCodeDept + SellingType + "!" + invtemp.getString(1) + "!" + refid + "!" + invtemp.getString(7)+ "!" + invtemp.getString(8) + "!"  + invtemp.getString(11) + "!"  + invtemp.getString(10) + "!" + invtemp.getString(9) + "!";
                }

                Detailsinvoice1 = SyntaxCodeDept + "1" + SellingType + "!" + refid + "!" ;
                Detailsinvoice2 = SyntaxCodeDept + "2" + SellingType + "!" + refid + "!" ;
                Detailsinvoice3 = SyntaxCodeDept + "3" + SellingType + "!" + refid + "!" ;
                Detailsinvoice4 = SyntaxCodeDept + "4" + SellingType + "!" + refid + "!" ;
                Detailsinvoice5 = SyntaxCodeDept + "5" + SellingType + "!" + refid + "!" ;
                Detailsinvoice6 = SyntaxCodeDept + "6" + SellingType + "!" + refid + "!" ;
                Detailsinvoice7 = SyntaxCodeDept + "7" + SellingType + "!" + refid + "!" ;
                Detailsinvoice8 = SyntaxCodeDept + "8" + SellingType + "!" + refid + "!" ;
                Detailsinvoice9 = SyntaxCodeDept + "9" + SellingType + "!" + refid + "!" ;
                Detailsinvoice10 = SyntaxCodeDept + "10" + SellingType + "!" + refid + "!" ;
                Detailsinvoice11 = SyntaxCodeDept + "11" + SellingType + "!" + refid + "!" ;
                Detailsinvoice12 = SyntaxCodeDept + "12" + SellingType + "!" + refid + "!" ;
                Detailsinvoice13 = SyntaxCodeDept + "13" + SellingType + "!" + refid + "!" ;
                Detailsinvoice14 = SyntaxCodeDept + "14" + SellingType + "!" + refid + "!" ;
                Detailsinvoice15 = SyntaxCodeDept + "15" + SellingType + "!" + refid + "!" ;
                Detailsinvoice16 = SyntaxCodeDept + "16" + SellingType + "!" + refid + "!" ;
                Detailsinvoice17 = SyntaxCodeDept + "17" + SellingType + "!" + refid + "!" ;
                Detailsinvoice18 = SyntaxCodeDept + "18" + SellingType + "!" + refid + "!" ;
                Detailsinvoice19 = SyntaxCodeDept + "19" + SellingType + "!" + refid + "!" ;
                Detailsinvoice20 = SyntaxCodeDept + "20" + SellingType + "!" + refid + "!" ;
                Detailsinvoice21 = SyntaxCodeDept + "21" + SellingType + "!" + refid + "!" ;
                Detailsinvoice22 = SyntaxCodeDept + "22" + SellingType + "!" + refid + "!" ;
                Detailsinvoice23 = SyntaxCodeDept + "23" + SellingType + "!" + refid + "!" ;
                Detailsinvoice24 = SyntaxCodeDept + "24" + SellingType + "!" + refid + "!" ;

                countmsg = 0;
                int countmsglen = 160;

                do {
                    if(countmsg <= 0){
                        Tempsendsyntax = Detailsinvoice + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice = Detailsinvoice + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 1){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 2){
                        Tempsendsyntax = Detailsinvoice2 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 3){
                        Tempsendsyntax = Detailsinvoice3 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 4){
                        Tempsendsyntax = Detailsinvoice4 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 5){
                        Tempsendsyntax = Detailsinvoice5 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 6){
                        Tempsendsyntax = Detailsinvoice6 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 7){
                        Tempsendsyntax = Detailsinvoice7 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 8){
                        Tempsendsyntax = Detailsinvoice8 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            countmsg = countmsg + 1;
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 9){
                        Tempsendsyntax = Detailsinvoice9 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 10){
                        Tempsendsyntax = Detailsinvoice9 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 11){
                        Tempsendsyntax = Detailsinvoice11 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 12){
                        Tempsendsyntax = Detailsinvoice12 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 13){
                        Tempsendsyntax = Detailsinvoice13 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 14){
                        Tempsendsyntax = Detailsinvoice14 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 15){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 16){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 17){
                        Tempsendsyntax = Detailsinvoice17 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 18){
                        Tempsendsyntax = Detailsinvoice18 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 19){
                        Tempsendsyntax = Detailsinvoice19 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 20){
                        Tempsendsyntax = Detailsinvoice20 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 21){
                        Tempsendsyntax = Detailsinvoice21 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice22 = Detailsinvoice2 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 22){
                        Tempsendsyntax = Detailsinvoice22 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 23){
                        Tempsendsyntax = Detailsinvoice23 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }else if(countmsg <= 24){
                        Tempsendsyntax = Detailsinvoice24 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > countmsglen){
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }else{
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":0:"  + invtemp.getString(3) + "/" ;
                        }
                    }
                } while (invtemp.moveToNext());

                // query receiver number
                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }
                if(countmsg <= 0){
                    sendSMS(theMobNo,Detailsinvoice);
                    // 	Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                }else if(countmsg <= 1){
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    //	Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                    // 	Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
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
                }else if(countmsg <= 7){
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                    sendSMS(theMobNo,Detailsinvoice6);
                    sendSMS(theMobNo,Detailsinvoice7);
                }else if(countmsg <= 8){
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
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                    sendSMS(theMobNo,Detailsinvoice6);
                    sendSMS(theMobNo,Detailsinvoice7);
                    sendSMS(theMobNo,Detailsinvoice9);
                }else if(countmsg <= 10){
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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
                    //	Toast.makeText(getApplicationContext(), "sms 9" ,Toast.LENGTH_LONG).show();
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

                Toast.makeText(getApplicationContext(), "The Invoice was Successfully Resend Thank You" ,Toast.LENGTH_LONG).show();

                Detailsinvoice = "";
                // SEND PAYMENT
            }
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

            Toast.makeText(getApplicationContext(), "Sending Order, Please Wait!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later! ERROR: " + e, Toast.LENGTH_LONG).show();
            Log.e("ERROR sendSMS", "SMS failed, please try again later! ERROR: ", e);
        }
    }
}
