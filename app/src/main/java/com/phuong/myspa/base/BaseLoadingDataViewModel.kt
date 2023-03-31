package com.phuong.myspa.base
import androidx.lifecycle.*
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus

abstract class BaseLoadingDataViewModel<T> : ViewModel() {
    protected val dataMutableLiveData = MutableLiveData<DataResponse<T>>(DataResponse.DataIdle())
    val dataLiveData: LiveData<DataResponse<T>> = dataMutableLiveData
    open val isEmpty: LiveData<Boolean> = Transformations.map(dataLiveData) {
        it.loadingStatus == LoadingStatus.Error
    }
    open val isLoading: LiveData<Boolean> = Transformations.map(dataLiveData) {
        it.loadingStatus == LoadingStatus.Loading
    }
    open val isLoadMore: LiveData<Boolean> = Transformations.map(dataLiveData) {
        it.loadingStatus == LoadingStatus.LoadingMore
    }
    protected fun <A, B> LiveData<A>.combine(other: LiveData<B>): PairLiveData<A, B> {
        return PairLiveData(this, other)
    }

    fun <A, B, C> LiveData<A>.combine(
        second: LiveData<B>,
        third: LiveData<C>,
    ): TripleLiveData<A, B, C> {
        return TripleLiveData(this, second, third)
    }

    inner class PairLiveData<A, B>(first: LiveData<A>, second: LiveData<B>) :
        MediatorLiveData<Pair<A?, B?>>() {
        init {
            addSource(first) { value = it to second.value }
            addSource(second) { value = first.value to it }
        }
    }

    inner class TripleLiveData<A, B, C>(
        first: LiveData<A>,
        second: LiveData<B>,
        third: LiveData<C>,
    ) :
        MediatorLiveData<Triple<A?, B?, C?>>() {
        init {
            addSource(first) { value = Triple(it, second.value, third.value) }
            addSource(second) { value = Triple(first.value, it, third.value) }
            addSource(third) { value = Triple(first.value, second.value, it) }
        }
    }
}