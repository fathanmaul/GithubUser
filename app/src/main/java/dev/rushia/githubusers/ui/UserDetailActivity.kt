package dev.rushia.githubusers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.rushia.githubusers.R
import dev.rushia.githubusers.data.response.UserDetailResult
import dev.rushia.githubusers.databinding.ActivityUserDetailBinding
import dev.rushia.githubusers.viewmodel.DetailViewModel

class UserDetailActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2,
        )
    }

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        username = intent.getStringExtra(EXTRA_USER).toString()

        if (viewModel.dataUser.value == null) {
            viewModel.getDetailUser(username.toString())
        }

        viewModel.dataUser.observe(this) {
            setDetailData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fab.setOnClickListener {
            val urlGithub = "https://github.com/$username"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(urlGithub)
            startActivity(intent)
        }
        viewPager()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar) // Id ke Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun viewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username = username.toString())
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position > 0 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                    viewPager.layoutParams.height = viewPager.getChildAt(0).height
                }
            }
        })
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailData(detailUser: UserDetailResult) {
        binding.tvName.text = detailUser.name
        binding.tvUsername.text = detailUser.login
        binding.tvRepo.text = detailUser.publicRepos.toString()
        binding.tvFollowers.text = detailUser.followers?.let { changeCounterValue(it) }
        binding.tvFollowing.text = detailUser.following?.let { changeCounterValue(it) }
        if (!this.isFinishing) {
            Glide.with(this).load(detailUser.avatarUrl).into(binding.ivAvatar)
        }
        binding.toolbar.title = detailUser.name
    }

    private fun changeCounterValue(value: Int): String {
        return if (value >= 1000) {
            val newValue = value.toDouble() / 1000.0
            String.format("%.1f", newValue) + "K"
        } else {
            value.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
            binding.appBarLayout.visibility = View.GONE
            binding.fab.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            binding.appBarLayout.visibility = View.VISIBLE
            binding.fab.visibility = View.VISIBLE
        }
    }
}