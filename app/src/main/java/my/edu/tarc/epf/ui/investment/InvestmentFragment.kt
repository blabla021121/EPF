package my.edu.tarc.epf.ui.investment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import my.edu.tarc.epf.R
import my.edu.tarc.epf.databinding.FragmentGalleryBinding
import my.edu.tarc.epf.databinding.FragmentInvestmentBinding
import java.util.*
import java.util.Calendar.*

class InvestmentFragment : Fragment() {

    private var _binding: FragmentInvestmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInvestmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDOB.setOnClickListener {
            val dateDialogFragment = DateDialogFragment { year, month, day ->
                onDateSelected(year, month, day)
            }
            dateDialogFragment.show(parentFragmentManager, "DatePicker")
        }
        binding.buttonCalculate.setOnClickListener {

        }
        binding.buttonReset.setOnClickListener {

        }
    }

    fun dayBetween(startDate: Calendar, endDate: Calendar): Long {
        val date = startDate.clone() as Calendar
        var daysBetween: Long = 0
        while (date.before(endDate)) {
            date.add(DAY_OF_MONTH, 1)
            daysBetween++
        }
        return daysBetween
    }

    private fun onDateSelected(year: Int, month: Int, day: Int) {
        binding.buttonDOB.text = String.format("%02d/%02d/%04d", day, month + 1, year)
        //set year, month, day to the module level variable
        with(calendar) {
            set(YEAR, year)
            set(MONTH, month)
            set(DAY_OF_MONTH, day)
        }
        val day = dayBetween(calendar, getInstance()).div(365)
        binding.textViewAge.text = day.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DateDialogFragment(val dateSetListener: (year: Int, month: Int, day: Int) -> Unit) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(YEAR)
            val month = c.get(MONTH)
            val day = c.get(DAY_OF_MONTH)
            return DatePickerDialog(requireContext(), this, year, month, day)
        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
            dateSetListener(year, month, day)
        }

    }
}