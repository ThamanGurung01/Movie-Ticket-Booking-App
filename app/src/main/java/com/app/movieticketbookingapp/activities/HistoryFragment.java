package com.app.movieticketbookingapp.activities;

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
import com.app.movieticketbookingapp.adapters.HistoryDisplayAdapter;
import com.app.movieticketbookingapp.models.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textNoHistory;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<Booking> bookingList;
    private HistoryDisplayAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        textNoHistory = view.findViewById(R.id.textNoHistory);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bookingList = new ArrayList<>();
        adapter = new HistoryDisplayAdapter(bookingList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchCompletedBookings();

        return view;
    }

    private void fetchCompletedBookings() {
        if (currentUser == null) return;

        String userEmail = currentUser.getEmail();

        db.collection("bookings")
                .whereEqualTo("userEmail", userEmail)
                .whereEqualTo("status", "completed")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) {
                        Toast.makeText(getContext(), "Error loading history", Toast.LENGTH_SHORT).show();
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
                    textNoHistory.setVisibility(bookingList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }
}
