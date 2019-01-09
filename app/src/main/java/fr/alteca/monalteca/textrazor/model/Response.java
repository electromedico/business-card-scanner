
package fr.alteca.monalteca.textrazor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("languageIsReliable")
    @Expose
    private Boolean languageIsReliable;
    @SerializedName("entities")
    @Expose
    private List<Entity> entities = new ArrayList<Entity>();
    private final static long serialVersionUID = -1898987061199788594L;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getLanguageIsReliable() {
        return languageIsReliable;
    }

    public void setLanguageIsReliable(Boolean languageIsReliable) {
        this.languageIsReliable = languageIsReliable;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

}
