package cl.sebastianfarias.spotifysearch.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Artist;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchResultAdapter extends ArrayAdapter<Artist> {
    public SearchResultAdapter(Context context, List<Artist> artist){
        super(context, R.layout.search_result_list_item, artist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.search_result_list_item,parent, false);
        }
        ImageView mArtistImageView      = (ImageView) v.findViewById(R.id.artist_img_list_view);
        TextView mArtistNameTextView    = (TextView) v.findViewById(R.id.artist_name_list_view);
        TextView mArtistPopTextView     = (TextView) v.findViewById(R.id.artist_popularity_list_view);

        Artist artist = getItem(position);
        Glide.with(getContext()).load(artist.getImage()).error(R.drawable.ic_image_black_24dp).placeholder(R.drawable.ic_image_black_24dp).into(mArtistImageView);
        mArtistNameTextView.setText(artist.getName());
        mArtistPopTextView.setText(artist.getPopularity());

        return v;
    }

    @Override
    public Artist getItem(int position) {
        return super.getItem(position);
    }
}
