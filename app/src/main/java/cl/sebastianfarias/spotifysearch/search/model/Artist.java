package cl.sebastianfarias.spotifysearch.search.model;

import java.io.Serializable;

/**
 * Created by Sebastian Farias on 13/03/2016.
 */
public class Artist implements Serializable {

    private static final long serialVersionUID = 1163107453792073182L;
    private String image;
    private String name;
    private String popularity;
    private String id;

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
}
