package makx.nitp.myapp;



import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.AlertDialog.*;

public class ComplaintActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mReference;
    ArrayList complaintlist;
    Complaint complaint;
    ArrayAdapter adapter;
    String message,complaintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        final ListView clistview = findViewById(R.id.clistview);
        Button btnLogComplaint = findViewById(R.id.logComplaint);

        mAuth = FirebaseAuth.getInstance();
        complaintlist = new ArrayList<>();

        final String email = mAuth.getCurrentUser().getEmail();

        mReference = FirebaseDatabase.getInstance().getReference().child("users").child(email.replace('.',',')).child("complaints");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintlist.clear();
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    complaint = item.getValue(Complaint.class);
                    complaintlist.add(complaint.getMessage());
                    Log.e("data",complaint.getMessage()+"");
                }

                adapter = new ArrayAdapter<>(ComplaintActivity.this,android.R.layout.simple_list_item_1,complaintlist);
                clistview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnLogComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder = new Builder(ComplaintActivity.this);
                builder.setTitle("Complaint");

                final EditText input = new EditText(ComplaintActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        message = input.getText().toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(email.replace('.',',')).child("noOfComplaints");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                complaintId = (String) dataSnapshot.getValue();
                                mReference.child(complaintId).child("Message").setValue(message);
                                mReference.child(complaintId).child("received").setValue("false");
                                mReference.child(complaintId).child("solved").setValue("false");

                                int newComplaintId = Integer.valueOf(complaintId) + 1;
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(email.replace('.',','))
                                        .child("noOfComplaints").setValue(newComplaintId+"");
                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });

    }
}
