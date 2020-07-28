package com.suleymanince.realmsimpleapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter extends BaseAdapter {

    List<KisiModel> list;
    Context context;

    public Adapter(List<KisiModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_liste,parent,false);

        TextView listKulAdi = convertView.findViewById(R.id.txt_list_kuladi);
        TextView listAdi = convertView.findViewById(R.id.txt_list_adi);
        TextView listSifre = convertView.findViewById(R.id.txt_list_sifre);

        listKulAdi.setText(list.get(position).getKullaniciAdi());
        listAdi.setText(list.get(position).getIsim());
        listSifre.setText(list.get(position).getSifre());
        return convertView;
    }
}
