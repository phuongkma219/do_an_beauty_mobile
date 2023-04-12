package com.phuong.myspa.ui.reminder


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.data.api.model.reminder.RemindType
import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.utils.Resource
import com.phuong.myspa.domain.repository.usecase.ReminderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val remindersRepository: ReminderUseCases
) : ViewModel() {

    var reminderList = MutableLiveData<Resource<List<Reminder>>>()

    fun getAllReminders() =
        viewModelScope.launch {
            remindersRepository.getRemindersUseCase().collect{
                reminderList.postValue(it)
            }
        }

    fun createReminder(
        title: String,
        shopId:String,
        serviceId:String,
        serviceName:String,
        description: String,
        repeat: RemindType,
        time: Instant
    ) = viewModelScope.launch {
        remindersRepository.insertReminderUseCase(
            Reminder(
                -1,
                title,
                shopId,
                serviceId,
                serviceName,
                description,
                time,
                Instant.now().plus(1, ChronoUnit.HOURS),
                repeat
            )
        ).collect {
        }
    }

    fun deleteReminder(
        reminder: Reminder
    ) = viewModelScope.launch {
        remindersRepository.deleteReminderUseCase(
            reminder
        ).collect {
            if(it is Resource.Success){
                getAllReminders()
            }
        }
    }
}