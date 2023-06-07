package com.example.speeronlinetest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.speeronlinetest.R
import com.example.speeronlinetest.adapter.FollowingAdapter
import com.example.speeronlinetest.api.Resource
import com.example.speeronlinetest.data.Profile
import com.example.speeronlinetest.databinding.FragmentFollowersBinding
import com.example.speeronlinetest.databinding.ItemProfileBinding
import com.example.speeronlinetest.viewmodel.ProfileViewModel

//This fragment displays the follower or followings details based on the input argument.
//It also shows a dialog on profile details of each user

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding

    private val viewModel: ProfileViewModel by activityViewModels()

    private val adapter = FollowingAdapter()
    private var selectedProfileDialog: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode: String? = arguments?.getString("mode")

        binding.followersRecyclerView.adapter = adapter
        binding.followersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener { followers ->
            viewModel.fetchSelectedUserProfile(followers.login)
        }


        if (mode == "following") {
            // Fetch the following list using the viewModel
            setToolbarTitle("Followings")
            viewModel.fetchFollowingList()
        } else {
            // Fetch the followers list using the viewModel
            setToolbarTitle("Followers")
            viewModel.fetchFollowersList()
        }

        viewModel.followingList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Handle loading state
                }
                is Resource.Success -> {
                    val followingList = resource.data
                    if (followingList.isNotEmpty()) {
                        // Display the following list
                        binding.followersRecyclerView.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE
                        // Update the adapter with the following list
                        adapter.updateFollowingList(followingList)

                    } else {
                        // Show empty state when the following list is empty
                        binding.followersRecyclerView.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    val errorMessage = resource.message
                    // Handle error state
                }
            }
        })

        viewModel.followersList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {

                is Resource.Loading -> {
                    // Handle loading state
                }
                is Resource.Success -> {
                    val followersList = resource.data
                    if (followersList.isNotEmpty()) {
                        binding.followersRecyclerView.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE
                        adapter.updateFollowingList(followersList)

                    } else {
                        // Show empty state when the following list is empty
                        binding.followersRecyclerView.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    val errorMessage = resource.message
                    // Handle error state
                }
            }
        })

        viewModel.selectedProfile.observe(viewLifecycleOwner, Observer { selectedProfile ->
            if (selectedProfile != null) {
                showUserProfileDialog(selectedProfile)
            }
        })

    }
    private fun showUserProfileDialog(profile: Profile) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val binding = ItemProfileBinding.inflate(inflater)

        binding.profile = profile
        // Load the image using Glide
        Glide.with(binding.avatarImageView)
            .load(profile.avatar_url)
            .placeholder(R.drawable.ic_person)
            .into(binding.avatarImageView)

        dialogBuilder.setView(binding.root)
        dialogBuilder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        selectedProfileDialog = dialog
        dialog.show()
    }

    private fun setToolbarTitle(title: String) {
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            activity.supportActionBar?.title = title
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        selectedProfileDialog?.dismiss()
        selectedProfileDialog = null
        viewModel.clearSelectedProfile()
    }

}

