package com.example.finalexam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private List<Model> modelList;

    public CustomAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("id", model.getId());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("description", model.getDescription());
            context.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Documents").document(model.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        modelList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, editBtn, deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            editBtn = itemView.findViewById(R.id.btn_edit);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }
    }
}

