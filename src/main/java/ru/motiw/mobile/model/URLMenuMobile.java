package ru.motiw.mobile.model;

/**
 * Навигация меню (URL) по системе
 */
public enum URLMenuMobile {

    CREATE_TASK("/m/#createtask");


    private String menuURL;

    URLMenuMobile(String menuURL) {
        this.menuURL = menuURL;
    }

    public String getMenuURLMobile() {
        return menuURL;
    }

}
