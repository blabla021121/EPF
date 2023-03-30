package my.edu.tarc.epf.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.edu.tarc.epf.R
import my.edu.tarc.epf.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding : FragmentAboutBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.buttonWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            with(intent){
                setData(Uri.parse("http://kwsp.gov.my"))
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}