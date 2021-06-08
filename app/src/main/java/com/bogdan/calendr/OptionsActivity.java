package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OptionsActivity extends AppCompatActivity {

    Button deleteButton;
    Button exportButton;
    Button importButton;
    ActivityResultLauncher<Intent> writeIntent;
    ActivityResultLauncher<Intent> readIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        deleteButton = findViewById(R.id.btn_delete_past);
        exportButton = findViewById(R.id.btn_export);
        importButton = findViewById(R.id.btn_import);

        deleteButton.setOnClickListener(v -> deletePastEvents());
        exportButton.setOnClickListener(v -> exportEvents());
        importButton.setOnClickListener(v -> importEvents());

        writeIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                assert result.getData() != null;
                try {
                    OutputStream out = getContentResolver().openOutputStream(result.getData().getData());
                    String s = "Hello";
                    out.write(s.getBytes(StandardCharsets.UTF_8));
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    assert result.getData() != null;
                    InputStream in = getContentResolver().openInputStream(result.getData().getData());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = in.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }

                    buffer.flush();
                    byte[] byteArray = buffer.toByteArray();

                    String text = new String(byteArray, StandardCharsets.UTF_8);
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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