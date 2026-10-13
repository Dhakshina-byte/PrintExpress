package com.example.cse5011;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse5011.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartItemDeleteListener {
        void onDelete(CartItem item, int position);
    }

    private Context context;
    private List<CartItem> itemList;
    private OnCartItemDeleteListener deleteListener;
    private boolean isHistoryView;

    public CartAdapter(Context context, List<CartItem> itemList, boolean isHistoryView, OnCartItemDeleteListener deleteListener) {
        this.context = context;
        this.itemList = itemList;
        this.isHistoryView = isHistoryView;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = itemList.get(position);

        holder.itemTitle.setText(item.getTitle());
        holder.itemDetails.setText("Size: " + item.getSize() + " | Qty: " + item.getQuantity());
        holder.itemPrice.setText(item.getPrice());
        holder.itemImage.setImageResource(item.getImageRes());

        if (isHistoryView) {
            holder.itemDate.setVisibility(View.VISIBLE);
            holder.itemDate.setText("Purchased: " + item.getDate());
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.itemDate.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                int pos = holder.getBindingAdapterPosition();
                if (deleteListener != null && pos != RecyclerView.NO_POSITION) {
                    deleteListener.onDelete(item, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle, itemDetails, itemDate, itemPrice;
        ImageButton btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartItemImage);
            itemTitle = itemView.findViewById(R.id.cartItemTitle);
            itemDetails = itemView.findViewById(R.id.cartItemDetails);
            itemDate = itemView.findViewById(R.id.cartItemDate);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            btnDelete = itemView.findViewById(R.id.btnDeleteCartItem);
        }
    }
}
