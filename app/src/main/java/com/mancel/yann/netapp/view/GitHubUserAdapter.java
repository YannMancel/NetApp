package com.mancel.yann.netapp.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.model.pojos.GitHubUser;

import java.util.List;

/**
 * Created by Yann MANCEL on 07/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.view
 */
public class GitHubUserAdapter extends RecyclerView.Adapter<GitHubUserViewHolder> {

    // INTERFACE -----------------------------------------------------------------------------------

    /**
     * Listener methods (Callback)
     */
    public interface Listener {
        void onClickDeleteButton(int position);
    }

    // FIELDS --------------------------------------------------------------------------------------

    private List<GitHubUser> mGitHubUsers;
    private RequestManager mRequestManager;
    private Listener mListener;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Initializes a GitHubUserAdapter object
     *
     * @param gitHubUsers a List<GitHubUser> object that contains the user list
     * @param requestManager a RequestManager object to connect with the Glide library
     * @param listener a listener interface for the callback
     */
    public GitHubUserAdapter(List<GitHubUser> gitHubUsers, RequestManager requestManager, Listener listener) {
        this.mGitHubUsers = gitHubUsers;
        this.mRequestManager = requestManager;
        this.mListener = listener;
    }

    // METHODS -------------------------------------------------------------------------------------

    @NonNull
    @Override
    public GitHubUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Creates a Context object to the LayoutInflater object
        Context context = viewGroup.getContext();

        // Creates an inflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        View view = layoutInflater.inflate(R.layout.fragment_main_item, viewGroup, false);

        return new GitHubUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GitHubUserViewHolder gitHubUserViewHolder, int i) {
        // Updates the item at the position i
        gitHubUserViewHolder.updateWithGitHubUser(this.mGitHubUsers.get(i), this.mRequestManager, this.mListener);
    }

    @Override
    public int getItemCount() {
        return this.mGitHubUsers.size();
    }

    /**
     * Returns a GitHubUser object thanks to the position value in argument
     *
     * @param position an integer that contains the position value in the List<GitHubUser> field
     *
     * @return a GitHubUser object
     */
    public GitHubUser getUser(int position) {
        return  this.mGitHubUsers.get(position);
    }
}
