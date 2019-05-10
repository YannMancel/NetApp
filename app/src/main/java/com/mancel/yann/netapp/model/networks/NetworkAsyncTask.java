package com.mancel.yann.netapp.model.networks;

import android.os.AsyncTask;

import com.mancel.yann.netapp.model.networks.myHttpURLConnection;

import java.lang.ref.WeakReference;

/**
 * Created by Yann MANCEL on 01/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.model
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, String> {

    // INTERFACE -----------------------------------------------------------------------------------

    /**
     * Listeners methods (Callback)
     */
    public interface Listeners {
        void onPreExecute();
        void doInBackground();
        void onPostExecute(String result);
    }

    // FIELDS --------------------------------------------------------------------------------------

    private WeakReference<Listeners> mCallback;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Initializes the NetworkAsyncTask object
     *
     * @param callback a object that implements a Listeners interface
     */
    public NetworkAsyncTask(Listeners callback) {
        this.mCallback = new WeakReference<>(callback);
    }

    // METHODS -------------------------------------------------------------------------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Calls the related callback method
        this.mCallback.get().onPreExecute();
    }

    @Override
    protected String doInBackground(String... url) {
        // Calls the related callback method
        this.mCallback.get().doInBackground();

        return myHttpURLConnection.startHttRequest(url[0]);
    }

    @Override
    protected void onPostExecute(String aLong) {
        super.onPostExecute(aLong);

        // Calls the related callback method
        this.mCallback.get().onPostExecute(aLong);
    }
}
