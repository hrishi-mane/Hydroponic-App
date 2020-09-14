package com.example.hydrophoicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    SwitchCompat switchCompat;

    EditText humidity,temperature; //EditText ref to backend

    String hum,temp;  //String variables to store the value of humidity and temperature from firebase.

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference modeRef = FirebaseDatabase.getInstance().getReference("Automatic");

    private Menu menu1;

    Drawable thumb;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidity = (EditText) findViewById(R.id.editText);
        temperature = (EditText) findViewById(R.id.editText2);

        switchCompat = (SwitchCompat) findViewById(R.id.switchButton);

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value", true));

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchCompat.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    switchCompat.setChecked(true);
                    myRef.child("status").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Turned ON", Toast.LENGTH_SHORT ).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed, Check the internet connection", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }

                else{
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    switchCompat.setChecked(false);
                    myRef.child("status").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Turned OFF", Toast.LENGTH_SHORT ).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed, Check the internet connection", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {  //If any value in the main parent object is changed what should happen.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hum = dataSnapshot.child("TestHumidity").getValue().toString();
                humidity.setText(hum);
                temp = dataSnapshot.child("TestTemperature").getValue().toString();
                temperature.setText(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mode_menu, menu);
        this.menu1 = menu;

        modeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(Integer.parseInt(dataSnapshot.getValue().toString()) == 0){
                    menu1.getItem(0).getSubMenu().getItem(0).setIcon(getResources().getDrawable( R.drawable.remove_ic_tick));
                    menu1.getItem(0).getSubMenu().getItem(1).setIcon(getResources().getDrawable( R.drawable.ic_tick ));
                    switchCompat.setClickable(true);
                }
                else {
                    menu1.getItem(0).getSubMenu().getItem(1).setIcon(getResources().getDrawable( R.drawable.remove_ic_tick));
                    menu1.getItem(0).getSubMenu().getItem(0).setIcon(getResources().getDrawable( R.drawable.ic_tick ));

                    myRef.child("Automatic").setValue(1);

                    switchCompat.setThumbDrawable(getResources().getDrawable( R.drawable.sw_disabled_thumb ));
                    switchCompat.setTrackDrawable(getResources().getDrawable( R.drawable.sw_disabled_track ));

                    switchCompat.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_auto:
                menu1.getItem(0).getSubMenu().getItem(1).setIcon(getResources().getDrawable( R.drawable.remove_ic_tick));
                menu1.getItem(0).getSubMenu().getItem(0).setIcon(getResources().getDrawable( R.drawable.ic_tick ));

                myRef.child("Automatic").setValue(1);

                switchCompat.setThumbDrawable(getResources().getDrawable( R.drawable.sw_disabled_thumb ));
                switchCompat.setTrackDrawable(getResources().getDrawable( R.drawable.sw_disabled_track ));
                switchCompat.setClickable(false);
                return true;
            case R.id.id_manual:
                myRef.child("Automatic").setValue(0);

                menu1.getItem(0).getSubMenu().getItem(0).setIcon(getResources().getDrawable( R.drawable.remove_ic_tick));
                menu1.getItem(0).getSubMenu().getItem(1).setIcon(getResources().getDrawable( R.drawable.ic_tick ));

                switchCompat.setThumbDrawable(getResources().getDrawable( R.drawable.sw_thumb ));
                switchCompat.setTrackDrawable(getResources().getDrawable( R.drawable.sw_track ));
                switchCompat.setClickable(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
