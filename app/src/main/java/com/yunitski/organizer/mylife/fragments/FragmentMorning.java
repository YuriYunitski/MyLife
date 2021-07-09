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

public class FragmentMorning extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    MorningItemAdapter adapter;
    ArrayList<MorningItem> morningItems;

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
        morningItems = new ArrayList<>();

        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + InputData.TaskEntry.MORNING_TABLE + ";", null);
        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_ID);
            int textIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK);
            int timeIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_TIME);
            int statusIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_STATUS);

            morningItems.add(new MorningItem(cursor.getString(idIndex), cursor.getString(textIndex), cursor.getString(timeIndex), cursor.getString(statusIndex)));
        }
        db.close();
        cursor.close();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MorningItemAdapter(morningItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnMorningClickListener(position -> showBottomSheetDialog(morningItems.get(position).getMorningItemText(), position));

    }

    private void showBottomSheetDialog(String name, int position) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        LinearLayout done = bottomSheetDialog.findViewById(R.id.doneLL);
        LinearLayout edit = bottomSheetDialog.findViewById(R.id.editLL);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.deleteLL);
        assert done != null;
        done.setOnClickListener(v -> {
            completeTask(position);
            bottomSheetDialog.dismiss();
            updateUI();
        });
        assert edit != null;
        edit.setOnClickListener(v -> {
            editTask(position);
            bottomSheetDialog.dismiss();
            updateUI();
        });
        assert delete != null;
        delete.setOnClickListener(v -> {
            deleteTask(position);
            bottomSheetDialog.dismiss();
            updateUI();
        });
        TextView itemNameTextView = bottomSheetDialog.findViewById(R.id.itemNameTextView);
        assert itemNameTextView != null;
        itemNameTextView.setText(name);

        bottomSheetDialog.show();
    }

    private void completeTask(int position) {
        String id = morningItems.get(position).getMorningItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String strSQL = "UPDATE " +
                InputData.TaskEntry.MORNING_TABLE +
                " SET " +
                InputData.TaskEntry.COLUMN_MORNING_TASK_STATUS +
                " = 'done' WHERE " +
                InputData.TaskEntry.COLUMN_ID +
                " = " +
                id +
                ";";
        db.execSQL(strSQL);
        db.close();
    }

    private void editTask(int position) {
    }

    private void deleteTask(int position) {
        String id = morningItems.get(position).getMorningItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(InputData.TaskEntry.MORNING_TABLE, InputData.TaskEntry.COLUMN_ID + "=?", new String[]{id});
        db.close();
    }



    @Override
    public void onClick(View v) {

    }
}
