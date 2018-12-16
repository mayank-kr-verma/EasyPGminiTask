package makx.nitp.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText email_id, passwordcheck, owneridfield;
    private RadioButton rbtenant, rbowner;
    private FirebaseAuth mAuth;
    private static final String TAG = "myapp ->";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        TextView btnSignIn = findViewById(R.id.login_page);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupActivity.this, signinActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email_id = findViewById(R.id.input_email);
        progressBar = findViewById(R.id.progressBar);
        passwordcheck = findViewById(R.id.input_password);
        owneridfield = findViewById(R.id.input_owner);
        rbtenant = findViewById(R.id.rbTenant);
        rbowner = findViewById(R.id.rbOwner);
        Button btnSignup = findViewById(R.id.btn_signup);
        final RadioGroup rgType = findViewById(R.id.rgType);

        rbtenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                owneridfield.setVisibility(View.VISIBLE);
            }
        });
        rbowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                owneridfield.setVisibility(View.GONE);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(signupActivity.this);
                final String email = email_id.getText().toString();
                final String password = passwordcheck.getText().toString();
                final String ownerid = owneridfield.getText().toString();
                int selectedId = rgType.getCheckedRadioButtonId();
                RadioButton rbType = findViewById(selectedId);

                final String userType = rbType.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userType.equals("Tenant") && TextUtils.isEmpty(ownerid)){
                    Toast.makeText(getApplicationContext(), "Enter OwnerId", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");

                                    String memail = email.replace('.',',');
                                    mDatabase.child("users").child(memail).child("type").setValue(userType);

                                    if(userType.equals("Tenant")){
                                        mDatabase.child("users").child(memail).child("billValue").setValue("0");
                                        mDatabase.child("users").child(memail).child("rentValue").setValue("0");
                                        mDatabase.child("users").child(memail).child("noOfComplaints").setValue("0");
                                        mDatabase.child("users").child(memail).child("owner").setValue(ownerid);
                                        mDatabase.child("users").child(ownerid.replace('.',',')).child("tenants").child(memail).setValue("true");
                                    }

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(signupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(signupActivity.this, "Error in registration. Is this account already registered?",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}