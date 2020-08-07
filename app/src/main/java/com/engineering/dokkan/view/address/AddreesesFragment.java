package com.engineering.dokkan.view.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.engineering.dokkan.R;
import com.engineering.dokkan.data.models.OrderItemModel;
import com.engineering.dokkan.data.models.viewAddressModel;
import com.engineering.dokkan.view.orders.OrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddreesesFragment extends Fragment implements View.OnClickListener {


    private ImageView arrowBack;
    private RecyclerView addressRecycle;
    private Button addAddressBtn;
    private AddressAdapter adapter ;
    private DatabaseReference databaseReference;
    private List<viewAddressModel> addressList;



    public AddreesesFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_addreeses, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView(view);
        initRecView();
        fetchAddresses();

        return view;
    }

    private void fetchAddresses() {
        databaseReference.child("addresses").child("user2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addressList = new ArrayList<viewAddressModel>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    viewAddressModel addressModel = dataSnapshot1.getValue(viewAddressModel.class);
                    addressList.add(addressModel);
                }
                adapter.changeData(addressList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView( View itemView) {
        arrowBack = itemView.findViewById(R.id.arrow_back);
        addressRecycle = itemView.findViewById(R.id.recyclerview_id_address);
        addAddressBtn =  itemView.findViewById(R.id.plus_button);
       addAddressBtn.setOnClickListener(this);
    }

    private void initRecView() {
        adapter = new AddressAdapter();
        addressRecycle.setAdapter(adapter);

    }


    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.plus_button:
               getNavController().navigate(R.id.action_addressesfragment_to_addAddressFragment);
                break;
            default:
                break;
        }
    }
}