package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class BuildScheduleActivity extends AppCompatActivity {
    MaterialCardView CrdStudentSchedule, CrdTeacherSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CrdStudentSchedule = findViewById(R.id.CrdStudentSchedule);
        CrdTeacherSchedule = findViewById(R.id.CrdTeacherSchedule);

        CrdStudentSchedule.setOnClickListener(v -> {
//            Intent intent = new Intent(this, BuildStudentScheduleActivity.class);
//            startActivity(intent);
        });

        CrdTeacherSchedule.setOnClickListener(v -> {
//            Intent intent = new Intent(this, BuildTeacherScheduleActivity.class);
//            startActivity(intent);
        });
    }
}