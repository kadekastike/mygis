package com.kadek.gis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kadek.gis.databinding.FragmentDialogBinding

class CustomDialogFragment (private val job: String,
private val pj : String,
private val created_at : String): DialogFragment() {

    private lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.jobResult.text = job
        binding.pjResult.text = pj
        binding.createdAtResult.text = created_at
        binding.okbtn.setOnClickListener {
            dialog?.dismiss()
        }
    }
}