package com.example.balochistanoiltraders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpanceDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "ExpanceDetails";
    TextInputEditText nameE, moneySpendE;
    TextView dateText;
    EditText descriptionE;
    String name, moneySpend, description, key, parentKey, date;
    DatabaseReference mDatabase;
    FloatingActionButton fab;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expance_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expance Detail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle intent = getIntent().getExtras();
        name = intent.getString("name");
        moneySpend = intent.getString("moneySpend");
        description = intent.getString("description");
        date = intent.getString("date");
        key = intent.getString("key");
        parentKey = intent.getString("parentKey");


              nameE = (TextInputEditText) findViewById(R.id.nameExpances);
              moneySpendE = (TextInputEditText) findViewById(R.id.moneySpend);
              descriptionE = (EditText) findViewById(R.id.description);
              dateText = (TextView) findViewById(R.id.date);
              nameE.setText(name);
              moneySpendE.setText(moneySpend);
              descriptionE.setText(description);
              dateText.setText(date);

              dateText.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if(nameE.isEnabled()){
                          showDateDialogue();

                      }
                  }
              });

              fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        ExpanceDetails.this.getResources().getString(R.string.email))) {
                    editable();
                }else {
                    Toast.makeText(ExpanceDetails.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, CashBorrowed.class);
        intent.putExtra("key",parentKey);
        startActivity(intent);

        return true;
    }
    private void dialogShow() {
        LayoutInflater li = LayoutInflater.from(this);
        View view= li.inflate(R.layout.layout_for_confirm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
        // set dialog message
        alertDialogBuilder.setTitle("Are You Sure to Act this?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                updateFirebase();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    private void updateFirebase() {
        final String name;
        final String moneySpendT;
        final String des;
        final String date;
        final String[] totalString = new String[1];
        name = nameE.getText().toString();
        moneySpendT = moneySpendE.getText().toString();
        des = descriptionE.getText().toString();
        date = dateText.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("cash_borrow").child(parentKey)
                .child("cash_borrow").child(key);
        mDatabase.child("name").setValue(name);
        mDatabase.child("spend_money").setValue(moneySpendT);
        mDatabase.child("description").setValue(des);
        mDatabase.child("date").setValue(date);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

        DatabaseReference databaseReference = mDatabase;
        final ArrayList<String> arrayList1 = new ArrayList<>();
        final ArrayList<String> arrayListAddition = new ArrayList<>();

        databaseReference.child(parentKey).child("cash_borrow").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    arrayList1.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: " + arrayList1);
                for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                    String value = dataSnapshot
                            .child(arrayList1.get(i))
                            .child("spend_money")
                            .getValue().toString();
                    arrayListAddition.add(value);
                }
                Log.d(TAG, "onDataChange: " + arrayListAddition);

                FirebaseDatabase.getInstance()
                        .getReference().child("cash_borrow").child(parentKey)
                        .child("money").child("money_borrowed")
                        .setValue(setupOverViewValue(arrayListAddition));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String setupOverViewValue(ArrayList<String> arrayListAddition) {

        int t = 0;
        for (int i = 0; i < arrayListAddition.size(); i++) {
            t = t + Integer.parseInt(arrayListAddition.get(i));
        }
        return String.valueOf(t);
    }


    private void showDateDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
           ;
        dateText.setText(String.valueOf(dayOfMonth) + "/" +
                String.valueOf(month+1) + "/" +
                String.valueOf(year));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void editable(){
        if(!nameE.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
            nameE.setEnabled(true);
            moneySpendE.setEnabled(true);
            descriptionE.setEnabled(true);

        }else if(nameE.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_edit));
            nameE.setEnabled(false);
            moneySpendE.setEnabled(false);
            descriptionE.setEnabled(false);
            dialogShow();
        }
    }
}
