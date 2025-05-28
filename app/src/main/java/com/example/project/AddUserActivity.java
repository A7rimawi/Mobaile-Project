package com.example.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    EditText edtFullName, edtUserName, edtEmail, edtPhoneNumber, edtPassword, edtConfirmPassword;
    Spinner spRole;
    Button btnAddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpViews();
    }
    private void setUpViews(){
        edtFullName = findViewById(R.id.edtFullName);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        spRole = findViewById(R.id.spRole);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String userName = edtUserName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhoneNumber.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            String role = spRole.getSelectedItem().toString();

            if (fullName.isEmpty() || userName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            addUserToDatabase(fullName, userName, email, phone, role, password);
        });
    }
    private void addUserToDatabase(String fullName, String userName, String email,
                                   String phone, String role, String password) {

        String url = "";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("full_name", fullName);
                params.put("username", userName);
                params.put("email", email);
                params.put("phone", phone);
                params.put("role", role);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}