package com.yunitski.organizer.mylife.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.itemClasses.DayItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DayItemAdapter extends RecyclerView.Adapter<DayItemAdapter.ViewHolder> {

    private List<DayItem> dayItemList;

    private OnDayItemClickListener listener;

    public DayItemAdapter(ArrayList<DayItem> dayItemList) {
        this.dayItemList = dayItemList;
    }

    public interface OnDayItemClickListener{
        void onDayItemClick(int position);
    }

    public void setOnDayClickListener(OnDayItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_card_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        DayItem dayItem = dayItemList.get(position);
        if (dayItem.getDayItemStatus().equals("wait")){
            holder.itemText.setText(dayItem.getDayItemText());
            holder.itemTime.setText(dayItem.getDayItemTime());
        } else {
            holder.itemText.setText(dayItem.getDayItemText());
            holder.itemTime.setText(dayItem.getDayItemTime());
            holder.dayItemMoreButton.setVisibility(View.GONE);
            holder.dayItemCardView.setCardBackgroundColor(Color.parseColor("#8BCA8E"));
        }
    }

    @Override
    public int getItemCount() {
        return dayItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemText;

        private final TextView itemTime;

        private final ImageButton dayItemMoreButton;

        private final CardView dayItemCardView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.dayItemTextView);

            itemTime = itemView.findViewById(R.id.dayItemTimeTextView);

            dayItemMoreButton = itemView.findViewById(R.id.dayItemMoreButton);

            dayItemCardView = itemView.findViewById(R.id.dayItemCardView);

            dayItemMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDayItemClick(position);
                        }
                    }
                }
            });
        }
    }
}