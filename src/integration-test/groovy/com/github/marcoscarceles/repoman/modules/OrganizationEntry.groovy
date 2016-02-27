package com.github.marcoscarceles.repoman.modules

import geb.Module

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class OrganizationEntry extends Module {

    static content = {
        name { $('td.nth-child(0)') }
        url { $('td.nth-child(1)') }
        avatar { $('td.nth-child(2)') }
        repos { $('td.nth-child(3)') }
    }
}
