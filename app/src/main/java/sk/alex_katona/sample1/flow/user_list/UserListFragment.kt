package sk.alex_katona.sample1.flow.user_list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.error_loading_layout.*
import kotlinx.android.synthetic.main.fragment_user_list.*
import kotlinx.coroutines.launch
import sk.alex_katona.sample1.R
import sk.alex_katona.sample1.common.AppNavigator
import sk.alex_katona.sample1.common.AppScreens
import sk.alex_katona.sample1.common.architecture.MviBaseFragment
import sk.alex_katona.sample1.helpers.show
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment : MviBaseFragment<UserListView, UserListViewState, UserListPresenter>(),
    UserListView {

    @Inject
    lateinit var presenter: UserListPresenter

    @Inject
    lateinit var appNavigator: AppNavigator

    private lateinit var userAdapter: UserAdapter
    private lateinit var adapter: ConcatAdapter

    private val userClickSubject = PublishSubject.create<User>()

    override fun initIntent(): Observable<Unit> = Observable.just(Unit)

    override fun userClickIntent(): Observable<User> = userClickSubject

    override fun getLayoutResId(): Int = R.layout.fragment_user_list

    override fun createPresenter(): UserListPresenter = presenter

    @ExperimentalPagingApi
    override fun init(view: View, savedInstanceState: Bundle?) {
        userAdapter = UserAdapter(userClickSubject).apply {
            addLoadStateListener {
                pb_loading.show(it.refresh == LoadState.Loading && !swipe_container.isRefreshing)
                gr_error.show(it.refresh is LoadState.Error)
                rv_users.show(it.refresh !is LoadState.Error)
            }

            addDataRefreshListener {
                swipe_container?.isRefreshing = false
            }

        }
        adapter = userAdapter.withLoadStateFooter(LoadingAdapter())

        rv_users.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UserListFragment.adapter
        }

        swipe_container.setOnRefreshListener {
            userAdapter.refresh()
        }

        bt_retry.setOnClickListener {
            userAdapter.retry()
        }

        Glide.with(requireContext())
            .load("https://www.thebluediamondgallery.com/wooden-tile/images/error.jpg")
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            )
            .circleCrop()
            .into(iv_error)
    }

    private fun setUpPagingData(data: PagingData<User>) {
        lifecycleScope.launch {
            userAdapter.submitData(data)
        }
    }

    override fun render(viewState: UserListViewState) {
        setUpPagingData(viewState.data)
        viewState.moveNextWithUserId?.let {
            appNavigator.navigateFrom(AppScreens.List(it))
        }
    }
}