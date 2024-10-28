package com.raulrh.practicaandroid.ui.shoppinglist.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<ShoppingItem> items;
    private ShoppingListDB dbHelper;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Eliminar el elemento de la base de datos y la lista
                    int position = getAdapterPosition();
                    dbHelper.deleteShoppingItem(items.get(position).getId());
                    items.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

        public void bind(ShoppingItem item) {
            textView.setText(item.getName());
            imageView.setImageResource(item.getImageResId());
            checkBox.setChecked(false);
        }
    }
}
