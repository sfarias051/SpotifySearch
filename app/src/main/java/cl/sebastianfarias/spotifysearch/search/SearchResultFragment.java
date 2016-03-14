package cl.sebastianfarias.spotifysearch.search;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Artist;
import cl.sebastianfarias.spotifysearch.search.model.ArtistModel;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchResultFragment extends Fragment implements OnItemClickListener {
    public static List<Artist> listArtist;
    public static SearchResultAdapter artistAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_list, container, false);

        ActionBar mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle(R.string.artist_title);
        mActionBar.setShowHideAnimationEnabled(false);
        mActionBar.show();

        if (listArtist.size()==0){
            TextView empty = (TextView) view.findViewById(R.id.search_empty_list);
            ListView list = (ListView) view.findViewById(R.id.search_list_view);
            empty.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            artistAdapter = new SearchResultAdapter(getActivity(),listArtist);
            ListView list = (ListView) view.findViewById(R.id.search_list_view);
            list.setAdapter(artistAdapter);
            list.setOnItemClickListener(this);
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Artist artistSelected = listArtist.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("artist", artistSelected);
        if (artistSelected != null){
            List<Artist> temp = ArtistModel.getInstance().getArtistList("RESULT_SEARCH");
            boolean exist = false;
            if (temp.size() == 0){
                temp.add(artistSelected);
                ArtistModel.getInstance().setArtistList("RESULT_SEARCH", temp);
                ArtistModel.getInstance().persistData();
            }else {
                for (Artist artist : temp) {
                    if(artist.getName().equalsIgnoreCase(artistSelected.getName())){
                        exist = true;
                        break;
                    }
                }
                if (!exist){
                    temp.add(artistSelected);
                    ArtistModel.getInstance().setArtistList("RESULT_SEARCH", temp);
                    ArtistModel.getInstance().persistData();
                }
            }

        }

        SearchArtistAlbumFragment artistAlbum = new SearchArtistAlbumFragment();
        artistAlbum.setArguments(bundle);
        ft.replace(R.id.main_container, artistAlbum);
        ft.addToBackStack("DETAIL");
        ft.commit();
        fm.executePendingTransactions();
    }


}
