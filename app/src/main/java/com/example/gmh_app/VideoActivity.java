package com.example.gmh_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmh_app.Adapter.VideoAdapter;
import com.example.gmh_app.Models.VideoModel;

import java.util.List;

public class VideoActivity extends AppCompatActivity {
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoModel> videoList;
    private int currentVideoIndex = 0;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "VideoProgressPrefs";
    private static final String SECTION_COMPLETION_PREFS = "VideoCompletionPrefs";
    private String sectionKey;

    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoList = getIntent().getParcelableArrayListExtra("video_list");
        sectionKey = getIntent().getStringExtra("section_key");

        if (videoList == null || videoList.isEmpty()) {
            Toast.makeText(this, "No videos available to display.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Video list from intent is null or empty.");
            return;
        }

        // Check if section was completed
        SharedPreferences completionPrefs = getSharedPreferences(SECTION_COMPLETION_PREFS, MODE_PRIVATE);
        boolean sectionCompleted = completionPrefs.getBoolean(sectionKey, false);

        if (sectionCompleted) {
            // Reset index to show all videos from the beginning when revisiting a completed section
            currentVideoIndex = 0;
        } else {
            // Resume from last watched video
            currentVideoIndex = sharedPreferences.getInt(sectionKey, 0);
        }

        videoAdapter = new VideoAdapter(this, videoList, position -> {
            currentVideoIndex = position;
            playNextVideo();
        });

        videoRecyclerView.setAdapter(videoAdapter);
        videoRecyclerView.scrollToPosition(currentVideoIndex);

        if (!sectionCompleted) {
            // Resume from the last watched video
            currentVideoIndex = sharedPreferences.getInt(sectionKey, 0);
            videoRecyclerView.scrollToPosition(currentVideoIndex);
            playNextVideo();  // Start autoplay when section is first visited
        } else {
            // Show video list when revisiting a completed section without autoplay
            currentVideoIndex = 0;
            videoRecyclerView.scrollToPosition(currentVideoIndex);
        }


    }

    private void playNextVideo() {
        if (currentVideoIndex >= videoList.size()) {
            Toast.makeText(this, "All videos completed.", Toast.LENGTH_SHORT).show();
            markSectionAsCompleted();
            return;
        }

        VideoModel currentVideo = videoList.get(currentVideoIndex);

        if (currentVideo.isQuestion()) {
            openQuestionActivity(currentVideo);
        } else {
            openVideoPlaybackActivity(currentVideo);
        }

        // Save progress in SharedPreferences
        sharedPreferences.edit().putInt(sectionKey, currentVideoIndex).apply();

        videoAdapter.notifyDataSetChanged();
    }

    private void markSectionAsCompleted() {
        SharedPreferences completionPrefs = getSharedPreferences(SECTION_COMPLETION_PREFS, MODE_PRIVATE);
        completionPrefs.edit().putBoolean(sectionKey, true).apply();
    }

    private void openVideoPlaybackActivity(VideoModel video) {
        Intent intent = new Intent(this, VideoPlaybackActivity.class);
        intent.putExtra(VideoPlaybackActivity.EXTRA_VIDEO_URI, video.getVideoUri());
        startActivityForResult(intent, VideoPlaybackActivity.REQUEST_CODE_VIDEO_PLAYBACK);
    }

    private void openQuestionActivity(VideoModel question) {
        Intent intent = new Intent(this, getQuestionActivityClass(question.getQuestionId()));
        if (intent != null) {
            startActivityForResult(intent, VideoPlaybackActivity.REQUEST_CODE_QUESTION);
        } else {
            Toast.makeText(this, "Unexpected question ID: " + question.getQuestionId(), Toast.LENGTH_SHORT).show();
        }
    }

    private Class<?> getQuestionActivityClass(int questionId) {
        switch (questionId) {
            case VideoModel.QUESTION_1: return IntroductionActivity.class;
            case VideoModel.QUESTION_2: return AfterVideo1Activity.class;
            case VideoModel.QUESTION_3: return UserRegistrationActivity.class;
            case VideoModel.QUESTION_4: return AfterVideo2Activity.class;
            case VideoModel.QUESTION_5: return BusinessDetailsActivity.class;
            case VideoModel.QUESTION_6: return AfterVideo3Activity.class;
            case VideoModel.QUESTION_7: return BusinessPracticesActivity.class;
            case VideoModel.QUESTION_8: return AfterVideo4Activity.class;
            case VideoModel.QUESTION_9: return MoneyInflowsActivity.class;
            case VideoModel.QUESTION_10: return AfterVideo5Activity.class;
            case VideoModel.QUESTION_11: return CountingMoneyInflows.class;
            case VideoModel.QUESTION_12: return AfterVideo6Activity.class;
            case VideoModel.QUESTION_13: return CorrectMoneyInflows.class;
            case VideoModel.QUESTION_14: return AfterVideo7Activity.class;
            case VideoModel.QUESTION_15: return UsingTransactionValuesToCalculateMoneyInflows.class;
            case VideoModel.QUESTION_16: return AfterVideo8Activity.class;
            case VideoModel.QUESTION_17: return BeforeVideo9Activity.class;
            case VideoModel.QUESTION_18: return AfterVideo9Activity.class;
            case VideoModel.QUESTION_19: return BeforeVideo10Activity.class;
            case VideoModel.QUESTION_20: return AfterVideo10.class;
            case VideoModel.QUESTION_21: return BeforeVideo11Activity.class;
            case VideoModel.QUESTION_22: return AfterVideo11Activity.class;
            case VideoModel.QUESTION_23: return BeforeVideo12Activity.class;
            case VideoModel.QUESTION_24: return AfterVideo12Activity.class;
            case VideoModel.QUESTION_25: return BeforeVideo13Activity.class;
            case VideoModel.QUESTION_26: return AfterVideo13Activity.class;
            case VideoModel.QUESTION_27: return BeforeVideo14Activity.class;
            case VideoModel.QUESTION_28: return AfterVideo14Activity.class;
            case VideoModel.QUESTION_29: return BeforeVideo15Activity.class;
            case VideoModel.QUESTION_30: return AfterVideo15Activity.class;
            default:
                Log.w(TAG, "No activity found for question ID: " + questionId);
                return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentVideoIndex++;
            playNextVideo();
        }
    }
}
