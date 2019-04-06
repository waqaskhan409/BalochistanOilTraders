package com.example.balochistanoiltraders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

@SuppressLint("ValidFragment")
public class Overview extends Fragment {
    private static final String TAG = "Overview";
    private DatabaseReference mDatabase;
    private Button submit;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayListAddition = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String preDeposite, preCash, preCashBorrowed, preLiability;
    TextInputLayout cashBorroed, netCash, deposite, liability;
    private String key;
    public Overview(String key) {
        this.key = key;
        Log.d(TAG, "Overview: "+ key);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_for_dailogue_total, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

        cashBorroed = (TextInputLayout) view.findViewById(R.id.cashBorrowed) ;
     netCash = (TextInputLayout) view.findViewById(R.id.netCash) ;
     deposite = (TextInputLayout) view.findViewById(R.id.depositeToBank) ;
     submit = (Button) view.findViewById(R.id.submit) ;
     submit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(mAuth.getCurrentUser().getEmail().equals(
                     getActivity().getResources().getString(R.string.email))) {
                 if (submit.getText().toString().equals("Edit")) {
                     netCash.getEditText().setFocusable(true);
                     netCash.getEditText().setEnabled(true);
                     netCash.getEditText().setFocusableInTouchMode(true);
                     submit.setText("Submit");
                 } else if (submit.getText().toString().equals("Submit")) {
                     mDatabase.child(key).child("money").child("cash").setValue(netCash.getEditText().getText().toString());
                     netCash.getEditText().setFocusable(true);
                     netCash.getEditText().setEnabled(false);
                     netCash.getEditText().setFocusableInTouchMode(true);
                     submit.setText("Edit");
                     firebaseValue();
                 }
             }else {
                 Toast.makeText(getActivity(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

             }
         }
     });
        liability = (TextInputLayout) view.findViewById(R.id.liability) ;
        firebaseValue();

        return view;
    }
    private void  firebaseValue(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                preDeposite = dataSnapshot.child(key)
                        .child("money")
                        .child("deposite_money")
                        .getValue().toString();

                preCashBorrowed = dataSnapshot.child(key)
                        .child("money")
                        .child("money_borrowed")
                        .getValue().toString();
                preCash = dataSnapshot.child(key)
                        .child("money")
                        .child("cash")
                        .getValue().toString();
                setupValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupValue() {


//        String depo = deposite.getEditText().getText().toString();
//        String cashB = cashBorroed.getEditText().getText().toString();
//        String netCashS = netCash.getEditText().getText().toString();
//        Log.d(TAG, "setupValue: " + depo + ":" + cashB + ":"+ netCashS);

        int liabilityInteger = (Integer.parseInt(preCashBorrowed) - (Integer.parseInt(preCash) + Integer.parseInt(preDeposite)));


        cashBorroed.getEditText().setText(preCashBorrowed);
        deposite.getEditText().setText(preDeposite);
        netCash.getEditText().setText(preCash);
        liability.getEditText().setText(String.valueOf(liabilityInteger));
    }
}
