package com.bogdan.calendr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.io.IOUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OptionsActivity extends AppCompatActivity {

    Button deleteButton;
    Button exportButton;
    Button importButton;
    ActivityResultLauncher<Intent> writeIntent;
    ActivityResultLauncher<Intent> readIntent;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        deleteButton = findViewById(R.id.btn_delete_past);
        exportButton = findViewById(R.id.btn_export);
        importButton = findViewById(R.id.btn_import);
        alertBuilder = new AlertDialog.Builder(this);

        deleteButton.setOnClickListener(v -> deletePastEvents());
        exportButton.setOnClickListener(v -> exportEvents());
        importButton.setOnClickListener(v -> importEvents());

        writeIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getData().getData() != null) {
                    alertBuilder.setTitle("Save this key");
                    final EditText input = new EditText(getApplicationContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    String key = getRandomString();
                    input.setText(key);
                    alertBuilder.setView(input);

                    alertBuilder.setPositiveButton("OK", (dialog, which) -> {
                        try {
                            writeToFile(result.getData().getData(), key);
                            Toast.makeText(getApplicationContext(), "Backup Saved", Toast.LENGTH_SHORT).show();
                        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException e) {
                            e.printStackTrace();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    alertBuilder.show();
                }
            }
        });
        readIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getData().getData() != null) {
                    alertBuilder.setTitle("Enter key");
                    final EditText input = new EditText(getApplicationContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertBuilder.setView(input);

                    alertBuilder.setPositiveButton("OK", (dialog, which) -> {
                        try {
                            readFromFile(result.getData().getData(), input.getText().toString());
                            System.exit(0);
                        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException e) {
                            Toast.makeText(getApplicationContext(), "Invalid Key", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    alertBuilder.show();
                }
            }
        });
    }

    private String getRandomString() {
        Random random = new SecureRandom();
        byte[] r = new byte[16];
        random.nextBytes(r);
        return new BigInteger(1, r).toString(16);
    }

    private void writeToFile(Uri uri, String key) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        OutputStream out = getContentResolver().openOutputStream(uri);
        File database = getApplicationContext().getDatabasePath(AppDatabase.name);
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII),"AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
        if (database.exists()) {
            IOUtils.copy(database.toURI().toURL(),cipherOut);
        }
        out.close();
    }

    private void readFromFile(Uri uri, String key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        InputStream in = getContentResolver().openInputStream(uri);
        File database = getApplicationContext().getDatabasePath(AppDatabase.name);
        FileOutputStream out = new FileOutputStream(database);
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII),"AES");
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        CipherInputStream cipherIn = new CipherInputStream(in, cipher);
        IOUtils.copy(cipherIn,out);
        in.close();
    }

    private void deletePastEvents() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MainActivity.db.eventDao().deleteEventsBefore(c);
                finish();
            }
        });
    }

    private void exportEvents() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        writeIntent.launch(intent);
    }

    private void importEvents() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        readIntent.launch(intent);
    }
}