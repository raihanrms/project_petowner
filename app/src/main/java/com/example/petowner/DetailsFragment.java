package com.example.petowner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    private ImageView image_profile;
    String full_name, address, url;
    public DetailsFragment() {
    }

    public DetailsFragment(String full_name, String address, String url) {
        this.full_name = full_name;
        this.address = address;
        this.url = url;
    }

    public DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView holder_profile = v.findViewById(R.id.holder_profile);
        TextView holder_name = v.findViewById(R.id.holder_name);
        TextView holder_address = v.findViewById(R.id.holder_address);


        holder_name.setText(full_name);
        holder_address.setText(address);
        Picasso.get().load(url).into(holder_profile);

        return v;
    }
    public void onBackPressed(){
        AppCompatActivity appCompatActivity = (AppCompatActivity)getContext();
        // fragment_search_view id was bar
        appCompatActivity.getSupportFragmentManager().beginTransaction().
                replace(R.id.bar, new ProfileFragment()).addToBackStack(null).commit();
    }
}