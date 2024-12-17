package com.example.finalexam;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailEt, mPasswordEt;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Views
        mEmailEt = findViewById(R.id.etEmail);
        mPasswordEt = findViewById(R.id.etPassword);
        mRegisterBtn = findViewById(R.id.btnRegister);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set OnClickListener untuk tombol Register
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set OnClickListener untuk navigasi ke LoginActivity (Already have an account?)
        TextView tvGoToLogin = findViewById(R.id.tvGoToLogin);
        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menavigasi ke LoginActivity saat sudah punya akun
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish(); // Menutup RegisterActivity setelah pindah ke LoginActivity
            }
        });
    }

    // Method untuk mendaftar pengguna
    private void registerUser() {
        String email = mEmailEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proses registrasi dengan Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Berhasil registrasi
                        Toast.makeText(RegisterActivity.this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                        // Pindah ke halaman login setelah registrasi
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // Gagal registrasi
                        Toast.makeText(RegisterActivity.this, "Registrasi Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
