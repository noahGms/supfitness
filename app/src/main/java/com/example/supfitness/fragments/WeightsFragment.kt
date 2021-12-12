package com.example.supfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supfitness.DBHelper
import com.example.supfitness.R
import com.example.supfitness.WeightsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class WeightsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button = view.findViewById<FloatingActionButton>(R.id.addWeightButton)
        button.setOnClickListener {
            handleDialog(view)
        }

        handleRecyclerView(view)
    }

    private fun handleDialog(view: View) {
        val dialogView = layoutInflater.inflate(R.layout.new_weight, null)
        val dialogBuilder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle("Add current weight")
        val dialog = dialogBuilder.show()

        val cancelButton = dialog.findViewById<Button>(R.id.new_weight_cancel_button)
        val confirmButton = dialog.findViewById<Button>(R.id.new_weight_confirm_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        confirmButton.setOnClickListener {
            val weightInput = dialog.findViewById<EditText>(R.id.new_weight_number_input)
            val db = activity?.let { DBHelper(it, null) }
            db?.addWeight(weightInput.text.toString())

            Snackbar.make(view, "New current weight added", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            dialog.dismiss()

            handleRecyclerView(view)
        }
    }

    fun handleRecyclerView(view: View) {
        val recyclerview = view.findViewById<RecyclerView>(R.id.weights_recycler_view)

        recyclerview.layoutManager = LinearLayoutManager(activity)

        val db = activity?.let { DBHelper(it, null) }
        val data = db?.getWeights()

        val adapter = data?.let { WeightsAdapter(it) }

        recyclerview.adapter = adapter
    }
}