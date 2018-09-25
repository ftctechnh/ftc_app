package com.acmerobotics.roadrunner.gui
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Rectangle
import javax.swing.*


class ScrollPanelHost(private val scrollPanel: JPanel) : JPanel(BorderLayout()), Scrollable {
    init {
        add(scrollPanel)
    }

    override fun getPreferredScrollableViewportSize(): Dimension {
        val preferredSize = scrollPanel.preferredSize
        if (parent is JViewport) {
            preferredSize.width += (parent.parent as JScrollPane).verticalScrollBar
                    .preferredSize.width
        }
        return preferredSize
    }

    override fun getScrollableTracksViewportHeight(): Boolean {
        if (parent is JViewport) {
            val viewport = parent as JViewport
            return preferredSize.height < viewport.height
        }
        return false
    }

    override fun getScrollableTracksViewportWidth() = true

    override fun getScrollableBlockIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int {
        return if (orientation == SwingConstants.HORIZONTAL)
            Math.max(visibleRect.width * 9 / 10, 1)
        else
            Math.max(visibleRect.height * 9 / 10, 1)
    }

    override fun getScrollableUnitIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int {
        return if (orientation == SwingConstants.HORIZONTAL)
            Math.max(visibleRect.width / 10, 1)
        else
            Math.max(visibleRect.height / 10, 1)
    }
}