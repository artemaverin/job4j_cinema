package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class SessionDto {

    private int id;
    private String filmName;
    private String hallsName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private int rowCount;
    private int placeCount;

    public SessionDto(int id, String filmName, String hallsName,
                      LocalDateTime startTime, LocalDateTime endTime, int price, int rowCount, int placeCount) {
        this.id = id;
        this.filmName = filmName;
        this.hallsName = hallsName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.rowCount = rowCount;
        this.placeCount = placeCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getHallsName() {
        return hallsName;
    }

    public void setHallsName(String hallsName) {
        this.hallsName = hallsName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionDto that = (SessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
