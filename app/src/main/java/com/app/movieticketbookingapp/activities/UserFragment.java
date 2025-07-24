package com.app.movieticketbookingapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.adapters.UserAdapter;
import com.app.movieticketbookingapp.models.User;
import com.google.firebase.firestore.*;

import java.util.*;

public class UserFragment extends Fragment {
    private FirebaseFirestore db;
    private List<User> userList;
    private UserAdapter adapter;
    private TextView textNoUsers;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_fragment, container, false);

        textNoUsers = view.findViewById(R.id.textNoUsers);
//        Button addUser = view.findViewById(R.id.buttonAddUser);
//        addUser.setOnClickListener(v -> {
//            new AddUserFragment().show(getParentFragmentManager(), "AddUserFragment");
//        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
//        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
//            @Override
//            public void onEditClick(User user) {
////                new EditUserFragment(user).show(getParentFragmentManager(), "EditUserFragment");
//            }
//
//            @Override
//            public void onDeleteClick(User user) {
//                new AlertDialog.Builder(requireContext())
//                        .setTitle("Delete User")
//                        .setMessage("Are you sure you want to delete " + user.getName() + "?")
//                        .setPositiveButton("Delete", (dialog, which) -> {
//                            db.collection("users").document(user.getId())
//                                    .delete()
//                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Deleted: " + user.getName(), Toast.LENGTH_SHORT).show())
//                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                        })
//                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//                        .setCancelable(true)
//                        .show();
//            }
//        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchUsers();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchUsers() {
        db.collection("users")
                .whereEqualTo("role", "user")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        Log.e("Firestore", "Error fetching users", error);
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setId(doc.getId());
                            userList.add(user);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    textNoUsers.setVisibility(userList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }
}
