package com.app.movieticketbookingapp.adapters;

import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.models.Movie;
import com.app.movieticketbookingapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UserBookingAdapter extends RecyclerView.Adapter<UserBookingAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Movie movie);
    }

    public void setOnItemClickListener(UserBookingAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserBookingAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, showTime, ticketPrice, totalTickets;
        Button buttonBookTicket;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            showTime = itemView.findViewById(R.id.textShowTime);
            ticketPrice = itemView.findViewById(R.id.textTicketPrice);
            totalTickets = itemView.findViewById(R.id.textTotalTickets);
            buttonBookTicket = itemView.findViewById(R.id.buttonBookTicket);
        }

        public void bind(Movie movie, OnItemClickListener listener) {
            title.setText(movie.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            showTime.setText("Show Time: " + sdf.format(movie.getShowTime().toDate()));

            ticketPrice.setText("Ticket Price: Rs. " + movie.getTicketPrice());
            totalTickets.setText("Tickets Available: " + movie.getTotalTickets());

            buttonBookTicket.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(movie);
                }
            });
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_booking, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movieList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}