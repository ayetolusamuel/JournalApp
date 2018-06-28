package com.setnumd.technologies.journalapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.setnumd.technologies.journalapp.contracts.Journal;
import com.setnumd.technologies.journalapp.database.AppDatabase;
import com.setnumd.technologies.journalapp.executor.AppExecutors;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
   // private FirebaseAuth firebaseAuth;
  //  private FirebaseAuth.AuthStateListener authStateListener;
    private TextView textViewUser;
    private EditText editTextTitle,editTextContent;
    private String email;
    public static final String DEFAULT_JOURNAL_VALUE = "journal_id";
    private String EXTRA_DIARY_ID = "extra_diary_id";
    private final static int DEFAULT_TASK_ID = -1;


    private Button savebtn;
    private int mTaskId = DEFAULT_TASK_ID;

    private String INSTANCE_TASK_ID = "instance_task_id";
    private AppDatabase database;
    public static String EXTRA_TASK_ID = "extra_task_id";
   // private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    //private ImageView imageView;
   // private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        viewConfig();

database = AppDatabase.getInstance(getApplicationContext());

            if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
                mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
            }



       // firebaseAuth = FirebaseAuth.getInstance();

       if (MainActivity.userName != null){
           textViewUser.setText("Welcome "+MainActivity.userName);
       }

            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
               savebtn.setText("update");
                if (mTaskId == DEFAULT_TASK_ID) {
                    // populate the UI
                    // COMPLETED (3) Assign the value of EXTRA_TASK_ID in the intent to mTaskId
                    // Use DEFAULT_TASK_ID as the default
                    mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                    // COMPLETED (4) Get the diskIO Executor from the instance of AppExecutors and
                    // call the diskIO execute method with a new Runnable and implement its run method
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            // COMPLETED (5) Use the loadTaskById method to retrieve the task with id mTaskId and
                            // assign its value to a final TaskEntry variable
                            final Journal task = database.diaryDao().loadDiaryById(mTaskId);
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



            
        }
    private void viewConfig() {
        textViewUser = findViewById(R.id.userTextView);
        editTextTitle = findViewById(R.id.edt_title);
        editTextContent = findViewById(R.id.edt_content);
        savebtn = findViewById(R.id.btn_Save);
        relativeLayout = findViewById(R.id.relativeLayout);
        //imageView = findViewById(R.id.imageview);
        relativeLayout.setOnClickListener(this);
        //imageView.setOnClickListener(this);
       // savebtn.setOnClickListener(this);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
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


  private void insertToDb() {

      String user, title, content;
      title = editTextTitle.getText().toString();
      content = editTextContent.getText().toString();
      user = email;
      if (!title.matches("") && !content.matches("")) {
          final Journal journal = new Journal(user, title, content);
          AppExecutors.getInstance().diskIO().execute(new Runnable() {
              @Override
              public void run() {
                  System.out.println("id " + mTaskId + "\t" + DEFAULT_TASK_ID);
                  if (mTaskId == DEFAULT_TASK_ID) {
                      // insert new task
                      database.diaryDao().insertDiary(journal);
                  } else {
                      //update task
                      journal.setId(mTaskId);
                      database.diaryDao().updateDiary(journal);
                  }
                  finish();

              }
          });


      } else {
          System.out.println("Fill neccessary fields..");
      }
  }
    public void saveToDatabaseButton(View view) {
        insertToDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.relativeLayout){

            InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==event.ACTION_DOWN){
            System.out.println("clicked");
            saveToDatabaseButton(v);
        }

        return false;
    }
}