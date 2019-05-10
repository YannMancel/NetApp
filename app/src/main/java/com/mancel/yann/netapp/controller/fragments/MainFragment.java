package com.mancel.yann.netapp.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.model.utils.GitHubCalls;
import com.mancel.yann.netapp.model.utils.GitHubStreams;
import com.mancel.yann.netapp.model.pojos.GitHubUser;
import com.mancel.yann.netapp.model.pojos.GitHubUserInfo;
import com.mancel.yann.netapp.model.networks.NetworkAsyncTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 01/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.controller.fragments
 */
public class MainFragment extends Fragment implements NetworkAsyncTask.Listeners, GitHubCalls.Callbacks {

    // FIELDS --------------------------------------------------------------------------------------

    // Eliminate findViewById calls by using @BindView on fields.
    @BindView(R.id.fragment_main_textview) TextView mTextView;

    private Disposable mDisposable;

    private static String TAG = MainFragment.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /*
     Warning: With the butterknife library, the constructor shall be empty.
              Its fields are not yet instantiated.
     */
    public MainFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Creates the View object
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Close correctly the stream of ReactiveX
        this.disposeWhenDestroy();
    }

    // Eliminate anonymous inner-classes for listeners by annotating methods with @OnClick and others.
    @OnClick(R.id.fragment_main_button) public void submit(View view) {
        // Executes the AsyncTask object
        //this.executeHttpRequest("https://api.github.com/users/JakeWharton/following");

        // Executes the Retrofit object
        //this.executeHttpRequestWithRetrofit("JakeWharton");

        // Execute the stream of ReactiveX
        //this.streamShowString();

        // Executes the Retrofit object with the stream of ReactiveX
        this.executeHttpRequestWithRetrofitAndReactiveX(getString(R.string.initial_username));
    }

    // --------------------------------
    // ASYNCTASK
    // --------------------------------

    @Override
    public void onPreExecute() {
        this.updateUIWhenStartingHTTPRequest();
    }

    @Override
    public void doInBackground() {}

    @Override
    public void onPostExecute(String result) {
        this.updateUIWhenEndingHTTPRequest(result);
    }

    /**
     * Executes an AsyncTask object thanks to the String value in argument
     *
     * @param url a String object that contains the URL
     */
    private void executeHttpRequest(String url) {
        new NetworkAsyncTask(this).execute(url);
    }

    // --------------------------------
    // RETROFIT
    // --------------------------------

    @Override
    public void onResponse(@Nullable List<GitHubUser> users) {
        // When getting response, we update UI
        if (users != null) {
            this.updateUIWithListOfUsers(users);
        }
    }

    @Override
    public void onFailure() {
        // When getting error, we update UI
        this.updateUIWhenEndingHTTPRequest(getString(R.string.error_happened));
    }

    /**
     * Executes an AsyncTask object thanks to the Retrofit object
     *
     * @param usename a String object that contains the usename
     */
    private void executeHttpRequestWithRetrofit(final String usename) {
        // Updates the UI
        this.updateUIWhenStartingHTTPRequest();

        // Starts the request of Retrofit object
        GitHubCalls.fetchUserFollowing(this, usename);
    }

    // --------------------------------
    // RETROFIT & REACTIVE X / RX JAVA
    // --------------------------------

    /**
     * Executes an AsyncTask object thanks to the Retrofit object
     *
     * @param usename a String object that contains the usename
     */
    private void executeHttpRequestWithRetrofitAndReactiveX(final String usename) {
        // Updates the UI
        this.updateUIWhenStartingHTTPRequest();

        // Creates the stream of ReactiveX and execute it
        /*
        this.mDisposable = GitHubStreams.streamFetchUserFollowing(usename)
                           .subscribeWith(new DisposableObserver<List<GitHubUser>>() {

                               @Override
                               public void onNext(List<GitHubUser> gitHubUsers) {
                                   // When getting response, we update UI
                                   if (gitHubUsers != null) {
                                       updateUIWithListOfUsers(gitHubUsers);
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
           */

        // Creates the stream of ReactiveX and execute it
        this.mDisposable = GitHubStreams.streamFetchUserFollowingAndFetchFirstUserInfo(usename)
                           .subscribeWith(new DisposableObserver<GitHubUserInfo>() {

                               @Override
                               public void onNext(GitHubUserInfo gitHubUserInfo) {
                                   // When getting response, we update UI
                                   if (gitHubUserInfo != null) {
                                       updateUIWithUsersInfo(gitHubUserInfo);
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

    // --------------------------------
    // REACTIVE X / RX JAVA
    // --------------------------------

    /**
     * Creates an Observable<String> object
     *
     * @return an Observable<String> object that contains a string
     */
    private Observable<String> getObservable() {
        return Observable.just("Cool !");
    }

    /**
     * Creates an DisposableObserver<String> object
     *
     * @return an DisposableObserver<String> object
     */
    private DisposableObserver<String> getSubscriber() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                // Update the TextView field
                mTextView.setText("Observable emits: " + s);

            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "on Error: " + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "on Complete!!!");
            }
        };
    }

    /**
     * Creates the stream and execute it
     */
    private void streamShowString() {
        // Creates the stream of ReactiveX and execute it
        this.mDisposable = this.getObservable()
                                .map(this.getFunctionUpperCase())
                                .flatMap(this.getSecondObservable())
                                .subscribeWith(this.getSubscriber());
    }

    /**
     * Creates a Function to the Map method of ReactiveX
     *
     * @return a Function object where the string has been modified
     */
    private Function<String, String> getFunctionUpperCase() {
        return new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        };
    }

    /**
     * Creates a Function to the FlatMap method of ReactiveX
     *
     * @return a Function object that creates an Observable object of ReactiveX and broadcasts something
     */
    private Function<String, Observable<String>> getSecondObservable() {
        return new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(String s) throws Exception {
                return Observable.just(s + " I love you !");
            }
        };
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

    // --------------------------------
    // UPDATE UI
    // --------------------------------

    /**
     * Updates the UI when the Thread is started
     */
    private void updateUIWhenStartingHTTPRequest() {
        this.mTextView.setText(getString(R.string.downloading));
    }

    /**
     * Updates the UI when the Thread is ended
     */
    private void updateUIWhenEndingHTTPRequest(String response) {
        this.mTextView.setText(response);
    }

    /**
     * Updates the UI with the user list
     *
     * @param users a List<GitHubUser> object that contains the user list
     */
    private void updateUIWithListOfUsers(List<GitHubUser> users) {
        // Creates a StringBuilder object
        StringBuilder stringBuilder = new StringBuilder();

        for (GitHubUser user : users){
            stringBuilder.append("- " + user.getLogin() + "\n");
        }

        // Updates the UI when the Thread is ended
        this.updateUIWhenEndingHTTPRequest(stringBuilder.toString());
    }

    /**
     * Updates the UI with the user list
     *
     * @param userInfo a GitHubUserInfo object that contains the user info
     */
    private void updateUIWithUsersInfo(GitHubUserInfo userInfo) {
        // Creates a String object
        String resume = "The first Following of Jake Wharthon is " + userInfo.getName() +
                        " with "+ userInfo.getFollowers() + " followers.";

        // Updates the UI when the Thread is ended
        this.updateUIWhenEndingHTTPRequest(resume);
    }
}
