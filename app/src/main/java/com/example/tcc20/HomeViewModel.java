package com.example.tcc20;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> headerNome = new MutableLiveData<>();

    public void setHeaderNome(String nome) {
        headerNome.setValue(nome);
    }

    public LiveData<String> getHeaderNome() {
        return headerNome;
    }
}
