package sk.alex_katona.sample1.flow.user_detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.android.synthetic.main.error_loading_layout.*
import kotlinx.android.synthetic.main.fragment_user_details.*
import sk.alex_katona.sample1.R
import sk.alex_katona.sample1.common.architecture.MviBaseFragment
import sk.alex_katona.sample1.helpers.loadAvatar
import sk.alex_katona.sample1.helpers.show
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailFragment :
    MviBaseFragment<UserDetailView, UserDetailViewState, UserDetailPresenter>(), UserDetailView {

    private val args: UserDetailFragmentArgs by navArgs()

    @Inject
    lateinit var presenter: UserDetailPresenter

    override fun initIntent(): Observable<Int> = Observable.just(args.userId)

    override fun getLayoutResId(): Int = R.layout.fragment_user_details

    override fun createPresenter(): UserDetailPresenter = presenter

    override fun init(view: View, savedInstanceState: Bundle?) {}

    override fun render(viewState: UserDetailViewState) {
        gr_error.show(viewState.isError)
        pb_loading.show(viewState.isLoading)
        viewState.userData?.let { userDetails ->
            loadAvatar(requireContext(), userDetails.avatarUrl, iv_user_avatar)
            tv_name.text = "${userDetails.firstName} ${userDetails.lastName}"
            tv_email.text = userDetails.email
        }

    }
}