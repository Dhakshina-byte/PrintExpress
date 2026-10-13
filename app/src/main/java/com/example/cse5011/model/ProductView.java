package com.example.cse5011.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cse5011.DB.dbconnect;
import com.example.cse5011.ImageSliderAdapter;
import com.example.cse5011.R;
import com.example.cse5011.cart;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductView extends AppCompatActivity {

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_view);

        // Get Views
        ImageView btnBack = findViewById(R.id.btnBack);
        ViewPager2 viewPager = findViewById(R.id.viewPagerImageSlider);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailPrice = findViewById(R.id.detailPrice);
        TextView detailDescription = findViewById(R.id.detailDescription);

        ChipGroup sizeChipGroup = findViewById(R.id.sizeChipGroup);
        Button btnDecrease = findViewById(R.id.btnDecrease);
        Button btnIncrease = findViewById(R.id.btnIncrease);
        TextView textQuantity = findViewById(R.id.textQuantity);

        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        // Get Intent Extras
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        int imageRes = intent.getIntExtra("imageRes", R.drawable.ic_launcher_foreground);

        if (title != null) detailTitle.setText(title);
        if (price != null) detailPrice.setText(price);
        if (description != null) detailDescription.setText(description);

        // Set up Slideshow Images
        List<Integer> sampleImages = new ArrayList<>();
        sampleImages.add(imageRes);
        sampleImages.add(R.drawable.ic_launcher_foreground);
        sampleImages.add(R.drawable.ic_launcher_background);

        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(sampleImages);
        viewPager.setAdapter(sliderAdapter);

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Quantity Selectors
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                textQuantity.setText(String.valueOf(quantity));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            textQuantity.setText(String.valueOf(quantity));
        });

        // Add to Cart
        btnAddToCart.setOnClickListener(v -> {
            int selectedChipId = sizeChipGroup.getCheckedChipId();
            String selectedSize = "S";
            if (selectedChipId != View.NO_ID) {
                Chip selectedChip = findViewById(selectedChipId);
                if (selectedChip != null) {
                    selectedSize = selectedChip.getText().toString();
                }
            }
            dbconnect db = new dbconnect(ProductView.this);
            db.addToCart(title != null ? title : "Product", price != null ? price : "$0.00", selectedSize, quantity, imageRes);
            Toast.makeText(ProductView.this, "Added " + quantity + "x " + title + " (" + selectedSize + ") to cart", Toast.LENGTH_SHORT).show();
        });

        // Buy Now
        btnBuyNow.setOnClickListener(v -> {
            int selectedChipId = sizeChipGroup.getCheckedChipId();
            String selectedSize = "S";
            if (selectedChipId != View.NO_ID) {
                Chip selectedChip = findViewById(selectedChipId);
                if (selectedChip != null) {
                    selectedSize = selectedChip.getText().toString();
                }
            }
            dbconnect db = new dbconnect(ProductView.this);
            db.addToCart(title != null ? title : "Product", price != null ? price : "$0.00", selectedSize, quantity, imageRes);

            Intent cartIntent = new Intent(ProductView.this, cart.class);
            startActivity(cartIntent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
