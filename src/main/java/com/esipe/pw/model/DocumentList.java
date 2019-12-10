package com.esipe.pw.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DocumentList extends PageData {
    @JsonProperty("data")
    private List<DocumentSummary> data = null;

    public DocumentList data(List<DocumentSummary> data){
        this.data = data;
        return this;
    }

    public List<DocumentSummary> getData() {
        return data;
    }

    public void setData(List<DocumentSummary> data) {
        this.data = data;
    }

    public DocumentList addDataItem(DocumentSummary dataItem) {
        if (this.data == null) {
            this.data = new ArrayList<DocumentSummary>();
        }
        this.data.add(dataItem);
        return this;
    }

    //ajouter code pour décrire et ajouter texte


}
