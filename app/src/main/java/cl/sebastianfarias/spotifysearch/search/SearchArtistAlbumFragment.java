package cl.sebastianfarias.spotifysearch.search;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Album;
import cl.sebastianfarias.spotifysearch.search.model.AlbumModel;
import cl.sebastianfarias.spotifysearch.search.model.Artist;
import cl.sebastianfarias.spotifysearch.search.model.ArtistModel;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchArtistAlbumFragment extends Fragment {

    private static Artist artist;
    private static Fragment fragment;
    private static Activity activity;
    public static List<Album> listAlbum;
    public static SearchArtistAlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.albums_list, container, false);
        fragment = this;
        activity = getActivity();
        ActionBar mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle(R.string.album_title);
        mActionBar.setShowHideAnimationEnabled(false);
        mActionBar.show();

        artist = (Artist) this.getArguments().get("artist");

        TextView nameArtist         = (TextView) view.findViewById(R.id.name_artist);
        TextView popularityArtist   = (TextView) view.findViewById(R.id.popularity_artist);
        nameArtist.setText(artist.getName());
        popularityArtist.setText(artist.getPopularity());

        if (AlbumModel.getInstance().getAlbumList(artist.getId()).size() == 0){
            SearchTask searchTask = new SearchTask(getActivity());
            searchTask.setParams(artist.getId());
            searchTask.setKeyPersist(artist.getId());
            searchTask.setAction(SearchConstants.ACTION_ALBUM);
            searchTask.setFragment(fragment);
            searchTask.execute();
        }

        listAlbum = AlbumModel.getInstance().getAlbumList(artist.getId());
        albumAdapter = new SearchArtistAlbumAdapter(getActivity(),listAlbum);
        ListView list = (ListView) view.findViewById(R.id.album_list_view);
        list.setAdapter(albumAdapter);


        return view;
    }

    public static void searchFullInfo(String ids,String key){
        SearchTask searchTask = new SearchTask(activity);
        searchTask.setParams(ids);
        searchTask.setAction(SearchConstants.ACTION_FULL_ALBUM);
        searchTask.setFragment(fragment);
        searchTask.setKeyPersist(key);
        searchTask.execute();
    }

    public static void refreshDataAdapter(){
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.detach(fragment).attach(fragment).commit();
    }

}
