package makx.nitp.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class TenantActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    final String appkey = "1a2c77215f9e0157832fcdce06b520683";
    String billValue = "0";
    String rentValue = "0";
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        Button button = findViewById(R.id.signout);
        Button btnRent = findViewById(R.id.rentPay);
        Button btnBill = findViewById(R.id.billPay);
        Button btnComp = findViewById(R.id.complaint);
        Button btnChat = findViewById(R.id.chatbot);

        Kommunicate.init(this, appkey);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

        final String email = mAuth.getCurrentUser().getEmail();
        mdatabase.child("users").child(email.replace('.',',')).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                billValue = map.get("billValue").toString();
                Log.d("billValue ",email+" :"+billValue);
                rentValue = map.get("rentValue").toString();
                Log.d("rentValue ",email+" :"+rentValue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(TenantActivity.this, signinActivity.class));
                }

            }
        };

        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdatabase.child("users").child(email.replace('.',',')).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        billValue = map.get("billValue").toString();
                        Log.d("billValue ",email+":"+billValue);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(TenantActivity.this);
                builder.setTitle("Pay Bill");
                builder.setMessage("Your current bill to pay is of "+billValue+" rupees.");
                builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdatabase.child("users").child(email.replace('.',',')).child("billValue").setValue("0");
                        Toast.makeText(TenantActivity.this,"Paid bill of "+billValue+" rupees",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdatabase.child("users").child(email.replace('.',',')).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        rentValue = map.get("rentValue").toString();
                        Log.d("rentValue ",email+":"+rentValue);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(TenantActivity.this);
                builder.setTitle("Pay Rent");
                builder.setMessage("Your current Rent due is of "+rentValue+" rupees.");
                builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdatabase.child("users").child(email.replace('.',',')).child("rentValue").setValue("0");
                        Toast.makeText(TenantActivity.this,"Paid rent of "+rentValue+" rupees",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });

        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TenantActivity.this, ComplaintActivity.class));
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> agentList = new ArrayList();
                agentList.add(""); //add your agentID

                List<String> botList = new ArrayList();
                botList.add("newbot"); //enter your integrated bot Ids

                Kommunicate.launchSingleChat(TenantActivity.this, "Support", Kommunicate.getVisitor(), false, true, agentList, botList, new KmCallback(){

                    @Override
                    public void onSuccess(Object message) {
                        Log.e("ChatLaunch", "Success : " + message);
                    }

                    @Override
                    public void onFailure(Object error) {
                        Log.e("ChatLaunch", "Failure : " + error);
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });


    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}