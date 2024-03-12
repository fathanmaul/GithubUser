package dev.rushia.githubusers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.rushia.githubusers.data.response.UserResultItem
import dev.rushia.githubusers.databinding.FragmentFollowersBinding
import dev.rushia.githubusers.viewmodel.FollowsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val EXTRA_USER = "extra_user"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var username: String


    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FollowsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollow.layoutManager = layoutManager
        username = arguments?.getString(EXTRA_USER).toString()
        if (viewModel.listUser.value.isNullOrEmpty()) {
            viewModel.getFollowersUsers(username)
        }
        viewModel.listUser.observe(viewLifecycleOwner) {
            setFollowersData(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setFollowersData(users: ArrayList<UserResultItem>) {
        val adapter = FollowsAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFollow.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFollow.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USER, username)
                }
            }
    }
}