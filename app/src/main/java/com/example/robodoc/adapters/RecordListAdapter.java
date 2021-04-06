package com.example.robodoc.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.fragments.user.RecordDetailFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    ArrayList<VitalInput> recordList;
    FragmentManager manager;

    public RecordListAdapter(FragmentManager manager, ArrayList<VitalInput> recordList){
        this.manager=manager;
        this.recordList=recordList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNum, tvDate, tvTime, tvID;
        Button btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum=itemView.findViewById(R.id.tvRecordNum);
            tvDate=itemView.findViewById(R.id.tvRecordDate);
            tvTime=itemView.findViewById(R.id.tvRecordTime);
            tvID=itemView.findViewById(R.id.tvRecordID);

            btnViewDetails=itemView.findViewById(R.id.btnViewRecordDetails);
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
        VitalInput record=recordList.get(position);
        holder.tvNum.setText(Integer.toString(position+1));
        holder.tvDate.setText(getRecordDate(record.getTimeOfInput()));
        holder.tvTime.setText(getRecordTime(record.getTimeOfInput()));
        holder.tvID.setText(record.getInputID());
        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordDetailFragment recordFragment=RecordDetailFragment.newInstance(record);
                recordFragment.show(manager,"RECORD DETAILS");
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    private String getRecordTime(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringTime="";

        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);

        if(hour==0)
            stringTime+="12:";
        else{
            if(hour<10)
                stringTime+="0";
            stringTime+=hour+":";
        }

        if(minute<10)
            stringTime+="0";
        stringTime+=minute+" ";

        if(calendar.get(Calendar.AM_PM)==1)
            stringTime+="PM";
        else
            stringTime+="AM";

        return stringTime;
    }

    private String getRecordDate(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringDate="";

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        if(day<10)
            stringDate+="0";
        stringDate+=day+"-";

        if(month<10)
            stringDate+="0";
        stringDate+=month+"-";

        stringDate+=year;

        return stringDate;
    }
}
