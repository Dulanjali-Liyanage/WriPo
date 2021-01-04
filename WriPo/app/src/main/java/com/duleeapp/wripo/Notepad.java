package com.duleeapp.wripo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Notepad extends AppCompatActivity {
    public static final String TAG = "TAG";
    Toolbar toolbar;
    EditText title, writenote;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.title);
        writenote = findViewById(R.id.writenote);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

    //display the menu on the tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.savenote){
            String mtitle = title.getText().toString();
            String mwritenote = writenote.getText().toString();

            //get the current user ID
            userID = fAuth.getCurrentUser().getUid();

            //save to firestore with an unique id for every poem
            DocumentReference documentReference = fstore.collection("poems").document();
            Map<String,Object> poem = new HashMap<>();
            poem.put("userID",userID);
            poem.put("title", mtitle);
            poem.put("poem",mwritenote);

            documentReference.set(poem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"new poem is created for "+ userID);
                    Toast.makeText(Notepad.this,"Saved Successfully",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure: "+ e.toString());
                }
            });

        }else if(item.getItemId() == R.id.addcover){

        }
        return super.onOptionsItemSelected(item);
    }
}
