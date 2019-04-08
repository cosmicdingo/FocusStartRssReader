package com.example.focusstartrssreader;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private Button rssFeedButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView feedTitleTextView;
    private TextView feedDescriptionTextView;
    private TextView feedLinkTextView;

    private List<RssFeedModel> feedModelList;
    private String feedTitle;
    private String feedDescription;
    private String feedLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.rssFeedEditText);
    }
}
