package etrash.datacake.project.kanaksasak.e_trash.Data;

/**
 * Created by IT on 3/12/2018.
 */

public class MenuModel {

    String Title;
    int Icon;

    public MenuModel() {
    }

    public MenuModel(String title, int icon) {
        Title = title;
        Icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}
