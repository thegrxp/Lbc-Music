package com.ainrom.lbcmusic.utils

import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import com.ainrom.lbcmusic.R
import com.ainrom.lbcmusic.di.Injector
import com.google.android.material.dialog.MaterialAlertDialogBuilder

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().addToBackStack(null).commit()
}

inline fun FragmentManager.inTransactionNoBackStack(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun <F> AppCompatActivity.replaceFragment(fragment: F, id: Int) where F : Fragment {
    supportFragmentManager.inTransactionNoBackStack {
        replace(id, fragment)
    }
}

fun <F> FragmentManager.addFragment(fragment: F, id: Int, tag: String? = null) where F : Fragment {
    val exitFade = Fade()
    exitFade.duration = ANIM_DURATION
    findFragmentById(id)?.exitTransition = exitFade

    fragment.enterTransition = TransitionInflater
        .from(Injector.get().context())
        .inflateTransition(R.transition.trans_bottom)

    inTransaction {
        add(id, fragment, tag)
    }
}

fun <F> FragmentManager.removeFragment(fragment: F, id: Int) where F : Fragment {
    val exitFade = Fade()
    exitFade.duration = ANIM_DURATION
    findFragmentById(id)?.exitTransition = TransitionInflater
        .from(Injector.get().context())
        .inflateTransition(R.transition.trans_bottom)

    inTransaction {
        remove(fragment)
    }
}

fun String.toast() {
    Toast.makeText(Injector.get().context(), this, Toast.LENGTH_SHORT).show()
}

fun View.showAlertDialog(
    messageTextRes: Int,
    actionClick: (dialogInterface: DialogInterface) -> Unit = {}
) {
    MaterialAlertDialogBuilder(this.context).apply {
        setCancelable(true)
        setMessage(messageTextRes)
        setNegativeButton(resources.getString(R.string.alert_dialog_cancel)) { dialog, _ -> dialog.dismiss() }
        setPositiveButton(resources.getString(R.string.alert_dialog_delete)) { dialog, _ -> actionClick(dialog) }
        show()
    }
}
