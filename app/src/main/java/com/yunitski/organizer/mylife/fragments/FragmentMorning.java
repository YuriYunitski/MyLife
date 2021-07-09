package com.yunitski.organizer.mylife.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.activities.MainActivity;
import com.yunitski.organizer.mylife.adapters.MorningItemAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;
import com.yunitski.organizer.mylife.itemClasses.MorningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentMorning extends Fragment {

    RecyclerView recyclerView;
    MorningItemAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_morning_layout, container, false);
        recyclerView = view.findViewById(R.id.morningRecyclerView);
        updateUI();
        ((MainActivity) requireActivity()).setFragmentRefreshListener(this::updateUI);
        return view;
    }

    private void updateUI(){
        ArrayList<MorningItem> morningItems = new ArrayList<>();

        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + InputData.TaskEntry.MORNING_TABLE + ";", null);
        while (cursor.moveToNext()){
            int textIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK);
            int timeIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_TIME);

            morningItems.add(new MorningItem(cursor.getString(textIndex), cursor.getString(timeIndex)));
        }
        db.close();
        cursor.close();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MorningItemAdapter(morningItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnMorningClickListener(new MorningItemAdapter.OnMorningItemClickListener() {
            @Override
            public void onMorningItemClick(int position) {
                showBottomSheetDialog(morningItems.get(position).getMorningItemText());
            }
        });

    }

    private void showBottomSheetDialog(String name) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        LinearLayout done = bottomSheetDialog.findViewById(R.id.doneLL);
        LinearLayout edit = bottomSheetDialog.findViewById(R.id.editLL);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.deleteLL);
        TextView itemNameTextView = bottomSheetDialog.findViewById(R.id.itemNameTextView);
        assert itemNameTextView != null;
        itemNameTextView.setText(name);

        bottomSheetDialog.show();
    }

}
