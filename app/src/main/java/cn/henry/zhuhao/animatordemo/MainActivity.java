package cn.henry.zhuhao.animatordemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zhuhao.hcharts.entity.BarData;
import com.zhuhao.hcharts.entity.PieData;
import com.zhuhao.hcharts.views.PieView;
import com.zhuhao.hcharts.views.SimpleBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieView view = (PieView) findViewById(R.id.pieview);
        ArrayList<PieData> datas = new ArrayList<>();
        PieData pieData = new PieData("喜羊羊", 60, Color.MAGENTA);
        PieData pieData2 = new PieData("懒羊羊", 30, Color.GREEN);
        PieData pieData3 = new PieData("美羊羊", 40, Color.CYAN);
        PieData pieData4 = new PieData("灰太狼", 20, Color.DKGRAY);
        PieData pieData5 = new PieData("红太狼", 20, Color.YELLOW);
        datas.add(pieData);
        datas.add(pieData2);
        datas.add(pieData3);
        datas.add(pieData4);
        datas.add(pieData5);
//        view.setStartAngle(90);
//        view.setData(datas);
//        view.setListener(new PieView.PieListener() {
//            @Ovride
//            public void onPieClicked(int position) {
//                Toast.makeText(getApplicationContext(), position + "pie", Toast.LENGTH_SHORT).show();
//            }
//        });
//        view.startAnimator();

        PieView.build(view).setStartAngle(90).setData(datas);


        SimpleBarView view1 = (SimpleBarView) this.findViewById(R.id.simple_bar_view);
        ArrayList<BarData> datas1 = new ArrayList<>();
        BarData barData1 = new BarData("喜羊羊", 60, Color.MAGENTA);
        BarData barData2 = new BarData("懒羊羊", 30, Color.GREEN);
        BarData barData3 = new BarData("美羊羊", 40, Color.CYAN);
        BarData barData4 = new BarData("灰太狼", 20, Color.DKGRAY);
        BarData barData5 = new BarData("红太狼", 20, Color.YELLOW);
        datas1.add(barData1);
        datas1.add(barData2);
        datas1.add(barData3);
        datas1.add(barData4);
        datas1.add(barData5);
        SimpleBarView.build(view1).setValueLineCount(3).setData(datas1).setTotalValue(70);
        view1.setListener(new SimpleBarView.BarListener() {
            @Override
            public void onBarClicked(int position) {
                Toast.makeText(getApplicationContext(), position + "bar", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
