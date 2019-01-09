
package fr.alteca.monalteca.textrazor.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextRazorResponse implements Serializable
{

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("time")
    @Expose
    private Double time;
    @SerializedName("ok")
    @Expose
    private Boolean ok;
    private final static long serialVersionUID = -4122172770220749963L;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}
