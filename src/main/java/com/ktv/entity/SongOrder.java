package com.ktv.entity;

public class SongOrder {
    private String account;
    private int songId;
    private int playCount;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    private String songName;
    private String singer;
    public SongOrder() {}

    public SongOrder(String account, int songId, int playCount) {
        this.account = account;
        this.songId = songId;
        this.playCount = playCount;
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }

    @Override
    public String toString() {
        return "SongOrder{" +
                "account='" + account + '\'' +
                ", songId=" + songId +
                ", playCount=" + playCount +
                '}';
    }
}