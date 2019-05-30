package com.km.crowdsource;

public class Recording {
    String Uri, fileName;
    boolean isPlaying;

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Recording(String uri, String fileName, boolean isPlaying) {
        this.Uri = uri;
        this.fileName = fileName;
        this.isPlaying = isPlaying;
    }
}
