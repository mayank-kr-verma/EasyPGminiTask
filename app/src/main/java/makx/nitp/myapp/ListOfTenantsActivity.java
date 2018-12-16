package makx.nitp.myapp;

import android.content.Intent;
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
import java.util.List;


public class ListOfTenantsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mReference;
    List<String> tenantlist;
    ArrayAdapter adapter;
    String message;
    ListView tlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftenants);
        tlistview = findViewById(R.id.tlistview);

        mAuth = FirebaseAuth.getInstance();
        tenantlist = new ArrayList<>();

        final String email = mAuth.getCurrentUser().getEmail();

        mReference = FirebaseDatabase.getInstance().getReference().child("users").child(email.replace('.',',')).child("tenants");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tenantlist.clear();
                if (dataSnapshot.exists()) {
                    int i = 0;
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        tenantlist.add(d.getKey().replace(',','.'));
                        i++;
                    }
                }
                Log.e("tenantlist",tenantlist.toString());
                adapter = new ArrayAdapter<>(ListOfTenantsActivity.this,android.R.layout.simple_list_item_1,tenantlist);
                tlistview.setAdapter(adapter);
                tlistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> l, View v, int position, long id)
                    {
                        String emailClicked = tenantlist.toArray()[position].toString().replace('.',',');
                        Log.i("ListView", "You clicked at " + emailClicked);
                        Intent n = new Intent(getApplicationContext(), IssuesActivity.class);
                        n.putExtra("email", emailClicked);
                        startActivity(n);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
