package cl.sebastianfarias.spotifysearch.search;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class SearchConstants {
    //GET URL API SPOTIFY
    public static final String SEARCH_ITEM_API = "https://api.spotify.com/v1/search";
    public static final String GET_ALBUMS_API = "https://api.spotify.com/v1/artists/";
    public static final String GET_ALBUM_YEAR_TRACK_API = "https://api.spotify.com/v1/albums/?ids=";

    //PARAMS
    public static final String QUERY_Q      ="q";
    public static final String QUERY_TYPE   = "type";

    //ACTION
    public static final String ACTION_SEARCH        = "SEARCH";
    public static final String ACTION_ALBUM         = "ALBUM";
    public static final String ACTION_FULL_ALBUM    = "FULL_ALBUM";
}
