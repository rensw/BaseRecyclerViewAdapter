# BaseRecyclerViewAdapter
通用RecyclerViewAdapter  可以Add 多个headview和footview

        /*
         * BaseHeadAdapter 可以Add多个head和foot
         */
        BaseHeadAdapter<String> baseHeadAdapter = new BaseHeadAdapter<String>(this, R.layout.item, datalist()) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.item_tx, s);
            }
        };
        baseHeadAdapter.addHeader(footview);
        baseHeadAdapter.addHeader(headview);
        baseHeadAdapter.addFooter(footview);
        baseHeadAdapter.setOnItemClickListener(this);

        //recyclerview.setAdapter(baseAdapter);
        recyclerview.setAdapter(baseHeadAdapter);
        
  修改自 https://github.com/hongyangAndroid/baseAdapter 感谢
