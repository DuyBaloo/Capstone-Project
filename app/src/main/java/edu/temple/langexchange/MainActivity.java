package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.temple.langexchange.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button button, buttonStartChat;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        buttonStartChat = findViewById(R.id.button3);
        Intent intentPrev = getIntent();
        String userName = intentPrev.getStringExtra("username");
        userId = ((MyAccount) getApplication()).getUserId();
        System.out.println("username received from login: " + userName);
        System.out.println("userId received from login: " + userId);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlashcardActivity.class);
                startActivity(intent);
            }
        });

        buttonStartChat.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, ChatRoomChoice.class);
              startActivity(intent);
          }
        });

    }
}