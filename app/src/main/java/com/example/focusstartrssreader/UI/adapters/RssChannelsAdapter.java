package com.example.focusstartrssreader.UI.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.UI.activities.MainActivity;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.ArrayList;
import java.util.List;

public class RssChannelsAdapter extends RecyclerView.Adapter<RssChannelsAdapter.ViewHolder> {

    private List<Channel> channels;

    public RssChannelsAdapter() {

        this.channels = new ArrayList<>();
    }

    // Метод onCreateViewHolder() вызывается, когда RecyclerView потребуется
    // новый экземпляр ViewHolder. RecyclerView многократно
    // вызывает метод при исходном создании RecyclerView для построения
    // набора объектов ViewHolder, которые будут выводиться на экране
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Получает объ ект LayoutInflater, который реобразует макет в CardView
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.channels_cardview_item, viewGroup,false);
        return new ViewHolder(cardView);
    }

    // Добавление данных в карточки осуществляется реализацией метода onBindViewHolder() адаптера
    // Метод onBindViewHolder() вызывается каждый раз, когда компоненту RecyclerView потребуется вывести
    // данные во ViewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final CardView cardView = viewHolder.cardView;
        TextView tvTitleChannel = (TextView) cardView.findViewById(R.id.tvChannelTitle);
        final String titleChannel = channels.get(i).getChannelTitle();
        tvTitleChannel.setText(titleChannel);

        /*TextView tvChannelLink = (TextView) cardView.findViewById(R.id.tvChannelLink);
        String channelLink = channels.get(i).getChannelLink();
        tvChannelLink.setText(channelLink);*/

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), MainActivity.class);
                intent.putExtra(MainActivity.CHANNEL_TITLE, titleChannel);
                cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public void addItems(List<Channel> newChannels) {
        this.channels = newChannels;
        notifyDataSetChanged();
    }

    public Channel getItem(int position) {
        return channels.get(position);
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


}
