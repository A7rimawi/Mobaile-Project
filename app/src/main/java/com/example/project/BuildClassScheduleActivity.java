package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registrar can create a schedule for each class using this Activity.
 * Compatible with the provided activity_create_schedule.xml.
 */
public class BuildClassScheduleActivity extends AppCompatActivity {
    private Spinner spClass, spDay, spSubject, spTeacher;
    private TimePicker timePickerStart;
    private Button btnAddPeriod, btnSaveSchedule;
    private LinearLayout layoutPeriodsList;

    // Store periods before saving
    private final List<Period> periodList = new ArrayList<>();

    // Caching loaded data for Spinners
    private List<String> classList = new ArrayList<>();
    private List<String> subjectList = new ArrayList<>();
    private List<String> teacherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build_class_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spClass = findViewById(R.id.spClass);
        spDay = findViewById(R.id.spDay);
        spSubject = findViewById(R.id.spSubject);
        spTeacher = findViewById(R.id.spTeacher);
        timePickerStart = findViewById(R.id.timepicker_start_time);
        btnAddPeriod = findViewById(R.id.btnaddperiod);
        btnSaveSchedule = findViewById(R.id.btn_save_schedule);
        layoutPeriodsList = findViewById(R.id.layout_periods_list);

        timePickerStart.setIs24HourView(true);

        loadSpinnerData();

        btnAddPeriod.setOnClickListener(v -> addPeriod());

        btnSaveSchedule.setOnClickListener(v -> saveScheduleToServer());
    }

    private void loadSpinnerData() {
        // Load classes
        loadSpinner("https://10.0.2.2:3000/get_classes.php", spClass, classList);
        // Load subjects
        loadSpinner("https://10.0.2.2:3000/get_subjects.php", spSubject, subjectList);
        // Load teachers
        loadSpinner("https://10.0.2.2:3000/get_teachers.php", spTeacher, teacherList);
        // Days (static, from strings.xml)
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(dayAdapter);
    }

    private void loadSpinner(String url, Spinner spinner, List<String> storageList) {
        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        storageList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            storageList.add(array.getString(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_dropdown_item, storageList);
                        spinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse error: " + url, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(req);
    }

    private void addPeriod() {
        String className = spClass.getSelectedItem() != null ? spClass.getSelectedItem().toString() : "";
        String day = spDay.getSelectedItem() != null ? spDay.getSelectedItem().toString() : "";
        String subject = spSubject.getSelectedItem() != null ? spSubject.getSelectedItem().toString() : "";
        String teacher = spTeacher.getSelectedItem() != null ? spTeacher.getSelectedItem().toString() : "";

        int hour = timePickerStart.getHour();
        int minute = timePickerStart.getMinute();
        String startTime = String.format("%02d:%02d:00", hour, minute);

        if (className.isEmpty() || day.isEmpty() || subject.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add to periodList
        periodList.add(new Period(className, day, subject, teacher, startTime));

        // Add a simple view to layoutPeriodsList
        Button periodView = new Button(this);
        periodView.setText(day + ": " + subject + " (" + startTime + "), " + teacher);
        periodView.setAllCaps(false);
        periodView.setEnabled(false);
        layoutPeriodsList.addView(periodView);

        Toast.makeText(this, "Period added", Toast.LENGTH_SHORT).show();
    }

    private void saveScheduleToServer() {
        if (periodList.isEmpty()) {
            Toast.makeText(this, "No periods to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Period p : periodList) {
            String url = "https://10.0.2.2:3000/add_class_period.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {},
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("class_name", p.className);
                    params.put("day", p.day);
                    params.put("subject_name", p.subject);
                    params.put("teacher_name", p.teacher);
                    params.put("start_time", p.startTime);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }

        Toast.makeText(this, "Schedule saved for class!", Toast.LENGTH_LONG).show();
        periodList.clear();
        layoutPeriodsList.removeAllViews();
    }

    // Helper class to represent a period
    private static class Period {
        String className, day, subject, teacher, startTime;
        Period(String className, String day, String subject, String teacher, String startTime) {
            this.className = className;
            this.day = day;
            this.subject = subject;
            this.teacher = teacher;
            this.startTime = startTime;
        }
    }
}