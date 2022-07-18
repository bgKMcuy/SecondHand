package id.finalproject.binar.secondhand.fragment.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.finalproject.binar.secondhand.R
import id.finalproject.binar.secondhand.databinding.FragmentDaftarJualBinding
import id.finalproject.binar.secondhand.repository.SellerAddProductRepository
import id.finalproject.binar.secondhand.repository.viewModelsFactory
import id.finalproject.binar.secondhand.service.ApiClient
import id.finalproject.binar.secondhand.service.ApiService
import id.finalproject.binar.secondhand.viewmodel.SellerProductViewModel

class DaftarJualFragment : Fragment() {

    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!

    private val apiService: ApiService by lazy { ApiClient.instance }
    private val sellerAddProductRepository: SellerAddProductRepository by lazy { SellerAddProductRepository(apiService) }
    private val sellerProductViewModel: SellerProductViewModel by viewModelsFactory { SellerProductViewModel(sellerAddProductRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toProfile(){
        binding.llSellerprofile.setOnClickListener {
//            findNavController().navigate(R.id.action)
        }
    }


}