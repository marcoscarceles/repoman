package com.github.marcoscarceles.repoman.pages

import com.github.marcoscarceles.repoman.modules.OrganizationEntry
import com.github.marcoscarceles.repoman.modules.RepoEntry
import geb.Page

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class OrganizationPage extends Page {

    static url = 'organization/show'

    static at = {
        title =~ / Organization | RepoMan$/
    }

    static content = {
        name { $('h1').text() }
        repos { moduleList RepoEntry, $('#show-organization tbody tr') }
        sortByName { $('#show-organization thead tr .sortable:nth-child(1)') }
        sortByPopularity { $('#show-organization thead tr .sortable:nth-child(2)') }
    }
}
