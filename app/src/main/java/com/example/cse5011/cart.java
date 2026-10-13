package com.example.cse5011;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse5011.DB.dbconnect;
import com.example.cse5011.model.CartItem;

import android.content.Intent;
import com.example.cse5011.model.payment;

import java.util.List;
import java.util.Locale;

public class cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotalPrice, textEmptyState, cartTitle;
    private LinearLayout paySection;
    private Button btnTabCart, btnTabHistory, btnPayNow;
    private dbconnect db;
    private boolean showingHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        db = new dbconnect(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        cartTitle = findViewById(R.id.cartTitle);
        recyclerView = findViewById(R.id.cartRecyclerView);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        textEmptyState = findViewById(R.id.textEmptyState);
        paySection = findViewById(R.id.paySection);

        btnTabCart = findViewById(R.id.btnTabCart);
        btnTabHistory = findViewById(R.id.btnTabHistory);
        btnPayNow = findViewById(R.id.btnPayNow);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Tab Switches
        btnTabCart.setOnClickListener(v -> {
            showingHistory = false;
            cartTitle.setText("My Cart");
            btnTabCart.setAlpha(1.0f);
            btnTabHistory.setAlpha(0.6f);
            loadCartData();
        });

        btnTabHistory.setOnClickListener(v -> {
            showingHistory = true;
            cartTitle.setText("Purchase History");
            btnTabHistory.setAlpha(1.0f);
            btnTabCart.setAlpha(0.6f);
            loadHistoryData();
        });

        // Pay Now Button
        btnPayNow.setOnClickListener(v -> {
            List<CartItem> items = db.getCartItems();
            if (!items.isEmpty()) {
                Intent intent = new Intent(cart.this, payment.class);
                intent.putExtra("totalPrice", textTotalPrice.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(cart.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // Initial Load
        loadCartData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showingHistory) {
            loadHistoryData();
        } else {
            loadCartData();
        }
    }

    private void loadCartData() {
        paySection.setVisibility(View.VISIBLE);
        List<CartItem> cartItems = db.getCartItems();

        if (cartItems.isEmpty()) {
            textEmptyState.setVisibility(View.VISIBLE);
            textEmptyState.setText("Your cart is empty");
            recyclerView.setVisibility(View.GONE);
            textTotalPrice.setText("Total: $0.00");
            btnPayNow.setEnabled(false);
        } else {
            textEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnPayNow.setEnabled(true);

            double total = calculateTotal(cartItems);
            textTotalPrice.setText(String.format(Locale.US, "Total: $%.2f", total));

            CartAdapter adapter = new CartAdapter(this, cartItems, false, (item, position) -> {
                db.deleteCartItem(item.getId());
                Toast.makeText(cart.this, "Item removed", Toast.LENGTH_SHORT).show();
                loadCartData(); // Refresh list & total
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void loadHistoryData() {
        paySection.setVisibility(View.GONE);
        List<CartItem> historyItems = db.getPurchaseHistory();

        if (historyItems.isEmpty()) {
            textEmptyState.setVisibility(View.VISIBLE);
            textEmptyState.setText("No purchase history found");
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            CartAdapter adapter = new CartAdapter(this, historyItems, true, null);
            recyclerView.setAdapter(adapter);
        }
    }

    private double calculateTotal(List<CartItem> items) {
        double total = 0.0;
        for (CartItem item : items) {
            String priceStr = item.getPrice().replaceAll("[^0-9.]", "");
            try {
                double unitPrice = Double.parseDouble(priceStr);
                total += unitPrice * item.getQuantity();
            } catch (NumberFormatException ignored) {}
        }
        return total;
    }
}
