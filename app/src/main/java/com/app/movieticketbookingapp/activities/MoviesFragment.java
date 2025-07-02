package com.app.movieticketbookingapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.adapters.MovieAdapter;
import com.app.movieticketbookingapp.models.Movie;
import com.google.firebase.firestore.*;

import java.util.*;

public class MoviesFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Movie> movieList;
    private MovieAdapter adapter;
    private TextView textNoMovies;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(com.app.movieticketbookingapp.R.layout.activity_movies_fragment, container, false);

        textNoMovies = view.findViewById(com.app.movieticketbookingapp.R.id.textNoMovies);
        Button addMovie = view.findViewById(com.app.movieticketbookingapp.R.id.buttonAddMovie);
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddMovieFragment().show(getParentFragmentManager(), "AddMovieFragment");
            }
        });

        //db
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchMovies();

        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void fetchMovies() {
        db.collection("movies").orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null ||value==null) {
                        Log.e("Firestore","Error fetching data ",error);
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    movieList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Movie movie = doc.toObject(Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (movieList.isEmpty()) {
                        textNoMovies.setVisibility(View.VISIBLE);
                    } else {
                        textNoMovies.setVisibility(View.GONE);
                    }
                });
    }
}