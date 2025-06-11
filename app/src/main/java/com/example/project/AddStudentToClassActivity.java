package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStudentToClassActivity extends AppCompatActivity {

    private Spinner spinnerClass;
    private ListView lvStudents;
    private Button btnSaveStudents;

    private final List<String> studentNames = new ArrayList<>();
    private final List<Integer> studentIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_to_class);

        spinnerClass = findViewById(R.id.spinner_class);
        lvStudents = findViewById(R.id.lv_students);
        btnSaveStudents = findViewById(R.id.btn_save_students);

        loadClasses();
        loadStudents();

        btnSaveStudents.setOnClickListener(v -> {
            String selectedClass = spinnerClass.getSelectedItem() != null
                    ? spinnerClass.getSelectedItem().toString() : "";

            if (selectedClass.isEmpty()) {
                Toast.makeText(this, "Please select a class", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Integer> selectedIds = getSelectedStudentIds();
            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "No students selected", Toast.LENGTH_SHORT).show();
                return;
            }

            saveStudentsToClass(selectedClass, selectedIds);
        });
    }

    private void loadClasses() {
        String url = "http://10.0.2.2:3000/get_classes.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<String> classList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            classList.add(jsonArray.getString(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerClass.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Failed to parse classes", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load classes: " + error.getMessage(), Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(request);
    }

    private void loadStudents() {
        String url = "http://10.0.2.2:3000/get_students.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        studentNames.clear();
                        studentIds.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject studentObj = jsonArray.getJSONObject(i);
                            studentNames.add(studentObj.getString("name") + " (" + studentObj.getString("email") + ")");
                            studentIds.add(studentObj.getInt("id"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_multiple_choice, studentNames);
                        lvStudents.setAdapter(adapter);
                        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Failed to parse students", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load students: " + error.getMessage(), Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(request);
    }

    private List<Integer> getSelectedStudentIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < lvStudents.getCount(); i++) {
            if (lvStudents.isItemChecked(i)) {
                selectedIds.add(studentIds.get(i));
            }
        }
        return selectedIds;
    }

    private void saveStudentsToClass(String selectedClass, List<Integer> selectedIds) {
            String url = "http://10.0.2.2:3000/add_students_to_class.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Students added to class successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Failed to add students: " + error.getMessage(), Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("class_name", selectedClass);
                params.put("student_ids", new JSONArray(selectedIds).toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
