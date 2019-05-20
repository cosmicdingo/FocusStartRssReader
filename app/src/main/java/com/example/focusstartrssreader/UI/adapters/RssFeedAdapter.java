package com.example.focusstartrssreader.UI.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.focusstartrssreader.DateConverter;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.ArrayList;
import java.util.List;

// Компонент ViewHolder указывает, какие view
// должны использоваться для каждого элемента данных
public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.ViewHolder> {

    private List<RssFeedModel> rssFeedModels;

    private Listener listener;

    public RssFeedAdapter() {
        this.rssFeedModels = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // указываем, что ViewHolder содержит CardView, которые
        // должны отображаться в RecyclerView
        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }


    // Активность использует этот метод для регистрации
    // себя в качестве слушателя
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // Метод onCreateViewHolder() вызывается, когда RecyclerView потребуется
    // новый экземпляр ViewHolder. RecyclerView многократно
    // вызывает метод при исходном создании RecyclerView для построения
    // набора объектов ViewHolder, которые будут выводиться на экране
    @NonNull
    @Override
    public RssFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // LayoutInflater преобразует макет в CardView
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RssFeedAdapter.ViewHolder viewHolder, final int i) {
        final CardView cardView = viewHolder.cardView;

        TextView tvTitle = (TextView) cardView.findViewById(R.id.tvTitle);
        final String title = rssFeedModels.get(i).getTitle();
        tvTitle.setText(title);

        TextView tvPubDate = (TextView) cardView.findViewById(R.id.tvPubDate);
        final String pubDate = DateConverter.timeToDate(rssFeedModels.get(i).getMillis());
        tvPubDate.setText(pubDate);

        final long newsId = rssFeedModels.get(i).getId();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(newsId);
                }
            }
        });
    }

    public void setRssFeedModels(List<RssFeedModel> newRssFeedModels) {
        this.rssFeedModels = newRssFeedModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return rssFeedModels.size();
    }
}
