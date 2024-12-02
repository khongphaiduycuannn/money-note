package com.ndmq.moneynote.presentation.edit_category

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.data.source.in_memory.defaultIconResources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    var id: Long? = null

    val selectedColor = MutableLiveData<Int>()

    val selectedIcon = MutableLiveData(defaultIconResources[0])

    var categoryType = 1

    fun deleteCurrentCategory(doOnSuccess: () -> Unit) {
        id?.let { id ->
            executeTask(
                request = {
                    appRepository.deleteCategory(id)
                },
                onSuccess = {
                    doOnSuccess()
                }
            )
        }
    }

    fun saveCategory(title: String, doOnSuccess: () -> Unit) {
        val icon = selectedIcon.value
        val color = selectedColor.value
        if (icon == null || color == null) return

        val category =
            Category(icon, color, title, categoryType).apply { id = this@EditCategoryViewModel.id }
        if (id == null) {
            addCategory(category, doOnSuccess)
        } else {
            updateCategory(category, doOnSuccess)
        }
    }

    private fun addCategory(category: Category, doOnSuccess: () -> Unit) {
        executeTask(
            request = {
                appRepository.addCategory(category)
            },
            onSuccess = {
                doOnSuccess()
            }
        )
    }

    private fun updateCategory(category: Category, doOnSuccess: () -> Unit) {
        executeTask(
            request = {
                appRepository.updateCategory(category)
            },
            onSuccess = {
                doOnSuccess()
            }
        )
    }
}