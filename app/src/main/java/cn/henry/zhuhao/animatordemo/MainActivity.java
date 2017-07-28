package cn.henry.zhuhao.animatordemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
        PieData pieData = new PieData("喜羊羊", 60, Color.MAGENTA);
        PieData pieData2 = new PieData("懒羊羊", 30,Color.GREEN);
        PieData pieData3 = new PieData("美羊羊", 40,Color.CYAN);
        PieData pieData4 = new PieData("灰太狼", 20,Color.DKGRAY);
        PieData pieData5 = new PieData("红太狼", 20,Color.YELLOW);
        datas.add(pieData);
        datas.add(pieData2);
        datas.add(pieData3);
        datas.add(pieData4);
        datas.add(pieData5);
        view.setStartAngle(90);
        view.setData(datas);
        view.setListener(new PieView.PieListener() {
            @Override
            public void onPieClicked(int position) {
                Toast.makeText(getApplicationContext(),position+"pie", Toast.LENGTH_SHORT).show();
            }
        });
        view.startAnimator();
    }
}
