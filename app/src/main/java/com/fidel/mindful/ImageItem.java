package com.fidel.mindful;

public class ImageItem {
    private final int imageResource;
    private final String title;

    public ImageItem(int imageResource, String title) {
        this.imageResource = imageResource;
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }
}

