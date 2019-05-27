package com.example.focusstartrssreader.ui.viewmodel.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SelectedNewsDetailFactory extends ViewModelProvider.NewInstanceFactory {

    private final long id;

    public SelectedNewsDetailFactory(long id) {
        super();
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SelectedNewsDetailViewModel.class) {
            return (T) new SelectedNewsDetailViewModel(id);
        }
        return null;
    }
}
