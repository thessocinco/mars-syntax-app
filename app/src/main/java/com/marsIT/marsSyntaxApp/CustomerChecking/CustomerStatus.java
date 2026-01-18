//package com.marsIT.marsapp.CustomerChecking;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.util.Log;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.marsIT.marsapp.R;
//import com.marsIT.marsapp.Toolbar.BaseToolbar;
//import java.util.ArrayList;
//
//public class CustomerStatus extends BaseToolbar {
//
//    private SQLiteDatabase db;
//    private String SalesmanIDval;
//    private String WeekSchedval;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.not_yet_visited_customers);
//
//        db = openOrCreateDatabase("MapDb", MODE_PRIVATE, null);
//
//        // TODO: Get SalesmanIDval and WeekSchedval from intent or shared preferences
//        SalesmanIDval = getIntent().getStringExtra("SalesmanID");
//        WeekSchedval = getIntent().getStringExtra("WeekSched");
//
//        loadNotVisitedCustomers();
//    }
//
//    private void loadNotVisitedCustomers() {
//        ArrayList<CustomerItem> customers = new ArrayList<>();
//
//        String query = "SELECT CName, CUSTOMERID FROM ScheduleCustomer S " +
//                "LEFT JOIN CUSTOMERS C ON s.CUSTOMERID = C.cid " +
//                "WHERE status = '' AND salesmanid = '" + SalesmanIDval + "' " +
//                "AND weeksched = '" + WeekSchedval + "' ORDER BY OrderVisit";
//
//        Cursor c = db.rawQuery(query, null);
//        if (c.moveToFirst()) {
//            do {
//                String name = c.getString(0);
//                String id = c.getString(1);
//                customers.add(new CustomerItem(id, name));
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new CustomerAdapter(this, customers));
//    }
//}