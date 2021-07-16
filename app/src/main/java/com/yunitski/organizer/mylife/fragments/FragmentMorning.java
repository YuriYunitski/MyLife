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
import com.yunitski.organizer.mylife.adapters.MorningItemAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;
import com.yunitski.organizer.mylife.itemClasses.MorningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentMorning extends Fragment {

    RecyclerView recyclerView;
    MorningItemAdapter adapter;
    ArrayList<MorningItem> morningItems, completedMorningItems, totalMorningItems;
    String time, dd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_morning_layout, container, false);
        recyclerView = view.findViewById(R.id.morningRecyclerView);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        dd = activity.getMyData();
        updateUIMorning();
        ((MainActivity) requireActivity()).setFragmentRefreshListener(this::updateUIMorning);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUIMorning(){
        morningItems = new ArrayList<>();

        completedMorningItems = new ArrayList<>();

        totalMorningItems = new ArrayList<>();

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        dd = activity.getMyData();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + InputData.TaskEntry.MORNING_TABLE + " WHERE " + InputData.TaskEntry.COLUMN_MORNING_TASK_DATE + " = '" + dd + "';", null);
        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_ID);
            int textIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK);
            int timeIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_TIME);
            int statusIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_STATUS);
            int dateIndex = cursor.getColumnIndex(InputData.TaskEntry.COLUMN_MORNING_TASK_DATE);

            if (cursor.getString(statusIndex).equals("wait") && cursor.getString(dateIndex).equals(dd)){
                String[] mit = cursor.getString(timeIndex).split(":");
                int hour = Integer.parseInt(mit[0]);
                int min = Integer.parseInt(mit[1]);
                int tTotal = (hour * 60) + min;
                morningItems.add(new MorningItem(cursor.getString(idIndex), cursor.getString(textIndex), cursor.getString(timeIndex), cursor.getString(statusIndex), cursor.getString(dateIndex), tTotal));
            } else if (cursor.getString(statusIndex).equals("done") && cursor.getString(dateIndex).equals(dd)){
                String[] mit = cursor.getString(timeIndex).split(":");
                int hour = Integer.parseInt(mit[0]);
                int min = Integer.parseInt(mit[1]);
                int tTotal = (hour * 60) + min;
                completedMorningItems.add(new MorningItem(cursor.getString(idIndex), cursor.getString(textIndex), cursor.getString(timeIndex), cursor.getString(statusIndex), cursor.getString(dateIndex), tTotal));
            }
        }
        db.close();
        cursor.close();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        Collections.sort(morningItems);
        totalMorningItems.addAll(morningItems);
        totalMorningItems.addAll(completedMorningItems);
        adapter = new MorningItemAdapter(totalMorningItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnMorningClickListener(position -> showBottomSheetDialog(morningItems.get(position).getMorningItemText(), position));

    }

//    public static void bubbleSort(ArrayList<Integer> timeArr, ArrayList<Integer> indexes) {
//        int n = timeArr.size();
//        for (int i = 0; i < n-1; i++)
//            for (int j = 0; j < n-i-1; j++)
//                if (timeArr.get(j) > timeArr.get(j + 1)) {
//                    int temp = timeArr.get(j);
//                    timeArr.get(j) = timeArr.get(j + 1);
//                    timeArr[j+1] = temp;
//                }
//    }

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
            updateUIMorning();
        });
        assert edit != null;
        edit.setOnClickListener(v -> {
            editTask(position);
            bottomSheetDialog.dismiss();
            updateUIMorning();
        });
        assert delete != null;
        delete.setOnClickListener(v -> {
            deleteTask(position);
            bottomSheetDialog.dismiss();
            updateUIMorning();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void editTask(int position) {
        String id = morningItems.get(position).getMorningItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.task_picker_dialog, null);
        EditText taskEditText = view.findViewById(R.id.taskEditText);
        TimePicker taskTimePicker = view.findViewById(R.id.taskTimePicker);
        taskTimePicker.setIs24HourView(true);
        taskEditText.setText(morningItems.get(position).getMorningItemText());
        String t = morningItems.get(position).getMorningItemTime();
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
                                InputData.TaskEntry.MORNING_TABLE +
                                " SET " +
                                InputData.TaskEntry.COLUMN_MORNING_TASK +
                                " = '" +
                                taskEditText.getText().toString() + "' ," +
                                InputData.TaskEntry.COLUMN_MORNING_TASK_TIME +
                                " = '" +
                                time +
                                "' WHERE " +
                                InputData.TaskEntry.COLUMN_ID +
                                " = " +
                                id +
                                ";";
                        db.execSQL(strSQL);
                        db.close();
                        updateUIMorning();
                    }
                })
                .setNegativeButton("cancel", (dialog, which) -> {

                });
        builder.create().show();

    }

    private void deleteTask(int position) {
        String id = morningItems.get(position).getMorningItemId();
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(InputData.TaskEntry.MORNING_TABLE, InputData.TaskEntry.COLUMN_ID + "=?", new String[]{id});
        db.close();
    }
}
