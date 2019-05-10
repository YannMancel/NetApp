package com.mancel.yann.netapp.model.utils;

import android.support.annotation.Nullable;

import com.mancel.yann.netapp.model.pojos.GitHubUser;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yann MANCEL on 04/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.model
 */
public class GitHubCalls {

    // INTERFACE -----------------------------------------------------------------------------------
    /**
     * Callbacks methods (Callback)
     */
    public interface Callbacks {
        void onResponse(@Nullable List<GitHubUser> users);
        void onFailure();
    }

    // METHODS -------------------------------------------------------------------------------------

    public static void fetchUserFollowing(Callbacks callbacks, String username) {
        // Creates a WeakReference object to avoid the memory leaks
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // Gets a Retrofit instance and the related endpoint
        GitHubService service = GitHubService.retrofit.create(GitHubService.class);

        // Creates the call on Github API
        Call<List<GitHubUser>> call = service.getFollowing(username);

        // Starts the call
        call.enqueue(new Callback<List<GitHubUser>>() {
            @Override
            public void onResponse(Call<List<GitHubUser>> call, Response<List<GitHubUser>> response) {
                // Calls the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) {
                    callbacksWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubUser>> call, Throwable t) {
                // Calls the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) {
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}
