package com.wengabytes.movieviewer.components.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.wengabytes.movieviewer.R
import com.wengabytes.movieviewer.base.BaseFragment
import kotlinx.android.synthetic.main.f_details.*

class DetailsFragment : BaseFragment<DetailsFragmentVM>() {
    // START: Implement Required Methods

    override fun provideVM(): DetailsFragmentVM =
        ViewModelProviders.of(this, factory)[DetailsFragmentVM::class.java]

    // END  : Implement Required Methods

    // START: Callbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group_vis_controller.isVisible = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listenVM()
        listenViews()
    }

    // END  : Callbacks

    // START: Custom Methods

    private fun listenVM() {
        vm.ldLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                textview_message.isVisible = false
            }

            baseInterface?.onLoading(isLoading)
        })

        vm.ldError.observe(viewLifecycleOwner, Observer {
            it?.consume()?.let { message ->
                with(textview_message) {
                    isVisible = true

                    text = getString(
                        R.string.format_errmsg_retry,
                        message,
                        getString(R.string.errmsg_tap_retry)
                    )
                }
            }
        })

        vm.ldUIModel.observe(viewLifecycleOwner, Observer {
            group_vis_controller.isVisible = true

            with(it)
            {
                textview_name.text = name
                textview_genre.text = genre
                textview_advisory_rating.text = advisoryRating
                textview_duration.text = duration
                textview_release_date.text = releaseDate
                textview_synopsis.text = synopsis
                textview_casts.text = casts

                Glide.with(context!!)
                    .load(linkPosterLandscape)
                    .placeholder(
                        AppCompatResources.getDrawable(
                            context!!,
                            R.drawable.logo_placeholder_art
                        )
                    )
                    .skipMemoryCache(true)
                    .into(imageview_portrait_landscape)

                Glide.with(context!!)
                    .load(linkPoster)
                    .placeholder(
                        AppCompatResources.getDrawable(
                            context!!,
                            R.drawable.logo_placeholder_art
                        )
                    )
                    .skipMemoryCache(true)
                    .into(imageview_portrait)
            }
        })

        vm.ldNavigateToSeatmap.observe(viewLifecycleOwner, Observer {
            it?.consume()?.let {
                findNavController().navigate(R.id.action_detailsFragment_to_seatmapFragment)
            }
        })
    }

    private fun listenViews() {
        button_view_seat_map.setOnClickListener {
            vm.getMovieSchedule()
        }

        textview_message.setOnClickListener {
            vm.getMovieDetails()
        }
    }

    // END  : Custom Methods
}