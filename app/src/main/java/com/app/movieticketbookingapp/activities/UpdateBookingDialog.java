package com.app.movieticketbookingapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.app.movieticketbookingapp.models.Booking;
import com.app.movieticketbookingapp.models.Movie;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateBookingDialog extends DialogFragment {

    private final Booking booking;
    private final Movie movie;

    public UpdateBookingDialog(Booking booking, Movie movie) {
        this.booking = booking;
        this.movie = movie;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Update Ticket Quantity");

        // Layout with padding
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(40, 30, 40, 10);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Input field
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter new quantity");
        input.setText(String.valueOf(booking.getQuantity()));
        input.setSelection(input.getText().length());

        layout.addView(input, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        builder.setView(layout);
        builder.setPositiveButton("Update", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            Button updateBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            updateBtn.setEnabled(true); // Enable by default

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String qtyStr = s.toString().trim();
                    int oldQty = booking.getQuantity();
                    int totalAvailable = movie.getTotalTickets();
                    int maxAllowed = totalAvailable + oldQty;

                    if (qtyStr.isEmpty()) {
                        input.setError("Enter a quantity");
                        updateBtn.setEnabled(false);
                        return;
                    }

                    try {
                        int newQty = Integer.parseInt(qtyStr);

                        if (newQty <= 0) {
                            input.setError("Must be at least 1");
                            updateBtn.setEnabled(false);
                        } else if (newQty > maxAllowed) {
                            input.setError("Only " + maxAllowed + " tickets available");
                            updateBtn.setEnabled(false);
                        } else {
                            input.setError(null);
                            updateBtn.setEnabled(true);
                        }
                    } catch (NumberFormatException e) {
                        input.setError("Invalid number");
                        updateBtn.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            updateBtn.setOnClickListener(v -> {
                String qtyStr = input.getText().toString().trim();
                int newQty = Integer.parseInt(qtyStr);
                int oldQty = booking.getQuantity();
                int difference = newQty - oldQty;
                int newAvailable = movie.getTotalTickets() - difference;

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("movies").document(movie.getId())
                        .update("totalTickets", newAvailable)
                        .addOnSuccessListener(unused -> {
                            double newTotalPrice = newQty * booking.getPricePerTicket();

                            db.collection("bookings").document(booking.getId())
                                    .update(
                                            "quantity", newQty,
                                            "totalPrice", newTotalPrice
                                    )
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Booking updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update booking", Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update tickets", Toast.LENGTH_SHORT).show());
            });
        });

        return dialog;
    }
}