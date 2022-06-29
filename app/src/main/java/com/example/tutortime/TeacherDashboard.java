package com.example.tutortime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDashboard extends AppCompatActivity {

    private Toolbar toolbar;

    MyTeacherAdapter adapter;

    RecyclerView.LayoutManager layoutTeacherManager;
    FirebaseDatabase firebaseTeacherDatabase;
    DatabaseReference databaseTeacherReference;
    private RecyclerView myTeacherRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        firebaseTeacherDatabase = FirebaseDatabase.getInstance();
        databaseTeacherReference = firebaseTeacherDatabase.getReference("Teacher")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        myTeacherRecyclerView = (RecyclerView) findViewById(R.id.myAllTeacherRecyclerView);
        layoutTeacherManager = new LinearLayoutManager(this);
        myTeacherRecyclerView.setLayoutManager(layoutTeacherManager);

        FirebaseRecyclerOptions<TeacherModel> options = new FirebaseRecyclerOptions.Builder<TeacherModel>()
                .setQuery(databaseTeacherReference, TeacherModel.class).build();

        adapter = new MyTeacherAdapter(options);
        myTeacherRecyclerView.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}