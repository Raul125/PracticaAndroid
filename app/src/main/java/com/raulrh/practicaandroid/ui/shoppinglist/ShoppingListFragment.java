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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private String selectedCategory;
    private String selectedImagePath;
    private String filterCategory;

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
                            selectedImagePath = saveImageToStorage(selectedImage);
                        } catch (IOException e) {
                            Toast.makeText(getContext(), getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        dbHelper = new ShoppingListDB(getActivity());
        adapter = new ShoppingListAdapter(shoppingItems, dbHelper, this);
        loadShoppingList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        setupSpinners();

        binding.buttonImage.setOnClickListener(v -> openImagePicker());

        binding.buttonAdd.setOnClickListener(v -> {
            String productName = binding.editTextProduct.getText().toString().trim();
            if (!productName.isEmpty() && selectedCategory != null) {
                String imagePath = selectedImagePath != null ? selectedImagePath : null;
                ShoppingItem newItem = new ShoppingItem(productName, imagePath, selectedCategory);
                shoppingItems.add(newItem);
                dbHelper.addShoppingItem(newItem);
                binding.editTextProduct.setText("");
                selectedImage = null;
                selectedImagePath = null;
                setDefaultImage(selectedCategory);
                loadShoppingList();
            } else {
                Toast.makeText(getContext(), getString(R.string.insert_name), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapterSpinner);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                setDefaultImage(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });

        ArrayAdapter<CharSequence> adapterFilterSpinner = ArrayAdapter.createFromResource(requireContext(),
                R.array.filter_categories, android.R.layout.simple_spinner_item);
        adapterFilterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilterCategory.setAdapter(adapterFilterSpinner);
        binding.spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCategory = parent.getItemAtPosition(position).toString();
                if (filterCategory.equals(getString(R.string.all_categories))) {
                    filterCategory = null;
                }

                adapter.filterByCategory(filterCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.filterByCategory(null);
            }
        });
    }

    private String saveImageToStorage(Bitmap image) {
        String imagePath = null;
        File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            imagePath = file.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(getContext(), getString(R.string.error_saving_image), Toast.LENGTH_SHORT).show();
        }

        return imagePath;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadShoppingList() {
        shoppingItems = dbHelper.getAllShoppingItems();
        adapter.updateList(shoppingItems, filterCategory);
    }

    private void setDefaultImage(String category) {
        int imageResId = R.drawable.otros;
        if (getString(R.string.meatFish).equals(category)) {
            imageResId = R.drawable.carnepescado;
        } else if (getString(R.string.dairy).equals(category)) {
            imageResId = R.drawable.lacteos;
        } else if (getString(R.string.fruitVegetables).equals(category)) {
            imageResId = R.drawable.frutasverduras;
        } else if (getString(R.string.canned).equals(category)) {
            imageResId = R.drawable.enlatados;
        } else if (getString(R.string.drinks).equals(category)) {
            imageResId = R.drawable.bebidas;
        }

        binding.buttonImage.setImageResource(imageResId);
        selectedImagePath = null;
    }
}