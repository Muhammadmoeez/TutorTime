package com.example.tutortime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuperAdminDashboard extends AppCompatActivity {


    private Toolbar toolbar;

    MyAdapter adapter;

    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_dashboard);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRecyclerView = (RecyclerView) findViewById(R.id.myAllAdminRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<AdminModel> options = new FirebaseRecyclerOptions.Builder<AdminModel>()
                .setQuery(databaseReference, AdminModel.class).build();

        adapter = new MyAdapter(options);
        myRecyclerView.setAdapter(adapter);


        toolbar = (Toolbar) findViewById(R.id.toolBarSA);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id =item.getItemId();

        if (id == R.id.addNewProfile){

            final CharSequence[] items={"Admin", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(SuperAdminDashboard.this);
            builder.setTitle("Registration");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (items[i].equals("Admin"))
                    {
                        Intent intent = new Intent(SuperAdminDashboard.this, AdminRegistrations.class);
                        startActivity(intent);


                    }


//                    else if (items[i].equals("Tuition"))
//                    {
//                        Intent intent = new Intent(SuperAdminDashboard.this, TuitionForTeacher.class);
//                        startActivity(intent);
//
//                    }

                    else if (items[i].equals("Cancel")){
                        dialogInterface.dismiss();
                    }

                }
            });
            builder.show();


        }

        else if (id == R.id.settingAdminProfile){

            Intent intent= new Intent(SuperAdminDashboard.this, UpdateMySuperAdminProfile.class);
            startActivity(intent);

        }




        else if (id == R.id.logoutAdminProfile){

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SuperAdminDashboard.this, LogIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}