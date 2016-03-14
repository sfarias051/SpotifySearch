package cl.sebastianfarias.spotifysearch.search.model;

import java.io.Serializable;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class Album implements Serializable {

    private static final long serialVersionUID = -3380682661800495415L;
    private String name;
    private String id;
    private String image;
    private String popularity;
    private String releaseDate;
    private String tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }
}
