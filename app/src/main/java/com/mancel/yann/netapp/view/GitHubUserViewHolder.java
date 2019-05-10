package com.mancel.yann.netapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.model.pojos.GitHubUser;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 07/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.view
 */
public class GitHubUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // FIELDS --------------------------------------------------------------------------------------

    // Eliminate findViewById calls by using @BindView on fields.
    @BindView(R.id.fragment_main_item_image) ImageView mImageUser;
    @BindView(R.id.fragment_main_item_name_user) TextView mNameUser;
    @BindView(R.id.fragment_main_item_url_user) TextView mURLUser;
    @BindView(R.id.fragment_main_item_delete_button) ImageButton mImageButton;

    private WeakReference<GitHubUserAdapter.Listener> mListenerWeakReference;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /*
     Warning: With the butterknife library, the constructor shall be empty.
              Its fields are not yet instantiated.
     */
    public GitHubUserViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Updates with a GitHubUser object
     *
     * @param gitHubUser a GitHubUser object that contains the user data
     * @param glide a RequestManager object to connect with the Glide library
     * @param callback a GitHubUserAdapter.Listener interface for the callback
     */
    public void updateWithGitHubUser(GitHubUser gitHubUser, RequestManager glide, GitHubUserAdapter.Listener callback) {
        // Updates the ImageView field
        glide.load(gitHubUser.getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(this.mImageUser);

        // Updates the TextView fields
        this.mNameUser.setText(gitHubUser.getLogin());
        this.mURLUser.setText(gitHubUser.getHtmlUrl());

        // Adds a listener to the ImageButton field (the whole class)
        this.mImageButton.setOnClickListener(this);

        // Initializes the WeakReference<GitHubUserAdapter.Listener> field to use the callback method
        this.mListenerWeakReference = new WeakReference<>(callback);
    }

    @Override
    public void onClick(View v) {
        // Calls the related Listener method (callback)
        GitHubUserAdapter.Listener callback = this.mListenerWeakReference.get();

        if (callback != null) {
            callback.onClickDeleteButton(getAdapterPosition());
        }
    }
}
