package cn.henry.zhuhao.animatordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cn.henry.zhuhao.animatordemo.entity.PieData;
import cn.henry.zhuhao.animatordemo.views.PieView;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieView view= (PieView) findViewById(R.id.pieview);
        ArrayList<PieData> datas = new ArrayList<>();
        PieData pieData = new PieData("sloop", 60);
        PieData pieData2 = new PieData("sloop", 30);
        PieData pieData3 = new PieData("sloop", 40);
        PieData pieData4 = new PieData("sloop", 20);
        PieData pieData5 = new PieData("sloop", 20);
        datas.add(pieData);
        datas.add(pieData2);
        datas.add(pieData3);
        datas.add(pieData4);
        datas.add(pieData5);
        view.setData(datas);
        view.setStartAngle(90);
        view.startAnimator();
    }
}
