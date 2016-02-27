package com.github.marcoscarceles.repoman.modules

import geb.Module

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class OrganizationEntry extends Module {

    static content = {
        avatar { $('td:nth-child(1) img') }
        name { $('td:nth-child(2)').text() }
    }
}
