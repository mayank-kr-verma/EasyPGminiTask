package makx.nitp.myapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IssuesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mReference;
    ArrayList complaintlist;
    Complaint complaint;
    ArrayAdapter adapter;
    String message,email,solved="false",received="false",issue;
    ListView clistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        clistview = findViewById(R.id.ilistview);


        mAuth = FirebaseAuth.getInstance();
        complaintlist = new ArrayList<>();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                email= null;
            } else {
                email= extras.getString("email");
            }
        } else {
            email= (String) savedInstanceState.getSerializable("email");
        }

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

                adapter = new ArrayAdapter<>(IssuesActivity.this,android.R.layout.simple_list_item_1,complaintlist);
                clistview.setAdapter(adapter);
                clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        Log.e("click","you selected "+pos);
                        final int position = pos;
                            mReference.child(position+"").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.e("datasnapshot",dataSnapshot.toString());
                                    solved = dataSnapshot.child("solved").getValue().toString();
                                    received = dataSnapshot.child("received").getValue().toString();
                                    issue = dataSnapshot.child("Message").getValue().toString();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        AlertDialog.Builder builder = new AlertDialog.Builder(IssuesActivity.this);
                        builder.setTitle("Complaint");

                        builder.setMessage(issue);


                        if(solved.equals("false")) {
                            builder.setPositiveButton("Solved", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mReference.child(position + "").child("solved").setValue("true");
                                    mReference.child(position + "").child("received").setValue("true");
                                }
                            });
                        }
                        if(received.equals("false")) {
                            builder.setNegativeButton("Received", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mReference.child(position + "").child("received").setValue("true");
                                }
                            });
                        }
                        builder.show();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
