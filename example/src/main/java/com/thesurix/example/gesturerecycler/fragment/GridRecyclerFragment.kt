package com.thesurix.example.gesturerecycler.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thesurix.example.gesturerecycler.R
import com.thesurix.example.gesturerecycler.adapter.MonthsAdapter
import com.thesurix.example.gesturerecycler.model.MonthItem
import com.thesurix.gesturerecycler.DefaultItemClickListener
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureManager
import com.thesurix.gesturerecycler.RecyclerItemTouchListener

class GridRecyclerFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        val adapter = MonthsAdapter(R.layout.grid_item)
        adapter.data = months
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(RecyclerItemTouchListener(object : DefaultItemClickListener<MonthItem>() {

            override fun onItemClick(item: MonthItem, position: Int): Boolean {
                Snackbar.make(view, "Click event on the $position position", Snackbar.LENGTH_SHORT).show()
                return true
            }

            override fun onItemLongPress(item: MonthItem, position: Int) {
                Snackbar.make(view, "Long press event on the $position position", Snackbar.LENGTH_SHORT).show()
            }

            override fun onDoubleTap(item: MonthItem, position: Int): Boolean {
                Snackbar.make(view, "Double tap event on the $position position", Snackbar.LENGTH_SHORT).show()
                return true
            }
        }))

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapter.getItem(position)
                return if (item.type === MonthItem.MonthItemType.HEADER) manager.spanCount else 1
            }
        }

        gestureManager = GestureManager.Builder(recyclerView)
                .setSwipeEnabled(true)
                .setLongPressDragEnabled(true)
                .build()

        adapter.setDataChangeListener(object : GestureAdapter.OnDataChangeListener<MonthItem> {
            override fun onItemRemoved(item: MonthItem, position: Int) {
                Snackbar.make(view, "Month removed from position $position", Snackbar.LENGTH_SHORT).show()
            }

            override fun onItemReorder(item: MonthItem, fromPos: Int, toPos: Int) {
                Snackbar.make(view, "Month moved from position $fromPos to $toPos", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.recycler_drag_menu) {
            gestureManager?.isManualDragEnabled = !gestureManager!!.isManualDragEnabled
        }
        return super.onOptionsItemSelected(item)
    }
}
