package com.github.marcoscarceles.repoman.modules

import geb.Module

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class RepoEntry extends Module {

    static content = {
        link { $('td:nth-child(1) a') }
        name { link.text() }
        popularity { $('td:nth-child(2)').text() as int }
    }
}
