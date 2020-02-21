package com.example.liuyx.hw9_3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static com.example.liuyx.hw9_3.R.layout.fragment_home;

/**
 * Created by Liuyx on 4/19/17.
 */

public class FragmentHome extends Fragment {

    EditText editText;
    Button clearButton;
    Button searchButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(fragment_home, container, false);
        editText = (EditText)homeView.findViewById(R.id.editText);
        clearButton = (Button)homeView.findViewById(R.id.clearButton);
        searchButton = (Button)homeView.findViewById(R.id.searchButton);
        return homeView;
    }
}
