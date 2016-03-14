package cl.sebastianfarias.spotifysearch.search;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Album;
import cl.sebastianfarias.spotifysearch.search.model.AlbumModel;
import cl.sebastianfarias.spotifysearch.search.model.Artist;


/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchTask extends AsyncTask<String, Void, String>  {
    public final String LOG_TAG = SearchTask.class.getSimpleName();
    private Fragment fragment;
    private Context mContext;
    private ProgressDialog dialog;
    private String action;
    private String param1;
    private String keyPersist;

    private static final String USER_AGENT = "Mozilla/5.0";

    public SearchTask(Context mContext) {
        this.mContext = mContext;
        dialog = new ProgressDialog(mContext);
    }

    @Override
    protected String doInBackground(String... params) {
        Uri builtUri = null;
        switch (action) {
            case SearchConstants.ACTION_SEARCH:
                //CALL TO SEARCH ARTIST
                builtUri = Uri.parse(SearchConstants.SEARCH_ITEM_API).buildUpon()
                        .appendQueryParameter(SearchConstants.QUERY_Q, param1)
                        .appendQueryParameter(SearchConstants.QUERY_TYPE, "artist")
                        .build();
                break;
            case SearchConstants.ACTION_ALBUM:
                builtUri = Uri.parse(SearchConstants.GET_ALBUMS_API.concat(param1).concat("/albums"));
                break;
            case SearchConstants.ACTION_FULL_ALBUM:
                builtUri = Uri.parse(SearchConstants.GET_ALBUM_YEAR_TRACK_API.concat(param1));
                break;
            default:
                break;
        }


        try {
            Log.d(LOG_TAG,"URL  : "+builtUri);
            URL url = new URL(builtUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("USER-AGENT", "Mozilla/5.0");

            urlConnection.connect();
            int responseCode    = 0;
            responseCode        = urlConnection.getResponseCode();
            Log.d(LOG_TAG,"RESPONSE CODE : "+urlConnection.getResponseCode());
            if (responseCode == 200) {
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                Log.d(LOG_TAG, "RESPONSE   : " + buffer.toString());
                return buffer.toString();
            } else {
                return "NOK";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.setMessage(mContext.getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(String responseObject) {
        Log.d(LOG_TAG, "onPostExecute  : ");
        if (responseObject != null) {
            switch (action) {
                case SearchConstants.ACTION_SEARCH:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    try {
                        JSONObject response = new JSONObject(responseObject);
                        JSONObject artist = new JSONObject();
                        JSONArray items = new JSONArray();
                        if (responseObject.equalsIgnoreCase("NOK")) {
                            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.has("artists")) {
                                artist = response.getJSONObject("artists");
                                if (artist.has("items")) {
                                    items = artist.getJSONArray("items");
                                }
                            }

                            List<Artist> artistList = getListArtist(items);
                            SearchActivity activity = (SearchActivity) mContext;
                            FragmentManager fm = activity.getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            SearchResultFragment searchResultFragmentdListFragment = new SearchResultFragment();
                            searchResultFragmentdListFragment.listArtist = artistList;
                            ft.replace(R.id.main_container, searchResultFragmentdListFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case SearchConstants.ACTION_ALBUM:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    try {
                        JSONObject response = new JSONObject(responseObject);
                        JSONArray items = new JSONArray();
                        if (responseObject.equalsIgnoreCase("NOK")) {
                            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.has("items")) {
                                items = response.getJSONArray("items");
                            }

                            String tempIds = "";
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject artistObj = null;
                                try {
                                    artistObj = items.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String id = "";
                                id = artistObj.getString("id") + ",";

                                tempIds = tempIds + id;
                            }
                            tempIds = tempIds.substring(0, tempIds.length() - 1);

                            SearchArtistAlbumFragment.searchFullInfo(tempIds, keyPersist);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case SearchConstants.ACTION_FULL_ALBUM:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    try {
                        JSONObject response = new JSONObject(responseObject);
                        JSONObject artist = new JSONObject();
                        JSONArray items = new JSONArray();
                        if (responseObject.equalsIgnoreCase("NOK")) {
                            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.has("albums")) {
                                items = response.getJSONArray("albums");
                            }

                            List<Album> albumList = getListAlbum(items);

                            AlbumModel.getInstance().setAlbumList(keyPersist, albumList);
                            AlbumModel.getInstance().persistData();
                            SearchArtistAlbumFragment.refreshDataAdapter();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    break;
            }
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<Album> getListAlbum(JSONArray albumListResponse) throws JSONException {
        ArrayList<Album> mItems = new ArrayList<>();

        if (albumListResponse != null) {
            for (int i = 0; i < albumListResponse.length(); i++) {
                JSONObject artistObj = null;
                try {
                    artistObj = albumListResponse.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String image = "";
                Album album = new Album();
                if (artistObj.has("images")){
                    if (artistObj.getJSONArray("images").length() > 0){
                        for (int x = 0; x < artistObj.getJSONArray("images").length(); x++){
                            int size = Integer.parseInt(artistObj.getJSONArray("images").getJSONObject(x).getString("height"));
                            if (size >= 200){
                                image = artistObj != null ? artistObj.getJSONArray("images").getJSONObject(x).getString("url") : null;
                            }
                        }
                    }
                }

                String name = artistObj != null ? artistObj.getString("name") : null;
                String popularity = artistObj != null ? artistObj.getString("popularity") : null;
                String id = artistObj != null ? artistObj.getString("id") : null;
                String releaseDate = artistObj != null ? artistObj.getString("release_date") : null;
                String tracks = artistObj != null ? artistObj.getJSONObject("tracks").toString() : null;

                album.setImage(image);
                album.setName(name);
                album.setPopularity(popularity);
                album.setId(id);
                album.setReleaseDate(releaseDate);
                album.setTracks(tracks);

                mItems.add(album);
            }

        }
        return mItems;
    }

    private ArrayList<Artist> getListArtist(JSONArray artistListResponse) throws JSONException {
        ArrayList<Artist> mItems = new ArrayList<>();

        if (artistListResponse != null) {
            for (int i = 0; i < artistListResponse.length(); i++) {
                JSONObject artistObj = null;
                try {
                    artistObj = artistListResponse.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String image = "";
                Artist artist = new Artist();
                if (artistObj.has("images")){
                    if (artistObj.getJSONArray("images").length() > 0){
                        for (int x = 0; x < artistObj.getJSONArray("images").length(); x++){
                            int size = Integer.parseInt(artistObj.getJSONArray("images").getJSONObject(x).getString("height"));
                            if (size >= 200){
                                image = artistObj != null ? artistObj.getJSONArray("images").getJSONObject(x).getString("url") : null;
                            }
                        }
                    }
                }

                String name = artistObj != null ? artistObj.getString("name") : null;
                String popularity = artistObj != null ? artistObj.getString("popularity") : null;
                String id = artistObj != null ? artistObj.getString("id") : null;
                artist.setImage(image);
                artist.setName(name);
                artist.setPopularity(popularity);
                artist.setId(id);

                mItems.add(artist);
            }

        }
        return mItems;
    }

    public void setParams(String param1) {
        this.param1 = param1;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setKeyPersist(String keyPersist) {
        this.keyPersist = keyPersist;
    }
}
