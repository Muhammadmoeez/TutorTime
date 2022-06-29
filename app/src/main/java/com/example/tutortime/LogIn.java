package com.example.tutortime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    FirebaseDatabase logInFirebaseDatabase;
    DatabaseReference logInDatabaseReference;


    EditText logInEmail, logInPassword;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    CheckBox checkBoxLogIn;


    String[] itemRole;
    Spinner logInRole;
    ArrayAdapter<String> adapterRole;

    // TextView registration;

    Button button, addTuitionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog = new ProgressDialog(LogIn.this);

        logInEmail = (EditText) findViewById(R.id.emailLogIn);
        logInPassword = (EditText) findViewById(R.id.passwordLogIn);
        checkBoxLogIn = (CheckBox) findViewById(R.id.logInCheckBox);

        logInFirebaseDatabase=FirebaseDatabase.getInstance();
        //    logInDatabaseReference=logInFirebaseDatabase.getReference("Drivers");

        firebaseAuth = FirebaseAuth.getInstance();

        // registration = (TextView) findViewById(R.id.reg);


        logInRole = (Spinner) findViewById(R.id.roleLogIn);

        itemRole = getResources().getStringArray(R.array.role);
        adapterRole = new ArrayAdapter<String >(LogIn.this, R.layout.spinner_layout, R.id.spinnerTextView,itemRole);
        logInRole.setAdapter(adapterRole);

        checkBoxLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    logInPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
                else {
                    logInPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });


        addTuitionBtn = (Button) findViewById(R.id.btnAddTuition);
        addTuitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(LogIn.this, TuitionForAdmin.class);
                startActivity(intent);
            }
        });



        button = (Button) findViewById(R.id.btnLogIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String email = logInEmail.getText().toString();
                String password = logInPassword.getText().toString();
                final String role = logInRole.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){
                    logInEmail.requestFocus();
                    logInEmail.setError("FIELD CANNOT BE EMPTY");
                }
                else if (TextUtils.isEmpty(password)){
                    logInPassword.requestFocus();
                    logInPassword.setError("FIELD CANNOT BE EMPTY");
                }

                else if (role.equals("Select")){
                    Toast.makeText(LogIn.this, "Please Select Member Type", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressDialog.setTitle("LogIn");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        if (role.equals("Super Admin"))
                                        {
                                            Query adminQuery = FirebaseDatabase.getInstance().getReference().child("Super Admin")
                                                    .orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());

                                            adminQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){

                                                        Toast.makeText(LogIn.this, "Welcome Super Admin", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LogIn.this, SuperAdminDashboard.class);
                                                        finish();
                                                        startActivity(intent);
                                                        progressDialog.dismiss();

                                                    }
                                                    else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LogIn.this, "You Are Not Admin", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }


                                       else if (role.equals("Admin"))
                                        {
                                            Query adminQuery = FirebaseDatabase.getInstance().getReference().child("Admin")
                                                    .orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());

                                            adminQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){

                                                        Toast.makeText(LogIn.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LogIn.this, AdminDashboard.class);
                                                        finish();
                                                        startActivity(intent);
                                                        progressDialog.dismiss();

                                                    }
                                                    else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LogIn.this, "You Are Not Admin", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        else if (role.equals("Teacher")){
                                            Query teacherQuery = FirebaseDatabase.getInstance().getReference().child("Teacher")
                                                    .orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());


                                            teacherQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){

                                                        Toast.makeText(LogIn.this, "Welcome Teacher", Toast.LENGTH_SHORT).show();


                                                        Intent intent = new Intent(LogIn.this, TeacherDashboard.class);
                                                        finish();
                                                        startActivity(intent);
                                                        progressDialog.dismiss();
                                                    }
                                                    else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LogIn.this, "You Are Not Teacher", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
//
//                                        else if (role.equals("Student")){
//
//                                        }
                                        else {

                                            progressDialog.dismiss();
                                            Toast.makeText(LogIn.this, "Check Role", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LogIn.this, "Account Not Exist", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }


            }
        });
    }

}