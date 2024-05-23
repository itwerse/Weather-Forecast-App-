package com.example.weatherapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Sys {


        @SerializedName  ("type")
        private Integer type;
        @SerializedName("id")
        private long id;
        @SerializedName("country")
        private String country;
        @SerializedName("sunrise")
        private long sunrise;
        @SerializedName("sunset")
        private long sunset;

        @SerializedName("type")
        public long getType() {
            return type;
        }

        @SerializedName("type")
        public void setType(Integer type) {
            this.type = type;
        }

        @SerializedName("id")
        public long getId() {
            return id;
        }

        @SerializedName("id")
        public void setId(Integer id) {
            this.id = id;
        }

        @SerializedName("country")
        public String getCountry() {
            return country;
        }

        @SerializedName("country")
        public void setCountry(String country) {
            this.country = country;
        }

        @SerializedName("sunrise")
        public long getSunrise() {
            return sunrise;
        }

        @SerializedName("sunrise")
        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        @SerializedName("sunset")
        public long getSunset() {
            return sunset;
        }

        @SerializedName("sunset")
        public void setSunset(long sunset) {
            this.sunset = sunset;
        }



    }

