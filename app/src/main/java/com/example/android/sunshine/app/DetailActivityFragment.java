package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent=getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
            ((TextView)root.findViewById(R.id.txt_weather)).setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            DetailActivity.weather=intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        return root;
    }
}
