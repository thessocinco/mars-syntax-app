package com.marsIT.marsSyntaxApp.Inventory;

import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
import java.util.List;
//import android.R.integer;
import android.annotation.SuppressLint;
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
import android.util.Log;
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
import androidx.annotation.RequiresApi;

import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;

@SuppressWarnings("deprecation")
public class ViewLoadingPlan extends BaseToolbar implements OnClickListener, android.content.DialogInterface.OnClickListener {

    private String tempstring;
    private boolean checkvalidation;
    private String theMobNo;
    private String theMessage;
    private String validation;
    private String DatabaseName;
    private String longitude;
    private String latitude;
    private TextView getinvoice;
    private String selectQueryProduct;
    private EditText editRemarks;
    private TextView custid;
    private TextView custname;
    private TextView terms;
    private TextView daysterms;
    private TextView syntaxdatetime;
    private List<String> lables;
    private SQLiteDatabase	db;
    private TextView amount;
    private Button btsendsave;
    private EditText quantitydtl;
    private EditText productdescriptiondtl;
    private EditText unitpricedtl;
    private EditText totalamountitemdtl;
    private TextView totalinvqty;
    private TextView totalinvamount;

//	private TextView totalamountinv;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewloadingplan);
        // Show the Up button in the action bar.
        db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

        try {
            btsendsave = (Button) findViewById(R.id.btresendsavedtlvl);
            btsendsave.setOnClickListener(this);

            quantitydtl = (EditText) findViewById(R.id.etqtyviewinvdtlvl);
            productdescriptiondtl = (EditText) findViewById(R.id.etdecriptionviewinvdtlvl);
            unitpricedtl = (EditText) findViewById(R.id.etunitpriceviewinvdtlvl);
            totalamountitemdtl = (EditText) findViewById(R.id.ettotalamountitemviewinvdtlvl);

            totalinvqty = (TextView) findViewById(R.id.tvtotalinvoiceqtyviewinvdtlvl);
            totalinvamount = (TextView) findViewById(R.id.tvtotalinvoiceamountdtlviewinvdtlvl);

            String Detailsinvoice = "";

            tempstring = (getIntent().getExtras().getString("refidview").toUpperCase());

            Cursor c2 = db.rawQuery("select * from InstallValue ", null);
            c2.moveToFirst();
            int cnt2 =  c2.getCount();
            if(cnt2 > 0){
                DatabaseName = c2.getString(6);	      // database name
            }else{
                DatabaseName = "";
            }

            Cursor ctemp = db.rawQuery("select description,Loadingplan.unitprice,qty,totalamount,refid,syntaxdatetime " +
                    "from Loadingplan  inner join products on Loadingplan.productid = products.productid  " +
                    " where refid = '"+ tempstring +"' order by description", null);
            ctemp.moveToFirst();
            int cnt =  ctemp.getCount();
            int toqty = 0;
            float tinvamount = 0;
            //   Toast.makeText(getApplicationContext(), "Trap", Toast.LENGTH_LONG).show();

            getinvoice = (TextView) findViewById(R.id.tvinvoicenumberviewinvdtlvl);
            getinvoice.setText(ctemp.getString(4));

            syntaxdatetime = (TextView) findViewById(R.id.tvsyntaxdatetimevl);
            syntaxdatetime.setText(ctemp.getString(5));

            if(cnt > 0){

                String qtytemp = "";
                String desctemp = "";
                String pricetemp = "";
                String amounttemp = "";

                do {
                    // 	lables.add(ctemp.getString(1));

                    tinvamount = tinvamount + ctemp.getFloat(3);    // item total amount
                    toqty = toqty + ctemp.getInt(2);				// total quantity

                    String sb = "";
                    qtytemp = qtytemp + ctemp.getString(2) + "\n";		// insert to text view
                    Detailsinvoice = Detailsinvoice + ctemp.getString(2);		//
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

                    desctemp = desctemp + sb + "\n";

                    sb = "";
                    pricetemp = pricetemp + ctemp.getString(1) + "\n";
                    amounttemp = amounttemp + ctemp.getString(3) + "\n";

                } while (ctemp.moveToNext());

                quantitydtl.setText(qtytemp);
                productdescriptiondtl.setText(desctemp);
                unitpricedtl.setText(pricetemp);
                totalamountitemdtl.setText(amounttemp);

                totalinvqty.setText(String.valueOf(toqty));

                DecimalFormat formatter = new DecimalFormat("###,###,###.##");

                totalinvamount.setText(String.valueOf(formatter.format(tinvamount)));
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        // getclick = spctype.getSelectedItemPosition();

//        String theMessage = "";
        String Detailsinvoice;
        String Detailsinvoice1;
        String Detailsinvoice2;
        String Detailsinvoice3;
        String Detailsinvoice4;
        String Detailsinvoice5;
        String Detailsinvoice6;

//        checkvalidation = true;

        if(v.getId() == R.id.btresendsavedtlvl){ // save details

            // load and save the invoice details
//            Detailsinvoice = ""; original, Detailsinvoice = ""; redundant initializer
            if("MARS2".equals(DatabaseName)){
                Detailsinvoice1 = "V2LOAD1!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice2 = "V2LOAD2!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice3 = "V2LOAD3!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice4 = "V2LOAD4!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice5 = "V2LOAD5!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice6 = "V2LOAD6!" + getinvoice.getText().toString() + "!" ;
            }else {
                Detailsinvoice1 = "VLOAD1!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice2 = "VLOAD2!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice3 = "VLOAD3!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice4 = "VLOAD4!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice5 = "VLOAD5!" + getinvoice.getText().toString() + "!" ;
                Detailsinvoice6 = "VLOAD6!" + getinvoice.getText().toString() + "!" ;
            }

            @SuppressLint("Recycle")
            Cursor invtemp = db.rawQuery("select * from loadingplan where refid = '"+ getinvoice.getText().toString() +"'", null);
            invtemp.moveToFirst();
            int cnt =  invtemp.getCount();
            if(cnt > 0){

                String Tempsendsyntax; // original, String Tempsendsyntax = ""; redundant initializer

                int countmsg = 0;
                int valqtylenth; // original, int valqtylenth = 0; redundant initializer
                if("MARS2".equals(DatabaseName)){
                    Detailsinvoice = "V2LOAD!" + getinvoice.getText().toString() + "!" + invtemp.getString(7) + "!" + invtemp.getString(6) + "!";
                }else{
                    Detailsinvoice = "VLOAD!" + getinvoice.getText().toString() + "!" + invtemp.getString(7) + "!" + invtemp.getString(6) + "!";
                }

                do {

                    if(countmsg <= 0){
                        Tempsendsyntax = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice = Detailsinvoice + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }

                    }else if(countmsg <= 1){
                        Tempsendsyntax = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice1 = Detailsinvoice1 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }else if(countmsg <= 2){
                        Tempsendsyntax = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice2 = Detailsinvoice2 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }else if(countmsg <= 3){
                        Tempsendsyntax = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice3 = Detailsinvoice3 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }else if(countmsg <= 4){
                        Tempsendsyntax = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice4 = Detailsinvoice4 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }else if(countmsg <= 5){
                        Tempsendsyntax = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice5 = Detailsinvoice5 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }else if(countmsg <= 6){
                        Tempsendsyntax = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;;
                        valqtylenth =  Tempsendsyntax.length();
                        if(valqtylenth > 160){
                            countmsg = countmsg + 1;
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }else{
                            Detailsinvoice6 = Detailsinvoice6 + invtemp.getString(1) + ":" +  invtemp.getString(3) + ":"  + invtemp.getString(2) + "/" ;
                        }
                    }
                } while (invtemp.moveToNext());

                // query receiver number - updated with Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                @SuppressLint("Recycle")
                Cursor c1 = db.rawQuery("Select * from RECEIVERNUMBER", null);
                c1.moveToFirst();
                cnt =  c1.getCount();
                if(cnt > 0){
                    theMobNo = c1.getString(0);
                }

                if(countmsg <= 0){
                    sendSMS(theMobNo, Detailsinvoice);
                    Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                }else if(countmsg <= 1){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    Toast.makeText(getApplicationContext(), Detailsinvoice1 ,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), Detailsinvoice ,Toast.LENGTH_LONG).show();
                }else if(countmsg <= 2){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    sendSMS(theMobNo, Detailsinvoice2);
                }else if(countmsg <= 3){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    sendSMS(theMobNo, Detailsinvoice2);
                    sendSMS(theMobNo, Detailsinvoice3);
                }else if(countmsg <= 4){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    sendSMS(theMobNo, Detailsinvoice2);
                    sendSMS(theMobNo, Detailsinvoice3);
                    sendSMS(theMobNo, Detailsinvoice4);
                }else if(countmsg <= 5){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    sendSMS(theMobNo, Detailsinvoice2);
                    sendSMS(theMobNo, Detailsinvoice3);
                    sendSMS(theMobNo, Detailsinvoice4);
                    sendSMS(theMobNo, Detailsinvoice5);
                }else if(countmsg <= 6){
                    sendSMS(theMobNo, Detailsinvoice);
                    sendSMS(theMobNo, Detailsinvoice1);
                    sendSMS(theMobNo, Detailsinvoice2);
                    sendSMS(theMobNo, Detailsinvoice3);
                    sendSMS(theMobNo, Detailsinvoice4);
                    sendSMS(theMobNo, Detailsinvoice5);
                    sendSMS(theMobNo, Detailsinvoice6);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // This ID represents the Home or Up button. In the case of this
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
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            Toast.makeText(getApplicationContext(), "SMS Sent! " , Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later!" + ", ERROR :" + e , Toast.LENGTH_LONG).show();
            Log.e("SMS_ERROR", "Failed to send SMS", e); // original, e.printStackTrace();
        }
    }
}
