
package id.rrdev.commons.ui.adapter

import id.rrdev.commons.model.dataAdditional.UrlTutorial
import id.rrdev.commons.model.dataIncome.Endorse

interface OnItemClicked {
    //data income
    fun onEventClick(data: Endorse) {}

    //data additional
    fun onEventClick(data: UrlTutorial) {}
}