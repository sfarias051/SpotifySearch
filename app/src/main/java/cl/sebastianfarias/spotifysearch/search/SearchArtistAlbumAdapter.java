package cl.sebastianfarias.spotifysearch.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Album;

/**
 * Created by Sebastian Farias on 14/03/2016.
 */
public class SearchArtistAlbumAdapter extends ArrayAdapter<Album> {
    public SearchArtistAlbumAdapter(Context context, List<Album> artist){
        super(context, R.layout.album_list_item, artist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.album_list_item,parent, false);
        }
        ImageView mAlbumImageView      = (ImageView) v.findViewById(R.id.album_img_list_view);
        TextView mAlbumNameTextView    = (TextView) v.findViewById(R.id.album_name_list_view);
        TextView mAlbumPopTextView     = (TextView) v.findViewById(R.id.album_popularity_list_view);
        TextView mAlbumReleaseTextVIew  = (TextView) v.findViewById(R.id.album_release_list_view);
        TextView mAlbumSongNameTextView  = (TextView) v.findViewById(R.id.album_song_name);
        TextView mAlbumSongMSTextView  = (TextView) v.findViewById(R.id.album_song_time);

        Album album = getItem(position);
        Glide.with(getContext()).load(album.getImage()).error(R.drawable.ic_image_black_24dp).placeholder(R.drawable.ic_image_black_24dp).into(mAlbumImageView);
        mAlbumNameTextView.setText(album.getName());
        mAlbumPopTextView.setText(album.getPopularity());
        mAlbumReleaseTextVIew.setText(album.getReleaseDate());
        //TODO CHECK
        String tempTime = "";
        String tempName = "";
        try {
            JSONObject track = new JSONObject(album.getTracks());
            for (int i = 0; i < track.getJSONArray("items").length(); i++) {
                if (tempTime.equalsIgnoreCase("")){
                    tempTime = track.getJSONArray("items").getJSONObject(i).get("duration_ms").toString();
                    tempName = track.getJSONArray("items").getJSONObject(i).get("name").toString();
                }else {
                    if (Integer.parseInt(tempTime) < Integer.parseInt(track.getJSONArray("items").getJSONObject(i).get("duration_ms").toString())){
                        tempTime = track.getJSONArray("items").getJSONObject(i).get("duration_ms").toString();
                        tempName = track.getJSONArray("items").getJSONObject(i).get("name").toString();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAlbumSongNameTextView.setText(tempName);
        mAlbumSongMSTextView.setText(tempTime);

        return v;
    }

    @Override
    public Album getItem(int position) {
        return super.getItem(position);
    }
}


