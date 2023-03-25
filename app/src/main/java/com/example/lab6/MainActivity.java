package com.example.lab6;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private MyAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv);

        if (savedInstanceState != null) {
            mTask = (MyAsyncTask) getLastCustomNonConfigurationInstance();
            if (mTask != null) {
                mTask.attach(this);
            }
        }

        if (mTask == null) {
            mTask = new MyAsyncTask(this);
            mTask.execute();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        mTask.detach();
        return mTask;
    }

    private static class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        private WeakReference<MainActivity> mActivityRef;

        public MyAsyncTask(MainActivity activity) {
            attach(activity);
        }

        public void attach(MainActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        public void detach() {
            mActivityRef.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Do background work here and periodically call publishProgress
            for (int i = 0; i < 100; i++) {
                if (isCancelled()) {
                    break;
                }
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            MainActivity activity = mActivityRef.get();
            if (activity != null) {
                activity.mTextView.setText(String.valueOf(values[0]));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity activity = mActivityRef.get();
            if (activity != null) {
                Toast.makeText(activity, "Task completed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



