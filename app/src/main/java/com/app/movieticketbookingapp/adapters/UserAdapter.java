package com.app.movieticketbookingapp.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.movieticketbookingapp.models.User;
import com.app.movieticketbookingapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(User user);
        void onDeleteClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        UserAdapter.listener = listener;
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, email,role;
//        View buttonEdit, buttonDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            email = itemView.findViewById(R.id.textEmail);
            number=itemView.findViewById(R.id.textNumber);
//            role = itemView.findViewById(R.id.textRole);
//            buttonEdit = itemView.findViewById(R.id.buttonEdit);
//            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(User user,int position) {
            number.setText((position + 1) + ".");
            name.setText(user.getName());
            email.setText(user.getEmail());
//            role.setText(user.getRole());

//            buttonEdit.setOnClickListener(v -> {
//                if (listener != null) listener.onEditClick(user);
//            });
//
//            buttonDelete.setOnClickListener(v -> {
//                if (listener != null) listener.onDeleteClick(user);
//            });
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
