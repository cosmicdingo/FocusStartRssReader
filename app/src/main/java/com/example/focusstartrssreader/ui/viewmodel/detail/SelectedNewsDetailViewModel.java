package com.example.focusstartrssreader.ui.viewmodel.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.SelectedNews;

public class SelectedNewsDetailViewModel extends ViewModel {

    private final long id;

    public SelectedNewsDetailViewModel(long id) {
        this.id = id;
    }

    public LiveData<SelectedNews> getSelectedNewsLiveData() {
        return RssFeedApp.getInstance().getFeedRepository().getSelectedNews(id);
    }
}
