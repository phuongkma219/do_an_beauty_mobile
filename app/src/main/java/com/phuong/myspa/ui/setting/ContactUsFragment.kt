package com.phuong.myspa.ui.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentContactUsBinding


class ContactUsFragment : AbsBaseFragment<FragmentContactUsBinding>() {
    override fun getLayout(): Int = R.layout.fragment_contact_us

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.cvEmail.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("admin@gmail.com.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Content")
            i.putExtra(Intent.EXTRA_TEXT, "Contact email")
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.cvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${"0987612345"}"))
            startActivity(intent)
        }
        binding.cvFb.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.facebook.com/admin.admin.77715869")
            startActivity(i)
        }
    }
}