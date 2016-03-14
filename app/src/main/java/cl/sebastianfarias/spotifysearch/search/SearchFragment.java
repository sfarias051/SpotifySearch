package cl.sebastianfarias.spotifysearch.search;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import cl.sebastianfarias.spotifysearch.R;
import cl.sebastianfarias.spotifysearch.search.model.Artist;
import cl.sebastianfarias.spotifysearch.search.model.ArtistModel;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchFragment extends Fragment {
    private EditText mSearchET;
    private Fragment fragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_artist_search, container, false);
        fragment= this;
        // Hide action bar
        ActionBar mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionBar.setShowHideAnimationEnabled(false);
        mActionBar.setTitle(null);

        mSearchET = (EditText) view.findViewById(R.id.search_et);
        Button mSearchBTN = (Button) view.findViewById(R.id.search_btn);
        mSearchBTN.setOnClickListener(onSearchClick);

        setHasOptionsMenu(true);
        return view;
    }

    private View.OnClickListener onSearchClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String search = mSearchET.getText().toString().trim();
            if (search.length() <= 0 ){
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setPositiveButton(R.string.ok, null);
                alert.setCancelable(false);
                alert.setMessage(R.string.search_error);
                alert.show();
            }else{
                SearchTask searchTask = new SearchTask(getActivity());
                searchTask.setParams(mSearchET.getText().toString());
                searchTask.setAction(SearchConstants.ACTION_SEARCH);
                searchTask.setFragment(fragment);
                searchTask.execute();
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_load_previous:
                List<Artist> artistList = ArtistModel.getInstance().getArtistList("RESULT_SEARCH");

                FragmentManager fm      = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SearchResultFragment searchResultFragmentdListFragment = new SearchResultFragment();
                searchResultFragmentdListFragment.listArtist = artistList;
                ft.replace(R.id.main_container, searchResultFragmentdListFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
