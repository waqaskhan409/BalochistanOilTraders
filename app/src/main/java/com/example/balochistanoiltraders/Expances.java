package com.example.balochistanoiltraders;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Expances extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    TextView textView;
    ExpancesAdapter mAdapter;
    String value;

    public Expances() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expances, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Expences");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerForExpances);

        LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);
        recyclerView.setHasFixedSize(true);
        textView = (TextView) view.findViewById(R.id.monthlyExpances);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        marginLayoutParams.setMargins(0, 240, 0, 0);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialogShowForMonthlySxpence();
            }
        });
        textView.setLayoutParams(marginLayoutParams);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowForStock();
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("monthly_expances").getValue().toString();
                textView.setText(value + " Rs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(mDatabase.child("expances"),Note.class).build();
    mAdapter = new ExpancesAdapter(options);
    recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    private void dialogShowForMonthlySxpence() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.prompts, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputEditText nameE = (TextInputEditText) view
                .findViewById(R.id.addToStock);

        // set dialog message
        alertDialogBuilder.setTitle("Monthly Expances Add Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Date date = new Date();
                                date.getDate();
                                String name, moneySpend, description, addressS;
                                name = nameE.getText().toString();
                                mDatabase.child("monthly_expances").setValue(name);


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

    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.layout_for_expances, null);


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);
        final View[] item = {null};
        final boolean[] cancel = {true};

        final TextInputEditText nameE = (TextInputEditText) view
                .findViewById(R.id.nameExpances);
        final TextInputEditText moneySpendE = (TextInputEditText) view
                .findViewById(R.id.moneySpend);

        final EditText descriptionE = (EditText) view
                .findViewById(R.id.description);
        // set dialog message
        alertDialogBuilder.setTitle("Party Add Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String name, moneySpend, description, addressS;
                                name = nameE.getText().toString();
                                moneySpend = moneySpendE.getText().toString();
                                description = descriptionE.getText().toString();
                                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(moneySpend) || TextUtils.isEmpty(description)){
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                long timeStamp = 1 * new Date().getTime();
                                    String NewDescTimeStamp= String.valueOf((9999999999999L+(-1 * timeStamp)));
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("monthly_expances").setValue(
                                                    String.valueOf(
                                                            (Integer.parseInt(value)-Integer.parseInt(moneySpend))
                                                    ));
                                mDatabase = mDatabase.child("expances").child(NewDescTimeStamp);
                                mDatabase.child("name").setValue(name);
                                mDatabase.child("spend_money").setValue(moneySpend);
                                mDatabase.child("description").setValue(description);
                                    Date date = new Date();
                                    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                                    Log.d(TAG, "Dialogue: "+format.format(date));
                                    mDatabase.child("date").setValue(format.format(date));

                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                    Toast.makeText(getContext(), "Data posted", Toast.LENGTH_SHORT).show();
                                }

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

}
