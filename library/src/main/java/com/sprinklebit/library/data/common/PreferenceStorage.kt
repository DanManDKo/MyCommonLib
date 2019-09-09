package com.sprinklebit.library.data.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 12/28/17
 */
@Suppress("unused")
class PreferenceStorage {

    constructor(context: Context, name: String) {
        this.preferences = context
                .getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    constructor(context: Context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object {

        @JvmStatic
        fun clear(context: Context, names: Array<String>) {
            for (name in names) {
                context.getSharedPreferences(name, Context.MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply()
            }
        }
    }

    private var preferenceSubject: PublishSubject<String> = PublishSubject.create<String>()

    private var preferences: SharedPreferences

    fun update(trigger: String, save: (SharedPreferences.Editor) -> Unit): Completable {
        return Completable.fromAction {
            val edit = preferences.edit()
            try {
                save.invoke(edit)
                edit.apply()
            } catch (e: Exception) {
                Timber.e(e)
                preferences.edit().clear().apply()
                save.invoke(edit)
                edit.apply()
            }
            preferenceSubject.onNext(trigger)
        }
    }

    fun <T> observe(trigger: String, actionGet: (SharedPreferences) -> T): Observable<T> {
        return Observable.create<T> { emitter ->
            try {
                emitter.onNext(actionGet.invoke(preferences))
            } catch (e: Throwable) {
                emitter.onError(e)
                preferences.edit().clear().apply()
                actionGet.invoke(preferences)
            }
            emitter.onComplete()
        }.repeatWhen { preferenceSubject.filter { s -> s == trigger } }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

}
