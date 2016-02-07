package com.yalantis.phoenix.sample;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Map;

/**
 * Created by Oleksii Shliama.
 *
 * Modified by Haruki Hasegawa (h6ah4i).
 *   Advanced RecyclerView integration demo
 *   - Swipe left: remove the item
 *   - Long press: initiate dragging the item
 */
public class RecyclerViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;
    private RecyclerViewDragDropManager mDragDropManager;
    private RecyclerViewSwipeManager mSwipeManager;
    private SampleAdapter mAdapter;
    private RecyclerView.Adapter mWrapperAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new SampleAdapter();

        mDragDropManager = new RecyclerViewDragDropManager();

        mDragDropManager.setInitiateOnLongPress(true);
        mDragDropManager.setInitiateOnMove(false);

        mSwipeManager = new RecyclerViewSwipeManager();

        mWrapperAdapter = mDragDropManager.createWrappedAdapter(mAdapter);
        mWrapperAdapter = mSwipeManager.createWrappedAdapter(mWrapperAdapter);

        recyclerView.setAdapter(mWrapperAdapter);

        mDragDropManager.attachRecyclerView(recyclerView);
        mSwipeManager.attachRecyclerView(recyclerView);

        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performRefresh();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }

    private void performRefresh() {
        createSampleList(mSampleList);
        mAdapter.notifyDataSetChanged();
    }

    private class SampleAdapter
            extends RecyclerView.Adapter<SampleHolder>
            implements DraggableItemAdapter<SampleHolder>, SwipeableItemAdapter<SampleHolder> {

        public SampleAdapter() {
            setHasStableIds(true);
        }

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            Map<String, Integer> data = mSampleList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mSampleList.size();
        }

        @Override
        public long getItemId(int position) {
            Map<String, Integer> data = mSampleList.get(position);
            return data.get(KEY_ID);
        }

        // --- DraggableItemAdapter ---
        @Override
        public boolean onCheckCanStartDrag(SampleHolder holder, int position, int x, int y) {
            return true;
        }

        @Override
        public ItemDraggableRange onGetItemDraggableRange(SampleHolder holder, int position) {
            return null;
        }

        @Override
        public void onMoveItem(int fromPosition, int toPosition) {
            if (fromPosition != toPosition) {
                final Map<String, Integer> item = mSampleList.remove(fromPosition);
                mSampleList.add(toPosition, item);
            }
        }
        // --- DraggableItemAdapter ---

        // --- SwipeableItemAdapter ---
        @Override
        public SwipeResultAction onSwipeItem(SampleHolder holder, final int position, int result) {
            if (result == SwipeableItemConstants.RESULT_SWIPED_LEFT) {
                return new SwipeResultActionRemoveItem() {
                    @Override
                    protected void onPerformAction() {
                        mSampleList.remove(position);
                        notifyItemRemoved(position);
                    }
                };
            } else {
                return null;
            }
        }

        @Override
        public int onGetSwipeReactionType(SampleHolder holder, int position, int x, int y) {
            return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT;
        }

        @Override
        public void onSetSwipeBackground(SampleHolder holder, int position, int type) {

        }
        // --- SwipeableItemAdapter ---
    }

    private class SampleHolder extends AbstractDraggableSwipeableItemViewHolder {

        private View mRootView;
        private View mContainerView;
        private ImageView mImageViewIcon;

        private Map<String, Integer> mData;

        public SampleHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mContainerView = itemView.findViewById(R.id.container);
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
        }

        public void bindData(Map<String, Integer> data) {
            mData = data;

            mContainerView.setBackgroundResource(mData.get(KEY_COLOR));
            mImageViewIcon.setImageResource(mData.get(KEY_ICON));
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainerView;
        }
    }

}
