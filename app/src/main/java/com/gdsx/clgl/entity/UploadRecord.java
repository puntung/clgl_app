package com.gdsx.clgl.entity;

/**
 * Created by Administrator on 2016/1/19.
 */
public class UploadRecord {
    private int id;
    private String path;
    private String wc;
    private int uploaded;

    public UploadRecord() {
    }

    public UploadRecord(int id, String path, String wc, int uploaded) {
        this.id = id;
        this.path = path;
        this.wc = wc;
        this.uploaded = uploaded;
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

    @Override
    public String toString() {
        return "UploadRecord{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", wc='" + wc + '\'' +
                ", uploaded=" + uploaded +
                '}';
    }
}
