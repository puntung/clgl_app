package com.gdsx.clgl.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/1/19.
 */
public class UploadRecord {
    private int id;
    private String path;
    private String wc;
    private int uploaded;
    private String uploadtime;

    public UploadRecord() {
    }

    public UploadRecord(int id, String path, String wc, int uploaded,String uploadtime) {
        this.id = id;
        this.path = path;
        this.wc = wc;
        this.uploaded = uploaded;
        this.uploadtime = uploadtime;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getWc() {
        return wc;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    @Override
    public String toString() {
        return "UploadRecord{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", wc='" + wc + '\'' +
                ", uploaded=" + uploaded +
                ", uploadtime=" + uploadtime +
                '}';
    }
}
