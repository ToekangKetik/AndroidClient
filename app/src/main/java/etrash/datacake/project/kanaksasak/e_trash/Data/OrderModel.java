package etrash.datacake.project.kanaksasak.e_trash.Data;

/**
 * Created by IT on 3/18/2018.
 */

public class OrderModel {
    String idorder, hp, nama, alamat, bag, date, time, status;

    public OrderModel() {
    }

    public OrderModel(String idorder, String hp, String nama, String alamat, String bag, String date, String time, String status) {
        this.idorder = idorder;
        this.hp = hp;
        this.nama = nama;
        this.alamat = alamat;
        this.bag = bag;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getIdorder() {
        return idorder;
    }

    public void setIdorder(String idorder) {
        this.idorder = idorder;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
