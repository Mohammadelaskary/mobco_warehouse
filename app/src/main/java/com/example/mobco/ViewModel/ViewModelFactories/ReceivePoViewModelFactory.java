package com.example.mobco.ViewModel.ViewModelFactories;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobco.ViewModel.ReceivePOViewModel;

public class ReceivePoViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private Activity activity;


    public ReceivePoViewModelFactory(Application application, Activity activity) {
        this.application = application;
        this.activity = activity;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ReceivePOViewModel(application,activity);
    }
}