package com.marsIT.marsSyntaxApp.InvoiceAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marsIT.marsSyntaxApp.R;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private final List<InvoiceProducts> invoiceProducts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView qty, description, unitPrice, totalAmount;

        public ViewHolder(View view) {
            super(view);
            qty = view.findViewById(R.id.etqtynew);
            description = view.findViewById(R.id.etdecriptionnew);
            unitPrice = view.findViewById(R.id.etunitpricenew);
            totalAmount = view.findViewById(R.id.etamountnew);
        }
    }

    public InvoiceAdapter(List<InvoiceProducts> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }

    @NonNull
    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InvoiceProducts u = invoiceProducts.get(position);
        holder.qty.setText(String.valueOf(u.qty));
        holder.description.setText(u.description);
        holder.unitPrice.setText(String.format(Locale.getDefault(), "%.2f", u.unitPrice));
        holder.totalAmount.setText(String.format(Locale.getDefault(), "%.2f", u.totalAmount));
    }

    @Override
    public int getItemCount() {
        return invoiceProducts.size();
    }
}
