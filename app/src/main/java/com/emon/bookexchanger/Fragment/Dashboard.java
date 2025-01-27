package com.emon.bookexchanger.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.emon.bookexchanger.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Dashboard extends Fragment {


    ArrayList<HashMap<String,String>> arrayList =new ArrayList<>();
    HashMap<String,String> hashMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        GridView listView = view.findViewById(R.id.listview);


        HashMapMethod();


        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);








        return view;
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View viewlist = inflater.inflate(R.layout.book_item, parent, false);



            return viewlist;
        }
    }

    private void HashMapMethod(){

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");

        arrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);



        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("fcdssd","csdcsd");
        arrayList.add(hashMap);
    }

}