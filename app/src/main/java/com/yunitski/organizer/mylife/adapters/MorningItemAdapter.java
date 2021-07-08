package com.yunitski.organizer.mylife.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.itemClasses.MorningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MorningItemAdapter extends RecyclerView.Adapter<MorningItemAdapter.ViewHolder> {

    private List<MorningItem> morningItemList;

    public MorningItemAdapter(ArrayList<MorningItem> morningItemList) {
        this.morningItemList = morningItemList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.morning_item_card_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MorningItemAdapter.ViewHolder holder, int position) {
        MorningItem morningItem = morningItemList.get(position);
        holder.itemText.setText(morningItem.getMorningItemText());
    }

    @Override
    public int getItemCount() {
        return morningItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemText;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.morningItemTextView);
        }
    }
}
