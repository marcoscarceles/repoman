package com.github.marcoscarceles.repoman.modules

import geb.Module

/**
 * Created by @marcos-carceles on 28/02/2016.
 */
class CommitEntry extends Module {

    static content = {
        link { $('td:nth-child(1)') }
        sha { $('td[am-field~="sha"]').text() }
        date { $('td[am-field~="date"]').text() }
        message { $('td[am-field~="message"]').text() }
    }
}
