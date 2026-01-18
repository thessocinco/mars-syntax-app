//package com.marsIT.marsapp.CustomerChecking;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.marsIT.marsapp.R;
//
//import java.util.List;
//
//public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
//    private final List<CustomerItem> customerList;
//    private final Context context;
//
//    public CustomerAdapter(Context context, List<CustomerItem> customerList) {
//        this.context = context;
//        this.customerList = customerList;
//    }
//
//    @NonNull
//    @Override
//    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.not_yet_visited_customers, parent, false);
//        return new CustomerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
//        CustomerItem item = customerList.get(position);
//        holder.tvCustomerName.setText(item.getCustomerName());
//        holder.layoutCustomer.setBackgroundColor(Color.parseColor("#EF9A9A")); // Always red
//    }
//
//    @Override
//    public int getItemCount() {
//        return customerList.size();
//    }
//
//    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
//        TextView tvCustomerName;
//        LinearLayout layoutCustomer;
//
//        public CustomerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
//            layoutCustomer = itemView.findViewById(R.id.layoutCustomer);
//        }
//    }
//}
//
