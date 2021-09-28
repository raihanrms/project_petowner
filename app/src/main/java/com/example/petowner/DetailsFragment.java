package com.example.petowner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    Button hire_sitter;
    private ImageView image_profile;
    String full_name, address, url, phone_no, nid, availability, price;

    public DetailsFragment() {
    }

    public DetailsFragment(String full_name, String address, String url, String phone_no, String nid, String availability, String price) {
        this.full_name = full_name;
        this.address = address;
        this.url = url;
        this.phone_no = phone_no;
        this.nid = nid;
        this.availability = availability;
        this.price = price;
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
        TextView holder_phone_no = v.findViewById(R.id.holder_phone_no);
        TextView holder_nid = v.findViewById(R.id.holder_nid);
        TextView holder_availability = v.findViewById(R.id.holder_availability);
        TextView holder_price = v.findViewById(R.id.holder_price);
        Button hire_sitter = v.findViewById(R.id.hire_sitter);

        hire_sitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO_EMAILS = {"jimarrafi10@gmail.com"};


                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);


                intent.putExtra(Intent.EXTRA_SUBJECT, "PetSitter App");
                intent.putExtra(Intent.EXTRA_TEXT, "You got hired");

                startActivity(Intent.createChooser(intent, "Choose your email client"));
            }
        });

        Picasso.get().load(url).into(holder_profile);

        holder_name.setText(full_name);
        holder_address.setText(address);
        holder_phone_no.setText(phone_no);
        holder_nid.setText(nid);
        holder_availability.setText(availability);
        holder_price.setText(price);

        return v;

    }

    public void onBackPressed(){
        AppCompatActivity appCompatActivity = (AppCompatActivity)getContext();
        // fragment_search_view id was bar
        appCompatActivity.getSupportFragmentManager().beginTransaction().
                replace(R.id.bar, new ProfileFragment()).addToBackStack(null).commit();
    }
}