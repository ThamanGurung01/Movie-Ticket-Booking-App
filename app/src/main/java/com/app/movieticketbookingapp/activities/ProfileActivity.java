package com.app.movieticketbookingapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.movieticketbookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView textName, textEmail, textRole;
    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textRole = findViewById(R.id.textRole);
        buttonEdit = findViewById(R.id.buttonEdit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Show email as read-only text (no editing)
            textEmail.setText("Email: " + user.getEmail());
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            textName.setText("Name: " + document.getString("name"));
                            textRole.setText("Role: " + document.getString("role"));
                        }
                    });
        }

        buttonEdit.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
            EditText editName = dialogView.findViewById(R.id.editName);
            EditText editPassword = dialogView.findViewById(R.id.editPassword);
            Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
            Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
            editName.setText(textName.getText().toString().replace("Name: ", "").trim());
            editPassword.setText("");

            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            buttonCancel.setOnClickListener(cancelView -> dialog.dismiss());

            buttonUpdate.setOnClickListener(updateView -> {
                String newName = editName.getText().toString().trim();
                String newPassword = editPassword.getText().toString().trim();

                if (newName.isEmpty()) {
                    editName.setError("Name cannot be empty");
                    editName.requestFocus();
                    return;
                }

                if (!newPassword.isEmpty() && newPassword.length() < 6) {
                    editPassword.setError("Password must be at least 6 characters");
                    editPassword.requestFocus();
                    return;
                }

                if (user != null) {
                    boolean hasChanges = false;

                    if (!newName.equals(textName.getText().toString().replace("Name: ", "").trim())) {
                        hasChanges = true;
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(user.getUid())
                                .update("name", newName)
                                .addOnSuccessListener(aVoid -> {
                                    textName.setText("Name: " + newName);
                                    Toast.makeText(ProfileActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProfileActivity.this, "Failed to update name: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }

                    if (!newPassword.isEmpty()) {
                        hasChanges = true;
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    editPassword.setError("Failed to update password: " + e.getMessage());
                                    editPassword.requestFocus();
                                });
                    }

                    if (!hasChanges) {
                        Toast.makeText(ProfileActivity.this, "No changes to update", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                    }
                }
            });


            dialog.show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
