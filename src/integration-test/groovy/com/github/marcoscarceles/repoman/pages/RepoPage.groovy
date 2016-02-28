package com.github.marcoscarceles.repoman.pages

import com.github.marcoscarceles.repoman.modules.CommitEntry
import geb.Page

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class RepoPage extends Page {

    static url = 'repo/show'

    static at = {
        title == 'Show Repo'
    }

    static content = {
        name { $('dd.name').text() }
        popularity { $('dd.popularity').text() }
        commits { moduleList CommitEntry, $('dd.commits tbody tr') }
    }
}
