package com.mycelium.wallet.activity.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.mycelium.wallet.MbwManager
import com.mycelium.wallet.R
import com.mycelium.wallet.activity.settings.SettingsPreference.fioActive
import com.mycelium.wallet.activity.settings.SettingsPreference.fioEnabled
import com.mycelium.wallet.activity.settings.SettingsPreference.mediaFlowEnabled

class ExternalServiceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_external_service)
        val mbwManager = MbwManager.getInstance(requireActivity().application)
        setHasOptionsMenu(true)
        val actionBar = (requireActivity() as SettingsActivity).supportActionBar
        actionBar?.setTitle(R.string.external_service)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val preferenceCategory = findPreference<PreferenceCategory>("container")
        val buySellServices = mbwManager.environmentSettings.buySellServices
        for (buySellService in buySellServices) {
            if (!buySellService.showEnableInSettings()) {
                continue
            }
            preferenceCategory?.addPreference(CheckBoxPreference(requireActivity()).apply {
                title = resources.getString(R.string.settings_service_enabled,
                        resources.getString(buySellService.title))
                layoutResource = R.layout.preference_layout
                setSummary(buySellService.settingDescription)
                isChecked = buySellService.isEnabled(mbwManager)
                widgetLayoutResource = R.layout.preference_switch
                onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference ->
                    val p = preference as CheckBoxPreference
                    buySellService.setEnabled(mbwManager, p.isChecked)
                    true
                }
            })
        }
        if (fioActive) {
            preferenceCategory?.addPreference(CheckBoxPreference(requireActivity()).apply {
                setTitle(R.string.settings_fiopresale_title)
                setSummary(R.string.settings_fiopresale_summary)
                isChecked = fioEnabled
                layoutResource = R.layout.preference_layout
                widgetLayoutResource = R.layout.preference_switch
                onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference ->
                    val p = preference as CheckBoxPreference
                    fioEnabled = p.isChecked
                    true
                }
            })
        }
        findPreference<CheckBoxPreference>("media_flow")?.apply {
            isChecked = mediaFlowEnabled
            onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference ->
                val p = preference as CheckBoxPreference
                mediaFlowEnabled = p.isChecked
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            fragmentManager?.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}