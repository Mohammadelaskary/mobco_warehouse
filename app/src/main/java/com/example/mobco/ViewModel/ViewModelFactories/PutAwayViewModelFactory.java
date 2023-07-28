package com.example.mobco.ViewModel.ViewModelFactories;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobco.ViewModel.InspectionViewModel;
import com.example.mobco.ViewModel.PutAwayViewModel;

public class PutAwayViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private Activity activity;


    public PutAwayViewModelFactory(Application application, Activity activity) {
        this.application = application;
        this.activity = activity;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PutAwayViewModel(application,activity);
    }
}