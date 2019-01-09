
package fr.alteca.monalteca.textrazor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entity implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("matchingTokens")
    @Expose
    private List<Integer> matchingTokens = new ArrayList<Integer>();
    @SerializedName("entityId")
    @Expose
    private String entityId;
    @SerializedName("freebaseTypes")
    @Expose
    private List<String> freebaseTypes = new ArrayList<String>();
    @SerializedName("confidenceScore")
    @Expose
    private Double confidenceScore;
    @SerializedName("wikiLink")
    @Expose
    private String wikiLink;
    @SerializedName("matchedText")
    @Expose
    private String matchedText;
    @SerializedName("freebaseId")
    @Expose
    private String freebaseId;
    @SerializedName("relevanceScore")
    @Expose
    private Double relevanceScore;
    @SerializedName("entityEnglishId")
    @Expose
    private String entityEnglishId;
    @SerializedName("startingPos")
    @Expose
    private Integer startingPos;
    @SerializedName("endingPos")
    @Expose
    private Integer endingPos;
    @SerializedName("wikidataId")
    @Expose
    private String wikidataId;
    @SerializedName("type")
    @Expose
    private List<String> type = new ArrayList<String>();
    private final static long serialVersionUID = 1861000154265638982L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getMatchingTokens() {
        return matchingTokens;
    }

    public void setMatchingTokens(List<Integer> matchingTokens) {
        this.matchingTokens = matchingTokens;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public List<String> getFreebaseTypes() {
        return freebaseTypes;
    }

    public void setFreebaseTypes(List<String> freebaseTypes) {
        this.freebaseTypes = freebaseTypes;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public void setMatchedText(String matchedText) {
        this.matchedText = matchedText;
    }

    public String getFreebaseId() {
        return freebaseId;
    }

    public void setFreebaseId(String freebaseId) {
        this.freebaseId = freebaseId;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public String getEntityEnglishId() {
        return entityEnglishId;
    }

    public void setEntityEnglishId(String entityEnglishId) {
        this.entityEnglishId = entityEnglishId;
    }

    public Integer getStartingPos() {
        return startingPos;
    }

    public void setStartingPos(Integer startingPos) {
        this.startingPos = startingPos;
    }

    public Integer getEndingPos() {
        return endingPos;
    }

    public void setEndingPos(Integer endingPos) {
        this.endingPos = endingPos;
    }

    public String getWikidataId() {
        return wikidataId;
    }

    public void setWikidataId(String wikidataId) {
        this.wikidataId = wikidataId;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

}
