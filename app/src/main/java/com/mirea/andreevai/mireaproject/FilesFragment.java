package com.mirea.andreevai.mireaproject;

import android.content.Context;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import com.mirea.andreevai.mireaproject.databinding.FragmentFilesBinding;

public class FilesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FragmentFilesBinding binding;

    public FilesFragment() {
        // Required empty public constructor
    }

    public static FilesFragment newInstance(String param1, String param2) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFilesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                text = binding.editTextText.getText().toString();

                String code = Code_Decode(text);
                FileOutputStream outputStream;
                try {
                    outputStream = getActivity().openFileOutput(binding.editTextFileName.getText().toString(), Context.MODE_PRIVATE);
                    outputStream.write(code.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.buttonDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                text = getTextFromFile();
                String decode = Code_Decode(text);
                FileOutputStream outputStream;
                try {
                    outputStream = getActivity().openFileOutput(binding.editTextFileName.getText().toString(), Context.MODE_PRIVATE);
                    outputStream.write(decode.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                text = getTextFromFile();
                binding.editTextTextPersonName2.setText(text);
            }
        });

        return view;
    }


    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = getActivity().openFileInput(binding.editTextFileName.getText().toString());
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;
        }
        catch (IOException ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show(); }
        }
        return null;
    }

    public String Code_Decode(String str) {
        /**
         * Функция шифрования и расшифрования.
         * Она одна, т.к. применяется операция XOR (^) с числом 17
         */
        String ss ="";
        for (int i = 0; i<str.length(); i++){
            char c = str.charAt(i);
            int k = (int) c; // ASCII-код
            k = k^17;
            c = (char) k;
            ss +=c ;
        }
        return ss;
    }

}