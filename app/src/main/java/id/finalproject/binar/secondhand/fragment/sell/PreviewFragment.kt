package id.finalproject.binar.secondhand.fragment.sell

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import id.finalproject.binar.secondhand.MainActivity
import id.finalproject.binar.secondhand.databinding.FragmentPreviewBinding
import id.finalproject.binar.secondhand.helper.SharedPreferences
import id.finalproject.binar.secondhand.model.network.Status
import id.finalproject.binar.secondhand.model.network.response.GetUserItem
import id.finalproject.binar.secondhand.repository.SellerAddProductRepository
import id.finalproject.binar.secondhand.repository.toRp
import id.finalproject.binar.secondhand.repository.viewModelsFactory
import id.finalproject.binar.secondhand.service.ApiClient
import id.finalproject.binar.secondhand.service.ApiService
import id.finalproject.binar.secondhand.viewmodel.SellerProductViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val apiService: ApiService by lazy { ApiClient.instance }
    private val sellerAddProductRepository: SellerAddProductRepository by lazy { SellerAddProductRepository(apiService) }
    private val sellerProductViewModel: SellerProductViewModel by viewModelsFactory { SellerProductViewModel(sellerAddProductRepository) }

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var accessToken: String
    private lateinit var body: PostProductRequestBody


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = SharedPreferences(requireContext())
        if(sharedPrefs.getLogin()){
            accessToken = sharedPrefs.getToken().toString()
            getProductData()
            toFormJualPage()
            postProduct()
        }
    }

    private fun toFormJualPage() {
        binding.btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun postProduct() {
        binding.btnTerbitkan2.setOnClickListener {
            observePost()
        }
    }

    private fun getProductData(){
        setFragmentResultListener("requestKey") { _, bundle ->
            val formBundle = bundle.getBundle("bundleKey")
            val name = formBundle?.getString("name").toString()
            val price = formBundle?.getString("price").toString()
            val description = formBundle?.getString("description").toString()
            val category = formBundle?.getString("category").toString()
            val location = formBundle?.getString("location").toString()
            val path = formBundle?.getString("path").toString()
            val imageFile = File(path)

            binding.apply {
                tvDeskripsi.text = description
                tvItemNama.text = name
                tvItemCategory.text = category
                tvItemHarga.text = price.toInt().toRp()
                vpImage.setImageURI(path.toUri())
            }
            addProduct(name,price,description,category,location,imageFile)
            observeSellerInfo()
        }
    }

    private fun addProduct(name: String, price: String, description: String, category: String, location: String,
                           imageFile: File
    ) {
        val namebody = name.toRequestBody("text/plain".toMediaType())
        val priceBody = price.toRequestBody("text/plain".toMediaType())
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val categoryBody = category.toRequestBody("text/plain".toMediaType())
        val locationBody = location.toRequestBody("text/plain".toMediaType())

        val imageBody = imageFile.asRequestBody("image/png".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)

        body = PostProductRequestBody(namebody, priceBody, descriptionBody, categoryBody, locationBody, image)
    }

    private fun observePost(
    ){
        sellerProductViewModel.postProduct(
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFzZGFzZEBnbWFpbC5jb20iLCJpYXQiOjE2NTY0OTgyMjh9.l25knICph9-8ZBanO08PHTMhzMr4kJGabGekEvx2Djw",
            accessToken,
            body.nameBody,
            body.descriptionBody,
            body.priceBody,
            body.categoryBody,
            body.locationBody,
            body.image
        ).observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    val bundle = Bundle()
                    bundle.putBoolean("addProduct", true)

                    val intent = Intent(this@PreviewFragment.requireContext(), MainActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent, bundle)
                    requireActivity().finish()
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun observeSellerInfo(){
        sellerProductViewModel.getUser(
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFzZGFzZEBnbWFpbC5jb20iLCJpYXQiOjE2NTY0OTgyMjh9.l25knICph9-8ZBanO08PHTMhzMr4kJGabGekEvx2Djw"
        accessToken
        ).observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> setSellerInfo(it1) }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setSellerInfo(sellerInfo: GetUserItem){
        Glide.with(this).load(sellerInfo.imageUrl).into(binding.ivSellerInfo)
        binding.tvSellerNama.text = sellerInfo.fullName
        binding.tvSellerCity.text = sellerInfo.city
    }
}