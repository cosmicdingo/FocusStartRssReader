package com.example.focusstartrssreader.service.listener;

import android.content.Context;

import com.example.focusstartrssreader.domain.model.RssFeedModel;

// используется для уведомления AddNewFeedActivity о том,
// что сервис закончил работу и готов отравить результаты в вызывающую активити
public interface OnFinishListener {
    void onFinished(Object object);
}
