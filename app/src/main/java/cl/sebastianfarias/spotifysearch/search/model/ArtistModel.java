package cl.sebastianfarias.spotifysearch.search.model;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cl.sebastianfarias.spotifysearch.search.SearchActivity;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class ArtistModel implements Serializable {

    private static final long serialVersionUID = -2478013921058337852L;
    private static String TAG = ArtistModel.class.getSimpleName();
    private static ArtistModel sArtistModel;
    private static String FILENAME = "ArtistModel.fwd";
    private HashMap<String, HashMap<String,Object>> artistList = new HashMap<>();

    private ArtistModel (){
        loadData();
    }

    public static ArtistModel getInstance(){
        if (sArtistModel == null){
            synchronized (ArtistModel.class){
                sArtistModel = new ArtistModel();
            }
        }
        return sArtistModel;
    }

    private void loadData(){
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fis = SearchActivity.context.getApplicationContext().openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);

            sArtistModel = (ArtistModel) ois.readObject();
            ois.close();
            fis.close();
            artistList = sArtistModel.artistList;

        } catch (FileNotFoundException e) {
            Log.e(TAG,"FileNotFoundException");
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            Log.e(TAG,"StreamCorruptedException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG,"IOException");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e(TAG,"ClassNotFoundException");
            e.printStackTrace();
        }
    }

    public void persistData(){
        FileOutputStream fos;
        ObjectOutputStream out;

        try {
            fos = SearchActivity.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            try {
                out = new ObjectOutputStream(fos);
                out.writeObject(sArtistModel);
                out.close();
            } catch (IOException e) {
                Log.e(TAG,"IOException");
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG,"FileNotFoundException");
            e.printStackTrace();
        }
    }

    public void setArtistList(String identify, List<Artist> artist) {
        HashMap<String, Object> entry = new HashMap<>();

        entry.put("list", artist);
        entry.put("isRefresh", false);
        artistList.put(identify, entry);
    }

    public List<Artist> getArtistList(String key) {
        if (artistList.containsKey(key)){
            List<Artist> artists = (List<Artist>) artistList.get(key).get("list");
            if (artists != null) {
                return artists;
            }
        }
        return new ArrayList<>();
    }
}
