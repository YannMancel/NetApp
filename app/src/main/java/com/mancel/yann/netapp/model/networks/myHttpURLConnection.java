package com.mancel.yann.netapp.model.networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yann MANCEL on 02/05/2019.
 * Name of the project: NetApp
 * Name of the package: com.mancel.yann.netapp.model
 */
public class myHttpURLConnection {

    // METHODS -------------------------------------------------------------------------------------

    public static String startHttRequest(String urlString) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Creates a ULR object
            URL url = new URL(urlString);

            // Creates a HttpURLConnection object [URL -> URLConnection]
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Opens a communications link to the resource referenced by this URL
            connection.connect();

            // Retrieves a InputStream abstract object (Binary flux)
            InputStream inputStream = connection.getInputStream();

            // Creates a BufferedReader object that contains the strings
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Reading and adding into the StringBuilder object
            String line;
            while ((line= bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
