package com.github.marcoscarceles.repoman.pages

import com.github.marcoscarceles.repoman.modules.OrganizationEntry
import geb.Page

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class OrganizationList extends Page {

    static url = "organization"

    static content = {
        organizations { moduleList OrganizationEntry, $('#list-organization tr') }
    }

}
