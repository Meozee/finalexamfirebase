package com.example.finalexam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Deklarasi Views
    private EditText mTitleEt, mDescriptionEt;
    private Button mSaveBtn, mListBtn;
    private ProgressBar progressBar;

    // Firebase Firestore Instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Data");
        }

        // Inisialisasi Views
        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mListBtn = findViewById(R.id.listBtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE); // Sembunyikan ProgressBar di awal

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Event Listener untuk Tombol Save
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // Event Listener untuk Tombol List
        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListActivity();
            }
        });
    }

    // Method untuk menyimpan data ke Firestore
    private void saveData() {
        String title = mTitleEt.getText().toString().trim();
        String description = mDescriptionEt.getText().toString().trim();

        // Validasi Input
        if (title.isEmpty()) {
            mTitleEt.setError("Title is required");
            mTitleEt.requestFocus();
            return;
        }
        if (description.isEmpty()) {
            mDescriptionEt.setError("Description is required");
            mDescriptionEt.requestFocus();
            return;
        }

        // Tampilkan ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Generate random ID untuk dokumen Firestore
        String id = UUID.randomUUID().toString();

        // Membuat objek data dalam bentuk Map
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", id);
        dataMap.put("title", title);
        dataMap.put("description", description);

        // Menyimpan data ke Firestore
        db.collection("Documents").document(id)
                .set(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE); // Sembunyikan ProgressBar

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                            clearFields(); // Kosongkan input fields
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("FirestoreError", e.getMessage());
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method untuk pindah ke ListActivity
    private void goToListActivity() {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

    // Method untuk mengosongkan input fields
    private void clearFields() {
        mTitleEt.setText("");
        mDescriptionEt.setText("");
    }
}
