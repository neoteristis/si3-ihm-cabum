package com.example.ihm_cabum.ui.profile;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class ProfileViewModel extends ViewModel {

    private SharedPreferences mPreferences;
    private MutableLiveData<String> mFirstName;
    private MutableLiveData<String> mLastName;
    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mCity;
    private MutableLiveData<Set<String>> mTransports;
    private MutableLiveData<String> mLanguage;
    private MutableLiveData<String> mUnitOfMeasure;
    private MutableLiveData<Boolean> mNotification;

    public ProfileViewModel(Application application) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        mFirstName = new MutableLiveData<>(mPreferences.getString("first_name", "Raphael"));
        mLastName = new MutableLiveData<>(mPreferences.getString("last_name", "Smith"));
        mEmail = new MutableLiveData<>(mPreferences.getString("email", "raphael.smith@gmail.com"));
        mCity = new MutableLiveData<>(mPreferences.getString("city", "Valbonne"));
        mTransports = new MutableLiveData<>(mPreferences.getStringSet("transports", null));
        mLanguage = new MutableLiveData<>(mPreferences.getString("language", "English"));
        mUnitOfMeasure = new MutableLiveData<>(mPreferences.getString("unit_of_measure", "Meters"));
        mNotification = new MutableLiveData<>(mPreferences.getBoolean("notification", false));
    }

    public LiveData<String> getFirstName() {
        return mFirstName;
    }
    public void setFirstName(String firstName) {
        mFirstName.setValue(firstName);
        mPreferences.edit().putString("first_name", firstName).apply();
    }
    public LiveData<String> getLastName() {
        return mLastName;
    }
    public void setLastName(String lastName) {
        mLastName.setValue(lastName);
        mPreferences.edit().putString("last_name", lastName).apply();
    }
    public LiveData<String> getEmail() {
        return mEmail;
    }
    public void setEmail(String email) {
        mEmail.setValue(email);
        mPreferences.edit().putString("email", email).apply();
    }
    public LiveData<String> getCity() {
        return mCity;
    }
    public void setCity(String city) {
        mCity.setValue(city);
        mPreferences.edit().putString("city", city).apply();
    }

    public LiveData<Set<String>> getTransports() {
        return mTransports;
    }

    public void setTransports(Set<String> transportMulti) {
        mTransports.setValue(transportMulti);
        mPreferences.edit().putStringSet("transports", transportMulti).apply();
    }

    public LiveData<String> getLanguage() {
        return mLanguage;
    }
    public void setLanguage(String language) {
        mLanguage.setValue(language);
        mPreferences.edit().putString("language", language).apply();
    }
    public LiveData<String> getUnitOfMeasure() {
        return mUnitOfMeasure;
    }
    public void setUnitOfMeasure(String unit) {
        mUnitOfMeasure.setValue(unit);
        mPreferences.edit().putString("unit_of_measure", unit).apply();
    }
    public LiveData<Boolean> getNotification() {
        return mNotification;
    }
    public void setNotification(Boolean notification) {
        mNotification.setValue(notification);
        mPreferences.edit().putBoolean("notification", notification).apply();
    }
}

