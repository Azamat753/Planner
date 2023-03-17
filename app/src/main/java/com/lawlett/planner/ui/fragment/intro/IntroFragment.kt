package com.lawlett.planner.ui.fragment.intro

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.databinding.FragmentIntroBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getLanguageList
import com.lawlett.planner.ui.adapter.LanguageAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import su.levenetc.android.textsurface.Text
import su.levenetc.android.textsurface.TextBuilder
import su.levenetc.android.textsurface.TextSurface
import su.levenetc.android.textsurface.animations.*
import su.levenetc.android.textsurface.contants.Align
import su.levenetc.android.textsurface.contants.Pivot
import su.levenetc.android.textsurface.contants.Side


class IntroFragment : BaseFragment<FragmentIntroBinding>(FragmentIntroBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<LanguageModel> {

    var isShowed = false
    var isLanguageSelected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        isLanguageSelected = BooleanPreference.getInstance(requireContext())
            ?.getBooleanData(Constants.LANGUAGE_SELECTED)!!

        if (!isLanguageSelected) {
            languageDialogShow()
        }
        launchTextSurface()
        openProgressFragment()
    }

    private fun languageDialogShow() {
        val adapter = LanguageAdapter()
        adapter.listener = this
        adapter.setData(getLanguageList())
        val dialog = requireContext().getDialog(R.layout.language_dialog)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.adapter = adapter
        dialog.show()
    }

    private fun launchTextSurface() {
        if (isLanguageSelected && BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.SPLASH_SCREEN) == false
        ) {
            binding.textSurface.postDelayed({ showTS() }, 1000)
            Handler().postDelayed({
                binding.textSurface.isClickable = true
                isShowed = true
            }, 15000)
        }
    }

    private fun openProgressFragment() {
        binding.textSurface.setOnClickListener {
            if (isShowed) {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                findNavController().navigate(R.id.category_fragment)
                BooleanPreference.getInstance(requireContext())
                    ?.saveBooleanData(Constants.SPLASH_SCREEN, true)
            }
        }
    }

    private fun showTS() {
        binding.textSurface.reset()
        playFirst(binding.textSurface)
    }

    override fun onClick(model: LanguageModel, position: Int) {
        requireActivity().changeLanguage(position)
        BooleanPreference.getInstance(requireContext())
            ?.saveBooleanData(Constants.LANGUAGE_SELECTED, true)
    }

    private fun playFirst(textSurface: TextSurface) {
        val animationSpeed = 300
        val textHello: Text = TextBuilder
            .create(getString(R.string.you_are_hello))
            .setSize(30F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.SURFACE_CENTER).build()
        val toolsFor: Text = TextBuilder
            .create(getString(R.string.tool))
            .setSize(30F)
            .setAlpha(0)
            .setColor(Color.RED)
            .setPosition(Align.BOTTOM_OF, textHello).build()
        val forReachAim: Text = TextBuilder
            .create(getString(R.string.got_you_aim))
            .setSize(26F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, toolsFor).build()
        val plannerText: Text = TextBuilder
            .create("Planner")
            .setSize(64F)
            .setAlpha(0)
            .setColor(Color.RED)
            .setPosition(Align.BOTTOM_OF, forReachAim).build()
        val plusText: Text = TextBuilder
            .create("+")
            .setSize(74F)
            .setAlpha(0)
            .setColor(Color.RED)
            .setPosition(Align.RIGHT_OF, plannerText).build()

        val whatICanDo: Text = TextBuilder
            .create(getString(R.string.what_can_i_do))
            .setSize(30F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, plusText).build()

        val createTaskText: Text = TextBuilder
            .create(getString(R.string.create_notes))
            .setSize(30F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, whatICanDo).build()
        val habitText: Text = TextBuilder
            .create(getString(R.string.habit))
            .setSize(26F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, createTaskText).build()
        val followFinance: Text = TextBuilder
            .create(getString(R.string.follow_finance))
            .setSize(26F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, habitText).build()
        val keepFocus: Text = TextBuilder
            .create(getString(R.string.keep_focus))
            .setSize(22F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, followFinance).build()
        val followTimeTable: Text = TextBuilder
            .create(getString(R.string.keep_schedule))
            .setSize(25F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, keepFocus).build()
        val writeStandUp: Text = TextBuilder
            .create(getString(R.string.create_standup))
            .setSize(22F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, followTimeTable).build()

        val newPositionUnderWriteStandUp: Text = TextBuilder
            .create("                        \n \n \n \n \n            ")
            .setSize(22F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, writeStandUp).build()

        val and: Text = TextBuilder
            .create(getString(R.string.also))
            .setSize(30F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.RIGHT_OF, newPositionUnderWriteStandUp).build()
        val createEvent: Text = TextBuilder
            .create(getString(R.string.to_create_event))
            .setSize(28F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, and).build()

        val keepIdea: Text = TextBuilder
            .create(getString(R.string.fix_idea))
            .setSize(22F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, createEvent).build()

        val keepDream: Text = TextBuilder
            .create(getString(R.string.dream_record))
            .setSize(22F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, keepIdea).build()

        val letsStart: Text = TextBuilder
            .create(getString(R.string.lets_start))
            .setSize(34F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, keepDream).build()

        val clickOnScreen: Text = TextBuilder
            .create(getString(R.string.click_by_screen))
            .setSize(26F)
            .setAlpha(0)
            .setColor(Color.WHITE)
            .setPosition(Align.BOTTOM_OF, letsStart).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            textSurface.play(
                Sequential(
                    ShapeReveal.create(textHello, animationSpeed, SideCut.show(Side.LEFT), false),
                    Parallel(
                        ShapeReveal.create(
                            textHello,
                            animationSpeed,
                            SideCut.hide(Side.LEFT),
                            false
                        ),
                        Sequential(
                            Delay.duration(300), ShapeReveal.create(
                                textHello, animationSpeed, SideCut.show(Side.LEFT),
                                false
                            )
                        )
                    ),
                    Sequential(
                        TransSurface(animationSpeed, toolsFor, Pivot.CENTER),
                        ShapeReveal.create(toolsFor, animationSpeed, SideCut.show(Side.LEFT), false)
                    ),
                    Delay.duration(animationSpeed),

                    Parallel(
                        TransSurface(
                            animationSpeed,
                            forReachAim,
                            Pivot.CENTER
                        ),
                        ShapeReveal.create(forReachAim, 1300, SideCut.show(Side.LEFT), false)
                    ),
                    Delay.duration(animationSpeed),
                    Parallel(
                        TransSurface.toCenter(plannerText, animationSpeed),
                        Rotate3D.showFromSide(plannerText, 750, Pivot.TOP)
                    ),
                    Delay.duration(animationSpeed),
                    Parallel(
                        TransSurface(animationSpeed, plusText, Pivot.RIGHT),
                        ShapeReveal.create(plusText, 1300, SideCut.show(Side.RIGHT), false)
                    ),
                    Delay.duration(animationSpeed),
                    Parallel(
                        TransSurface(1500, whatICanDo, Pivot.RIGHT),
                        ShapeReveal.create(whatICanDo, 1300, SideCut.show(Side.LEFT), false),
                        Rotate3D.showFromSide(whatICanDo, 1750, Pivot.CENTER)
                    ),
                    Sequential(
                        TransSurface(animationSpeed, createTaskText, Pivot.CENTER),
                        ShapeReveal.create(
                            createTaskText,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),

                    Sequential(
                        TransSurface(animationSpeed, habitText, Pivot.CENTER),
                        ShapeReveal.create(
                            habitText,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface(
                            animationSpeed,
                            followFinance,
                            Pivot.CENTER
                        ),
                        ShapeReveal.create(
                            followFinance,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface.toCenter(keepFocus, animationSpeed),
                        Rotate3D.showFromSide(keepFocus, 750, Pivot.CENTER)
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface(animationSpeed, followTimeTable, Pivot.CENTER),
                        ShapeReveal.create(
                            followTimeTable,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface(animationSpeed, followFinance, Pivot.CENTER),
                        ShapeReveal.create(
                            writeStandUp,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),
                    Delay.duration(animationSpeed),

                    Sequential(
                        TransSurface(animationSpeed, and, Pivot.CENTER),
                        ShapeReveal.create(and, animationSpeed, SideCut.show(Side.LEFT), false)
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface(
                            animationSpeed,
                            createEvent,
                            Pivot.CENTER
                        ),
                        ShapeReveal.create(
                            createEvent,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),

                    Sequential(
                        TransSurface(animationSpeed, keepIdea, Pivot.CENTER),
                        ShapeReveal.create(keepIdea, animationSpeed, SideCut.show(Side.LEFT), false)
                    ),
                    Sequential(
                        TransSurface(animationSpeed, keepDream, Pivot.CENTER),
                        ShapeReveal.create(
                            keepDream,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),

                    Sequential(
                        TransSurface(700, letsStart, Pivot.CENTER),
                        ShapeReveal.create(
                            letsStart,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),
                    Delay.duration(animationSpeed),
                    Sequential(
                        TransSurface(animationSpeed, clickOnScreen, Pivot.CENTER),
                        ShapeReveal.create(
                            clickOnScreen,
                            animationSpeed,
                            SideCut.show(Side.LEFT),
                            false
                        )
                    ),

                    Alpha.hide(textHello, 1000),
                    Alpha.hide(toolsFor, 1000),
                    Alpha.hide(forReachAim, 1000),
                    Alpha.hide(plannerText, 100),
                    Alpha.hide(plusText, 1000),
                    Alpha.hide(
                        createTaskText, 1000
                    ),
                    Alpha.hide(habitText, 1000),
                    Alpha.hide(followFinance, 1000),
                    Alpha.hide(keepFocus, 1000),
                    Alpha.hide(followTimeTable, 1000),
                    Alpha.hide(writeStandUp, 1000),
                    Alpha.hide(and, 1000),
                    Alpha.hide(createEvent, 1000),
                    Alpha.hide(keepIdea, 1000),
                    Alpha.hide(keepDream, 1000),
                    Alpha.hide(letsStart, 1000),
                    Alpha.hide(clickOnScreen, 100000),
                )
            )
        }
    }
}
