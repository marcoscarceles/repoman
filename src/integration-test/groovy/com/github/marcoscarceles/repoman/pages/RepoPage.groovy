package com.github.marcoscarceles.repoman.pages

import com.github.marcoscarceles.repoman.modules.CommitEntry
import geb.Page

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class RepoPage extends Page {

    static url = 'repo/show'

    static at = {
        title =~ / Repo | RepoMan$/
    }

    static content = {
        name { $('h1').text() }
        popularity { $('h2.popularity-value').text() as int }
        commits { moduleList CommitEntry, $('#show-repo tbody tr') }
    }
}
