package com.silvertouch.demo

import com.silvertouch.demo.database.MyDatabase
import com.silvertouch.demo.repository.CategoryRepository
import com.silvertouch.demo.repository.ContactRepository
import com.silvertouch.demo.viewmodel.CategoryViewModel
import com.silvertouch.demo.viewmodel.ContactViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single {
        MyDatabase.getInstance(
            context = get()
        )
    }
    factory { get<MyDatabase>().daoCategory() }
    factory { get<MyDatabase>().daoContact() }
}

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { ContactRepository(get()) }
}

val uiModule = module {
    viewModel { CategoryViewModel(get()) }
    viewModel { ContactViewModel(get()) }
}