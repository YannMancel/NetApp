package com.mancel.yann.netapp.model.utils;

import com.mancel.yann.netapp.model.pojos.GitHubUser;
import com.mancel.yann.netapp.model.pojos.GitHubUserInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yann MANCEL on 07/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.model
 */
public class GitHubStreams {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Creates a HTTP request with Retrofit and ReactiveX
     *
     * @param username a String object that contains the value asked in argument of the HTTP request
     *
     * @return an Observable object that containt the response of the HTTP request
     */
    public static Observable<List<GitHubUser>> streamFetchUserFollowing(String username) {
        // Gets a Retrofit instance and the related endpoint
        GitHubService gitHubService = GitHubService.retrofitWithReactiveX.create(GitHubService.class);

        return gitHubService.getFollowingWithReactiveX(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    /**
     * Creates a HTTP request with Retrofit and ReactiveX
     *
     * @param username a String object that contains the value asked in argument of the HTTP request
     *
     * @return an Observable object that containt the response of the HTTP request
     */
    public static Observable<GitHubUserInfo> streamFetchUserInfo(String username) {
        // Gets a Retrofit instance and the related endpoint
        GitHubService gitHubService = GitHubService.retrofitWithReactiveX.create(GitHubService.class);

        return gitHubService.getUserInfoWithReactiveX(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    /**
     * Creates HTTP requests with Retrofit and ReactiveX
     *
     * @param username a String object that contains the value asked in argument of the HTTP request
     *
     * @return an Observable object that containt the response of the HTTP request
     */
    public static Observable<GitHubUserInfo> streamFetchUserFollowingAndFetchFirstUserInfo(String username) {
        return streamFetchUserFollowing(username)
                .map(new Function<List<GitHubUser>, GitHubUser>() {

                         @Override
                         public GitHubUser apply(List<GitHubUser> gitHubUsers) throws Exception {
                             return gitHubUsers.get(0);
                         }
                     })
                .flatMap(new Function<GitHubUser, Observable<GitHubUserInfo>>() {

                             @Override
                             public Observable<GitHubUserInfo> apply(GitHubUser gitHubUser) throws Exception {
                                 return streamFetchUserInfo(gitHubUser.getLogin());
                             }
                         });
    }
}
