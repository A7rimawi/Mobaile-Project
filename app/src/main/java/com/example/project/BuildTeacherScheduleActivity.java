package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTeacherScheduleActivity extends AppCompatActivity {

    Spinner spClass, spTeacher, spSubject, spDay;
    EditText edtStartTime, edtEndTime;
    Button btnAddTeacherSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build_teacher_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spClass = findViewById(R.id.spClass);
        spTeacher = findViewById(R.id.spTeacher);
        spSubject = findViewById(R.id.spSubject);
        spDay = findViewById(R.id.spDay);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        btnAddTeacherSchedule = findViewById(R.id.btnAddTeacherSchedule);


        loadDataFromServer();

        btnAddTeacherSchedule.setOnClickListener(v -> {
            String className = spClass.getSelectedItem().toString();
            String teacherName = spTeacher.getSelectedItem().toString();
            String subjectName = spSubject.getSelectedItem().toString();
            String day = spDay.getSelectedItem().toString();
            String startTime = edtStartTime.getText().toString().trim();
            String endTime = edtEndTime.getText().toString().trim();

            if (className.isEmpty() || teacherName.isEmpty() || subjectName.isEmpty() || day.isEmpty()
                    || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            addTeacherScheduleToDatabase(className, teacherName, subjectName, day, startTime, endTime);
        });
    }

    private void loadDataFromServer() {
        loadSpinnerData("https://your-server.com/get_classes.php", spClass);
        loadSpinnerData("https://your-server.com/get_teachers.php", spTeacher);
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
                        Toast.makeText(this, "Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void addTeacherScheduleToDatabase(String className, String teacherName, String subjectName,
                                              String day, String startTime, String endTime) {

        String url = "";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Schedule added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("class", className);
                params.put("teacher", teacherName);
                params.put("subject", subjectName);
                params.put("day", day);
                params.put("start_time", startTime);
                params.put("end_time", endTime);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
