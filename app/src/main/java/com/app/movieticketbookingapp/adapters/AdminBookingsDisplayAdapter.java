package com.app.movieticketbookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.models.Booking;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminBookingsDisplayAdapter extends RecyclerView.Adapter<AdminBookingsDisplayAdapter.ViewHolder> {

    public interface OnBookingActionListener {
        void onAddClick(Booking booking);
        void onCancelClick(Booking booking);
    }

    private final List<Booking> bookingList;
    private final OnBookingActionListener listener;

    public AdminBookingsDisplayAdapter(List<Booking> bookingList, OnBookingActionListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminBookingsDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookingsDisplayAdapter.ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.textUserName.setText("User: " + booking.getUserEmail());
        holder.textMovieTitle.setText("Movie: " + booking.getMovieTitle());
        holder.textQuantity.setText("Tickets: " + booking.getQuantity());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        if (booking.getShowTime() != null) {
            holder.textShowTime.setText("Show Time: " + sdf.format(booking.getShowTime().toDate()));
        } else {
            holder.textShowTime.setText("Show Time: N/A");
        }

        holder.buttonAdd.setOnClickListener(v -> listener.onAddClick(booking));
        holder.buttonCancel.setOnClickListener(v -> listener.onCancelClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textMovieTitle, textQuantity, textShowTime;
        Button buttonAdd, buttonCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textUserName = itemView.findViewById(R.id.textUserName);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitle);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textShowTime = itemView.findViewById(R.id.textShowTime);

            buttonAdd = itemView.findViewById(R.id.buttonAddMore);
            buttonCancel = itemView.findViewById(R.id.buttonCancelBooking);
        }
    }
}
