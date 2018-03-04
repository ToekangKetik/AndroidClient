package etrash.datacake.project.kanaksasak.e_trash.Data;

/**
 * Created by IT on 1/26/2018.
 */

public class UploadInfo {
    public String name;
    public String url;
    public String desc;
    public String price;

    public UploadInfo() {
    }

    public UploadInfo(String name, String url, String desc, String price) {
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
