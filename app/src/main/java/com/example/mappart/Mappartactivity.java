
package com.example.mappart;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mappartactivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner spinnerName = null;
    private int old = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappartactivity);

       spinnerName = (Spinner)findViewById(R.id.spinnerName); //객체 생성

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fixed"); //null값으로 받지 않고 고정값으로 받기 위해 fixed설정

        myRef.getParent().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ArrayList<String> list = new ArrayList<>(); //spinner를 설정
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                for(DataSnapshot snapshot : datasnapshot.getChildren()) { //snapshot에 list들이 넘어옴
                    String key =snapshot.getKey(); //list를 받은 snapshot에 키를 받아옴
                    if(key.equals("fixed")) { //키가 fixed면 무시
                        continue;
                    }

                    list.add(key);
                }
                spinnerName.setAdapter(adapter); //어댑터 설정

                if(old !=-1) {
               spinnerName.setSelection(old);
                }

                spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //spinner의 항목을 눌렀을 때 실행
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String s =(String)parent.getItemAtPosition(position);
                        old=position; //마지막으로 선택했던 포지션값을 old에 넣어줌
                        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show(); //짧게 Toast 메세지를 출력

                        DatabaseReference ref = database.getReference(s); //database에 있는 키값을 가져옴

                        ref.addValueEventListener(new ValueEventListener() { //키값의 위도,경도 값을 가져옴
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {//데이터가 바뀔 때
                                DO datas = datasnapshot.getValue(DO.class);

                                LatLng point = new LatLng(Double.parseDouble(datas.getLatitude()),Double.parseDouble(datas.getLongitude()));
                                //latitude와 longitude 값 가져오기
                                mMap.addMarker(new MarkerOptions().position(point).title("marker"));//지도에 마커표시
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));//마커지점으로 포인트가 옮겨짐
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(17.5f));//카메라 줌인
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

}

