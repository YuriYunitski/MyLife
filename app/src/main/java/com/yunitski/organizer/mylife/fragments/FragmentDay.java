package com.yunitski.organizer.mylife.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.activities.MainActivity;
import com.yunitski.organizer.mylife.adapters.DayItemAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;
import com.yunitski.organizer.mylife.itemClasses.DayItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentDay extends Fragment {

    RecyclerView recyclerView;
    DayItemAdapter adapter;
    ArrayList<DayItem> dayItems, completedDayItems, totalDayItems;
    String time, dd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_layout, container, false);
        recyclerView = view.findViewById(R.id.dayRecyclerView);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        dd = activity.getMyData();
        updateUI();
        ((MainActivity) requireActivity()).setFragmentRefreshListener1(this::updateUI);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUI() {

        dayItems = new ArrayList<>();
        completedDayItems = new ArrayList<>();
        totalDayItems = new ArrayList<>();
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        dd = activity.getMyData();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + InputData.TaskEntry.DAY_TABLE + " WHERE " + InputData.TaskEntry.COLUMN_DAY_TASK_DATE + " = '" + dd + "';", null);
        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_ID);
            int textIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_DAY_TASK);
            int timeIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_DAY_TASK_TIME);
            int statusIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_DAY_TASK_STATUS);
            int dateIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_DAY_TASK_DATE);

            if (cursor.getString(statusIndex).equals("wait") && cursor.getString(dateIndex).equals(dd)){
                dayItems.add(new DayItem(cursor.getString(idIndex), cursor.getString(textIndex), cursor.getString(timeIndex), cursor.getString(statusIndex), cursor.getString(dateIndex)));
            } else if (cursor.getString(statusIndex).equals("done") && cursor.getString(dateIndex).equals(dd)){
                completedDayItems.add(new DayItem(cursor.getString(idIndex), cursor.getString(textIndex), cursor.getString(timeIndex), cursor.getString(statusIndex), cursor.getString(dateIndex)));
            }

        }
        db.close();
        cursor.close();

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        totalDayItems.addAll(dayItems);
        totalDayItems.addAll(completedDayItems);
        adapter = new DayItemAdapter(totalDayItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnDayClickListener(position -> showBottomSheetDialog(dayItems.get(position).getDayItemText(), position));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        String id = dayItems.get(position).getDayItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String strSQL = "UPDATE " +
                InputData.TaskEntry.DAY_TABLE +
                " SET " +
                InputData.TaskEntry.COLUMN_DAY_TASK_STATUS +
                " = 'done' WHERE " +
                InputData.TaskEntry.COLUMN_ID +
                " = " +
                id +
                ";";
        db.execSQL(strSQL);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editTask(int position){
        String id = dayItems.get(position).getDayItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.task_picker_dialog, null);
        EditText taskEditText = view.findViewById(R.id.taskEditText);
        TimePicker taskTimePicker = view.findViewById(R.id.taskTimePicker);
        taskTimePicker.setIs24HourView(true);
        taskEditText.setText(dayItems.get(position).getDayItemText());
        String t = dayItems.get(position).getDayItemTime();
        String[] s = t.split(":");
        String h = s[0];
        String m = s[1];
        taskTimePicker.setHour(Integer.parseInt(h));
        taskTimePicker.setMinute(Integer.parseInt(m));
        time = taskTimePicker.getHour() + ":" + taskTimePicker.getMinute();
        taskTimePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            String mi = "" + minute;
            if (minute < 10){
                mi = "0" + minute;
            }
            time = "" + hourOfDay + ":" + mi;
        });
        builder.setTitle("Change")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("ok", (dialog, which) -> {
                    if (!taskEditText.getText().toString().isEmpty()){
                        String strSQL = "UPDATE " +
                                InputData.TaskEntry.DAY_TABLE +
                                " SET " +
                                InputData.TaskEntry.COLUMN_DAY_TASK +
                                " = '" +
                                taskEditText.getText().toString() + "' ," +
                                InputData.TaskEntry.COLUMN_DAY_TASK_TIME +
                                " = '" +
                                time +
                                "' WHERE " +
                                InputData.TaskEntry.COLUMN_ID +
                                " = " +
                                id +
                                ";";
                        db.execSQL(strSQL);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("cancel", (dialog, which) -> {

                });
        builder.create().show();
    }

    private void deleteTask(int position){
        String id = dayItems.get(position).getDayItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(InputData.TaskEntry.DAY_TABLE, InputData.TaskEntry.COLUMN_ID + "=?", new String[]{id});
        db.close();
    }
}
