package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mTask = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = mTask

    private val mPriorityList = MutableLiveData<Array<String>>()
    val priorityList: LiveData<Array<String>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    fun load(taskId: Int) {
        mTaskRepository.load(taskId, object : APIListener<TaskModel> {
            override fun onSuccess(result: TaskModel, statusCode: Int) {
                mTask.value = result
            }

            override fun onFailure(message: String) {
                mTask.value = null
                mValidation.value = ValidationListener(message)
            }

        })
    }

    fun loadPriorities() {
        val priorities = arrayOf("Low", "Medium", "High", "Critical")
        mPriorityList.value = priorities
    }

    fun save(task: TaskModel) {
        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean, statusCode: Int) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuccess(result: Boolean, statusCode: Int) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }

            })
        }
    }

}