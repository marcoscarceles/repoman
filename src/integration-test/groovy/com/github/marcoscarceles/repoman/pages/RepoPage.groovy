package com.github.marcoscarceles.repoman.pages

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
        repoOwner { $('[aria-labelledby="owner-label"]').text() }
        name { $('[aria-labelledby="name-label"]').text() }
    }
}
