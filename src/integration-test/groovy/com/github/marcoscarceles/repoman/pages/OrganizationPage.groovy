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
        title == 'Show Organization'
    }

    static content = {
        name { $('[aria-labelledby="name-label"]').text() }
        repos { moduleList RepoEntry, $('[aria-labelledby="repos-label"] tbody tr') }
        sortByName { $('thead tr .sortable:nth-child(2)') }
        sortByPopularity { $('thead tr .sortable:nth-child(2)') }
    }
}
