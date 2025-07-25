package com.app.movieticketbookingapp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.models.Booking;
import com.app.movieticketbookingapp.models.Movie;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookTicketFragment extends DialogFragment {

    private Movie movie;

    public BookTicketFragment(Movie movie) {
        this.movie = movie;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_book_ticket_fragment, container, false);

        TextView textMovieTitle = view.findViewById(R.id.textMovieTitle);
        TextView textPricePerTicket = view.findViewById(R.id.textPriceInfo);
        TextView textAvailableTicket=view.findViewById(R.id.textAvailableTicket);
        EditText editTextQuantity = view.findViewById(R.id.editTextTicketCount);
        TextView textTotalPrice = view.findViewById(R.id.textTotalPrice);
        Button buttonBook = view.findViewById(R.id.buttonConfirmBooking);
        TextView textMovieSchedule = view.findViewById(R.id.textMovieSchedule);

        if (movie != null && movie.getShowTime() != null && movie.getTitle()!=null && movie.getTicketPrice()!=0.0 && movie.getTotalTickets()!=0) {Timestamp timestamp = movie.getShowTime();
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            String formattedDate = sdf.format(date);
            textMovieSchedule.setText("Schedule: " + formattedDate);
            textMovieTitle.setText(movie.getTitle());
            textPricePerTicket.setText("Price: Rs. " + movie.getTicketPrice());
            textAvailableTicket.setText("Available Tickets: "+movie.getTotalTickets());
        }
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                if (!input.isEmpty()) {
                    int quantity = Integer.parseInt(input);
                    int available = movie.getTotalTickets();

                    if (quantity > available) {
                        editTextQuantity.setText(String.valueOf(available));
                        editTextQuantity.setSelection(editTextQuantity.getText().length());
                        Toast.makeText(getContext(), "Only " + available + " tickets available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double total = quantity * movie.getTicketPrice();
                    textTotalPrice.setText("Total: Rs. " + total);
                } else {
                    textTotalPrice.setText("Total: Rs. 0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        buttonBook.setOnClickListener(v -> {
            String qtyStr = editTextQuantity.getText().toString().trim();
            if (qtyStr.isEmpty()) {
                Toast.makeText(getContext(), "Enter number of tickets", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(qtyStr);
            int available = movie.getTotalTickets();

            if (quantity <= 0) {
                Toast.makeText(getContext(), "Enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity > available) {
                Toast.makeText(getContext(), "Only " + available + " tickets available", Toast.LENGTH_SHORT).show();
                return;
            }

            int remaining = available - quantity;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(getContext(), "Please log in to book tickets", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = user.getEmail();
            double totalPrice = quantity * movie.getTicketPrice();

            Booking booking = new Booking(
                    userEmail,
                    movie.getId(),
                    movie.getTitle(),
                    quantity,
                    movie.getTicketPrice(),
                    totalPrice,
                    movie.getShowTime(),
                    Timestamp.now()
            );

            FirebaseFirestore.getInstance().collection("bookings")
                    .add(booking)
                    .addOnSuccessListener(docRef -> {
                        FirebaseFirestore.getInstance().collection("movies")
                                .document(movie.getId())
                                .update("totalTickets", remaining)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getContext(), "Tickets booked successfully", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to update tickets", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Booking failed", Toast.LENGTH_SHORT).show();
                    });
        });
        return view;
    }
}
