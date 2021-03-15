package edu.temple.langexchange;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    Button button;
    ArrayList<String> correctAnswer = new ArrayList<>();
    ArrayList<Flashcards> test = new ArrayList<>();
    String res = "";
    int indexAnswered = 0;
    ListView list;
    ArrayList<String> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_quiz);
        getSupportActionBar().setTitle(R.string.quiz_title);
        list = findViewById(R.id.quizDisplay);

        //test = new Flashcards[5];
        test.add(new Flashcards(1, "Hi", "Hola", "A Word You Use to Greet People"));
        test.add(new Flashcards(2, "One", "Uno", "Number One"));
        test.add(new Flashcards(3, "Two", "Dos", "Number Two"));
        test.add(new Flashcards(4, "Three", "Tres", "Number Three"));
        test.add(new Flashcards(5, "Four", "Cuatro", "Number Four"));

//        FlashcardAdapter adapter = new FlashcardAdapter(this, test);

        button = findViewById(R.id.quizDisplayBtn);


        for(Flashcards card : test)
        {
            questions.add(card.definition);
            correctAnswer.add(card.originalWord.toUpperCase());
        }

        System.out.println(correctAnswer);
        QuizAdapter adapter = new QuizAdapter(this, questions);
        list.setAdapter(adapter);

        int quizLength = correctAnswer.size();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuizActivity.this, QuizTaking.class);
                intent.putExtra("position", position);
                System.out.println("size of array:" + correctAnswer.size());
                indexAnswered = position;
                startActivityForResult(intent, 1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            res = data.getStringExtra("QuizAnswer");
            if(res.equals(correctAnswer.get(indexAnswered)))
            {
                System.out.println("CORRECT");
                list.getChildAt(indexAnswered).setBackgroundColor(getResources().getColor(R.color.correct_color));

            }
            else{
                System.out.println("INCORRECT");
                list.getChildAt(indexAnswered).setBackgroundColor(getResources().getColor(R.color.incorrect_color));
            }
            System.out.println(res);
        }
    }
}
