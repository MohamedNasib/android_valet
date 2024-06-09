package com.icapp.icapp.Models.Input.Arrival;

public class ArrivalInputParams {
    String ticket_number;
    String plate_number;
    Long front_image_date;
    Long left_image_date;
    Long back_image_date;
    Long right_image_date;
    String user_id;
    String latitude;
    String longitude;

    public ArrivalInputParams(String ticket_number, String plate_number,
                              Long front_image_date, Long left_image_date,
                              Long back_image_date, Long right_image_date,
                              String user_id, String latitude, String longitude) {
        this.ticket_number = ticket_number;
        this.plate_number = plate_number;
        this.front_image_date = front_image_date;
        this.left_image_date = left_image_date;
        this.back_image_date = back_image_date;
        this.right_image_date = right_image_date;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}