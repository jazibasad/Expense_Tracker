package com.example.openendedlab;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private TextView tvTotalAmount, tvTransactionCount;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;
    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTransactionCount = findViewById(R.id.tvTransactionCount);
        dbHelper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        firestoreHelper = new FirestoreHelper(this);

        calculateStats();
    }

    private void calculateStats() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // We should fetch from Firestore as it is the source of truth now, or DB helper if we sync.
        // For simplicity and to match MainActivity, let's use FirestoreHelper.
        // Alternatively, since AddExpense writes to both, local DB might be okay but Firestore is better for multi-device.
        // Wait, FirestoreHelper.getExpenses is async.
        
        firestoreHelper.getExpenses(new FirestoreHelper.OnExpensesLoadedListener() {
            @Override
            public void onLoaded(List<Expense> expenses) {
                double total = 0;
                for (Expense expense : expenses) {
                    total += expense.getAmount();
                }

                tvTotalAmount.setText(String.format(Locale.getDefault(), "PKR %.2f", total));
                tvTransactionCount.setText("Total Transactions: " + expenses.size());
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(StatsActivity.this, "Failed to load stats", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
