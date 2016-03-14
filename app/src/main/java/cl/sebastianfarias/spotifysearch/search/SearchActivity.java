package cl.sebastianfarias.spotifysearch.search;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
public class SearchActivity extends ActionBarActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        context = this;

        FragmentManager fm      = this.getFragmentManager();
        FragmentTransaction ft  = fm.beginTransaction();

        //LOAD FRAGMENT SEARCH SCREEN
        SearchFragment searchFragment = new SearchFragment();
        ft.add(R.id.main_container,searchFragment);
        ft.commit();
        fm.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
