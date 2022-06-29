package com.example.tutortime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminRegistrations extends AppCompatActivity {


    // Variable Declarations

    private static final String TAG = "AdminRegistrations";
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    Uri adminSelectImageUri;
    private StorageTask uploadTask;
    CheckBox checkBoxAdmin;
    private ProgressDialog progressDialog;
    StorageReference storageReference;
    ImageView adminProfileImage;




    EditText adminUserName,adminAddress, adminContactNumber,adminCNIC, adminEmail, adminPassword, adminConfirmPassword;
    Button adminRegistrations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registrations);


        //initializations

        progressDialog = new ProgressDialog(this);
        adminProfileImage = (ImageView) findViewById(R.id.profileImageAdmin);

        checkBoxAdmin = (CheckBox) findViewById(R.id.adminCheckBox);


        adminUserName = (EditText) findViewById(R.id.userNameAdmin);
        adminContactNumber = (EditText) findViewById(R.id.contactNumberAdmin);
        adminCNIC = (EditText) findViewById(R.id.cnicAdmin);
        adminEmail = (EditText) findViewById(R.id.emailAdmin);
        adminAddress = (EditText) findViewById(R.id.addressAdmin);

        adminPassword = (EditText) findViewById(R.id.passwordAdmin);
        adminConfirmPassword = (EditText) findViewById(R.id.confirmPasswordAdmin);
        adminRegistrations = (Button) findViewById(R.id.saveDataAdmin);


        checkBoxAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    adminPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    adminConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    adminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    adminConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        adminProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });


        adminRegistrations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adminProfileImage.getDrawable()  == null ){

                    Toast.makeText(AdminRegistrations.this, "Select Profile Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addAdminData();
                }

            }
        });


    }
    private  String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }



    private void addAdminData() {

        final String userAdminName = adminUserName.getText().toString();
        final String contactAdminNumber = adminContactNumber.getText().toString();
        final String cnicAdmin = adminCNIC.getText().toString();
        final String addressAdmin = adminAddress.getText().toString();
        final String emailAdmin = adminEmail.getText().toString();
        final String passwordAdmin = adminPassword.getText().toString();
        final String confirmAdminPassword = adminConfirmPassword.getText().toString();
        final String roleAdmin = "Admin";


        if (TextUtils.isEmpty(userAdminName)){
            adminUserName.requestFocus();
            adminUserName.setError("Please Enter Your Name");
        }
        else if (TextUtils.isEmpty(contactAdminNumber)){
            adminContactNumber.requestFocus();
            adminContactNumber.setError("Please Enter Contact Number");
        }

        else if (!contactAdminNumber.matches("\\+[0-9]{10,13}$")){
            adminContactNumber.requestFocus();
            adminContactNumber.setError("+923326882570");
        }


        else if (TextUtils.isEmpty(cnicAdmin)){
            adminCNIC.requestFocus();
            adminCNIC.setError("Please Enter CNIC");
        }
        else if (!cnicAdmin.matches("[0-9]{5}-[0-9]{7}-[0-9]{1}$")){
            adminCNIC.requestFocus();
            adminCNIC.setError("33100-XXXXXXX-X");
        }


        else if (TextUtils.isEmpty(emailAdmin)) {
            adminEmail.requestFocus();
            adminEmail.setError("Please Enter Email");
        }

        else if (TextUtils.isEmpty(addressAdmin)) {
            adminAddress.requestFocus();
            adminAddress.setError("Please Enter Email");
        }
        else if (TextUtils.isEmpty(passwordAdmin)) {
            adminPassword.requestFocus();
            adminPassword.setError("Please Enter Password");
        }
        else if (TextUtils.isEmpty(confirmAdminPassword)) {
            adminConfirmPassword.requestFocus();
            adminConfirmPassword.setError("Please Confirm Password");
        }
        else if (!passwordAdmin.equals(confirmAdminPassword)){
            adminConfirmPassword.requestFocus();
            adminConfirmPassword.setError("Password not match");
        }
        else {
            progressDialog.setTitle("Insert Data");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference("Admin");
            final StorageReference myref = storageReference.child(System.currentTimeMillis() + "." + getExtension(adminSelectImageUri));

            uploadTask = myref.putFile(adminSelectImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            myref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String profileImageURIAdmin = String.valueOf(uri);
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailAdmin, passwordAdmin)
                                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()){
                                                        final HashMap insertAdminData = new HashMap();

                                                        insertAdminData.put("ProfileImageURL", profileImageURIAdmin);
                                                        insertAdminData.put("Name", userAdminName);
                                                        insertAdminData.put("ContactNumber", contactAdminNumber);
                                                        insertAdminData.put("CNIC", cnicAdmin);
                                                        insertAdminData.put("Email",emailAdmin);

                                                        insertAdminData.put("Address",addressAdmin);
                                                        insertAdminData.put("Password", passwordAdmin);
                                                        insertAdminData.put("ConfirmPassword", confirmAdminPassword);
                                                        insertAdminData.put("Role",roleAdmin);

                                                        FirebaseDatabase.getInstance().getReference().child("Admin")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .setValue(insertAdminData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){

                                                                    Intent intent = new Intent(AdminRegistrations.this, SuperAdminDashboard.class);
                                                                    startActivity(intent);
                                                                    Toast.makeText(AdminRegistrations.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else {
                                                                    Toast.makeText(AdminRegistrations.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });

                                                    }
                                                    else {
                                                        Toast.makeText(AdminRegistrations.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }
    }


    private void SelectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CAMERA){


        }
        else if (requestCode == SELECT_FILE){
            adminSelectImageUri = data.getData();
            adminProfileImage.setImageURI(adminSelectImageUri);

        }



    }

}