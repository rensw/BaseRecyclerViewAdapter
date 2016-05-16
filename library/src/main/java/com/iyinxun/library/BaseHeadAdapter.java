package com.iyinxun.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * rsw
 * Add 多个 head foot
 * 2016/5/13
 */
public abstract class BaseHeadAdapter<T> extends BaseAdapter<T> {


    private static final int HEADER_VIEW_TYPE = -1000;
    private static final int FOOTER_VIEW_TYPE = -2000;

    private final List<View> mHeaders = new ArrayList<View>();
    private final List<View> mFooters = new ArrayList<View>();

    public BaseHeadAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }


    /**
     * 添加头部VIEW
     */
    public void addHeader(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null header!");
        }
        mHeaders.add(view);
    }

    /**
     * 添加底部VIEW
     */
    public void addFooter(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null footer!");
        }
        mFooters.add(view);
    }

    /**
     * 设置头部是否可见
     */
    public void setHeaderVisibility(boolean shouldShow) {
        for (View header : mHeaders) {
            header.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置底部是否可见
     */
    public void setFooterVisibility(boolean shouldShow) {
        for (View footer : mFooters) {
            footer.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @return 返回头部的size
     */
    public int getHeaderCount() {
        return mHeaders.size();
    }

    /**
     * @return 返回底部的size
     */
    public int getFooterCount() {
        return mFooters.size();
    }

    /**
     * 获取 headview
     */
    public View getHeader(int i) {
        return i < mHeaders.size() ? mHeaders.get(i) : null;
    }

    /**
     * 获取 Footview
     */
    public View getFooter(int i) {
        return i < mFooters.size() ? mFooters.get(i) : null;
    }

    private boolean isHeader(int viewType) {
        return viewType >= HEADER_VIEW_TYPE && viewType < (HEADER_VIEW_TYPE + mHeaders.size());
    }

    private boolean isFooter(int viewType) {
        return viewType >= FOOTER_VIEW_TYPE && viewType < (FOOTER_VIEW_TYPE + mFooters.size());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getLayoutPosition() < mHeaders.size()||holder.getLayoutPosition()>=getItemCount()-mFooters.size())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeader(getItemViewType(position))) {
                        return gridManager.getSpanCount();
                    } else if (isFooter(getItemViewType(position))) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (isHeader(viewType)) {
            int whichHeader = Math.abs(viewType - HEADER_VIEW_TYPE);
            View headerView = mHeaders.get(whichHeader);
            return ViewHolder.get(mContext, null, viewGroup, headerView, -1);
        } else if (isFooter(viewType)) {
            int whichFooter = Math.abs(viewType - FOOTER_VIEW_TYPE);
            View footerView = mFooters.get(whichFooter);
            return ViewHolder.get(mContext, null, viewGroup, footerView, -1);

        } else {
            ViewHolder viewHolder = ViewHolder.get(mContext, null, viewGroup, mLayoutId, -1);
            setListener(viewGroup, viewHolder, viewType);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (position < mHeaders.size()) {
            return;
        } else if (mHeaders.size() <= position && position < getItemCount() - mFooters.size()) {
            // This is a real position, not a header or footer. Bind it.
            super.onBindViewHolder(viewHolder, position - mHeaders.size());
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return mHeaders.size() + super.getItemCount() + mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaders.size()) {
            return HEADER_VIEW_TYPE + position;

        } else if (position < (mHeaders.size() + super.getItemCount())) {
            return super.getItemViewType(position - mHeaders.size());

        } else {
            return FOOTER_VIEW_TYPE + position - mHeaders.size() - super.getItemCount();
        }
    }

    /**
     * item 点击事件和长按事件
     *
     * @param parent
     * @param viewHolder
     * @param viewType
     */
    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    if (position < mHeaders.size()) {
                        return;
                    }
                    try {
                        mOnItemClickListener.onItemClick(parent, v, position - mHeaders.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    if (position < mHeaders.size()) {
                        return false;
                    }
                    return mOnItemClickListener.onItemLongClick(parent, v, position - mHeaders.size());
                }
                return false;
            }
        });
    }
}
