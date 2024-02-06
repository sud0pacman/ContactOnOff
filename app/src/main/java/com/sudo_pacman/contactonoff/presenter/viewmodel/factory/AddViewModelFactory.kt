import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.ContactAddViewModelImpl
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.ContactViewModelImpl

@Suppress("UNCHECKED_CAST")
class AddViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactAddViewModelImpl::class.java)) {
            return ContactAddViewModelImpl(ContactRepositoryImpl.getInstance()) as T
        } else {
            throw IllegalArgumentException("Required ContactViewModelImpl")
        }
    }

    /*
    @Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(ContactViewModelImpl::class.java)) {
            return ContactViewModelImpl(ContactRepositoryImpl.getInstance()) as T
        } else throw IllegalArgumentException("Required MainViewModelImpl")
    }
}
    * */
}
