package com.example.tcc20;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ObjectClasses.BancoDeDados;

public class MetasFragment extends Fragment {

    HomeActivity context = (HomeActivity) requireContext();

    private BancoDeDados banco = context.banco;

    public MetasFragment() {
        // Required empty public constructor
    }

    public static MetasFragment newInstance(String param1, String param2) {
        MetasFragment fragment = new MetasFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metas, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}