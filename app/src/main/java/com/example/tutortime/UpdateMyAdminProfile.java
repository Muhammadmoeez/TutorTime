package com.example.tutortime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateMyAdminProfile extends AppCompatActivity {


    private FirebaseAuth firebaseAuthForAdminID;
    private DatabaseReference databaseReferenceForAdmin;
    private String currentUserForMyID;



    EditText showNameAdmin,  showNumberAdmin, showCNICAdmin, showAddressAdmin;
    TextView showEmailAdmin;
    CircleImageView showProfileImgAdmin;
    Button showMyShowAllData;

    String updateAdminProfile, updateAdminName, updateAdminContactNumber,updateAdminCNIC, updateAdminAddress, updateAdminEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_admin_profile);

        firebaseAuthForAdminID = FirebaseAuth.getInstance();
        currentUserForMyID = firebaseAuthForAdminID.getCurrentUser().getUid();

        databaseReferenceForAdmin =  FirebaseDatabase.getInstance().getReference("Admin").child(currentUserForMyID);



        showProfileImgAdmin = (CircleImageView) findViewById(R.id.updateMyImageAdmin);
        showNameAdmin = (EditText) findViewById(R.id.updateMyName);
        showNumberAdmin = (EditText) findViewById(R.id.updateMyNumber);
        showCNICAdmin = (EditText) findViewById(R.id.updateMyCNIC);
        showAddressAdmin = (EditText) findViewById(R.id.updateMyAddress);
        showEmailAdmin = (TextView) findViewById(R.id.updateMyEmail);
        showMyShowAllData = (Button) findViewById(R.id.updateMyData);


        databaseReferenceForAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    updateAdminProfile = snapshot.child("ProfileImageURL").getValue().toString();
                    updateAdminName = snapshot.child("Name").getValue().toString();
                    updateAdminContactNumber = snapshot.child("ContactNumber").getValue().toString();
                    updateAdminCNIC = snapshot.child("CNIC").getValue().toString();
                    updateAdminAddress = snapshot.child("Address").getValue().toString();
                    updateAdminEmail = snapshot.child("Email").getValue().toString();




                    Picasso.get().load(updateAdminProfile).into(showProfileImgAdmin);
                    showNameAdmin.setText(updateAdminName);
                    showNumberAdmin.setText(updateAdminContactNumber);
                    showCNICAdmin.setText(updateAdminCNIC);
                    showAddressAdmin.setText(updateAdminAddress);
                    showEmailAdmin.setText(updateAdminEmail);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        showMyShowAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMyData();

            }
        });

    }

    private void updateMyData() {

        String saveUpdateMyName = showNameAdmin.getText().toString();
        String saveUpdateMyNumber = showNumberAdmin.getText().toString();
        String saveUpdateMyCNIC = showCNICAdmin.getText().toString();
        String saveUpdateMyAddress = showAddressAdmin.getText().toString();


        if (TextUtils.isEmpty(saveUpdateMyName)){

            showNameAdmin.requestFocus();
            showNameAdmin.setError("");
        }
        else if (TextUtils.isEmpty(saveUpdateMyNumber)){
            showNumberAdmin.requestFocus();
            showNumberAdmin.setError("");
        }
        else if (!saveUpdateMyNumber.matches("\\+[0-9]{10,13}$")){
            showNumberAdmin.requestFocus();
            showNumberAdmin.setError("+923326882570");
        }
        else if (TextUtils.isEmpty(saveUpdateMyCNIC)){
            showCNICAdmin.requestFocus();
            showCNICAdmin.setError("");
        }
        else if (!saveUpdateMyCNIC.matches("[0-9]{5}-[0-9]{7}-[0-9]{1}$")){
            showCNICAdmin.requestFocus();
            showCNICAdmin.setError("33100-XXXXXXX-X");
        }
        else if (TextUtils.isEmpty(saveUpdateMyAddress)){
            showAddressAdmin.requestFocus();
            showAddressAdmin.setText("");
        }
        else {

            HashMap adminUpdateData = new HashMap();
            adminUpdateData.put("Name",saveUpdateMyName);
            adminUpdateData.put("ContactNumber",saveUpdateMyNumber);
            adminUpdateData.put("CNIC",saveUpdateMyCNIC);
            adminUpdateData.put("Address",saveUpdateMyAddress);


            databaseReferenceForAdmin.updateChildren(adminUpdateData).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        final AlertDialog.Builder alert = new AlertDialog.Builder(UpdateMyAdminProfile.this);
                        alert.setTitle("Update Data");
                        alert.setMessage("Information updated successfully");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                                Intent intent = new Intent(UpdateMyAdminProfile.this, AdminDashboard.class);
                                startActivity(intent);
                            }
                        });
                        alert.show();
                    }

                }
            });

        }




    }
}