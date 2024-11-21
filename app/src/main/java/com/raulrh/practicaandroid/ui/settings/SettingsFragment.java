package com.raulrh.practicaandroid.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.databinding.SettingsFragmentBinding;
import com.raulrh.practicaandroid.util.SharedPrefsUtil;

public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadConfig();

        binding.saveConfigButton.setOnClickListener(v -> saveConfig());
    }

    private void loadConfig() {
        int rows = SharedPrefsUtil.getInt(requireContext(), "minesweeper_rows", 10);
        int cols = SharedPrefsUtil.getInt(requireContext(), "minesweeper_cols", 10);
        int mines = SharedPrefsUtil.getInt(requireContext(), "minesweeper_mines", 15);

        binding.editTextRows.setText(String.valueOf(rows));
        binding.editTextCols.setText(String.valueOf(cols));
        binding.editTextMines.setText(String.valueOf(mines));
    }

    private void saveConfig() {
        try {
            int rows = Integer.parseInt(binding.editTextRows.getText().toString());
            int cols = Integer.parseInt(binding.editTextCols.getText().toString());
            int mines = Integer.parseInt(binding.editTextMines.getText().toString());

            if (rows <= 0 || cols <= 0) {
                Toast.makeText(requireContext(), "El número de filas y columnas debe ser mayor que 0", Toast.LENGTH_LONG).show();
                return;
            }

            if (rows > 30 || cols > 30) {
                Toast.makeText(requireContext(), "Máximo de 30 filas y columnas", Toast.LENGTH_LONG).show();
                return;
            }

            if (mines < 0) {
                Toast.makeText(requireContext(), "El número de minas no puede ser negativo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mines >= rows * cols) {
                Toast.makeText(requireContext(), "El número de minas debe ser menor que el número de celdas", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPrefsUtil.putInt(requireContext(), "minesweeper_rows", rows);
            SharedPrefsUtil.putInt(requireContext(), "minesweeper_cols", cols);
            SharedPrefsUtil.putInt(requireContext(), "minesweeper_mines", mines);

            Toast.makeText(requireContext(), "Configuración guardada", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Por favor, ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }
}