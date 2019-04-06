package com.example.balochistanoiltraders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CashBorrowDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "CashBorrowDetail";
    TextView mDate;
    TextInputEditText nameE, moneySpendE;
    EditText descriptionE;
    String name, moneySpend, description, key, date;
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
        Bundle intent = getIntent().getExtras();
        name = intent.getString("name");
        moneySpend = intent.getString("moneySpend");
        description = intent.getString("description");
        date = intent.getString("date");
        key = intent.getString("key");

        nameE = (TextInputEditText) findViewById(R.id.nameExpances);
        moneySpendE = (TextInputEditText) findViewById(R.id.moneySpend);
        descriptionE = (EditText) findViewById(R.id.description);
        mDate = (TextView) findViewById(R.id.date);
        mDate.setText(date);
        if(nameE.isEnabled()){
            mDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        nameE.setText(name);
        moneySpendE.setText(moneySpend);
        descriptionE.setText(description);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        CashBorrowDetail.this.getResources().getString(R.string.email))) {
                    editable();
                }else {
                    Toast.makeText(CashBorrowDetail.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
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
        String name, moneySpend, des, addressS;
        name = nameE.getText().toString();
        moneySpend = moneySpendE.getText().toString();
        des = descriptionE.getText().toString();


        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("expances").child(key);
        mDatabase.child("name").setValue(name);
        mDatabase.child("spend_money").setValue(moneySpend);
        mDatabase.child("description").setValue(des);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dat =  String.valueOf(dayOfMonth) + "/" +
                String.valueOf(month+1) + "/" +
                String.valueOf(year) ;
        mDate.setText(dat);
    }
}
