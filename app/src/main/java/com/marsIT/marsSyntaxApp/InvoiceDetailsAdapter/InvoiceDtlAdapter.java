//package com.marsIT.marsapp.InvoiceDetailsAdapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.marsIT.marsapp.R;
//import java.util.List;
//import java.util.Locale;
//
//public class InvoiceDtlAdapter extends RecyclerView.Adapter<InvoiceDtlAdapter.ViewHolder> {
//
//    private final List<InvoiceDtlProducts> invoiceDtlProducts;
//
//    public InvoiceDtlAdapter(List<InvoiceDtlProducts> invoiceDtlProducts) {
//        this.invoiceDtlProducts = invoiceDtlProducts;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView dtlQty, dtlDescription, dtlUnitPrice, dtlTotalAmount;
//
//        public ViewHolder(View view) {
//            super(view);
//            dtlQty = view.findViewById(R.id.dtlqtynew);
//            dtlDescription = view.findViewById(R.id.dtldecriptionnew);
//            dtlUnitPrice = view.findViewById(R.id.dtlunitpricenew);
//            dtlTotalAmount = view.findViewById(R.id.dtlamountnew);
//        }
//    }
//
//    @NonNull
//    @Override
//    public InvoiceDtlAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice_dtl, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull InvoiceDtlAdapter.ViewHolder holder, int position) {
//        InvoiceDtlProducts item = invoiceDtlProducts.get(position);
//        holder.dtlQty.setText(String.valueOf(item.getQuantity()));
//        holder.dtlDescription.setText(item.getDescription());
//        holder.dtlUnitPrice.setText(String.format(Locale.getDefault(), "%.2f", item.getUnitPrice()));
//        holder.dtlTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", item.getTotalAmount()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return invoiceDtlProducts.size();
//    }
//}
