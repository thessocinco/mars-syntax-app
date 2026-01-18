package com.marsIT.marsSyntaxApp.Inventory;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marsIT.marsSyntaxApp.SalesInventoryAdapter.InventoryAdapter;
import com.marsIT.marsSyntaxApp.SalesInventoryAdapter.InventoryItem;
import com.marsIT.marsSyntaxApp.Toolbar.BaseToolbar;
import com.marsIT.marsSyntaxApp.InvoiceData.InvoiceDtl;
import com.marsIT.marsSyntaxApp.R;
import com.marsIT.marsSyntaxApp.SendReason.ViewCustomerReason;

public class SalesInventory extends BaseToolbar {
    private List<String> lables;
    private String salesmanid;
    private SQLiteDatabase	db;

//    private EditText details;
//    private EditText item;
//    private EditText BegInv;
//    private EditText EndInv;
//    private EditText SalesQty;

    private TextView pdcsalestv;
    private TextView cashsalestv;
    private TextView creditsalestv;
    private TextView totalsalestv;
    private TextView assignedsalesman;
    private Spinner listinvoice;
    private Spinner listcustomernoorder;
    private Spinner listloadingplan;
    private Spinner spcustomer;
    int countload = 0;

    EditText inputSearch;

    private String QueryData;

    // excel grid view
    RecyclerView InventoryItemRecycler;
    List<InventoryItem> inventoryItem = new ArrayList<>();
//    InventoryAdapter adapter;
    // excel grid view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesinventory);

        setupToolbar("Sales Inventory");
        // force menu to appear
        invalidateOptionsMenu();

        // set up RecyclerView
        InventoryItemRecycler = findViewById(R.id.InventoryItemRecycler);
        InventoryItemRecycler.setLayoutManager(new LinearLayoutManager(this));
        InventoryAdapter adapter = new InventoryAdapter(inventoryItem);
        InventoryItemRecycler.setAdapter(adapter);

        try {
            db = openOrCreateDatabase("MapDb",MODE_PRIVATE, null);

            // load total sales of pdc cash and credit
            float PdcSales = 0;
            float CashSales = 0;
            float CreditSales =0;

            int cnt;
            Cursor ctemp = db.rawQuery("select terms, Sum(TOTALamount) as amount from invoicedtl group by terms", null);
            ctemp.moveToFirst();
            cnt =  ctemp.getCount();
            if(cnt > 0){

                // salesDetails = salesDetails + ctemp.getString(0);
                do {
                    try {
                        String term = ctemp.getString(0);
                        String amountStr = ctemp.getString(1);

                        if (term == null || amountStr == null) {
                            Log.w("SalesParse", "Null term or amount encountered, skipping...");
                            continue;
                        }

                        float amount = Float.parseFloat(amountStr); // may throw NumberFormatException

                        switch (term.toUpperCase()) {
                            case "CASH":
                                CashSales = amount;
                                Log.d("SalesParse", "Parsed CASH: " + amount);
                                break;

                            case "PDC":
                                PdcSales = amount;
                                Log.d("SalesParse", "Parsed PDC: " + amount);
                                break;

                            case "CREDIT":
                                CreditSales = amount;
                                Log.d("SalesParse", "Parsed CREDIT: " + amount);
                                break;

                            default:
                                Log.w("SalesParse", "Unknown term encountered: " + term);
                                break;
                        }

                    } catch (NumberFormatException e) {
                        Log.e("SalesParse", "Invalid amount format in cursor row: " + e.getMessage(), e);
                    } catch (Exception e) {
                        Log.e("SalesParse", "Unexpected error while parsing sales data", e);
                    }
                } while (ctemp.moveToNext());
            }

            // deplay details
            pdcsalestv = findViewById(R.id.tvpdcsalesamt);
            pdcsalestv.setText("" + PdcSales);

            cashsalestv = findViewById(R.id.tvcashsalesamt);
            cashsalestv.setText("" + CashSales);

            creditsalestv = findViewById(R.id.tvcreditsalesamt);
            creditsalestv.setText("" + CreditSales);

            totalsalestv = findViewById(R.id.tvtotalsalesamount);
            totalsalestv.setText("" + (CreditSales + PdcSales + CashSales));

            // load inventory details
            String Transaction =  (getIntent().getExtras().getString("transaction"));
            salesmanid = (getIntent().getExtras().getString("salesmanidvalue"));

            assignedsalesman = findViewById(R.id.tvassignedsalesman);
            assignedsalesman.setText(salesmanid);

            Button btadditemvaninv = findViewById(R.id.btadditem);
            Button btadditemvaninventory = findViewById(R.id.btadditemInv);

            // excel gridview function
            InventoryItemRecycler = findViewById(R.id.InventoryItemRecycler);
            InventoryItemRecycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new InventoryAdapter(inventoryItem);
            InventoryItemRecycler.setAdapter(adapter);
            // excel gridview function

            // excel gridview function for recyclerview
            if ("Booking".equals(Transaction)) {
                btadditemvaninv.setEnabled(false);
                btadditemvaninventory.setEnabled(false);
            } else {
                Cursor ctemp2 = null;
                try {
                    ctemp2 = db.rawQuery("SELECT * FROM products WHERE salesmanid LIKE ?", new String[]{salesmanid});
                    if (ctemp2 != null && ctemp2.moveToFirst()) {
                        do {
                            String productid = ctemp2.getString(0);
                            String description = ctemp2.getString(1);
                            int beginningInventory = Integer.parseInt(ctemp2.getString(3));

                            // truncate or pad description
                            if (description.length() > 46) {
                                description = description.substring(0, 46);
                            }

                            // get total sales per product
                            int salesQuantity = 0;
                            Cursor invsItem = null;
                            try {
                                invsItem = db.rawQuery(
                                        "SELECT SUM(qty) AS quantity FROM invoicedtl WHERE productid = ?", new String[]{productid}
                                );

                                if (invsItem != null && invsItem.moveToFirst()) {
                                    salesQuantity = invsItem.isNull(0) ? 0 : invsItem.getInt(0);
                                }
                            } catch (Exception e) {
                                Log.e("InventoryLoad", "Error reading sales quantity for productid: " + productid, e);
                            } finally {
                                if (invsItem != null) invsItem.close();
                            }

                            float endingInventory = beginningInventory - salesQuantity;

                            Log.d("InventoryLoad", "Loaded: " + description + " | Begin: " + beginningInventory +
                                    " | Sold: " + salesQuantity + " | End: " + endingInventory);

                            // add item to the list
                            inventoryItem.add(new InventoryItem(description, beginningInventory, salesQuantity, endingInventory));

                        } while (ctemp2.moveToNext());
                    } else {
                        Log.d("InventoryLoad", "No products found for salesmanid: " + salesmanid);
                    }
                } catch (Exception e) {
                    Log.e("InventoryLoad", "Error loading inventory items", e);
                } finally {
                    if (ctemp2 != null) ctemp2.close();
                }

                // notify adapter of data change
                adapter.notifyDataSetChanged();
                Log.d("InventoryLoad", "Inventory list updated in adapter");
            // excel gridview function for recyclerview

//                item = findViewById(R.id.etitemsview);
//                item.setText(itemsb);
//
//                BegInv = findViewById(R.id.etbegview);
//                BegInv.setText(begsb);
//
//                EndInv = findViewById(R.id.etEinvview);
//                EndInv.setText(Endsb);
//
//                SalesQty = findViewById(R.id.etsalesview);
//                SalesQty.setText(salessb);
            }

            listinvoice = findViewById(R.id.splistinvoiceview);
            QueryData = "select distinct(refid) from invoicedtl  ORDER BY REFID";
            loadSpinnerinvoice();

            listcustomernoorder  = findViewById(R.id.splistcustomernoorder);
            QueryData ="select distinct(Num) from noOrdercustomerlist ORDER BY NUM";
            loadSpinnernoorder();

            listloadingplan  = findViewById(R.id.splistloadingplan);
            loadSpinnerloadingplan();

            EditText etsam = findViewById(R.id.etsearchviewsales);
            inputSearch = findViewById(R.id.etsearchviewsales);

            spcustomer = findViewById(R.id.spcustomerviewsales);

            spcustomer.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (countload == 0) {
                        countload = 1; // skip initial call on spinner setup
                    } else {
                        try {
                            String selectedCustomer = spcustomer.getSelectedItem() != null ? spcustomer.getSelectedItem().toString() : "";

                            if (!selectedCustomer.isEmpty()) {
                                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                                Cursor c2 = db.rawQuery("SELECT cid FROM Customers WHERE cname LIKE ? ORDER BY cname", new String[] { selectedCustomer });

                                if (c2.moveToFirst()) {
                                    String customerId = c2.getString(0);
                                    QueryData = "SELECT DISTINCT(refid) FROM invoicedtl WHERE customerid LIKE '"
                                            + customerId + "' ORDER BY REFID";
                                    loadSpinnerinvoice();

                                    QueryData = "SELECT DISTINCT(Num) FROM noOrdercustomerlist WHERE customerid LIKE '"
                                            + customerId + "' ORDER BY NUM";
                                    loadSpinnernoorder();
                                }
                                c2.close();
                                db.close();
                            }
                        } catch (Exception e) {
                            Log.e("SpinnerSelection", "Error loading data for selected customer", e);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    QueryData = "SELECT  cname FROM customers where cname like '%"+ cs  +"%' ORDER BY CNAME";
                    loadSpinnerData();
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

            Button btview = findViewById(R.id.btviewinvoicedetails);
            btview.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                // load invoicedatails
                try {
                    String tempstring = listinvoice.getSelectedItem() != null ? listinvoice.getSelectedItem().toString() : "";

                    if (!tempstring.isEmpty()) {
                        Intent intent = new Intent(SalesInventory.this, InvoiceDtl.class);
                        intent.putExtra("refidview", tempstring);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SalesInventory.this, "Please select an invoice", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("SalesInventory", "Error opening InvoiceDtl", e);
                    Toast.makeText(SalesInventory.this, "Failed to open invoice details.", Toast.LENGTH_SHORT).show();
                }
            });

            Button btviewreason = findViewById(R.id.btviewcustomernoorder);
            btviewreason.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                // load invoicedatails
                try {
                    String tempstring = listcustomernoorder.getSelectedItem() != null ? listcustomernoorder.getSelectedItem().toString() : "";

                    if (!tempstring.isEmpty()) {
                        Intent intent = new Intent(SalesInventory.this, ViewCustomerReason.class);
                        intent.putExtra("numreason", tempstring);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SalesInventory.this, "Please select a customer no order", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("SalesInventory", "Error opening ViewCustomerReason", e);
                    Toast.makeText(SalesInventory.this, "Failed to open customer reason.", Toast.LENGTH_SHORT).show();
                }
            });

            Button btviewloadingplan = findViewById(R.id.btloadingplan);
            btviewloadingplan.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                // load invoicedatails
                try {
                    String tempstring = listloadingplan.getSelectedItem() != null ? listloadingplan.getSelectedItem().toString() : "";

                    if (!tempstring.isEmpty()) {
                        Intent intent = new Intent(SalesInventory.this, ViewLoadingPlan.class);
                        intent.putExtra("refidview", tempstring);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SalesInventory.this, "Please select a loading plan", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("SalesInventory", "Error starting ViewLoadingPlan", e);
                    Toast.makeText(SalesInventory.this, "Failed to open loading plan.", Toast.LENGTH_SHORT).show();
                }
            });

            btadditemvaninv.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                // load invoicedatails
                try {
                    Intent intent = new Intent(SalesInventory.this, AddItem.class);
                    String tempstring = salesmanid; // customer type
                    intent.putExtra("salesmanidvalueadditem", tempstring);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("SalesInventory", "Error launching AddItem", e);
                    Toast.makeText(getApplicationContext(), "Failed to open Add Item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            btadditemvaninventory.setOnClickListener(v -> {
                // TODO Auto-generated method stub
                // load invoicedatails
                try {
                    Intent intent = new Intent(SalesInventory.this, AddItemInventory.class);
                    String tempstring = assignedsalesman.getText().toString(); // customer type
                    intent.putExtra("salesmanidvalueadditeminv", tempstring);
                    startActivity(intent);
                    // MainActivity.this.finish();

                } catch (Exception e) {
                    Log.e("SalesInventory", "Failed to start AddItemInventory", e);
                    Toast.makeText(getApplicationContext(), "Error opening Add Item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Log.e("SalesInventory", "Error in onCreate", e);
            Toast.makeText(getApplicationContext(), "Initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerData() {
        Log.d("SpinnerCustomer", "Loading customer spinner data...");
        try {
            lables = new ArrayList<>();

            Cursor cursor = db.rawQuery(QueryData, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String customerName = cursor.getString(0);
                        lables.add(customerName);
                        Log.d("SpinnerCustomer", "Loaded customer: " + customerName);
                    } while (cursor.moveToNext());
                } else {
                    Log.w("SpinnerCustomer", "No customers found.");
                }
                cursor.close();
            } else {
                Log.e("SpinnerCustomer", "Cursor is null. Query may have failed.");
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spcustomer.setAdapter(dataAdapter);
            Log.d("SpinnerCustomer", "Spinner set with " + lables.size() + " customers.");

        } catch (Exception e) {
            Log.e("SpinnerCustomer", "Error loading customer spinner: " + e.getMessage(), e);
        }

        // handle spinner selection and load related data
        try {
            String selectedCustomer = "" + spcustomer.getSelectedItem().toString();
            if (!selectedCustomer.isEmpty()) {
                SQLiteDatabase db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
                Cursor c2 = db.rawQuery("SELECT cid FROM Customers WHERE cname LIKE ? ORDER BY cname", new String[]{selectedCustomer});

                if (c2 != null && c2.moveToFirst()) {
                    String customerId = c2.getString(0);
                    Log.d("SpinnerCustomer", "Selected customer ID: " + customerId);

                    QueryData = "SELECT DISTINCT(refid) FROM invoicedtl WHERE customerid LIKE '" + customerId + "'";
                    loadSpinnerinvoice();

                    QueryData = "SELECT DISTINCT(Num) FROM noOrdercustomerlist WHERE customerid LIKE '" + customerId + "'";
                    loadSpinnernoorder();
                } else {
                    Log.w("SpinnerCustomer", "No matching customer ID found for selected name: " + selectedCustomer);
                }

                if (c2 != null) c2.close();
            } else {
                Log.d("SpinnerCustomer", "No customer selected.");
            }

        } catch (Exception e) {
            Log.e("SpinnerCustomer", "Error loading customer-related invoice or no-order data: " + e.getMessage(), e);
        }
    }

    private void loadSpinnerinvoice() {
        try {
            Log.d("SpinnerInvoice", "Starting to load invoice spinner.");

            lables = new ArrayList<>();
            // Toast.makeText(getApplicationContext(), "load Customer",Toast.LENGTH_LONG).show();

            // execute the query
            Cursor cursor = db.rawQuery(QueryData, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String invoiceId = cursor.getString(0);
                        lables.add(invoiceId);
                        Log.d("SpinnerInvoice", "Loaded invoice ID: " + invoiceId);
                    } while (cursor.moveToNext());
                } else {
                    Log.d("SpinnerInvoice", "No invoice data found.");
                }
                cursor.close();
            } else {
                Log.e("SpinnerInvoice", "Cursor is null. Query might have failed.");
            }

            // create and set adapter
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listinvoice.setAdapter(dataAdapter);

            Log.d("SpinnerInvoice", "Spinner loaded successfully with " + lables.size() + " items.");
        } catch (Exception e) {
            Log.e("SpinnerInvoice", "Error loading spinner data: " + e.getMessage(), e);
        }
    }

    private void loadSpinnerloadingplan() {
        try {
            Log.d("SpinnerLoad", "Starting to load loading plan spinner.");

            lables = new ArrayList<>();
            // Toast.makeText(getApplicationContext(), "load Customer",Toast.LENGTH_LONG).show();

            Cursor cursor = db.rawQuery("SELECT DISTINCT(refid) FROM loadingplan", null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String refId = cursor.getString(0);
                        lables.add(refId);
                        Log.d("SpinnerLoad", "Loaded refid: " + refId);
                    } while (cursor.moveToNext());
                } else {
                    Log.d("SpinnerLoad", "No loading plan data found.");
                }
                cursor.close();
            } else {
                Log.e("SpinnerLoad", "Cursor is null. Query may have failed.");
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listloadingplan.setAdapter(dataAdapter);

            Log.d("SpinnerLoad", "Spinner loaded successfully with " + lables.size() + " items.");
        } catch (Exception e) {
            Log.e("SpinnerLoad", "Error loading spinner data: " + e.getMessage(), e);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.sales_inventory, menu);
//        return true;
//    }

    // loadSpinnernoorder
    private void loadSpinnernoorder() {
        try {
            lables = new ArrayList<>();
            Log.d("loadCustomerData", "Started loading customer data");
            // Toast.makeText(getApplicationContext(), "load Customer",Toast.LENGTH_LONG).show();

            Cursor cursor = db.rawQuery(QueryData, null);
            Log.d("loadCustomerData", "Query executed: " + QueryData);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String customer = cursor.getString(0);
                    lables.add(customer);
                    Log.d("loadCustomerData", "Loaded customer: " + customer);
                } while (cursor.moveToNext());
            } else {
                Log.d("loadCustomerData", "No data returned from query");
            }

            if (cursor != null) cursor.close();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lables);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listcustomernoorder.setAdapter(dataAdapter);

            Log.d("loadCustomerData", "Adapter set with " + lables.size() + " items");

        } catch (Exception e) {
            Log.e("loadCustomerData", "Error loading customer data", e);
        }
    }
}
