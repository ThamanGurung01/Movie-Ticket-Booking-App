package com.app.movieticketbookingapp.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.models.Movie;
import com.app.movieticketbookingapp.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, details, description, genres;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            details = itemView.findViewById(R.id.textDetails);
            description = itemView.findViewById(R.id.textDescription);
            genres = itemView.findViewById(R.id.textGenres);
        }

        public void bind(Movie movie) {
            title.setText(movie.getTitle());
            details.setText(movie.getYear() + " | " + movie.getLanguage() + " | " + movie.getDuration() + " mins");
            description.setText(movie.getDescription());
            genres.setText("Genres: " + String.join(", ", movie.getGenres()));
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

