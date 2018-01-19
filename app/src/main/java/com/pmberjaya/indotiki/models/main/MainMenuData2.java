package com.pmberjaya.indotiki.models.main;

import java.util.ArrayList;

/**
 * Created by edwin on 06/01/2017.
 */

public class MainMenuData2 {
    String menuName;
    int menuImage;
    String status;
    boolean coming_soon;
    private ArrayList<SubMenuData> submenu = null;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public boolean isComing_soon() {
        return coming_soon;
    }

    public void setComing_soon(boolean coming_soon) {
        this.coming_soon = coming_soon;
    }

    public ArrayList<SubMenuData> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(ArrayList<SubMenuData> submenu) {
        this.submenu = submenu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
