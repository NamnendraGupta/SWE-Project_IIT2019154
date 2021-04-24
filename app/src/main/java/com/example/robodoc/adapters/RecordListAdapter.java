package com.example.robodoc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.fragments.shared.RecordsFragmentDirections;
import com.example.robodoc.utils.DateTimeUtils;

import java.util.ArrayList;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private final ArrayList<VitalInput> recordList;
    private final NavController navController;

    public RecordListAdapter(ArrayList<VitalInput> recordList, NavController navController){
        this.recordList=recordList;
        this.navController=navController;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNum, tvDate, tvTime;
        private ImageButton btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum=itemView.findViewById(R.id.tvRecordNum);
            tvDate=itemView.findViewById(R.id.tvRecordDate);
            tvTime=itemView.findViewById(R.id.tvRecordTime);
            btnViewDetails=itemView.findViewById(R.id.imgBtnViewRecordDetails);
        }

    }

    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder holder, int position) {
        String positionText= Integer.toString(position+1);
        VitalInput record=recordList.get(position);
        holder.tvNum.setText(positionText);
        holder.tvDate.setText(DateTimeUtils.getDisplayDate(record.getTimeOfInput()));
        holder.tvTime.setText(DateTimeUtils.getDisplayTime(record.getTimeOfInput()));
        holder.btnViewDetails.setOnClickListener(v -> showRecordDetails(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    private void showRecordDetails(int position){
        NavDirections action= RecordsFragmentDirections.ActionRecordInfo(position);
        navController.navigate(action);
    }
}
