package com.movieticket.model;

import javax.persistence.*;

@Entity
@Table (name="movie")
public class Movie {
    @Id
    @GeneratedValue (strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private String language;
    private String imageUrl;

    private String casting;
    private String duration;
    private String genre;
    private String dateOfRelease;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCasting() {
        return casting;
    }

    public void setCasting(String casting) {
        this.casting = casting;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(String dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }
}
