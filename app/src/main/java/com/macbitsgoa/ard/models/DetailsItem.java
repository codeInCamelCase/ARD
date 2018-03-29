package com.macbitsgoa.ard.models;

/**
 * Created by aayush on 29/3/18.
 */

public class DetailsItem {

    private String title;
    private String tag;

    public DetailsItem(String title,String tag){
        this.title=title;
        this.tag=tag;
    }

    public String getTitle(){
        return title;
    }


    public String getTAG(){
        return tag;
    }
}
