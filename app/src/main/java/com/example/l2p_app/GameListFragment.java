package com.example.l2p_app;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.l2p_app.databinding.GameListBinding;
import com.example.l2p_app.databinding.NavHeaderMainBinding;
import com.example.l2p_app.models.MySingleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class GameListFragment extends Fragment implements View.OnClickListener {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoac6FsI:APA91bEXNuvc8k9Ln9wLGUOpkdlTi7OW3sR_VCHWtXopWnz-AO4c0zJGGas2F0zAIL5pauRf-rdO-Nzgnfm8qSApWO1WwYNvtqGzggmo7s1NCR8Q-kdNXhJkq_lgIp8tzPQIQ9WB7DY6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String USER_TOKEN;

    private View gameListView;
    private GameListBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = GameListBinding.inflate(inflater, container, false);

        ImageView valorant = binding.imageView2;
        ImageView overwatch = binding.imageView;
        valorant.setOnClickListener(this);
        overwatch.setOnClickListener(this);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StorageReference storageReference = firebaseStorage.getReference();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_global_roomCreation);
                /*

                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "Nueva solicitud";
                NOTIFICATION_MESSAGE = "Tienes una nueva solicitud para la sala NUEVA SALA"; //tomar nombre de sala
                USER_TOKEN = "eLsbe_nnRpKRQhv19Fnuy2:APA91bE9WJrRYjTAWJhjrHd7_2NtJz1gujGnpBTbZKqszDZsX9JrNMLmDfK2oElYmtwzAZLYWJsq3mPnK1U46Vay9635IAzQJYWMVZ7G2ybW445U0ratFU2w6La61DP5TqiM9sNuoxvf";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                /*
                {
                    to: "Topic"  or REGISTRATION_ID-token unico de la aplicacion para cada dispositivo
                    data: {
                        title:
                        message:
                    }
                    time_to_live: "600" opcional
                }
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", USER_TOKEN);
                    notification.put("data", notifcationBody);
                    notification.put("time_to_live", 600);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                sendNotification(notification);*/
            }
        });
    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        //edtTitle.setText("");
                        //edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        String game;
        GameListFragmentDirections.ActionNavHomeToNavRooms action;
        Intent intent = new Intent(getActivity(), RoomsActivity.class);
        switch (view.getId()){
            case R.id.imageView:
                game = "Valorant";
                /*
                action = GameListFragmentDirections.actionNavHomeToNavRooms(game);
                NavHostFragment.findNavController(GameListFragment.this)
                        .navigate((NavDirections) action);*/

                intent.putExtra("game", game);
                startActivity(intent);
                break;
            case R.id.imageView2:
                game = "Overwatch";
                /*
                action = GameListFragmentDirections.actionNavHomeToNavRooms(game);
                NavHostFragment.findNavController(GameListFragment.this)
                        .navigate((NavDirections) action);*/
                intent.putExtra("game", game);
                startActivity(intent);
                break;
        }

    }

}