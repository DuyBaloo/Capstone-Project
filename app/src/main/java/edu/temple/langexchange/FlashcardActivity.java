package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    TextView text;
    GridView gridView;

    Button makeFlashcardBtn;
    Button makeQuizBtn;

    List<Flashcards> flashcardList;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().setTitle(R.string.flashcard_title);


        setupBottomNavigationView();

        text = findViewById(R.id.flashcardText);
        gridView = findViewById(R.id.flashcardGrid);

        makeFlashcardBtn = findViewById(R.id.createFlashcardBtn);
        makeQuizBtn = findViewById(R.id.createQuizBtn);

        text.setText(R.string.flashcard_instructions);

        flashcardList = new ArrayList<>();

        // access the database
        ref = FirebaseDatabase.getInstance().getReference().child("Flashcards");

        int userId = ((MyAccount) getApplication()).getUserId();

        // listen for changes on db
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flashcardList.clear();

                // this is used to populate our array of flashcards
                for (DataSnapshot flashcardData : snapshot.getChildren()) {
                    Flashcards flashcard = flashcardData.getValue(Flashcards.class);

                    if (flashcard.id == userId) {
                        flashcardList.add(flashcard);
                    }
                }

                // custom adapter used to show our data to users
                FlashcardAdapter adapter = new FlashcardAdapter(FlashcardActivity.this, flashcardList);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlashcardActivity.this, DisplayFlashcard.class);
                intent.putExtra("original", flashcardList.get(position).originalWord);
                intent.putExtra("translation", flashcardList.get(position).translatedWord);
                intent.putExtra("definition", flashcardList.get(position).definition);
                startActivity(intent);
            }
        });

        makeFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashcardActivity.this, MakeFlashcard.class);
                startActivityForResult(intent, 1);
            }
        });

        makeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashcardActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(FlashcardActivity.this, "Added flashcard to database", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navBar);
        BottomNavigationHelper.enableNavigation(FlashcardActivity.this, bottomNavigationViewEx);

        // BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch ((item.getItemId())){
            case R.id.ic_logout:{
                ((MyAccount) getApplication()).setUserId(-1);
                ((MyAccount) getApplication()).setUsername("");
                ((MyAccount) getApplication()).setPrefLang("");
                Intent intent = new Intent(FlashcardActivity.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(FlashcardActivity.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }


}
