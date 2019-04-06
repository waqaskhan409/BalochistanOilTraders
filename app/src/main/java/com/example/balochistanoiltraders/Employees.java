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
import android.support.v7.widget.Toolbar;
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
 * {@link Employees.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Employees#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Employees extends Fragment {
    private static final String TAG = "Employees";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Employees() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Employees.
     */


    // TODO: Rename and change types and number of parameters
    public static Employees newInstance(String param1, String param2) {
        Employees fragment = new Employees();
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
        View view = inflater.inflate(R.layout.fragment_employees, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Employees");
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("employees");
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view. findViewById(R.id.employeeList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);


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
        FirebaseRecyclerOptions<Note> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(mDatabase, Note.class).build();
        employeeAdapter = new EmployeeAdapter(firebaseRecyclerOptions);
        recyclerView.setAdapter(employeeAdapter);
    }

    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.emplyee_form, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputEditText name = (TextInputEditText) view
                .findViewById(R.id.employeeNameForEmployee);
        final TextInputEditText phoneNumber = (TextInputEditText) view
                .findViewById(R.id.employeePhoneNumberForEmployee);
        final TextInputEditText jobDescription = (TextInputEditText) view
                .findViewById(R.id.jobDescriptionForEmployee);
        final EditText address = (EditText) view
                .findViewById(R.id.addressForEmployee);
        // set dialog message
        alertDialogBuilder.setTitle("Employee Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String employeeName, phoneNo, salary, addressS;
                                employeeName = name.getText().toString();
                                phoneNo = phoneNumber.getText().toString();
                                salary = jobDescription.getText().toString();
                                addressS = address.getText().toString();
                                if(TextUtils.isEmpty(employeeName) || TextUtils.isEmpty(phoneNo) || TextUtils.isEmpty(addressS) || TextUtils.isEmpty(salary)){
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();

                                }else {
                                    mDatabase = mDatabase.child(employeeName);
                                    mDatabase.child("employee_name").setValue(employeeName);
                                    mDatabase.child("phone_number").setValue(phoneNo);
                                    mDatabase.child("salary").setValue(salary);
                                    mDatabase.child("address").setValue(addressS);

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

    @Override
    public void onStop() {
        super.onStop();
        employeeAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        employeeAdapter.startListening();
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
