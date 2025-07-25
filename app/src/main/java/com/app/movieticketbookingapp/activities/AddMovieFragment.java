package com.app.movieticketbookingapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.DialogFragment;

import com.app.movieticketbookingapp.models.Movie;
import com.app.movieticketbookingapp.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddMovieFragment extends DialogFragment {
    Calendar calendar;
    EditText dateTime;
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
            dateTime = view.findViewById(R.id.editDateTime);
            EditText tickets = view.findViewById(R.id.totalTickets);
            EditText ticketPrice = view.findViewById(R.id.editTextTicketPrice);
            Button save = view.findViewById(R.id.buttonSave);
            Button cancel = view.findViewById(R.id.buttonCancel);

            calendar = Calendar.getInstance();

            dateTime.setOnClickListener(v -> showDateTimePicker());

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
                String showTimeString = dateTime.getText().toString().trim();
                String totalTicketsStr = tickets.getText().toString().trim();
                String ticketPriceStr = ticketPrice.getText().toString().trim();
                if (movieTitle.isEmpty() || movieYear.isEmpty() || movieLanguage.isEmpty() || movieDuration.isEmpty() ||
                        movieDescription.isEmpty() || selectedList.isEmpty() || showTimeString.isEmpty() || totalTicketsStr.isEmpty() || ticketPriceStr.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Timestamp showTimestamp;
                try {
                    showTimestamp = new Timestamp(Objects.requireNonNull(sdf.parse(showTimeString)));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                    return;
                }

                int totalTicketsCount = Integer.parseInt(totalTicketsStr);
                double parseTicketPrice=Double.parseDouble(ticketPriceStr);
                Movie movie = new Movie(
                        movieTitle,
                        Integer.parseInt(movieYear),
                        movieLanguage,
                        Integer.parseInt(movieDuration),
                        movieDescription,
                        selectedList,
                        Timestamp.now(),
                        showTimestamp,
                        totalTicketsCount,
                        parseTicketPrice
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
    private void showDateTimePicker() {
        // Show DatePicker first
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                dateTime.setText(sdf.format(calendar.getTime()));

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
