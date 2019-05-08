package com.example.focusstartrssreader;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

// Компонент ViewHolder указывает, какие view
// должны использоваться для каждого элемента данных
public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.ViewHolder> {

    private List<RssFeedModel> rssFeedModels;

    public RssFeedAdapter(List<RssFeedModel> rssFeedModels) {
        this.rssFeedModels = rssFeedModels;
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


    // Метод onCreateViewHolder() вызывается, когда RecyclerView потребуется
    // новый экземпляр ViewHolder. RecyclerView многократно
    // вызывает метод при исходном создании RecyclerView для построения
    // набора объектов ViewHolder, которые будут выводиться на экране
    @NonNull
    @Override
    public RssFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // LayoutInflator преобразует макет в CardView
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RssFeedAdapter.ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        TextView tvTitle = (TextView) cardView.findViewById(R.id.tvTitle);
        TextView tvPubDate = (TextView) cardView.findViewById(R.id.tvPubDate);

        tvTitle.setText(rssFeedModels.get(i).getTitle());
        tvPubDate.setText(rssFeedModels.get(i).getPubDate());
    }

    @Override
    public int getItemCount() {
        return rssFeedModels.size();
    }
}
