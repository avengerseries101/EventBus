package com.example.eventbusexampleusingretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    List<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_getData);
        textView = findViewById(R.id.tv_result);

        button.setOnClickListener(v -> {
            fetchQuestionsDataFromJsonAPI();
        });
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventBus event) {
        for (int i = 0; i < list.size(); i++) {
            User userData = list.get(i);
            Integer id = userData.getId();
            String name = userData.getName();
            String username = userData.getUsername();
            String email = userData.getEmail();
            Log.i("@@@@@", "onMessageEvent : userData : " + userData.toString());
            textView.append("User Id : " + id + "\n" + "Name : " + name + "\n" + "UserName : " + username + "\n" + "Email : " + email + "\n\n");
        }
        Toast.makeText(this, event.message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void fetchQuestionsDataFromJsonAPI() {
        String BASE_URL = "https://jsonplaceholder.typicode.com/";

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonAPIinterface api = retro.create(JsonAPIinterface.class);
        Call<List<User>> call = api.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    recyclerView.setLayoutManager(layoutManager);
//                    adapter = new RecyclerAdapter(getContext(), list);
//                    recyclerView.setAdapter(adapter);
                    EventBus.getDefault().post(new MessageEventBus("Successfully Got The Data"));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to Load the data, Server busy.Try again later!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
