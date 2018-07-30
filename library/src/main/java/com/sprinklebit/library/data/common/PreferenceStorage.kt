package com.sprinklebit.library.data.common

import android.content.Context
import android.content.SharedPreferences

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 12/28/17
 */
class PreferenceStorage(context: Context, name: String) {

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

    private val preferenceSubject = PublishSubject.create<String>()

    private var preferences: SharedPreferences = context
            .getSharedPreferences(name, Context.MODE_PRIVATE)

    fun set(trigger: String, save: (SharedPreferences.Editor) -> Unit) {
        val edit = preferences.edit()
        save.invoke(edit)
        edit.apply()
        preferenceSubject.onNext(trigger)
    }

    fun <T> get(actionGet: (SharedPreferences) -> T): T {
        return actionGet.invoke(preferences)
    }

    fun update(trigger: String, save: (SharedPreferences.Editor) -> Unit): Completable {
        return Completable.fromAction {
            val edit = preferences.edit()
            save.invoke(edit)
            edit.apply()
            preferenceSubject.onNext(trigger)
        }
    }

    fun <T> observe(trigger: String, actionGet: (SharedPreferences) -> T): Observable<T> {
        return Observable.create<T> { emitter ->
            try {
                emitter.onNext(actionGet.invoke(preferences))
            } catch (e: Throwable) {
                emitter.onError(e)
            }

            emitter.onComplete()
        }.repeatWhen { preferenceSubject.filter { s -> s == trigger } }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

}
