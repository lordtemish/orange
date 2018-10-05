package com.dynamica.orange.Form;

public class FileObjectForm {
    String id;
    String file;
    String type;
    String url;
    public  FileObjectForm(String file){
        id=System.identityHashCode(file)+"";
        this.file=file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getFile() {
        return file;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
