package com.example.booksmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BooksViewActivity extends AppCompatActivity {
    ConstraintLayout layout;
    String theme;
    RecyclerView booksView;
    BooksCustomAdapter customAdapter;
    FloatingActionButton addBtn,bsearch;
    DataBaseHelper dataBaseHelper;
    ArrayList<String> ids, names, authors, dates, comments, ratings;

    ActivityResultLauncher<Intent> afterAdding = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                recreate();
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_view_activity);
        layout = findViewById(R.id.books_view_layout);
        booksView = findViewById(R.id.books_view);
        addBtn = findViewById(R.id.add_book_btn);
        bsearch=findViewById(R.id.bsearch);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            theme = bundle.getString("theme");
            if (theme.equals("sunny day")) {
                layout.setBackgroundResource(R.drawable.gk);
            } else {
                layout.setBackgroundResource(R.drawable.warmeveningbackground);
            }
        }

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BooksViewActivity.this, AddBookActivity.class);
            afterAdding.launch(intent);
        });
        bsearch.setOnClickListener(view -> {
            Intent intent=new Intent(BooksViewActivity.this,book_search.class);
            afterAdding.launch(intent);
        });

        dataBaseHelper = new DataBaseHelper(BooksViewActivity.this, "books", null, 1);
        ids = new ArrayList<>();
        names = new ArrayList<>();
        authors = new ArrayList<>();
        dates = new ArrayList<>();
        comments = new ArrayList<>();
        ratings = new ArrayList<>();
        getData();

        customAdapter = new BooksCustomAdapter(BooksViewActivity.this, this, ids, names,
                authors, dates, comments, ratings);
        booksView.setAdapter(customAdapter);
        booksView.setLayoutManager(new LinearLayoutManager(BooksViewActivity.this));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Books");
        }
    }

    void getData() {
        Cursor cursor = dataBaseHelper.readAll();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
                names.add(cursor.getString(1));
                authors.add(cursor.getString(2));
                dates.add(cursor.getString(3));
                comments.add(cursor.getString(4));
                ratings.add(cursor.getString(5));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
