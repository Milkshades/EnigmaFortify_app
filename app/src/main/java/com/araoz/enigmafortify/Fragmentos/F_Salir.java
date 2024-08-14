package com.araoz.enigmafortify.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.araoz.enigmafortify.MainActivity;
import com.araoz.enigmafortify.R;

public class F_Salir extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f_salir, container, false);

        Button buttonSalir = view.findViewById(R.id.button_salir);
        buttonSalir.setOnClickListener(v -> {
            // Intent to go back to MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish(); // Close the current activity
        });

        return view;
    }
}
