package com.mancel.yann.netapp;

import android.support.test.runner.AndroidJUnit4;

import com.mancel.yann.netapp.model.pojos.GitHubUser;
import com.mancel.yann.netapp.model.pojos.GitHubUserInfo;
import com.mancel.yann.netapp.model.utils.GitHubStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

//import static org.junit.Assert.*;

/**
 * Created by Yann MANCEL on 09/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp
 */
@RunWith(AndroidJUnit4.class)
public class GitHubStreamsTest {

    @Test
    public void streamFetchUserFollowingTest() throws Exception {
        // Creates an Observable object
        Observable<List<GitHubUser>> observableUsers = GitHubStreams.streamFetchUserFollowing("JakeWharton");

        // Creates an TestObserver object (Subscriber for the instrumented test)
        TestObserver<List<GitHubUser>> testObserver = new TestObserver<>();

        // Launches the Observable object in subscribing the TestObserver object
        observableUsers.subscribeWith(testObserver)
                       .assertNoErrors()
                       .assertNoTimeout()
                       .awaitTerminalEvent();

        // Retrieves the GitHub users list
        List<GitHubUser> gitHubUserList = testObserver.values().get(0);

        // Checks if the test is good or not
        assertThat("Jake Wharton follows only 12 users.",gitHubUserList.size() == 12);
    }

    @Test
    public void streamFetchUserInfoTest() throws Exception {
        // Creates an Observable object
        Observable<GitHubUserInfo> observableUserInfo = GitHubStreams.streamFetchUserInfo("JakeWharton");

        // Creates an TestObserver object (Subscriber for the instrumented test)
        TestObserver<GitHubUserInfo> testObserver = new TestObserver<>();

        // Launches the Observable object in subscribing the TestObserver object
        observableUserInfo.subscribeWith(testObserver)
                          .assertNoErrors()
                          .assertNoTimeout()
                          .awaitTerminalEvent();

        // Retrieves the GitHub user information
        GitHubUserInfo gitHubUserInfo = testObserver.values().get(0);

        // Checks if the test is good or not
        assertThat("Jake Wharton Github's ID is 66577.",gitHubUserInfo.getId() == 66577);
    }
}
