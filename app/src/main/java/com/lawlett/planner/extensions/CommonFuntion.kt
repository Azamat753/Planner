package com.lawlett.planner.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.cdev.achievementview.AchievementView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.AchievementModel
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.ui.adapter.HabitAdapter
import java.util.*

 fun rewardAnAchievement(completeTask: Int,activity: Activity,achievementViewModel: AchievementViewModel,achievementView: AchievementView) {
    if (completeTask % 5 == 0) {
        var currentLevel: Int = activity.getCurrentLevel()!!
        currentLevel += 1
        val model = AchievementModel(level = currentLevel, id = 1)
        achievementViewModel.update(model)
        achievementView.show("Уровень повышен!", "Уровень $currentLevel")
    }
}

 fun checkDay(habitModel: HabitModel,habitViewModel: HabitViewModel) {
    val calendar = Calendar.getInstance()
    val currentDay = calendar[Calendar.DAY_OF_MONTH]
    val dayFromRoom: Int = habitModel.myDay
//        if (currentDay != dayFromRoom) { todo to on
    val today = (habitModel.currentDay + 1)
    val model = HabitModel(
        id = habitModel.id,
        myDay = currentDay,
        currentDay = today,
        title = habitModel.title,
        icon = habitModel.icon,
        allDays = habitModel.allDays
    )
    habitViewModel.update(model)
//        } else {
//            Toast.makeText(requireContext(), R.string.your_habit_is_done, Toast.LENGTH_SHORT).show()
//        }
}

 fun showHabitDayUpDialog(
    model: HabitModel,position: Int,context: Context,habitAdapter: HabitAdapter,
    habitViewModel: HabitViewModel,activity: Activity,achievementViewModel: AchievementViewModel,
    achievementView: AchievementView) {
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setMessage(context.getString(R.string.you_is_done_habit_today))
        .setCancelable(false)
        .setPositiveButton(context.getString(R.string.ofCourse)) { _, _ ->
            checkDay(model,habitViewModel)
            rewardAnAchievement(model.currentDay,activity,achievementViewModel,achievementView)
            habitAdapter.notifyItemChanged(position)
        }
        .setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
    val alert = dialogBuilder.create()
    alert.setTitle(context.getString(R.string.attention_alert))
    alert.show()
}
