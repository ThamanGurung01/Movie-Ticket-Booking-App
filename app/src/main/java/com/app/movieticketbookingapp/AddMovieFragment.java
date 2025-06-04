package com.app.movieticketbookingapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

public class AddMovieFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set full width
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        try {
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

            String[] genres = getResources().getStringArray(R.array.genres_array);
            boolean[] selectedGenres = new boolean[genres.length];
            ArrayList<String> selectedList = new ArrayList<>();

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

                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    if (selectedList.isEmpty()) {
                        genreTextView.setText("Choose Genre");
                    }
                });

                builder.show();
            });

            save.setOnClickListener(v -> {
                String movieTitle = title.getText().toString().trim();
                String movieYear = year.getText().toString().trim();
                String movieLanguage = language.getText().toString().trim();
                String movieDuration = duration.getText().toString().trim();
                String movieDescription = description.getText().toString().trim();
                if (movieTitle.isEmpty() || movieYear.isEmpty() || movieLanguage.isEmpty() || movieDuration.isEmpty() || movieDescription.isEmpty() || selectedList.isEmpty()) {
                    Toast.makeText(getContext(), "Fill in both fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                Movie movie = new Movie(
                        movieTitle,
                        Integer.parseInt(movieYear),
                        movieLanguage,
                        Integer.parseInt(movieDuration),
                        movieDescription,
                        selectedList
                );
                db.collection("movies").add(movie).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Movie added!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
// for checking data
//                Toast.makeText(getContext(), "Saved: " + movieTitle + " (" + movieYear + ")\n" + "Language: " + movieLanguage + "\n" +
//                        "Duration: " + movieDuration + " mins\n" + "Description: " + movieDescription + "\n" + "Genres: "
//                        + String.join(", ", selectedList), Toast.LENGTH_LONG).show();
//                dismiss();
            });

            cancel.setOnClickListener(v -> dismiss());

            return view;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
