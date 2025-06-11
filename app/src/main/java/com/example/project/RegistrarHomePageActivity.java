package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class RegistrarHomePageActivity extends AppCompatActivity {
    MaterialCardView CrdAddStudent, CrdAddSubject, CrdBuildSchedule, CrdAddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Bind views
        CrdAddStudent = findViewById(R.id.CrdAddStudent);
        CrdAddSubject = findViewById(R.id.CrdAddSubject);
        CrdBuildSchedule = findViewById(R.id.CrdBuildSchedule);
        CrdAddUser = findViewById(R.id.CrdAddUser);

        // Set click listeners
        CrdAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentToClassActivity.class);
            startActivity(intent);
        });

        CrdAddSubject.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddSubjectActivity.class);
            startActivity(intent);
        });

        CrdBuildSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, BuildClassScheduleActivity.class);
            startActivity(intent);
        });

        CrdAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });
    }

}