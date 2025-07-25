package com.app.movieticketbookingapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.models.Booking;

import java.util.List;

public class BookingDisplayAdapter extends RecyclerView.Adapter<BookingDisplayAdapter.ViewHolder> {

    public interface OnBookingActionListener {
        void onAddClick(Booking booking);
        void onCancelClick(Booking booking);
    }

    private List<Booking> bookingList;
    private OnBookingActionListener listener;

    public BookingDisplayAdapter(List<Booking> bookingList, OnBookingActionListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textQuantity, textTotalPrice, textShowTime;
        Button buttonAdd, buttonCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textBookingTitle);
            textQuantity = itemView.findViewById(R.id.textBookingQuantity);
            textTotalPrice = itemView.findViewById(R.id.textBookingTotalPrice);
            textShowTime = itemView.findViewById(R.id.textBookingShowTime);
            buttonAdd = itemView.findViewById(R.id.buttonAddMore);
            buttonCancel = itemView.findViewById(R.id.buttonCancelBooking);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Booking booking, OnBookingActionListener listener) {
            textTitle.setText(booking.getMovieTitle());
            textQuantity.setText("Quantity: " + booking.getQuantity());
            textTotalPrice.setText("Total: Rs. " + booking.getTotalPrice());

            textShowTime.setText("Show Time: " + new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a", java.util.Locale.getDefault())
                    .format(booking.getShowTime().toDate()));

            buttonAdd.setOnClickListener(v -> listener.onAddClick(booking));
            buttonCancel.setOnClickListener(v -> listener.onCancelClick(booking));
        }
    }

    @Override
    public BookingDisplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_booking, parent, false);
        return new BookingDisplayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingDisplayAdapter.ViewHolder holder, int position) {
        holder.bind(bookingList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}

