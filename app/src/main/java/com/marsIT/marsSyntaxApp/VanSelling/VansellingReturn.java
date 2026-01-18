package com.marsIT.marsSyntaxApp.VanSelling;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.R;

public class VansellingReturn extends BaseToolbar {
    private List<String> lables;
    private String salesmanid;
    private String Databasename;
    private String Departmentcode;
    private SQLiteDatabase	db;
    private TextView assignedsalesman;
    private TextView returntype;
    private Spinner listinvoice;
    int countload = 0;

    EditText inputSearch;

    private String QueryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vansellingreturn);

        setupToolbar("VanSelling Return");
        // Force menu to appear
        invalidateOptionsMenu();

        try {
            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            // load total sales of pdc cash and credit
            // deplay details
            // load inventory details
            //  String Transaction =  (getIntent().getExtras().getString("transaction"));
            salesmanid = (getIntent().getExtras().getString("salesmanidvalue"));
            Databasename = (getIntent().getExtras().getString("Databasename"));
            Departmentcode = (getIntent().getExtras().getString("Departmentcode"));

            assignedsalesman  = (TextView) findViewById(R.id.tvassignedsalesmanvrv);
            assignedsalesman.setText(salesmanid);

            // load return invoices
            listinvoice = (Spinner) findViewById(R.id.splistreturnviewvrv);
            QueryData = "select distinct(refid) from Returndtl ORDER BY REFID";
            loadSpinnerinvoice();

            Button btview = (Button) findViewById(R.id.btviewreturndetailsvrv);
            btview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ButtonClick", "btviewreturndetailsvrv clicked");

                    if (listinvoice.getSelectedItem() == null) {
                        Log.e("ButtonClick", "No item selected in Spinner");
                        Toast.makeText(VansellingReturn.this, "Please select an invoice", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String tempstring = listinvoice.getSelectedItem().toString();
                    Log.d("ButtonClick", "Selected invoice: " + tempstring);

                    Intent intent = new Intent(VansellingReturn.this, VansellingReturnDtl.class);
                    intent.putExtra("refidview", tempstring);
                    startActivity(intent);
                }
            });

            Button btnewreturn = (Button) findViewById(R.id.btnewvanreturnvrv);
            btnewreturn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // load invoicedatails
                    Intent intent = new Intent(VansellingReturn.this, VansellingReturnNew.class);

                    //		String tempstring = listcustomernoorder.getSelectedItem().toString();    		// cust type
                    intent.putExtra("salesmanid",salesmanid);
                    intent.putExtra("Databasename",Databasename);
                    intent.putExtra("Departmentcode",Departmentcode);

                    startActivity(intent);
                }
            });

            Button btbarcode = (Button) findViewById(R.id.btscanbarcode);
            btbarcode.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // load invoicedatails
                    Intent intent = new Intent(VansellingReturn.this, VansellingReturnNew.class);

                    //		String tempstring = listcustomernoorder.getSelectedItem().toString();    		// cust type
                    intent.putExtra("salesmanid",salesmanid);
                    intent.putExtra("Databasename",Databasename);
                    intent.putExtra("Departmentcode",Departmentcode);

                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerinvoice() {

        try {
            lables = new ArrayList<String>();

            // Toast.makeText(getApplicationContext(), "load Customer",Toast.LENGTH_LONG).show();
            //SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(QueryData, null);

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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lables);

            // Drop down layout style - list view with radio button
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            listinvoice.setAdapter(dataAdapter);
        }catch (Exception e){
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.sales_inventory, menu);
//        return true;
//    }
}
