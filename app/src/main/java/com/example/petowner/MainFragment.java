package com.example.petowner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment  extends Fragment {

    private OnFragmentItemSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container,false);

        //Button btnlogout = view.findViewById(R.id.btnlogout);
        Button verifyEmailbtn = view.findViewById(R.id.verifyEmailbtn);
        TextView verifyEmailMsg = view.findViewById(R.id.verifyEmailMsg);


       verifyEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEmailverifyBtnSelected();
            }
        });
        verifyEmailMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEmailverifyMsgSelected();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;
        }
        else{
            throw  new ClassCastException(context.toString()+ "must implement listener");
        }
    }

    public interface OnFragmentItemSelectedListener{
         void onEmailverifyBtnSelected();
         void onEmailverifyMsgSelected();
    }

    

}
