package com.raulrh.practicaandroid.ui.shoppinglist.data;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ItemShoppingBinding;
import com.raulrh.practicaandroid.ui.shoppinglist.ShoppingListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private final List<ShoppingItem> items;
    private final List<ShoppingItem> filteredShoppingItems;
    private final ShoppingListDB dbHelper;
    private final ShoppingListFragment fragment;

    public ShoppingListAdapter(List<ShoppingItem> items, ShoppingListDB dbHelper, ShoppingListFragment fragment) {
        this.items = items;
        this.filteredShoppingItems = new ArrayList<>(items);
        this.dbHelper = dbHelper;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoppingBinding binding = ItemShoppingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShoppingListAdapter.ShoppingListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingItem item = filteredShoppingItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return filteredShoppingItems.size();
    }

    public void updateList(List<ShoppingItem> newItems, String filterCategory) {
        items.clear();
        items.addAll(newItems);
        filterByCategory(filterCategory);
    }

    public void filterByCategory(String category) {
        filteredShoppingItems.clear();
        if (category == null) {
            filteredShoppingItems.addAll(items);
        } else {
            for (ShoppingItem item : items) {
                if (item.getCategory().equals(category)) {
                    filteredShoppingItems.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ShoppingItem item = filteredShoppingItems.get(position);
        dbHelper.deleteShoppingItem(item.getId());
        deleteImageFromStorage(item.getImagePath());
        filteredShoppingItems.remove(item);
        items.remove(item);
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

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private final ItemShoppingBinding binding;

        public ShoppingListViewHolder(ItemShoppingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage(fragment.getString(R.string.confirm_delete))
                            .setPositiveButton(fragment.getString(R.string.yes), (dialog, which) -> {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    deleteItem(position);
                                }
                            })
                            .setNegativeButton(fragment.getString(R.string.no), (dialog, which) -> binding.checkBox.setChecked(false))
                            .show();
                }
            });
        }

        public void bind(ShoppingItem item) {
            binding.textView.setText(item.getName());
            if (item.getImagePath() != null) {
                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath()));
            } else {
                setDefaultImage(item.getCategory(), binding.imageView);
            }

            binding.checkBox.setChecked(false);
            binding.categoryText.setText(item.getCategory());
        }

        private void setDefaultImage(String category, ImageView imageView) {
            int imageResId = R.drawable.otros;
            if (fragment.getString(R.string.meatFish).equals(category)) {
                imageResId = R.drawable.carnepescado;
            } else if (fragment.getString(R.string.dairy).equals(category)) {
                imageResId = R.drawable.lacteos;
            } else if (fragment.getString(R.string.fruitVegetables).equals(category)) {
                imageResId = R.drawable.frutasverduras;
            } else if (fragment.getString(R.string.canned).equals(category)) {
                imageResId = R.drawable.enlatados;
            } else if (fragment.getString(R.string.drinks).equals(category)) {
                imageResId = R.drawable.bebidas;
            }

            imageView.setImageResource(imageResId);
        }
    }
}