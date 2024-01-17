package com.example.rxjava_retorfit_weather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    //定義固定值
    private static final String BASE_URL = "https://opendata.cwb.gov.tw/";
    private static final String authorization = "CWB-5AD7B04F-DFEA-4CD0-8650-2AFE30A4CDDE"; //使用openai的驗證碼
    //private static final String elementName = "MaxT"; //字串的名稱  (注意參數名稱必須與 service端 一致)
    private int keep_index; //index 用來暫存 Spinner的值 (可以用索引值找到對應的城市名稱)
    private String keep_locationName = "";
    private String keep_data = ""; //抓取的資料 飽含了溫度 高低溫等等

    //做變數宣告
    Spinner cityitem;
    Button display_button;
    TextView temperature_textView;  //最高溫
    TextView cityname_textView;    //城市名稱
    TextView temperaturemin_textView; //最低溫
    TextView percentage_textView; //百分比
    TextView comfortable_textView; //舒適度
    TextView Weather_state_textView; //天氣狀況

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //綁定元件
        byid();

        //設定下拉式選單
        setcityitem();

        //city-Spinner 監聽器
        cityitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //當被選擇時做動作
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keep_index = i;
            }
            //當沒有做麼事時做動作
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //顯示溫度
        display();
    }
    //設定下拉式選單
    private void setcityitem(){
        //建立下拉市選單的資料
        //Map<String , Integer> citynamemap = new HashMap<>(); //定義鍵值對 前面是城市名稱  後面是索引值
        String[] city_name_A = new String[] { "嘉義縣", "新北市", "嘉義市", "新竹縣", "新竹市",
                "臺北市"," 臺南市", "宜蘭縣", "苗栗縣", "雲林縣",
                "花蓮縣", "臺中市","臺東縣","桃園市","南投縣",
                "高雄市","金門縣","屏東縣","基隆市","澎湖縣","彰化縣","連江縣"};  //這些資料都有做過資料的順序對應 用來節省收尋時間

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<> (this, android.R.layout.simple_spinner_dropdown_item, city_name_A);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityitem.setAdapter(adapter); //將名稱加入倒(spinner)清單裡
    }
    //拿取伺服器的資料
    private void getweather_inservice(){
        //https://opendata.cwb.gov.tw/
        //設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BASE_URL) //設定基本URL
                .build();
        MyAPIWeatherService myAPIService = retrofit.create(MyAPIWeatherService.class);


        //notice 資料夾 app\manifests\AndroidManifest.xml
        //開頭需要放入網路權限<uses-permission android:name="android.permission.INTERNET" />
        //拿取service資料
        //解說函式 subscribeOn() : 被觀察者(Observerable)被指定的路線是主程緒或副程序
        //解說函式 observeOn() : 我的觀察者也就是我的UI介面,顯示在主線程或副線程上
        //解說函式 subscribe() : 代表我訂閱的觀察者(也就是我的UI介面)，且畫面會顯示在我指定的觀察者身上
        // AndroidSchedulers.mainThread() 主線程路線
        // Schedulers.io()  副線程路線
        myAPIService.getWeater(authorization)
                .subscribeOn(Schedulers.io())   //Schedulers.io() 如果有網路、檔案存取需求推薦使用，Rxjava 會幫我們管理 ThreadPool reuse 的部分。
                .observeOn(AndroidSchedulers.mainThread()) // observeOn是指主畫面顯示 在()裡是 指在主線程執行回覆。
                .subscribe(new Observer<Weather_Respone>() {

                    //初始化
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    //連線成功做動作
                    @Override
                    public void onNext(@NonNull Weather_Respone weather_respone) {
                        Log.d("title","請求成功");
                        cityname_textView.setText("城市名稱:" + weather_respone.getRecords().getLocation().get(keep_index).getLocationName());
                        for(int i = 0; i < 5;i++){
                            String keep_data = weather_respone.getRecords().getLocation().get(keep_index).getWeatherElement().get(i).getTime().get(0).getParameter().getParameterName();
                            setWeather_textView(i , keep_data);
                        }
                    }

                    //連線失敗做動作
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("title","請求失敗");
                    }

                    //完成所有執行做動作
                    @Override
                    public void onComplete() {

                    }
                });
    }
    //按下按鈕顯示
    private void display(){
        //顯示溫度按鈕
        display_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getweather_inservice();//拿取伺服器的資料
            }
        });
    }
    //綁定元件
    private void byid(){
        cityitem = findViewById(R.id.spinner);
        display_button = findViewById(R.id.button);
        temperature_textView = findViewById(R.id.textView2);
        cityname_textView = findViewById(R.id.textView3);
        temperaturemin_textView = findViewById(R.id.textView5);
        percentage_textView = findViewById(R.id.textView6);
        comfortable_textView = findViewById(R.id.textView7);
        Weather_state_textView = findViewById(R.id.textView4);
    }

    //設定每個TextView有關天氣數據的資料
    private void setWeather_textView(int i , String data){
        //MaxT對高溫  MinT最低溫 Wx氣候狀況 CI 舒服或悶熱 //(注意順序必須與數據顯示一致)
        //{"WX", "PoP", "MinT", "CI", "MaxT"} 順序
        //MaxT對高溫  MinT最低溫 Wx氣候狀況 CI 舒服或悶熱
        if(i == 0){
            Weather_state_textView.setText("天氣狀況:" + data);
        }
        else if(i == 1){
            percentage_textView.setText("百分比:" + data);
        }
        else if(i == 2){
            temperaturemin_textView.setText("最低溫度:" + data + "C");
        }
        else if(i == 3){
            comfortable_textView.setText("舒適程度:" + data);
        }
        else{
            temperature_textView.setText("最高溫度:" + data + "C");
        }
    }
}