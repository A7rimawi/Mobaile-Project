package com.example.project;

import android.os.Bundle;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class AddSubjectActivity extends AppCompatActivity {
    EditText edtSubjectName, edtSubjectCode, edtTeacherName, edtClass, edtDescription;
    Button btnAddSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpViews();
    }
    private void setUpViews(){
        edtSubjectName = findViewById(R.id.edtSbjectName); // Note the typo in ID
        edtSubjectCode = findViewById(R.id.edtSubjectCode);
        edtTeacherName = findViewById(R.id.edtTeacherName);
        edtClass = findViewById(R.id.edtClass);
        edtDescription = findViewById(R.id.edtDescription);
        btnAddSubject = findViewById(R.id.btnAddSubject);

        btnAddSubject.setOnClickListener(v -> {
            String subjectName = edtSubjectName.getText().toString().trim();
            String subjectCode = edtSubjectCode.getText().toString().trim();
            String teacherName = edtTeacherName.getText().toString().trim();
            String className = edtClass.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (subjectName.isEmpty() || subjectCode.isEmpty() || teacherName.isEmpty() || className.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            addSubjectToDatabase(subjectName, subjectCode, teacherName, className, description);
        });
    }
    private void addSubjectToDatabase(String subjectName, String subjectCode,
                                      String teacherName, String className, String description) {
        String url = "";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Subject added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_name", subjectName);
                params.put("subject_code", subjectCode);
                params.put("teacher_name", teacherName);
                params.put("class", className);
                params.put("description", description);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}