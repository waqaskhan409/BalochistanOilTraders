package com.example.balochistanoiltraders;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link Account.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private PartyAccountAdapter mAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("accounts_for_party");
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.accountList);
        LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);

//        li.setStackFromEnd(false);
//        li.setReverseLayout(true);
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
        setupAdapter();
        return view;
    }
    void setupAdapter(){
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(mDatabase,Note.class).build();
        mAdapter = new PartyAccountAdapter(options);
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

    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.party_name_form, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputEditText name = (TextInputEditText) view
                .findViewById(R.id.partyNameForAccounts);
        final TextInputEditText phoneNumber = (TextInputEditText) view
                .findViewById(R.id.phoneNumberForAccounts);
        final TextInputEditText jobDescription = (TextInputEditText) view
                .findViewById(R.id.businessNameForAccounts);
        final EditText address = (EditText) view
                .findViewById(R.id.businessDescriptionForAccounts);
        // set dialog message
        alertDialogBuilder.setTitle("Party Add Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String partyname, phoneNo, jobDes, addressS;
                                partyname = name.getText().toString();
                                phoneNo = phoneNumber.getText().toString();
                                jobDes = jobDescription.getText().toString();
                                addressS = address.getText().toString();
                                if(TextUtils.isEmpty(partyname) || TextUtils.isEmpty(phoneNo) || TextUtils.isEmpty(addressS) || TextUtils.isEmpty(jobDes)) {
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();
                                }else {
                                    mDatabase = mDatabase.child(partyname);
                                    mDatabase.child("party_name").setValue(partyname);
                                    mDatabase.child("phone_number").setValue(phoneNo);
                                    mDatabase.child("job_description").setValue(jobDes);
                                    mDatabase.child("address").setValue(addressS);

                                    Date date = new Date();
                                    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                                    Log.d(TAG, "Dialogue: "+format.format(date));
                                    mDatabase.child("date").setValue(format.format(date));

                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("accounts_for_party");
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
