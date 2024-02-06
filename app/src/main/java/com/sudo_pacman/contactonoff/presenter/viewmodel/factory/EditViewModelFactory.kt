import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.ContactViewModelImpl
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.EditContactViewModelImpl

@Suppress("UNCHECKED_CAST")
class EditViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditContactViewModelImpl::class.java)) {
            return EditContactViewModelImpl(ContactRepositoryImpl.getInstance()) as T
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
