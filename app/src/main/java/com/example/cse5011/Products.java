package com.example.cse5011;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse5011.DB.dbconnect;
import com.example.cse5011.model.Product;

import java.util.List;

public class Products extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private dbconnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new dbconnect(this);
        // Insert sample products into SQLite if table is empty
        db.insertSampleProductsIfEmpty();

        // Fetch products list from SQLite database
        List<Product> productList = db.getAllProducts();

        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView cartIcon = findViewById(R.id.cartIcon);

        profileIcon.setOnClickListener(v -> Toast.makeText(Products.this, "Profile clicked", Toast.LENGTH_SHORT).show());
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Products.this, cart.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
