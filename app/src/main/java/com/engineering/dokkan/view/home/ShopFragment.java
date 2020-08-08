package com.engineering.dokkan.view.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.engineering.dokkan.R;
import com.engineering.dokkan.data.models.ProductitemModel;
import com.engineering.dokkan.data.models.ShopitemModel;
import com.engineering.dokkan.view.Favourite.ShopRecycAdaptar;
import com.engineering.dokkan.view.base.BaseFragment;
import com.engineering.dokkan.view.shop.ShopPageFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {
    RecyclerView recyclerView;
    ArrayList<ShopitemModel> data;
    private DatabaseReference dbReference;
    Bundle bundle1 ;
    MainViewModel mainViewModel;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initializeViews(View view) {
        showShops(view);
    }

    @Override
    public void setListeners() {
    }


    private void showShops(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerview_id_shop);
        data = new ArrayList<>();
        bundle1 = getArguments();
        String idshop = bundle1.getString(Constants.CATEGORY_KEY);
        Log.d("ID_SHOP", "CAT_ID: "+idshop);
        initViewModel();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getShopByCategory(idshop);
    }

    private void getShopByCategory(String catId) {
        dbReference = FirebaseDatabase.getInstance().getReference("shops");
        Query query = FirebaseDatabase.getInstance().getReference("shops")
                .orderByChild("categoryid").equalTo(catId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ShopitemModel shops = snapshot.getValue(ShopitemModel.class);
                    data.add(shops);
                }
                ShopRecyclerAdaptar adapter = new ShopRecyclerAdaptar(getContext()
                        ,data , ListenerShops);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity() , databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    void initViewModel() {
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        mainViewModel.catId.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String newCatID) {
                getShopByCategory(newCatID);
            }
        });
    }

    NavController getNavController(){
        return Navigation.findNavController(getActivity() ,R.id.my_nav_host);
    }


    ShopRecyclerAdaptar.ItemClickListener ListenerShops = new ShopRecyclerAdaptar.ItemClickListener() {
        @Override
        public void onItemClick(ShopitemModel item) {
//            ShopPageFragment shopPageFragment = new ShopPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.SHOP_KEY , item.getKey());
            getNavController().navigate(R.id.action_homeFragment2_to_shopPageFragment , bundle);

            Toast.makeText(getActivity(), "item Clicked", Toast.LENGTH_SHORT).show();


        }
    };




}
