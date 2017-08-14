# HCharts
这是一个基于android的平台的图表框架，支持点击，动画。

<p><img src="https://github.com/HenryHaoson/HCharts/blob/master/Gifs/PieView.gif" alt="image" width="50%" height="50%"/></p>
<p><img src="https://github.com/HenryHaoson/HCharts/blob/master/Gifs/SimpleBarView.gif" alt="image" width="50%" height="50%"/></p>


## Installation

TODO: Describe the installation process

## Usage

- PieChart
> xml

```
        <com.zhuhao.hcharts.views.PieView
            android:id="@+id/pieview"
            android:layout_width="match_parent"
            android:layout_height="400dp" />
```

> java

```
           PieView.build(view).setStartAngle(90).setData(datas);
           view.setListener(new PieView.PieListener() {
               @Override
               public void onPieClicked(int position) {
                   Toast.makeText(getApplicationContext(), position + "pie", Toast.LENGTH_SHORT).show();
               }
           });
```

-SimpleBarChart
>xml

```
        <com.zhuhao.hcharts.views.SimpleBarView
            android:id="@+id/simple_bar_view"
            android:layout_width="match_parent"
            android:layout_height="0dp" />
```

> java

```
        SimpleBarView.build(view1).setValueLineCount(3).setData(datas1).setTotalValue(70);
        view1.setListener(new SimpleBarView.BarListener() {
            @Override
            public void onBarClicked(int position) {
                Toast.makeText(getApplicationContext(), position + "bar", Toast.LENGTH_SHORT).show();
            }
        });
```
## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

TODO: Write history

## Credits

TODO: Write credits

## License

TODO: Write license