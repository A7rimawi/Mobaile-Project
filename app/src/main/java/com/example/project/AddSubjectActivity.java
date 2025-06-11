package com.example.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText edtSubjectName, edtDescription;
    private RecyclerView rvImagePicker;
    private Button btnAddSubject;

    private ImagePickerAdapter imagePickerAdapter;
    private ArrayList<String> imageNames = new ArrayList<>();
    private static final String BASE_IMAGE_URL = "http://10.0.2.2:3000/images/";
    private String selectedImageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        edtSubjectName = findViewById(R.id.edtSbjectName);
        edtDescription = findViewById(R.id.edtDescription);
        rvImagePicker = findViewById(R.id.rvImagePicker);
        btnAddSubject = findViewById(R.id.btnAddSubject);

        setupRecyclerView();
        loadImageNames(); // Load image names from server

        btnAddSubject.setOnClickListener(v -> addSubjectToDatabase());
    }

    private void setupRecyclerView() {
        rvImagePicker.setLayoutManager(new LinearLayoutManager(this));
        imagePickerAdapter = new ImagePickerAdapter(imageNames, imageName -> {
            selectedImageName = imageName; // Set the selected image name
        });
        rvImagePicker.setAdapter(imagePickerAdapter);
    }

    private void loadImageNames() {
        String url = "http://10.0.2.2:3000/get_images.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        imageNames.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            imageNames.add(jsonArray.getString(i)); // Add image name (e.g., "phys.png")
                        }
                        imagePickerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void addSubjectToDatabase() {
        String subjectName = edtSubjectName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String imageUrl = BASE_IMAGE_URL + selectedImageName;

        if (subjectName.isEmpty() || description.isEmpty() || selectedImageName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/add_subject.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Subject added successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_name", subjectName);
                params.put("description", description);
                params.put("image_url", imageUrl); // Full image URL
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}