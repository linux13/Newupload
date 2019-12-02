package com.example.filee;

import com.google.firebase.database.Exclude;

public class Model {
    String filename,fileurl,mkey;
    Model(){

    }

    public Model(String filename, String fileurl) {
        this.filename = filename;
        this.fileurl = fileurl;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    @Exclude
    public String getMkey(){
        return mkey;
    }

    @Exclude
    public void setMkey(String mkey ){
        this.mkey=mkey;
    }
}
