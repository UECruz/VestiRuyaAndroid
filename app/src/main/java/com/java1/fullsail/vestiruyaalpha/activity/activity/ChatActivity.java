package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.ChatAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.ChatModel;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityChatBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends BaseActivity {

    private ActivityChatBinding binding;
    private String customerId;

    ChatAdapter adapter;
    ArrayList<ChatModel> chatList=new ArrayList<>();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=ChatActivity.this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_chat);
        customerId=getIntent().getStringExtra("customerId");
        setUpClicks();
        setAdapter();
        getChatList();
    }

    private void setUpClicks() {
        binding.icBack.setOnClickListener(this);
        binding.buttonChatSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.icBack:

                finish();

                break;

            case R.id.button_chat_send:



                if(binding.editChatMessage.getText().toString()==null || binding.editChatMessage.getText().toString().isEmpty())
                {
                    Toast.makeText(mActivity,"Please enter message",Toast.LENGTH_LONG).show();
                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
                    String currentDateandTime = sdf.format(new Date());

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("message", binding.editChatMessage.getText().toString());
                    result.put("sender", firebaseUser.getUid());
                    result.put("senderName", getProfileData(mActivity).getUsername());
                    result.put("timestamp", currentDateandTime);
                    binding.editChatMessage.setText("");

                    mDatabase.child("Chats").child(firebaseUser.getUid() + "" + customerId).push().setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                            }
                        }
                    });
                }

                break;



        }
    }

    private void getChatList() {
        mDialog.showCustomDalog();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                mDialog.closeDialog();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatModel cm = ds.getValue(ChatModel.class);
                    chatList.add(cm);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.closeDialog();
            }
        };
        mDatabase.child(Constant.CHATS).child(user.getKey() + customerId).addValueEventListener(valueEventListener);
    }



    private void setAdapter() {
        adapter = new ChatAdapter(chatList, user.getKey());
        binding.rvChat.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child(Constant.CHATS).child(user.getKey() + customerId).removeEventListener(valueEventListener);
    }
}
