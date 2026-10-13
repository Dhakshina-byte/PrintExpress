package com.example.cse5011;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import com.example.cse5011.model.Product;
import com.example.cse5011.model.ProductView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private List<Product> fullProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.fullProductList = new ArrayList<>(productList);
    }

    public void filter(String text) {
        productList.clear();
        if (text == null || text.trim().isEmpty()) {
            productList.addAll(fullProductList);
        } else {
            String query = text.toLowerCase().trim();
            for (Product item : fullProductList) {
                if (item.getTitle().toLowerCase().contains(query) ||
                    item.getDescription().toLowerCase().contains(query)) {
                    productList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productTitle.setText(product.getTitle());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(product.getPrice());
        holder.productImage.setImageResource(product.getImageRes());

        View.OnClickListener openDetails = v -> {
            Intent intent = new Intent(context, ProductView.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("title", product.getTitle());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("imageRes", product.getImageRes());
            context.startActivity(intent);
        };

        holder.viewBtn.setOnClickListener(openDetails);
        holder.itemView.setOnClickListener(openDetails);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productDescription, productPrice;
        Button viewBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            viewBtn = itemView.findViewById(R.id.viewBtn);
        }
    }
}
