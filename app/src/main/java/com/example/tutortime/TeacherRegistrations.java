package com.example.tutortime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherRegistrations extends AppCompatActivity {


    private ProgressDialog progressDialog;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    Uri teacherSelectImageUri;
    private StorageTask uploadTask;
    CheckBox checkBoxTeacher;
    StorageReference storageReference;
    ImageView teacherProfileImage;

    EditText teacherUserName, teacherEmail, teacherPassword, teacherConfirmPassword,  teacherContactNumber, teacherAddress,
            teacherAge, teacherCNIC,
            teacherQualification,
            teacherExperience;

    Spinner  teacherGender, teacherHowDidYouFindUs;
    String[]  itemsGender, itemsHowDidYouFindUs;
    ArrayAdapter<String>  adapterGender, adapterHowDidYouFindUs;

    Button teacherDataInsert;



    //ClassSpinnerDeclarationDeclarationWithDataBase
    Spinner teacherRegistrationClassCategoryFromDataBase;
    ArrayList<String> itemBrandClassFromDataBase;
    ArrayAdapter<String> adapterBrandClassFromDataBase;
    DatabaseReference databaseReferenceClassFromDataBase;
    ValueEventListener listenerClassFromDataBase;


    //SubCategorySpinnerDeclarationWithDataBase

    Spinner teacherRegistrationClassSubjectFromDataBase;
    ArrayList<String> itemBrandClassSubjectFromDataBase;
    ArrayAdapter<String> adapterBrandClassSubjectFromDataBase;
    DatabaseReference databaseReferenceClassSubjectFromDataBase;
    ValueEventListener listenerClassSubjectFromDataBase;

    private FirebaseAuth firebaseAuthForAdminID;
    private DatabaseReference databaseReferenceForAdminID;
    private String currentUserForAdminID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registrations);

        progressDialog = new ProgressDialog(this);

        firebaseAuthForAdminID = FirebaseAuth.getInstance();
        currentUserForAdminID = firebaseAuthForAdminID.getCurrentUser().getUid();
        Toast.makeText(this, ""+currentUserForAdminID, Toast.LENGTH_LONG).show();

      //  databaseReferenceForAdminID = FirebaseDatabase.getInstance().getReference("Admin").child(currentUserForAdminID);



        databaseReferenceClassFromDataBase = FirebaseDatabase.getInstance().getReference("You Can Teach Class");
        databaseReferenceClassSubjectFromDataBase = FirebaseDatabase.getInstance().getReference("You Can Teach Subject");

        //SpinnerInitializationCategory

        teacherRegistrationClassCategoryFromDataBase = (Spinner) findViewById(R.id.classesYouCanTeachTeacher);
        itemBrandClassFromDataBase = new ArrayList<>();
        adapterBrandClassFromDataBase = new ArrayAdapter<String >(TeacherRegistrations.this,
                R.layout.spinner_layout , R.id.spinnerTextView,itemBrandClassFromDataBase);
        teacherRegistrationClassCategoryFromDataBase.setAdapter(adapterBrandClassFromDataBase);
        retrieveDataClassFromDataBase();


        //SpinnerInitializationSubCategory

        teacherRegistrationClassCategoryFromDataBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                teacherRegistrationClassSubjectFromDataBase = (Spinner) findViewById(R.id.subjectsYouCanTeachTeacher);
                itemBrandClassSubjectFromDataBase = new ArrayList<>();
                adapterBrandClassSubjectFromDataBase = new ArrayAdapter<String >(TeacherRegistrations.this,
                        R.layout.spinner_layout , R.id.spinnerTextView,itemBrandClassSubjectFromDataBase);
                teacherRegistrationClassSubjectFromDataBase.setAdapter(adapterBrandClassSubjectFromDataBase);
                retrieveDataClassSubjectFromDataBase();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        // profile Image
        teacherProfileImage = (ImageView) findViewById(R.id.profileImageTeacher);

        //EditText

        teacherUserName = (EditText) findViewById(R.id.userNameTeacher);
        teacherEmail = (EditText) findViewById(R.id.emailTeacher);
        teacherPassword = (EditText) findViewById(R.id.passTeacher);
        teacherConfirmPassword = (EditText) findViewById(R.id.cPassTeacher);
        checkBoxTeacher = (CheckBox) findViewById(R.id.teacherCheckBox);

        checkBoxTeacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    teacherPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    teacherConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    teacherPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    teacherConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        teacherContactNumber = (EditText) findViewById(R.id.contactNumberTeacher);
        teacherAddress = (EditText) findViewById(R.id.addressTeacher);
        teacherAge = (EditText) findViewById(R.id.ageTeacher);
        teacherCNIC = (EditText) findViewById(R.id.cnicTeacher);
        teacherQualification = (EditText) findViewById(R.id.qualificationTeacher);
        teacherExperience = (EditText) findViewById(R.id.experienceTeacher);


        teacherGender = (Spinner) findViewById(R.id.genderTeacher);
        itemsGender = getResources().getStringArray(R.array.gender);
        adapterGender = new ArrayAdapter<String>(TeacherRegistrations.this, R.layout.spinner_layout, R.id.spinnerTextView, itemsGender);
        teacherGender.setAdapter(adapterGender);



        teacherHowDidYouFindUs = (Spinner) findViewById(R.id.howDidYouFindUsTeacher);
        itemsHowDidYouFindUs = getResources().getStringArray(R.array.howDidYouFindUs);
        adapterHowDidYouFindUs = new ArrayAdapter<String>(TeacherRegistrations.this, R.layout.spinner_layout, R.id.spinnerTextView, itemsHowDidYouFindUs);
        teacherHowDidYouFindUs.setAdapter(adapterHowDidYouFindUs);


        teacherDataInsert = (Button) findViewById(R.id.saveDataTeacher);


        teacherProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

        teacherDataInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teacherProfileImage.getDrawable() == null) {
                    Toast.makeText(TeacherRegistrations.this, "Please select your Profile Image", Toast.LENGTH_SHORT).show();
                }
                else {

                    insertData();
                }
            }
        });
    }


    // Create Method

    //Class Spinner
    public void retrieveDataClassFromDataBase(){
        listenerClassFromDataBase = databaseReferenceClassFromDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item:dataSnapshot.getChildren()){
                    itemBrandClassFromDataBase.add(item.getValue().toString());

                }
                adapterBrandClassFromDataBase.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Class Subject Spinner
    public void retrieveDataClassSubjectFromDataBase(){
        final String category = teacherRegistrationClassCategoryFromDataBase.getSelectedItem().toString();


        listenerClassSubjectFromDataBase = databaseReferenceClassSubjectFromDataBase
                .child(category).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot item:dataSnapshot.getChildren()){
                            itemBrandClassSubjectFromDataBase.add(item.getValue().toString());

                        }
                        adapterBrandClassSubjectFromDataBase.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    private void SelectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            teacherProfileImage.setImageURI(teacherSelectImageUri);

            if (requestCode == REQUEST_CAMERA){


            }
            else if (requestCode == SELECT_FILE){
                teacherSelectImageUri = data.getData();
                teacherProfileImage.setImageURI(teacherSelectImageUri);

            }
        }
    }

    private  String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void insertData() {

        final String userTeacherName = teacherUserName.getText().toString();
        final String emailTeacher = teacherEmail.getText().toString();
        final String passwordTeacher = teacherPassword.getText().toString();
        final String confirmPasswordTeacher = teacherConfirmPassword.getText().toString();


        final String secondContactNumber = "033326882570";
        final String contactNumberTeacher = teacherContactNumber.getText().toString();
        final String addressTeacher = teacherAddress.getText().toString();
        final String ageTeacher = teacherAge.getText().toString();
        final String cNICTeacher = teacherCNIC.getText().toString();
        final String qualificationTeacher = teacherQualification.getText().toString();
        final String experienceTeacher = teacherExperience.getText().toString();
;

        final String classesYouCanTeachTeacher = teacherRegistrationClassCategoryFromDataBase.getSelectedItem().toString();
        final String subjectsYouCanTeacher = teacherRegistrationClassSubjectFromDataBase.getSelectedItem().toString();
        final String genderTeacher = teacherGender.getSelectedItem().toString();
        final String howDidYouFindUsTeacher = teacherHowDidYouFindUs.getSelectedItem().toString();
        final String roleTeacher = "Teacher";
        final String descriptionTeacher = "Description";


        if (TextUtils.isEmpty(userTeacherName)){

            teacherUserName.requestFocus();
            teacherUserName.setError("Please Enter Your Name");
        }
        else if (TextUtils.isEmpty(emailTeacher)){
            teacherEmail.requestFocus();
            teacherEmail.setError("Please Enter Your Email");
        }
        else if (TextUtils.isEmpty(passwordTeacher)){
            teacherPassword.requestFocus();
            teacherPassword.setError("Please Enter Set Your Password");
        }
        else if (TextUtils.isEmpty(confirmPasswordTeacher)){
            teacherConfirmPassword.requestFocus();
            teacherConfirmPassword.setError("Please Enter Your Password");
        }
        else if (!passwordTeacher.matches(confirmPasswordTeacher)) {

            teacherConfirmPassword.requestFocus();
            teacherConfirmPassword.setError("You Password is Not Matched");

        }

        else if (TextUtils.isEmpty(contactNumberTeacher)){
            teacherContactNumber.requestFocus();
            teacherContactNumber.setError("Please Enter Your Contact Number");
        }
        else if (!contactNumberTeacher.matches("\\+[0-9]{10,13}$")) {

            teacherContactNumber.requestFocus();
            teacherContactNumber.setError("+923008656610");

        } else if (TextUtils.isEmpty(addressTeacher)) {
            teacherAddress.requestFocus();
            teacherAddress.setError("Please Enter Your Address");
        } else if (TextUtils.isEmpty(ageTeacher)) {
            teacherAge.requestFocus();
            teacherAge.setError("Please Enter Your Age");
        } else if (TextUtils.isEmpty(cNICTeacher)) {
            teacherCNIC.requestFocus();
            teacherCNIC.setError("Please Enter Your CNIC Number");
        }
        else if (!cNICTeacher.matches("[0-9]{5}-[0-9]{7}-[0-9]{1}$")) {

            teacherCNIC.requestFocus();
            teacherCNIC.setError("33100-XXXXXXX-X");

        }

        else if (TextUtils.isEmpty(qualificationTeacher)) {
            teacherQualification.requestFocus();
            teacherQualification.setError("Please Enter Your Qualifications");
        } else if (TextUtils.isEmpty(experienceTeacher)) {
            teacherExperience.requestFocus();
            teacherExperience.setError("You Enter Your Experience");
        }

        else if (classesYouCanTeachTeacher.equals("Which Class do you Teach")) {
            Toast.makeText(this, "Select Class You can Teach", Toast.LENGTH_SHORT).show();
        }
        else if (subjectsYouCanTeacher.equals("Which Subject do you Teach")) {
            Toast.makeText(this, "Select Subject Teach", Toast.LENGTH_SHORT).show();
        }
        else if (genderTeacher.equals("Gender")) {
            teacherGender.requestFocus();
            Toast.makeText(this, "Select Your Gender", Toast.LENGTH_SHORT).show();
        }
        else if (howDidYouFindUsTeacher.equals("How Did You Find Us")) {
            Toast.makeText(this, "How Did You Find Us", Toast.LENGTH_SHORT).show();
        }

        else {
            progressDialog.setTitle("Insert Data");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference("Teacher");
            final StorageReference myreff = storageReference.child(System.currentTimeMillis() + "." + getExtension(teacherSelectImageUri));

            uploadTask = myreff.putFile(teacherSelectImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            myreff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String profileImageTeacher = String.valueOf(uri);

                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTeacher, passwordTeacher)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    progressDialog.dismiss();

                                                    if (task.isSuccessful()) {


                                                        final HashMap insertTeacherData = new HashMap();

                                                        insertTeacherData.put("ProfileImageURLTeacher", profileImageTeacher);
                                                        insertTeacherData.put("UserName", userTeacherName);
                                                        insertTeacherData.put("Email", emailTeacher);
                                                        insertTeacherData.put("Password", passwordTeacher);
                                                        insertTeacherData.put("ConfirmPassword", confirmPasswordTeacher);
                                                        insertTeacherData.put("SecondContactNumber", secondContactNumber);
                                                        insertTeacherData.put("ContactNumber", contactNumberTeacher);
                                                        insertTeacherData.put("Address", addressTeacher);
                                                        insertTeacherData.put("Age", ageTeacher);
                                                        insertTeacherData.put("CNIC", cNICTeacher);
                                                        insertTeacherData.put("Qualification", qualificationTeacher);
                                                        insertTeacherData.put("SubjectsYouCan", subjectsYouCanTeacher);
                                                        insertTeacherData.put("Experience", experienceTeacher);
                                                      //  insertTeacherData.put("PreferableTeachingArea", preferableTeachingAreaTeacher);
                                                        insertTeacherData.put("ClassYouCanTeach", classesYouCanTeachTeacher);
                                                        insertTeacherData.put("Gender", genderTeacher);
                                                        insertTeacherData.put("HowDidYouFindUs", howDidYouFindUsTeacher);
                                                      //  insertTeacherData.put("Route", routeTeacher);
                                                        insertTeacherData.put("Role", roleTeacher);
                                                        insertTeacherData.put("Description", descriptionTeacher);

                                                        FirebaseDatabase.getInstance().getReference().child("Teacher")
                                                                .child(currentUserForAdminID)
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .setValue(insertTeacherData)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Intent intent = new Intent(TeacherRegistrations.this, AdminDashboard.class);
                                                                            startActivity(intent);
                                                                            Toast.makeText(TeacherRegistrations.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                                                        } else {

                                                                            Toast.makeText(TeacherRegistrations.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });

                                                    } else {
                                                        Toast.makeText(TeacherRegistrations.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

}