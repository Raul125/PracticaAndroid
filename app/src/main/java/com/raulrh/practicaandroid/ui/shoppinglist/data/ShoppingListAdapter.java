package com.raulrh.practicaandroid.ui.shoppinglist.data;

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
        private final CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    int position = getAdapterPosition();
                    deleteItem(position);
                }
            });
        }

        public void bind(ShoppingItem item) {
            textView.setText(item.getName());
            if (item.getImagePath() != null) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath()));
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }

            checkBox.setChecked(false);
        }
    }
}