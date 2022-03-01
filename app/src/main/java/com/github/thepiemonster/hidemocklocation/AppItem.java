package com.github.thepiemonster.hidemocklocation;

import android.graphics.drawable.Drawable;

class AppItem {

    private final CharSequence name;
    private final String packageName;
    private final Drawable icon;
    private boolean isChecked;

    public AppItem(CharSequence name, Drawable icon, String packageName) {
        this(name, icon, packageName, false);
    }

    public AppItem(CharSequence name, Drawable icon, String packageName, boolean isChecked) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public CharSequence getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AppItem{" +
                "name=" + name +
                ", icon=" + icon +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppItem appItem = (AppItem) o;

        return packageName.equals(appItem.packageName);
    }

    @Override
    public int hashCode() {
        return packageName.hashCode();
    }
}
