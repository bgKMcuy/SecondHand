package id.finalproject.binar.secondhand.fragment.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.finalproject.binar.secondhand.model.network.Resource
import id.finalproject.binar.secondhand.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class ProfileViewModel(private val userRepo: UserRepo): ViewModel() {
    fun getUser(access_token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(
                Resource.success(userRepo.getUser(access_token)))
        } catch (e: Exception) {
            emit(
                (e as? HttpException)!!.response()?.errorBody()?.string()?.let {
                    Resource.error(
                        data = null,
                        message = it
                    )
                }
            )
        }
    }
}