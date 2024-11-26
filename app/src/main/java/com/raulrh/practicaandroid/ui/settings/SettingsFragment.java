package com.raulrh.practicaandroid.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.SettingsFragmentBinding;
import com.raulrh.practicaandroid.util.SharedPrefsUtil;

public class SettingsFragment extends Fragment {

    private final static int MAX_ROWS = 15;
    private final static int MIN_ROWS = 5;
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
        int rows = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_ROWS, 10);
        int cols = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_COLS, 10);
        int mines = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_MINES, 15);

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
                Toast.makeText(requireContext(), getString(R.string.toast_rows_cols_positive), Toast.LENGTH_LONG).show();
                return;
            }

            if (rows > MAX_ROWS || cols > MAX_ROWS) {
                Toast.makeText(requireContext(), getString(R.string.toast_rows_cols_max, MAX_ROWS), Toast.LENGTH_LONG).show();
                return;
            }

            if (rows < MIN_ROWS || cols < MIN_ROWS) {
                Toast.makeText(requireContext(), getString(R.string.toast_rows_cols_min, MIN_ROWS), Toast.LENGTH_LONG).show();
                return;
            }

            if (mines < 0) {
                Toast.makeText(requireContext(), getString(R.string.toast_mines_negative), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mines >= rows * cols) {
                Toast.makeText(requireContext(), getString(R.string.toast_mines_less_cells), Toast.LENGTH_LONG).show();
                return;
            }

            SharedPrefsUtil.putInt(requireContext(), SharedPrefsUtil.PREF_KEY_ROWS, rows);
            SharedPrefsUtil.putInt(requireContext(), SharedPrefsUtil.PREF_KEY_COLS, cols);
            SharedPrefsUtil.putInt(requireContext(), SharedPrefsUtil.PREF_KEY_MINES, mines);

            Toast.makeText(requireContext(), getString(R.string.toast_config_saved), Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), getString(R.string.toast_invalid_numbers), Toast.LENGTH_SHORT).show();
        }
    }
}