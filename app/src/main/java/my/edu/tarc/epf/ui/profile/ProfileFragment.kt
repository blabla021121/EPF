package my.edu.tarc.epf.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import my.edu.tarc.epf.R
import my.edu.tarc.epf.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream


class ProfileFragment : Fragment(), MenuProvider {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    //pick a profile pic file
    //implicit intent
    private val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        if(uri != null){
            binding.imageViewProfile.setImageURI(uri)
        }
    }

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        //let profile fragment manage the menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewProfile.setOnClickListener{
            //invoke on implicit intent
            getPhoto.launch("image/*")
        }

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(getString(R.string.name),
        getString(R.string.nav_header_title))

        val email = sharedPreferences.getString(getString(R.string.email),
        getString(R.string.nav_header_subtitle))

        binding.editTextName.setText(name)
        binding.editTextEmailAddress.setText(email)

        val image = readProfilePicture()
        if(image != null){
            binding.imageViewProfile.setImageBitmap(image)
        }else{
            binding.imageViewProfile.setImageResource(R.drawable.default_profile_pic)
        }
    }

    private fun saveProfilePicture(view: View) {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)
        val image = view as ImageView

        val bd = image.drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val outputStream: OutputStream

        try{
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }
    }

    private fun readProfilePicture(): Bitmap? {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)

        if(file.isFile){
            try{
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                return bitmap
            }catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId==R.id.action_save){
            //save profile info and picture
            saveProfilePicture(binding.imageViewProfile)

            //save user info to shared pref
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmailAddress.text.toString()
            with(sharedPreferences.edit()){
                putString(getString(R.string.name), name)
                putString(getString(R.string.email), email)
                apply()
            }

        }else if(menuItem.itemId==android.R.id.home){
            //handling the up button
            findNavController().navigateUp()
        }
        return true
    }
}