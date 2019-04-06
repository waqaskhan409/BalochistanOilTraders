package com.example.balochistanoiltraders;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Depreciation extends Fragment {
    private static final String TAG = "Depreciation";
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("depreciation");
    RecyclerView recyclerView;
    DepreciationAdapter mAdapter;
    FirebaseAuth mAuth =  FirebaseAuth.getInstance();


    public Depreciation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_depreciation, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerForDepreciation);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Depreciations");
        LinearLayoutManager li = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(li);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        getActivity().getResources().getString(R.string.email))) {
                    dialogShowForStock();
                }else {
                    Toast.makeText(getActivity(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });

        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>().
                setQuery(mDatabase, Note.class).build();
        mAdapter = new DepreciationAdapter(options);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {

        mAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onStart() {
        mAdapter.startListening();
        super.onStart();
    }

    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.form_for_depreciation, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputLayout nameE = (TextInputLayout) view
                .findViewById(R.id.name);
        final TextInputLayout loan = (TextInputLayout) view
                .findViewById(R.id.loan);
        final TextInputLayout Description = (TextInputLayout) view
                .findViewById(R.id.description);

        // set dialog message
        alertDialogBuilder.setTitle("Employee Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String name, load, des, addressS;
                                name = nameE.getEditText().getText().toString();
                                load = loan.getEditText().getText().toString();
                                des = Description.getEditText().getText().toString();
                                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(load) || TextUtils.isEmpty(des)){
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();

                                }else {
                                    mDatabase = mDatabase.child(name);
                                    mDatabase.child("name").setValue(name);
                                    mDatabase.child("loan").setValue(load);
                                    mDatabase.child("description").setValue(des);

                                    Date date = new Date();
                                    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                                    Log.d(TAG, "Dialogue: "+format.format(date));
                                    mDatabase.child("date").setValue(format.format(date));

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
