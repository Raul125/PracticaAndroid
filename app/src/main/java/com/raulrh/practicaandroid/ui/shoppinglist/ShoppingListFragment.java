package com.raulrh.practicaandroid.ui.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ShoppinglistFragmentBinding.inflate(inflater, container, false);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        binding.buttonImage.setImageURI(selectedImageUri);
                    }
                });

        dbHelper = new ShoppingListDB(getActivity());
        adapter = new ShoppingListAdapter(shoppingItems, dbHelper);
        loadShoppingList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonImage.setOnClickListener(v -> openImagePicker());

        binding.buttonAdd.setOnClickListener(v -> {
            String productName = binding.editTextProduct.getText().toString();
            if (!productName.isEmpty() && selectedImageUri != null) {
                ShoppingItem newItem = new ShoppingItem(productName, selectedImageUri.toString());
                shoppingItems.add(newItem);
                dbHelper.addShoppingItem(newItem);
                binding.editTextProduct.setText("");
                selectedImageUri = null;
                loadShoppingList();
            }
        });

        return binding.getRoot();
    }

    private void loadShoppingList() {
        shoppingItems = dbHelper.getAllShoppingItems();
        adapter.updateList(shoppingItems);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}