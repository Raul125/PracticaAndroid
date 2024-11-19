package com.raulrh.practicaandroid.ui.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ShoppinglistFragmentBinding;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingItem;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingListAdapter;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingListDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    private ShoppinglistFragmentBinding binding;
    private List<ShoppingItem> shoppingItems = new ArrayList<>();
    private ShoppingListAdapter adapter;
    private ShoppingListDB dbHelper;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Bitmap selectedImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ShoppinglistFragmentBinding.inflate(inflater, container, false);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        binding.buttonImage.setImageURI(selectedImageUri);
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        dbHelper = new ShoppingListDB(getActivity());
        adapter = new ShoppingListAdapter(shoppingItems, dbHelper);
        loadShoppingList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonImage.setOnClickListener(v -> openImagePicker());

        binding.buttonAdd.setOnClickListener(v -> {
            String productName = binding.editTextProduct.getText().toString().trim();
            if (!productName.isEmpty()) {
                String imagePath = selectedImage != null ? saveImageToStorage(selectedImage) : null;
                ShoppingItem newItem = new ShoppingItem(productName, imagePath);
                shoppingItems.add(newItem);
                dbHelper.addShoppingItem(newItem);
                binding.editTextProduct.setText("");
                selectedImage = null;
                binding.buttonImage.setImageResource(R.drawable.carrito);
                loadShoppingList();
            } else {
                Toast.makeText(getContext(), "Debes ingresar un nombre para el producto", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private String saveImageToStorage(Bitmap image) {
        String imagePath = null;
        try {
            File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            imagePath = file.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imagePath;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadShoppingList() {
        shoppingItems = dbHelper.getAllShoppingItems();
        adapter.updateList(shoppingItems);
        adapter.notifyDataSetChanged();
    }
}