package com.badzohugues.staticlbcapp.misc.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingDecoration(private val spacing: Int, private val isGrid: Boolean = false) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildLayoutPosition(view) == 0 && !isGrid) outRect.top = spacing
        outRect.left = spacing
        outRect.right = spacing
        outRect.bottom = spacing
    }
}