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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Movie movie);
        void onDeleteClick(Movie movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, details, description, genres,showTime,totalTickets,ticketPrice;
        Button buttonEdit, buttonDelete;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            details = itemView.findViewById(R.id.textDetails);
            description = itemView.findViewById(R.id.textDescription);
            genres = itemView.findViewById(R.id.textGenres);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            showTime = itemView.findViewById(R.id.textShowTime);
            totalTickets = itemView.findViewById(R.id.textTotalTickets);
            ticketPrice=itemView.findViewById(R.id.textTicketPrice);
        }

        public void bind(Movie movie) {
            title.setText(movie.getTitle());
            details.setText(movie.getYear() + " | " + movie.getLanguage() + " | " + movie.getDuration() + " mins");
            description.setText(movie.getDescription());
            genres.setText("Genres: " + String.join(", ", movie.getGenres()));
            ticketPrice.setText("Ticket Price: Rs. " + movie.getTicketPrice());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            showTime.setText("Show Time: " + sdf.format(movie.getShowTime().toDate()));
            totalTickets.setText("Tickets Available: " + movie.getTotalTickets());
            buttonEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(movie);
            });

            buttonDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(movie);
            });
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}