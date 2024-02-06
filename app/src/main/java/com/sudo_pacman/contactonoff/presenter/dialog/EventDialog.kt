package com.sudo_pacman.contactonoff.presenter.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sudo_pacman.contactonoff.R

class EventDialog : BottomSheetDialogFragment() {
    private var clickEditButtonListener: (() -> Unit)? = null
    private var clickDeleteButtonListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.event_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<LinearLayoutCompat>(R.id.lineEdit).setOnClickListener {
            clickEditButtonListener?.invoke()
            dismiss()
        }
        view.findViewById<LinearLayoutCompat>(R.id.lineDelete).setOnClickListener {
            clickDeleteButtonListener?.invoke()
            dismiss()
        }
    }

    fun setClickEditButtonListener(block: () -> Unit) {
        clickEditButtonListener = block
    }

    fun setClickDeleteButtonListener(block: () -> Unit) {
        clickDeleteButtonListener = block
    }
}