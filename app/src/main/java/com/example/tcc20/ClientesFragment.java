package com.example.tcc20;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ObjectClasses.BancoDeDados;

public class ClientesFragment extends Fragment {

    HomeActivity context = (HomeActivity) requireContext();

    private BancoDeDados banco = context.banco;

    public ClientesFragment() {
        // Required empty public constructor
    }


    public static ClientesFragment newInstance(String param1, String param2) {
        ClientesFragment fragment = new ClientesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);


        // Inflate the layout for this fragment
        return view;
    }
}