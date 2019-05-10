package com.mancel.yann.netapp.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.model.pojos.GitHubUserInfo;
import com.mancel.yann.netapp.model.utils.GitHubStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/*
 TODO Change the size of the ImageView on Landscape configuration
 */

/**
 * Created by Yann MANCEL on 10/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.controller.fragments
 */
public class DetailFragment extends Fragment {

    // FIELDS --------------------------------------------------------------------------------------

    // Eliminate findViewById calls by using @BindView on fields.
    @BindView(R.id.activity_detail_image) ImageView mUserImage;
    @BindView(R.id.activity_detail_username) TextView mUserName;
    @BindView(R.id.activity_detail_following_number) TextView mFollowing;
    @BindView(R.id.activity_detail_follower_number) TextView mFollower;
    @BindView(R.id.activity_detail_depository_number) TextView mDepository;

    private String mLoginUser;
    private Disposable mDisposable;

    private static String TAG = DetailFragment.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /*
     Warning: With the butterknife library, the constructor shall be empty.
              Its fields are not yet instantiated.
     */
    public DetailFragment() {
    }

    // METHODS -------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Creates the View object
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, view);

        // Retrieves the login user from the Intent object
        this.retrieveLoginUserFromIntent();

        // Launches the HTTP request thanks to ReactiveX and Retrofit
        this.launchHttpRequest();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Close correctly the stream of ReactiveX
        this.disposeWhenDestroy();
    }

    /**
     * Retrieves the login user from the Intent object
     */
    private void retrieveLoginUserFromIntent() {
        // Retrieves the Intent object from the MainFragmentWithRetrofitAndReactiveX object
        Intent intent = getActivity().getIntent();

        // Checks if the Intent object is null
        if (intent == null) {
            Toast.makeText(getContext(), getString(R.string.error_happened), Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieves the login user which has been put into the Intent object
        this.mLoginUser = intent.getStringExtra("loginUser");
    }

    // RETROFIT & REACTIVEX/RXJAVA
    /**
     * Launches the HTTP request thanks to ReactiveX and Retrofit
     */
    private void launchHttpRequest() {
        // Creates the stream of ReactiveX and execute it
        this.mDisposable = GitHubStreams.streamFetchUserInfo(this.mLoginUser)
                .subscribeWith(new DisposableObserver<GitHubUserInfo>() {

                    @Override
                    public void onNext(GitHubUserInfo gitHubUserInfo) {
                        // Updates the UI
                        updateUI(gitHubUserInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", getString(R.string.on_error, TAG, Log.getStackTraceString(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", getString(R.string.on_complete, TAG));
                    }
                });
    }

    /**
     * Closes the stream of ReactiveX
     */
    private void disposeWhenDestroy() {
        // During the destroy of the activity object
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // UPDATE UI
    /**
     * Updates the UI thanks to the GitHubUserInfo object in parameter
     *
     * @param userInfo a GitHubUserInfo object that contains all the data on the user
     */
    private void updateUI (GitHubUserInfo userInfo) {
        // ImageView
        Glide.with(this)
                .load(userInfo.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(this.mUserImage);

        // TextView
        this.mUserName.setText(userInfo.getLogin());
        this.mFollowing.setText(String.valueOf(userInfo.getFollowing()));
        this.mFollower.setText(String.valueOf(userInfo.getFollowers()));
        this.mDepository.setText(String.valueOf(userInfo.getPublicRepos()));
    }
}
