package com.example.frogmitest.usecase

import com.example.frogmitest.data.FrogmiRepository
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(private val repository: FrogmiRepository) {

    suspend operator fun invoke(page: Int) = repository.getStores(page)


}