package com.app.movieticketbookingapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.adapters.BookingDisplayAdapter;
import com.app.movieticketbookingapp.models.Booking;
import com.app.movieticketbookingapp.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CurrentBookedFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textNoBookings;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<Booking> bookingList;
    private BookingDisplayAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_current_booked_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        textNoBookings = view.findViewById(R.id.textNoBookings);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bookingList = new ArrayList<>();
        adapter = new BookingDisplayAdapter(bookingList, new BookingDisplayAdapter.OnBookingActionListener() {
            @Override
            public void onAddClick(Booking booking) {
                db.collection("movies").document(booking.getMovieDocId()).get()
                        .addOnSuccessListener(snapshot -> {
                            Movie movie = snapshot.toObject(Movie.class);
                            if (movie != null) {
                                movie.setId(snapshot.getId());
                                new UpdateBookingDialog(booking, movie)
                                        .show(getChildFragmentManager(), "UpdateBookingDialog");
                            } else {
                                Toast.makeText(getContext(), "Movie data not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to fetch movie", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelClick(Booking booking) {
                cancelBooking(booking);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        if (currentUser == null) return;

        String userEmail = currentUser.getEmail();

        db.collection("bookings")
                .whereEqualTo("userEmail", userEmail).whereEqualTo("status", "active")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) {
                        Toast.makeText(getContext(), "Error loading bookings", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bookingList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Booking booking = doc.toObject(Booking.class);
                        if (booking != null) {
                            booking.setId(doc.getId());
                            bookingList.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    textNoBookings.setVisibility(bookingList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    private void cancelBooking(Booking booking) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("bookings").document(booking.getId()).delete()
                            .addOnSuccessListener(unused -> {
                                db.collection("movies").document(booking.getMovieDocId())
                                        .update("totalTickets", FieldValue.increment(booking.getQuantity()))
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_SHORT).show();
                                            fetchBookings();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error restoring tickets", Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error cancelling booking", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
