package com.example.eventbusexampleusingretrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonAPIinterface {
    @GET("users")
    Call<List<User>> getUsers();
}
