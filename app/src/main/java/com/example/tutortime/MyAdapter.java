package com.example.tutortime;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

public class MyAdapter extends FirebaseRecyclerAdapter<AdminModel, MyAdapter.MyAllUserViewHolder> {

    public MyAdapter(@NonNull FirebaseRecyclerOptions<AdminModel> options) {
        super(options);





    }

    @Override
    protected void onBindViewHolder(@NonNull MyAllUserViewHolder myAllUserViewHolder, int position, @NonNull AdminModel adminModel) {

        myAllUserViewHolder.textViewUserName.setText(adminModel.getName());
        myAllUserViewHolder.textViewUserCNIC.setText(adminModel.getCNIC());
        myAllUserViewHolder.textViewUserPhone.setText(adminModel.getContactNumber());




        myAllUserViewHolder.imageViewViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus =  DialogPlus.newDialog(myAllUserViewHolder.textViewUserName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.view_layout))
                        .setExpanded(true,1500)
                        .create();

                View myViewForView = dialogPlus.getHolderView();
                TextView receiveMyViewName = myViewForView.findViewById(R.id.viewName);
                TextView receiveMyViewNumber = myViewForView.findViewById(R.id.viewNumber);
                TextView receiveMyViewEmail = myViewForView.findViewById(R.id.viewEmail);
                TextView receiveMyViewCNIC = myViewForView.findViewById(R.id.viewCNIC);
                TextView receiveMyViewAddress = myViewForView.findViewById(R.id.viewAddress);

                receiveMyViewName.setText(adminModel.getName());
                receiveMyViewNumber.setText(adminModel.getContactNumber());
                receiveMyViewEmail.setText(adminModel.getEmail());
                receiveMyViewCNIC.setText(adminModel.getCNIC());
                receiveMyViewAddress.setText(adminModel.getAddress());


                dialogPlus.show();

            }
        });





        myAllUserViewHolder.imageViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus =  DialogPlus.newDialog(myAllUserViewHolder.textViewUserName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_layout))
                        .setExpanded(true,1500)
                        .create();

                View myViewForUpdate = dialogPlus.getHolderView();
                EditText receiveMyName = myViewForUpdate.findViewById(R.id.updateName);
                EditText receiveMyNumber = myViewForUpdate.findViewById(R.id.updateNumber);
                TextView receiveMyEmail = myViewForUpdate.findViewById(R.id.updateEmail);
                EditText receiveMyCNIC = myViewForUpdate.findViewById(R.id.updateCNIC);
                EditText receiveMyAddress = myViewForUpdate.findViewById(R.id.updateAddress);

                Button receiveMyUpdateBTN = myViewForUpdate.findViewById(R.id.updateData);


                receiveMyName.setText(adminModel.getName());
                receiveMyNumber.setText(adminModel.getContactNumber());
                receiveMyEmail.setText(adminModel.getEmail());
                receiveMyCNIC.setText(adminModel.getCNIC());
                receiveMyAddress.setText(adminModel.getAddress());


                dialogPlus.show();

                receiveMyUpdateBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String updateMyReceivedName = receiveMyName.getText().toString();
                        String updateMyReceivedNumber = receiveMyNumber.getText().toString();
                        String updateMyReceivedEmail = receiveMyEmail.getText().toString();
                        String updateMyReceivedCNIC = receiveMyCNIC.getText().toString();
                        String updateMyReceivedAddress = receiveMyAddress.getText().toString();


                        if (TextUtils.isEmpty(updateMyReceivedName)){
                            receiveMyName.requestFocus();
                            receiveMyName.setText("Not Empty");
                        }
                        else if (TextUtils.isEmpty(updateMyReceivedNumber)){
                            receiveMyNumber.requestFocus();
                            receiveMyNumber.setText("Not Empty");
                        }

                        else if (!updateMyReceivedNumber.matches("\\+[0-9]{10,13}$")){
                            receiveMyNumber.requestFocus();
                            receiveMyNumber.setText("+923326882570");

                        }
                        else if (TextUtils.isEmpty(updateMyReceivedEmail)){
                            receiveMyEmail.requestFocus();
                            receiveMyEmail.setText("Not Empty");
                        }

                        else if (TextUtils.isEmpty(updateMyReceivedCNIC)){
                            receiveMyCNIC.requestFocus();
                            receiveMyCNIC.setError("Not Empty");
                        }
                        else if (!updateMyReceivedCNIC.matches("[0-9]{5}-[0-9]{7}-[0-9]{1}$")){
                            receiveMyCNIC.requestFocus();
                            receiveMyCNIC.setError("33100-XXXXXXX-X");

                        }
                        else if (TextUtils.isEmpty(updateMyReceivedAddress)){
                            receiveMyAddress.requestFocus();
                            receiveMyAddress.setError("Not Empty");
                        }



                        else {

                            HashMap updateProfileData = new HashMap();
                            updateProfileData.put("Name",updateMyReceivedName);
                            updateProfileData.put("ContactNumber",updateMyReceivedNumber);
//                            updateProfileData.put("UserEmail",updateMyReceivedEmail);
                            updateProfileData.put("CNIC", updateMyReceivedCNIC);
                            updateProfileData.put("Address", updateMyReceivedAddress);


                            FirebaseDatabase.getInstance().getReference()
                                    .child("Admin")
                                    .child(getRef(position).getKey())
                                    .updateChildren(updateProfileData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogPlus.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            dialogPlus.dismiss();
                                        }
                                    });

                        }

                    }
                });
            }
        });




        myAllUserViewHolder.imageViewDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myAllUserViewHolder.textViewUserName.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference()
                                .child("Admin")
                                .child(getRef(position).getKey()).removeValue();

                        //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        dialogInterface.dismiss();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        dialogInterface.dismiss();

                    }
                });


                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public MyAllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_admin, parent, false);
        return new MyAllUserViewHolder(view);
    }


    public static class MyAllUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewUserName, textViewUserCNIC, textViewUserPhone;

        ImageView imageViewViewProfile, imageViewEditProfile, imageViewDeleteProfile;

        public FullUserInterFace fullUserInterFace;


        public MyAllUserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = (TextView) itemView.findViewById(R.id.myUserName);
            textViewUserCNIC = (TextView) itemView.findViewById(R.id.myUserCNIC);
            textViewUserPhone = (TextView) itemView.findViewById(R.id.myUserPhone);

            imageViewViewProfile = (ImageView) itemView.findViewById(R.id.viewProfile);
            imageViewEditProfile = (ImageView) itemView.findViewById(R.id.editProfile);
            imageViewDeleteProfile = (ImageView) itemView.findViewById(R.id.deleteProfile);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
         //   fullUserInterFace.onClick(view,false);

        }

        public void fullUserData(FullUserInterFace myFullUserInterFace){

            this.fullUserInterFace = myFullUserInterFace;
        }
    }
}
