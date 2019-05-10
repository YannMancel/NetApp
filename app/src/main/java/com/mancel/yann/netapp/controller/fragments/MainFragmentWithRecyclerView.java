package com.mancel.yann.netapp.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.controller.activities.DetailActivity;
import com.mancel.yann.netapp.model.utils.GitHubStreams;
import com.mancel.yann.netapp.model.pojos.GitHubUser;
import com.mancel.yann.netapp.model.utils.ItemClickSupport;
import com.mancel.yann.netapp.view.GitHubUserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/*
 TODO Change the UI (remove the Button field)
 TODO Take in account the savedInstanceState
 */

/**
 * Created by Yann MANCEL on 07/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.controller.fragments
 */
public class MainFragmentWithRecyclerView extends Fragment implements GitHubUserAdapter.Listener {

    // FIELDS --------------------------------------------------------------------------------------

    // Eliminate findViewById calls by using @BindView on fields.
    @BindView(R.id.fragment_main_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_main_recycler_view) RecyclerView mRecyclerView;

    private Disposable mDisposable;
    private List<GitHubUser> mGitHubUsersList;
    private GitHubUserAdapter mGitHubUserAdapter;

    private static String TAG = MainFragmentWithRecyclerView.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /*
     Warning: With the butterknife library, the constructor shall be empty.
              Its fields are not yet instantiated.
     */
    public MainFragmentWithRecyclerView() {}

    // METHODS -------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Creates the View object
        View view = inflater.inflate(R.layout.fragment_main_with_recyclerview, container, false);

        ButterKnife.bind(this, view);

        // Configures the RecyclerView field
        this.configureRecyclerView();

        // Configures the SwipeRefreshLayout field
        this.configureSwipeRefreshLayout();

        // Configures the clicks of the RecyclerView field
        this.configureOnClickRecyclerView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Close correctly the stream of ReactiveX
        this.disposeWhenDestroy();
    }

    @Override
    public void onClickDeleteButton(int position) {
        // Retrieves the GitHubUser object thanks to the position value in argument
        GitHubUser gitHubUser = this.mGitHubUserAdapter.getUser(position);

        // Displays a short message
        Toast.makeText(getContext(), getString(R.string.try_to_delete_item, gitHubUser.getLogin()), Toast.LENGTH_SHORT).show();
    }

    // Eliminate anonymous inner-classes for listeners by annotating methods with @OnClick and others.
    @OnClick(R.id.fragment_main_recycler_view_button) public void submit(View view) {
        // Executes the Retrofit object with the stream of ReactiveX
        this.executeHttpRequestWithRetrofitAndReactiveX(getString(R.string.initial_username));
    }

    /**
     * Configures the RecyclerView field
     */
    private void configureRecyclerView() {
        // Reset list
        this.mGitHubUsersList = new ArrayList<>();

        // Creates adapter passing the list of users
        this.mGitHubUserAdapter = new GitHubUserAdapter(this.mGitHubUsersList, Glide.with(this), this);

        // Attaches the adapter to the RecyclerView to populate items
        this.mRecyclerView.setAdapter(this.mGitHubUserAdapter);

        // Sets layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Configures the SwipeRefreshLayout field
     */
    private void configureSwipeRefreshLayout() {
        // Creates the Listener
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Executes the Retrofit object with the stream of ReactiveX
                executeHttpRequestWithRetrofitAndReactiveX(getString(R.string.initial_username));
            }
        });
    }

    /**
     * Configures the clicks of the RecyclerView field
     */
    private void configureOnClickRecyclerView() {
        // Creates the Listeners to manage the clicks on the RecyclerView field
        ItemClickSupport.addTo(this.mRecyclerView, R.layout.fragment_main_item)
                        .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                                        // Retrieves the login of the user thanks to the item position
                                                        final String loginUser = mGitHubUserAdapter.getUser(position)
                                                                                                   .getLogin();
                                                        // Displays a short message
                                                        //Toast.makeText(getContext(), loginUser, Toast.LENGTH_SHORT).show();

                                                        // Launches the DetailActivity class
                                                        launchDetailActivity(loginUser);
                                                    }
                                                });
    }

    // RETROFIT & REACTIVEX/RXJAVA
    /**
     * Executes an AsyncTask object thanks to the Retrofit object and ReactiveX
     *
     * @param usename a String object that contains the usename
     */
    private void executeHttpRequestWithRetrofitAndReactiveX(final String usename) {
        // Updates the UI
        this.updateUIWhenStartingHTTPRequest();

        // Creates the stream of ReactiveX and execute it
        this.mDisposable = GitHubStreams.streamFetchUserFollowing(usename)
                           .subscribeWith(new DisposableObserver<List<GitHubUser>>() {

                               @Override
                               public void onNext(List<GitHubUser> gitHubUsers) {
                                   // When getting response, we update UI
                                   if (gitHubUsers != null) {
                                       updateUI(gitHubUsers);
                                   }
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
        // During the destroy of the Fragment object
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // UPDATE UI
    /**
     * Updates the UI when the Thread is started
     */
    private void updateUIWhenStartingHTTPRequest() {
        // Displays a short message
        Toast.makeText(getContext(), getString(R.string.downloading), Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the UI with the user list
     *
     * @param users a List<GitHubUser> object that contains the user list
     */
    private void updateUI(List<GitHubUser> users){
        // Stops refreshing and clear actual list of users
        this.mSwipeRefreshLayout.setRefreshing(false);

        // Updates the data
        if (!this.mGitHubUsersList.isEmpty()) {
            this.mGitHubUsersList.clear();
        }
        this.mGitHubUsersList.addAll(users);

        // Updates the UI thanks to the Adapter field related to the RecyclerView
        this.mGitHubUserAdapter.notifyDataSetChanged();
    }

    // OTHERS ACTIVITIES
    /**
     * Launches the DetailActivity class
     *
     * @param loginUser a String object that contains the login user value
     */
    private void launchDetailActivity(final String loginUser) {
        // Calls the DetailActivity class thanks to this Intent object
        Intent intent = new Intent(getContext(), DetailActivity.class);

        // Adds the loginUser to the Intent object
        intent.putExtra("loginUser", loginUser);

        // Removes all the activities of the pile until the parent activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Starts the DetailActivity
        startActivity(intent);
    }
}
