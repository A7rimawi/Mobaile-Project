package com.example.project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {

    EditText edtFullName, edtEmail, edtPhoneNumber, edtDOP, edtClass;
    RadioGroup genderGroup;
    Button btnAddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtDOP.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, day) -> {
                String dob = day + "/" + (month + 1) + "/" + year;
                edtDOP.setText(dob);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });
        setUpViews();
    }
    private void setUpViews(){
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtDOP = findViewById(R.id.edtDOP);
        edtClass = findViewById(R.id.edtClass);
        genderGroup = findViewById(R.id.genderGroup);
        btnAddStudent = findViewById(R.id.btnAddStudent);

        btnAddStudent.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhoneNumber.getText().toString().trim();
            String dob = edtDOP.getText().toString().trim();
            String studentClass = edtClass.getText().toString().trim();

            int selectedId = genderGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRadio = findViewById(selectedId);
            String gender = selectedRadio.getText().toString();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || studentClass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call method to add student
            addStudentToDatabase(fullName, email, phone, dob, gender, studentClass);
        });

    }
    private void addStudentToDatabase(String fullName, String email, String phone,
                                      String dob, String gender, String studentClass) {
        String url ="";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("full_name", fullName);
                params.put("email", email);
                params.put("phone", phone);
                params.put("dob", dob);
                params.put("gender", gender);
                params.put("class", studentClass);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}