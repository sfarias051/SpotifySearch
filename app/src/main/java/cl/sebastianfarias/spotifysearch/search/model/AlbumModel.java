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
import java.util.HashMap;
import java.util.List;

import cl.sebastianfarias.spotifysearch.search.SearchActivity;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class AlbumModel implements Serializable {

    private static final long serialVersionUID = -2478013921058337852L;
    private static String TAG = AlbumModel.class.getSimpleName();
    private static AlbumModel sAlbumModel;
    private static String FILENAME = "AlbumModel.fwd";
    private HashMap<String, HashMap<String,Object>> albumList = new HashMap<>();

    private AlbumModel (){
        loadData();
    }

    public static AlbumModel getInstance(){
        if (sAlbumModel == null){
            synchronized (AlbumModel.class){
                sAlbumModel = new AlbumModel();
            }
        }
        return sAlbumModel;
    }

    private void loadData(){
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fis = SearchActivity.context.getApplicationContext().openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);

            sAlbumModel = (AlbumModel) ois.readObject();
            ois.close();
            fis.close();
            albumList = sAlbumModel.albumList;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
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
                out.writeObject(sAlbumModel);
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

    public void setAlbumList(String identify, List<Album> album) {
        HashMap<String, Object> entry = new HashMap<>();

        entry.put("list", album);
        entry.put("isRefresh", false);
        albumList.put(identify, entry);
    }

    public List<Album> getAlbumList(String key) {
        if (albumList.containsKey(key)) {
            List<Album> albums = (List<Album>) albumList.get(key).get("list");
            if (albums != null) {
                return albums;
            }
        }
        return new ArrayList<>();
    }

}
