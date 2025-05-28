package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildStudentScheduleActivity extends AppCompatActivity {
    Spinner spClass, spStudent, spSubject, spDay;
    EditText edtStartTime, edtEndTime;
    Button btnSaveSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build_student_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spClass = findViewById(R.id.spClass);
        spStudent = findViewById(R.id.spStudent);
        spSubject = findViewById(R.id.spSubject);
        spDay = findViewById(R.id.spDay);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        btnSaveSchedule = findViewById(R.id.btnSaveSchedule);

        loadDataFromServer();

        btnSaveSchedule.setOnClickListener(v -> {
            String selectedClass = spClass.getSelectedItem().toString();
            String selectedStudent = spStudent.getSelectedItem().toString();
            String selectedSubject = spSubject.getSelectedItem().toString();
            String selectedDay = spDay.getSelectedItem().toString();
            String startTime = edtStartTime.getText().toString().trim();
            String endTime = edtEndTime.getText().toString().trim();

            if (startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Please enter start and end time", Toast.LENGTH_SHORT).show();
                return;
            }

            saveStudentSchedule(selectedClass, selectedStudent, selectedSubject, selectedDay, startTime, endTime);
        });
    }
    private void loadDataFromServer() {
        loadSpinnerData("https://your-server.com/get_classes.php", spClass);
        loadSpinnerData("https://your-server.com/get_students.php", spStudent);
        loadSpinnerData("https://your-server.com/get_subjects.php", spSubject);
    }

    private void loadSpinnerData(String url, Spinner spinner) {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        List<String> itemList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            itemList.add(array.getString(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_dropdown_item, itemList);
                        spinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void saveStudentSchedule(String className, String studentName, String subjectName, String day,
                                     String startTime, String endTime) {

        String url = "https://your-server.com/add_student_schedule.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Schedule saved", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Failed to save: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("class_name", className);
                params.put("student_name", studentName);
                params.put("subject_name", subjectName);
                params.put("day", day);
                params.put("start_time", startTime);
                params.put("end_time", endTime);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}