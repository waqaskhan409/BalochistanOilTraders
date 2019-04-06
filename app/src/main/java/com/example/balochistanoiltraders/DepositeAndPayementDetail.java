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
import android.support.design.widget.TextInputLayout;
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

import static android.support.constraint.Constraints.TAG;

public class DepositeAndPayementDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "DepositeAndPayementDeta";
    private TextInputEditText moneyTransfer, bankName, remainingMoney;
    private EditText description;
    private TextView date;
    private FloatingActionButton fab;
    String moneyTransferS, bankNameS, dateE, descriptionOfMoney, key, parentKey;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Detail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle data = getIntent().getExtras();
        moneyTransferS = data.getString("moneyTransfer");
        bankNameS = data.getString("bankName");
        descriptionOfMoney = data.getString("description");
        dateE = data.getString("date");
        key = data.getString("key");
        parentKey = data.getString("parentKey");

        Log.d(TAG, "onCreate: parentKey :"+ parentKey);
        Log.d(TAG, "onCreate: key :"+ key);
        moneyTransfer = (TextInputEditText) findViewById(R.id.moneyTransferForAccountDetails);
        bankName = (TextInputEditText) findViewById(R.id.bankNameForAccountDetails);
        description = (EditText) findViewById(R.id.descriptionForAccountDetails);
        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bankName.isEnabled()){
                    showDateDialogue();
                }
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        DepositeAndPayementDetail.this.getResources().getString(R.string.email))) {
                    editable();
                }else {
                    Toast.makeText(DepositeAndPayementDetail.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        setTextOnEditText();
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, CashBorrowed.class);
        intent.putExtra("key",parentKey);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        final String moneyT, bankN, remainingM, des, totalString[] = new String[0];
        moneyT = moneyTransfer.getText().toString();
        bankN = bankName.getText().toString();
        remainingM = date.getText().toString();
        des = description.getText().toString();


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("cash_borrow").child(parentKey).child("deposite_money").child(key);
        databaseReference.child("money_transfer").setValue(moneyT);
        databaseReference.child("bank_name").setValue(bankN);
        databaseReference.child("date").setValue(remainingM);
        databaseReference.child("description").setValue(des);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

        databaseReference = mDatabase;
        final ArrayList<String> arrayList1 = new ArrayList<>();
        final ArrayList<String> arrayListAddition = new ArrayList<>();

        databaseReference.child(parentKey).child("deposite_money").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    arrayList1.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: " + arrayList1);
                for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                    String value = dataSnapshot
                            .child(arrayList1.get(i))
                            .child("money_transfer")
                            .getValue().toString();
                    arrayListAddition.add(value);
                }
                Log.d(TAG, "onDataChange: " + arrayListAddition);

                FirebaseDatabase.getInstance()
                        .getReference().child("cash_borrow").child(parentKey)
                        .child("money").child("deposite_money")
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    void setTextOnEditText(){
        moneyTransfer.setText(moneyTransferS);
        bankName.setText(bankNameS);
        date.setText(dateE);
        description.setText(descriptionOfMoney);
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

        date.setText(String.valueOf(dayOfMonth) + "/" +
                String.valueOf(month+1) + "/" +
                String.valueOf(year));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void editable(){
        if(!moneyTransfer.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
            moneyTransfer.setEnabled(true);
            bankName.setEnabled(true);
            description.setEnabled(true);
        }else if(moneyTransfer.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_edit));
            moneyTransfer.setEnabled(false);
            bankName.setEnabled(false);
            description.setEnabled(false);
            dialogShow();

        }
    }
}
