package io.phatcat.mana.view.guidedsteps;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.FragmentGuidedStepDetailsBinding;
import io.phatcat.mana.model.Step;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.utils.StringUtils;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class GuidedStepDetailsFragment extends DaggerFragment {
    private static final String TAG = GuidedStepDetailsFragment.class.getName();
    private static final String ARG_PLAYBACK_POSITION = "ARG_PLAYBACK_POSITION";
    private static final String ARG_WINDOW_INDEX= "ARG_WINDOW_INDEX";
    private static final String ARG_PLAY_WHEN_READY= "ARG_PLAY_WHEN_READY";

    @Inject ViewModelFactory viewModelFactory;
    private FragmentGuidedStepDetailsBinding binding;
    private ExoPlayer player;
    private Step step;

    private long playbackPosition;
    private int currentWindowIndex;
    private boolean playWhenReady;

    /**
     * Required empty public constructor
     */
    public GuidedStepDetailsFragment() {}

    public static GuidedStepDetailsFragment getInstance(Step step) {
        GuidedStepDetailsFragment fragment = new GuidedStepDetailsFragment();
        fragment.setArguments(BundleUtils.Builder.create().putStep(step).build());
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuidedStepDetailsBinding.inflate(inflater, container, false);

        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        step = BundleUtils.getStep(bundle);
        if (step == null) {
            Toast.makeText(requireContext(), "No step found", Toast.LENGTH_LONG).show();
            requireActivity().finish();
        }
        else {
            playbackPosition = bundle.getLong(ARG_PLAYBACK_POSITION, 0);
            currentWindowIndex = bundle.getInt(ARG_WINDOW_INDEX, 0);
            playWhenReady = bundle.getBoolean(ARG_PLAY_WHEN_READY, true);
            initViews(step);
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        BundleUtils.Builder.proxy(outState).putStep(step);

        if (player != null) {
            outState.putLong(ARG_PLAYBACK_POSITION, player.getCurrentPosition());
            outState.putInt(ARG_WINDOW_INDEX, player.getCurrentWindowIndex());
            outState.putBoolean(ARG_PLAY_WHEN_READY, player.getPlayWhenReady());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (StringUtils.isNotBlank(step.videoUrl)) {
                initExoPlayer(step.videoUrl);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && player == null) {
            if (StringUtils.isNotBlank(step.videoUrl)) {
                hideSystemUi();
                initExoPlayer(step.videoUrl);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && player != null) {
            releasePlayer();
        }
    }

    private void initViews(Step step) {
        binding.setStep(step);

        boolean hasPlayer = StringUtils.isNotBlank(step.videoUrl);
        boolean hasThumbnail = StringUtils.isNotBlank(step.thumbnailUrl);
        binding.setIsMediaPresent(hasPlayer || hasThumbnail);

        if (!hasPlayer && hasThumbnail) {
            Log.d(TAG, "No video available, falling back to thumbnail: " + step.thumbnailUrl);
            Picasso.get()
                    .load(step.thumbnailUrl)
                    .fit()
                    .into(binding.recipeImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image load successful");
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, getString(R.string.error_network), e);
                        }
                    });
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setLayoutMarginTop(View v, Float margin) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.setMargins(params.leftMargin, margin.intValue(), params.rightMargin, params.bottomMargin);
        }
    }

    private void initExoPlayer(String url) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(requireContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
        }

        binding.exoPlayer.setPlayer(player);

        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("user-agent"));
        MediaSource mediaSource = factory.createMediaSource(Uri.parse(url));

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindowIndex, playbackPosition);
        player.prepare(mediaSource, false, false);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(TAG, "ExoPlaybackException: " + error.getCause().getMessage());
                Toast.makeText(requireActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();

                TransitionManager.beginDelayedTransition(binding.exoPlayer);
                binding.exoPlayer.setVisibility(View.GONE);
            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindowIndex = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    /**
     * Used for devices running under API 24 for split window mode
     */
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.exoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
