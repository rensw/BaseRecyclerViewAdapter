package com.iyinxun.RcAdapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iyinxun.library.BaseAdapter;
import com.iyinxun.library.BaseHeadAdapter;
import com.iyinxun.library.OnItemClickListener;
import com.iyinxun.library.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {


    private RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialize();
    }

    private List<String> datalist() {
        List<String> mlist = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mlist.add("我是" + i + "item");
        }
        return mlist;
    }

    private void initialize() {
        View headview = View.inflate(this, R.layout.head, null);
        View footview = View.inflate(this, R.layout.foot, null);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        // recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setLayoutManager (new GridLayoutManager(this, 2));
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3,
//                StaggeredGridLayoutManager.VERTICAL));
         /*
         * BaseAdapter 没有addHeader和addFooter 方法
         */
        BaseAdapter<String> baseAdapter = new BaseAdapter<String>(this, R.layout.item, datalist()) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.item_tx, s);
            }
        };
        baseAdapter.setOnItemClickListener(this);


        /*
         * BaseHeadAdapter 可以Add无限head和foot  不用addHeader和addFooter  跟BaseAdapter一样
         */
        BaseHeadAdapter<String> baseHeadAdapter = new BaseHeadAdapter<String>(this, R.layout.item, datalist()) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.item_tx, s);
                TextView txtview = holder.getView(R.id.item_tx);
                txtview.setHeight(50 + (holder.getLayoutPosition() % 3) * 50);
            }

        };
        baseHeadAdapter.addHeader(footview);
        baseHeadAdapter.addHeader(headview);
        baseHeadAdapter.addFooter(footview);
        baseHeadAdapter.setOnItemClickListener(this);

        //recyclerview.setAdapter(baseAdapter);
        recyclerview.setAdapter(baseHeadAdapter);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        Toast.makeText(this, position + "项", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, int position) {
        return false;
    }
}
