package com.java1.fullsail.vestiruyaalpha.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.activity.CustomizeActivity;
import com.java1.fullsail.vestiruyaalpha.activity.activity.SummaryActivity;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.CustomizeAdapter;
import com.java1.fullsail.vestiruyaalpha.databinding.FragmentCustomizeBinding;
import com.java1.fullsail.vestiruyaalpha.activity.model.Customize;

import java.util.ArrayList;

import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Back_Details;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Embellishment;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Fabrics;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.MATERIAL;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Neckline;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Silhouette;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Sleeves;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.Straps;

public class CustomizeFragment extends BaseFragment {
    private int pos = 0;
    FragmentCustomizeBinding binding;
    ArrayList<Customize> list = new ArrayList<>();
    private CustomizeAdapter adapter;
    private boolean isEdit;
    private String orderId;

    public static CustomizeFragment newInstance(int pos, Bundle bundle) {
        bundle.putInt("position", pos);
        CustomizeFragment fg = new CustomizeFragment();
        fg.setArguments(bundle);
        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt("position");
            isEdit = getArguments().getBoolean("isEdit", false);
            if (isEdit) {
                orderId = getArguments().getString("orderId");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomizeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    private String getTitle() {
        String title = "";
        switch (pos) {
            case 0:
                title = "Select Your Dress Type";
                break;
            case 1:
                title = "Select Your Fabric";
                break;
            case 2:
                title = "Select Your Neckline Type";
                break;
            case 3:
                title = "Select Your Sleeves";
                break;
            case 4:
                title = "Select Your Straps";
                break;
            case 5:
                title = "Select Your Back Details Type";
                break;
            case 6:
                title = "Select Your Embellishment Type";
                break;
        }
        return title;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomizeActivity) getActivity()).binding.tvTitle.setText(getTitle());
        if (pos == 0) {
            ((CustomizeActivity) getActivity()).binding.tvBack.setVisibility(View.VISIBLE);
        } else {
            ((CustomizeActivity) getActivity()).binding.tvBack.setVisibility(View.GONE);
        }
        setListeners();
        setAdapter();
        fetchData();
    }

    private void fetchData() {
        list.clear();
        DatabaseReference ref = null;
        if (pos == 0) {
            ref = mDatabase.child(MATERIAL).child(Silhouette).getRef();
        } else if (pos == 1) {
            ref = mDatabase.child(MATERIAL).child(Fabrics).getRef();
        } else if (pos == 2) {
            ref = mDatabase.child(MATERIAL).child(Neckline).getRef();
        } else if (pos == 3) {
            ref = mDatabase.child(MATERIAL).child(Sleeves).getRef();
        } else if (pos == 4) {
            ref = mDatabase.child(MATERIAL).child(Straps).getRef();
        } else if (pos == 5) {
            ref = mDatabase.child(MATERIAL).child(Back_Details).getRef();
        } else if (pos == 6) {
            ref = mDatabase.child(MATERIAL).child(Embellishment).getRef();
        }


        if (ref != null) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataChild : dataSnapshot.getChildren()) {
                        if (dataChild.getKey().equals("Lace")){
//                           if( dataChild.getValue().equals("Appligue")){
                            for (DataSnapshot childChild : dataChild.getChildren()) {
                                if (childChild.getValue() instanceof ArrayList) {

                                    for (DataSnapshot childChild1 : childChild.getChildren()) {
                                        Customize customize = childChild1.getValue(Customize.class);
                                        Log.d("name",customize.getName());
                                        list.add(customize);
                                    }
                                }
                            }
                            //}
//                            if( dataChild.getValue().equals("Plain")){
//                                for (DataSnapshot childChild : dataChild.getChildren()) {
//                                    if (childChild.getValue() instanceof ArrayList) {
//
//                                        for (DataSnapshot childChild1 : childChild.getChildren()) {
//                                            Customize customize = childChild1.getValue(Customize.class);
//                                            Log.d("name1",customize.getName());
//                                            list.add(customize);
//                                        }
//                                    }
//                                }
//                            }
//                            if( dataChild.getValue().equals("Trim")){
//                                for (DataSnapshot childChild : dataChild.getChildren()) {
//                                    if (childChild.getValue() instanceof ArrayList) {
//
//                                        for (DataSnapshot childChild1 : childChild.getChildren()) {
//                                            Customize customize = childChild1.getValue(Customize.class);
//                                            Log.d("name2",customize.getName());
//                                            list.add(customize);
//                                        }
//                                    }
//                                }
//                            }

                        }
                        if (dataChild.getValue() instanceof ArrayList) {

                            for (DataSnapshot childChild : dataChild.getChildren()) {
                                Customize customize = childChild.getValue(Customize.class);
                                Log.d("name4",customize.getName());
                                list.add(customize);
                            }
                        } else {
                            Customize customize = dataChild.getValue(Customize.class);
                            //    Log.d("name5",customize.getName());
                            list.add(customize);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mActivity, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setAdapter() {
        adapter = new CustomizeAdapter(list);
        binding.rvCategories.setLayoutManager(new GridLayoutManager(mActivity, 2));
        binding.rvCategories.setAdapter(adapter);
    }

    private void setListeners() {
        binding.btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (adapter.selectedIndex > -1) {
                    if (pos == 6) {
                        ((CustomizeActivity) mActivity).summeryMap.put("embellishment", list);
                        //goto summary screen
                        Intent mIntent = new Intent(mActivity, SummaryActivity.class);
                        if (isEdit) {
                            mIntent.putExtra("isEdit", true);
                            mIntent.putExtra("orderId", orderId);
                        }
//                        Gson gson = new Gson();
//                        String list = gson.toJson(((CustomizeActivity) mActivity).summeryMap);
                        mIntent.putExtra("data", ((CustomizeActivity) mActivity).summeryMap);
                        startActivity(mIntent);
                        mActivity.finish();
                    } else {
                        if (pos == 0) {
                            ((CustomizeActivity) mActivity).summeryMap.put("bodytype", list);
                            //  Log.e("message",((CustomizeActivity) mActivity).summeryMap.get(1).get(0).getName());
                        } else if (pos == 1) {
                            ((CustomizeActivity) mActivity).summeryMap.put("fabrics", list);
                            Log.e("message",((CustomizeActivity) mActivity).summeryMap.get("fabrics").get(0).getName());
                        } else if (pos == 2) {
                            ((CustomizeActivity) mActivity).summeryMap.put("neckline", list);
                        } else if (pos == 3) {
                            ((CustomizeActivity) mActivity).summeryMap.put("sleeves", list);
                        } else if (pos == 4) {
                            ((CustomizeActivity) mActivity).summeryMap.put("straps", list);
                        } else if (pos == 5) {
                            ((CustomizeActivity) mActivity).summeryMap.put("backDetails", list);
                        }
                        Bundle b = new Bundle();
                        if (isEdit) {
                            b.putBoolean("isEdit", true);
                            b.putString("orderId", orderId);
                        }
                        ((CustomizeActivity) getActivity()).gotoFragment(CustomizeFragment.newInstance(pos + 1, b));
                    }
                } else {
                    Toast.makeText(mActivity, "Please select on card to process", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
