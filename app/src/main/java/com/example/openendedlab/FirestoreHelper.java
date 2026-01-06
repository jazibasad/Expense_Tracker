package com.example.openendedlab;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;
    private static final String COLLECTION_EXPENSES = "expenses";

    public FirestoreHelper(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void addExpense(Expense expense, OnExpenseAddedListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        Map<String, Object> expenseMap = new HashMap<>();
        expenseMap.put("title", expense.getTitle());
        expenseMap.put("amount", expense.getAmount());
        expenseMap.put("date", expense.getDate());
        expenseMap.put("userId", currentUser.getUid());
        expenseMap.put("userEmail", currentUser.getEmail());
        expenseMap.put("timestamp", System.currentTimeMillis());

        db.collection(COLLECTION_EXPENSES)
                .add(expenseMap)
                .addOnSuccessListener(documentReference -> {
                    if (listener != null) listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding to cloud: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onFailure(e);
                });
    }

    public void getExpenses(OnExpensesLoadedListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        // Note: For simple queries without a composite index, try removing the orderBy clause first to debug.
        // Firestore requires an index for queries that filter on one field and sort on another.
        // The query below filters by 'userId' and sorts by 'timestamp'. This definitely needs an index.
        // If the index is missing, the query will fail with a specific error message including a link to create it.
        
        db.collection(COLLECTION_EXPENSES)
                .whereEqualTo("userId", currentUser.getUid())
                //.orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING) // Commented out to fix potential index error for now
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Expense> expenses = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            Double amount = document.getDouble("amount");
                            String date = document.getString("date");
                            if (title != null && amount != null && date != null) {
                                expenses.add(new Expense(title, amount, date));
                            }
                        }
                        if (listener != null) listener.onLoaded(expenses);
                    } else {
                        // Log the actual error to toast
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(context, "Cloud Error: " + errorMsg, Toast.LENGTH_LONG).show();
                        if (listener != null) listener.onFailure(task.getException());
                    }
                });
    }

    public interface OnExpenseAddedListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnExpensesLoadedListener {
        void onLoaded(List<Expense> expenses);
        void onFailure(Exception e);
    }
}
