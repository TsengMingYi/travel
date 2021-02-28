package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<TravelManager.TravelData> travelDataList = new ArrayList<>();
    private ArrayList<TravelManager.TravelData> filterList = new ArrayList<>();
    private String currentCityName="";

    public void setCurrentCityName(String currentCityName) {
        this.currentCityName = currentCityName;
        filter();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.travel_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TravelManager.TravelData travelData = filterList.get(position);
        holder.Name.setText(travelData.Name);
        //holder.Id.setText(travelData.Id);
        holder.Add.setText(travelData.Add);
        holder.Region.setText(travelData.Region);
        holder.Town.setText(travelData.Town);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), MapActivity.class);

                Intent intent = new Intent();
                intent.setAction("Hello");
                intent.addCategory("Hi");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("Name",holder.Name.getText().toString());
                intent.putExtra("Px", travelData.Px);
                intent.putExtra("Py", travelData.Py);
                view.getContext().startActivity(intent);
            }
        });
    }
    public void refreshUI( ArrayList<TravelManager.TravelData> travelDataList) {
        this.travelDataList = travelDataList;
        filter();
        notifyDataSetChanged();
    }




    /**
     * 1.過濾 travelDataList裡面的每一筆資料的Region是否重複，重複就不添加到filterList裡面，不重複就添加到filterList裡面。
     */
    private void filter(){
        /**
         * 1.先把filterList清空
         * 2.用for廻圈去對比travelDataList裡面的每一筆資料的Region
         * 3.
         *
         * **/
        filterList.clear();
        for(int i = 0;i<travelDataList.size();i++){
            if(travelDataList.get(i).Region.equals(currentCityName)){
                filterList.add(travelDataList.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        //TextView Id;
        TextView Add;
        TextView Region;
        TextView Town;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            //Id = itemView.findViewById(R.id.Id);
            Add = itemView.findViewById(R.id.Add);
            Region = itemView.findViewById(R.id.Region);
            Town = itemView.findViewById(R.id.Town);
        }
    }
}
