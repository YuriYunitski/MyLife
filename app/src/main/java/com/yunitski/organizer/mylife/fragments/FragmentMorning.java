package com.yunitski.organizer.mylife.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.adapters.MorningItemAdapter;
import com.yunitski.organizer.mylife.itemClasses.MorningItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentMorning extends Fragment {

    RecyclerView recyclerView;
    MorningItemAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_morning_layout, container, false);
        recyclerView = view.findViewById(R.id.morningRecyclerView);
        ArrayList<MorningItem> morningItems = new ArrayList<>();
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        morningItems.add(new MorningItem("чё там"));
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MorningItemAdapter(morningItems);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
