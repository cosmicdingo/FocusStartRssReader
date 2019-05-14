package com.example.focusstartrssreader.service;

import android.content.Context;

// используется для уведомления AddNewFeedActivity о том,
// что сервис закончил работу и готов отравить результаты в вызывающую активити
public interface FetchFeedInterface {

    Context getContext();
    void onFinished(Object object);
}
