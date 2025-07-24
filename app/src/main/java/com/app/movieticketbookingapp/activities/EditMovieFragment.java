package com.app.movieticketbookingapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.DialogFragment;

import com.app.movieticketbookingapp.models.Movie;
import com.app.movieticketbookingapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditMovieFragment extends DialogFragment {
    private Movie movie;
    private String docId;

    public EditMovieFragment(Movie movie) {
        this.movie = movie;
        this.docId = movie.getId();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.activity_add_movie_fragment, container, false);

        EditText title = view.findViewById(R.id.editTextTitle);
        EditText year = view.findViewById(R.id.editTextYear);
        EditText language = view.findViewById(R.id.editTextLanguage);
        EditText duration = view.findViewById(R.id.editTextDuration);
        EditText description = view.findViewById(R.id.editTextDescription);
        Button save = view.findViewById(R.id.buttonSave);
        Button cancel = view.findViewById(R.id.buttonCancel);
        TextView genreTextView = view.findViewById(R.id.textViewGenrePicker);


        title.setText(movie.getTitle());
        year.setText(String.valueOf(movie.getYear()));
        language.setText(movie.getLanguage());
        duration.setText(String.valueOf(movie.getDuration()));
        description.setText(movie.getDescription());

        String[] genres = getResources().getStringArray(R.array.genres_array);
        boolean[] selectedGenres = new boolean[genres.length];
        ArrayList<String> selectedList = new ArrayList<>();

        for (int i = 0; i < genres.length; i++) {
            if (movie.getGenres().contains(genres[i])) {
                selectedGenres[i] = true;
                selectedList.add(genres[i]);
            }
        }

        genreTextView.setText(String.join(", ", selectedList));

        genreTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Genres");

            builder.setMultiChoiceItems(genres, selectedGenres, (dialog, which, isChecked) -> {
                if (isChecked) selectedList.add(genres[which]);
                else selectedList.remove(genres[which]);
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                genreTextView.setText(String.join(", ", selectedList));
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {});
            builder.show();
        });

        save.setText("Update");
        save.setOnClickListener(v -> {
            String movieTitle = title.getText().toString().trim();
            String movieYear = year.getText().toString().trim();
            String movieLanguage = language.getText().toString().trim();
            String movieDuration = duration.getText().toString().trim();
            String movieDescription = description.getText().toString().trim();

            if (movieTitle.isEmpty() || movieYear.isEmpty() || movieLanguage.isEmpty() || movieDuration.isEmpty() || movieDescription.isEmpty() || selectedList.isEmpty()) {
                Toast.makeText(getContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            movie.setTitle(movieTitle);
            movie.setYear(Integer.parseInt(movieYear));
            movie.setLanguage(movieLanguage);
            movie.setDuration(Integer.parseInt(movieDuration));
            movie.setDescription(movieDescription);
            movie.setGenres(selectedList);

            db.collection("movies").document(docId)
                    .set(movie)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Movie updated!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        cancel.setOnClickListener(v -> dismiss());

        return view;
    }
}