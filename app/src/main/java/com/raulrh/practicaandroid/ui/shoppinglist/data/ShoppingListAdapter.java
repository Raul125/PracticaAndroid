package com.raulrh.practicaandroid.ui.shoppinglist.data;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;

import java.io.File;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private final List<ShoppingItem> items;
    private final ShoppingListDB dbHelper;

    public ShoppingListAdapter(List<ShoppingItem> items, ShoppingListDB dbHelper) {
        this.items = items;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<ShoppingItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ShoppingItem item = items.get(position);
        dbHelper.deleteShoppingItem(item.getId());
        deleteImageFromStorage(item.getImagePath());
        items.remove(position);
        notifyItemRemoved(position);
    }

    private void deleteImageFromStorage(String imagePath) {
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        private final TextView category;
        private final CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);
            category = itemView.findViewById(R.id.categoryText);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("¿Estás seguro de que quieres eliminar este elemento de la lista?")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    deleteItem(position);
                                }
                            })
                            .setNegativeButton("No", (dialog, which) -> checkBox.setChecked(false))
                            .show();
                }
            });
        }

        public void bind(ShoppingItem item) {
            textView.setText(item.getName());
            if (item.getImagePath() != null) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath()));
            } else {
                setDefaultImage(item.getCategory(), imageView);
            }

            checkBox.setChecked(false);
            category.setText(item.getCategory());
        }

        private void setDefaultImage(String category, ImageView imageView) {
            int imageResId = R.drawable.carrito; // Default image
            switch (category) {
                case "Frutas y Verduras":
                    imageResId = R.drawable.home;
                    break;
                case "Carnes y Pescados":
                    imageResId = R.drawable.home;
                    break;
                case "Lácteos":
                    imageResId = R.drawable.home;
                    break;
                case "Enlatados":
                    imageResId = R.drawable.home;
                    break;
                case "Bebidas":
                    imageResId = R.drawable.home;
                    break;
                case "Aceites y Condimentos":
                    imageResId = R.drawable.home;
                    break;
            }
            imageView.setImageResource(imageResId);
        }
    }
}