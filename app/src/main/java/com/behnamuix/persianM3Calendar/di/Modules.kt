package com.behnamuix.persianM3Calendar.di

import com.behnamuix.persianM3Calendar.viewModel.PersianCalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { PersianCalendarViewModel() }
}


