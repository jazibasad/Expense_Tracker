package com.example.openendedlab;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDate;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DatabaseHelper(this);
        firestoreHelper = new FirestoreHelper(this);
        mAuth = FirebaseAuth.getInstance();

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(title, amount, date);
        
        // Save to SQLite (Local backup)
        dbHelper.addExpense(expense, currentUser.getEmail());
        
        // Save to Firestore (Cloud)
        firestoreHelper.addExpense(expense, new FirestoreHelper.OnExpenseAddedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddExpenseActivity.this, "Expense added to cloud", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddExpenseActivity.this, "Saved locally. Cloud sync failed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
