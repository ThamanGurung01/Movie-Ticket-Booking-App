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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.R;
import com.app.movieticketbookingapp.adapters.MovieAdapter;
import com.app.movieticketbookingapp.adapters.UserBookingAdapter;
import com.app.movieticketbookingapp.models.Movie;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Movie> movieList;
    private UserBookingAdapter adapter;
    private TextView textNoMovies;
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_booking_fragment, container, false);

    textNoMovies = view.findViewById(R.id.textNoMovies);
    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
    movieList = new ArrayList<>();
    adapter = new UserBookingAdapter(movieList);
    adapter.setOnItemClickListener(new UserBookingAdapter.OnItemClickListener() {
        @Override
        public void onClick(Movie movie) {
            new BookTicketFragment(movie).show(getParentFragmentManager(), "BookTicketFragment");
        }});
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    db = FirebaseFirestore.getInstance();

    fetchMovies();
      return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void fetchMovies() {
        db.collection("movies").whereEqualTo("status", "active").orderBy("createdAt", Query.Direction.DESCENDING)
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
                            movie.setId(doc.getId());
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