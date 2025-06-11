package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        // Make sure your root layout in activity_reset_password.xml has android:id="@+id/main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Called by the Button's android:onClick="updatePassword"
     */
    public void updatePassword(View view) {
        EditText etNew     = findViewById(R.id.etNewPassword);
        EditText etConfirm = findViewById(R.id.etConfirmPassword);

        String newPass = etNew.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();
        String username = getIntent().getStringExtra("username");
        // Make sure you launch this Activity with:
        // intent.putExtra("username", theUsername);

        if (newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in both password fields.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!newPass.equals(confirm)) {
            Toast.makeText(this,
                    "Passwords do not match.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // All validation passed – now call your PHP endpoint via Volley
        updatePasswordInDatabase(username, newPass);
    }

    private void updatePasswordInDatabase(String username, String password) {
        String url = "http://10.0.2.2/update_password.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.optBoolean("success", false);
                        Toast.makeText(
                                this,
                                success ? "✔ Password updated successfully."
                                        : "✖ Failed to update password.",
                                Toast.LENGTH_SHORT
                        ).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                this,
                                "Response parse error.",
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        queue.add(request);
    }
}