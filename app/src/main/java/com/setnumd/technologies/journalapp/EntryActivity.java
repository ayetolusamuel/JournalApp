package com.setnumd.technologies.journalapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.setnumd.technologies.journalapp.contracts.Journal;
import com.setnumd.technologies.journalapp.contracts.JournalEntryContract;
import com.setnumd.technologies.journalapp.database.AppDatabase;
import com.setnumd.technologies.journalapp.executor.AppExecutors;

public class EntryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView textViewUser;
    private EditText editTextTitle,editTextContent;
    private String email;
    private String EXTRA_DIARY_ID = "extra_journal_id";
    private final static int DEFAULT_DIARY_ID = -1;


    AppDatabase mdb;
    private Button savebtn;
    private int mTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        textViewUser = findViewById(R.id.userTextView);
        editTextTitle = findViewById(R.id.edt_title);
        editTextContent = findViewById(R.id.edt_content);
        savebtn = findViewById(R.id.btn_Save);


        firebaseAuth = FirebaseAuth.getInstance();

        mdb = AppDatabase.getInstance(getApplicationContext());

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    email = firebaseAuth.getCurrentUser().getDisplayName();

                    textViewUser.setText("Welcome "+email);
                } else {
                    Log.d("MainAtctivity ", "Error, Try to Login");
                }
            }
        };

//        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_)) {
//            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
//        }


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ID)) {
            savebtn.setText("Update");

                mTaskId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);
                // COMPLETED (4) Get the diskIO Executor from the instance of AppExecutors and
                // call the diskIO execute method with a new Runnable and implement its run method
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // COMPLETED (5) Use the loadTaskById method to retrieve the task with id mTaskId and
                        // assign its value to a final TaskEntry variable
                        final Journal task = mdb.diaryDao().loadDiaryById(mTaskId);
                        // COMPLETED (6) Call the populateUI method with the retrieve tasks
                        // Remember to wrap it in a call to runOnUiThread
                        // We will be able to simplify this once we learn more
                        // about Android Architecture Components
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(task);
                            }
                        });
                    }
                });
            }
        }





    //}

    private void populateUI(Journal task) {
        // COMPLETED (7) return if the task is null
        System.out.println("Task "+task);
        if (task == null) {
            return;
        }

        // COMPLETED (8) use the variable task to populate the UI
        editTextTitle.setText(task.getTitle());
        editTextContent.setText(task.getContent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

  private void insertToDb(){

      String user, title, content;
      title = editTextTitle.getText().toString();
      content = editTextContent.getText().toString();
      user = email;
        final Journal journal = new Journal(user,title,content);
      AppExecutors.getInstance().diskIO().execute(new Runnable() {
          @Override
          public void run() {
              System.out.println(mTaskId+ "\t\t"+DEFAULT_DIARY_ID);
              if (mTaskId == DEFAULT_DIARY_ID){
                  mdb.diaryDao().insertDiary(journal);
              }else{
                  journal.setId(mTaskId);
                  mdb.diaryDao().updateDiary(journal);
              }

              finish();
          }
      });


      editTextTitle.setText("");editTextContent.setText("");

  }

    public void saveToDatabaseButton(View view) {
        insertToDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}