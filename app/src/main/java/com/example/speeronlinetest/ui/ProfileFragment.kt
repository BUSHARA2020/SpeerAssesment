package com.example.speeronlinetest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.githubdemo.api.ApiClient.apiService
import com.example.speeronlinetest.R
import com.example.speeronlinetest.data.Profile
import com.example.speeronlinetest.databinding.FragmentProfileBinding
import com.example.speeronlinetest.repository.GitHubRepositoryImpl
import com.example.speeronlinetest.viewmodel.ProfileViewModel
import com.example.speeronlinetest.viewmodel.ProfileViewModelFactory

/*
* This fragment capture the username and fetch the user details
* It also passes the argument follower/following FollowerFragment based on the click event
*/

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory(GitHubRepositoryImpl(apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fetchButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT)
                    .show()
            } else {
                fetchUserProfile(username)
            }
        }

        binding.resetButton.setOnClickListener {
            binding.usernameEditText.text.clear()
            resetProfile();
        }

        binding.followersLayout.setOnClickListener {
            navigateToFollowerFragment("followers")
        }

        binding.followingLayout.setOnClickListener {
            navigateToFollowerFragment("following")
        }


        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            if (profile != null) {
                displayProfile(profile)
            }
        })


        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            showErrorView("No matching profile for this user id")
        })

    }

    private fun navigateToFollowerFragment(mode: String) {
        val bundle = Bundle().apply {
            putString("mode", mode)
        }
        findNavController().navigate(R.id.action_profileFragment_to_followersFragment, bundle)
    }


    private fun fetchUserProfile(username: String) {
        viewModel.fetchUserProfile(username)
    }

    private fun displayProfile(profile: Profile) {
        binding.profileLayout.visibility = View.VISIBLE
        binding.notFoundTextView.visibility = View.GONE

        // Set the profile details in the UI
        Glide.with(this)
            .load(profile.avatar_url)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.avatarImageView)

        binding.usernameTextView.text = profile.login
        binding.nameTextView.text = profile.name
        binding.descriptionTextView.text = profile.bio
        binding.followersTextView.text = getString(R.string.count, profile.followers)
        binding.followingTextView.text = getString(R.string.count, profile.following)
    }

    private fun showNotFoundView() {
        binding.profileLayout.visibility = View.GONE
        binding.notFoundTextView.visibility = View.VISIBLE
    }

    private fun resetProfile() {
        binding.profileLayout.visibility = View.GONE
        binding.notFoundTextView.visibility = View.GONE
        viewModel.resetProfile()
    }

    private fun showErrorView(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }
}
