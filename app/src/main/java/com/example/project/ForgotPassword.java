package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    private EditText etUsername, etRecoveryCode;
    private Button btnVerify;
    private static final String URL_RECOVERY_CHECK = "http://10.0.2.2/recovery_check.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bindViews();
        btnVerify.setOnClickListener(this::onVerifyClicked);
    }

    private void bindViews() {
        etUsername     = findViewById(R.id.etUsername);
        etRecoveryCode = findViewById(R.id.etRecoveryCode);
        btnVerify      = findViewById(R.id.btnVerify);
    }

    private void onVerifyClicked(View v) {
        String user = etUsername.getText().toString().trim();
        String code = etRecoveryCode.getText().toString().trim();
        if (user.isEmpty() || code.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in both fields",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        checkRecoveryCodeInDatabase(user, code);
    }

    private void checkRecoveryCodeInDatabase(String username, String code) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_RECOVERY_CHECK,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.optBoolean("success", false);
                        if (success) {
                            // Navigate to ResetPassword activity
                            Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(
                                    this,
                                    "✖ Invalid code. Please try again.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                this,
                                "Response parse error",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                },
                error -> Toast.makeText(
                        this,
                        "Network error: " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show()
        ) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("code", code);
                return params;
            }
        };

        queue.add(request);
    }
}