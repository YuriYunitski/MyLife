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
import com.yunitski.organizer.mylife.itemClasses.EveningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EveningItemAdapter extends RecyclerView.Adapter<EveningItemAdapter.ViewHolder> {

    private List<EveningItem> eveningItemList;

    private OnEveningItemClickListener listener;

    public EveningItemAdapter(ArrayList<EveningItem> eveningItemList) {
        this.eveningItemList = eveningItemList;
    }

    public interface OnEveningItemClickListener{
        void onEveningItemClick(int position);
    }

    public void setOnEveningClickListener(OnEveningItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evening_item_card_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        EveningItem eveningItem = eveningItemList.get(position);
        if (eveningItem.getEveningItemStatus().equals("wait")){
            holder.itemText.setText(eveningItem.getEveningItemText());
            holder.itemTime.setText(eveningItem.getEveningItemTime());
        } else {
            holder.itemText.setText(eveningItem.getEveningItemText());
            holder.itemTime.setText(eveningItem.getEveningItemTime());
            holder.eveningItemMoreButton.setVisibility(View.GONE);
            holder.eveningItemCardView.setCardBackgroundColor(Color.parseColor("#8BCA8E"));
        }
    }

    @Override
    public int getItemCount() {
        return eveningItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemText;

        private final TextView itemTime;

        private final ImageButton eveningItemMoreButton;

        private final CardView eveningItemCardView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.eveningItemTextView);

            itemTime = itemView.findViewById(R.id.eveningItemTimeTextView);

            eveningItemMoreButton = itemView.findViewById(R.id.eveningItemMoreButton);

            eveningItemCardView = itemView.findViewById(R.id.eveningItemCardView);

            eveningItemMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEveningItemClick(position);
                        }
                    }
                }
            });
        }
    }
}