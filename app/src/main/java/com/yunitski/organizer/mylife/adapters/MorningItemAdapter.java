package com.yunitski.organizer.mylife.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.itemClasses.MorningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MorningItemAdapter extends RecyclerView.Adapter<MorningItemAdapter.ViewHolder> {

    private final List<MorningItem> morningItemList;

    private List<MorningItem> selectedList = new ArrayList<>();

    private OnMorningItemClickListener listener;

    private boolean multiSelect = false;

    private final ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    public MorningItemAdapter(ArrayList<MorningItem> morningItemList) {
        this.morningItemList = morningItemList;
    }

    public interface OnMorningItemClickListener{
        void onMorningItemClick(int position);
    }

    public void setOnMorningClickListener(OnMorningItemClickListener listener){
        this.listener = listener;
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
        if (morningItem.getMorningItemStatus().equals("wait")){
            holder.itemText.setText(morningItem.getMorningItemText());
            holder.itemTime.setText(morningItem.getMorningItemTime());
        } else {

            holder.itemText.setText(morningItem.getMorningItemText());
            holder.itemTime.setText(morningItem.getMorningItemTime());
            holder.morningItemMoreButton.setVisibility(View.GONE);
            holder.morningItemCardView.setCardBackgroundColor(Color.parseColor("#8BCA8E"));
        }
    }

    @Override
    public int getItemCount() {
        return morningItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemText;

        private final TextView itemTime;

        private final ImageButton morningItemMoreButton;

        private final LinearLayout morningItemLinearLayout;

        private final CardView morningItemCardView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.morningItemTextView);

            itemTime = itemView.findViewById(R.id.morningItemTimeTextView);

            morningItemMoreButton = itemView.findViewById(R.id.morningItemMoreButton);

            morningItemCardView = itemView.findViewById(R.id.morningItemCardView);

            morningItemLinearLayout = itemView.findViewById(R.id.morningItemLinearLayout);

            morningItemMoreButton.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onMorningItemClick(position);
                    }
                }
            });


        }

        private void selectItem(MorningItem morningItem){
            if (multiSelect){
                if (selectedList.contains(morningItem)){
                    selectedList.remove(morningItem);
                    if (morningItem.getMorningItemStatus().equals("wait")){
                        morningItemCardView.setCardBackgroundColor(Color.parseColor("#EDFFFD"));
                    } else {
                        morningItemCardView.setCardBackgroundColor(Color.parseColor("#8BCA8E"));
                    }

                } else {
                    selectedList.add(morningItem);
                    morningItemCardView.setCardBackgroundColor(Color.LTGRAY);
                }
            }
        }

        void update(MorningItem morningItem){
            if (morningItem.getMorningItemStatus().equals("wait")){
                itemText.setText(morningItem.getMorningItemText());
                itemTime.setText(morningItem.getMorningItemTime());
            } else {
                itemText.setText(morningItem.getMorningItemText());
                itemTime.setText(morningItem.getMorningItemTime());
                morningItemMoreButton.setVisibility(View.GONE);
                morningItemCardView.setCardBackgroundColor(Color.parseColor("#8BCA8E"));
            }
        }
    }
}
