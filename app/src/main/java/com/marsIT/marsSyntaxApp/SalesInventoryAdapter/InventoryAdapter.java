package com.marsIT.marsSyntaxApp.SalesInventoryAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marsIT.marsSyntaxApp.R;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private final List<InventoryItem> inventoryItem;

    public InventoryAdapter(List<InventoryItem> inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, beginningInventory, salesQuantity, endingInventory;

        public ViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.etitemsview);
            beginningInventory = view.findViewById(R.id.etbegview);
            salesQuantity = view.findViewById(R.id.etsalesview);
            endingInventory = view.findViewById(R.id.etEinvview);
        }
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inventory_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryAdapter.ViewHolder holder, int position) {
        InventoryItem item = inventoryItem.get(position);
        holder.description.setText(item.description);
        holder.beginningInventory.setText(String.valueOf(item.beginningInventory));
        holder.salesQuantity.setText(String.valueOf(item.salesQuantity));
        holder.endingInventory.setText(String.valueOf(item.endingInventory));
    }

    @Override
    public int getItemCount() {
        return inventoryItem.size();
    }
}
