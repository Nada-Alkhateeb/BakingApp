package com.example.baking.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.baking.Adapter.StepsAdapter;
import com.example.baking.R;
import com.example.baking.model.Recipe;
import com.example.baking.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener

        {

    private TextView stepDescription;
    private Button bStepNext;
    private Button bStepPrev;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;


    private Recipe recipe;
    private Step step;
    private List<Step> stepArrayList;


    private int mStepIndex;

    long playBackPosition;
    boolean playWhenReady;
    int currentWindow;

    private String videoUrl;
    private String description;
    private String thumbnailUrl;


            @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail,container,false);

        stepDescription = view.findViewById(R.id.description);
        bStepPrev = view.findViewById(R.id.previous_step);
        bStepNext = view.findViewById(R.id.next_step);
        mPlayerView = view.findViewById(R.id.exoplayer);


        Step steep=getArguments().getParcelable("step");




        recipe=getArguments().getParcelable("Recipe");

        step=getArguments().getParcelable("step");

        mStepIndex=recipe.getSteps().indexOf(step);

        stepArrayList=getArguments().getParcelableArrayList("ArrayList");

        if(!(step==null) && step.getId() != 0){
            stepDescription.setText(step.getDescription());
            setTitleWithStep(step);
            mStepIndex=step.getId();
        }else if (!(recipe == null)){
            bStepPrev.setVisibility(View.INVISIBLE);
            stepDescription.setText(recipe.getSteps().get(0).getDescription());
            initializePlayer(Uri.parse(recipe.getSteps().get(0).getVideoURL()));
            mStepIndex=0;
            //mMediaSession.setActive(true);
            }

                bStepNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mExoPlayer != null) {
                            mExoPlayer.stop();
                        }
                        Log.i("TEST", "onLoadingChanged 0000: " + mStepIndex + "");
                        if (mStepIndex < recipe.getSteps().size() - 1) {
                            mStepIndex++;
                            if (mStepIndex==0){
                                bStepPrev.setVisibility(View.INVISIBLE);
                            } else if (mStepIndex == 1){
                                bStepPrev.setVisibility(View.VISIBLE);
                            }
                            if (bStepNext.getVisibility() == View.INVISIBLE){
                                bStepNext.setVisibility(View.VISIBLE);
                            }
                            setTitleWithStep(recipe.getSteps().get(mStepIndex));
                        } else {
                            bStepNext.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                bStepPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mExoPlayer != null) {
                            mExoPlayer.stop();
                        }

                        if (mStepIndex > 0) {
                            mStepIndex--;
                            if (bStepNext.getVisibility() == View.INVISIBLE && mStepIndex<6){
                                bStepNext.setVisibility(View.VISIBLE);
                            }
                            if (mStepIndex==0 && bStepNext.getVisibility() == View.INVISIBLE){
                                bStepPrev.setVisibility(View.INVISIBLE);
                                bStepNext.setVisibility(View.VISIBLE);}
                            if (mStepIndex==0){
                                bStepPrev.setVisibility(View.INVISIBLE);
                            }
                            setTitleWithStep(recipe.getSteps().get(mStepIndex));

                        } else {
                            bStepPrev.setVisibility(View.INVISIBLE);

                        }



                    }
                });

        return view;
    }


            private void initializePlayer(Uri mediaUri) {

                if (mExoPlayer == null) {

                    TrackSelector trackSelector = new DefaultTrackSelector();
                    LoadControl loadControl = new DefaultLoadControl();
                    mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                    mPlayerView.setPlayer(mExoPlayer);

                    mExoPlayer.setPlayWhenReady(playWhenReady);
                    mExoPlayer.seekTo(currentWindow, playBackPosition);
                    mExoPlayer.addListener(this);

                    String userAgent = Util.getUserAgent(getContext(), "Baking");
                    MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory
                            (getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                    mExoPlayer.prepare(mediaSource);
                    mExoPlayer.setPlayWhenReady(true);

                }

            }



            void reset() {
                playBackPosition = 0;
                currentWindow = 0;
                playWhenReady = false;
            }



     private void setTitleWithStep(Step index) {
         videoUrl = index.getVideoURL();
         description = index.getDescription();
         thumbnailUrl = index.getThumbnailURL();

         Log.i("TEST", "onLoadingChanged: " + index.getVideoURL() + "");

         if (description != null && !description.isEmpty()) {
             stepDescription.setText(description);
         }
         if (videoUrl != null && !videoUrl.isEmpty()) {
             Log.i("TEST", "onLoadingChanged: " + index.getVideoURL() + "");
             mPlayerView.setVisibility(View.VISIBLE);
             releasePlayer();
             initializePlayer(Uri.parse(videoUrl));

         } else {
             mPlayerView.setVisibility(View.GONE);
         }
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            private void releasePlayer() {
                if (mExoPlayer != null) {

                    reset();
                    mExoPlayer.release();
                    mExoPlayer = null;

                }
            }

            @Override
            public void onPause() {
                super.onPause();
                releasePlayer();
            }

            @Override
            public void onStop() {
                super.onStop();
                releasePlayer();
            }

            @Override
            public void onDestroyView() {
                super.onDestroyView();
                releasePlayer();
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }



            public interface StepFragmentListener {
        void onStepFragmentListener(Bundle bundle);
    }


}
