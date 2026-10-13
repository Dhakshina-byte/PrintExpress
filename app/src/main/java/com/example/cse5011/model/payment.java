package com.example.cse5011.model;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cse5011.DB.dbconnect;
import com.example.cse5011.R;

public class payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        ImageView btnBack = findViewById(R.id.btnBack);
        ProgressBar orderProgressBar = findViewById(R.id.orderProgressBar);
        TextView textOrderStatus = findViewById(R.id.textOrderStatus);
        TextView stepPrinting = findViewById(R.id.stepPrinting);

        TextView textPaymentTotal = findViewById(R.id.textPaymentTotal);
        RadioGroup radioGroupPayment = findViewById(R.id.radioGroupPayment);
        RadioButton radioCard = findViewById(R.id.radioCard);
        RadioButton radioUpi = findViewById(R.id.radioUpi);
        RadioButton radioCod = findViewById(R.id.radioCod);

        EditText editCardNumber = findViewById(R.id.editCardNumber);
        EditText editCardExpiry = findViewById(R.id.editCardExpiry);
        EditText editCardCvv = findViewById(R.id.editCardCvv);

        Button btnConfirmPay = findViewById(R.id.btnConfirmPay);

        // Get Total Amount from Intent
        Intent intent = getIntent();
        String totalPrice = intent.getStringExtra("totalPrice");
        if (totalPrice != null) {
            textPaymentTotal.setText("Total Amount: " + totalPrice);
        } else {
            textPaymentTotal.setText("Total Amount: $0.00");
        }

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Toggle payment fields based on radio selection
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCard) {
                editCardNumber.setHint("Card Number");
                editCardExpiry.setVisibility(View.VISIBLE);
                editCardCvv.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioUpi) {
                editCardNumber.setHint("UPI ID (e.g. name@upi)");
                editCardExpiry.setVisibility(View.GONE);
                editCardCvv.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioCod) {
                editCardNumber.setHint("Delivery Phone Number");
                editCardExpiry.setVisibility(View.GONE);
                editCardCvv.setVisibility(View.GONE);
            }
        });

        // Confirm & Pay Button
        btnConfirmPay.setOnClickListener(v -> {
            dbconnect db = new dbconnect(payment.this);
            boolean checkoutSuccess = db.checkoutCart();

            if (checkoutSuccess) {
                // Update Order Progress Bar to Step 2: Printing (50%)
                orderProgressBar.setProgress(50);
                textOrderStatus.setText("Status: Order Confirmed & Printing Started!");
                stepPrinting.setTextColor(Color.parseColor("#4CAF50"));
                stepPrinting.setTypeface(null, Typeface.BOLD);

                btnConfirmPay.setEnabled(false);
                btnConfirmPay.setText("Payment Completed ✓");

                Toast.makeText(payment.this, "Payment Successful! Order placed for printing.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(payment.this, "Payment processed. Order placed!", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
