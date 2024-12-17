package com.example.finalexam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Views
    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddBtn;

    // Adapter and Model
    private CustomAdapter adapter;
    private List<Model> modelList;

    // Firestore and Progress Dialog
    private FirebaseFirestore db;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("List Data");
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);

        // RecyclerView setup
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ProgressDialog
        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setCancelable(false); // Prevent dismissing by tapping outside
        pd.show();

        // Initialize list and adapter
        modelList = new ArrayList<>();
        adapter = new CustomAdapter(this, modelList);
        mRecyclerView.setAdapter(adapter);

        // Load data from Firestore
        fetchData();

        // Add button listener to navigate back to MainActivity
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
            }
        });
    }

    private void fetchData() {
        // Clear existing data to avoid duplication
        modelList.clear();
        adapter.notifyDataSetChanged();

        // Fetch documents from Firestore
        db.collection("Documents")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        pd.dismiss(); // Dismiss progress dialog on success

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Model model = new Model(
                                        doc.getString("id"),
                                        doc.getString("title"),
                                        doc.getString("description")
                                );
                                modelList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss(); // Ensure progress dialog is dismissed
                        Log.e("Firestore Error", e.getMessage());
                        Toast.makeText(ListActivity.this, "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
