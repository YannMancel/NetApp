package com.mancel.yann.netapp.model.utils;

import com.mancel.yann.netapp.model.pojos.GitHubUser;
import com.mancel.yann.netapp.model.pojos.GitHubUserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yann MANCEL on 02/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.model
 */
public interface GitHubService {

    // Retrofit
    @GET("users/{username}/following")
    Call<List<GitHubUser>> getFollowing(@Path("username") String username);

    // Retrofit + ReactiveX
    @GET("users/{username}/following")
    Observable<List<GitHubUser>> getFollowingWithReactiveX(@Path("username") String username);

    // Retrofit + ReactiveX
    @GET("users/{username}")
    Observable<GitHubUserInfo> getUserInfoWithReactiveX(@Path("username") String username);

    // Retrofit
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Retrofit + ReactiveX
    public static final Retrofit retrofitWithReactiveX = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
