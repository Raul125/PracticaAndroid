package com.raulrh.practicaandroid.ui.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ShoppinglistFragmentBinding;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingItem;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingListAdapter;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingListDB;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    private ShoppinglistFragmentBinding binding;

    private List<ShoppingItem> shoppingItems = new ArrayList<>();
    private ShoppingListAdapter adapter;
    private ShoppingListDB dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ShoppinglistFragmentBinding.inflate(inflater, container, false);

        dbHelper = new ShoppingListDB(getActivity());
        adapter = new ShoppingListAdapter(shoppingItems, dbHelper);
        loadShoppingList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonAdd.setOnClickListener(v -> {
            String productName = binding.editTextProduct.getText().toString();
            if (!productName.isEmpty()) {
                ShoppingItem newItem = new ShoppingItem(productName, R.drawable.ic_launcher_foreground);
                shoppingItems.add(newItem);
                dbHelper.addShoppingItem(newItem);
                binding.editTextProduct.setText("");
                loadShoppingList();
            }
        });

        return binding.getRoot();
    }

    private void loadShoppingList() {
        shoppingItems = dbHelper.getAllShoppingItems();
        adapter.updateList(shoppingItems);
    }
}