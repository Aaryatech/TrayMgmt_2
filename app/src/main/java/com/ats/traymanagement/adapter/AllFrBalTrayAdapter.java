package com.ats.traymanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.traymanagement.R;
import com.ats.traymanagement.activity.FrTrayReportEightDaysActivity;
import com.ats.traymanagement.model.AllFrBalanceTrayReport;

import java.util.ArrayList;
import java.util.List;

public class AllFrBalTrayAdapter extends RecyclerView.Adapter<AllFrBalTrayAdapter.MyViewHolder> implements Filterable {

    private ArrayList<AllFrBalanceTrayReport> frList;
    private ArrayList<AllFrBalanceTrayReport> filteredFrList;
    private Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFrName, tvBalSmall, tvBalBig, tvBalLid;
        public CardView card;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvBalSmall = view.findViewById(R.id.tvBalSmall);
            tvBalBig = view.findViewById(R.id.tvBalBig);
            tvBalLid = view.findViewById(R.id.tvBalLid);
            card = view.findViewById(R.id.card);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }


    public AllFrBalTrayAdapter(ArrayList<AllFrBalanceTrayReport> frList, Context context) {
        this.frList = frList;
        this.filteredFrList = frList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_all_fr_bal_tray, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvFrName.setText(filteredFrList.get(position).getFrName());
        holder.tvBalSmall.setText(""+filteredFrList.get(position).getBalanceSmall());
        holder.tvBalBig.setText(""+filteredFrList.get(position).getBalanceBig());
        holder.tvBalLid.setText(""+filteredFrList.get(position).getBalanceLead());

        if(position%2==0){
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorList));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, FrTrayReportEightDaysActivity.class);
                intent.putExtra("frId",filteredFrList.get(position).getFrId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(frList != null){
            return filteredFrList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredFrList = frList;
                } else {
                    ArrayList<AllFrBalanceTrayReport> filteredList = new ArrayList<>();
                    for (AllFrBalanceTrayReport report : frList) {
                        if (report.getFrName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(report);
                        }
                    }
                    filteredFrList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredFrList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredFrList = (ArrayList<AllFrBalanceTrayReport>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
