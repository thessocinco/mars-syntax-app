package com.marsIT.marsSyntaxApp.VanSelling;

import java.util.ArrayList;
import java.util.List;
//import android.R.integer;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
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

public class VansellingReturnDtl extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {
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
    private EditText umdtl;
    private EditText expirydtl;
    private EditText edpaymenttypedtl;
    private EditText edBankInitialdtl;
    private EditText edCheckNumberdtl;
    private EditText edAccountNumberdtl;
    private EditText edCheckDatedtl;
    private EditText edAmountdtl;
    private TextView totalinvqty;
    private TextView returntype;
    private TextView salesman;
//	private TextView totalamountinv;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vansellingreturndtl);

        setupToolbar("VanSelling Return Details");
        // Force menu to appear
        invalidateOptionsMenu();

        db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

        try {
            //Get widgets reference from XML layout
            //	GridView gView = (GridView) findViewById(R.id.gvinvoicedtl);

            btsendsave = (Button) findViewById(R.id.btresendsavevrd);
            btsendsave.setOnClickListener(this);

            quantitydtl = (EditText) findViewById(R.id.etqtyviewvrd);
            productdescriptiondtl = (EditText) findViewById(R.id.etdecriptionviewvrd);
            umdtl = (EditText) findViewById(R.id.etumvrd);
            expirydtl = (EditText) findViewById(R.id.etexpirydatevrd);

            totalinvqty = (TextView) findViewById(R.id.tvtotalqtyvrd);
            salesman= (TextView) findViewById(R.id.tvsalesmanvrd);
            // payment details

            returntype = (TextView) findViewById(R.id.tvreturntypedtl);
            tempstring = (getIntent().getExtras().getString("refidview").toUpperCase());

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

            salesman.setText(SalesmanIDval);

            String querystring; // String querystring = ""; - original

            querystring = "select description,um,expirydate,qty,refid,syntaxdatetime,returntype " +
                    "from Returndtl inner join products on Returndtl.productid = products.productid  " +
                    " where refid = '"+ tempstring +"' and products.SALESMANID = '"+ SalesmanIDval +"' order by description";

            Cursor ctemp = db.rawQuery(querystring, null);

            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            int toqty = 0;
            float tinvamount = 0;
            //   Toast.makeText(getApplicationContext(), "Trap", Toast.LENGTH_LONG).show();

            getinvoice = (TextView) findViewById(R.id.tvinvoicenumbervrd);
            getinvoice.setText(tempstring);
            syntaxdatetime = (TextView) findViewById(R.id.tvsyntaxdatetimevrd);
            syntaxdatetime.setText(ctemp.getString(5));
            lables = new ArrayList<String>();

            int countrec = 0;

            if(cnt > 0){
                String qtytemp = "";
                String desctemp = "";
                String pricetemp = "";
                String amounttemp = "";

                //	description				0
                //	um						1
                //	expirydate				2
                //	qty						3
                //	refid					4
                //	syntaxdatetime			5
                String RType = ctemp.getString(6);
                if("G".equals(RType)){
                    returntype.setText("Good Stock Return");
                }else{
                    returntype.setText("Bad Stock Return");
                }

                do {
                    //   	lables.add(ctemp.getString(1));

                    //tinvamount = tinvamount + ctemp.getFloat(7);    // item total amount
                    toqty = toqty + ctemp.getInt(3);				// total quantity

                    String sb = "";
                    qtytemp = qtytemp + ctemp.getString(3) + "\n";		// insert to text view

                    lables.add(ctemp.getString(3));			// quantity

                    //    	if("".equals(ListDtl)){
                    //    	}else{
                    //    		ListDtl = ListDtl + ",";
                    //   	}

                    //   	ListDtl = ListDtl + ctemp.getString(6);

                    Detailsinvoice = Detailsinvoice + ctemp.getString(3);		//
                    int valqtylenth = 0;

                    String latval = ctemp.getString(0);   // product description
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

                    lables.add(sb);						// productdescription details

                    //	Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_SHORT).show();

                    desctemp = desctemp + sb + "\n";

                    sb = "";
                    pricetemp = pricetemp + ctemp.getString(1) + "\n";

                    lables.add(ctemp.getString(1)); 			// um

                    amounttemp = amounttemp + ctemp.getString(2) + "\n";
                    lables.add(ctemp.getString(2));				// expirydate

                    countrec = countrec + 4;
                } while (ctemp.moveToNext());

                quantitydtl.setText(qtytemp);
                productdescriptiondtl.setText(desctemp);
                umdtl.setText(pricetemp);
                expirydtl.setText(amounttemp);
                totalinvqty.setText(String.valueOf(toqty));

                // DecimalFormat formatter = new DecimalFormat("###,###,###.##");
                //  totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));
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
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
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

        if(v.getId() == R.id.btresendsavevrd){  // save details
            // load and save the invoice details

            //	Refid VARCHAR			0
            //	Salesmanid VARCHAR		1
            //	productid varchar		2
            //	Qty integer				3
            //	expirydate varchar		4
            //	InvoiceDate varchar		5
            //	syntaxdatetime varchar	6
            //	OUTLET VARCHAR			7
            //	returntype varchar(1)	8

            Detailsinvoice = "";

            Cursor invtemp = db.rawQuery("select * from Returndtl where refid = '"+ getinvoice.getText().toString() +"'", null);
            invtemp.moveToFirst();
            int cnt =  invtemp.getCount();
            if(cnt > 0){

                String Tempsendsyntax = "";

                int countmsg = 0;
                int valqtylenth = 0;

                String SyntaxCodeDept = "";

                String refid = getinvoice.getText().toString();

                tempstring = invtemp.getString(7);
                if("L".equals(tempstring)){
                    SyntaxCodeDept = "VRET";
                }else if("A".equals(tempstring)){
                    SyntaxCodeDept = "VRET";
                }else if("AG".equals(tempstring)){
                    SyntaxCodeDept = "VRET";
                }else if("CC".equals(tempstring)){
                    if("MARS2".equals(DatabaseName)){
                        SyntaxCodeDept = "VRET2";
                    }else{
                        SyntaxCodeDept = "VRET";
                    }
                }else if("UC".equals(tempstring)){
                    SyntaxCodeDept = "VRET";
                }else if("UB".equals(tempstring)){
                    SyntaxCodeDept = "VRET";
                }

                //Toast.makeText(getApplicationContext(), "trp " + invtemp.getString(2) , Toast.LENGTH_LONG).show();

                //SyntaxCodeDept = "VRET";
                // SyntaxCodeDept!salesmanid!refid!return type!syntax datetime!productid:qty:expiry/
                Detailsinvoice = SyntaxCodeDept +  "!" + salesman.getText().toString() + "!" + refid  + "!"  + invtemp.getString(8) + "!" + invtemp.getString(6) + "!";
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
                    if(countmsg <= 0){
                        Tempsendsyntax = Detailsinvoice + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice = Detailsinvoice + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }

                    }else if(countmsg <= 1){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 2){
                        Tempsendsyntax = Detailsinvoice2 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 3){
                        Tempsendsyntax = Detailsinvoice3 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 4){
                        Tempsendsyntax = Detailsinvoice4 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 5){
                        Tempsendsyntax = Detailsinvoice5 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 6){
                        Tempsendsyntax = Detailsinvoice6 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 7){
                        Tempsendsyntax = Detailsinvoice7 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice7 = Detailsinvoice7 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 8){
                        Tempsendsyntax = Detailsinvoice8 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice8 = Detailsinvoice8 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 9){
                        Tempsendsyntax = Detailsinvoice9 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice9 = Detailsinvoice9 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 10){
                        Tempsendsyntax = Detailsinvoice10 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice10 = Detailsinvoice10 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 11){
                        Tempsendsyntax = Detailsinvoice11 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice11 = Detailsinvoice11 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 12){
                        Tempsendsyntax = Detailsinvoice12 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice12 = Detailsinvoice12 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 13){
                        Tempsendsyntax = Detailsinvoice13 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice13 = Detailsinvoice13 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 14){
                        Tempsendsyntax = Detailsinvoice14 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice14 = Detailsinvoice14 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 15){
                        Tempsendsyntax = Detailsinvoice15 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice15 = Detailsinvoice15 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 16){
                        Tempsendsyntax = Detailsinvoice16 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice16 = Detailsinvoice16 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 17){
                        Tempsendsyntax = Detailsinvoice17 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice17 = Detailsinvoice17 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 18){
                        Tempsendsyntax = Detailsinvoice18 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice18 = Detailsinvoice18 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 19){
                        Tempsendsyntax = Detailsinvoice19 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice19 = Detailsinvoice19 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 20){
                        Tempsendsyntax = Detailsinvoice20 + invtemp.getString(2) + ":" + invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }else{
                            Detailsinvoice20 = Detailsinvoice20 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4) + "/" ;
                        }
                    }else if(countmsg <= 21){
                        Tempsendsyntax = Detailsinvoice21 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }else{
                            Detailsinvoice21 = Detailsinvoice21 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }
                    }else if(countmsg <= 22){
                        Tempsendsyntax = Detailsinvoice22 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }else{
                            Detailsinvoice22 = Detailsinvoice22 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }
                    }else if(countmsg <= 23){
                        Tempsendsyntax = Detailsinvoice23 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }else{
                            Detailsinvoice23 = Detailsinvoice23 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }
                    }else if(countmsg <= 24){
                        Tempsendsyntax = Detailsinvoice24 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
                        }else{
                            Detailsinvoice24 = Detailsinvoice24 + invtemp.getString(2) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(4)  + "/" ;
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

                //   Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();

                if(countmsg <= 0){
                    sendSMS(theMobNo,Detailsinvoice);
                    Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
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
                    sendSMS(theMobNo,Detailsinvoice8);
                    sendSMS(theMobNo,Detailsinvoice9);
                }else if(countmsg <= 10){
                    sendSMS(theMobNo,Detailsinvoice);
                    sendSMS(theMobNo,Detailsinvoice1);
                    sendSMS(theMobNo,Detailsinvoice2);
                    sendSMS(theMobNo,Detailsinvoice3);
                    sendSMS(theMobNo,Detailsinvoice4);
                    sendSMS(theMobNo,Detailsinvoice5);
                    sendSMS(theMobNo,Detailsinvoice6);
                    sendSMS(theMobNo,Detailsinvoice7);
                    sendSMS(theMobNo,Detailsinvoice9);
                    sendSMS(theMobNo,Detailsinvoice8);
                    sendSMS(theMobNo,Detailsinvoice10);
                }else if(countmsg <= 11){
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
                }else if(countmsg >= 20){
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
                }else if(countmsg >= 21){
                    //   	Toast.makeText(get1pplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
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
                }else if(countmsg >= 22){
                    //   	Toast.makeText(get1pplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
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
                }else if(countmsg >= 23){
                    //   	Toast.makeText(get1pplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
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
                }else if(countmsg >= 24){
                    //   	Toast.makeText(get1pplicationContext(), "sms 7" ,Toast.LENGTH_LONG).show();
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

            @SuppressWarnings("deprecation")
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
