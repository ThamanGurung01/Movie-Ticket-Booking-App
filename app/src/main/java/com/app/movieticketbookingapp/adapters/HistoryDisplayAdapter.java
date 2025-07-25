package com.app.movieticketbookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.models.Booking;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryDisplayAdapter extends RecyclerView.Adapter<HistoryDisplayAdapter.ViewHolder> {

    private final List<Booking> bookingList;

    public HistoryDisplayAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.textMovieName.setText(booking.getMovieTitle());
        holder.textQuantity.setText("Tickets: " + booking.getQuantity());
        holder.textPrice.setText("Total cost: " +booking.getTotalPrice());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        if (booking.getShowTime() != null) {
            holder.textShowTime.setText("Show Time: " + sdf.format(booking.getShowTime().toDate()));
        } else {
            holder.textShowTime.setText("Show Time: N/A");
        }

        holder.textStatus.setText("Status: " + booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMovieName, textQuantity, textShowTime, textStatus,textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMovieName = itemView.findViewById(R.id.textMovieName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textShowTime = itemView.findViewById(R.id.textShowTime);
            textStatus = itemView.findViewById(R.id.textStatus);
            textPrice=itemView.findViewById(R.id.textPrice);
        }
    }
}
